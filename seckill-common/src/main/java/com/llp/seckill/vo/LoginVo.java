package com.llp.seckill.vo;


import com.llp.seckill.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 *  登录页VO
 */
@Getter
@Setter
@ToString
public class LoginVo {

    @NotNull
    @IsMobile  //因为框架没有校验手机格式注解，所以自己定义
    private String mobile;

    @NotNull
    private String password;

}
