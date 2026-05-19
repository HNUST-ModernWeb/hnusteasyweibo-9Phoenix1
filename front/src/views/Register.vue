<template>
  <div class="auth-page">
    <!-- Animated Background -->
    <div class="auth-bg">
      <div class="bg-gradient"></div>
      <div class="glow-orb orb-1"></div>
      <div class="glow-orb orb-2"></div>
      <div class="glow-orb orb-3"></div>
      <div class="particles">
        <span v-for="n in 15" :key="n" :style="particleStyle(n)"></span>
      </div>
    </div>

    <div class="auth-container">
      <!-- Left Side - Branding -->
      <div class="brand-section">
        <div class="brand-content">
          <div class="logo-wrapper">
            <div class="logo-glow"></div>
            <div class="logo">
              <svg viewBox="0 0 24 24" fill="none">
                <defs>
                  <linearGradient id="logoGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                    <stop offset="0%" style="stop-color:#667eea"/>
                    <stop offset="100%" style="stop-color:#764ba2"/>
                  </linearGradient>
                </defs>
                <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" stroke="url(#logoGrad)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
          </div>
          <h1 class="brand-title">
            <span class="gradient-text">Share</span>Space
          </h1>
          <p class="brand-tagline">加入我们的社区，开启分享之旅</p>
          
          <div class="features">
            <div class="feature-item" v-for="(feature, i) in features" :key="i" :style="{ animationDelay: `${i * 0.1}s` }">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" v-html="feature.icon"></svg>
              </div>
              <span>{{ feature.text }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Side - Form -->
      <div class="form-section">
        <div class="form-container glass-card">
          <div class="form-header">
            <h2>创建账号</h2>
            <p>填写以下信息开始你的分享之旅</p>
          </div>

          <form @submit.prevent="handleRegister" class="auth-form">
            <div class="input-group" :class="{ 'focused': focusedInput === 'phone', 'filled': form.phone }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="5" y="2" width="14" height="20" rx="2" ry="2"/>
                  <line x1="12" y1="18" x2="12.01" y2="18"/>
                </svg>
              </div>
              <input 
                v-model="form.phone"
                type="tel"
                maxlength="11"
                @focus="focusedInput = 'phone'"
                @blur="focusedInput = ''"
                required
              >
              <label>手机号</label>
              <div class="input-line"></div>
            </div>

            <div class="input-group" :class="{ 'focused': focusedInput === 'nickname', 'filled': form.nickname }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </div>
              <input 
                v-model="form.nickname"
                type="text"
                @focus="focusedInput = 'nickname'"
                @blur="focusedInput = ''"
                required
              >
              <label>昵称</label>
              <div class="input-line"></div>
            </div>

            <div class="input-group" :class="{ 'focused': focusedInput === 'password', 'filled': form.password }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </div>
              <input 
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                @focus="focusedInput = 'password'"
                @blur="focusedInput = ''"
                required
                minlength="6"
              >
              <label>密码</label>
              <button type="button" class="toggle-password" @click="showPassword = !showPassword">
                <svg v-if="showPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                  <line x1="1" y1="1" x2="23" y2="23"/>
                </svg>
              </button>
              <div class="input-line"></div>
            </div>

            <div class="input-group" :class="{ 'focused': focusedInput === 'confirmPassword', 'filled': form.confirmPassword, 'error': passwordError }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </div>
              <input 
                v-model="form.confirmPassword"
                :type="showConfirmPassword ? 'text' : 'password'"
                @focus="focusedInput = 'confirmPassword'"
                @blur="focusedInput = ''"
                required
              >
              <label>确认密码</label>
              <button type="button" class="toggle-password" @click="showConfirmPassword = !showConfirmPassword">
                <svg v-if="showConfirmPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                  <line x1="1" y1="1" x2="23" y2="23"/>
                </svg>
              </button>
              <div class="input-line"></div>
            </div>
            <span v-if="passwordError" class="error-text">{{ passwordError }}</span>
            
            <div v-if="errorMessage" class="error-alert">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <line x1="12" y1="8" x2="12" y2="12"/>
                <line x1="12" y1="16" x2="12.01" y2="16"/>
              </svg>
              <span>{{ errorMessage }}</span>
            </div>

            <div class="form-options">
              <label class="remember-me">
                <input type="checkbox" v-model="form.agree" required>
                <span class="checkmark"></span>
                <span>我已阅读并同意 <a href="#" class="gradient-link">用户协议</a> 和 <a href="#" class="gradient-link">隐私政策</a></span>
              </label>
            </div>

            <button type="submit" class="submit-btn" :disabled="isLoading || !form.agree">
              <span v-if="isLoading" class="btn-loader"></span>
              <span v-else>
                创建账号
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="5" y1="12" x2="19" y2="12"/>
                  <polyline points="12 5 19 12 12 19"/>
                </svg>
              </span>
            </button>
          </form>

          <div class="form-footer">
            <p>已有账号? <router-link to="/login" class="gradient-link">立即登录</router-link></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostsStore } from '../stores/posts'

const router = useRouter()
const postsStore = usePostsStore()
const userStore = useUserStore()

const form = ref({
  phone: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  agree: false
})

const focusedInput = ref('')
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const isLoading = ref(false)

const features = [
  { icon: '<path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>', text: '与好友实时互动' },
  { icon: '<rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/>', text: '分享精彩瞬间' },
  { icon: '<path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>', text: '发现有趣的人' }
]

const passwordError = computed(() => {
  if (form.value.confirmPassword && form.value.password !== form.value.confirmPassword) {
    return '两次输入的密码不一致'
  }
  return ''
})

const particleStyle = (n) => {
  return {
    left: `${Math.random() * 100}%`,
    animationDelay: `${Math.random() * 5}s`,
    animationDuration: `${5 + Math.random() * 5}s`
  }
}

const errorMessage = ref('')

const handleRegister = async () => {
  if (form.value.password !== form.value.confirmPassword) {
    errorMessage.value = '两次输入的密码不一致'
    return
  }
  
  if (form.value.password.length < 6) {
    errorMessage.value = '密码长度至少为6位'
    return
  }
  
  // 验证手机号格式
  const phoneRegex = /^1[3-9]\d{9}$/
  if (!phoneRegex.test(form.value.phone)) {
    errorMessage.value = '请输入正确的手机号'
    return
  }
  
  isLoading.value = true
  errorMessage.value = ''
  
  try {
    await userStore.register({
      phone: form.value.phone,
      nickname: form.value.nickname,
      password: form.value.password
    })
    // 注册成功后预加载帖子数据
    await postsStore.fetchPosts()
    router.push('/')
  } catch (error) {
    errorMessage.value = error.message || '注册失败，请重试'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #e0f7fa 0%, #f3e5f5 50%, #fff3e0 100%);
}

/* Background */
.auth-bg {
  position: fixed;
  inset: 0;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse at 20% 30%, rgba(102, 126, 234, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 70%, rgba(118, 75, 162, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 50% 50%, rgba(250, 112, 154, 0.1) 0%, transparent 60%);
}

.glow-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.4;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  top: -100px;
  left: -100px;
  animation: orbFloat 8s ease-in-out infinite;
}

.orb-2 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #fa709a, #fee140);
  bottom: -50px;
  right: 10%;
  animation: orbFloat 10s ease-in-out infinite reverse;
}

.orb-3 {
  width: 250px;
  height: 250px;
  background: linear-gradient(135deg, #4facfe, #00f2fe);
  top: 40%;
  right: -50px;
  animation: orbFloat 12s ease-in-out infinite;
}

@keyframes orbFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(20px, -20px) scale(1.05); }
}

.particles {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.particles span {
  position: absolute;
  width: 6px;
  height: 6px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  opacity: 0;
  animation: particleFloat linear infinite;
}

@keyframes particleFloat {
  0% {
    opacity: 0;
    transform: translateY(100vh) scale(0);
  }
  10% {
    opacity: 0.6;
  }
  90% {
    opacity: 0.6;
  }
  100% {
    opacity: 0;
    transform: translateY(-100vh) scale(1);
  }
}

/* Container */
.auth-container {
  display: grid;
  grid-template-columns: 1fr 480px;
  max-width: 1200px;
  width: 100%;
  margin: 0 20px;
  position: relative;
  z-index: 1;
  gap: 60px;
  align-items: center;
}

/* Brand Section */
.brand-section {
  padding: 40px;
}

.brand-content {
  max-width: 400px;
}

.logo-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 32px;
}

.logo-glow {
  position: absolute;
  inset: -20px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  filter: blur(30px);
  opacity: 0.3;
  animation: logoGlow 3s ease-in-out infinite;
}

@keyframes logoGlow {
  0%, 100% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(1.1); }
}

.logo {
  position: relative;
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.2);
}

.logo svg {
  width: 48px;
  height: 48px;
}

.brand-title {
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 12px;
  letter-spacing: -2px;
  color: #2d3748;
}

.gradient-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: block;
}

.brand-tagline {
  font-size: 20px;
  color: #4a5568;
  margin-bottom: 48px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 16px;
  color: #4a5568;
  opacity: 0;
  animation: fadeInUp 0.5s ease-out forwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.feature-icon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  color: #667eea;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.1);
}

.feature-icon svg {
  width: 22px;
  height: 22px;
}

/* Form Section */
.form-section {
  padding: 20px 0;
}

.form-container {
  padding: 48px;
  background: rgba(255, 255, 255, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 32px;
  box-shadow: 
    0 8px 32px rgba(31, 38, 135, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.6);
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
  color: #2d3748;
}

.form-header p {
  color: #718096;
  font-size: 15px;
}

/* Input Groups */
.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  position: relative;
}

.input-group.error input {
  border-color: #e53e3e;
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 20px;
  height: 20px;
  color: #a0aec0;
  transition: color 0.15s ease;
}

.input-group input {
  width: 100%;
  padding: 16px 16px 16px 52px;
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  font-size: 15px;
  color: #2d3748;
  transition: border-color 0.15s ease, background-color 0.15s ease, box-shadow 0.15s ease;
}

.input-group input:focus {
  outline: none;
  border-color: #667eea;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.input-group label {
  position: absolute;
  left: 52px;
  top: 50%;
  transform: translateY(-50%);
  color: #a0aec0;
  font-size: 15px;
  pointer-events: none;
  transition: top 0.15s ease, left 0.15s ease, transform 0.15s ease, font-size 0.15s ease, color 0.15s ease;
}

.input-group.focused label,
.input-group.filled label {
  top: -8px;
  left: 12px;
  transform: translateY(0);
  font-size: 12px;
  color: #667eea;
  background: white;
  padding: 0 8px;
  border-radius: 4px;
}

.input-group.focused .input-icon,
.input-group.filled .input-icon {
  color: #667eea;
}

.toggle-password {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #a0aec0;
  transition: color 0.2s;
}

.toggle-password:hover {
  color: #667eea;
}

.toggle-password svg {
  width: 20px;
  height: 20px;
}

.error-text {
  font-size: 13px;
  color: #e53e3e;
  margin-top: -12px;
  margin-left: 4px;
}

.error-alert {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: rgba(229, 62, 62, 0.1);
  border: 1px solid rgba(229, 62, 62, 0.2);
  border-radius: 12px;
  color: #e53e3e;
  font-size: 14px;
}

.error-alert svg {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

/* Form Options */
.form-options {
  font-size: 14px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  color: #4a5568;
}

.remember-me input {
  display: none;
}

.checkmark {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(102, 126, 234, 0.3);
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  flex-shrink: 0;
}

.checkmark::after {
  content: '';
  width: 8px;
  height: 8px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 2px;
  opacity: 0;
  transform: scale(0);
  transition: all 0.2s;
}

.remember-me input:checked + .checkmark {
  border-color: #667eea;
}

.remember-me input:checked + .checkmark::after {
  opacity: 1;
  transform: scale(1);
}

.gradient-link {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 600;
  text-decoration: none;
}

.gradient-link:hover {
  opacity: 0.8;
}

/* Submit Button */
.submit-btn {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 16px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.4);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 30px rgba(102, 126, 234, 0.5);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.submit-btn svg {
  width: 20px;
  height: 20px;
}

.btn-loader {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Form Footer */
.form-footer {
  text-align: center;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid rgba(102, 126, 234, 0.1);
  font-size: 14px;
  color: #718096;
}

/* Responsive */
@media (max-width: 1024px) {
  .auth-container {
    grid-template-columns: 1fr;
    max-width: 480px;
  }
  
  .brand-section {
    display: none;
  }
}

@media (max-width: 480px) {
  .form-container {
    padding: 32px 24px;
  }
  
  .form-header h2 {
    font-size: 24px;
  }
}
</style>
