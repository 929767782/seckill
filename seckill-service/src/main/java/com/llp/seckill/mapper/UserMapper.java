package com.llp.seckill.mapper;


import com.llp.seckill.entiry.User;
import org.apache.ibatis.annotations.*;

/**
 *   用户相关的Mapper
 */
@Mapper
public interface UserMapper {

    @Select("select * from sk_user where id = #{id}")
    public User getById(@Param("id") long id);

    @Select("select * from sk_user where nickname = #{loginAccount} or mobile = #{loginAccount}")
    public User getByLoginAccount(@Param("loginAccount") String loginAccount);

    @Update("update sk_user set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);

    @Insert("insert into sk_user(nickname,password,salt,mobile,register_date) values(#{nickname},#{password},#{salt},#{mobile},#{registerDate})")
    public void save(User toBeSave);

    @Select("select count(*) from sk_user where mobile = #{mobile}")
    public long selectMobileCount(String mobile);

    @Select("select count(*) from sk_user where nickname = #{nickname}")
    public long selectNicknameCount(String nickname);


}

