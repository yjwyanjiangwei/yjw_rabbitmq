package com.atguigu.rabbitmq.deadmessage;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: Consumer02
 * @History:
 * @description:
 */
public class Consumer02 {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) ->
        {
            System.out.println("Consumer02接收的消息是:  " + new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(Consumer01.DEAD_QUEUE,false,deliverCallback,consumerTag -> {});
    }
}

