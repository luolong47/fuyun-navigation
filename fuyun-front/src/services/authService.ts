import { api } from 'boot/axios'
import type { User } from '../types/user'

export class AuthService {
  private static readonly TOKEN_KEY = 'fuyun_token'
  private static readonly USER_KEY = 'fuyun_user'

  static async getGitHubAuthUrl(): Promise<string> {
    const response = await api.get('/api/auth/github/url')
    return response.data
  }

  static async getCurrentUser(): Promise<User | null> {
    const token = this.getToken()
    if (!token) return null

    try {
      const response = await api.get('/api/auth/me', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      return response.data
    } catch {
      this.clearAuth()
      return null
    }
  }

  static handleGitHubCallback(code: string): Promise<User> {
    return api.get('/api/auth/github/callback', {
      params: { code }
    }).then((response: { data: User }) => {
      const user = response.data
      if (user.token) {
        this.setAuth(user.token, user)
      }
      return user
    })
  }

  static setAuth(token: string, user: User) {
    localStorage.setItem(this.TOKEN_KEY, token)
    localStorage.setItem(this.USER_KEY, JSON.stringify(user))
  }

  static setToken(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token)
  }

  static getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY)
  }

  static getUser(): User | null {
    const userData = localStorage.getItem(this.USER_KEY)
    return userData ? JSON.parse(userData) : null
  }

  static clearAuth() {
    localStorage.removeItem(this.TOKEN_KEY)
    localStorage.removeItem(this.USER_KEY)
  }

  static async logout(): Promise<void> {
    try {
      await api.post('/api/auth/logout')
    } finally {
      this.clearAuth()
    }
  }

  static isAuthenticated(): boolean {
    return !!this.getToken()
  }
}