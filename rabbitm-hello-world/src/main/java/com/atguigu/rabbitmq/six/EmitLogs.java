package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: EmitLogs
 * @History:
 * @description:
 */
public class EmitLogs {
    public static final String EXCHANGE_NAME = "logs01";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            channel.basicPublish(EXCHANGE_NAME,"aaa",null,message.getBytes("UTF-8"));
            System.out.println("消息" + message + "已经发出");
        }
    }
}

