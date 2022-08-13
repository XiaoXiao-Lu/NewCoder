package com.kinnon.controller;

import com.alibaba.fastjson.JSON;
import com.kinnon.annotation.LoginRequired;
import com.kinnon.domain.User;
import com.kinnon.service.impl.LikeService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-11 23:37
 */
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostholer;



    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId,int entityUserId) {
        User user = hostholer.getUser();
        likeService.like(user.getId(), entityType, entityId,entityUserId);
        long likeCount = likeService.getLikeCount(entityType, entityId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeService.findEntityLikeStatus(user.getId(), entityType, entityId));
        return NewCoderUtil.getJSONString(0,null,map);
    }



}
