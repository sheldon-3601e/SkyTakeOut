package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.stereotype.Service;

/**
 * @ClassName DishService
 * @Author 26483
 * @Date 2023/11/10 18:55
 * @Version 1.0
 * @Description 菜品服务
 */

public interface DishService {

    /**
     * 添加菜品和对应的口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
