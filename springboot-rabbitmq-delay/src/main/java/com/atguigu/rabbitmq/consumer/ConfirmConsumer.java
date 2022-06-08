package com.atguigu.rabbitmq.consumer;

import com.atguigu.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: ConfirmConsumer
 * @History:
 * @description:
 */
@Slf4j
@Component
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void receiveConfirm(Message message) throws Exception
    {
        String s = new String(message.getBody(),"UTF-8");
        log.info("当前时间为:{},从消息队列中获取到的消息为:{}",new Date().toString(),s);
    }
}

