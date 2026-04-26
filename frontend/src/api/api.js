import axios from 'axios';

// 配置后端 Spring Boot 的地址
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
});

// 响应拦截器：自动提取后端 ApiResponse 里的 data 部分
api.interceptors.response.use(
  res => {
    if (res.data.code === 200) return res.data.data;
    alert('错误: ' + res.data.message);
    return Promise.reject(res.data.message);
  },
  err => {
    alert('网络异常，请检查后端服务是否启动');
    return Promise.reject(err);
  }
);

// 角色映射：前端显示名 -> 后端 Role 枚举名
export const roleMap = {
  '语文老师': 'CHINESE_TEACHER',
  '班主任': 'HEAD_TEACHER',
  '心理老师': 'PSYCHOLOGY_TEACHER',
  '管理员': 'ADMIN'
};

export default api;
