package com.fuyun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executor;

/**
 * WebFlux 与 JPA 集成配置类
 * 用于桥接响应式编程和阻塞式JPA操作
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Configuration
@EnableAsync
public class WebFluxJpaConfig {
    
    /**
     * 创建用于JPA阻塞操作的调度器
     */
    @Bean
    public Scheduler jpaScheduler() {
        return Schedulers.fromExecutor(jpaExecutor());
    }
    
    /**
     * 创建专门用于JPA操作的线程池
     */
    @Bean(name = "jpaExecutor")
    public Executor jpaExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("jpa-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }
}