package com.zheng.acvsystem.interceptions;

import com.zheng.acvsystem.utill.JwtUtil;
import com.zheng.acvsystem.utill.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// 拦截器 : 用于在请求处理之前进行拦截和处理   需要继承:HandlerInterceptor
@Component  //将当前类的对象注册到IOC容器中
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;  //Redis模板类 用于操作Redis数据库

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        //获取请求头中的  Authorization 令牌
        String token = request.getHeader("Authorization");
        try {
            //尝试从Redis中获取相同的令牌
            //1.通过StringRedisTemplate对象的opsForValue方法获取操作字符串的ValueOperations对象
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            //2.通过ValueOperations对象 set方法获取Redis中存储的令牌
            String redisToken = operations.get(token); //从Redis中获取与token键关联的字符串值  在redis中 token本身就是健名
            //判断：如果 redisToken 为空 则拦截请求
            if (redisToken == null) {
                //抛出异常 被下面的 catch捕获
                throw new RuntimeException();
            }
            //通过JwtUtil工具类解析令牌（token）, 获取到业务数据claims
            Map<String, Object> claims = JwtUtil.parseToken(token);

            //把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);

            //ture表示放行
            return true;
        } catch (Exception e) {
            //通过response设置状态码为401 表示未授权
            response.setStatus(401);
            //false表示不放行
            return false;
        }

    }


    //afterCompletion方法：在请求处理完成后调用，用于清除ThreadLocal中的数据
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();// 清除ThreadLocal中的数据，防止内存泄漏
    }
}
