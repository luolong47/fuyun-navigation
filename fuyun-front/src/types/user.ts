export interface User {
  id: number
  username: string
  email: string
  avatarUrl?: string
  displayName?: string
  bio?: string
  location?: string
  blogUrl?: string
  company?: string
  token?: string
}

export interface AuthResponse {
  user: User
  token: string
}