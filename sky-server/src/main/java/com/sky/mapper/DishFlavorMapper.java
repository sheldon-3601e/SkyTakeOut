package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName DishFlavorMapper
 * @Author 26483
 * @Date 2023/11/10 19:40
 * @Version 1.0
 * @Description 菜品口味表
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

}
