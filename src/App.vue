<template>
  <div id="app">
    <!-- 导航栏 -->
    <nav v-if="$route.meta.showNav !== false">
      <div class="nav-left">
        <router-link to="/upload">上传</router-link> |
        <router-link to="/management">管理</router-link>
      </div>
      <div class="nav-right">
        <!-- 显示当前用户名 -->
        <span v-if="username">{{ username }}</span>
        
        <!-- 登出按钮和登录按钮的条件渲染 -->
        <button v-if="isAuthenticated" @click="logout">登出</button>
        <router-link v-else to="/login">
          <button>登录</button>
        </router-link>
      </div>
    </nav>

    <!-- 路由视图：这里会根据当前路由显示对应的组件 -->
    <router-view></router-view>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/authStore'
import { useRouter } from 'vue-router'
import { computed } from 'vue'

export default {
  name: 'App',
  computed: {
    // 从 Pinia store 获取用户名和认证状态
    username() {
      const authStore = useAuthStore()
      return authStore.username
    },
    isAuthenticated() {
      const authStore = useAuthStore()
      console.log("当前认证状态 isAuthenticated:", authStore.isAuthenticated); // 打印当前认证状态
      return authStore.isAuthenticated
    }
  },
  methods: {
    // 登出操作
    logout() {
      const authStore = useAuthStore()
      authStore.logout() // 清除认证信息
      this.$router.push("/login")  // 登出后跳转到登录页
    }
  }
}
</script>

<style scoped>
/* 添加一些全局样式 */
nav {
  display: flex;
  justify-content: space-between;  /* 两边对齐 */
  align-items: center;  /* 垂直居中 */
  padding: 20px 30px;
  background-color: #f8f9fa;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none;
  padding: 0 10px;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.nav-left {
  font-weight: bold;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-right span {
  font-weight: bold;
  color: #333;
  font-size: 16px;
}

.nav-right button {
  padding: 8px 15px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.nav-right button:hover {
  background-color: #36a77b;
}

.nav-right button:focus {
  outline: none;
}
</style>
