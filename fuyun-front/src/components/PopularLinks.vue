<template>
  <div class="popular-links q-pa-md">
    <div class="text-h6 text-weight-bold q-mb-md">
      <q-icon name="trending_up" class="q-mr-sm" />
      热门导航
    </div>
    
    <div class="links-grid">
      <q-card
        v-for="link in popularLinks"
        :key="link.id"
        flat
        bordered
        class="popular-link-card q-mb-sm cursor-pointer"
        @click="openLink(link)"
      >
        <q-card-section class="row items-center q-pa-md">
          <q-avatar
            :icon="getLinkIcon(link.icon)"
            color="red-5"
            text-color="white"
            class="q-mr-md"
          />
          <div class="col">
            <div class="text-weight-medium">{{ link.title }}</div>
            <div class="text-caption text-grey-6">{{ link.description }}</div>
            <div class="text-caption text-grey-5 q-mt-xs">
              <q-icon name="visibility" class="q-mr-xs" />
              {{ link.clickCount }} 次访问
              <q-chip
                dense
                size="sm"
                color="red-5"
                text-color="white"
                class="q-ml-sm"
              >
                热门
              </q-chip>
            </div>
          </div>
          <q-icon name="launch" class="text-grey-5" />
        </q-card-section>
      </q-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { NavigationLink } from '../types/navigation'
import { NavigationService } from '../services/navigationService'

const popularLinks = ref<NavigationLink[]>([])

onMounted(async () => {
  try {
    popularLinks.value = await NavigationService.getPopularLinks()
  } catch (error) {
    console.error('获取热门链接失败:', error)
  }
})

const getLinkIcon = (iconName: string): string => {
  const iconMap: Record<string, string> = {
    weibo: 'chat',
    github: 'code',
    stackoverflow: 'help',
    xinhua: 'article',
    bilibili: 'play_circle',
    youtube: 'smart_display',
    google: 'search',
    baidu: 'search'
  }
  return iconMap[iconName] || 'link'
}

const openLink = async (link: NavigationLink) => {
  try {
    // 增加点击次数
    await NavigationService.incrementClickCount(link.id)
    
    // 在新标签页中打开链接
    window.open(link.url, '_blank')
  } catch (error) {
    console.error('打开链接失败:', error)
    // 即使统计失败也要打开链接
    window.open(link.url, '_blank')
  }
}
</script>

<style lang="scss" scoped>
.popular-links {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 12px;
}

.popular-link-card {
  transition: all 0.3s ease;
  border-left: 4px solid #f44336;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}

@media (max-width: 600px) {
  .links-grid {
    grid-template-columns: 1fr;
  }
}
</style>