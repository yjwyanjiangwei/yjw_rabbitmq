package com.atguigu.rabbitmq.config;

import com.atguigu.rabbitmq.controller.PublishController;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author: yjw
 * @create: 2022/6/7
 * @FileName: ConfirmCpnfig
 * @History:
 * @description:
 */
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    public static final String BACKUP_EXCHANGE = "backup_exchange";
    public static final String CONFIRM_QUEUE = "confirm_queue";

    public static final String BACKUP_QUEUE = "backup_queue";
    public static final String WARNING_QUEUE = "warning_queue";
    @Bean
    public DirectExchange confirmDirectExchange()
    {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE).build();
    }
    @Bean
    public FanoutExchange confirmBackupExchange()
    {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }
    @Bean
    public Queue confirmQueue()
    {
        return new Queue(CONFIRM_QUEUE);
    }

    @Bean
    public Queue backupQueue()
    {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }
    @Bean
    public Queue warningQueue()
    {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }
    @Bean
    public Binding bindingBackupQueueWithConfirmBackupExchange(@Qualifier("confirmBackupExchange") FanoutExchange confirmBackupExchange,
                           @Qualifier("backupQueue") Queue backupQueue)
    {
        return BindingBuilder.bind(backupQueue).to(confirmBackupExchange);
    }
    @Bean
    public Binding bindingWarningQueueWithConfirmBackupExchange(@Qualifier("confirmBackupExchange") FanoutExchange confirmBackupExchange,
                                                               @Qualifier("warningQueue") Queue warningQueue)
    {
        return BindingBuilder.bind(warningQueue).to(confirmBackupExchange);
    }
    @Bean
    public Binding binding(@Qualifier("confirmDirectExchange") DirectExchange confirmDirectExchange,
                           @Qualifier("confirmQueue") Queue confirmQueue)
    {
        return BindingBuilder.bind(confirmQueue).to(confirmDirectExchange).with("key1");
    }
}

