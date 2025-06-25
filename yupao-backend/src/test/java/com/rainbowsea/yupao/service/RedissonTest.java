package com.rainbowsea.yupao.service;


import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;


    @Test
    void test() {
        // list 数据存在代 本地 JVM 内存中
        List<String> list = new ArrayList<>();
        list.add("yupi");
        list.get(0);
        System.out.println("list: " + list.get(0));
        list.remove(0);


        // 数据存入 redis 的内存中
        RList<Object> rList = redissonClient.getList("test-list");  // 表示 redis 当中的 key
        rList.add("yupi");
        System.out.println("rlist:" + rList.get(0));
    }
}
