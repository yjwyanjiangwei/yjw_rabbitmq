package com.atguigu.rabbitmq.utils;

/**
 * @author: yjw
 * @create: 2022/6/5
 * @FileName: SleepUtils
 * @History:
 * @description:
 */
public class SleepUtils {
    public static void sleep(int seconds)
    {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

