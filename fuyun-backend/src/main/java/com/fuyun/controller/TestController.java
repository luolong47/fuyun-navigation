package com.fuyun.controller;

import com.fuyun.dto.TestDto;
import com.fuyun.entity.Test;
import com.fuyun.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 测试控制器 - 演示WebFlux响应式API
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    
    private final TestService testService;
    
    /**
     * 获取所有测试数据
     */
    @GetMapping
    public Flux<Test> findAll() {
        log.info("获取所有测试数据");
        return testService.findAll();
    }
    
    /**
     * 根据ID获取测试数据
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Test>> findById(@PathVariable Long id) {
        log.info("根据ID获取测试数据: {}", id);
        return testService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * 创建测试数据
     */
    @PostMapping
    public Mono<Test> create(@RequestBody TestDto testDto) {
        log.info("创建测试数据: {}", testDto.getName());
        Test test = new Test();
        test.setName(testDto.getName());
        test.setDescription(testDto.getDescription());
        return testService.save(test);
    }
    
    /**
     * 更新测试数据
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Test>> update(@PathVariable Long id, @RequestBody Test test) {
        log.info("更新测试数据: {}", id);
        return testService.findById(id)
                .flatMap(existing -> {
                    existing.setName(test.getName());
                    existing.setDescription(test.getDescription());
                    return testService.save(existing);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * 删除测试数据
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        log.info("删除测试数据: {}", id);
        return testService.findById(id)
                .flatMap(existing -> testService.deleteById(id)
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().<Void>build()));
    }
    
    /**
     * 根据名称搜索测试数据
     */
    @GetMapping("/search")
    public Flux<Test> searchByName(@RequestParam String name) {
        log.info("根据名称搜索测试数据: {}", name);
        return testService.findByNameContaining(name);
    }
}