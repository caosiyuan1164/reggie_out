package com.itheima.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegisterEmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail,String code){
        // 验证真的使用我们自己线程
        System.out.println(Thread.currentThread().getName());
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置发件人账号
        message.setFrom(fromEmail);
        // 设置收件人账号
        message.setTo(toEmail);
        // 设置邮件标题
        message.setSubject("【Reggie外卖】注册激活邮件");
        // 设置邮件内容
        String content = "您的验证码为:" + code + "，请注意，您的验证码有效期为5分钟";
        message.setText(content);
        javaMailSender.send(message);
    }
}
