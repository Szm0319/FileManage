<template>
  <div class="upload-container">
    <h1>文件上传</h1>
    <FileUpload @files-selected="handleFilesSelected" />
    <el-button 
      type="primary"
      @click="uploadFiles"
      :disabled="uploading"
    >
      {{ uploading ? '上传中...' : '开始上传' }}
    </el-button>
    
    <div v-for="file in uploadQueue" :key="file.uid" class="progress-item">
      <div class="file-info">
        {{ file.name }} ({{ formatFileSize(file.size) }})
      </div>
      <el-progress 
        :percentage="file.progress"
        :status="file.status"
        :stroke-width="12"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import FileUpload from '@/components/FileUpload.vue'
import { useFileStore } from '@/store/fileStore'
import { ElMessage } from 'element-plus'

const fileStore = useFileStore()
const uploadQueue = ref([])
const uploading = ref(false)

const handleFilesSelected = (files) => {
  console.log('Files selected:', files) // 添加日志
  if (!files || files.length === 0) {
    console.error('No files received')
    return
  }
  uploadQueue.value = files.map(file => ({
    ...file,
    progress: 0,
    status: 'pending',
    uid: file.uid || Math.random().toString(36).substr(2, 9), // 确保每个文件有唯一的uid
  }))
}

const uploadFiles = async () => {
  uploading.value = true
  try {
    if (uploadQueue.value.length === 0) {
      console.log('No files to upload') // 添加日志
      ElMessage.warning('请先选择文件')
      return
    }
    for (const file of uploadQueue.value) {
      console.log(`Starting upload for file: ${file.name}`) // 添加日志
      if (!file.raw) {
        console.error(`Invalid file object: missing 'raw' property for file ${file.name}`)
        continue
      }
      await fileStore.uploadFile(file.raw, progress => {
        file.progress = Math.floor(progress * 100)
      })
      file.status = 'success'
    }
    ElMessage.success('文件上传成功')
  } catch (error) {
    console.error('Upload error:', error) // 添加日志
    ElMessage.error(error.message)
    // 更新所有失败文件的状态
    uploadQueue.value.forEach(f => f.status = 'exception')
  } finally {
    uploading.value = false
  }
}

const formatFileSize = (bytes) => {
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let unitIndex = 0
  while (bytes >= 1024 && unitIndex < units.length - 1) {
    bytes /= 1024
    unitIndex++
  }
  return `${bytes.toFixed(2)} ${units[unitIndex]}`
}
</script>