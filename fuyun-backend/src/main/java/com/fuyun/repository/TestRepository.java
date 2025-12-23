package com.fuyun.repository;

import com.fuyun.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 测试数据仓库接口
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    
    /**
     * 根据名称模糊查询（忽略大小写）
     */
    List<Test> findByNameContainingIgnoreCase(String name);
}