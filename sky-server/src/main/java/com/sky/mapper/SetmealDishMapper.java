package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName SetmealDishMapper
 * @Author 26483
 * @Date 2023/11/10 21:57
 * @Version 1.0
 * @Description 套餐和菜品关联表
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据DIshId查询套餐和菜品关系
     * @param dishId
     * @return
     */
    @Select("select * from SetmealDish where dish_id = #{dishId}")
    SetmealDish selectByDishId(Long dishId);

    @Select(("select count(*) from setmeal_dish where dish_id = #{dishId}"))
    Integer countByDishId(Long dishId);
}
