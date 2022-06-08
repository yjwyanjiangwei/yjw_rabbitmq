package com.atguigu.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: DelayedQueueConfig
 * @History:
 * @description:
 */

@Configuration
public class DelayedQueueConfig {
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    public static final String DELAYED_QUEUE = "delayed.queue";
    public static final String DELAYED_ROUTINGKEY = "delayed.routingkey";

    @Bean
    public Queue queue()
    {
        return new Queue(DELAYED_QUEUE);
    }
    //定义自定义交换机
    //1.交换机名称
    //2.交换机类型
    //3. 是否自动删除
    @Bean
    public CustomExchange delayedExchange()
    {
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE,"x-delayed-message",true, false,arguments);
    }
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("queue") Queue queue,
            @Qualifier("delayedExchange") Exchange delayedExchange)
    {
        return BindingBuilder.bind(queue).to(delayedExchange).with(DELAYED_ROUTINGKEY).noargs();
    }
}

