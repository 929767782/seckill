package com.llp.kill.schedule;

import com.llp.common.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SeckillScheduled {


    @Autowired
    private SeckillService secKillService;

    /*
        cron: 秒、分、时、日、月、星期、（年）
        *表示所有的值都必须要运行
        ?表示不关心这个值是什么，忽略这个条件

        1、【日期】与【星期几】两者为互斥的，不能同时设置。这个比较容易理解，假设日期设置为1，星期几也设置为1，那含义是什么：每月1日且为周一时触发，可能一年之中没用任何天能满足上述条件，更重要的是什么人能有这种xx的需求呢？

        2、【日期】与【星期几】中任何一个设置值（包含 *），另一个必需使用 ？来进行屏蔽，此时 ？起到屏蔽的作用。

        3、？只能出现在【日期】与【星期几】中，且不能同时出现
     */
    /**
     * 定时任务
     * 每天三点上架最近三天的秒杀商品
     */
    @Async//异步执行，从线程池中选择一个线程执行定时任务
    @Scheduled(cron = "0 0 3 * * ?")
    //@Scheduled(cron = "30 * * * * ?")
    public void uploadSeckillSkuLatest3Days() {
        secKillService.uploadSeckillSkuLatest3Days();
    }
}
