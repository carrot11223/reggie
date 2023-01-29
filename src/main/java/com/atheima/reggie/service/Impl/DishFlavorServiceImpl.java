package com.atheima.reggie.service.Impl;

import com.atheima.reggie.entity.DishFlavor;
import com.atheima.reggie.mapper.DishFlavorMapper;
import com.atheima.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
