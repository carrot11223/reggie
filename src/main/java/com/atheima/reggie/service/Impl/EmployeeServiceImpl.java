package com.atheima.reggie.service.Impl;

import com.atheima.reggie.entity.Employee;
import com.atheima.reggie.mapper.EmployeeMapper;
import com.atheima.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{
}
