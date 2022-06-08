package com.atguigu.rabbitmq.controller;

import com.atguigu.rabbitmq.config.DelayedQueueConfig;
import com.atguigu.rabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: SendMessage
 * @History:
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message)
    {
        log.info("当前时间:{} 发送一条消息给两个ttl队列:{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend("X","XA","来自ttl为10s的队列" + message);
        rabbitTemplate.convertAndSend("X","XB","来自ttl为40s的队列" + message);
    }

    @GetMapping("/sendExpiration/{message}/{time}")
    public void sendMessage(@PathVariable String message,@PathVariable String time)
    {
        log.info("发送消息时间: {},发送一条消息市场{}msTTL信息给队列QC:{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE,"XC",message, msg ->
        {
            msg.getMessageProperties().setExpiration(time);
            return msg;
        });
    }
    @GetMapping("/sendMessagewithExpiration/{message}/{expirationTime}")
    public void sendMessage(@PathVariable String message,@PathVariable Integer expirationTime)
    {
        log.info("发送消息时间: {},发送一条消息时长{}ms 信息给队列QC消息为:{}",new Date().toString(),expirationTime,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE,
                DelayedQueueConfig.DELAYED_ROUTINGKEY,message, msg ->
                {
                    //此处设置延迟消息的单位为ms
                    msg.getMessageProperties().setDelay(expirationTime);
                    return msg;
                });
    }
}

