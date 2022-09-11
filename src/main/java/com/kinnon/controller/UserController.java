package com.kinnon.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kinnon.annotation.LoginRequired;
import com.kinnon.domain.User;
import com.kinnon.service.UserService;
import com.kinnon.service.FollowService;
import com.kinnon.service.LikeService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-06 15:07
 */
@Slf4j
@RequestMapping("/user")
@Controller
@Transactional
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
   private LikeService likeService;

   @Autowired
   private HostHolder hostHolder;

   @Autowired
    private FollowService followService;

   //设置
   @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }

    //修改头像
    @LoginRequired
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

    //头像
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

    //修改密码
    @PostMapping("/updatePassword")
    public String updatePassword(String oldPassword, String newPassword,String confirmPassword, Model model){
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)){
            model.addAttribute("error", "密码不能为空");
            return "/site/setting";
        }
        if(!newPassword.equals(confirmPassword)){
            model.addAttribute("differror", "两次密码不一致");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user, oldPassword, newPassword);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg", "修改密码成功");
            model.addAttribute("target", "/user/setting");
            return "/site/operate-result";
        }else {
            model.addAttribute("pwderror", map.get("oldPasswordMsg"));
        }

        return "/site/setting";
    }

    //主页
    @RequestMapping("/profile/{userId}")
    public String getProfilePage(Model model,@PathVariable("userId") int userId){

        User targetUser = userService.getById(userId);
        User currentUser = hostHolder.getUser();
        if (targetUser == null){
            throw new RuntimeException("该用户不存在");
        }
        int likeCount = likeService.getUserLikeCount(userId);
        model.addAttribute("targetUser", targetUser);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("myUser", currentUser);

        long followeeCount = followService.getFolloweeCount(userId, NewCoderConstant.ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        long followerCount = followService.getFollowerCount(NewCoderConstant.ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        boolean followStatus =  false;
        if ( currentUser != null){
            followStatus = followService.hasFollow(currentUser.getId(), NewCoderConstant.ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollow", followStatus);

        return "/site/profile";
    }



}
