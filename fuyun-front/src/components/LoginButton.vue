<template>
  <div class="login-container">
    <q-btn
      v-if="!authStore.isAuthenticated"
      color="primary"
      icon="fab fa-github"
      label="GitHub 登录"
      :loading="authStore.loading"
      @click="handleGitHubLogin"
      rounded
      unelevated
    />
    
    <div v-else class="user-info">
      <q-avatar>
        <img :src="authStore.user?.avatarUrl || '/default-avatar.png'" />
      </q-avatar>
      <q-menu>
        <div class="user-menu">
          <div class="user-header">
            <q-avatar size="40px">
              <img :src="authStore.user?.avatarUrl || '/default-avatar.png'" />
            </q-avatar>
            <div class="user-details">
              <div class="user-name">{{ authStore.user?.displayName || authStore.user?.username }}</div>
              <div class="user-email">{{ authStore.user?.email }}</div>
            </div>
          </div>
          <q-separator />
          <q-list dense>
            <q-item clickable v-close-popup @click="viewProfile">
              <q-item-section avatar>
                <q-icon name="person" />
              </q-item-section>
              <q-item-section>个人资料</q-item-section>
            </q-item>
            <q-item clickable v-close-popup @click="handleLogout">
              <q-item-section avatar>
                <q-icon name="logout" />
              </q-item-section>
              <q-item-section>退出登录</q-item-section>
            </q-item>
          </q-list>
        </div>
      </q-menu>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '../stores/authStore'
import { useQuasar } from 'quasar'

const authStore = useAuthStore()
const $q = useQuasar()

const handleGitHubLogin = async () => {
  try {
    await authStore.loginWithGitHub()
  } catch {
    $q.notify({
      type: 'negative',
      message: 'GitHub登录失败，请重试'
    })
  }
}

const handleLogout = async () => {
  try {
    await authStore.logout()
    $q.notify({
      type: 'positive',
      message: '已成功退出登录'
    })
  } catch {
    $q.notify({
      type: 'negative',
      message: '退出登录失败'
    })
  }
}

const viewProfile = () => {
  $q.notify({
    type: 'info',
    message: '个人资料功能开发中'
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-menu {
  min-width: 250px;
  padding: 8px 0;
}

.user-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  gap: 12px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: 600;
  font-size: 14px;
  color: var(--q-primary);
}

.user-email {
  font-size: 12px;
  color: var(--q-color-text-secondary);
  margin-top: 2px;
}
</style>