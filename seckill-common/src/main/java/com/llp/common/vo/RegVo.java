package com.llp.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RegVo {
    @NotEmpty(message = "用户名不能为空")
    @Length(min=6,max=19,message = "用户名长度在6—18字符")
    private String nickname;

    @NotEmpty(message = "密码必须填写")
    private String password;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式不正确")
    private String mobile;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}
