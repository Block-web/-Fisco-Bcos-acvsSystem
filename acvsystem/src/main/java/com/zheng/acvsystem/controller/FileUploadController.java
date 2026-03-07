package com.zheng.acvsystem.controller;

import com.zheng.acvsystem.entity.Result;
import com.zheng.acvsystem.utill.AliOssUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RestController
public class FileUploadController {
    @PostMapping("/upload")   //MultipartFile类：封装了文件上传的相关信息
    public Result<String> upload(MultipartFile file) throws Exception {
        //1.把文件的内容存储到本地磁盘上
        //2.获取原文件名
        String name = file.getOriginalFilename();
        //保证文件唯一                                 // name.lastIndexOf(".")：获取文件名中的最后一个"."的索引
        //String filename = studentName + name.substring(name.lastIndexOf("."));
       /* 3.把文件存储到本地磁盘上              MultipartFile.transferTo方法： 把上传的文件内容转移到指定的文件中
        file.transferTo(new File("C:\\Users\\zzw43\\Desktop\\资料SpringBoot+Vue3\\file\\"+name));*/
        String url = AliOssUtil.uploadFile(name, file.getInputStream());

        //上传成功返回url
        return Result.success(url);
    }


}
