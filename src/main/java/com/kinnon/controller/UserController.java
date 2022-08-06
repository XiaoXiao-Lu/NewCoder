package com.kinnon.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kinnon.domain.User;
import com.kinnon.service.UserService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderUtil;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Kinnon
 * @create 2022-08-06 15:07
 */
@Slf4j
@RequestMapping("/user")
@Controller
public class UserController {

   @Value("${newcoder.path.upload}")
    private String uploadPath;

   @Value("${newcoder.path.domain}")
    private String domain;

   @Value("${server.servlet.context-path}")
    private String contextPath;

   @Autowired
   private UserService userService;

   @Autowired
   private HostHolder hostHolder;

    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }

    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headImage, Model model){
        if (headImage == null){
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }
        String fileName = headImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error", "文件格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        fileName = NewCoderUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headImage.transferTo(dest);
        } catch (IOException e) {
           log.error("上传文件失败：",e.getMessage());
           throw new RuntimeException("上传文件失败：" + e.getMessage());
        }
        User user = hostHolder.getUser();
       String headUrl = domain + contextPath + "/user/header/" + fileName;
        int i = userService.updateHeader(user.getId(), headUrl);
        return "redirect:/user/setting";
    }

    @GetMapping("/header/{fileName}")
    public void uploadHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
       fileName = uploadPath + "/" + fileName;
       String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
         response.setContentType("image/" + suffix);
        try(FileInputStream fis = new FileInputStream(fileName)) {
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            log.error("读取图像失败：",e.getMessage());
        }finally {

        }
    }



}
