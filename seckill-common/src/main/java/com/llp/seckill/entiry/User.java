package com.llp.seckill.entiry;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *  用户信息
 */
@Getter
@Setter
public class User {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;

}
