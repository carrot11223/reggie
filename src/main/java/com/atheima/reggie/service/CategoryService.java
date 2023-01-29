package com.atheima.reggie.service;

import com.atheima.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
