<template>
  <q-page class="q-pa-md navigation-page">
    <!-- 页面标题 -->
    <div class="text-center q-mb-xl">
      <h1 class="text-h4 text-weight-bold text-primary q-mb-md">
        浮云导航
      </h1>
      <div class="text-h6 text-grey-6">
        精选优质网站，让上网更便捷
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="row justify-center q-pa-xl">
      <q-spinner-dots size="40px" color="primary" />
    </div>

    <!-- 导航内容 -->
    <div v-else class="navigation-content">
      <!-- 热门导航 -->
      <PopularLinks />
      
      <!-- 分类导航 -->
      <NavigationCard
        v-for="category in categories"
        :key="category.id"
        :category="category"
      />
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { NavigationCategory } from '../types/navigation'
import { NavigationService } from '../services/navigationService'
import PopularLinks from '../components/PopularLinks.vue'
import NavigationCard from '../components/NavigationCard.vue'

const categories = ref<NavigationCategory[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    categories.value = await NavigationService.getActiveCategories()
  } catch (error) {
    console.error('获取导航分类失败:', error)
  } finally {
    loading.value = false
  }
})
</script>

<style lang="scss" scoped>
.navigation-page {
  background: #f5f5f5;
  min-height: 100vh;
}

.navigation-content {
  max-width: 1200px;
  margin: 0 auto;
}

@media (max-width: 600px) {
  .q-pa-md {
    padding: 8px;
  }
}
</style>
