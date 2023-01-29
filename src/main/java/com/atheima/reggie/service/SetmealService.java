package com.atheima.reggie.service;

import com.atheima.reggie.common.R;
import com.atheima.reggie.dto.SetmealDto;
import com.atheima.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SetmealService extends IService<Setmeal> {
public R<String> saveWithDishes(SetmealDto setmealDto);
}
