package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品的分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     *
     * @param dishIds
     */
    void deleteBatch(ArrayList<Long> dishIds);

    /**
     * 根据主键获取菜品详细信息
     *
     * @param id
     * @return
     */
    DishVO getDishWithFlavorByDishId(Long id);

    /**
     * 修改菜品和口味信息
     *
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId
     * @return
     */
    List<Dish> getDishByCategoryId(Long categoryId);

    /**
     * 条件查询菜品和口味
     *
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 菜品的起售停售
     * @param id
     * @param status
     */
    void updateStatus(Long id, int status);
}
