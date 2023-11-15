package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ClassName ShoppingCartMapper
 * @Author 26483
 * @Date 2023/11/15 1:48
 * @Version 1.0
 * @Description ShoppingCartMapper
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据ID修改数量
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where user_id = #{userId}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart" +
            "(name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time)" +
            "values " +
            "(#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void add(ShoppingCart shoppingCart);
}
