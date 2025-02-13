<template>
  <div class="management-container">
    <h1>文件管理</h1>
    <el-button type="primary" @click="refreshFiles">刷新列表</el-button>
    
    <el-table :data="fileStore.files" v-loading="fileStore.loading">
      <el-table-column prop="fileName" label="文件名" />
      <el-table-column prop="fileSize" label="大小" :formatter="formatSize" />
      <el-table-column prop="uploadTime" label="上传时间" />
      <el-table-column prop="uploader" label="上传者" /> 
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button @click="downloadFile(row.fileName)" type="success">下载</el-button>
          <el-button @click="deleteFile(row.fileName)" type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { useFileStore } from '@/store/fileStore'
import { onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const fileStore = useFileStore()

onMounted(() => {
  fileStore.fetchFiles()
})

// 定义 formatFileSize 函数
const formatFileSize = (bytes) => {
  if (bytes === undefined || bytes === null) {
    return '未知大小';
  }
  
  const units = ['B', 'KB', 'MB', 'GB', 'TB'];
  let unitIndex = 0;
  while (bytes >= 1024 && unitIndex < units.length - 1) {
    bytes /= 1024;
    unitIndex++;
  }
  return `${bytes.toFixed(2)} ${units[unitIndex]}`;
}

const formatSize = (row) => {
  // 现在使用 fileSize 而不是 size
  return formatFileSize(row?.fileSize); 
}

const downloadFile = async (fileName) => {
  try {
    await fileStore.downloadFile(fileName);
    ElMessage.success('文件开始下载');
  } catch (error) {
    ElMessage.error(error.message);
  }
}

const deleteFile = async (fileName) => {
  try {
    await fileStore.deleteFile(fileName)
    ElMessage.success('文件删除成功')
  } catch (error) {
    ElMessage.error(error.message)
  }
}

const refreshFiles = () => {
  fileStore.fetchFiles()
}
</script>