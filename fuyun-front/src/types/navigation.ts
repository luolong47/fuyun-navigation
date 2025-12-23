export interface NavigationCategory {
  id: number
  name: string
  description: string
  icon: string
  sortOrder: number
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export interface NavigationLink {
  id: number
  title: string
  description: string
  url: string
  icon: string
  sortOrder: number
  isActive: boolean
  clickCount: number
  category: NavigationCategory
  createdAt: string
  updatedAt: string
}