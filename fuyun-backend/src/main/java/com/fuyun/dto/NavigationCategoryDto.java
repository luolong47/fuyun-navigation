package com.fuyun.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 导航分类DTO
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Data
public class NavigationCategoryDto {
    
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    private String name;
    
    @Size(max = 200, message = "描述长度不能超过200个字符")
    private String description;
    
    @Size(max = 100, message = "图标长度不能超过100个字符")
    private String icon;
    
    private Integer sortOrder = 0;
    
    private Boolean isActive = true;
}