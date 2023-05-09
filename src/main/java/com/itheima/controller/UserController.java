package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.R;
import com.itheima.common.RegisterEmailService;
import com.itheima.domain.User;
import com.itheima.domain.UserCode;
import com.itheima.service.IUserCodeService;
import com.itheima.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-04-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RegisterEmailService mailService;

    @Autowired
    private IUserCodeService userCodeService;

    @Autowired
    private IUserService service;

    @GetMapping("/mail")
    public void sendMail(){
        System.out.println("发送邮件了");
        mailService.sendEmail("q15052702519@163.com","475523");
    }

    @PostMapping("/login")
    public R<String> login(HttpServletRequest request, @RequestBody UserCode userCode){
        //首先打印一下userCode
        System.out.println(userCode);
        //根据service查询一下是否有这个号码和code的对应关系
        LambdaQueryWrapper<UserCode> queryWrapper = new LambdaQueryWrapper();
        //条件查询
        queryWrapper.eq(UserCode::getPhone,userCode.getPhone()).eq(UserCode::getCode,userCode.getCode());
        UserCode res = userCodeService.getOne(queryWrapper);
        if (res != null){//查到对应关系
            //判断当前时间与验证码的获取时间是否相差超过5分钟
            LocalDateTime nowTime = LocalDateTime.now();
            long nowTimeLong = Timestamp.valueOf(nowTime).getTime();
            long createTimeLong = Timestamp.valueOf(res.getCreateTime()).getTime();
            if ((nowTimeLong - createTimeLong) >= 5 * 60 * 1000){
                userCodeService.removeById(res.getId());
                return R.error("验证码已过期，请重新获取验证码");
            }
        }else {//未查到对应关系
            return R.error("验证码错误");
        }
        //通过上面的所有，就证明通过验证，可以登录了

        //保存
        //首先要查询号码在表中是否有对应记录，如果有，不需保存，并将对应id存到session中，如果没有，则存入
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,res.getPhone());
        User user = service.getOne(userLambdaQueryWrapper);
        if (user == null){
            //查到的用户为空
            //将对应的电话号码存成用户存到用户表中去
            user = new User();
            user.setPhone(res.getPhone());
        }

        //设置session
        request.getSession().setAttribute("user",user.getId());
        return R.success("登录成功！");
    }

    /**
     * 发送验证码的方法
     * @return
     */
    @PostMapping("/getCode/{phone}")
    public R<String> getCode(@PathVariable String phone){
        //0.首先检验表中是否已经存在该号码，如果存在该号码，则删除该记录
        LambdaQueryWrapper<UserCode> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(UserCode::getPhone,phone);
        userCodeService.remove(queryWrapper);
        //1.首先随机生成验证码。
        Random random = new Random();
        int sum = 0;
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(9);
            sum = sum*10 + num;
        }
        String code = Integer.toString(sum);
        if (code.length() < 6){
            int diff = 6 - code.length();
            String prefix = "";
            for (int i = 0; i < diff; i++) {
                prefix += "0";
            }
            code = prefix + code;
        }
        System.out.println(code);
        //2.发送验证码到指定邮箱
        mailService.sendEmail(phone,code);//得先能发出去，再存，如果不能发出去，那么存到数据库中的是无效数据
        //3.将之存到对应表中
        UserCode userCode = new UserCode();
        userCode.setPhone(phone);
        userCode.setCode(code);
        userCode.setCreateTime(LocalDateTime.now());
        userCodeService.save(userCode);
        //4.返回
        return R.success("发送成功！");
    }
}

