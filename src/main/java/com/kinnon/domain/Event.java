package com.kinnon.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-15 16:20
 */

@Data
public class Event {

    private String topic;
    /**
     * 发送信息操作者
     */
    private int userId;
    /**
     * 事件类型
     */
    private int entityType;
    /**
     * 事件对象id
     */
    private int entityId;
    /**
     * 事件对象的归属id
     */
    private int entityUserId;

    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
