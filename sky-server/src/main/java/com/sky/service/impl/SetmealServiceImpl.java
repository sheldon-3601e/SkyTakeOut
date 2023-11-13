package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.controller.admin.SetmealController;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName setmealServiceImpl
 * @Author 26483
 * @Date 2023/11/13 14:31
 * @Version 1.0
 * @Description SetmealService
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void saveSetmeal(SetmealDTO setmealDTO) {
        //添加套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);
        Long id = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            //添加套餐菜品对应关系
            setmealDish.setSetmealId(id);
            setmealDishMapper.save(setmealDish);
        });

    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //查询套餐是否为在售状态
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getSetmealById(id);
            if (setmeal.getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //删除套餐
        setmealMapper.deleteBatch(ids);

        //删除套餐和菜品的对应关系
        setmealDishMapper.deleteBatchBySetmealIds(ids);
    }

    @Override
    @Transactional
    public SetmealVO getSetmealById(Long id) {
        //查询套餐本身
        Setmeal setmeal = setmealMapper.getSetmealById(id);

        //查询套餐关联菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        //组装成返回值
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //提取信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        //修改套餐信息
        setmealMapper.update(setmeal);

        //修改套餐菜品关系表
        //删除旧的关系
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(setmeal.getId());
        setmealDishMapper.deleteBatchBySetmealIds(longs);
        //插入新的关系
        setmealDishes.forEach( setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.save(setmealDish);
        });
    }

    @Override
    @Transactional
    public void updateStatus(Integer status, Long id) {
        if (status.equals(StatusConstant.ENABLE)) {
            // 套餐内有停售商品，不能上架
            // 查出菜品ID
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
            // 查出菜品状态
            for (SetmealDish setmealDish : setmealDishes) {
                Dish dish = dishMapper.selectDishById(setmealDish.getDishId());
                if (dish.getStatus().equals(StatusConstant.DISABLE)) {
                    // 如果是停售，抛出异常
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }

        // 没有异常，正常修改状态
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
        System.out.println();
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
