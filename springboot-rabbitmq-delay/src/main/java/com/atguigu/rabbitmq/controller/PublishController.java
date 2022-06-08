package com.atguigu.rabbitmq.controller;

import com.atguigu.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: PublishController
 * @History:
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/advanced")
public class PublishController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/confirm/{message}")
    public void confirm(@PathVariable("message") String message)
    {
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                "key1",message,correlationData1);
        log.info("发送的消息内容为:" + message);

        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                "key12",message,correlationData2);
        log.info("发送的消息内容为:" + message);
    }
}

