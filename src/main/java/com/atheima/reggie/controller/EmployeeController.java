package com.atheima.reggie.controller;

import com.atheima.reggie.common.R;
import com.atheima.reggie.entity.Employee;
import com.atheima.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> getEmployee(HttpServletRequest request, @RequestBody Employee employee){
        //1.对用户输入的密码进行加密
        String password = employee.getPassword();
        String s = DigestUtils.md5DigestAsHex(password.getBytes());
        password = s;
        //2.根据用户的名称，在数据库中查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee one = employeeService.getOne(wrapper);
        //3.返回结果为null，没查到的话
        if (one==null) {
            return R.error("登录失败");
        }
        //4.密码比对
        if (!one.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //5.查看登录的状态，是否被禁用
        if (one.getStatus()==0) {
            return R.error("账号已被禁用");
        }
        //6.登录成功，将用户id 存入session，并返回登录成功的结果
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
       log.info("新增的员工信息{}",employee.toString());
       employee.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));
       Long id= (Long)request.getSession().getAttribute("employee");
       employee.setCreateTime(LocalDateTime.now());
       employee.setUpdateTime(LocalDateTime.now());
       employee.setCreateUser(id);
       employee.setCreateUser(id);
       employee.setUpdateUser(id);
        employeeService.save(employee);
        return R.success("保存员工信息成功");
    }

    /**
     * 员工信息的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //创建分页构造器
        Page pageInfo = new Page(page,pageSize);
        //创建条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (name!=null) {
            lambdaQueryWrapper.eq(Employee::getName,name);
        }
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateUser);
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
    log.info(employee.toString());
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
@GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
    Employee byId = employeeService.getById(id);
    if (byId!=null){
        return R.success(byId);
    }
   return R.error("没有查询到");
}
}
