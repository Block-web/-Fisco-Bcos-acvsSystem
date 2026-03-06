package com.zheng.acvsystem.service.impl;

import com.zheng.acvsystem.entity.VerifyLog;
import com.zheng.acvsystem.mapper.VerifyLogMapper;
import com.zheng.acvsystem.service.VerifyLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yourname
 * @since 2026-01-15
 */
@Service
public class VerifyLogServiceImpl extends ServiceImpl<VerifyLogMapper, VerifyLog> implements VerifyLogService {
    @Autowired  //导入映射层
    private VerifyLogMapper verifyLogMapper;
}
