import { defineStore } from 'pinia'
import { login, register } from '@/api/userApi'

export const useAuthStore = defineStore('auth', {
  state: () => {
    // 从 localStorage 恢复认证状态
    const token = localStorage.getItem('token')
    const username = localStorage.getItem('username')
    console.log("token",token)
    return {
      token: token || null, 
      username: username || null,
      loading: false,
      isAuthenticated: token && username ? true : false,  // 根据 token 和 username 设置认证状态
      error: null
    }
  },
  actions: {
    setLoading(isLoading) {
      this.loading = isLoading;
    },
    async login({ username, password }) {
      this.setLoading(true);  // 设置加载状态为 true
      this.error = null
      try {
        const response = await login({ username, password })
        this.isAuthenticated = true
        this.token = response.data;  // 更新 Pinia 状态
        this.username = username
        console.log("当前认证状态 isAuthenticated:", this.isAuthenticated); // 打印当前认证状态
        localStorage.setItem('username', this.username);
        localStorage.setItem('isAuthenticated', 'true'); // 存储认证状态
        if (this.token) {
          localStorage.setItem('token', this.token);
        } else {
          console.error("token 无效，无法存储");
        }
        return response.data
      } catch (error) {
          this.error = error.response ? error.response.data : error.message || '登录失败';
          throw new Error(this.error);
        } finally {
          this.setLoading(false);
        }
      },

    async register({ username, password }) {
      this.setLoading(true);
      this.error = null
      try {
        const response = await register({ username, password })
        if (response.status === 201) {
          console.log(response.data)
          return response.data
        }
        throw new Error('注册失败')
      } catch (error) {
        this.error = error.message || '注册失败'
        throw new Error(this.error)
      } finally {
        this.setLoading(false);
      }
    },

    logout() {
      this.isAuthenticated = false
      this.username = null
      localStorage.removeItem('token');  // 清除 localStorage 中的 token
      localStorage.removeItem('username');
      localStorage.removeItem('isAuthenticated'); // 清除认证状态
    }
  }
})
