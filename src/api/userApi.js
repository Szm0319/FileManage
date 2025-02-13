// src/api/userApi.js
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8082/user',
  timeout: 30000
})

// 登录 API
export const login = (credentials) => {
  return api.post('/login', credentials)
}

// 注册 API
export const register = (userData) => {
  return api.post('/register', userData)
}