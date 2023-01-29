package com.atheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.atheima.reggie.common.BaseContext;
import com.atheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获取当前路径
        String requestURI = request.getRequestURI();
        //不需要处理的路径
        String urls[] = new String[]{"/backend/**","/front/**","/employee/login","/employee/logout","/common/**","/user/login","/user/sendMsg"};
        //判断是否需要处理
        log.info("当前请求路径{}",request.getRequestURI());
        if(check(urls,requestURI)){
            log.info("当前路径{}不需要处理",request.getRequestURI());
            filterChain.doFilter(request,response);
            return;
        }
        //需要处理，是否登录
        log.info("当前路径{}需要处理",request.getRequestURI());
        if (request.getSession().getAttribute("employee") != null){
            Long id =(Long) request.getSession().getAttribute("employee");
            BaseContext.set(id);
            filterChain.doFilter(request,response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    //封装方法，判断是否不需要检查这个路径
    public boolean check(String urls[],String requestURI){
        for (String url : urls) {
           if (PATH_MATCHER.match(url,requestURI)){
               return true;
           }
        }
        return false;

    }
}
