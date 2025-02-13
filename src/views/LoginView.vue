<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>系统登录</h2>
      <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginFormRef"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
          />
        </el-form-item>

        <el-form-item label="密&nbsp&nbsp&nbsp码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
            prefix-icon="el-icon-lock"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            native-type="submit"
            :loading="authStore.loading"
            class="login-button"
          >
            登录
          </el-button>
        </el-form-item>

        <div class="register-link">
          没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/authStore' // 确保这里路径正确
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore() // 获取 authStore 实例
const loginFormRef = ref(null);

const loginForm = ref({
  username: '',
  password: ''
})

const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { 
      pattern: /^[a-zA-Z0-9_]{3,20}$/, // 只允许字母、数字和下划线，长度在3到20之间
      message: '用户名3到20个字符，包含字母、数字和下划线',
      trigger: ['blur', 'change']
    }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
})
// 处理登录操作
const handleLogin = async () => {
  try {
    if (!loginFormRef.value) {
      console.error("loginFormRef 未定义");
      return;
    }

    const valid = await loginFormRef.value.validate();
    if (!valid) return;

    console.log("表单验证通过，开始执行登录操作");
    authStore.setLoading(true);

    const response = await authStore.login({ 
      username: loginForm.value.username, 
      password: loginForm.value.password 
    });
    ElMessage.success("登录成功！");
    router.push("/upload");  

  } catch (error) {
    // 登录失败，显示 authStore 中的错误信息
    if (authStore.error) {
      console.error("登录失败，错误信息：", authStore.error);
      ElMessage.error(authStore.error); // 显示错误信息
    }
  } finally {
    authStore.setLoading(false);
  }
};
</script>

<style scoped>
.login-container {
  width: 400px;
  margin: 100px auto;
  background-color: #f4f7fc;
  padding: 20px;
  border-radius: 8px;
}

.login-card {
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  background-color: white;
}

.login-button {
  width: 100%;
  height: 40px;
}

.register-link {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
}

.register-link a {
  color: #42b983;
  font-weight: bold;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
