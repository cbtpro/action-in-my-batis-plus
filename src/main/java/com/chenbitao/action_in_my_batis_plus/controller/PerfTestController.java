package com.chenbitao.action_in_my_batis_plus.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenbitao.action_in_my_batis_plus.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/perf")
@RequiredArgsConstructor
public class PerfTestController {

    private final IService<User> userService;

    /**
     * 性能测试：批量插入用户数据
     *
     * @param total   插入总条数（例如 1_000_000 表示 100 万）
     * @param batch   每批插入条数（例如 1000）
     * @param threads 使用线程数（例如 4）
     */
    @PostMapping("/insert")
    public String insertUsers(@RequestParam(defaultValue = "1000000") long total,
                              @RequestParam(defaultValue = "1000") int batch,
                              @RequestParam(defaultValue = "4") int threads) throws InterruptedException {

        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        long totalBatches = total / batch;
        CountDownLatch latch = new CountDownLatch((int) totalBatches);

        for (long batchNo = 0; batchNo < totalBatches; batchNo++) {
            final long startId = batchNo * batch;
            executor.submit(() -> {
                try {
                    List<User> users = generateUsers(startId, batch);
                    userService.saveBatch(users, batch);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        long cost = System.currentTimeMillis() - start;
        return "✅ 插入完成，共插入 " + total + " 条，用时 " + cost + " ms";
    }

    /**
     * 批量生成用户数据
     */
    private List<User> generateUsers(long startId, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    String name = "user_" + (startId + i);
                    String email = name + "@example.com";
                    return User.builder()
                            .username(name)
                            .usernameReversal(new StringBuilder(name).reverse().toString())
                            .age(ThreadLocalRandom.current().nextInt(18, 60))
                            .email(email)
                            .emailReversal(new StringBuilder(email).reverse().toString())
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .deleted(0)
                            .build();
                })
                .toList();
    }
}