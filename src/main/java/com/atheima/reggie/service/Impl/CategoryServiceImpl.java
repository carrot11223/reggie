package com.atheima.reggie.service.Impl;

import com.atheima.reggie.common.CustomException;
import com.atheima.reggie.entity.Category;
import com.atheima.reggie.entity.Dish;
import com.atheima.reggie.entity.Setmeal;
import com.atheima.reggie.mapper.CategoryMapper;
import com.atheima.reggie.service.CategoryService;
import com.atheima.reggie.service.DishService;
import com.atheima.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(lambdaQueryWrapper);
        if (count1>0) {
            //抛出业务处理异常
        throw new CustomException("当前菜品关联了该菜品分类");
        }

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Setmeal::getCategoryId,id);
        int count = setmealService.count(lambdaQueryWrapper1);
        if (count>0) {
            //抛出一个业务处理异常
            throw new CustomException("当前套餐关联了该菜品分类");
        }
    }
}
