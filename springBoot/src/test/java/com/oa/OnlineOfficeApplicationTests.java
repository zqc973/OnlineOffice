package com.oa;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.oa.constant.JwtTokenConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
public class OnlineOfficeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void getEncodePassword(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "123456";
        //$2a$10$M/KyYYQw9cCZSXAGHRAt.usX3TzALO.WWYMb4VpnXOpewsgLLlWeq
        log.info("加密后的密码：{}",passwordEncoder.encode(password));
    }
    @Test
    public void v(){
       String s =  "\ufffd̅5\ufffd7(և\ufffd\u001f\n\ufffd;L\ufffd\ufffd7\ufffdl\ufffd;\ufffd\ufffd\u0016\ufffd\ufffdyCͅ\ufffd\ufffd9,\ufffdE\ufffdz\ufffd:\ufffd\ufffdU\ufffd\u001a\b\ufffd\u001b\"\ufffd\u0014u5f)\u0015v\ufffdd\ufffd\ufffdizC~\u0016\ufffdɬ\ufffd\ufffd\ufffd\ufffd\u001e\ufffd\u001eq\ufffdD\ufffd\ufffdb\ufffdrv7\ufffd\ufffd\ufffd\ufffd9R\ufffd\ufffd\u0001\u0004)\ufffd\u007f\ufffdl Z\ufffds\ufffd6=\ufffdBq\ufffd\ufffd%\ufffd\ufffd\ufffd\ufffd\ufffd\u0000\f\ufffd7\ufffd\u000bcr\ufffdp\ufffd]Y\ufffd\ufffd\ufffd\ufffd(0\u0019?\ufffdQ\ufffd]\ufffd\r\u001f\ufffd'<\ufffd\ufffd7\u0012\ufffd\ufffd\ufffd\ufffd\u0016a2\ufffd(\u0017ڃ\ufffd\ufffdxf\ufffd\u0000\ufffd\ufffdp\ufffd\ufffd\ufffd\ufffd\ufffd{\ufffd\u000e\ufffd\n\b\ufffd";

        AES aes = SecureUtil.aes(JwtTokenConstant.JWT_TOKEN_SECRET.getBytes());
        byte[] decrypt = aes.decrypt(s);
        System.out.println(new String(decrypt));
    }
}
