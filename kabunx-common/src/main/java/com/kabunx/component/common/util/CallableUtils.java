package com.kabunx.component.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用回调函数工具类
 */
@Slf4j
public class CallableUtils {

    private static final String INVALID_VALUE = "undefined";

    private static final Long DEFAULT_MILLIS = 10000L;

    public static void when(String value, Consumer<String> callback) {
        if (StringUtils.isEmpty(value) || INVALID_VALUE.equals(value)) {
            return;
        }
        callback.accept(value);
    }

    public static <T> void when(T value, Consumer<T> callback) {
        if (Objects.isNull(value)) {
            return;
        }
        callback.accept(value);
    }

    /**
     * 每日定时循环任务
     *
     * @param startHour 开始时间
     * @param endHour   结束时间
     * @param supplier  回调
     */
    public static void dailyLoop(int startHour, int endHour, Supplier<Boolean> supplier) {
        // 死循环 监听队列是否有消息需要消费
        for (; ; ) {
            try {
                // 检测当前时间是否在触达时间内
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startTime = TimeUtils.today(startHour, 0, 0);
                LocalDateTime endTime = TimeUtils.today(endHour, 0, 0);
                // 时间未到
                if (now.isBefore(startTime)) {
                    Duration duration = Duration.between(now, startTime);
                    log.warn("[DailyLoop] 调度时间暂未开始，将等待【{}】ms", duration.toMillis());
                    ThreadUtils.sleepMillis(duration.toMillis());
                    continue;
                }
                // 时间已过
                if (now.isAfter(endTime)) {
                    // 第二天开始的时间
                    LocalDateTime nextStartTime = TimeUtils.tomorrow(startHour, 0, 0);
                    Duration duration = Duration.between(now, nextStartTime);
                    log.warn("[DailyLoop] 调度时间已过期，下一次执行将等待【{}】ms", duration.toMillis());
                    ThreadUtils.sleepMillis(duration.toMillis());
                    continue;
                }
                long ms = DEFAULT_MILLIS;
                if (supplier.get()) {
                    // 执行成功将明天继续
                    LocalDateTime nextStartTime = TimeUtils.tomorrow(startHour, 0, 0);
                    Duration duration = Duration.between(LocalDateTime.now(), nextStartTime);
                    log.warn("[DailyLoop] 当前调度任务已完成，下一次执行将等待【{}】ms", duration.toMillis());
                    ms = duration.toMillis();
                }
                ThreadUtils.sleepMillis(ms);
            } catch (Exception ex) {
                log.error("[DailyLoop] 循环体内逻辑异常", ex);
            }
        }
    }
}
