<template>
  <div class="teacher-panel">
    <!-- 学生选择栏 -->
    <div class="student-bar">
      <label>当前学生：</label>
      <select v-model="currentStudent" class="student-select">
        <option value="小明">小明</option>
        <option value="小红">小红</option>
      </select>
      <button class="btn-refresh" @click="refreshData">🔄 刷新</button>
    </div>

    <!-- 模拟评估触发按钮 -->
    <div class="action-bar">
      <button class="btn btn-trigger" @click="triggerIntervention">
        ⚠️ 模拟系统评估（消极≥3次）→ 发放一级任务
      </button>
    </div>

    <!-- 心理波动图 -->
    <div class="card chart-card">
      <h3>📈 心理波动趋势</h3>
      <div ref="chartRef" class="chart-container"></div>
    </div>

    <!-- 两列布局 -->
    <div class="two-columns">
      <!-- 左侧任务列表 -->
      <div class="card">
        <h3>📋 干预任务列表</h3>
        <div v-if="tasks.length === 0" class="empty-state">暂无任务，点击上方按钮触发</div>
        <div v-for="t in tasks" :key="t.id" class="task-item">
          <div class="task-info">
            <span class="task-level" :class="'level-' + t.level">{{ t.level === 1 ? '一级' : '二级' }}</span>
            <span class="task-status">{{ t.status }}</span>
            <span class="task-assigned">👤 {{ t.assignedTo }}</span>
          </div>
          <div class="task-actions" v-if="t.status === 'pending' && t.level === 1">
            <button class="btn-action good" @click="handleTeacherDecision('好转')">✅ 好转</button>
            <button class="btn-action warn" @click="handleTeacherDecision('需心理干预')">🔄 需心理干预</button>
          </div>
        </div>
      </div>

      <!-- 右侧交流与分析 -->
      <div class="card">
        <h3>💬 交流记录 & 心理分析</h3>
        <!-- 快捷交流 -->
        <div class="note-input">
          <textarea v-model="noteContent" placeholder="输入简短沟通内容..." rows="2"></textarea>
          <button class="btn-submit" @click="submitNote">✉️ 提交记录</button>
        </div>

        <!-- 心理分析表单 -->
        <div class="analysis-form">
          <h4>📝 心理多维分析记录</h4>
          <div class="analysis-field">
            <label>学业压力：</label>
            <input type="text" v-model="analysisForm.academicPressure" placeholder="描述学业压力...">
          </div>
          <div class="analysis-field">
            <label>人际关系：</label>
            <input type="text" v-model="analysisForm.socialRelationship" placeholder="描述人际关系...">
          </div>
          <div class="analysis-field">
            <label>情绪状态：</label>
            <input type="text" v-model="analysisForm.emotionalState" placeholder="描述情绪状态...">
          </div>
          <div class="analysis-field">
            <label>自我认知：</label>
            <input type="text" v-model="analysisForm.selfCognition" placeholder="描述自我认知...">
          </div>
          <div class="analysis-field">
            <label>专家报告：</label>
            <textarea v-model="analysisForm.expertReport" placeholder="综合性分析与建议..." rows="3"></textarea>
          </div>
          <div class="analysis-field">
            <label>其他补充：</label>
            <textarea v-model="analysisForm.extra" placeholder="任何额外信息..." rows="2"></textarea>
          </div>
          <button class="btn-submit" @click="submitAnalysis">📋 保存心理分析记录</button>
        </div>

        <!-- 历史分析记录 -->
        <div v-if="analysisHistory.length > 0" class="analysis-history">
          <h4>📜 历史心理分析记录</h4>
          <div v-for="(a, idx) in analysisHistory" :key="idx" class="analysis-item">
            <div class="analysis-meta">【{{ a.role }}】{{ a.timestamp }}</div>
            <div><strong>学业压力：</strong>{{ a.academicPressure || '—' }}</div>
            <div><strong>人际关系：</strong>{{ a.socialRelationship || '—' }}</div>
            <div><strong>情绪状态：</strong>{{ a.emotionalState || '—' }}</div>
            <div><strong>自我认知：</strong>{{ a.selfCognition || '—' }}</div>
            <div><strong>专家报告：</strong>{{ a.expertReport || '—' }}</div>
            <div v-if="a.extra"><strong>补充：</strong>{{ a.extra }}</div>
          </div>
        </div>

        <!-- 交流记录列表 -->
        <div class="note-list">
          <div v-for="(n, idx) in notes" :key="idx" class="note-item">
            <div class="note-meta">【{{ n.role }}】</div>
            <div class="note-content">{{ n.content }}</div>
          </div>
          <div v-if="notes.length === 0 && analysisHistory.length === 0" class="empty-state">暂无交流或分析记录</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

const currentStudent = ref('小明')
const noteContent = ref('')
const tasks = ref([])
const notes = ref([])
const analysisHistory = ref([])
const chartRef = ref(null)

const analysisForm = ref({
  academicPressure: '', socialRelationship: '', emotionalState: '', selfCognition: '', expertReport: '', extra: ''
})

// 加载任务、交流、分析、图表
const loadTasks = async () => {
  const res = await axios.get(`http://localhost:8080/api/task/${currentStudent.value}`)
  tasks.value = res.data
}
const loadNotes = async () => {
  const res = await axios.get(`http://localhost:8080/api/note/${currentStudent.value}`)
  notes.value = res.data
}
const loadAnalysis = async () => {
  const res = await axios.get(`http://localhost:8080/api/analysis/${currentStudent.value}`)
  analysisHistory.value = res.data
}
const loadChart = async () => {
  const res = await axios.get('http://localhost:8080/api/metrics')
  const chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: res.data.map(i => i.time), name: '时间' },
    yAxis: { type: 'value', name: '心理指标分数' },
    series: [{
      type: 'line', data: res.data.map(i => i.score), smooth: true,
      lineStyle: { color: '#4F46E5', width: 3 },
      areaStyle: { opacity: 0.1, color: '#818CF8' },
      symbol: 'circle', symbolSize: 6, itemStyle: { color: '#4F46E5' }
    }]
  })
}
const refreshData = async () => {
  await loadTasks()
  await loadNotes()
  await loadAnalysis()
  await loadChart()
}

// 触发任务
const triggerIntervention = async () => {
  await axios.post(`http://localhost:8080/api/task/trigger/${currentStudent.value}`)
  alert('已触发一级任务')
  await refreshData()
}

// 班主任决策
const handleTeacherDecision = async (decision) => {
  await axios.post('http://localhost:8080/api/task/teacher/complete', {
    studentId: currentStudent.value, decision
  })
  alert(decision === '好转' ? '流程结束' : '已转交心理老师')
  await refreshData()
}

// 提交交流
const submitNote = async () => {
  if (!noteContent.value.trim()) return
  await axios.post('http://localhost:8080/api/note', {
    studentId: currentStudent.value, role: '班主任', content: noteContent.value
  })
  noteContent.value = ''
  await loadNotes()
}

// 提交分析
const submitAnalysis = async () => {
  const form = analysisForm.value
  if (!form.academicPressure && !form.socialRelationship && !form.emotionalState && !form.selfCognition && !form.expertReport) {
    alert('请至少填写一个字段')
    return
  }
  await axios.post('http://localhost:8080/api/analysis', {
    studentId: currentStudent.value, role: '班主任',
    ...form
  })
  alert('分析记录已保存')
  analysisForm.value = { academicPressure: '', socialRelationship: '', emotionalState: '', selfCognition: '', expertReport: '', extra: '' }
  await loadAnalysis()
}

watch(currentStudent, () => refreshData())
onMounted(() => refreshData())
</script>

<style scoped>
/* 完全保留你原有的所有美观样式，并增加分析表单相关样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
  font-family: 'Segoe UI', 'PingFang SC', Roboto, 'Helvetica Neue', sans-serif;
  background: #f8fafc;
  min-height: 100vh;
}

/* 头部 */
.header {
  text-align: center;
  margin-bottom: 2rem;
}

.header h1 {
  font-size: 2.2rem;
  background: linear-gradient(135deg, #1E293B, #4F46E5);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: #64748B;
  font-size: 1rem;
}

/* 学生选择栏 */
.student-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  background: white;
  padding: 0.5rem 1rem;
  border-radius: 60px;
  width: fit-content;
  margin-left: auto;
  margin-right: auto;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.student-select, .role-select {
  padding: 0.3rem 0.8rem;
  border-radius: 30px;
  border: 1px solid #CBD5E1;
  background: white;
  font-size: 0.9rem;
}

.btn-refresh {
  background: #64748B;
  color: white;
  border: none;
  padding: 0.3rem 1rem;
  border-radius: 30px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: 0.2s;
}
.btn-refresh:hover {
  background: #475569;
}

/* 触发按钮 */
.btn-trigger {
  background: #DC2626;
  color: white;
  padding: 0.7rem 1.8rem;
  font-size: 1rem;
  font-weight: 600;
  border: none;
  border-radius: 40px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
}
.btn-trigger:hover {
  background: #B91C1C;
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(220, 38, 38, 0.3);
}

/* 卡片通用样式 */
.card {
  background: white;
  border-radius: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  margin-bottom: 2rem;
  transition: box-shadow 0.2s;
}
.card:hover {
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
}
.card h3 {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: #1E293B;
  border-left: 5px solid #4F46E5;
  padding-left: 0.8rem;
}

/* 图表区域 */
.chart-card {
  padding: 1rem;
}
.chart-container {
  width: 100%;
  height: 320px;
}

/* 两列布局 */
.two-columns {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}
@media (max-width: 800px) {
  .two-columns {
    grid-template-columns: 1fr;
  }
}

/* 左侧任务列表样式 */
.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #F1F5F9;
  padding: 0.75rem 1rem;
  border-radius: 16px;
  margin-bottom: 0.6rem;
  flex-wrap: wrap;
  gap: 0.5rem;
}
.task-info {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  flex-wrap: wrap;
}
.task-level {
  font-weight: 700;
  padding: 0.2rem 0.8rem;
  border-radius: 30px;
  font-size: 0.85rem;
  background: #E2E8F0;
  color: #334155;
}
.level-1 { background: #DBEAFE; color: #1E40AF; }
.level-2 { background: #D1FAE5; color: #065F46; }
.task-status {
  background: white;
  padding: 0.2rem 0.7rem;
  border-radius: 30px;
  font-size: 0.8rem;
  font-weight: 500;
  color: #0F172A;
}
.task-assigned {
  font-size: 0.7rem;
  background: #E2E8F0;
  padding: 0.2rem 0.6rem;
  border-radius: 20px;
}
.task-actions {
  display: flex;
  gap: 0.5rem;
}
.btn-action {
  font-size: 0.7rem;
  padding: 0.3rem 0.8rem;
  border-radius: 30px;
  border: none;
  cursor: pointer;
  font-weight: 500;
  transition: 0.1s;
}
.good { background: #10B981; color: white; }
.warn { background: #F59E0B; color: white; }
.btn-action:hover { transform: scale(0.96); }

/* 右侧交流与表单样式 */
.note-input {
  display: flex;
  gap: 0.8rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  align-items: flex-start;
}
.note-input textarea {
  flex: 1;
  padding: 0.6rem 1rem;
  border-radius: 20px;
  border: 1px solid #CBD5E1;
  font-family: inherit;
  font-size: 0.9rem;
  resize: vertical;
  transition: 0.2s;
}
.note-input textarea:focus {
  outline: none;
  border-color: #4F46E5;
  box-shadow: 0 0 0 2px rgba(79, 70, 229, 0.2);
}
.btn-submit {
  background: #6366F1;
  color: white;
  padding: 0.5rem 1.2rem;
  border-radius: 30px;
  font-weight: 500;
  white-space: nowrap;
  border: none;
  cursor: pointer;
}
.btn-submit:hover {
  background: #4F46E5;
  transform: scale(0.98);
}

/* 分析表单新增样式 */
.analysis-form {
  background: #F9FAFB;
  border-radius: 24px;
  padding: 1rem;
  margin: 1rem 0;
  border: 1px solid #E2E8F0;
}
.analysis-form h4 {
  margin-bottom: 0.8rem;
  color: #1E293B;
  font-weight: 600;
}
.analysis-field {
  margin-bottom: 0.8rem;
  display: flex;
  flex-direction: column;
}
.analysis-field label {
  font-weight: 600;
  font-size: 0.85rem;
  color: #334155;
  margin-bottom: 0.2rem;
}
.analysis-field input, .analysis-field textarea {
  border: 1px solid #CBD5E1;
  border-radius: 16px;
  padding: 0.5rem 0.8rem;
  font-family: inherit;
  font-size: 0.85rem;
  transition: 0.2s;
}
.analysis-field input:focus, .analysis-field textarea:focus {
  outline: none;
  border-color: #4F46E5;
  box-shadow: 0 0 0 2px rgba(79, 70, 229, 0.2);
}

.analysis-history {
  margin-top: 1.2rem;
  border-top: 1px dashed #CBD5E1;
  padding-top: 0.8rem;
}
.analysis-item {
  background: #F1F5F9;
  border-radius: 20px;
  padding: 0.8rem;
  margin-bottom: 0.8rem;
}
.analysis-meta {
  font-size: 0.7rem;
  color: #4F46E5;
  margin-bottom: 0.4rem;
}
.analysis-item div {
  font-size: 0.85rem;
  margin-top: 0.2rem;
  word-break: break-word;
}

/* 交流记录列表 */
.note-list {
  max-height: 400px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}
.note-item {
  background: #F8FAFE;
  border-radius: 18px;
  padding: 0.8rem 1rem;
  border-left: 4px solid #818CF8;
  transition: 0.1s;
}
.note-meta {
  font-weight: bold;
  font-size: 0.75rem;
  color: #4F46E5;
  margin-bottom: 0.3rem;
}
.note-content {
  font-size: 0.9rem;
  color: #1E293B;
  word-break: break-word;
}
.empty-state {
  text-align: center;
  color: #94A3B8;
  padding: 2rem;
  font-size: 0.9rem;
}
</style>
