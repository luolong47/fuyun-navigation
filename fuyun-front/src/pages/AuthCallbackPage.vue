<template>
  <div class="auth-callback-page">
    <q-page class="flex flex-center">
      <div class="callback-container">
        <div v-if="loading" class="loading-section">
          <q-spinner-dots size="40px" color="primary" />
          <div class="loading-text">正在处理GitHub登录...</div>
        </div>
        
        <div v-else-if="error" class="error-section">
          <q-icon name="error" size="48px" color="negative" />
          <div class="error-text">登录失败</div>
          <div class="error-message">{{ error }}</div>
          <q-btn 
            color="primary" 
            label="返回首页" 
            @click="goHome"
            class="q-mt-md"
          />
        </div>
        
        <div v-else class="success-section">
          <q-icon name="check_circle" size="48px" color="positive" />
          <div class="success-text">登录成功</div>
          <div class="success-message">正在跳转到首页...</div>
        </div>
      </div>
    </q-page>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import { useQuasar } from 'quasar'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const $q = useQuasar()

const loading = ref(true)
const error = ref('')

onMounted(async () => {
  const code = route.query.code as string
  
  if (!code) {
    error.value = '未收到授权码'
    loading.value = false
    return
  }

  try {
    await authStore.handleGitHubCallback(code)
    $q.notify({
      type: 'positive',
      message: '登录成功！'
    })
    
    void setTimeout(() => {
      void router.push('/')
    }, 1500)
  } catch (err) {
    console.error('GitHub回调处理失败:', err)
    error.value = 'GitHub登录失败，请重试'
    loading.value = false
  }
})

const goHome = () => {
  void router.push('/')
}
</script>

<style scoped>
.auth-callback-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.callback-container {
  background: white;
  padding: 48px;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  text-align: center;
  min-width: 320px;
}

.loading-section,
.error-section,
.success-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.loading-text,
.error-text,
.success-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--q-primary);
}

.error-message,
.success-message {
  font-size: 14px;
  color: var(--q-color-text-secondary);
}

.error-text {
  color: var(--q-negative);
}

.success-text {
  color: var(--q-positive);
}
</style>