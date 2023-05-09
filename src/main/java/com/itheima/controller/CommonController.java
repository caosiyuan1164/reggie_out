package com.itheima.controller;

import com.itheima.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会被删除
        System.out.println(file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取后缀名
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));//这样截取的是.jpg
        //使用uuid重新生成文件名，防止文件名重复造成覆盖
        String uuid = UUID.randomUUID().toString();

        //文件名，留给之后返回给业面使用
        String fileName = uuid + postfix;

        //创建一个目录对象
        File dir = new File(basePath);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(dir.getAbsolutePath() + "\\" + uuid + postfix));
        }catch (IOException e){
            e.printStackTrace();
        }

        return R.success(fileName);
    }


    /**
     * 文件下载功能，将上传的图片显示在网页上
     * @param name
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，读取文件内容
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流将文件写回浏览器
            //字节数组用于传输流
            ServletOutputStream outputStream = response.getOutputStream();
            //设置数据类型
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = fis.read(b)) != -1){
                outputStream.write(b,0,len);
            }

            fis.close();
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
