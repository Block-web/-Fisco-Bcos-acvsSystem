package com.zheng.acvsystem.Validation;

import com.zheng.acvsystem.anao.Role;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
//ConstraintValidator<Role, String>  第一个参数为自定义注解，第二个参数为校验的字段类型
public class RoleValidation implements ConstraintValidator<Role, String> {

    @Override   //isValid提供 校验逻辑
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果角色值为 null，则由 @NotNull 或 @NotBlank 注解处理
        if (value == null) {
            return true;
        }
        // 检查角色值是否在有效范围内
        return "COLLEGE".equals(value) || "STUDENT".equals(value) || "COMPANY".equals(value);
    }
}   