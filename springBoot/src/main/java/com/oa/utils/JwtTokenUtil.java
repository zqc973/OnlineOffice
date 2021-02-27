package com.oa.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.oa.constant.JwtTokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
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

        //Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        //ArrayList<String> authoritiesList = new ArrayList<>(authorities.size() * 2);
        ////设置权限
        //authorities.forEach(auth -> authoritiesList.add(auth.getAuthority()));
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenConstant.CLAIM_KEY_USERNAME, userDetails.getUsername());
        //claims.put(JwtTokenConstant.CLAIM_KEY_AUTHORITIES, authoritiesList);
        return JwtTokenConstant.JWT_TOKEN_HEAD+generateToken(claims);
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
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameByToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpiration(token);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token){
        return  !isTokenExpiration(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token){
        Claims claims = getClaimsByToken(token);
        return generateToken(claims);
    }

    /**
     * 判断token是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpiration(String token) {
        Date expireDate = getExpiredDateByToken(token);
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
     * 根据荷载生成token
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration())
                .signWith(SignatureAlgorithm.HS512, JwtTokenConstant.CLAIM_KEY_SECRET)
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpiration() {
        return new Date(System.currentTimeMillis() + JwtTokenConstant.CLAIM_KEY_EXPIRATION*1000);
    }

    /**
     * 为token加密
     * @return
     */
    public String encryptToken(String token) {
        AES aes = SecureUtil.aes(JwtTokenConstant.JWT_TOKEN_SECRET.getBytes());
        return new String(aes.encrypt(token));
    }

    /**
     * 为token加密
     * @return
     */
    public String decryptToken(String encrypt) {
        AES aes = SecureUtil.aes(JwtTokenConstant.JWT_TOKEN_SECRET.getBytes());
        return new String(aes.decrypt(encrypt));
    }


}
