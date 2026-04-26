<template>
  <div class="admin-panel">
    <div class="card">
      <h3>👥 用户管理</h3>

      <!-- 角色筛选栏 -->
      <div class="filter-bar">
        <button
          v-for="role in ['全部', '语文老师', '班主任', '心理老师']"
          :key="role"
          class="filter-btn"
          :class="{ active: currentFilter === role }"
          @click="currentFilter = role"
        >
          {{ role }}
        </button>
      </div>

      <!-- 用户列表表格 -->
      <div class="user-table-wrapper">
        <table class="user-table">
          <thead>
            <tr>
              <th>头像</th>
              <th>用户名</th>
              <th>角色</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in filteredUsers" :key="user.id">
              <td class="avatar-cell">
                <div class="avatar" :style="{ background: getAvatarColor(user.name) }">
                  {{ user.name.charAt(0) }}
                </div>
              </td>
              <td>
                <span v-if="editingId !== user.id">{{ user.name }}</span>
                <input v-else v-model="editName" class="edit-input" />
              </td>
              <td>
                <span v-if="editingId !== user.id">{{ user.role }}</span>
                <select v-else v-model="editRole" class="edit-select">
                  <option value="语文老师">语文老师</option>
                  <option value="班主任">班主任</option>
                  <option value="心理老师">心理老师</option>
                </select>
              </td>
              <td class="actions">
                <template v-if="editingId === user.id">
                  <button class="action-btn save" @click="saveUser(user.id)">保存</button>
                  <button class="action-btn cancel" @click="cancelEdit">取消</button>
                </template>
                <template v-else>
                  <button class="action-btn edit" @click="startEdit(user)">编辑</button>
                  <button class="action-btn delete" @click="deleteUser(user.id)">删除</button>
                </template>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 添加用户表单 -->
      <div class="add-user-section">
        <h4>➕ 添加新用户</h4>
        <div class="add-form">
          <input v-model="newUserName" placeholder="用户名" class="add-input" />
          <select v-model="newUserRole" class="add-select">
            <option value="语文老师">语文老师</option>
            <option value="班主任">班主任</option>
            <option value="心理老师">心理老师</option>
          </select>
          <button class="btn-add" @click="addUser">添加用户</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const users = ref([])
const editingId = ref(null)
const editName = ref('')
const editRole = ref('')
const newUserName = ref('')
const newUserRole = ref('语文老师')
const currentFilter = ref('全部')

const filteredUsers = computed(() => {
  if (currentFilter.value === '全部') return users.value
  return users.value.filter(u => u.role === currentFilter.value)
})

const getAvatarColor = (name) => {
  const colors = ['#10B981', '#3B82F6', '#8B5CF6', '#F59E0B', '#EF4444', '#06B6D4']
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return colors[Math.abs(hash) % colors.length]
}

const fetchUsers = async () => {
  const res = await axios.get('http://localhost:8080/api/users')
  users.value = res.data
}

const startEdit = (user) => {
  editingId.value = user.id
  editName.value = user.name
  editRole.value = user.role
}

const cancelEdit = () => {
  editingId.value = null
  editName.value = ''
  editRole.value = ''
}

const saveUser = async (id) => {
  await axios.put(`http://localhost:8080/api/users/${id}`, {
    name: editName.value,
    role: editRole.value
  })
  cancelEdit()
  await fetchUsers()
}

const deleteUser = async (id) => {
  if (confirm('确定要删除该用户吗？')) {
    await axios.delete(`http://localhost:8080/api/users/${id}`)
    await fetchUsers()
  }
}

const addUser = async () => {
  if (!newUserName.value.trim()) {
    alert('请输入用户名')
    return
  }
  await axios.post('http://localhost:8080/api/users', {
    name: newUserName.value,
    role: newUserRole.value
  })
  newUserName.value = ''
  await fetchUsers()
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.admin-panel {
  max-width: 1000px;
  margin: 0 auto;
}
.card {
  background: white;
  border-radius: 28px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  transition: box-shadow 0.2s;
}
.card:hover {
  box-shadow: 0 12px 24px rgba(0,0,0,0.08);
}
.card h3 {
  border-left: 5px solid #EF4444;
  padding-left: 1rem;
  margin-bottom: 1.2rem;
  font-weight: 600;
  color: #1e293b;
}

.filter-bar {
  display: flex;
  gap: 0.8rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}
.filter-btn {
  background: #f1f5f9;
  border: none;
  padding: 0.4rem 1.2rem;
  border-radius: 40px;
  font-size: 0.9rem;
  font-weight: 500;
  color: #334155;
  cursor: pointer;
  transition: all 0.2s;
}
.filter-btn.active {
  background: #4F46E5;
  color: white;
  box-shadow: 0 2px 6px rgba(79,70,229,0.3);
}
.filter-btn:hover:not(.active) {
  background: #e2e8f0;
}

.user-table-wrapper {
  overflow-x: auto;
  margin-bottom: 2rem;
}
.user-table {
  width: 100%;
  border-collapse: collapse;
}
.user-table th, .user-table td {
  padding: 0.8rem 0.5rem;
  text-align: left;
  border-bottom: 1px solid #eef2ff;
}
.user-table th {
  font-weight: 600;
  color: #1e293b;
  font-size: 0.9rem;
}
.avatar-cell {
  width: 60px;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 1rem;
}
.edit-input, .edit-select {
  padding: 0.3rem 0.6rem;
  border-radius: 20px;
  border: 1px solid #cbd5e1;
  font-size: 0.85rem;
}
.actions {
  display: flex;
  gap: 0.5rem;
}
.action-btn {
  padding: 0.2rem 0.8rem;
  border-radius: 30px;
  border: none;
  font-size: 0.75rem;
  font-weight: 500;
  cursor: pointer;
  transition: 0.1s;
}
.action-btn.edit {
  background: #F59E0B;
  color: white;
}
.action-btn.delete {
  background: #EF4444;
  color: white;
}
.action-btn.save {
  background: #10B981;
  color: white;
}
.action-btn.cancel {
  background: #94a3b8;
  color: white;
}

.add-user-section {
  border-top: 1px solid #eef2ff;
  padding-top: 1.2rem;
  margin-top: 0.5rem;
}
.add-user-section h4 {
  font-size: 1rem;
  margin-bottom: 1rem;
  color: #1e293b;
}
.add-form {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  align-items: center;
}
.add-input, .add-select {
  padding: 0.5rem 1rem;
  border-radius: 30px;
  border: 1px solid #cbd5e1;
  font-size: 0.9rem;
  flex: 1;
  min-width: 150px;
}
.btn-add {
  background: #4F46E5;
  color: white;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 40px;
  cursor: pointer;
  font-weight: 500;
  transition: 0.2s;
}
.btn-add:hover {
  background: #4338ca;
  transform: scale(0.98);
}
</style>