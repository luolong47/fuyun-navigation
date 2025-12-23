package com.fuyun.repository;

import com.fuyun.entity.NavigationLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导航链接仓库接口
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Repository
public interface NavigationLinkRepository extends JpaRepository<NavigationLink, Long> {
    
    /**
     * 根据分类ID和激活状态查找链接
     */
    List<NavigationLink> findByCategoryIdAndIsActiveOrderBySortOrderAsc(Long categoryId, Boolean isActive);
    
    /**
     * 根据分类ID查找所有链接
     */
    List<NavigationLink> findByCategoryIdOrderBySortOrderAsc(Long categoryId);
    
    /**
     * 根据标题或描述搜索链接
     */
    @Query("SELECT n FROM NavigationLink n WHERE n.isActive = true AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NavigationLink> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 获取热门链接（按点击次数排序）
     */
    List<NavigationLink> findTop10ByIsActiveOrderByClickCountDesc(Boolean isActive);
    
    /**
     * 增加点击次数
     */
    @Query("UPDATE NavigationLink n SET n.clickCount = n.clickCount + 1 WHERE n.id = :id")
    void incrementClickCount(@Param("id") Long id);
}