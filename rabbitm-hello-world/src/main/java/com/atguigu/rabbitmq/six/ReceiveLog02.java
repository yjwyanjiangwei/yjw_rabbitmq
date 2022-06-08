package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: ReceiveLog01
 * @History:
 * @description:
 */
public class ReceiveLog02 {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        channel.exchangeDeclare(EmitLogs.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue,EmitLogs.EXCHANGE_NAME,"bbb");
        DeliverCallback deliverCallback = (consumerTag, message) ->
        {
            String s = new String(message.getBody(), "UTF-8");
            System.out.println("c2接收到消息为:" + s );
        };
        channel.basicConsume(queue,false,deliverCallback,consumerTag -> {});
    }

}

