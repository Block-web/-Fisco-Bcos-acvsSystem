package com.zheng.acvsystem.utill;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String KEY = "zhengzhongwu";

    //接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims) // 添加一个claims负载, 用于存储业务数据
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 6)) // 添加一个过期时间负载, 用于设置token的过期时间
                .sign(Algorithm.HMAC256(KEY)); // 使用HMAC256算法, 并传入密钥, 对token进行签名
    }

    //接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()  // 构建一个JWT验证器
                .verify(token)  // 验证token,并返回一个JWT对象
                .getClaim("claims")  // 获取token中的claims负载
                .asMap();
    }

}
