package com.fuyun.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 导航链接DTO
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Data
public class NavigationLinkDto {
    
    private Long id;
    
    @NotBlank(message = "链接标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;
    
    @Size(max = 200, message = "描述长度不能超过200个字符")
    private String description;
    
    @NotBlank(message = "链接地址不能为空")
    @Size(max = 500, message = "链接地址长度不能超过500个字符")
    private String url;
    
    @Size(max = 100, message = "图标长度不能超过100个字符")
    private String icon;
    
    private Integer sortOrder = 0;
    
    private Boolean isActive = true;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    private String categoryName;
}