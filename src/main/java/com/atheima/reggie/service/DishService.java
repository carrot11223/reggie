package com.atheima.reggie.service;

import com.atheima.reggie.common.R;
import com.atheima.reggie.dto.DishDto;
import com.atheima.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);
    public DishDto getWithFlavors(Long id);
}
