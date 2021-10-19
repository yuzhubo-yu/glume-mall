package com.glume.glumemall.admin.util;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类
 * @author tuoyingtao
 * @create 2021-10-18 11:03
 */
@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {

    /* JWT加解密使用的密钥 */
    private String secret;

    /* JWT的超期限时间(60*60*24*7) */
    private Long expiration;

    /* JWT 名称 */
    private String header;

    /**
     * 创建Token
     * @param username
     * @return
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 从Token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        return getTokenBody(token).getSubject();
    }

    private Claims getTokenBody(String token) {
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 验证Token是否过期
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }
}
