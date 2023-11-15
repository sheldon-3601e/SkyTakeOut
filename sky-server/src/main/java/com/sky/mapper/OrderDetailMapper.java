package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}
