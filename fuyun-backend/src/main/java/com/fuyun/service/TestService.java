package com.fuyun.service;

import com.fuyun.entity.Test;
import com.fuyun.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

/**
 * 测试服务类 - 演示WebFlux与JPA的集成
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    
    private final TestRepository testRepository;
    private final Scheduler jpaScheduler;
    
    /**
     * 获取所有测试数据 - 响应式
     */
    public Flux<Test> findAll() {
        return Mono.fromCallable(() -> testRepository.findAll())
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 根据ID查找测试数据 - 响应式
     */
    public Mono<Test> findById(Long id) {
        return Mono.fromCallable(() -> testRepository.findById(id))
                .flatMap(optional -> optional.map(Mono::just).orElse(Mono.empty()))
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 保存测试数据 - 响应式
     */
    public Mono<Test> save(Test test) {
        return Mono.fromCallable(() -> testRepository.save(test))
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 删除测试数据 - 响应式
     */
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> testRepository.deleteById(id))
                .subscribeOn(jpaScheduler)
                .then();
    }
    
    /**
     * 根据名称查找测试数据 - 响应式
     */
    public Flux<Test> findByNameContaining(String name) {
        return Mono.fromCallable(() -> testRepository.findByNameContainingIgnoreCase(name))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
}