package com.atheima.reggie.controller;

import com.atheima.reggie.common.R;
import com.atheima.reggie.entity.Category;
import com.atheima.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增菜品成功");
    }

    /**
     * 实现分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Category> eq = lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete(Long id){
    //categoryService.removeById(id);
        categoryService.remove(id);
    return R.success("删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
   @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("更新成功");
 }

    /**
     * 根据条件查询菜品分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
       //创建条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        LambdaQueryWrapper<Category> lambdaQueryWrapper1 = lambdaQueryWrapper.orderByDesc(Category::getSort).orderByAsc(Category::getCreateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
