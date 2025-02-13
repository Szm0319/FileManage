<template>
  <el-upload
    class="upload-demo"
    drag
    multiple
    :auto-upload="false"
    :on-change="handleFileChange"
    :file-list="fileList"
  >
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">
      将文件拖到此处或<em>点击选择</em>
    </div>
    <template #tip>
      <div class="el-upload__tip">
        支持任意格式文件，单个文件不超过1GB
      </div>
    </template>
  </el-upload>
</template>

<script setup>
import { ref } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'

const emit = defineEmits(['files-selected'])
const fileList = ref([])

const handleFileChange = (file, fileList) => {
  const updatedFileList = fileList.map(file => ({
    ...file,
    raw: file.raw || file // 确保 raw 属性存在
  }))
  emit('files-selected', updatedFileList)
}
</script>

<style scoped>
/* 你的样式 */
</style>