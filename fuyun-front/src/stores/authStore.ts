import { defineStore } from 'pinia'
import type { User } from '../types/user'
import { AuthService } from '../services/authService'

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  loading: boolean
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: null,
    isAuthenticated: false,
    loading: false
  }),

  actions: {
    async initializeAuth() {
      // 检查URL中是否有token参数（用于处理GitHub登录回调）
      const urlParams = new URLSearchParams(window.location.search)
      const tokenFromUrl = urlParams.get('token')
      
      if (tokenFromUrl) {
        // 保存token并清理URL
        AuthService.setToken(tokenFromUrl)
        
        // 尝试从token解析用户信息或从后端获取
        this.loading = true
        try {
          const user = await AuthService.getCurrentUser()
          if (user) {
            this.user = user
            this.isAuthenticated = true
            // 清理URL中的token参数
            const newUrl = window.location.pathname + window.location.hash
            window.history.replaceState({}, document.title, newUrl)
          }
        } catch (error) {
          console.error('从token获取用户信息失败', error)
          AuthService.clearAuth()
        } finally {
          this.loading = false
        }
        return
      }
      
      if (AuthService.isAuthenticated()) {
        this.loading = true
        try {
          const user = await AuthService.getCurrentUser()
          if (user) {
            this.user = user
            this.isAuthenticated = true
          }
        } catch (error) {
          console.error('初始化认证失败', error)
          AuthService.clearAuth()
        } finally {
          this.loading = false
        }
      }
    },

    async loginWithGitHub() {
      try {
        this.loading = true
        const authUrl = await AuthService.getGitHubAuthUrl()
        window.location.href = authUrl
      } catch (error) {
        console.error('GitHub登录失败', error)
        this.loading = false
        throw error
      }
    },

    async handleGitHubCallback(code: string) {
      try {
        this.loading = true
        const user = await AuthService.handleGitHubCallback(code)
        this.user = user
        this.isAuthenticated = true
        return user
      } catch (error) {
        console.error('处理GitHub回调失败', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async logout() {
      try {
        this.loading = true
        await AuthService.logout()
        this.user = null
        this.isAuthenticated = false
      } catch (error) {
        console.error('退出登录失败', error)
      } finally {
        this.loading = false
      }
    }
  }
})