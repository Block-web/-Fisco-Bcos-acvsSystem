package com.zheng.acvsystem.controller;

import com.zheng.acvsystem.entity.Certificate;
import com.zheng.acvsystem.entity.Result;
import com.zheng.acvsystem.service.CertificateService;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodecException;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.v3.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.v3.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.v3.transaction.model.exception.TransactionBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 前端控制器   证书控制器
 * </p>
 *
 * @author zzw
 * @since 2026-01-15
 */
@RestController
@RequestMapping("/certificate")
public class CertificateController {
    @Autowired  //导入服务层
    private CertificateService certificateService;

    //定义配置文件路径
    public static final String CONFIG_FILE = "src/main/resources/conf/config.toml";
    public static final String ABI_CONFIGPATH = "src/main/resources/abi/";
    public static final String BIN_CONFIGPATH = "src/main/resources/bin/";
    public static final String CONTRACTADDRESS = "0xc8ead4b26b2c6ac14c9fd90d9684c9bc2cc40085";


    //添加证书
    @PostMapping("/issue")
    public Result issue(@RequestBody @Validated Certificate certificate) throws TransactionBaseException, ContractCodecException, IOException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = LocalDate.now().format(dateFormatter);
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        String certNo = "C" + dateStr + uuidPart;
        // 初始化BcosSDK对象
        BcosSDK sdk = BcosSDK.build(CONFIG_FILE);
        // 获取Client对象，此处传入的群组名 group0
        Client client = sdk.getClient("group0");
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().getCryptoKeyPair();
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, ABI_CONFIGPATH, BIN_CONFIGPATH);
        // 部署HelloWorld合约。第一个参数为合约名称，第二个参数为合约构造函数的列表，是List<Object>类型。
        TransactionResponse response = transactionProcessor.deployByContractLoader("CertHashRegistry", new ArrayList<>());
        // 创建调用交易函数的参数，此处为传入一个参数
        List<Object> params = new ArrayList<>();
        //certNo
        params.add(certNo);
        //fileHash
        params.add(certificate.getFileHash());
        //collegeId
        params.add(certificate.getCollegeId());
        // 调用HelloWorld合约，合约地址为helloWorldAddress， 调用函数名为『set』，函数参数类型为params
        TransactionResponse transactionResponse = transactionProcessor.
                sendTransactionAndGetResponseByContractLoader("CertHashRegistry", CONTRACTADDRESS, "issue", params);
        // 从 TransactionResponse 中获取交易回执
        TransactionReceipt receipt = transactionResponse.getTransactionReceipt();
        // 判断交易是否成功

        if (receipt != null && receipt.getStatus() == 0) {
            // 交易成功
            String txHash = receipt.getTransactionHash();
            // 处理成功逻辑
            //1.将证书信息保存到数据库  先将数据存入对象certificate 在通过saveOrUpdate方法保存到数据库
            certificate.setCertNo(certNo);
            certificate.setTxHash(txHash);
            certificateService.saveOrUpdate(certificate);
            return Result.success("证书颁发成功");

        } else {
            return Result.error(receipt.getMessage() != null ? receipt.getMessage() : "证书颁发失败：区块链交易异常");
        }
    }

    //根据学生姓名获取证书信息中的url地址值
    @GetMapping("get-certificate")
    public Result getCertificateByStudentName(@RequestParam @NotNull String studentName) {
        Certificate certificate = certificateService.lambdaQuery().eq(Certificate::getStudentName, studentName).one();
        String pdfPath = certificate.getPdfPath();
        return Result.success(pdfPath);
    }
}



