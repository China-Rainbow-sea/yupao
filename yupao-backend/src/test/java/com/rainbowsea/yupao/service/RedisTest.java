package com.rainbowsea.yupao.service;


import com.rainbowsea.yupao.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {


    @Resource
    private RedisTemplate redisTemplate; // 实例化 操作 Redis 的实例模板


    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 增
        valueOperations.set("yupiString","dog");
        valueOperations.set("yupiInt",1);
        valueOperations.set("yupiDouble",2.0);

        User user = new User();
        user.setId(1L);
        user.setUsername("yupi");

        valueOperations.set("yupiUser",user);

        // 查
        Object yupi = valueOperations.get("yupiString");
        Assertions.assertTrue("dog".equals((String)yupi));
        yupi = valueOperations.get("yupiInt");
        Assertions.assertTrue(1 == (Integer) yupi);
        yupi = valueOperations.get("yupiDouble");
        Assertions.assertTrue(2.0 == (Double) yupi);

        System.out.println(valueOperations.get("yupiUser"));


    }
}
