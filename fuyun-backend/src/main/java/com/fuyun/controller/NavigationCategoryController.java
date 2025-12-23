package com.fuyun.controller;

import com.fuyun.dto.NavigationCategoryDto;
import com.fuyun.entity.NavigationCategory;
import com.fuyun.service.NavigationCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 导航分类控制器
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class NavigationCategoryController {
    
    private final NavigationCategoryService categoryService;
    
    /**
     * 获取所有激活的分类
     */
    @GetMapping("/active")
    public Flux<NavigationCategory> findAllActive() {
        log.info("获取所有激活的导航分类");
        return categoryService.findAllActive();
    }
    
    /**
     * 获取所有分类
     */
    @GetMapping
    public Flux<NavigationCategory> findAll() {
        log.info("获取所有导航分类");
        return categoryService.findAll();
    }
    
    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NavigationCategory>> findById(@PathVariable Long id) {
        log.info("根据ID获取导航分类: {}", id);
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * 创建分类
     */
    @PostMapping
    public Mono<ResponseEntity<NavigationCategory>> create(@RequestBody NavigationCategoryDto dto) {
        log.info("创建导航分类: {}", dto.getName());
        return categoryService.create(dto)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<NavigationCategory>> update(@PathVariable Long id, @RequestBody NavigationCategoryDto dto) {
        log.info("更新导航分类: {}", id);
        return categoryService.update(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        log.info("删除导航分类: {}", id);
        return categoryService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().<Void>build());
    }
}