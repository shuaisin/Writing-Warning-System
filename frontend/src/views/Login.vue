<template>
  <div class="login-container">
    <div class="login-card">
      <h1>📝 作文心理分析系统</h1>
      <p class="subtitle">请选择角色登录（演示）</p>
      <div class="role-buttons">
        <button @click="login('语文老师')" class="role-btn chinese">📚 语文老师</button>
        <button @click="login('班主任')" class="role-btn teacher">👩‍🏫 班主任</button>
        <button @click="login('心理老师')" class="role-btn psych">🧠 心理老师</button>
        <button @click="login('管理员')" class="role-btn admin">⚙️ 管理员</button>
      </div>
      <div class="demo-note">
        <p>演示账号无需密码，点击即可登录</p>
        <p>实际系统将接入真实认证</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const login = (role) => {
  localStorage.setItem('userRole', role)
  localStorage.setItem('userName', role)
  router.push('/dashboard')
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
  padding: 2.5rem;
  width: 90%;
  max-width: 500px;
  text-align: center;
  box-shadow: 0 25px 50px -12px rgba(0,0,0,0.25);
}
.login-card h1 {
  font-size: 2rem;
  background: linear-gradient(135deg, #1E293B, #4F46E5);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  margin-bottom: 0.5rem;
}
.subtitle {
  color: #64748B;
  margin-bottom: 2rem;
}
.role-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
  margin-bottom: 2rem;
}
.role-btn {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  border: none;
  border-radius: 40px;
  cursor: pointer;
  transition: all 0.2s;
  color: white;
  flex: 1 0 auto;
  min-width: 120px;
}
.role-btn.chinese { background: #10B981; }
.role-btn.teacher { background: #3B82F6; }
.role-btn.psych { background: #8B5CF6; }
.role-btn.admin { background: #EF4444; }
.role-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px -5px rgba(0,0,0,0.2);
}
.demo-note p {
  font-size: 0.8rem;
  color: #94A3B8;
  margin: 0.2rem 0;
}
</style>