package com.atheima.reggie.controller;

import com.atheima.reggie.common.R;
import com.atheima.reggie.dto.SetmealDto;
import com.atheima.reggie.entity.Category;
import com.atheima.reggie.entity.Setmeal;
import com.atheima.reggie.service.CategoryService;
import com.atheima.reggie.service.SetmealDishService;
import com.atheima.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    SetmealDishService dishService;
    @Autowired
    SetmealService setmealService;
    @Autowired
    CategoryService categoryService;
    /**
     * 新增套餐
     * @param setmealDto
     */
    @PostMapping
    public R<String> saveSetMeal(@RequestBody SetmealDto setmealDto){
        R<String> stringR = setmealService.saveWithDishes(setmealDto);
        return stringR;
    }
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //创建分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        //创建对象
        SetmealDto setmealDto = new SetmealDto();
        Page<SetmealDto> pageDto = new Page<>();
        //创建条件选择器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //条件查询
        lambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getCreateTime);
        setmealService.page(pageInfo,lambdaQueryWrapper);
        //对象拷贝 除了records属性
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> collect = records.stream().map((item) -> {
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            String name1 = byId.getName();
            setmealDto.setCategoryName(name1);
            return setmealDto;
        }).collect(Collectors.toList());
        pageDto.setRecords(collect);
        return R.success(pageDto);
    }
}
