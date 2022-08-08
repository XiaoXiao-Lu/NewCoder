package com.kinnon.controller;

import com.kinnon.annotation.LoginRequired;
import com.kinnon.domain.Comment;
import com.kinnon.domain.DiscussPost;
import com.kinnon.service.CommentService;
import com.kinnon.service.DiscussPostService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author Kinnon
 * @create 2022-08-08 13:18
 */
@Controller
@RequestMapping("comment")
public class CommentController implements NewCoderConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        Integer userId = hostHolder.getUser().getId();
        comment.setUserId(userId);
        comment.setCreateTime(new Date());
        comment.setStatus(0);

        commentService.insertComment(comment);

        return "redirect:/discuss/detail/" + discussPostId;
    }



}
