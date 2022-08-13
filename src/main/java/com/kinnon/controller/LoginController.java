package com.kinnon.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.code.kaptcha.Producer;
import com.kinnon.domain.User;
import com.kinnon.mapper.UserMapper;
import com.kinnon.service.UserService;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import com.kinnon.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Kinnon
 * @create 2022-08-04 12:45
 */
@Controller
@Slf4j
public class LoginController implements NewCoderConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value(("${server.servlet.context-path}"))
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginrPage(){
        return "/site/login";
    }


    @PostMapping("/register")
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);

        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已向您发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @GetMapping("/activation/{userId}/{code}")
    public String action(Model model,@PathVariable int userId, @PathVariable String code){
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","恭喜你，账号激活成功！可以正常使用。");
            model.addAttribute("target","/login");
        }else if( result == ACTIVATION_REPEAAT){
            model.addAttribute("msg","你的账号已激活，请勿重复激活");
            model.addAttribute("target","/index");

        }else{
            model.addAttribute("msg","账号激活失败,激活码有误");
            model.addAttribute("target","/index");

        }
        return "/site/operate-result";
    }


    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response){
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //验证码归属
        String owner = NewCoderUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", owner);
        cookie.setPath(contextPath);
        cookie.setMaxAge(60);
        response.addCookie(cookie);

        String key = RedisKeyUtil.getKaptchaKey(owner);
        redisTemplate.opsForValue().set(key, text,60, TimeUnit.SECONDS);

        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {

            log.error("响应验证码失败" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(String username, String password, boolean rememberme,
                        HttpServletResponse response, String code,Model model ,@CookieValue("kaptchaOwner") String kafkaToken){

//        String kaptcha =(String) session.getAttribute("kaptcha");
        String kaptcha = null;
        if (StringUtils.isNotBlank(kafkaToken)){
            String key = RedisKeyUtil.getKaptchaKey(kafkaToken);
            kaptcha = (String) redisTemplate.opsForValue().get(key);
        }
        if (StringUtils.isBlank(code) || StringUtils.isBlank(kaptcha) || !code.equalsIgnoreCase(kaptcha)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }

        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpSession session, @CookieValue String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }

    @GetMapping("/forget")
    public String getForgetPage(){
        return "/site/forget";
    }

}
