package com.atheima.reggie.controller;

import com.atheima.reggie.common.R;
import com.atheima.reggie.entity.User;
import com.atheima.reggie.service.UserService;
import com.atheima.reggie.utils.SMSUtils;
import com.atheima.reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 创建一个User 实现验证码登录
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 发送手机验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
         //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isEmpty(phone)){
            //利用工具类ValidateCode 生成四位验证码
           String integer = ValidateCodeUtils.generateValidateCode(4).toString();
           log.info("生成的验证码：{}",integer);
            //调用阿里云提供的接口API发送短信
           // SMSUtils.sendMessage("橙的陈子","",phone,integer);
            //需要将生成的验证码保存到session当中
             session.setAttribute(phone,integer);
             return R.success("手机验证码发送成功");
        }
             return R.error("手机验证码发送失败");
    }

    @PostMapping("/login")
    public R<User>login(@RequestBody Map map,HttpSession session){
        //获取页面中的手机号
        String phone = map.get("phone").toString();
        //获取页面填入的验证码
        Object code = map.get("code");
        //和session中存的验证码进行比对，是否登录成功
        User one = new User();
        if (code!=null && session.getAttribute("phone") == code){
            //登录成功后 查看是新用户还是老用户，新用户就将用户信息添加到表当中去
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            one = userService.getOne(lambdaQueryWrapper);
            if (one==null){
                one.setPhone(phone);
                userService.save(one);
            }
            session.setAttribute("user",one.getId());
            //页面返回用户
            return R.success(one);
        }
        //登录失败
        return R.error("验证码错误");
    }
}
