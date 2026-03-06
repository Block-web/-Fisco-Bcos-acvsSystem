package com.zheng.acvsystem;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;


public class CodeGen {
    public static void main(String[] args) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/acvsystem?useSSL=false&serverTimezone=UTC",
                        "root",
                        "123456")
                .globalConfig(builder -> {
                    builder.author("yourname")        // 作者
                            .outputDir(System.getProperty("user.dir") + "/src/main/java") // 输出目录
                            .enableSwagger()           // 开启 swagger 注解
                            .commentDate("yyyy-MM-dd");
                })
                .packageConfig(builder -> {
                    builder.parent("com.zheng.acvsystem")      // 包名根路径
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user", "certificate", "verify_log") // 要生成的表
                            .addTablePrefix("")                         // 表前缀（可选）
                            .entityBuilder()
                            .enableLombok()                            // Lombok
                            .enableChainModel()                        // 链式 setter
                            .controllerBuilder()
                            .enableRestStyle()                         // @RestController
                            .formatFileName("%sController")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 模板
                .execute();
    }
}

