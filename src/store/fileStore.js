// src/store/fileStore.js
import { defineStore } from 'pinia'
import { uploadFile, deleteFile, getFileList, downloadFile } from '@/api/fileApi'

export const useFileStore = defineStore('file', {
  state: () => ({
    files: [],
    loading: false,
    error: null
  }),
  actions: {
    async uploadFile(file, onProgress) {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('username', localStorage.getItem('username'))
      try {
        const response = await uploadFile(formData, {
          onUploadProgress: progressEvent => {
            const percent = progressEvent.loaded / progressEvent.total
            onProgress(percent)
          }
        })
        console.log('Upload response:', response.data) // 添加日志
        this.files.push(response.data)
        return response.data
      } catch (error) {
        console.error('Upload failed:', error) // 添加日志
        throw new Error('文件上传失败')
      }
    },
    
    async fetchFiles() {
      this.loading = true
      try {
        const response = await getFileList()
        this.files = response.data
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },
    
    async deleteFile(fileId) {
      try {
        await deleteFile(fileId)
        this.files = this.files.filter(file => file.id !== fileId)
      } catch (error) {
        throw new Error('文件删除失败')
      }
    },
    async downloadFile(fileName) {
      try {
        const response = await downloadFile(fileName); // 调用后端下载接口
    
        // 检查响应状态和数据类型
        if (response.status !== 200 || response.data.constructor.name !== 'ArrayBuffer') {
          throw new Error('无法下载文件');
        }
    
        // 将 ArrayBuffer 转换为 Blob
        const blob = new Blob([response.data]);
    
        // 创建一个 URL 用于下载
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName; // 设置下载文件名
    
        // 将 <a> 标签添加到 DOM 中并模拟点击
        document.body.appendChild(link);
        link.click();
    
        // 下载完成后移除 <a> 标签
        link.remove();
    
        // 清理创建的对象 URL
        window.URL.revokeObjectURL(url);
    
      } catch (error) {
        console.error('下载文件时出错:', error);
        throw new Error('下载文件失败，请重试');
      }
    }
  }    
  })