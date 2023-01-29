package com.atheima.reggie.controller;

import com.atheima.reggie.common.R;
import com.atheima.reggie.dto.DishDto;
import com.atheima.reggie.entity.Category;
import com.atheima.reggie.entity.Dish;
import com.atheima.reggie.service.CategoryService;
import com.atheima.reggie.service.DishFlavorService;
import com.atheima.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;
    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
}

   @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
       Page<Dish> pageInfo = new Page<>(page,pageSize);
       Page<DishDto> dishDtoPage = new Page<>();

       LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.like(name != null,Dish::getName,name);

       queryWrapper.orderByDesc(Dish::getUpdateTime);


       dishService.page(pageInfo,queryWrapper);


       BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

       List<Dish> records = pageInfo.getRecords();

       List<DishDto> list = records.stream().map((item) -> {
           DishDto dishDto = new DishDto();

           BeanUtils.copyProperties(item,dishDto);

           Long categoryId = item.getCategoryId();

           Category category = categoryService.getById(categoryId);

           if(category != null){
               String categoryName = category.getName();
               dishDto.setCategoryName(categoryName);
           }
           return dishDto;
       }).collect(Collectors.toList());

       dishDtoPage.setRecords(list);

       return R.success(dishDtoPage);
   }

    /**
     * 根据id查询Dish以及DishDto
     * @return
     */
   @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
       DishDto withFlavors = dishService.getWithFlavors(id);
       return R.success(withFlavors);
   }

    /**
     * 根据条件来查询菜品
     * @param dish
     * @return
     */
   @GetMapping("/list")
   public R<List<Dish>> list(Dish dish){
       //构造条件查询
       LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
       lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
       //构造条件查询
       lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

       List<Dish> list = dishService.list(lambdaQueryWrapper);
       return R.success(list);

   }
}
