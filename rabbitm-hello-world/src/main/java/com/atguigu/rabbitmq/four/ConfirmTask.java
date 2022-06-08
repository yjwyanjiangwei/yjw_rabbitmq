package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author: yjw
 * @FileName: ConfirmTask
 * @create: 2022/6/6
 * @History:
 * @description: 验证发布确认模式
 * 单个确认模式
 * 批量确认模式
 * 异步发布确认模式
 */
public class ConfirmTask {
    public static final int PUBLISH_MESSAGE_COUNT = 1000;
    public static void main(String[] args) throws Exception {
//        ConfirmTask.publishSingleMessage();
//        ConfirmTask.publishBatchMessage();
        ConfirmTask.asynchronous();
    }
    //        1.单个确认模式
    public static void publishSingleMessage() throws Exception
    {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启信道确认模式
        channel.confirmSelect();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < PUBLISH_MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            //单个消息进行发布确认
            boolean b = channel.waitForConfirms();
            if (b)
            {
                System.out.println("消息发送成功");
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序执行时间为: "  + (endTime - startTime) + "ms");
    }
    //        2.批量确认模式
    public static void publishBatchMessage() throws Exception
    {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启信道确认模式
        channel.confirmSelect();
        //定义批量发布确认的基数
        int batchSize = 100;
        //定义初始化发布消息确认的个数
        int startPublishMessageCount = 0;
        int batchCount = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < PUBLISH_MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            startPublishMessageCount ++;
            if (startPublishMessageCount == batchSize)
            {
                channel.waitForConfirms();
                startPublishMessageCount = 0;
                batchCount ++ ;
                System.out.println("进行了第" + batchCount + "次批量发布确认");
            }
        }
        if (startPublishMessageCount > 0)
        {
            channel.waitForConfirms();
            batchCount ++ ;
            System.out.println("进行了第" + batchCount + "次批量发布确认");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序执行时间为: "  + (endTime - startTime) + "ms");
    }
    public static void asynchronousPublish() throws Exception
    {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启信道确认模式
        channel.confirmSelect();
        /*
        创建一个线程安全有序的哈希表,使用与高并发的情况下
        1.轻松的将序号与消息进行关联,
        2.批量进行删除已经成功的消息
        * 支持高并发
         */
        //消息确认成功的回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple) ->
        {
            System.out.println("确认消息的编号 :" + deliveryTag);
        };
        //消息确认失败的回调函数
        ConfirmCallback nackCallback = (deliveryTag,multiple) ->
        {

            System.out.println("未确认消息的编号 :" + deliveryTag);
        };
        //发送消息之前进行监听消息成功与否,
        channel.addConfirmListener(ackCallback,nackCallback);//此处的监听是异步进行通知
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < PUBLISH_MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish( "",queueName,null,message.getBytes());
            //在此处记录所有的消息
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序执行时间为: "  + (endTime - startTime) + "ms");
    }
    public static void asynchronous() throws Exception
    {
        Channel channel = RabbitMqUtil.getChannel();
        //使用信道进行声明一个队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        //开启信道消息确认
        channel.confirmSelect();

        ConfirmCallback ackCallback = (deliveryTag, multiple) ->
        {
            if (multiple)
            {
                ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag);
                confirmed.clear();
            }
            else
            {
                concurrentSkipListMap.remove(deliveryTag);
            }
            System.out.println("确认的消息为:" + deliveryTag);
        };

        ConfirmCallback nackCallback = (deliveryTag, multiple) ->
        {
            String s = concurrentSkipListMap.get(deliveryTag);
            System.out.println("未被确认的消息 :" + s + " 序号为:" + deliveryTag);
        };
        channel.addConfirmListener(ackCallback, nackCallback);

        Long startTime = System.currentTimeMillis();

        for (int i = 0; i < PUBLISH_MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //此处将所有的消息给记录在并发跳跃链表hash表中
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
        }

        long endTime = System.currentTimeMillis();
        System.out.println(PUBLISH_MESSAGE_COUNT + "个消息被异步处理花费的时间为:" + (endTime - startTime) + "ms");
    }
}

