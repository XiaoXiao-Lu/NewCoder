package com.kinnon.controller;

import com.kinnon.domain.DiscussPost;
import com.kinnon.service.DiscussPostService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author Kinnon
 * @create 2022-08-07 13:15
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        if (hostHolder.getUser() == null) {
            return NewCoderUtil.getJSONString(403, "你还没未登录");
        }
        int userId = hostHolder.getUser().getId();
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(userId);
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setStatus(0);
        discussPost.setType(0);
        discussPost.setCommentCount(0);
        discussPost.setScore(0.00);
        int result = discussPostService.addDiscussPost(discussPost);

        return NewCoderUtil.getJSONString(0, "发布成功");

    }

}
