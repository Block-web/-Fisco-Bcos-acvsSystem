package com.zheng.acvsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

public class Certificate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String certNo;
    @NotNull
    private String studentName;
    @NotNull
    private String idCard;
    @NotNull
    private Long collegeId;
    @NotNull
    private String major;
    @NotNull
    private String degreeType;
    @NotNull
    private LocalDate graduationDate;
    @NotNull
    private String pdfPath;
    @NotNull
    private String fileHash;

    private String txHash;

    private Byte status;

    private LocalDateTime createTime;
}
