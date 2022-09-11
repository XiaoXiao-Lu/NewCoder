package com.kinnon.service;

import com.kinnon.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author Kinnon
 * @create 2022-08-11 23:06
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void like(int userId, int entityType, int entityId,int entityUserId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entiyLikeKey = "like:" + entityType + ":" + entityId;
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                boolean isMember = operations.opsForSet().isMember(entiyLikeKey, userId);
                operations.multi();
                if (isMember) {
                    operations.opsForSet().remove(entiyLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey );
                } else {
                    operations.opsForSet().add(entiyLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey );
                }

                return operations.exec();
            }
        });


    }

    //返回用户收到的赞
    public int getUserLikeCount(int userId) {
        String likeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(likeKey);
        return count == null ? 0 : count.intValue();
    }
    
    /**
     * 是否点赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int  findEntityLikeStatus(int userId, int entityType, int entityId) {
        String key = "like:" + entityType + ":" + entityId;
        return redisTemplate.opsForSet().isMember(key, userId)? 1:0;
    }
    
    /**
     * 获取点赞数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long getLikeCount(int entityType, int entityId) {
        String key = "like:" + entityType + ":" + entityId;
        return redisTemplate.opsForSet().size(key);
    }

}
