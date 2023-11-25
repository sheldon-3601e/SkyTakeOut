package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @ClassName UserMapper
 * @Author 26483
 * @Date 2023/11/12 18:53
 * @Version 1.0
 * @Description UserMapper
 */
@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from sky_take_out.user where openid = #{openid}")
    User getByOpenId(String openid);

    /**
     * 插入用户
     * @param user
     */
    void insert(User user);

    @Select("select * from sky_take_out.user where id = #{userId}")
    User getById(Long userId);

    /**
     * 动态查询用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
