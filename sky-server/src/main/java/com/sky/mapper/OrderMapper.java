package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName OrderMapper
 * @Author 26483
 * @Date 2023/11/15 23:12
 * @Version 1.0
 * @Description OrderMapper
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入数据
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询包括详细信息
     *
     * @param orders
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO orders);
}
