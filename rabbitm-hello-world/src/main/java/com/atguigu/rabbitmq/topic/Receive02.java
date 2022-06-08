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
public class Receive02 {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        channel.exchangeDeclare(EmitLogs.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName, EmitLogs.EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName, EmitLogs.EXCHANGE_NAME,"lazy.#");
        DeliverCallback deliverCallback = (consumerTag, message) ->
        {
            String s = new String(message.getBody(), "UTF-8");
            System.out.println("q2接收到消息为:" + s + "    对应绑定的routingKey为" + message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName,false,deliverCallback,consumerTag -> {});
    }
}

