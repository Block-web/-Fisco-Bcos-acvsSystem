package com.zheng.acvsystem.utill;

import com.aliyun.oss.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.InputStream;

//该类是阿里云OSS工具类
public class AliOssUtil {
    // Endpoint以（广东）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-guangzhou.aliyuncs.com";
    // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
    //EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

    // 定义两个变量用于存储访问凭证
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";

    // 填写Bucket名称，例如：web-learning-hush。
    private static final String BUCKET_NAME = "acvs";

    public static String uploadFile(String objectName, InputStream in) throws Exception {

        // 填写Bucket所在地域。以guangzhou为例，Region填写为cn-guangzhou。
        String region = "";

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        String url = "";

        try {
            // 填写字符串。
            String content = "Hello OSS，你好世界";

            //创建一个变量用于存储本地路径
            String PngPath = "C:\\Users\\zzw43\\Desktop\\资料SpringBoot+Vue3\\file\\屏幕截图 2025-11-19 113659.png";

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, in);

            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            //代码执行到这没抛异常说明 图片上传成功  可以返回url地址 下面给url地址赋值
            //url组成 https://Bucket名称.区域节点./Object名称               //截取斜杠后的字符
            url = "https://" + BUCKET_NAME + "." + ENDPOINT.substring(ENDPOINT.lastIndexOf("/") + 1) + "/" + objectName;

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}
