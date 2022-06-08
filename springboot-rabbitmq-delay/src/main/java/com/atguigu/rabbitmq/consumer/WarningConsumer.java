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
public class WarningConsumer {
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void receiveConfirm(Message message) throws Exception
    {
        String s = new String(message.getBody(),"UTF-8");
        log.error("警告发现不可路由的消息为:{}",s);
    }
}

