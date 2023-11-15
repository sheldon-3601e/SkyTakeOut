package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

/**
 * @ClassName UserServiceImpl
 * @Author 26483
 * @Date 2023/11/12 18:31
 * @Version 1.0
 * @Description UserServiceImpl
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        String openid = getOpenId(userLoginDTO.getCode());

        //判断openId是否为空，如果为空，登陆失败
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 查询该用户是否为新用户
        User user = userMapper.getByOpenId(openid);

        // 不存在自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    /**
     * 调用微信接口服务，获取微信用户的openId
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        // 请求微信openId
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", weChatProperties.getAppid());
        hashMap.put("secret", weChatProperties.getSecret());
        hashMap.put("js_code", code);
        hashMap.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN, hashMap);

        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
