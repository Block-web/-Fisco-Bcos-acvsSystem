package com.zheng.acvsystem.anao;

// 自定义校验注解：校验角色参数是否合法
//Spring Boot 2.7.x 及以下版本：使用 javax.validation 包
import com.zheng.acvsystem.Validation.RoleValidation;
import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

//1. 通过@NotNull 获取初始注解
//2. 创建一个RoleValidation类去继承ConstraintValidator接口 重写里面的方法并提供校验规则
//3. 在需要校验的字段上添加@Role注解

@Documented
@Constraint(validatedBy = {RoleValidation.class}) //指定提供校验规则的类
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.TYPE_USE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)

public @interface Role {
    //校验失败后的提示信息
    String message() default "角色参数的值只能是 COLLEGE、STUDENT 或 COMPANY";

    //分组校验
    Class<?>[] groups() default {};

    //负载： 用于获取State注解的附加信息
    Class<? extends javax.validation.Payload>[] payload() default {};
}

