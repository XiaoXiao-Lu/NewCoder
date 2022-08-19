package com.kinnon;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kinnon
 * @create 2022-08-15 15:19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringbootProjectDemo1Application.class)
public class KafkaTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public  void kafka() {
        kafkaProducer.sendMessage("test","hello kafka");
        kafkaProducer.sendMessage("test","在吗 kafka");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);

    }

}

@Component
class KafkaConsumer {

    @KafkaListener(topics = "test")
    public void handleMessage(ConsumerRecord record) {
        System.out.println("kafka消费者收到消息：" + record.value());

    }

}


