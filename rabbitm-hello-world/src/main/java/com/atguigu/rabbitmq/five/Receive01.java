package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: Receive
 * @History:
 * @description: 日志接收方
 */
public class Receive01 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        String queueName = channel.queueDeclare().getQueue();
        //将交换机与队列给绑定起来
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        DeliverCallback deliverCallback = (consumerTag,message) ->
        {
            String s = new String(message.getBody(), "UTF-8");
            System.out.println("接收到消息为:" + s );
        };
        channel.basicConsume(queueName,false,deliverCallback,cancelCallback -> {});
    }
}

