package com.atguigu.rabbitmq.consumer;

import com.atguigu.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: DelayedQueueConsumer
 * @History:
 * @description: 消费延迟队列的消费者代码
 */
@Slf4j
@Component
public class DelayedQueueConsumer {

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE)
    public void receiveDelayedQueue(Message message) throws Exception
    {
        String s = new String(message.getBody(), "UTF-8");
        log.info("当前时间: {},收到延迟队列的消息{}",new Date().toString(),s);
    }
}

