package com.fuyun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试数据传输对象
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    
    private String name;
    private String description;
}