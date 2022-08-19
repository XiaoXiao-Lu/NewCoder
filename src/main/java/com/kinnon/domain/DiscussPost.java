package com.kinnon.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName discuss_post
 */
@TableName(value ="discuss_post")
@Data
@Document(indexName = "discusspost",shards = 6,replicas = 3)
public class DiscussPost implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @Id
    private Integer id;

    /**
     * 
     */
    @Field(type = FieldType.Integer)
    private Integer userId;

    /**
     * 
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;

    /**
     * 0-普通; 1-置顶;
     */
    @Field(type = FieldType.Integer)
    private Integer type;

    /**
     * 0-正常; 1-精华; 2-拉黑;
     */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 
     */
    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Integer)
    private Integer commentCount;

    /**
     * 
     */
    @Field(type = FieldType.Double)
    private Double score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}