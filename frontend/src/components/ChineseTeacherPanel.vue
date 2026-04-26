<template>
  <div class="chinese-panel">
    <!-- 文字作文上传卡片 -->
    <div class="card upload-card text-card">
      <h3>📝 上传文字作文</h3>
      <div class="upload-area">
        <div class="student-selector">
          <label>选择学生：</label>
          <select v-model="selectedStudentText">
            <option value="小明">小明</option>
            <option value="小红">小红</option>
          </select>
        </div>
        <textarea 
          v-model="textContent" 
          placeholder="在此粘贴学生作文内容..." 
          rows="4"
        ></textarea>
        <button class="btn-submit" @click="uploadTextEssay">🚀 上传并分析</button>
      </div>
    </div>

    <!-- 图片作文上传卡片 -->
    <div class="card upload-card image-card">
      <h3>📸 上传图片作文</h3>
      <div class="upload-area">
        <div class="student-selector">
          <label>选择学生：</label>
          <select v-model="selectedStudentImage">
            <option value="小明">小明</option>
            <option value="小红">小红</option>
          </select>
        </div>
        <div class="image-upload">
          <input 
            type="file" 
            accept="image/*" 
            ref="fileInput" 
            @change="handleFileChange"
            style="display: none"
          />
          <button class="btn-upload" @click="triggerFileSelect">
            📁 选择图片
          </button>
          <div v-if="previewUrl" class="image-preview">
            <img :src="previewUrl" alt="预览" />
            <button class="btn-clear" @click="clearPreview">✖</button>
          </div>
          <button 
            class="btn-submit" 
            @click="uploadImageEssay" 
            :disabled="!selectedFile"
            :class="{ disabled: !selectedFile }"
          >
            🚀 上传并分析
          </button>
        </div>
      </div>
    </div>

    <!-- 历史作文记录窗口（同时显示文字和图片） -->
    <div class="card history-card">
      <h3>📚 已上传作文记录</h3>
      <div v-if="essayHistory.length === 0" class="empty-state">
        暂无作文记录，请上传文字或图片作文。
      </div>
      <div class="essay-grid">
        <div v-for="essay in essayHistory" :key="essay.id" class="essay-card">
          <!-- 如果是图片作文显示缩略图，文字作文显示文本预览 -->
          <div v-if="essay.type === 'image'" class="essay-thumb">
            <img :src="essay.imageUrl" alt="作文图片" />
          </div>
          <div v-else class="essay-thumb text-preview">
            <div class="text-preview-content">{{ essay.content.slice(0, 80) }}...</div>
          </div>
          <div class="essay-info">
            <div class="essay-student">👩‍🎓 {{ essay.studentId }}</div>
            <div class="essay-time">🕒 {{ formatTime(essay.time) }}</div>
            <div class="essay-sentiment" :class="essay.sentiment === '消极' ? 'negative' : 'positive'">
              {{ essay.sentiment }} 情绪
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

// 文字上传相关
const selectedStudentText = ref('小明')
const textContent = ref('')

// 图片上传相关
const selectedStudentImage = ref('小明')
const selectedFile = ref(null)
const previewUrl = ref('')
const fileInput = ref(null)

// 历史记录
const essayHistory = ref([])

// 文字上传
const uploadTextEssay = async () => {
  if (!textContent.value.trim()) {
    alert('请输入作文内容')
    return
  }
  try {
    const res = await axios.post('http://localhost:8080/api/essay/upload/text', {
      studentId: selectedStudentText.value,
      content: textContent.value
    })
    alert(res.data.message + '\n' + res.data.triggerMessage)
    textContent.value = ''
    await loadEssayHistory()
  } catch (err) {
    console.error(err)
    alert('上传失败：' + err.message)
  }
}

// 图片上传相关函数
const triggerFileSelect = () => {
  fileInput.value.click()
}
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    selectedFile.value = file
    const reader = new FileReader()
    reader.onload = (e) => {
      previewUrl.value = e.target.result
    }
    reader.readAsDataURL(file)
  }
}
const clearPreview = () => {
  selectedFile.value = null
  previewUrl.value = ''
  fileInput.value.value = ''
}
const uploadImageEssay = async () => {
  if (!selectedFile.value) {
    alert('请先选择图片')
    return
  }
  const formData = new FormData()
  formData.append('studentId', selectedStudentImage.value)
  formData.append('image', selectedFile.value)
  try {
    const res = await axios.post('http://localhost:8080/api/essay/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    alert(res.data.message + '\n' + res.data.triggerMessage)
    clearPreview()
    await loadEssayHistory()
  } catch (err) {
    console.error(err)
    alert('上传失败：' + err.message)
  }
}

// 加载历史记录（文字+图片混合）
const loadEssayHistory = async () => {
  // 由于历史接口是全局的，我们用当前选中的学生（默认取文字上传的学生，可独立）
  // 为了方便，我们监听两个学生变更都会重新加载，这里统一使用一个学生变量（可以是文本下拉框的，也可以做统一）
  // 简单起见，我们用 selectedStudentText 作为历史查询的学生
  try {
    const res = await axios.get(`http://localhost:8080/api/essay/history/${selectedStudentText.value}`)
    essayHistory.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return `${date.getMonth()+1}/${date.getDate()} ${date.getHours()}:${date.getMinutes().toString().padStart(2,'0')}`
}

// 监听学生选择（文字）变化，重新加载历史
watch(selectedStudentText, () => {
  loadEssayHistory()
})

onMounted(() => {
  loadEssayHistory()
})
</script>

<style scoped>
/* 卡片基础样式 */
.chinese-panel {
  max-width: 1200px;
  margin: 0 auto;
}
.card {
  background: white;
  border-radius: 28px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  transition: box-shadow 0.2s;
}
.card:hover {
  box-shadow: 0 12px 24px rgba(0,0,0,0.08);
}
.card h3 {
  border-left: 5px solid #10B981;
  padding-left: 1rem;
  margin-bottom: 1.2rem;
  font-weight: 600;
  color: #1e293b;
}

/* 上传区域 */
.upload-area {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.student-selector {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.student-selector select {
  padding: 0.4rem 1rem;
  border-radius: 30px;
  border: 1px solid #cbd5e1;
  background: white;
}
textarea {
  width: 100%;
  padding: 0.8rem;
  border-radius: 20px;
  border: 1px solid #cbd5e1;
  font-family: inherit;
  resize: vertical;
}
.image-upload {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 1rem;
}
.btn-upload, .btn-submit {
  padding: 0.5rem 1.2rem;
  border-radius: 40px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  transition: 0.2s;
}
.btn-upload {
  background: #f1f5f9;
  color: #1e293b;
}
.btn-upload:hover {
  background: #e2e8f0;
}
.btn-submit {
  background: #10B981;
  color: white;
}
.btn-submit:hover:not(.disabled) {
  background: #059669;
  transform: scale(0.98);
}
.btn-submit.disabled {
  background: #94a3b8;
  cursor: not-allowed;
}
.image-preview {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
}
.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.btn-clear {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 50%;
  width: 22px;
  height: 22px;
  font-size: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 历史记录网格 */
.essay-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 1.2rem;
  margin-top: 1rem;
}
.essay-card {
  background: #f8fafc;
  border-radius: 20px;
  overflow: hidden;
  transition: transform 0.2s;
  border: 1px solid #eef2ff;
}
.essay-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.05);
}
.essay-thumb {
  height: 160px;
  overflow: hidden;
}
.essay-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.essay-thumb.text-preview {
  background: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 1rem;
  font-size: 0.85rem;
  color: #334155;
  line-height: 1.4;
}
.essay-info {
  padding: 0.8rem;
}
.essay-student, .essay-time {
  font-size: 0.85rem;
  color: #475569;
  margin-bottom: 0.3rem;
}
.essay-sentiment {
  display: inline-block;
  padding: 0.2rem 0.7rem;
  border-radius: 30px;
  font-size: 0.75rem;
  font-weight: 600;
  margin-top: 0.4rem;
}
.essay-sentiment.positive {
  background: #d1fae5;
  color: #065f46;
}
.essay-sentiment.negative {
  background: #fee2e2;
  color: #991b1b;
}
.empty-state {
  text-align: center;
  color: #94a3b8;
  padding: 2rem;
}
</style>