package com.itheima.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.common.BaseContext;
import com.itheima.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //用于匹配通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        System.out.println(Thread.currentThread().getId());
        //1.首先获得uri
        String uri = request.getRequestURI();
        System.out.println("拦截到请求：" + uri);
        //2.本次请求是否需要处理
        //不需要处理的url的数组
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/getCode/**"
        };
        //3.如果不需要处理，直接放行
        boolean flag = check(uri,urls);
        System.out.println(flag);
        if (check(uri,urls)){
            filterChain.doFilter(request,response);
            return;
        }
        //4.判断登录状态，如果已经登录，直接放行
        HttpSession session = request.getSession();
        Object id = session.getAttribute("employee");
        Object user = session.getAttribute("user");
        if (id != null){
            System.out.println("用户已登录，用户id:" + id);
            BaseContext.setCurrentId((Long)id);
            filterChain.doFilter(request,response);
            return;
        }
        //判断移动端的用户的登录状态
        if (user != null){
            System.out.println("用户已登录，用户id:" + user);
            BaseContext.setCurrentId((Long)user);
            filterChain.doFilter(request,response);
            return;
        }
        //5.如果未登录返回未登录结果
        System.out.println("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 用于匹配请求的uri是否是不需要处理的uri
     * @param uri
     * @param urls
     * @return
     */
    public boolean check(String uri,String[] urls){
        for (String url : urls) {
            if(PATH_MATCHER.match(url,uri)){//第一个是pattern，就是含有通配符的，必须写前面
                //匹配上了，不需要处理，直接放行
                return true;
            }
        }
        return false;
    }
}
