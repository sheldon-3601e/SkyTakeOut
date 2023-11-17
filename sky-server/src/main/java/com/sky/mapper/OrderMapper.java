package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /**
     * 根据订单ID查询订单
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.orders where id = #{id}")
    Orders getById(Long id);


    /**
     * 修改订单支付状态
     * @param id
     * @param cancelled
     */
    @Update("update sky_take_out.orders set pay_status = #{cancelled} where id = #{id};")
    void setPayStatus(Long id, Integer cancelled);

    /**
     * 管理端查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQueryByAdmin(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计各个状态的订单数量
     * @param status
     * @return
     */
    @Select("select count(id) from sky_take_out.orders where status = #{status}")
    Integer countStatus(int status);
}
