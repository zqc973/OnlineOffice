package com.oa;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.oa.constant.JwtTokenConstant;
import com.oa.utils.RedisUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class OnlineOfficeApplicationTests {

    @Resource
    private RedisUtil redisUtil;

    @Test
    void getToken() {
        Map<String, Object> claims = new HashMap<>();

        claims.put(JwtTokenConstant.CLAIM_KEY_USERNAME, "zqc");
        String s = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.parse("2022-01-01 00:00:00"))
                .signWith(SignatureAlgorithm.HS512, JwtTokenConstant.CLAIM_KEY_SECRET)
                .compact();

        redisUtil.set(s,s);
        System.out.println(s);
    }

    @Test
    public void getEncodePassword(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "123456";
        //$2a$10$zB7quEcn3xXes6IhAOrGGuafLCdwEqlAzydA0N51nnzmakA0xjzCm
        log.info("加密后的密码：{}",passwordEncoder.encode(password));
    }
}
