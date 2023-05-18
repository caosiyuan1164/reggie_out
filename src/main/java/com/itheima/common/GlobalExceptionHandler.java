package com.itheima.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@ControllerAdvice(annotations = {RestController.class, Controller.class})//指定异常处理器要处理的类
@ResponseBody//为了能将返回值以json形式返回到前端
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        System.out.println("异常处理器启动！");
        System.out.println(ex.getMessage());

        //1.判断message中是否包含Duplicate entry，因为该异常不一定都是重复键入不可重复的属性值
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] strs = message.split(" ");
            String username = strs[2];
            return R.error(username + "已被使用！");
        }
        return R.error("其他异常");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        System.out.println("异常处理器启动！");
        System.out.println(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
