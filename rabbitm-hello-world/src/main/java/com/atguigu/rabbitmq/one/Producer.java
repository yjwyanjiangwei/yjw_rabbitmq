package com.atguigu.rabbitmq.one;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Producer
 * @History:
 * @description:
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置ip,连接mq的队列
        connectionFactory.setHost("192.168.86.20");
        //用户名
        connectionFactory.setUsername("admin");
        //密码
        connectionFactory.setPassword("123");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 声明一个队列,
         * 1.队列名称已经定义,
         * 2. 第二个参数表示队列是否需要保存消息,默认是存储在内存中,
         * false表示不是持久化,true表示进行持久化
         * 3. 该队列是否只供一个消费者进行消费,是否进行消息共享,默认不支持多个消费者
         * 共享消息,如果需要设置为消息共享的话,那么第三个参数设置为true
         * 4.第四个参数表示是否自动删除,最后一个消费者断开连接之后,队列是否自动删除
         * true表示自动删除,false表示不自动删除
         * 5.其它参数
         */
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,false,false,false,arguments);
        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2. 路由的key是哪个,本次是队列的名称
         * 3. 表示其它参数信息
         * 4.发送消息的消息体
         */
        for (int i = 0; i < 10; i++) {
            String message = "message" + i;
            if (i == 5)
            {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }
            else
            {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }
        System.out.println("消息发送完毕!!!");
    }
}

