package com.llp.user.service;

import com.llp.common.entity.User;
import com.llp.common.exception.GlobalException;
import com.llp.common.result.CodeMsg;
import com.llp.common.utils.MD5Util;
import com.llp.common.utils.UUIDUtil;
import com.llp.common.vo.LoginVo;
import com.llp.common.vo.RegVo;
import com.llp.user.mapper.UserMapper;
import com.llp.user.redis.CodeKey;
import com.llp.user.redis.RedisService;
import com.llp.user.redis.UserKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.llp.common.constant.AuthServerConstant.COOKIE_NAME_TOKEN;

/**
 *   用户服务类
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;


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
    public boolean updatePassword(String token, String formPass) {
        //根据token取user
        User user = redisService.get(UserKey.token,token,User.class);
        if(user == null) {
            throw new GlobalException(CodeMsg.TOKEN_INVALID);
        }
        //更新数据库
        User toBeUpdate = new User();
        toBeUpdate.setId(user.getId());
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        userMapper.update(toBeUpdate);
        return true;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String loginAccount = loginVo.getLoginAccount();
        //以用户名或电话号登录的进行查询
        User user = userMapper.getByLoginAccount(loginAccount);
        if(user!=null){
            String loginPassword = MD5Util.formPassToDBPass(loginVo.getPassword(), user.getSalt());
            if(loginPassword.equals(user.getPassword())){
                //生成唯一id作为token
                String token = UUIDUtil.uuid();
                user.setPassword("");
                addCookie(response, token, user);
                return token;
            }else{
                throw new GlobalException(CodeMsg.PASSWORD_ERROR);
            }
        }
        throw new GlobalException(CodeMsg.LOGINACCOUNT_NOT_EXIST);

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
