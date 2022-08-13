package com.kinnon.controller;

import com.kinnon.domain.Comment;
import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.Page;
import com.kinnon.domain.User;
import com.kinnon.service.CommentService;
import com.kinnon.service.DiscussPostService;
import com.kinnon.service.UserService;
import com.kinnon.service.impl.LikeService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Kinnon
 * @create 2022-08-07 13:15
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements NewCoderConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

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

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPostDetail(@PathVariable("discussPostId") int id, Model model, Page page) {
        //帖子

        DiscussPost discussPost = discussPostService.getById(id);
        model.addAttribute("discussPost", discussPost);

        //作者
        User user = userService.getById(discussPost.getUserId());
        //用户
        User currentUser = hostHolder.getUser();
        model.addAttribute("user", user);
        model.addAttribute("likeCount",likeService.getLikeCount(NewCoderConstant.ENTITY_TYPE_POST,discussPost.getId()));
        model.addAttribute("likeStatus",currentUser==null?0:likeService.findEntityLikeStatus(currentUser.getId(),
                NewCoderConstant.ENTITY_TYPE_POST,discussPost.getId()));

        page.setLimit(5);
        page.setPath("/discuss/detail/" + id);
        page.setRows(discussPost.getCommentCount());

        //给帖子的评论
        List<Comment> comments = commentService.selectCommentList(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> commentsMapList = new ArrayList<>();
        if (comments != null) {
            for (Comment comment : comments) {
                Map<String, Object> map = new HashMap<>();
                User commentUser = userService.getById(comment.getUserId());
                map.put("comment", comment);
                map.put("user", commentUser);
                map.put("likeCount",likeService.getLikeCount(NewCoderConstant.ENTITY_TYPE_COMMENT,comment.getId()));
                map.put("likeStatus",currentUser==null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(),
                        ENTITY_TYPE_COMMENT,comment.getId()));

                //评论中的评论
                List<Comment> comments1 = commentService.selectCommentList(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                List<Map<String, Object>> commentsMapList1 = new ArrayList<>();
                if (comments1 != null) {
                    for (Comment comment1 : comments1) {
                        Map<String, Object> map1 = new HashMap<>();
                        User commentUser1 = userService.getById(comment1.getUserId());
                        map1.put("comment", comment1);
                        map1.put("user", commentUser1);
                        //回复目标的
                        User user1 = comment1.getTargetId() == 0 ? null : userService.getById(comment1.getTargetId());
                        map1.put("target", user1);
                        map1.put("likeCount",likeService.getLikeCount(NewCoderConstant.ENTITY_TYPE_COMMENT,comment1.getId()));
                        map1.put("likeStatus",currentUser==null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(),
                                NewCoderConstant.ENTITY_TYPE_COMMENT,comment1.getId()));
                        commentsMapList1.add(map1);
                    }
                }
                map.put("comment2", commentsMapList1);
                //评论帖子的评论的回复数量
                int count = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
                map.put("replyCount", count);
                commentsMapList.add(map);
            }
        }
        model.addAttribute("comments", commentsMapList);
        return "/site/discuss-detail";
    }


}
