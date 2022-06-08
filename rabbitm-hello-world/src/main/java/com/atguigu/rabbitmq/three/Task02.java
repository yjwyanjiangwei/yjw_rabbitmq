package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Task01
 * @History:
 * @description:
 */
public class Task02 {
    public static final String QUEUE_NAME = "ack_queue01";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //开启发布确认的方法
        channel.confirmSelect();
        //使用信道声明一个队列
        //第二个参数存储在那里 是否需要持久化,持久化之后会保存在磁盘中,
        // 没有开启持久化就会保存在内存中,如果mq出现宕机情况,那么消息就会丢失
        //第三个参数是否消息共享
        //第四个参数,是否处理完消息进行删除
        //第五个为其它参数
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME,durable, false,false, null);
        //从控制太发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //第一个参数为交换机,此处的交换机不能为null,应指定为""空串,就会使用默认的交换机进行工作,
            // 第二个为队列名称,第三个将消息设置为持久化,
            // 第四个输入消息的字节数组
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        }
    }
}

