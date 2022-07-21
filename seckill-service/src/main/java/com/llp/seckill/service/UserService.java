package com.llp.seckill.service;


import com.llp.seckill.entiry.User;
import com.llp.seckill.exception.GlobalException;
import com.llp.seckill.mapper.UserMapper;
import com.llp.seckill.redis.CodeKey;
import com.llp.seckill.redis.RedisService;
import com.llp.seckill.redis.UserKey;
import com.llp.seckill.result.CodeMsg;
import com.llp.seckill.utils.MD5Util;
import com.llp.seckill.utils.UUIDUtil;
import com.llp.seckill.vo.LoginVo;
import com.llp.seckill.vo.RegVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *   用户服务类
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    public User getById(long id) {
        //对象缓存
        User user = redisService.get(UserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userMapper.getById(id);
        //再存入缓存
        if (user != null) {
            redisService.set(UserKey.getById, "" + id, user);
        }
        return user;
    }

    /**
     * 典型缓存同步场景：更新密码
     */
    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        User user = getById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        userMapper.update(toBeUpdate);
        //更新缓存：先删除再插入
        redisService.delete(UserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(UserKey.token, token, user);
        return true;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成唯一id作为token
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }
    public boolean register(HttpServletResponse response, RegVo regVo) {
        if (regVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String code = redisService.get(CodeKey.code,regVo.getMobile(),String.class);
        if(!StringUtils.isEmpty(code) && regVo.getCode().equals(code.split("_")[0])){
            redisService.delete(CodeKey.code,regVo.getMobile());
            //1 检查电话号是否唯一
            if(!checkMobileUnique(regVo.getMobile())){
                return false;
            }
            //2 检查用户名是否唯一
            if(!checkNickNameUnique(regVo.getNickname())){
                return false;
            }
            //3 该用户信息唯一，进行插入
            User entity = new User();
            //3.1 保存基本信息
            entity.setNickname(regVo.getNickname());
            entity.setMobile(regVo.getMobile());
            entity.setRegisterDate(new Date());
            //3.2 使用加密保存密码
            String salt = UUIDUtil.uuid().substring(0,8);
            entity.setSalt(salt);
            String calcPass = MD5Util.formPassToDBPass(regVo.getPassword(), salt);

            entity.setPassword(calcPass);

            // 4 保存用户信息
            userMapper.save(entity);
            return true;
        }
        return false;

    }

    public boolean checkNickNameUnique(String nickname) {
        return userMapper.selectNicknameCount(nickname)==0;
    }

    private boolean checkMobileUnique(String mobile) {
        return userMapper.selectMobileCount(mobile)==0;
    }

    /**
     * 将token做为key，用户信息做为value 存入redis模拟session
     * 同时将token存入cookie，保存登录状态
     */
    public void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");//设置为网站根目录
        response.addCookie(cookie);
    }

    /**
     * 根据token获取用户信息
     */
    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期，有效期等于最后一次操作+有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

}
