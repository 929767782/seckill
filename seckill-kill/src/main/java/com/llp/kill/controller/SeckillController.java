package com.llp.kill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.llp.common.entity.SeckillOrder;
import com.llp.common.entity.User;
import com.llp.common.result.CodeMsg;
import com.llp.common.result.Result;
import com.llp.kill.rabbitmq.MQSender;
import com.llp.kill.rabbitmq.SeckillMessage;
import com.llp.kill.redis.GoodsKey;
import com.llp.kill.redis.RedisService;
import com.llp.kill.remote.GoodsService;
import com.llp.kill.remote.OrderService;
import com.llp.kill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *  秒杀controller
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController{


    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    //基于令牌桶算法的限流实现类
    RateLimiter rateLimiter = RateLimiter.create(10);

    //做标记，判断该商品是否被处理过了
    private Set<String> localOverSet = new HashSet<>();

    /**
     * GET POST
     * 1、GET幂等,服务端获取数据，无论调用多少次结果都一样
     * 2、POST，向服务端提交数据，不是幂等
     * <p>
     * 将同步下单改为异步下单
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, User user, @RequestParam("goodsId") long goodsId, @RequestParam("randomCode") String randomCode) {

        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return  Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        GoodsVo goodsVo = redisService.get(GoodsKey.getUploadGoods, "" + goodsId, GoodsVo.class);
        if(!goodsVo.getRandomCode().equals(randomCode)){
            return Result.error(CodeMsg.NOT_START);
        }
        model.addAttribute("user", user);
        //判断重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //内存标记，减少redis访问
        if(localOverSet.contains(randomCode)){
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getGoodsStock, randomCode);//10
        if (stock < 0) {
            localOverSet.add(randomCode);
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        sender.sendSeckillMessage(message);
        return Result.success(0);//排队中
    }



    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(orderId);
    }
}
