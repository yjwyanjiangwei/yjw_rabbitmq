package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Work01
 * @History:
 * @description: 这是一个工作线程,相当于消费者
 */
public class Work01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //消息的接收
        DeliverCallback deliverCallback = (consumerTag,message) ->
                System.out.println(new String(message.getBody()));
        CancelCallback cancelCallback = consumerTag ->
                System.out.println("消费着取消消费接口回调逻辑");
        System.out.println("C2等待接收消息");
        channel.basicConsume(QUEUE_NAME, true,deliverCallback,cancelCallback);
    }
}

