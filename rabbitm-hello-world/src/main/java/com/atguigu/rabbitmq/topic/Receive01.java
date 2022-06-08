package com.atguigu.rabbitmq.topic;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: Receive01
 * @History:
 * @description:
 */
public class Receive01 {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        channel.exchangeDeclare(EmitLogs.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare("Q1",false,false,false,null);
        channel.queueBind("Q1", EmitLogs.EXCHANGE_NAME,"*.orange.*");
        DeliverCallback deliverCallback = (consumerTag, message) ->
        {
            String s = new String(message.getBody(), "UTF-8");
            System.out.println("q1接收到消息为:" + s + "    对应绑定的键为:" + message.getEnvelope().getRoutingKey());
        };

        channel.basicConsume("Q1",true,deliverCallback,consumerTag -> {});
    }
}

