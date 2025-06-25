package com.rainbowsea.yupao.one;


import com.rainbowsea.yupao.mapper.UserMapper;
import com.rainbowsea.yupao.model.User;
import com.rainbowsea.yupao.service.UserService;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RunWith(SpringRunner.class)
public class InsertUsers {


    @Resource
    private UserService userService;

    // CPU 密集型,分配的核心线程数 = CPU -1
    // IO 密集型，分配的核心线程数可以大于 CPU 核数
    private ExecutorService executorService = new ThreadPoolExecutor(16, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 批量插入用户
     */
    // @Scheduled(fixedDelay = 5000, fixedRate = Long.MAX_VALUE)
    @Test
    public void doInsertUsers() {

        StopWatch stopWatch = new StopWatch();  // 监视查看内容信息
        stopWatch.start();  // 开始时间点

        final int INSERT_NUM = 10000;  // shift + ctrl + u 切换为大写

        List<CompletableFuture<Void>> futureList = new ArrayList<>();  // 列表

        // 分 10 组
        int j = 0;
        for (int i = 0; i < INSERT_NUM; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;

                User user = new User();
                user.setUsername("假鱼皮");
                user.setUserAccount("fakeuyupi");
                user.setAvatarUrl("https://profile-avatar.csdnimg.cn/2f8ef0ed68a647bcad04088152e00c69_weixin_61635597.jpg!1");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("123");
                user.setEmail("123@qq.com");
                user.setTags("[]");
                user.setUserStatus(0);
                user.setIsDelete(0);
                user.setPlanetCode("1111111");

                userList.add(user);

                if(j % 10000 == 0) {
                    break;
                }
            }

            // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName:" + Thread.currentThread().getName());
                userService.saveBatch(userList, 1000);
            },executorService);

            futureList.add(future);

        }

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();


        stopWatch.stop(); // 结束时间点
        System.out.println(stopWatch.getTotalTimeMillis()); // 打印显示提示信息


    }
}
