package com.zheng.acvsystem.fiscobcos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.eventsub.EventSubCallback;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CertHashRegistry extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506106f6806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063206e33e91461004657806365b2a8631461006d5780637338b1a414610082575b600080fd5b61005961005436600461054d565b610095565b604051901515815260200160405180910390f35b61008061007b3660046105ba565b6101b8565b005b61008061009036600461054d565b610281565b6000806000856040516100a89190610627565b90815260200160405180910390206040518060800160405290816000820180546100d190610643565b80601f01602080910402602001604051908101604052809291908181526020018280546100fd90610643565b801561014a5780601f1061011f5761010080835404028352916020019161014a565b820191906000526020600020905b81548152906001019060200180831161012d57829003601f168201915b505050918352505060018201546020808301919091526002830154604083015260039092015460ff166060909101528551868201208251805192019190912091925014801561019c5750828160200151145b80156101af5750806060015160ff166001145b95945050505050565b600080826040516101c99190610627565b908152604051908190036020019020600381015490915060ff166001146102295760405162461bcd60e51b815260206004820152600f60248201526e185b1c9958591e481c995d9bdad959608a1b60448201526064015b60405180910390fd5b60038101805460ff19169055604051610243908390610627565b604051908190038120428252907fa97b8e48f0c27a412b5ccd838adab54a628707d30022c301a3cb6be67fbf8fac9060200160405180910390a25050565b82516010146102c85760405162461bcd60e51b815260206004820152601360248201527231b2b93a2737903632b733ba341032b93937b960691b6044820152606401610220565b6000836040516102d89190610627565b90815260405190819003602001902080546102f290610643565b1590506103315760405162461bcd60e51b815260206004820152600d60248201526c636572744e6f2065786973747360981b6044820152606401610220565b60408051608081018252838152602081018390524281830152600160608201529051600090610361908690610627565b9081526020016040518091039020600082015181600001908051906020019061038b929190610411565b506020820151600182015560408083015160028301556060909201516003909101805460ff191660ff909216919091179055516103c9908490610627565b60405180910390207f6c4429514141747043382089b390e7fcdb1c3df3f48c34d26f095fc0f2ec04c68383426040516104049392919061067e565b60405180910390a2505050565b82805461041d90610643565b90600052602060002090601f01602090048101928261043f5760008555610485565b82601f1061045857805160ff1916838001178555610485565b82800160010185558215610485579182015b8281111561048557825182559160200191906001019061046a565b50610491929150610495565b5090565b5b808211156104915760008155600101610496565b634e487b7160e01b600052604160045260246000fd5b600082601f8301126104d157600080fd5b813567ffffffffffffffff808211156104ec576104ec6104aa565b604051601f8301601f19908116603f01168101908282118183101715610514576105146104aa565b8160405283815286602085880101111561052d57600080fd5b836020870160208301376000602085830101528094505050505092915050565b60008060006060848603121561056257600080fd5b833567ffffffffffffffff8082111561057a57600080fd5b610586878388016104c0565b9450602086013591508082111561059c57600080fd5b506105a9868287016104c0565b925050604084013590509250925092565b6000602082840312156105cc57600080fd5b813567ffffffffffffffff8111156105e357600080fd5b6105ef848285016104c0565b949350505050565b60005b838110156106125781810151838201526020016105fa565b83811115610621576000848401525b50505050565b600082516106398184602087016105f7565b9190910192915050565b600181811c9082168061065757607f821691505b6020821081141561067857634e487b7160e01b600052602260045260246000fd5b50919050565b606081526000845180606084015261069d8160808501602089016105f7565b60208301949094525060408101919091526080601f909201601f1916010191905056fea2646970667358221220d0407eebe418339a91750dce670de98eaa31e96ecb19e20abdc7721d38c2ebbf64736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b506106f9806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80630dec3c981461004657806380ff75e51461005b578063959d6fee14610082575b600080fd5b610059610054366004610550565b610095565b005b61006e610069366004610550565b61022c565b604051901515815260200160405180910390f35b6100596100903660046105bd565b61034f565b82516010146100e257604051636381e58960e11b815260206004820152601360248201527231b2b93a2737903632b733ba341032b93937b960691b60448201526064015b60405180910390fd5b6000836040516100f2919061062a565b908152604051908190036020019020805461010c90610646565b15905061014c57604051636381e58960e11b815260206004820152600d60248201526c636572744e6f2065786973747360981b60448201526064016100d9565b6040805160808101825283815260208101839052428183015260016060820152905160009061017c90869061062a565b908152602001604051809103902060008201518160000190805190602001906101a6929190610414565b506020820151600182015560408083015160028301556060909201516003909101805460ff191660ff909216919091179055516101e490849061062a565b60405180910390207f1da288d77ef72dbd719c6ba49ff51bec1b542c29ce4caba75634bdd7b6e7980483834260405161021f93929190610681565b60405180910390a2505050565b60008060008560405161023f919061062a565b908152602001604051809103902060405180608001604052908160008201805461026890610646565b80601f016020809104026020016040519081016040528092919081815260200182805461029490610646565b80156102e15780601f106102b6576101008083540402835291602001916102e1565b820191906000526020600020905b8154815290600101906020018083116102c457829003601f168201915b505050918352505060018201546020808301919091526002830154604083015260039092015460ff16606090910152855186820120825180519201919091209192501480156103335750828160200151145b80156103465750806060015160ff166001145b95945050505050565b60008082604051610360919061062a565b908152604051908190036020019020600381015490915060ff166001146103bc57604051636381e58960e11b815260206004820152600f60248201526e185b1c9958591e481c995d9bdad959608a1b60448201526064016100d9565b60038101805460ff191690556040516103d690839061062a565b604051908190038120428252907f27716753890e3d68868dbe8c2c892cc5492e03b57a5417e41ae77c84bb2965679060200160405180910390a25050565b82805461042090610646565b90600052602060002090601f0160209004810192826104425760008555610488565b82601f1061045b57805160ff1916838001178555610488565b82800160010185558215610488579182015b8281111561048857825182559160200191906001019061046d565b50610494929150610498565b5090565b5b808211156104945760008155600101610499565b63b95aa35560e01b600052604160045260246000fd5b600082601f8301126104d457600080fd5b813567ffffffffffffffff808211156104ef576104ef6104ad565b604051601f8301601f19908116603f01168101908282118183101715610517576105176104ad565b8160405283815286602085880101111561053057600080fd5b836020870160208301376000602085830101528094505050505092915050565b60008060006060848603121561056557600080fd5b833567ffffffffffffffff8082111561057d57600080fd5b610589878388016104c3565b9450602086013591508082111561059f57600080fd5b506105ac868287016104c3565b925050604084013590509250925092565b6000602082840312156105cf57600080fd5b813567ffffffffffffffff8111156105e657600080fd5b6105f2848285016104c3565b949350505050565b60005b838110156106155781810151838201526020016105fd565b83811115610624576000848401525b50505050565b6000825161063c8184602087016105fa565b9190910192915050565b600181811c9082168061065a57607f821691505b6020821081141561067b5763b95aa35560e01b600052602260045260246000fd5b50919050565b60608152600084518060608401526106a08160808501602089016105fa565b60208301949094525060408101919091526080601f909201601f1916010191905056fea26469706673582212201d5c483a2621707f568296077a649f80031563a5dc6a55e2b5fc7edc09474f1064736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"string\",\"name\":\"certNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"fileHash\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"collegeId\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"issueTime\",\"type\":\"uint256\"}],\"name\":\"Issued\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"string\",\"name\":\"certNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"revokeTime\",\"type\":\"uint256\"}],\"name\":\"Revoked\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"certNo\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"fileHash\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"collegeId\",\"type\":\"uint256\"}],\"name\":\"issue\",\"outputs\":[],\"selector\":[1933095332,233585816],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"certNo\",\"type\":\"string\"}],\"name\":\"revoke\",\"outputs\":[],\"selector\":[1706207331,2510122990],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"certNo\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"fileHash\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"collegeId\",\"type\":\"uint256\"}],\"name\":\"verify\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[544093161,2164225509],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ISSUE = "issue";

    public static final String FUNC_REVOKE = "revoke";

    public static final String FUNC_VERIFY = "verify";

    public static final Event ISSUED_EVENT = new Event("Issued", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REVOKED_EVENT = new Event("Revoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected CertHashRegistry(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<IssuedEventResponse> getIssuedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ISSUED_EVENT, transactionReceipt);
        ArrayList<IssuedEventResponse> responses = new ArrayList<IssuedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            IssuedEventResponse typedResponse = new IssuedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.certNo = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.fileHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.collegeId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.issueTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeIssuedEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(ISSUED_EVENT);
        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeIssuedEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(ISSUED_EVENT);
        subscribeEvent(topic0,callback);
    }

    public List<RevokedEventResponse> getRevokedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REVOKED_EVENT, transactionReceipt);
        ArrayList<RevokedEventResponse> responses = new ArrayList<RevokedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RevokedEventResponse typedResponse = new RevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.certNo = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.revokeTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeRevokedEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(REVOKED_EVENT);
        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeRevokedEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(REVOKED_EVENT);
        subscribeEvent(topic0,callback);
    }

    public TransactionReceipt issue(String certNo, String fileHash, BigInteger collegeId) {
        final Function function = new Function(
                FUNC_ISSUE, 
                Arrays.<Type>asList(new Utf8String(certNo),
                new Utf8String(fileHash),
                new Uint256(collegeId)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return executeTransaction(function);
    }

    public Function getMethodIssueRawFunction(String certNo, String fileHash, BigInteger collegeId)
            throws ContractException {
        final Function function = new Function(FUNC_ISSUE, 
                Arrays.<Type>asList(new Utf8String(certNo),
                new Utf8String(fileHash),
                new Uint256(collegeId)),
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForIssue(String certNo, String fileHash,
            BigInteger collegeId) {
        final Function function = new Function(
                FUNC_ISSUE, 
                Arrays.<Type>asList(new Utf8String(certNo),
                new Utf8String(fileHash),
                new Uint256(collegeId)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return createSignedTransaction(function);
    }

    public String issue(String certNo, String fileHash, BigInteger collegeId,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ISSUE, 
                Arrays.<Type>asList(new Utf8String(certNo),
                new Utf8String(fileHash),
                new Uint256(collegeId)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple3<String, String, BigInteger> getIssueInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ISSUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, BigInteger>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue()
                );
    }
    /**
     * 调用区块链智能合约的revoke方法，撤销指定证书
     *
     * @param certNo 证书编号，唯一标识要撤销的证书
     * @return 交易回执，包含交易哈希、状态码等信息
     *
     * 该方法通过FISCO BCOS SDK调用智能合约的revoke函数：
     * 1. 创建合约调用函数对象，封装证书编号参数
     * 2. 将字符串参数转换为智能合约所需的编码格式
     * 3. 执行区块链交易，在链上标记证书为已撤销
     * 4. 返回交易执行结果的回执
     */
    public TransactionReceipt revoke(String certNo) {
        final Function function = new Function(
                FUNC_REVOKE, 
                Arrays.<Type>asList(new Utf8String(certNo)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return executeTransaction(function);
    }

    public Function getMethodRevokeRawFunction(String certNo) throws ContractException {
        final Function function = new Function(FUNC_REVOKE, 
                Arrays.<Type>asList(new Utf8String(certNo)),
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForRevoke(String certNo) {
        final Function function = new Function(
                FUNC_REVOKE, 
                Arrays.<Type>asList(new Utf8String(certNo)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return createSignedTransaction(function);
    }

    public String revoke(String certNo, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REVOKE, 
                Arrays.<Type>asList(new Utf8String(certNo)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple1<String> getRevokeInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REVOKE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }
    /**
     * 调用区块链智能合约的verify方法，验证证书的有效性
     *
     * @param certNo     证书编号，用于唯一标识要验证的证书
     * @param fileHash   文件哈希值，用于验证证书PDF的完整性
     * @param collegeId  颁发证书的学院ID，用于验证证书的颁发机构
     * @return 验证结果：true表示证书有效，false表示证书无效或不存在
     * @throws ContractException 智能合约调用异常，可能由参数错误、合约不存在等原因引起
     *
     * 该方法通过FISCO BCOS SDK调用智能合约的verify函数：
     * 1. 创建合约调用函数对象，封装验证所需的参数
     * 2. 将Java类型参数转换为智能合约所需的编码格式
     * 3. 执行区块链调用（只读操作，不产生交易）
     * 4. 解析合约返回结果并转换为Java Boolean类型
     *
     * 注：与issue和revoke方法不同，verify是只读操作，不会产生区块交易，仅查询区块链状态
     */
    public Boolean verify(String certNo, String fileHash, BigInteger collegeId) throws
            ContractException {
        final Function function = new Function(FUNC_VERIFY, 
                Arrays.<Type>asList(new Utf8String(certNo),
                new Utf8String(fileHash),
                new Uint256(collegeId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public Function getMethodVerifyRawFunction(String certNo, String fileHash, BigInteger collegeId)
            throws ContractException {
        final Function function = new Function(FUNC_VERIFY, 
                Arrays.<Type>asList(new Utf8String(certNo),
                new Utf8String(fileHash),
                new Uint256(collegeId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public static CertHashRegistry load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new CertHashRegistry(contractAddress, client, credential);
    }

    public static CertHashRegistry deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(CertHashRegistry.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }

    public static class IssuedEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] certNo;

        public String fileHash;

        public BigInteger collegeId;

        public BigInteger issueTime;
    }

    public static class RevokedEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] certNo;

        public BigInteger revokeTime;
    }
}
