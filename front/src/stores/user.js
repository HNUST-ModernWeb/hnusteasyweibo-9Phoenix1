import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '../api/user'

export const useUserStore = defineStore('user', () => {
  // State
  const currentUser = ref(JSON.parse(localStorage.getItem('user')) || null)
  const token = ref(localStorage.getItem('token') || null)
  
  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const userInfo = computed(() => currentUser.value)
  
  // Actions
  // 登录（使用手机号）
  const login = async (credentials) => {
    try {
      // credentials 应该包含 { phone, password }
      const response = await userApi.login(credentials)
      
      if (response.code === 200 && response.data) {
        const user = response.data
        const userToken = 'user_' + user.id
        
        currentUser.value = user
        token.value = userToken
        
        localStorage.setItem('user', JSON.stringify(user))
        localStorage.setItem('token', userToken)
        
        return user
      } else {
        throw new Error(response.message || '登录失败')
      }
    } catch (error) {
      throw new Error('登录失败：' + error.message)
    }
  }
  
  // 注册
  const register = async (data) => {
    try {
      // data 应该包含 { phone, nickname, password }
      const response = await userApi.register(data)
      
      if (response.code === 200) {
        // 注册成功后自动登录
        const user = {
          id: response.data?.id || Date.now(),
          phone: data.phone,
          nickname: data.nickname || '用户' + data.phone.slice(-4),
          avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${data.phone}`,
          bio: '',
          status: 1, // 默认状态为正常
          followers: 0,
          following: 0,
          posts: 0
        }
        
        const userToken = 'user_' + user.id
        
        currentUser.value = user
        token.value = userToken
        
        localStorage.setItem('user', JSON.stringify(user))
        localStorage.setItem('token', userToken)
        
        return user
      } else {
        throw new Error(response.message || '注册失败')
      }
    } catch (error) {
      throw new Error('注册失败：' + error.message)
    }
  }
  
  const logout = () => {
    currentUser.value = null
    token.value = null
    localStorage.removeItem('user')
    localStorage.removeItem('token')
  }
  
  const updateProfile = async (data) => {
    if (currentUser.value) {
      try {
        await userApi.updateUser(currentUser.value.id, data)
        currentUser.value = { ...currentUser.value, ...data }
        localStorage.setItem('user', JSON.stringify(currentUser.value))
      } catch (error) {
        throw new Error('更新失败：' + error.message)
      }
    }
  }
  
  // 获取用户详细信息
  const fetchUserInfo = async (userId) => {
    try {
      const user = await userApi.getUserById(userId)
      return user
    } catch (error) {
      throw new Error('获取用户信息失败：' + error.message)
    }
  }
  
  // 检查手机号是否存在
  const checkPhoneExists = async (phone) => {
    try {
      const response = await userApi.checkPhone(phone)
      return response.exists
    } catch (error) {
      return false
    }
  }
  
  return {
    currentUser,
    token,
    isLoggedIn,
    userInfo,
    login,
    register,
    logout,
    updateProfile,
    fetchUserInfo,
    checkPhoneExists
  }
})
