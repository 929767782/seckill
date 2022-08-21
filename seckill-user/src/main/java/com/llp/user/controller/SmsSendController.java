package com.llp.user.controller;

import com.llp.common.result.CodeMsg;
import com.llp.common.result.Result;
import com.llp.common.utils.SmsUtil;
import com.llp.user.redis.CodeKey;
import com.llp.user.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/sms")
public class SmsSendController {

    @Resource
    private SmsUtil smsUtil;

    @Autowired
    RedisService redisService;

    /**
     * 提供给别的服务进行调用
     * @param mobile
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/sendCode")
    public Result<String> sendCode(@RequestParam("mobile") String mobile) {
        //接口防刷,在redis中缓存phone-code
        String v = redisService.get(CodeKey.code, mobile, String.class);

        if (!StringUtils.isEmpty(v)) {
            long pre = Long.parseLong(v.split("_")[1]);
            //如果存储的时间小于60s，说明60s内发送过验证码
            if (System.currentTimeMillis() - pre < 60000) {
                return Result.error(CodeMsg.CODE_ERROR);
            }
        }
        //如果存在的话，删除之前的验证码
        redisService.delete(CodeKey.code,mobile);
        //获取到6位数字的验证码
        String code = String.valueOf((int)((Math.random() + 1) * 100000));
        //在redis中进行存储并设置过期时间
        redisService.set(CodeKey.code,mobile,code+"_"+System.currentTimeMillis());
        //发送验证码
//        smsUtil.sendCode(phone,code);
        System.out.println(mobile+"_"+code);
        return Result.success("发送成功");
    }

}
