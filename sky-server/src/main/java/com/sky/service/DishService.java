package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    /**
     * 菜品的分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param dishIds
     */
    void deleteBatch(ArrayList<Long> dishIds);
}
