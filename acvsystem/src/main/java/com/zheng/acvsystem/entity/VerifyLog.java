package com.zheng.acvsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("验证日志")
@Accessors(chain = true)
@TableName("verify_log")
@Data // 提供getter、setter、toString、equals、hashCode等方法

public class VerifyLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("证书ID")
    private Long certId;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("验证类型")
    private String verifyType;

    @ApiModelProperty("验证结果")
    private String result;

    @ApiModelProperty("验证时间")
    private LocalDateTime verifyTime;
}
