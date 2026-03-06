package com.zheng.acvsystem.service.impl;

import com.zheng.acvsystem.entity.Certificate;
import com.zheng.acvsystem.mapper.CertificateMapper;
import com.zheng.acvsystem.service.CertificateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yourname
 * @since 2026-01-15
 */
@Service
@Slf4j
public class CertificateServiceImpl extends ServiceImpl<CertificateMapper, Certificate> implements CertificateService {
    @Autowired  //导入映射层
    private CertificateMapper certificateMapper;
}



