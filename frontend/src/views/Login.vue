<template>
  <div class="login-container">
    <div class="login-card">
      <h1>Writing Warning System</h1>
      <p class="subtitle">学生心理健康作文分析系统</p>
      
      <div class="role-buttons">
        <button @click="handleLogin('语文老师')" class="role-btn chinese">
          语文老师登录
        </button>
        <button @click="handleLogin('班主任')" class="role-btn teacher">
          班主任登录
        </button>
        <button @click="handleLogin('心理老师')" class="role-btn psych">
          心理老师登录
        </button>
        <button @click="handleLogin('管理员')" class="role-btn admin">
          管理员登录
        </button>
      </div>

      <div class="demo-note">
        <p>系统将自动根据角色分配 ID 并连接数据库</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { roleMap } from '../api/api'

const router = useRouter()

const handleLogin = (roleName) => {
  // 这里应调用 api.post('/users/login', {username, password})
  // 现在后端还没写登录注册逻辑所以仅做模拟-存储关键的用户上下文信息-后续需补全！
  localStorage.setItem('userRole', roleName);
  localStorage.setItem('roleEnum', roleMap[roleName]);
  localStorage.setItem('userName', roleName + '测试账号');
  
  // 模拟 ID，实际应由后端登录接口返回
  localStorage.setItem('userId', '1'); 
  localStorage.setItem('schoolId', '1'); 

  router.push('/dashboard');
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  background: white;
  border-radius: 32px;
  padding: 3rem;
  width: 100%;
  max-width: 480px;
  text-align: center;
  box-shadow: 0 25px 50px -12px rgba(0,0,0,0.25);
}
.login-card h1 {
  font-size: 2.2rem;
  background: linear-gradient(135deg, #1E293B, #4F46E5);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  margin-bottom: 0.5rem;
  font-weight: 800;
}
.subtitle {
  color: #64748B;
  margin-bottom: 2.5rem;
  font-size: 1.1rem;
}
.role-buttons {
  display: grid;
  gap: 1.2rem;
}
.role-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 1.2rem;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  font-size: 1.1rem;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.role-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1);
}
.chinese { background: #E0F2FE; color: #0369A1; }
.teacher { background: #FEF3C7; color: #92400E; }
.psych { background: #DCFCE7; color: #166534; }
.admin { background: #F3F4F6; color: #374151; }
.demo-note { margin-top: 2rem; color: #94A3B8; font-size: 0.9rem; }
</style>
