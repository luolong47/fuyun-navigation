import { api } from 'boot/axios'
import type { NavigationCategory, NavigationLink } from '../types/navigation'

export class NavigationService {
  /**
   * 获取所有激活的导航分类
   */
  static async getActiveCategories(): Promise<NavigationCategory[]> {
    const response = await api.get('/api/categories/active')
    return response.data
  }

  /**
   * 根据分类ID获取导航链接
   */
  static async getLinksByCategory(categoryId: number): Promise<NavigationLink[]> {
    const response = await api.get(`/api/links/category/${categoryId}`)
    return response.data
  }

  /**
   * 获取热门导航链接
   */
  static async getPopularLinks(): Promise<NavigationLink[]> {
    const response = await api.get('/api/links/popular')
    return response.data
  }

  /**
   * 增加链接点击次数
   */
  static async incrementClickCount(linkId: number): Promise<void> {
    await api.post(`/api/links/${linkId}/click`)
  }

  /**
   * 获取所有导航链接
   */
  static async getAllLinks(): Promise<NavigationLink[]> {
    const response = await api.get('/api/links')
    return response.data
  }
}