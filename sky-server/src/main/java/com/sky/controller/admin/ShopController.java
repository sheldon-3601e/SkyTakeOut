package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ShopController
 * @Author 26483
 * @Date 2023/11/12 15:23
 * @Version 1.0
 * @Description 店铺相关接口
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String key = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation(value = "设置店铺状态")
    public Result setStatus(@PathVariable("status") Integer status){
        log.info("设置店铺状态为：{}",status == 1 ? "营业" : "打烊");

        redisTemplate.opsForValue().set(key, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation(value = "获取店铺状态")
    public Result<Integer> getStatus(){

        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(key);
        log.info("获取店铺状态:{}", shopStatus == 1 ? "营业" : "打烊");

        return Result.success(shopStatus);
    }

}
