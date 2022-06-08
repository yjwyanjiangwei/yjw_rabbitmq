package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Consumer
 * @History:
 * @description:
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";
    public static final String FED_EXCHANGE = "fed_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.86.21");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        //工厂初始化之后,创建连接
        Connection connection = connectionFactory.newConnection();
        //由连接来创建信道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(FED_EXCHANGE,BuiltinExchangeType.DIRECT);
        //声明一个队列与交换机进行绑定
        channel.queueDeclare("node02.queue",true,
                false,false,null);
        channel.queueBind("node02.queue",FED_EXCHANGE,"routeKey",null);
        //消费者根据信道来接收消息,消费消息
        /**
         * 1. 表示消费哪个队列中的消息
         * 2.消息接收成功之后是否要自动应答,true表示自动应答,false表示手动应答
         * 3.消费者未成功消费的回调
         * 4. 消费者取消消费回调
         */
        DeliverCallback deliverCallback = (consumerTag,message) ->
                System.out.println(new String(message.getBody()));

        CancelCallback cancelCallback = consumerTag ->
        {
            System.out.println("消费消息被中断");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}

