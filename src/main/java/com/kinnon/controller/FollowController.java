package com.kinnon.controller;

import com.kinnon.domain.Event;
import com.kinnon.domain.Page;
import com.kinnon.domain.User;
import com.kinnon.event.EventProducer;
import com.kinnon.service.UserService;
import com.kinnon.service.impl.FollowService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-13 1:12
 */

@Controller
public class FollowController implements NewCoderConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.follow(user.getId() ,entityType, entityId);


        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(user.getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return NewCoderUtil.getJSONString(0, "已关注");
    }

    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.unFollow(user.getId() ,entityType, entityId);

        return NewCoderUtil.getJSONString(0, "取消关注");
    }

    //访问关注列表
    @RequestMapping("/followee/{userId}")
    public String getFolloweePage(@PathVariable("userId") int userId, Model model, Page page) {
        User targetUser = userService.getById(userId);
        if (targetUser == null) {
            throw new RuntimeException("该用户不存在");
        }
        page.setRows((int)followService.getFolloweeCount(userId, NewCoderConstant.ENTITY_TYPE_USER));
        page.setLimit(5);
        page.setPath("/followee/" + userId);
        List<Map<String, Object>> followees = followService.getFollowees(userId, page.getOffset(), page.getLimit());
        if (followees != null) {
            for (Map<String, Object> followee : followees) {
                User user = (User) followee.get("user");
                boolean b = hasFollow(user.getId());
                followee.put("hasFollow", b);
            }
        }


        model.addAttribute("targetUser", targetUser);
        model.addAttribute("followees", followees);
        return "/site/followee";
    }

    //访问粉丝列表
    @RequestMapping("/follower/{userId}")
    public String getFollowerPage(@PathVariable("userId") int userId, Model model,Page page) {
        User targetUser = userService.getById(userId);
        if (targetUser == null) {
            throw new RuntimeException("该用户不存在");
        }
        page.setRows((int)followService.getFollowerCount( NewCoderConstant.ENTITY_TYPE_USER ,userId));
        page.setLimit(5);
        page.setPath("/follower/" + userId);
        List<Map<String, Object>> followers = followService.getFollowers(userId, 0, 10);
        if (followers != null) {
            for (Map<String, Object> follower : followers) {
                User user = (User) follower.get("user");
                boolean b = hasFollow(user.getId());
                follower.put("hasFollow", b);
            }
        }

        model.addAttribute("followers", followers);
        model.addAttribute("targetUser", targetUser);
        return "/site/follower";
    }

    private boolean hasFollow(int userId){
        if (hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollow(hostHolder.getUser().getId(), NewCoderConstant.ENTITY_TYPE_USER, userId);
    }

}
