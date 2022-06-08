package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: Producer
 * @History:
 * @description:
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null, next.getBytes());
            System.out.println("发送 :" + next + " 成功");
        }

    }
}

