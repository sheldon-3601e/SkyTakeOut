package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName setmealController
 * @Author 26483
 * @Date 2023/11/13 11:11
 * @Version 1.0
 * @Description 套餐相关接口
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api("套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("修改店铺状态")
    public Result updateStatus(
            @PathVariable("status") Integer status,
            @RequestParam("id") Long id) {
        setmealService.updateStatus(status,id);

        return Result.success();
    }

    @PutMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("修改套餐")
    public Result<SetmealVO> getSetmealById(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐:{}", setmealDTO);

        setmealService.update(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐")
    public Result<SetmealVO> getSetmealById(@PathVariable("id") Long id) {
        log.info("根据ID查询套餐:{}", id);

        SetmealVO setmealVO = setmealService.getSetmealById(id);
        return Result.success(setmealVO);
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("删除套餐")
    public Result deleteBatch(@RequestParam("ids") List<Long> ids) {
        log.info("删除套餐:{}", ids);

        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("添加套餐")
    @CachePut(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result saveSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("添加套餐:{}", setmealDTO);

        setmealService.saveSetmeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询:{}", setmealPageQueryDTO);

        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

}
