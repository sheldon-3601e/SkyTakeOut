package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from sky_take_out.dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 加入菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insertDish(Dish dish);

    /**
     * 菜品的分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID查询菜品
     * @param dishId
     * @return
     */
    @Select("select * from sky_take_out.dish where id = #{dishId}")
    Dish selectDishById(Long dishId);


    /**
     * 根据ID删除菜品
     * @param dishId
     */
    @Delete("delete from sky_take_out.dish where id = #{dishId}")
    void deleteById(Long dishId);

    /**
     * 根据主键批量删除
     * @param dishIds
     */
    void deleteBatchById(ArrayList<Long> dishIds);

    /**
     * 根据主键获取菜品详细信息
     * @param id
     * @return
     */
    DishVO getDishById(Long id);

    /**
     * 根据ID动态更改菜品信息
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return
     */
    @Select("select * from sky_take_out.dish where category_id = #{categoryId};")
    List<Dish> getDIshByCategoryId(Long categoryId);

    /**
     * 根据查询条件，动态查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
