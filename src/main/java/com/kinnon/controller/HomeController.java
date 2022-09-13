package com.kinnon.controller;

import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.Page;
import com.kinnon.domain.User;
import com.kinnon.service.DiscussPostService;
import com.kinnon.service.MessageService;
import com.kinnon.service.UserService;

import com.kinnon.service.LikeService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-02 22:50
 */

@Controller
public class HomeController implements NewCoderConstant {

    @Autowired
    private DiscussPostService discussdPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;


    @RequestMapping("/index")
    public String getIndexPage(Model model, Page page,@RequestParam(name="orderMode",defaultValue="0") int orderMode){
        page.setRows(discussdPostService.getDiscussPostRows(0));
        page.setPath("/index?orderMode=" + orderMode);
        List<DiscussPost> disCussPosts = discussdPostService.getDisCussPosts(0, page.getOffset(), page.getLimit(),orderMode);
        List<Map<String,Object>> disCussPostsMapList = new ArrayList<>();
        if (disCussPosts != null ){
            for (DiscussPost disCussPost : disCussPosts) {
                HashMap<String, Object> map = new HashMap<>();
                User user = userService.getById(disCussPost.getUserId());
                map.put("post",disCussPost);
                map.put("user",user);
                map.put("likeCount",likeService.getLikeCount(NewCoderConstant.ENTITY_TYPE_POST,disCussPost.getId()));
                disCussPostsMapList.add(map);
            }
        }

        model.addAttribute("disCussPosts",disCussPostsMapList);
        model.addAttribute("orderMode",orderMode);
        return "/index";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "/error/error";
    }

    // 拒绝访问时的提示页面
    @GetMapping ( "/denied")
    public String getDeniedPage() {
        return "/error/404";
    }

}
