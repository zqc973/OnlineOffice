package com.oa.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.oa.constant.JwtTokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @PackageName:com.oa.utils
 * @ClassName:JwtTokenUtil
 * @Description: jwtToken工具类
 * @Author: zqc
 * @date 2021/2/22 10:51
 */
@Component
public class JwtTokenUtil {

    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenConstant.CLAIM_KEY_USERNAME, userDetails.getUsername());
        return generateToken(claims, JwtTokenConstant.CLAIM_KEY_EXPIRATION);
    }
    /**
     * 根据用户信息生成刷新token
     * @param userDetails
     * @return
     */
    public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenConstant.CLAIM_KEY_USERNAME, userDetails.getUsername());
        return generateToken(claims, JwtTokenConstant.REFRESH_KEY_EXPIRATION);
    }

    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameByToken(String token){
        String username;
        try {
            Claims claims = getClaimsByToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 校验token合法性
     * @param token
     * @return
     */
    public boolean validateToken(String token){
        return null != getClaimsByToken(token);
    }

    /**
     * 刷新token
     * @return
     */
    public Map<String, Object> refreshToken(String refreshToken){
        Claims claims = getClaimsByToken(refreshToken);
        Map<String, Object> token = new HashMap<>();
        token.put("access_token", generateToken(claims, JwtTokenConstant.CLAIM_KEY_EXPIRATION));
        token.put("refresh_token", generateToken(claims, JwtTokenConstant.REFRESH_KEY_EXPIRATION));

        return token;
    }

    /**
     * 判断token是否失效
     * @param token
     * @return
     */
    public boolean isTokenExpiration(String token) {
        Date expireDate = null;
        try {
            expireDate = getExpiredDateByToken(token);
        } catch (Exception e) {
            return false;
        }
        return expireDate.before(new Date());
    }


    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateByToken(String token) {
        return getClaimsByToken(token).getExpiration();
    }

    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsByToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JwtTokenConstant.CLAIM_KEY_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据荷载及失效时间生成token,
     * @param claims 荷载
     * @param expiration 时间（秒）
     * @return
     */
    private String generateToken(Map<String, Object> claims, long expiration){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration(expiration*1000))
                .signWith(SignatureAlgorithm.HS512, JwtTokenConstant.CLAIM_KEY_SECRET)
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpiration(long expiration) {
        return new Date(System.currentTimeMillis() + expiration);
    }


}
