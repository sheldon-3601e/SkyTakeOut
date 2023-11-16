package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @Select("select * from sky_take_out.setmeal_dish where dish_id = #{dishId}")
    SetmealDish selectByDishId(Long dishId);

    /**
     * 根据DishId查询数量
     * @param dishId
     * @return
     */
    @Select(("select count(*) from sky_take_out.setmeal_dish where dish_id = #{dishId}"))
    Integer countByDishId(Long dishId);

    /**
     * 添加套餐菜品对应关系
     * @param setmealDish
     */
    @Insert("insert into sky_take_out.setmeal_dish" +
            "(setmeal_id, dish_id, name, price, copies) " +
            "values" +
            "(#{setmealId}, #{dishId}, #{name}, #{price}, #{copies})")
    void save(SetmealDish setmealDish);

    /**
     * 根据套餐ID批量删除
     * @param ids
     */
    void deleteBatchBySetmealIds(List<Long> ids);

    /**
     * 根据套餐ID获取关系
     *
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);
}
