package com.zheng.acvsystem.utill;

import org.fisco.bcos.sdk.v3.model.TransactionReceipt;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class BlockchainSyncUtil {

    /** 默认链上等待时间（秒） */
    private static final long DEFAULT_TIMEOUT = 10;

    private BlockchainSyncUtil() {}

    /**
     * 同步等待交易完成，超时转运行时异常
     */
    public static TransactionReceipt waitForTx(CompletableFuture<TransactionReceipt> future) {
        return waitForTx(future, DEFAULT_TIMEOUT);
    }

    public static TransactionReceipt waitForTx(CompletableFuture<TransactionReceipt> future, long timeoutSeconds) {
        try {
            TransactionReceipt receipt = future.get(timeoutSeconds, TimeUnit.SECONDS);
            if (!"0x0".equals(receipt.getStatus())) {
                throw new RuntimeException("链上交易失败，status=" + receipt.getStatus());
            }
            return receipt;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("线程被中断", e);
        } catch (Exception e) {
            throw new RuntimeException("链上交易异常", e);
        }
    }

    /**
     * 同步调用 verify 这类只读接口（无 TransactionReceipt）
     */
    public static <T> T waitForCall(CompletableFuture<T> future) {
        return waitForCall(future, DEFAULT_TIMEOUT);
    }

    public static <T> T waitForCall(CompletableFuture<T> future, long timeoutSeconds) {
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("链上查询异常", e);
        }
    }
}