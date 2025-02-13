<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>注册页面</h2>
      <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleRegister">
        <el-form-item label="用户名" prop="username">
          <div class="input-with-icon">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              prefix-icon="el-icon-user"
            />
            <el-tooltip class="item" effect="dark" content=
            "用户名长度应在 3 到 20 个字符之间，只允许字母、数字和下划线" placement="right" trigger="hover">
              <InfoFilled class="custom-icon" />
            </el-tooltip>
          </div>
        </el-form-item>

        <el-form-item label="密&nbsp&nbsp&nbsp码" prop="password">
          <div class="input-with-icon">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
            />
            <el-tooltip class="item" effect="dark" content="密码长度应在 6 到 20 个字符之间" placement="right" trigger="hover">
              <InfoFilled class="custom-icon" />
            </el-tooltip>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            native-type="submit"
            :loading="loading"
            class="register-button"
          >
            注册
          </el-button>
        </el-form-item>

        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/authStore' // 引入 authStore
import { ElMessage } from 'element-plus' // 引入 ElMessage
import { InfoFilled } from '@element-plus/icons-vue' // 引入有效图标

// 定义表单和验证规则
const form = ref({
  username: '',
  password: ''
})

const formRef = ref(null)  // 用于表单引用
const router = useRouter()
const authStore = useAuthStore()  // 获取 authStore

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20之间', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  const valid = await formRef.value.validate()  // 验证表单
  if (!valid) return

  try {
    // 调用 authStore 的 register 动作
    await authStore.register({ username: form.value.username, password: form.value.password })
    ElMessage.success("注册成功！");
    // 注册成功后，路由会自动跳转到登录页面
    router.push('/login')
  } catch (error) {
    // 错误信息显示
    ElMessage.error(authStore.error || '注册失败，请稍后再试')
  }
}
</script>

<style scoped>
.register-container {
  width: 400px;
  margin: 100px auto;
  background-color: #f4f7fc;
  padding: 20px;
  border-radius: 8px;
}

.register-card {
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  background-color: white;
}

.register-button {
  width: 100%;
  height: 40px;
}

.login-link {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
}

.login-link a {
  color: #42b983;
  font-weight: bold;
}

.login-link a:hover {
  text-decoration: underline;
}

.el-tooltip .el-tooltip__popper {
  max-width: 200px;
}

.input-with-icon {
  position: relative;
  width: 100%; 
}

.custom-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 14px;  /* 设置图标的宽度 */
  height: 14px; /* 设置图标的高度 */
  fill: rgb(48, 49, 51);  /* 设置颜色 */
}
</style>
