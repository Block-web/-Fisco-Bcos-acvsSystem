package com.zheng.acvsystem.utill;

import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.v3.transaction.manager.TransactionProcessorFactory;

import java.io.IOException;

public class FiscoBcosUtil {
    private BcosSDK sdk;
    private Client client;
    private AssembleTransactionProcessor transactionProcessor;

    // 在需要时初始化
    private void initSDK() throws IOException {
        if (sdk == null) {
            // 使用类路径访问配置文件
            sdk = BcosSDK.build("classpath:conf/config.toml");
            client = sdk.getClient("group0");
            CryptoKeyPair keyPair = client.getCryptoSuite().getCryptoKeyPair();
            transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "classpath:abi/", "classpath:bin/");
        }
    }
}
