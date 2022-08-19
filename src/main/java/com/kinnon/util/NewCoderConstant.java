package com.kinnon.util;

/**
 * @author Kinnon
 * @create 2022-08-04 22:55
 */
public interface NewCoderConstant {

    //激活成功
    int ACTIVATION_SUCCESS = 0;

    //重复激活
    int ACTIVATION_REPEAAT = 1;

    int ACTIVATION_FAIL = 2;

    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 实体用户：帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体用户：评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体用户：用户
     */
    int ENTITY_TYPE_USER = 3;


    int UNREAD = 0;
    int READ = 1;

    /**
     * 主题：评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 主题：关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * 主题：发帖
     */
    String TOPIC_PUBLISH = "publish";

    /**
     * 主题：点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 系统ID
     */
    int SYSTEM_USER_ID = 1;



}
