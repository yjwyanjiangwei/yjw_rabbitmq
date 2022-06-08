package com.atguigu.rabbitmq.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: TtlQueueConfig
 * @History:
 * @description: ttl延迟队列 配置文件类
 */
@Configuration
public class TtlQueueConfig {
    //普通交换机的名称
    public static final String X_EXCHANGE = "X";
    //死信交换机的名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列的名称
    public static final String A_QUEUE = "QA";
    public static final String B_QUEUE = "QB";
    public static final String C_QUEUE = "QC";
    //死信队列的名称
    public static final String D_DEAD_LETTER_QUEUE = "QD";
    //声明x交换机,此处声明了多个交换机
    @Bean("xExchange")
    public DirectExchange xExchange()
    {
        return new DirectExchange(X_EXCHANGE);
    }

    //声明y交换机
    @Bean("yExchange")
    public DirectExchange yExchange()
    {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }
 /*   //通过普通队列携带参数将死信给转发到死信交换机
    Map<String,Object> map = new HashMap<>();
    //携带参数里面可以是过期时间10000ms = 10s,该处设置的过期时间设置在消息生产时指定会更灵活些
    //map.put("x-message-ttl", 10000);
    //正常队列设置死信交换机是谁
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE);
    //设置死信交换机的routingKey,找到死信队列
        map.put("x-dead-letter-routing-key","lisi");
    //        map.put("x-max-length", 6);*/
    //声明普通队列,此处定义了多个实例,故需要起别名
    @Bean("queueA")
    public Queue queueA()
    {
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(A_QUEUE).withArguments(arguments).build();
    }

    @Bean("queueB")
    public Queue queueB()
    {
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(B_QUEUE).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC()
    {
        Map<String,Object> arguments = new HashMap<>(2);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信routing-key
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(C_QUEUE).withArguments(arguments).build();
    }

    //声明死信队列
    @Bean("queueD")
    public Queue queueD()
    {
        return QueueBuilder.durable(D_DEAD_LETTER_QUEUE).build();
    }
    //绑定交换机与队列 三次
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,@Qualifier("xExchange") DirectExchange xExchange)
    {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,@Qualifier("xExchange") DirectExchange xExchange)
    {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange)
    {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    @Bean
    public Binding queueDBindingX(@Qualifier("queueD") Queue queueD,@Qualifier("yExchange") DirectExchange yExchange)
    {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}

