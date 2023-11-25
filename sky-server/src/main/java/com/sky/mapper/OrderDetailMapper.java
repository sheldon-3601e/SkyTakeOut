package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @ClassName OrderDetailMapper
 * @Author 26483
 * @Date 2023/11/15 23:12
 * @Version 1.0
 * @Description OrderDetailMapper
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入数据
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 根据订单号查询详情
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);
}
