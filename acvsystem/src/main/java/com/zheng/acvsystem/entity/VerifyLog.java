package com.zheng.acvsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzw
 * @since 2026-01-15
 */

@Accessors(chain = true)
@TableName("verify_log")
@Data // 提供getter、setter、toString、equals、hashCode等方法

public class VerifyLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long certId;

    private Long companyId;

    private String verifyType;

    private String result;

    private LocalDateTime verifyTime;
}
