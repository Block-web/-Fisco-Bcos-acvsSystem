package com.zheng.acvsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yourname
 * @since 2026-01-15
 */

@Accessors(chain = true)
@TableName("certificate")
@Data // 提供getter、setter、toString、equals、hashCode等方法
@ApiModel("学术证书")
@NoArgsConstructor
@AllArgsConstructor

public class Certificate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("学术证书ID")
    private Long id;

    @ApiModelProperty("学术证书编号")
    private String certNo;
    @NotNull
    @ApiModelProperty("学生姓名")
    private String studentName;
    @NotNull
    @ApiModelProperty("学生身份证号")
    private String idCard;
    @NotNull
    @ApiModelProperty("学院ID")
    private Long collegeId;
    @NotNull
    @ApiModelProperty("专业")
    private String major;
    @NotNull
    @ApiModelProperty("学位类型")
    private String degreeType;
    @NotNull
    @ApiModelProperty("毕业日期")
    private LocalDate graduationDate;
    @ApiModelProperty("PDF文件路径")
    @NotNull
    private String pdfPath;
    @ApiModelProperty("文件哈希值")
    @NotNull
    private String fileHash;
    @ApiModelProperty("交易哈希值")
    private String txHash;
    @ApiModelProperty("状态")
    private Byte status;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
