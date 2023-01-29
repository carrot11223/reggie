package com.atheima.reggie.service.Impl;

import com.atheima.reggie.common.R;
import com.atheima.reggie.dto.SetmealDto;
import com.atheima.reggie.entity.Category;
import com.atheima.reggie.entity.Setmeal;
import com.atheima.reggie.entity.SetmealDish;
import com.atheima.reggie.mapper.SetmealMapper;
import com.atheima.reggie.service.CategoryService;
import com.atheima.reggie.service.SetmealDishService;
import com.atheima.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
   @Autowired
    SetmealDishService setmealDishService;

    /**
     * 根据传过来的数据 保存到相应的表中
     * @return
     */
    @Transactional
    public R<String> saveWithDishes(SetmealDto setmealDto) {
       this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
        return R.success("保存成功");
    }


}
