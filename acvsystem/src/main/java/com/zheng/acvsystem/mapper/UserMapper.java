package com.zheng.acvsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zheng.acvsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yourname
 * @since 2026-01-15
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
        
}
