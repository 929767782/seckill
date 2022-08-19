package com.llp.common.entity;

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
    private String mobile;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;

}
