package com.drstrong.health.product.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ClassName: MetricsConfig
 * @Author:fant
 * @Description: Mysql HikariCP连接池信息采集
 * @Date:Create in 11:55 2022/3/29
 * @version: V1.0
 */
@Configuration
@Component
public class MetricsConfig {
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;
    @Resource
    @Qualifier("prometheusMeterRegistry")
    public MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        new ExecutorServiceMetrics(taskExecutor.getThreadPoolExecutor(), "taskExecutor", null).bindTo(meterRegistry);
//        new CommonsObjectPool2Metrics().bindTo(meterRegistry);
    }

}
