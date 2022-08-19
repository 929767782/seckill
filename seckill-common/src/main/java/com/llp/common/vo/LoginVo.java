package com.llp.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *  登录页VO
 */
@Getter
@Setter
@ToString
public class LoginVo {


    @NotEmpty(message = "登录账户不能为空")
    private String loginAccount;

    @NotNull
    private String password;

}
