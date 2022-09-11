package com.kinnon.controller;

import com.kinnon.domain.Event;
import com.kinnon.domain.User;
import com.kinnon.event.EventProducer;
import com.kinnon.service.LikeService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-11 23:37
 */
@Controller
public class LikeController implements NewCoderConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostholer;

    @Autowired
    private EventProducer eventProducer;



    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId,int entityUserId,int postId) {
        User user = hostholer.getUser();
        likeService.like(user.getId(), entityType, entityId,entityUserId);
        long likeCount = likeService.getLikeCount(entityType, entityId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        map.put("likeStatus", likeStatus);

        if (likeStatus == 1){

            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(user.getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);

        }






        return NewCoderUtil.getJSONString(0,null,map);
    }



}
