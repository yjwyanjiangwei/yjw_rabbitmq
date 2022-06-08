package com.atguigu.rabbitmq.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: MyCallback
 * @History:
 * @description:
 */
@Slf4j
@Configuration
public class MyCallback implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init()
    {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
    /*
    1.发消息成功了 的回调
    * 第一个参数保存回调消息的id以及相关信息
    * 第二个参数,表示消息是否收到消息 ack =true
    第三个参数为失败的原因 此处为成功情况下,故为Null
    * 2. 发消息失败进行的回调
    2.1第一个参数保存回调消息的id以及相关信息
    2.2 第二个参数,表示消息是否收到消息 ack =false
    2.3 第三个参数 失败的原因 一段描述
    */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack)
        {
            log.info("交换机已经接收到id为{}的消息" + id);
        }
        else
        {
            log.info("交换机没有收到id为{}的消息" +id);
        }
    }


    //当消息不能到达mq时将消息给回退的接口的回调方法
    //只有投递失败才会到达这里
    @SneakyThrows
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息{}对应的id{},被交换机{}给退回,退回原因为{},路由键为{}",
                new String(message.getBody(),"UTF-8"),replyCode,exchange,replyText,routingKey);
    }
}

