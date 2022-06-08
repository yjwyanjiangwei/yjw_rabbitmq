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
public class Receive02 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列
        String queue = channel.queueDeclare().getQueue();
        /*
         * 绑定交换机与队列*/
        channel.queueBind(queue, EXCHANGE_NAME,"" );
        System.out.println("等待结束消息,把接收到消息给打印到控制台");
        DeliverCallback deliverCallback = (consumerTag,message) ->
        {
            String s = new String(message.getBody(),"UTF-8");
            System.out.println("01控制台打印接收到的消息:" + s);
        };
        channel.basicConsume(queue,false,deliverCallback, consumerTag -> {});
    }
}

