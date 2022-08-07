package com.kinnon.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName discuss_post
 */
@TableName(value ="discuss_post")
@Data
public class DiscussPost implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    /**
     * 0-普通; 1-置顶;
     */
    private Integer type;

    /**
     * 0-正常; 1-精华; 2-拉黑;
     */
    private Integer status;

    /**
     * 
     */
    private Date createTime;

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 
     */
    private Double score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}