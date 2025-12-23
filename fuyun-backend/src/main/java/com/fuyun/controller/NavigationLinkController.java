package com.fuyun.controller;

import com.fuyun.dto.NavigationLinkDto;
import com.fuyun.entity.NavigationLink;
import com.fuyun.service.NavigationLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 导航链接控制器
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@Slf4j
public class NavigationLinkController {
    
    private final NavigationLinkService linkService;
    
    /**
     * 根据分类ID获取链接
     */
    @GetMapping("/category/{categoryId}")
    public Flux<NavigationLink> findByCategoryId(@PathVariable Long categoryId) {
        log.info("根据分类ID获取导航链接: {}", categoryId);
        return linkService.findByCategoryId(categoryId);
    }
    
    /**
     * 获取所有链接
     */
    @GetMapping
    public Flux<NavigationLink> findAll() {
        log.info("获取所有导航链接");
        return linkService.findAll();
    }
    
    /**
     * 根据ID获取链接
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NavigationLink>> findById(@PathVariable Long id) {
        log.info("根据ID获取导航链接: {}", id);
        return linkService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * 搜索链接
     */
    @GetMapping("/search")
    public Flux<NavigationLink> searchByKeyword(@RequestParam String keyword) {
        log.info("根据关键词搜索导航链接: {}", keyword);
        return linkService.searchByKeyword(keyword);
    }
    
    /**
     * 获取热门链接
     */
    @GetMapping("/popular")
    public Flux<NavigationLink> findPopularLinks() {
        log.info("获取热门导航链接");
        return linkService.findPopularLinks();
    }
    
    /**
     * 创建链接
     */
    @PostMapping
    public Mono<ResponseEntity<NavigationLink>> create(@RequestBody NavigationLinkDto dto) {
        log.info("创建导航链接: {}", dto.getTitle());
        return linkService.create(dto)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
    
    /**
     * 更新链接
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<NavigationLink>> update(@PathVariable Long id, @RequestBody NavigationLinkDto dto) {
        log.info("更新导航链接: {}", id);
        return linkService.update(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
    
    /**
     * 删除链接
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        log.info("删除导航链接: {}", id);
        return linkService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().<Void>build());
    }
    
    /**
     * 记录点击
     */
    @PostMapping("/{id}/click")
    public Mono<ResponseEntity<Void>> incrementClickCount(@PathVariable Long id) {
        log.info("增加导航链接点击次数: {}", id);
        return linkService.incrementClickCount(id)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().<Void>build());
    }
}