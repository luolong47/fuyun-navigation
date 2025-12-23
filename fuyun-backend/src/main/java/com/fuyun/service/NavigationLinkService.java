package com.fuyun.service;

import com.fuyun.dto.NavigationLinkDto;
import com.fuyun.entity.NavigationLink;
import com.fuyun.repository.NavigationLinkRepository;
import com.fuyun.repository.NavigationCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

/**
 * 导航链接服务类
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NavigationLinkService {
    
    private final NavigationLinkRepository linkRepository;
    private final NavigationCategoryRepository categoryRepository;
    private final Scheduler jpaScheduler;
    
    /**
     * 根据分类ID获取所有激活的链接
     */
    @Transactional(readOnly = true)
    public Flux<NavigationLink> findByCategoryId(Long categoryId) {
        log.info("根据分类ID获取导航链接: {}", categoryId);
        return Mono.fromCallable(() -> linkRepository.findByCategoryIdAndIsActiveOrderBySortOrderAsc(categoryId, true))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 获取所有链接
     */
    @Transactional(readOnly = true)
    public Flux<NavigationLink> findAll() {
        log.info("获取所有导航链接");
        return Mono.fromCallable(() -> linkRepository.findAll())
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 根据ID获取链接
     */
    @Transactional(readOnly = true)
    public Mono<NavigationLink> findById(Long id) {
        log.info("根据ID获取导航链接: {}", id);
        return Mono.fromCallable(() -> linkRepository.findById(id))
                .flatMap(optional -> optional.map(Mono::just).orElse(Mono.empty()))
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 搜索链接
     */
    @Transactional(readOnly = true)
    public Flux<NavigationLink> searchByKeyword(String keyword) {
        log.info("根据关键词搜索导航链接: {}", keyword);
        return Mono.fromCallable(() -> linkRepository.searchByKeyword(keyword))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 获取热门链接
     */
    @Transactional(readOnly = true)
    public Flux<NavigationLink> findPopularLinks() {
        log.info("获取热门导航链接");
        return Mono.fromCallable(() -> linkRepository.findTop10ByIsActiveOrderByClickCountDesc(true))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 创建链接
     */
    public Mono<NavigationLink> create(NavigationLinkDto dto) {
        log.info("创建导航链接: {}", dto.getTitle());
        
        return Mono.fromCallable(() -> {
            // 验证分类是否存在
            if (!categoryRepository.existsById(dto.getCategoryId())) {
                throw new IllegalArgumentException("分类不存在: " + dto.getCategoryId());
            }
            
            NavigationLink link = new NavigationLink();
            link.setTitle(dto.getTitle());
            link.setDescription(dto.getDescription());
            link.setUrl(dto.getUrl());
            link.setIcon(dto.getIcon());
            link.setSortOrder(dto.getSortOrder());
            link.setIsActive(dto.getIsActive());
            link.setClickCount(0L);
            
            return categoryRepository.findById(dto.getCategoryId())
                    .map(category -> {
                        link.setCategory(category);
                        return linkRepository.save(link);
                    })
                    .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + dto.getCategoryId()));
        }).subscribeOn(jpaScheduler);
    }
    
    /**
     * 更新链接
     */
    public Mono<NavigationLink> update(Long id, NavigationLinkDto dto) {
        log.info("更新导航链接: {}", id);
        
        return findById(id).flatMap(existing -> {
            return Mono.fromCallable(() -> {
                // 验证分类是否存在
                if (!categoryRepository.existsById(dto.getCategoryId())) {
                    throw new IllegalArgumentException("分类不存在: " + dto.getCategoryId());
                }
                
                existing.setTitle(dto.getTitle());
                existing.setDescription(dto.getDescription());
                existing.setUrl(dto.getUrl());
                existing.setIcon(dto.getIcon());
                existing.setSortOrder(dto.getSortOrder());
                existing.setIsActive(dto.getIsActive());
                
                return categoryRepository.findById(dto.getCategoryId())
                        .map(category -> {
                            existing.setCategory(category);
                            return linkRepository.save(existing);
                        })
                        .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + dto.getCategoryId()));
            }).subscribeOn(jpaScheduler);
        });
    }
    
    /**
     * 删除链接
     */
    public Mono<Void> deleteById(Long id) {
        log.info("删除导航链接: {}", id);
        return findById(id)
                .flatMap(link -> {
                    return Mono.fromRunnable(() -> linkRepository.delete(link))
                            .subscribeOn(jpaScheduler)
                            .then();
                });
    }
    
    /**
     * 增加点击次数
     */
    public Mono<Void> incrementClickCount(Long id) {
        log.info("增加导航链接点击次数: {}", id);
        return Mono.fromRunnable(() -> linkRepository.incrementClickCount(id))
                .subscribeOn(jpaScheduler)
                .then();
    }
}