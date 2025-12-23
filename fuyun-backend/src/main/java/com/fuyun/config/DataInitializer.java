package com.fuyun.config;

import com.fuyun.entity.NavigationCategory;
import com.fuyun.entity.NavigationLink;
import com.fuyun.repository.NavigationCategoryRepository;
import com.fuyun.repository.NavigationLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 数据初始化器
 * 
 * @author Fuyun Team
 * @since 2025-12-23
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final NavigationCategoryRepository categoryRepository;
    private final NavigationLinkRepository linkRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化导航数据...");
        
        // 检查是否已有数据
        if (categoryRepository.count() > 0) {
            log.info("数据库中已有数据，跳过初始化");
            return;
        }
        
        // 创建导航分类
        NavigationCategory socialCategory = new NavigationCategory();
        socialCategory.setName("社交媒体");
        socialCategory.setDescription("常用社交平台");
        socialCategory.setIcon("social");
        socialCategory.setSortOrder(1);
        socialCategory.setIsActive(true);
        
        NavigationCategory devCategory = new NavigationCategory();
        devCategory.setName("开发工具");
        devCategory.setDescription("编程开发相关工具");
        devCategory.setIcon("code");
        devCategory.setSortOrder(2);
        devCategory.setIsActive(true);
        
        NavigationCategory newsCategory = new NavigationCategory();
        newsCategory.setName("新闻资讯");
        newsCategory.setDescription("国内外新闻网站");
        newsCategory.setIcon("news");
        newsCategory.setSortOrder(3);
        newsCategory.setIsActive(true);
        
        NavigationCategory videoCategory = new NavigationCategory();
        videoCategory.setName("视频娱乐");
        videoCategory.setDescription("视频和娱乐网站");
        videoCategory.setIcon("video");
        videoCategory.setSortOrder(4);
        videoCategory.setIsActive(true);
        
        // 保存分类
        categoryRepository.saveAll(Arrays.asList(socialCategory, devCategory, newsCategory, videoCategory));
        
        // 创建导航链接
        NavigationLink weibo = new NavigationLink();
        weibo.setTitle("微博");
        weibo.setDescription("新浪微博 - 随时随地发现新鲜事");
        weibo.setUrl("https://weibo.com");
        weibo.setIcon("weibo");
        weibo.setSortOrder(1);
        weibo.setIsActive(true);
        weibo.setCategory(socialCategory);
        
        NavigationLink github = new NavigationLink();
        github.setTitle("GitHub");
        github.setDescription("全球最大的代码托管平台");
        github.setUrl("https://github.com");
        github.setIcon("github");
        github.setSortOrder(1);
        github.setIsActive(true);
        github.setCategory(devCategory);
        
        NavigationLink stackoverflow = new NavigationLink();
        stackoverflow.setTitle("Stack Overflow");
        stackoverflow.setDescription("程序员问答社区");
        stackoverflow.setUrl("https://stackoverflow.com");
        stackoverflow.setIcon("stackoverflow");
        stackoverflow.setSortOrder(2);
        stackoverflow.setIsActive(true);
        stackoverflow.setCategory(devCategory);
        
        NavigationLink xinhua = new NavigationLink();
        xinhua.setTitle("新华网");
        xinhua.setDescription("中国主流新闻网站");
        xinhua.setUrl("http://www.xinhuanet.com");
        xinhua.setIcon("xinhua");
        xinhua.setSortOrder(1);
        xinhua.setIsActive(true);
        xinhua.setCategory(newsCategory);
        
        NavigationLink bilibili = new NavigationLink();
        bilibili.setTitle("哔哩哔哩");
        bilibili.setDescription("年轻人的文化社区");
        bilibili.setUrl("https://www.bilibili.com");
        bilibili.setIcon("bilibili");
        bilibili.setSortOrder(1);
        bilibili.setIsActive(true);
        bilibili.setCategory(videoCategory);
        
        NavigationLink youtube = new NavigationLink();
        youtube.setTitle("YouTube");
        youtube.setDescription("全球最大的视频分享平台");
        youtube.setUrl("https://www.youtube.com");
        youtube.setIcon("youtube");
        youtube.setSortOrder(2);
        youtube.setIsActive(true);
        youtube.setCategory(videoCategory);
        
        // 保存链接
        linkRepository.saveAll(Arrays.asList(weibo, github, stackoverflow, xinhua, bilibili, youtube));
        
        log.info("导航数据初始化完成！");
    }
}