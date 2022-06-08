package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author: yjw
 * @create: 2022/6/6
 * @FileName: EmitLogs
 * @History:
 * @description: 日志的发送方
 */
public class EmitLogs {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        //使用工具类生成信道
        Channel channel = RabbitMqUtil.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
        }
    }
}

