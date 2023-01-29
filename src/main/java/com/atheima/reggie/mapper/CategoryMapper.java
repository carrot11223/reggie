package com.atheima.reggie.mapper;

import com.atheima.reggie.entity.Category;
import com.atheima.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
