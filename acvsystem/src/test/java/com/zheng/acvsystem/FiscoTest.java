package com.zheng.acvsystem;

import com.zheng.acvsystem.entity.Result;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodecException;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.v3.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.v3.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.v3.transaction.model.exception.TransactionBaseException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FiscoTest {
    public static final String CONFIG_FILE = "src/main/resources/conf/config.toml";
    public static final String ABI_CONFIGPATH = "src/main/resources/abi/";
    public static final String BIN_CONFIGPATH = "src/main/resources/bin/";
    public static final String CONTRACTADDRESS = "0xc8ead4b26b2c6ac14c9fd90d9684c9bc2cc40085";


    //异步接收器
    static TransactionCallback callback = new TransactionCallback() {
        @Override
        public void onResponse(TransactionReceipt receipt) {
            if ("0x0".equals(receipt.getStatus())) {
                Result.success("异步发证成功, txHash={}" + receipt.getTransactionHash() + "}");
                // 这里可以：发 WebSocket、写数据库、推 MQ
            } else {
                Result.error("异步发证失败, status={" + receipt.getStatus() + "}");
            }
        }
    };

    @Test
    public  void test() throws IOException, TransactionBaseException, ContractCodecException {
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
        params.add("C202500000002");
        //fileHash
        params.add("66c7f0f462c6f6e6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6f6a");
        //collegeId
        params.add(BigInteger.valueOf(1));
        // 调用HelloWorld合约，合约地址为helloWorldAddress， 调用函数名为『set』，函数参数类型为params
        TransactionResponse transactionResponse = transactionProcessor.
                sendTransactionAndGetResponseByContractLoader("CertHashRegistry", CONTRACTADDRESS, "issue", params);


    }
}
