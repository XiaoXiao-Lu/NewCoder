package com.kinnon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Kinnon
 * @create 2022-08-11 20:08
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        redisTemplate.opsForValue().increment("kinnon1111");
        System.out.println(redisTemplate.opsForValue().get("kinnon1111"));
    }
}
