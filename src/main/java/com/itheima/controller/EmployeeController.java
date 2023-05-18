package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Employee;
import com.itheima.service.IEmployeeService;
import com.itheima.service.impl.EmployeeServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-03-12
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService service;


    /**
     * 访问路径为login,访问方式为post
     *
     * @param request  用于在登录成功之后将用户名密码存入session中
     * @param employee 将传过来的用户名密码封装成employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将业面提交的密码用md5进行加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据业面提交的用户名进行查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = service.getOne(queryWrapper);


        //3.如果没有查询到返回登录结果失败
        if (emp == null) {
            return R.error("登录失败");
        }

        //4.比对密码，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        //6.登录成功，将员工id存入session并返回登录结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出功能
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        /**
         *
         *
         */
        //1.将员工的id从session中移除
        request.removeAttribute("employee");
        //2.返回结果
        return R.success("退出成功！");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        //System.out.println(employee);

        //1.根据姓名查询数据库，看是否存在，如果存在，则添加失败
        /*LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getName,employee.getName());
        Employee query = service.getOne(queryWrapper);
        System.out.println(query);
        if (query != null){
            return R.error("重复添加用户，添加失败！");
        }*/
        //2.如果不存在，将剩余属性添加进去，保存，返回保存成功结果
        String initPassword = "123456";//初始密码都设置为123456
        initPassword = DigestUtils.md5DigestAsHex(initPassword.getBytes());
        employee.setPassword(initPassword);//设置密码
        employee.setStatus(1);//设置状态，1为可使用

        LocalDateTime nowTime = LocalDateTime.now();//获取当前时间
//        employee.setCreateTime(nowTime);
//        employee.setUpdateTime(nowTime);

        //获取session中的id，以获得createUser
        Long createUserId = (Long) request.getSession().getAttribute("employee");
        System.out.println(createUserId);
//        employee.setCreateUser(createUserId);
//        employee.setUpdateUser(createUserId);

        service.save(employee);


        //System.out.println(employee);

        return R.success("添加成功");
    }

    /**
     * 员工信息的分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> getByCondition(Integer page, Integer pageSize, String name) {
        System.out.println("page = " + page);
        System.out.println("pageSize = " + pageSize);
        System.out.println("name = " + name);

        //1.创建Page对象
        Page pageInfo = new Page(page, pageSize);//page里要把参数传递进去
        //2.创建条件查询构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        //模糊查询，且在name不为空下
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);//注意，此处的StringUtils得是common包下才可以使用isNotEmpty方法
        //添加查询条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);//按照更新时间，降序查询(从最近的查到最久远的)
        //3.查询
        service.page(pageInfo, queryWrapper);//这里不用返回值，在page方法内部会直接将数据封装到pageInfo
        return R.success(pageInfo);
    }

    /**
     * 更新员工的方法，不止是更新状态，也可以编辑员工中的更改
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateStatus(HttpServletRequest request, @RequestBody Employee employee) {
        System.out.println(Thread.currentThread().getId());
        //注意：前端的long型数据到后端后丢失精度
        //可以在将java对象序列化时将long型序列化为字符串形式
        //具体操作为：在Employee类中id字段上加注解@JsonSerialize(using = ToStringSerializer.class)
        System.out.println(employee);
        Long id = (Long) request.getSession().getAttribute("employee");
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
//        employee.setUpdateUser(id);
//        employee.setUpdateTime(LocalDateTime.now());

        service.updateById(employee);
        return R.success("修改完成！");
    }

    /**
     * 在编辑员工时先查询员工，返回到前端
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        //查询
        Employee employee = service.getById(id);

        if (employee != null) {
            return R.success(employee);
        }
        return R.error("用户不存在");
    }
}

