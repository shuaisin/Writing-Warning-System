<template> 
  <div class="dashboard-new">
    <!-- 顶部导航栏 -->
    <nav class="navbar">
      <div class="logo">
        <span class="logo-icon">📝</span>
        <span class="logo-text">作文心理分析系统</span>
      </div>
      <div class="user-menu">
        <div class="user-avatar">{{ userRole.charAt(0) }}</div>
        <span class="user-name">{{ userName }}（{{ userRole }}）</span>
        <button class="logout-btn" @click="logout">
          <span>退出</span>
        </button>
      </div>
    </nav>

    <!-- 主要内容区域，选择显示对应角色的工作台面板 -->
    <main class="main-content">
      <div class="work-area">
        <ChineseTeacherPanel v-if="userRole === '语文老师'" />
        <ClassTeacherPanel v-else-if="userRole === '班主任'" />
        <PsychologistPanel v-else-if="userRole === '心理老师'" />
        <AdminPanel v-else-if="userRole === '管理员'" />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ChineseTeacherPanel from '../components/ChineseTeacherPanel.vue'
import ClassTeacherPanel from '../components/ClassTeacherPanel.vue'
import PsychologistPanel from '../components/PsychologistPanel.vue'
import AdminPanel from '../components/AdminPanel.vue'
//导入所需工具和组件
const router = useRouter()
const userRole = ref(localStorage.getItem('userRole') || '')
const userName = ref(localStorage.getItem('userName') || '用户')

const logout = () => {
  localStorage.clear()
  router.push('/login')
}
//退出登录函数，清除本地存储并跳转到登录页
onMounted(() => {
  // 可以在这里做额外初始化
})
</script>

<style scoped>  
.dashboard-new {
  min-height: 100vh;
  background: linear-gradient(145deg, #f0f4ff 0%, #e9eff8 100%);
  font-family: 'Segoe UI', 'PingFang SC', Roboto, sans-serif;
}

/* 导航栏 */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background: rgba(255,255,255,0.8);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 10;
}
.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 700;
  font-size: 1.3rem;
  background: linear-gradient(135deg, #1E293B, #4F46E5);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
}
.logo-icon {
  font-size: 1.8rem;
  background: none;
  color: #4F46E5;
}
.user-menu {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.user-avatar {
  width: 36px;
  height: 36px;
  background: #4F46E5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  text-transform: uppercase;
}
.user-name {
  font-weight: 500;
  color: #1e293b;
}
.logout-btn {
  background: #ef4444;
  border: none;
  padding: 0.4rem 1rem;
  border-radius: 40px;
  color: white;
  cursor: pointer;
  transition: 0.2s;
}
.logout-btn:hover {
  background: #dc2626;
}

/* 主要内容区域 */
.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
}

/* 工作区 */
.work-area {
  margin-top: 0;
}
</style>