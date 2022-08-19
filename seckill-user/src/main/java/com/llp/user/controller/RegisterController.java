package com.llp.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 *  注册controller
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
    private static Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    UserService userService;


    @RequestMapping("/to_register")
    public String toRegister() {
        return "reg";
    }

    @RequestMapping("/do_register")
    @ResponseBody
    public Result<String> doRegister(HttpServletResponse response, @Valid RegVo regVo) {//加入JSR303参数校验
        log.info(regVo.toString());
        if(userService.register(response, regVo)){
            return Result.success("注册成功");
        }
        return Result.error(CodeMsg.REGISTER_ERROR);

    }
}