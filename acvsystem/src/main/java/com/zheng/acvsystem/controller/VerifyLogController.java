package com.zheng.acvsystem.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.zheng.acvsystem.entity.Certificate;
import com.zheng.acvsystem.entity.Result;
import com.zheng.acvsystem.entity.VerifyLog;
import com.zheng.acvsystem.service.VerifyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodecException;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.v3.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.v3.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;
import org.fisco.bcos.sdk.v3.transaction.model.exception.TransactionBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器  验证日志
 * </p>
 *
 * @author zzw
 * @since 2026-01-15
 */
@Api(tags = "验证日志控制器")
@RestController
@RequestMapping("/verifyLog")
public class VerifyLogController {
    public static final String VERIFY = "verify";
    //导入服务层
    @Autowired
    private VerifyLogService verifyLogService;

    //定义配置文件路径
    public static final String CONFIG_FILE = "src/main/resources/conf/config.toml";
    public static final String ABI_CONFIGPATH = "src/main/resources/abi/";
    public static final String BIN_CONFIGPATH = "src/main/resources/bin/";
    public static final String CONTRACTADDRESS = "0xc8ead4b26b2c6ac14c9fd90d9684c9bc2cc40085";

    //查询用户信息
    @ApiOperation("证书验证--区块链")
    @PostMapping("/getVerifyLog")
    public Result getVerifyLog(@RequestBody Certificate certificate, @RequestParam Long companyName) throws ContractException, TransactionBaseException, ContractCodecException, IOException {
        //查询数据库 确定有在进行验证的证书
        Certificate CertNo = Db.lambdaQuery(Certificate.class)
                .eq(Certificate::getCertNo, certificate.getCertNo())
                .eq(Certificate::getFileHash, certificate.getFileHash())
                .eq(Certificate::getCollegeId, certificate.getCollegeId())
                .one();
        if (CertNo == null || CertNo.getStatus() == 0) {
            VerifyLog verifyLog = new VerifyLog();
            verifyLog.setResult("FAIL");
            verifyLog.setCompanyId(companyName);
            verifyLog.setCertId(CertNo.getId());
            verifyLogService.save(verifyLog);
            return Result.error("该证书不存在与数据库或该证书已失效");
        }
        // 初始化BcosSDK对象
        BcosSDK sdk = BcosSDK.build(CONFIG_FILE);
        // 获取Client对象，此处传入的群组名 group0
        Client client = sdk.getClient("group0");
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().getCryptoKeyPair();
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, ABI_CONFIGPATH, BIN_CONFIGPATH);
        // 部署HelloWorld合约。第一个参数为合约名称，第二个参数为合约构造函数的列表，是List<Object>类型。
        TransactionResponse response = transactionProcessor.deployByContractLoader("CertHashRegistry", new ArrayList<>());
        // 创建调用交易函数的参数，传入参数
        List<Object> params = new ArrayList<>();
        params.add(certificate.getCertNo());
        params.add(certificate.getFileHash());
        params.add(certificate.getCollegeId());
        TransactionResponse transactionResponse = transactionProcessor.
                sendTransactionAndGetResponseByContractLoader("CertHashRegistry", CONTRACTADDRESS, VERIFY, params);
        // 从 TransactionResponse 中获取交易回执
        TransactionReceipt receipt = transactionResponse.getTransactionReceipt();
        if (receipt == null) {
            VerifyLog verifyLog = new VerifyLog();
            verifyLog.setResult("FAIL");
            verifyLog.setCompanyId(companyName);
            verifyLog.setCertId(CertNo.getId());
            verifyLogService.save(verifyLog);
            return Result.error("该证书不存在或该证书已失效");
        }
        //当通过验证时，更新数据库留存记录
        VerifyLog verifyLog = new VerifyLog();
        verifyLog.setResult("PASS");
        verifyLog.setCompanyId(companyName);
        verifyLog.setCertId(CertNo.getId());
        verifyLogService.save(verifyLog);
        return Result.success(verifyLog);
    }

    @ApiOperation("查询证书--数据库")
    @PostMapping("/getVerifyLogSql")
    public Result getVerifyLogSql(@RequestBody Certificate certificate, @RequestParam Long companyName) {
        //根据证书编号查询Sql并记录
        String fileHash = certificate.getFileHash();
        Certificate selectCertNo = Db.lambdaQuery(Certificate.class).eq(Certificate::getFileHash, fileHash).one();
        //创建一个验证日志对象
        VerifyLog verifyLog = new VerifyLog();
        verifyLog.setCompanyId(companyName);
        verifyLog.setCertId(selectCertNo.getId());
        verifyLog.setVerifyTime(LocalDateTime.now());
        //判断是否存在该证书
        if (selectCertNo == null || selectCertNo.getStatus() != 1) {
            verifyLog.setResult("FAIL");
            verifyLogService.save(verifyLog);
            return Result.error("该证书不存在与数据库或该证书已失效");
        }
            verifyLog.setResult("PASS");
            verifyLogService.save(verifyLog);
            return Result.success("验证结果：" + verifyLog.getResult());
       /*   查询结束保存结果并返回
            无论查询是否成功都返回Result<success>
        */
    }


}
