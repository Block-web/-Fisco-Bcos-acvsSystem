package com.zheng.acvsystem.service.impl;

import com.zheng.acvsystem.entity.User;
import com.zheng.acvsystem.mapper.UserMapper;
import com.zheng.acvsystem.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired //导入映射层
    private UserMapper userMapper;


}
