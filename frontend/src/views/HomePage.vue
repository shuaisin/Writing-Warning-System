<template>
  <div class="homepage">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="nav-container">
        <div class="logo">
          <span class="logo-icon">📝</span>
          <span class="logo-text">作文心理分析系统</span>
        </div>
        <div class="nav-links">
          <a href="#" @click.prevent="scrollToSection('features')">功能</a>
          <a href="#" @click.prevent="scrollToSection('about')">关于</a>
        </div>
        <div class="nav-actions">
          <button class="login-btn" @click="goToLogin">登录</button>
        </div>
      </div>
    </nav>

    <!-- 英雄区 Hero -->
    <section class="hero">
      <div class="hero-content">
        <h1>让每一篇作文<br>都成为心灵的窗口</h1>
        <p class="hero-subtitle">
          基于情感分析的智能心理干预系统，帮助老师及时发现学生心理动态，实现精准关怀。
        </p>
        <div class="hero-buttons">
          <button class="btn-primary" @click="goToLogin">开始使用</button>
          <button class="btn-secondary" @click="scrollToSection('features')">了解更多</button>
        </div>
      </div>
      <div class="hero-image">
        <div class="floating-card">🧠 情绪识别</div>
        <div class="floating-card">📈 动态干预</div>
        <div class="floating-card">👥 多角色协同</div>
      </div>
    </section>

    <!-- 功能特性区 -->
    <section id="features" class="features">
      <div class="container">
        <h2 class="section-title">核心功能</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">📚</div>
            <h3>作文智能存档</h3>
            <p>语文老师批量上传作文，系统自动进行情感分析，实时监测学生心理状态。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📊</div>
            <h3>心理波动趋势</h3>
            <p>可视化图表展示学生心理指标变化，帮助老师快速定位需要干预的学生。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔄</div>
            <h3>多级干预流转</h3>
            <p>班主任→心理老师→家长，任务自动流转，确保每个学生得到及时帮助。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💬</div>
            <h3>交流记录与分析</h3>
            <p>班主任和心理老师可记录沟通内容，并填写结构化心理评估报告。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">👥</div>
            <h3>多角色工作台</h3>
            <p>语文老师、班主任、心理老师、管理员，各自拥有专属操作界面。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">⚙️</div>
            <h3>灵活部署</h3>
            <p>支持本地化部署，数据安全可控，未来可接入真实数据库和AI模型。</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 关于/流程介绍 -->
    <section id="about" class="about">
      <div class="container">
        <h2 class="section-title">干预流程</h2>
        <div class="flow-steps">
          <div class="step">
            <div class="step-num">1</div>
            <h4>上传作文</h4>
            <p>语文老师上传学生作文，系统分析情绪倾向</p>
          </div>
          <div class="step-arrow">→</div>
          <div class="step">
            <div class="step-num">2</div>
            <h4>触发干预</h4>
            <p>消极次数≥3次，自动发放一级任务给班主任</p>
          </div>
          <div class="step-arrow">→</div>
          <div class="step">
            <div class="step-num">3</div>
            <h4>班主任介入</h4>
            <p>与学生交流，记录沟通，决策好转或需心理干预</p>
          </div>
          <div class="step-arrow">→</div>
          <div class="step">
            <div class="step-num">4</div>
            <h4>心理老师跟进</h4>
            <p>心理老师评估，决定好转或联系家长</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <p>© 2025 作文心理分析系统 | 守护每一颗心灵</p>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}

const scrollToSection = (sectionId) => {
  const el = document.getElementById(sectionId)
  if (el) el.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.homepage {
  font-family: 'Segoe UI', 'PingFang SC', Roboto, 'Helvetica Neue', sans-serif;
  background: #fefeff;
  color: #1e293b;
}

/* 导航栏 */
.navbar {
  position: sticky;
  top: 0;
  background: rgba(255,255,255,0.9);
  backdrop-filter: blur(12px);
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  z-index: 100;
}
.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 700;
  font-size: 1.4rem;
}
.logo-icon {
  font-size: 1.8rem;
}
.logo-text {
  background: linear-gradient(135deg, #1e293b, #4f46e5);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
}
.nav-links {
  display: flex;
  gap: 2rem;
}
.nav-links a {
  text-decoration: none;
  color: #334155;
  font-weight: 500;
  transition: color 0.2s;
}
.nav-links a:hover {
  color: #4f46e5;
}
.login-btn {
  background: #4f46e5;
  color: white;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 30px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}
.login-btn:hover {
  background: #4338ca;
  transform: scale(0.98);
}

/* 英雄区 */
.hero {
  max-width: 1200px;
  margin: 0 auto;
  padding: 4rem 2rem;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 2rem;
}
.hero-content {
  flex: 1;
  min-width: 280px;
}
.hero-content h1 {
  font-size: 3rem;
  font-weight: 800;
  background: linear-gradient(135deg, #1e293b, #4f46e5);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  line-height: 1.2;
  margin-bottom: 1rem;
}
.hero-subtitle {
  font-size: 1.2rem;
  color: #475569;
  margin-bottom: 2rem;
  line-height: 1.5;
}
.hero-buttons {
  display: flex;
  gap: 1rem;
}
.btn-primary, .btn-secondary {
  padding: 0.7rem 1.8rem;
  border-radius: 40px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}
.btn-primary {
  background: #4f46e5;
  color: white;
  box-shadow: 0 2px 6px rgba(79,70,229,0.3);
}
.btn-primary:hover {
  background: #4338ca;
  transform: translateY(-2px);
}
.btn-secondary {
  background: #f1f5f9;
  color: #1e293b;
}
.btn-secondary:hover {
  background: #e2e8f0;
}
.hero-image {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
}
.floating-card {
  background: white;
  padding: 1rem 1.5rem;
  border-radius: 40px;
  box-shadow: 0 10px 20px -5px rgba(0,0,0,0.1);
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

/* 功能特性 */
.features {
  background: #f8fafc;
  padding: 4rem 2rem;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
}
.section-title {
  text-align: center;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 2.5rem;
  color: #0f172a;
}
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}
.feature-card {
  background: white;
  border-radius: 24px;
  padding: 1.5rem;
  box-shadow: 0 4px 10px rgba(0,0,0,0.02);
  transition: transform 0.2s;
  border: 1px solid #eef2ff;
}
.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 25px -12px rgba(0,0,0,0.08);
}
.feature-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}
.feature-card h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
}
.feature-card p {
  color: #475569;
  line-height: 1.5;
}

/* 流程区 */
.about {
  padding: 4rem 2rem;
}
.flow-steps {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
}
.step {
  background: white;
  border-radius: 32px;
  padding: 1.5rem;
  text-align: center;
  width: 200px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}
.step-num {
  width: 40px;
  height: 40px;
  background: #4f46e5;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  font-weight: bold;
}
.step h4 {
  margin-bottom: 0.5rem;
}
.step-arrow {
  font-size: 1.5rem;
  color: #94a3b8;
}
@media (max-width: 800px) {
  .step-arrow {
    display: none;
  }
  .flow-steps {
    flex-direction: column;
  }
}

/* 页脚 */
.footer {
  background: #0f172a;
  color: #94a3b8;
  text-align: center;
  padding: 2rem;
  font-size: 0.9rem;
}
</style>