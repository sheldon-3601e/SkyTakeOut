package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
