package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Author 26483
 * @Date 2023/11/12 18:30
 * @Version 1.0
 * @Description UserService
 */

public interface UserService {

    User wxLogin(UserLoginDTO userLoginDTO);

}
