package com.atheima.reggie.service.Impl;

import com.atheima.reggie.common.R;
import com.atheima.reggie.dto.DishDto;
import com.atheima.reggie.entity.Dish;
import com.atheima.reggie.entity.DishFlavor;
import com.atheima.reggie.mapper.DishMapper;
import com.atheima.reggie.service.DishFlavorService;
import com.atheima.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService  {
   @Autowired
   DishFlavorService dishFlavorService;
    /**
     * 新增菜品，保存口味
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //将基本信息先保存到菜品表当中
        this.save(dishDto);
        //菜品id
        Long categoryId = dishDto.getCategoryId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(categoryId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @Override
    public DishDto getWithFlavors(Long id) {
        Dish byId = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId,dishDto);
        //条件查询菜品口味
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getId, id);
        List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }
}
