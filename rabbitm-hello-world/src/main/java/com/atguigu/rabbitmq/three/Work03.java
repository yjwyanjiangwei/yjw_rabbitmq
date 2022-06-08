package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Work02
 * @History:
 * @description: 消息在手动应答中不丢失,重新放回队列
 */
public class Work03 {
    public static final String QUEUE_NAME = "ack_queue01";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("C2等待结束消息处理事件较短");
        //采用手动应答
        DeliverCallback deliverCallback = (consumerTag, message) ->
        {
            SleepUtils.sleep(30);
            System.out.println("接收到消息: " + new String(message.getBody(),"UTF-8"));
            //第一个参数消息的标记
            //第二个参数是否指定批量应答
            channel.basicAck(message.getEnvelope( ).getDeliveryTag(),false);
        };
        //设置处理消息为不公平分发
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,consumerTag ->
            System.out.println(consumerTag + "消费者取消消费消息回调逻辑")
        );
    }
}

