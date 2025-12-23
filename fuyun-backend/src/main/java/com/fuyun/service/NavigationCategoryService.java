package com.fuyun.service;

import com.fuyun.dto.NavigationCategoryDto;
import com.fuyun.entity.NavigationCategory;
import com.fuyun.repository.NavigationCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

/**
 * 导航分类服务类
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NavigationCategoryService {
    
    private final NavigationCategoryRepository categoryRepository;
    private final Scheduler jpaScheduler;
    
    /**
     * 获取所有激活的分类
     */
    @Transactional(readOnly = true)
    public Flux<NavigationCategory> findAllActive() {
        log.info("获取所有激活的导航分类");
        return Mono.fromCallable(() -> categoryRepository.findByIsActiveOrderBySortOrderAsc(true))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 获取所有分类
     */
    @Transactional(readOnly = true)
    public Flux<NavigationCategory> findAll() {
        log.info("获取所有导航分类");
        return Mono.fromCallable(() -> categoryRepository.findAll())
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 根据ID获取分类
     */
    @Transactional(readOnly = true)
    public Mono<NavigationCategory> findById(Long id) {
        log.info("根据ID获取导航分类: {}", id);
        return Mono.fromCallable(() -> categoryRepository.findById(id))
                .flatMap(optional -> optional.map(Mono::just).orElse(Mono.empty()))
                .subscribeOn(jpaScheduler);
    }
    
    /**
     * 创建分类
     */
    public Mono<NavigationCategory> create(NavigationCategoryDto dto) {
        log.info("创建导航分类: {}", dto.getName());
        
        return Mono.fromCallable(() -> {
            // 检查名称是否已存在
            if (categoryRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("分类名称已存在: " + dto.getName());
            }
            
            NavigationCategory category = new NavigationCategory();
            category.setName(dto.getName());
            category.setDescription(dto.getDescription());
            category.setIcon(dto.getIcon());
            category.setSortOrder(dto.getSortOrder());
            category.setIsActive(dto.getIsActive());
            
            return categoryRepository.save(category);
        }).subscribeOn(jpaScheduler);
    }
    
    /**
     * 更新分类
     */
    public Mono<NavigationCategory> update(Long id, NavigationCategoryDto dto) {
        log.info("更新导航分类: {}", id);
        
        return findById(id).flatMap(existing -> {
            return Mono.fromCallable(() -> {
                // 检查名称是否被其他分类使用
                NavigationCategory existingByName = categoryRepository.findByName(dto.getName());
                if (existingByName != null && !existingByName.getId().equals(id)) {
                    throw new IllegalArgumentException("分类名称已存在: " + dto.getName());
                }
                
                existing.setName(dto.getName());
                existing.setDescription(dto.getDescription());
                existing.setIcon(dto.getIcon());
                existing.setSortOrder(dto.getSortOrder());
                existing.setIsActive(dto.getIsActive());
                
                return categoryRepository.save(existing);
            }).subscribeOn(jpaScheduler);
        });
    }
    
    /**
     * 删除分类
     */
    public Mono<Void> deleteById(Long id) {
        log.info("删除导航分类: {}", id);
        return findById(id)
                .flatMap(category -> {
                    return Mono.fromRunnable(() -> categoryRepository.delete(category))
                            .subscribeOn(jpaScheduler)
                            .then();
                });
    }
}