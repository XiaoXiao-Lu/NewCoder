package com.kinnon.controller;

import com.kinnon.annotation.LoginRequired;
import com.kinnon.domain.Comment;
import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.Event;
import com.kinnon.event.EventProducer;
import com.kinnon.service.CommentService;
import com.kinnon.service.DiscussPostService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        int userId = hostHolder.getUser().getId();
        comment.setUserId(userId);
        comment.setCreateTime(new Date());
        comment.setStatus(0);

        commentService.insertComment(comment);

        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(userId)
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("comment", comment)
                .setData("postId",discussPostId);

        if (comment.getEntityType() == NewCoderConstant.ENTITY_TYPE_POST){
            //被评论的帖子
            DiscussPost discussPost = discussPostService.getById(comment.getEntityId());
            event.setEntityUserId(discussPost.getUserId());
        }else if(comment.getEntityType() == NewCoderConstant.ENTITY_TYPE_COMMENT){
            Comment targetComment = commentService.getById(comment.getEntityId());
            event.setEntityUserId(targetComment.getUserId());
        }
        eventProducer.fireEvent(event);
        if (comment.getEntityType() == ENTITY_TYPE_POST){
            //触发评论事件
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(userId)
                    .setEntityType(comment.getEntityType())
                    .setEntityId(comment.getEntityId());
            eventProducer.fireEvent(event);

            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey,discussPostId);

        }


        return "redirect:/discuss/detail/" + discussPostId;
    }



}
