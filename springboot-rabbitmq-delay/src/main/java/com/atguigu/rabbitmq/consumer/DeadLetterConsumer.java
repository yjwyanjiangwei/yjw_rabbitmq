package com.atguigu.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: DeadLetterConsumer
 * @History:
 * @description:
 */
@Slf4j
@Component
public class DeadLetterConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception
    {
        String s = new String(message.getBody(), "UTF-8");
        log.info("当前时间: {},收到死信队列的消息{}",new Date().toString(),s);
    }
}

