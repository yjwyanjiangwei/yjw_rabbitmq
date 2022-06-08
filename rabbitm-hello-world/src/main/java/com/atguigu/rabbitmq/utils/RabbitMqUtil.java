package com.atguigu.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Util
 * @History:
 * @description: 创建消息队列信道的工具类
 */
public class RabbitMqUtil {
    public static Channel getChannel() throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.86.20");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        //工厂初始化之后,创建连接
        Connection connection = connectionFactory.newConnection();
        //由连接来创建信道
        return connection.createChannel();
    }
}

