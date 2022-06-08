package com.atguigu.rabbitmq.deadmessage;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: Producer
 * @History:
 * @description:
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i < 11; i++) {
            String message = "消息" + i;
            channel.basicPublish(Consumer01.NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
        }
    }
}

