package com.kinnon;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kinnon
 * @create 2022-08-11 20:08
 */
@SpringBootTest
@ContextConfiguration(classes = SpringbootProjectDemo1Application.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void test() {
        redisTemplate.opsForValue().increment("kinnon1111");
        System.out.println(redisTemplate.opsForValue().get("kinnon1111"));
    }

    @Test
    public void teetHll(){
        String hllKey = "test:hll:01";
        for (int i = 0; i < 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(hllKey,i);
        }
        for (int i = 0; i < 100000; i++) {
            int temp = (int)(Math.random() * 100000 +1);
            redisTemplate.opsForHyperLogLog().add(hllKey,temp);
        }

        System.out.println(redisTemplate.opsForHyperLogLog().size(hllKey));
    }
}
