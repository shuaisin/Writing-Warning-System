<template>
  <div class="dashboard-wrapper">
    <nav class="navbar">
      <div class="logo">
        <span class="logo-text">Writing Warning System</span>
      </div>
      <div class="user-info">
        <div class="avatar">{{ userRole.charAt(0) }}</div>
        <div class="meta">
          <span class="name">{{ userName }}</span>
          <span class="role-tag">{{ userRole }}</span>
        </div>
        <button class="logout-btn" @click="handleLogout">退出</button>
      </div>
    </nav>

    <main class="content-area">
      <div class="container">
        <ChineseTeacherPanel v-if="userRole === '语文老师'" />
        <HeadTeacherPanel v-else-if="userRole === '班主任'" />
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
import PsychologistPanel from '../components/PsychologistPanel.vue'
import AdminPanel from '../components/AdminPanel.vue'
import HeadTeacherPanel from "../components/HeadTeacherPanel.vue";

const router = useRouter()
const userRole = ref('')
const userName = ref('')

onMounted(() => {
  userRole.value = localStorage.getItem('userRole') || ''
  userName.value = localStorage.getItem('userName') || ''
  if (!userRole.value) router.push('/login')
})

const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.dashboard-wrapper {
  min-height: 100vh;
  background-color: #F8FAFC;
}
.navbar {
  height: 70px;
  background: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}
.logo {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  font-size: 1.4rem;
  font-weight: 700;
  color: #1E293B;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.avatar {
  width: 40px;
  height: 40px;
  background: #4F46E5;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}
.meta { display: flex; flex-direction: column; }
.name { font-size: 0.95rem; font-weight: 600; color: #1E293B; }
.role-tag { font-size: 0.75rem; color: #64748B; }
.logout-btn {
  margin-left: 1rem;
  padding: 0.5rem 1rem;
  border: 1px solid #E2E8F0;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s;
}
.logout-btn:hover { background: #F1F5F9; color: #EF4444; border-color: #EF4444; }
.content-area { padding: 2rem; }
.container { max-width: 1200px; margin: 0 auto; }
</style>
