package com.zheng.acvsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zheng.acvsystem.anao.Role;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author yourname
 * @since 2026-01-15
 */
@Validated
@TableName("user")  // 数据库表名
@Accessors(chain = true)  // 链式调用  例如：user.setUsername("zzw").setPassword("123456").setRole("COLLEGE");
@Data // 提供getter、setter、toString、equals、hashCode等方法
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 主键id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    @JsonIgnore  // 忽略密码字段，防止密码泄露
    private String password;

    @NotNull(message = "角色不能为空")
    @Role(message = "角色值无效，只能是 COLLEGE、STUDENT 或 COMPANY")
    private String role;

    @NotNull(message = "真实姓名不能为空",groups = updateUser.class)
    private String realName;

    //@Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",message = "身份证号格式错误")
    private String idCard;

    private LocalDateTime createTime;

    // 更新用户时需要校验的字段
    public interface updateUser {
    }
}
