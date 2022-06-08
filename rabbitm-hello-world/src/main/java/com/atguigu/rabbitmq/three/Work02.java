package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Work02
 * @History:
 * @description: 消息在手动应答中不丢失,重新放回队列
 */
public class Work02 {
    public static final String QUEUE_NAME = "ack_queue01";

    /*public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("C1等待结束消息处理事件较短");
        //采用手动应答
        DeliverCallback deliverCallback = (consumerTag, message) ->
        {
            SleepUtils.sleep(1);
            System.out.println("接收到消息: " + new String(message.getBody(),"UTF-8"));
            //第一个参数消息的标记
            //第二个参数是否指定批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //第二个参数为手动应答
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,consumerTag ->
        {
            System.out.println(consumerTag + "消费者取消消费消息回调逻辑");
        });
    }*/


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //设置处理消息为不公平分发
//        int prefetchCount = 0;//默认为轮询处理任务
//        int prefetchCount = 1;//表示开启不公平分发,充分利用每个消费者
        int prefetchCount = 2;//表示设置预取值,提前清除设备的性能,给其分配适宜的任务数量
        channel.basicQos(prefetchCount);
        //第一个参数为队列名称,第二个为是否是自动应答,第三个消费者未成功的回调,第四个消费者取消处理消息的回调
        channel.basicConsume(QUEUE_NAME,false, (consumerTag,message) -> {
            SleepUtils.sleep(1);
            System.out.println("接收到的消息: " + new String(message.getBody(),"UTF-8"));
            //第二个不进行批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        },consumerTag ->
                System.out.println("消费者取消处理消息的回调函数"));
    }
}

