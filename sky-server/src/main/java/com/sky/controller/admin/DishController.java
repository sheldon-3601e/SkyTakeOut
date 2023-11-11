package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DishController
 * @Author 26483
 * @Date 2023/11/10 18:50
 * @Version 1.0
 * @Description 菜品的相关接口
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品的相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result saveDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);

        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("商品分页查询：{}",dishPageQueryDTO);

        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result deleteBatch(@RequestParam ArrayList<Long> ids) {
        log.info("批量删除菜品：{}",ids);

        dishService.deleteBatch(ids);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据Id查询菜品")
    public Result<DishVO> getDishById(@PathVariable("id") Long id) {
        log.info("根据Id查询菜品:{}",id);

        DishVO dishVO = dishService.getDishWithFlavorByDishId(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}",dishDTO);

        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

}
