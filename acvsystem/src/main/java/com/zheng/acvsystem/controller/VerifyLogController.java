package com.zheng.acvsystem.controller;

import com.zheng.acvsystem.service.VerifyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器  验证日志
 * </p>
 *
 * @author zzw
 * @since 2026-01-15
 */
@RestController
@RequestMapping("/verifyLog")
public class VerifyLogController {
    //导入服务层
    @Autowired
    private  VerifyLogService verifyLogService;

}
