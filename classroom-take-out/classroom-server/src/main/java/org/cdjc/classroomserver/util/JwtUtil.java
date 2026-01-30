package org.cdjc.classroomserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;


public class JwtUtil {

    /**
     * 使用 HS256 的密钥
     * 密钥长度至少需要256位（ 32个字节 ）才能满足HS256的要求
     */
    private static final String SECRET = "ClassroomTakeOutSecretKeyForJWTTokenGeneration2024MustBeAtLeast256Bits";
    private static final long JWT_EXPIRE_MS = 7200000;

    /**
     * 生成 JWT
     *
     * @param subject   主题，一般放 userId 或 username
     * @param claims    自定义负载（角色、用户名等）
     */
    public static String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_EXPIRE_MS);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 token，失败或过期会抛异常（spring会自己解析是什么异常），调用方自己捕获
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取签名密钥
     * 将字符串直接转换为字节数组，确保密钥长度满足HS256要求
     */
    private static Key getSigningKey() {
        byte[] keyBytes = SECRET.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


