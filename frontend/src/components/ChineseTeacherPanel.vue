<template>
  <div class="panel-container">
    <section class="card upload-section">
      <div class="card-header">
        <h3>🚀 心理指标深度诊断 (KG + 隐喻识别)</h3>
        <span class="badge">AI 赋能</span>
      </div>
      
      <div class="form-body">
        <div class="input-row">
          <label>学生学号：</label>
          <input v-model="form.studentId" placeholder="例如: 20230001" class="custom-input" />
        </div>
        
        <div class="input-row">
          <label>作文内容：</label>
          <textarea 
            v-model="form.content" 
            placeholder="在此粘贴学生作文正文，AI 将通过知识图谱识别其中的心理隐喻词汇..." 
            rows="8"
            class="custom-textarea"
          ></textarea>
        </div>

        <div class="action-row">
          <button @click="submitAnalysis" :disabled="loading" class="submit-btn">
            {{ loading ? '深度分析中，正在检索知识图谱...' : '开始执行 AI 诊断' }}
          </button>
        </div>
      </div>
    </section>

    <section class="card history-section">
      <div class="card-header">
        <h3>📋 诊断报告记录</h3>
        <button @click="fetchHistory" class="refresh-btn">🔄 刷新列表</button>
      </div>

      <div v-if="history.length === 0" class="empty-state">
        暂无诊断记录，请在上方提交作文。
      </div>

      <div class="report-grid">
        <div v-for="item in history" :key="item.id" class="report-card">
          <div class="report-main">
            <div class="report-top">
              <span class="stu-name">{{ item.student?.name }} ({{ item.student?.studentId }})</span>
              <span :class="['status-tag', item.hasPsychologicalIssue ? 'danger' : 'normal']">
                {{ item.hasPsychologicalIssue ? '检出异常' : '心理健康' }}
              </span>
            </div>
            <div class="report-content">
              <strong>诊断简报：</strong>
              <p>{{ item.analysisResult || '暂无分析内容' }}</p>
            </div>
            <div class="report-footer">
              <span class="time">提交时间: {{ formatTime(item.submitTime) }}</span>
              <span v-if="item.transferredToPsychologist" class="transfer-badge">已转办心理老师</span>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import api from '../api/api'

const loading = ref(false)
const history = ref([])
const form = reactive({
  studentId: '',
  content: ''
})

// 获取当前老师的分析历史
const fetchHistory = async () => {
  const teacherId = localStorage.getItem('userId')
  try {
    const data = await api.get(`/compositions/teacher/${teacherId}`)
    history.value = data.reverse() // 最新的在前面
  } catch (err) { console.error('获取历史失败', err) }
}

// 提交作文并触发后端计算逻辑
const submitAnalysis = async () => {
  if (!form.studentId || !form.content) return alert('请填写完整信息')
  
  loading.value = true
  const teacherId = localStorage.getItem('userId')
  
  try {
    // 对应你后端的 CompositionImportDTO 结构
    await api.post('/compositions/import', {
      chineseTeacherId: teacherId,
      compositions: [{
        studentId: form.studentId,
        content: form.content
      }]
    })
    
    alert('AI 分析完成！已更新诊断报告。')
    form.content = ''
    form.studentId = ''
    await fetchHistory()
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return '-'
  return new Date(timeStr).toLocaleString()
}

onMounted(fetchHistory)
</script>

<style scoped>
.panel-container { display: flex; flex-direction: column; gap: 2rem; }
.card { background: white; border-radius: 20px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); overflow: hidden; }
.card-header { 
  padding: 1.2rem 1.5rem; 
  background: #F1F5F9; 
  display: flex; 
  justify-content: space-between; 
  align-items: center;
  border-bottom: 1px solid #E2E8F0;
}
.card-header h3 { font-size: 1.1rem; color: #1E293B; margin: 0; }
.badge { background: #4F46E5; color: white; padding: 2px 8px; border-radius: 6px; font-size: 0.75rem; }
.form-body { padding: 1.5rem; }
.input-row { margin-bottom: 1.2rem; display: flex; flex-direction: column; gap: 0.5rem; }
.input-row label { font-weight: 600; color: #475569; font-size: 0.9rem; }
.custom-input { padding: 0.8rem; border: 1px solid #CBD5E1; border-radius: 10px; font-size: 1rem; }
.custom-textarea { padding: 1rem; border: 1px solid #CBD5E1; border-radius: 10px; font-size: 1rem; line-height: 1.6; resize: none; }
.submit-btn { 
  width: 100%; padding: 1.2rem; background: #4F46E5; color: white; border: none; 
  border-radius: 12px; font-weight: bold; font-size: 1.1rem; cursor: pointer; transition: 0.3s;
}
.submit-btn:hover { background: #4338CA; transform: translateY(-2px); }
.submit-btn:disabled { background: #94A3B8; cursor: not-allowed; }

.report-grid { padding: 1.5rem; display: grid; grid-template-columns: 1fr; gap: 1rem; }
.report-card { border: 1px solid #E2E8F0; border-radius: 12px; padding: 1.2rem; transition: 0.2s; }
.report-card:hover { border-color: #4F46E5; background: #F8FAFC; }
.report-top { display: flex; justify-content: space-between; margin-bottom: 1rem; }
.stu-name { font-weight: bold; color: #1E293B; }
.status-tag { padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: bold; }
.status-tag.danger { background: #FEE2E2; color: #EF4444; }
.status-tag.normal { background: #DCFCE7; color: #166534; }
.report-content { font-size: 0.95rem; color: #475569; line-height: 1.6; margin-bottom: 1rem; }
.report-footer { display: flex; justify-content: space-between; font-size: 0.8rem; color: #94A3B8; border-top: 1px solid #F1F5F9; pt: 0.8rem; }
.transfer-badge { color: #F59E0B; font-weight: bold; }
.refresh-btn { background: none; border: 1px solid #CBD5E1; padding: 4px 12px; border-radius: 8px; cursor: pointer; }
.empty-state { padding: 3rem; text-align: center; color: #94A3B8; }
</style>
