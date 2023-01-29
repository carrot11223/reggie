package com.atheima.reggie.controller;

import com.atheima.reggie.common.R;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")

/**
 * 文件上传功能
 */
public class CommonController {
    @Value("${reggie.path}")
    private String path;
    private String fileName;
    @PostMapping("/upload")
    //此处的file是一个临时文件，请求消失之后就会不见，我们要把这个文件放到我们的服务器上面
    public R<String> upload(MultipartFile file){
        //此处获取的是上传文件的原始名字，但是为了防止原始的名字有重复，覆盖掉了
        //原来上传的图片，所以这个时候我们使用UUID的方式上传，防止重复
        String originalFilename = file.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(i);
        //使用 UUID的方式
        String s = UUID.randomUUID().toString();//此时生成的是一串序列，还需要拼接原始文件的后缀suffix
        fileName = s+substring;
        File dir = new File(path);
        if (!dir.exists()) {
            //目录不存在 就创建该目录
            dir.mkdirs();
        }


        try {
            //将临时文件转存到我们设定的位置
            file.transferTo(new File(path+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return R.success(fileName);
    }

    /**
     * 实现文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
       //name是当前从浏览器获取的之前上传的照片名字
        try {
            //创建一个输入流
            FileInputStream inputStream = new FileInputStream(new File(path+fileName));
            //创建一个输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //规定输出的类型
            response.setContentType("image/jpeg");
            //创建一个byte数组
            byte[] bytes = new byte[1024];
            //创建一个长度变量
            int len = 0;
            //循环写出到浏览器当中
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
            //关闭流
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
