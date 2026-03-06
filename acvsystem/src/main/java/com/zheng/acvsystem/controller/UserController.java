package com.zheng.acvsystem.controller;

import com.zheng.acvsystem.anao.Role;
import com.zheng.acvsystem.entity.Result;
import com.zheng.acvsystem.entity.User;
import com.zheng.acvsystem.service.UserService;
import com.zheng.acvsystem.utill.JwtUtil;
import com.zheng.acvsystem.utill.Md5Util;
import com.zheng.acvsystem.utill.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器  用户
 * </p>
 *
 * @author zzw
 * @since 2026-01-15
 */

@Validated //开启参数校验
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate; //Redis模板类 用于操作Redis数据库
    @Autowired  //导入服务层
    private UserService userService;

    //注册接口
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password, @Role String role) {
        //判断用户名是否已存在
        if (userService.lambdaQuery().eq(User::getUsername, username).one() != null) {
            return Result.error("用户名已存在");
        }
        //创建用户对象 存储 用户信息
        User user = new User();
        user.setUsername(username);
        user.setPassword(Md5Util.getMD5String(password)); //密码加密存储
        user.setRole(role);
        //保存用户 运用了mybatis plus 提供的save方法   （需要先在服务层接口继承IService<User>接口  如何在服务层实现类继承ServiceImpl<UserMapper, User>类）
        userService.save(user);
        return Result.success("用户:" + username + "注册成功");
    }

    //登录接口
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password, @Role String role) {
        //根据用户名和密码查询用户
        User loginUser = userService.lambdaQuery()
                //.eq(实体类字段引用, 要匹配的值)  mybatis plus 会自动根据实体类字段引用生成sql语句
                .eq(User::getUsername, username)  //根据用户名查询用户
                .eq(User::getPassword, Md5Util.getMD5String(password)) //根据密码查询用户 and 密码加密后比较
                .eq(User::getRole, role)  //根据角色查询用户
                .one();  // one方法：执行查询，返回User对象
        if (loginUser == null) {
            return Result.error("用户名或密码错误");
        }
        //登录成功
        //创建一个map集合用于存放载荷
        Map<String, Object> claims = new HashMap<>();
        //添加基本信息作为载荷
        claims.put("id", loginUser.getId());
        claims.put("username", loginUser.getUsername());
        claims.put("role", loginUser.getRole());
        //载荷claims通过JWT工具类生成token
        String token = JwtUtil.genToken(claims);
        //把生成的token存储到Redis中
        //通过opsForValue()方法获取ValueOperations对象
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        //通过ValueOperations对象 set方法操作数据  存储token到Redis中  过期时间6小时
        operations.set(token, token, 6, TimeUnit.HOURS);
        //返回token
        return Result.success(token);
    }

    //通过username查询用户信息
    @GetMapping("/userInfo")
    public Result<User> getuserInfo() {
       /* 根据用户名查询用户
        思路：从JWT令牌（请求头）中获取用户信息
        调用JWTUtil工具类解析token*/

        /*Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/

        //调用userService根据用户名查询用户 返回User对象
        //从ThreadLocal中获取用户名字
        Map claims = (Map) ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        //为了不响应密码 需要将User对象中password属性添加JsonIgnore注解
        return Result.success(user);
    }

    //college用户更新基本信
    @PutMapping("/update")
    public Result updateByUserName(@RequestBody User user) {
        if (user.getRealName() == null || user.getIdCard() == null) {
            return Result.error("真实姓名或身份证号不能为空");
        }
        //验证身份证号是否重复
        if (userService.lambdaQuery().eq(User::getIdCard, user.getIdCard()).one() != null) {
            return Result.error("身份证号已存在");
        }
        //验证身份证号格式是否正确
        if (!user.getIdCard().matches("^[1-9]\\d{5}(?:18|19|20)\\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\\d|30|31)\\d{3}[\\dXx]$")) {
            return Result.error("身份证号格式错误");
        }
        userService.lambdaUpdate().set(User::getRealName, user.getRealName())
                .set(User::getIdCard, user.getIdCard())  //参数一：要更新的字段  参数二：要更新的值
                .eq(User::getUsername, user.getUsername())// 添加更新条件
                .update();
        return Result.success("用户信息更新成功");
    }

    //更新密码
    @PutMapping("/update-password")
    public Result updatePassword(String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        //判断用户是否存在
        if (userService.lambdaQuery().eq(User::getUsername, username).one() == null) {
            return Result.error("用户未登录");
        }
        //更新密码
        userService.lambdaUpdate().set(User::getPassword, Md5Util.getMD5String(password)).update();
        return Result.success("密码更新成功");
    }   




}
