package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.kinnon.domain.DiscussPost;
import com.kinnon.service.DiscussPostService;
import com.kinnon.mapper.DiscussPostMapper;
import com.kinnon.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author Thinkpad
* @description 针对表【discuss_post】的数据库操作Service实现
* @createDate 2022-08-02 14:42:03
*/
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost>
    implements DiscussPostService{

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    // Caffeine核心接口: Cache, LoadingCache, AsyncLoadingCache

    // 帖子列表缓存
    private LoadingCache<String, List<DiscussPost>> postListCache;

    // 帖子总数缓存
    private LoadingCache<Integer, Integer> postRowsCache;


    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Nullable
                    @Override
                    public List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误!");
                        }

                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误!");
                        }

                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);

                        // 二级缓存: Redis -> mysql

                        log.debug("load post list from DB.");
                        return discussPostMapper.selectDisCussPosts(0, offset, limit, 1);
                    }
                });
        // 初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer key) throws Exception {
                        log.debug("load post rows from DB.");
                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }

    @Override
    public List<DiscussPost> getDisCussPosts(int userId, int offset, int limit,int orderMode) {
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(offset + ":" + limit);
        }
        log.debug("load post list from DB.");
        List<DiscussPost> discussPosts = discussPostMapper.selectDisCussPosts(userId, offset, limit,orderMode);

        return discussPosts;
    }

    @Override
    public int getDiscussPostRows(int userId) {
        if (userId == 0) {
            return postRowsCache.get(userId);
        }

        log.debug("load post rows from DB.");
        int i = discussPostMapper.selectDiscussPostRows(userId);
        return i;
    }

    @Override
    public int addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null){
            throw new RuntimeException("参数不能为空");
        }
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));


        return discussPostMapper.insertDiscussPost(discussPost);
    }

    public int updateCommentCount(int id, int commentCount) {
        int rows = discussPostMapper.updateDiscussPostCommetnCount(id, commentCount);
        return rows;
    }

    public int selectDiscussPostCount(int userId) {
        int i = discussPostMapper.selectCommentCount(userId);
        return i;
    }

    public int updateStatus(int id,int status){
        return discussPostMapper.updateStatusById(status,id);
    }

    public int updateType(int id,int type){
        return discussPostMapper.updateTypeById(type,id);
    }

    @Override
    public int updateScore(int postId, double score) {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setId(postId);
        discussPost.setScore(score);
        discussPostMapper.updateById(discussPost);
        return 0;
    }
}




