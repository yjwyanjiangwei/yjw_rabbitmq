package com.atguigu.rabbitmq.deadmessage;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: Consumer01
 * @History:
 * @description:
 */
public class Consumer01 {
    //定义一个死信交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //定义一个死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead _exchange";
    //普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //声明死信交换机以及普通交换机的类型
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //通过普通队列携带参数将死信给转发到死信交换机
        Map<String,Object> map = new HashMap<>();
        //携带参数里面可以是过期时间10000ms = 10s,该处设置的过期时间设置在消息生产时指定会更灵活些
        //map.put("x-message-ttl", 10000);
        //正常队列设置死信交换机是谁
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信交换机的routingKey,找到死信队列
        map.put("x-dead-letter-routing-key","lisi");
//        map.put("x-max-length", 6);
        //声明普通队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,map);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //绑定普通的交换机与普通的队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信的交换机与死信的队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收消息...... ");

        DeliverCallback deliverCallback = (consumerTag,message) ->
        {
            String s = new String(message.getBody(), "UTF-8");
            if (s.equals("info4"))
            {
                //如果在接收消息时没有开启手动应答的话,无法拒绝消息的消费
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }
            else
            {
                System.out.println("Consumer01接收的消息是:  " + s);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        //开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback , consumerTag -> {});
    }
    public static void deadMessage() throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //声明普通交换机以及普通队列
        channel.exchangeDeclare(NORMAL_EXCHANGE,BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
        //在声明正常队列时,传递参数由正常的消息队列去寻找死信交换机,当发生死信时,死信就可以找到对应的消息队列
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        map.put("x-message-ttl", 10000);
        map.put("x-dead-queue-routing-key", "lisi");
        map.put("x-max-length", 5);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,map);
        //声明死信交换机以及死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //将交换机与队列进行绑定
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        DeliverCallback deliverCallback = (consumerTag, message) ->
        {
            String s = new String(message.getBody(), "UTF-8");
            if (s.equals("消息4"))
            {
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
                System.out.println("被拒绝接收的消息为:" + s);
            }
            else
            {
                channel.basicAck(message.getEnvelope().getDeliveryTag(),true);
                System.out.println("被处理的消息为:" + s);
            }
        };
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumerTag -> {});
    }
}

