package com.fuyun.repository;

import com.fuyun.entity.NavigationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导航分类仓库接口
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Repository
public interface NavigationCategoryRepository extends JpaRepository<NavigationCategory, Long> {
    
    /**
     * 根据是否激活状态查找分类
     */
    List<NavigationCategory> findByIsActiveOrderBySortOrderAsc(Boolean isActive);
    
    /**
     * 根据名称查找分类
     */
    NavigationCategory findByName(String name);
    
    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);
}