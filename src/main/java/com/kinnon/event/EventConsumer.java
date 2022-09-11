package com.kinnon.event;

import com.alibaba.fastjson.JSONObject;
import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.Event;
import com.kinnon.domain.Message;
import com.kinnon.service.DiscussPostService;
import com.kinnon.service.MessageService;
import com.kinnon.service.ElasticsearchService;
import com.kinnon.util.NewCoderConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-15 16:44
 */
@Component
@Slf4j
public class EventConsumer implements NewCoderConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private DiscussPostService discussPostService;

    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record ==null || record.value() == null){
            log.error("消费端消息为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event ==null) {
            log.error("消息格式错误!");
        }
        Message message = new Message();
        message.setFromId(NewCoderConstant.SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setStatus(0);
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (event.getData() != null) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);

    }

    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){
        if (record ==null || record.value() == null){
            log.error("消费端消息为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event ==null) {
            log.error("消息格式错误!");
        }

        DiscussPost post = discussPostService.getById(event.getEntityId());
        elasticsearchService.save(post);

    }

    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record){
        if (record ==null || record.value() == null){
            log.error("消费端消息为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event ==null) {
            log.error("消息格式错误!");
        }


        elasticsearchService.deleteDiscussPOst(event.getEntityId());

    }






}
