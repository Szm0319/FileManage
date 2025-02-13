import axios from 'axios'
import { useAuthStore } from '@/store/authStore'

const api = axios.create({
  baseURL: 'http://localhost:8082/api',
  timeout: 0
})

// 设置请求拦截器，每次发送请求前都会自动附加 Token
api.interceptors.request.use(
  (config) => {
    //console.log('Request interceptor')
    const token = localStorage.getItem('token')
    const username = localStorage.getItem('username')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}` // 添加 token 到请求头
    }
    if (username) {
      config.headers['Username'] = username // 添加 username 到请求头
    }
    return config
  },

  (error) => {
    console.log('Request error')  // 请求错误的日志
    console.error('Request error response:', error.response);  // 打印错误信息
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    //console.log('Response interceptor')
    return response  // 如果响应正常，直接返回
  },
  (error) => {
    console.log('Response error')  // 响应错误的日志
    console.error('Error response:', error.response);  // 打印错误信息
    // 处理 token 过期的情况
    if (error.response && error.response.status === 401) {
      const authStore = useAuthStore()
      authStore.logout()  // 清除 Pinia 状态中的认证信息
      alert('您的登录已过期，请重新登录')
    }
    return Promise.reject(error)
  }
)



export const uploadFile = (formData, config) => {
  console.log('Uploading file to:', `${api.defaults.baseURL}/files/upload`)  // 添加日志
  return api.post('/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers  // 保留传递过来的其他头部信息
    },
    onUploadProgress: config.onUploadProgress  // 添加这一行，传递进度回调
  })
}

export const deleteFile = (filename) => {
  return api.delete(`/files/delete/${filename}`)
}

export const getFileList = () => {
  return api.get('/files/list')
}

export const downloadFile = (filename) => {
  return api.get(`/files/download/${filename}`, {
    responseType: 'arraybuffer'  // 设置响应类型为 blob，以便处理文件下载
  })
}
