package com.kinnon.controller;

import com.alibaba.fastjson.JSONObject;
import com.kinnon.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kinnon
 * @create 2022-08-01 21:56
 */

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test1")
    public void test1(HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        try (
                PrintWriter writer = response.getWriter();
        ){
            writer.write("<h1>Kinnon</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @GetMapping("/test2/{id}/{id2}")
    @ResponseBody
    public String test2(@PathVariable Integer id2,@PathVariable Integer id){
        System.out.println("id" + id);
        System.out.println("id2" + id2);

        return "PathVariable";
    }

    @GetMapping("/test3")
    @ResponseBody
    public User test3(){
        User kinnon = new User();
        return kinnon;
    }


}
