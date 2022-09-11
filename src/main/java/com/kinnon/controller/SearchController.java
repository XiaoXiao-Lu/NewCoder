package com.kinnon.controller;

import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.Page;
import com.kinnon.service.UserService;
import com.kinnon.service.ElasticsearchService;
import com.kinnon.service.LikeService;
import com.kinnon.util.NewCoderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-18 0:11
 */
@Controller
public class SearchController implements NewCoderConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/search")
    public String search(String keyword, Page page, Model model) {

        List<Map<String, Object>> discussList = elasticsearchService.searchDiscussPost(keyword, page.getCurrent()-1,page.getLimit() );
        long total=0;
        if (discussList.size() != 0 && discussList != null) {
            total = (long) discussList.get(0).get("total");
            for (Map<String, Object> map : discussList) {
                DiscussPost discussPost = (DiscussPost) map.get("post");
                map.put("user",userService.getById(discussPost.getUserId()));
                long likeCount = likeService.getLikeCount(ENTITY_TYPE_POST,discussPost.getId());
                map.put("likeCount",likeCount);
            }
        }
        model.addAttribute("discussPostList", discussList);
        model.addAttribute("keyword", keyword);
        page.setPath("/search?keyword=" + keyword);
        page.setRows((int)total);
        return "/site/search";
    }

}
