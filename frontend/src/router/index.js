import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/HomePage.vue'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'

const routes = [
  { path: '/', name: 'Home', component: HomePage },          
  { path: '/login', component: Login },
  { 
    path: '/dashboard', 
    component: Dashboard,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('userRole')
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
  } else {
    next()
  }
})

export default router
