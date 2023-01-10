package com.zlz9.springbootmanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SpringbootManagerApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
    }
    @Test
    void redisUtils(){

    }
    @Test
    void testBcrypassword(){
        String encode = passwordEncoder.encode("admin123");
        System.out.println(encode);
    }
}
