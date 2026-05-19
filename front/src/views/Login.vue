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
          <p class="brand-tagline">连接创意，分享无限</p>
          
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
            <h2>欢迎回来</h2>
            <p>登录以继续你的创作之旅</p>
          </div>

          <form @submit.prevent="handleLogin" class="auth-form">
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

            <div class="form-options">
              <label class="remember-me">
                <input type="checkbox" v-model="form.remember">
                <span class="checkmark"></span>
                <span>记住我</span>
              </label>
              <a href="#" class="forgot-link">忘记密码?</a>
            </div>

            <button type="submit" class="submit-btn" :disabled="isLoading">
              <span v-if="isLoading" class="btn-loader"></span>
              <span v-else>
                登录
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="5" y1="12" x2="19" y2="12"/>
                  <polyline points="12 5 19 12 12 19"/>
                </svg>
              </span>
            </button>
          </form>

          <div class="social-login">
            <div class="divider">
              <span>或使用以下方式</span>
            </div>
            <div class="social-buttons">
              <button class="social-btn" v-for="social in socials" :key="social.name">
                <svg viewBox="0 0 24 24" fill="currentColor" v-html="social.icon"></svg>
              </button>
            </div>
          </div>

          <div class="form-footer">
            <p>还没有账号? <router-link to="/register" class="gradient-link">立即注册</router-link></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostsStore } from '../stores/posts'

const router = useRouter()
const userStore = useUserStore()
const postsStore = usePostsStore()

const form = ref({
  phone: '',
  password: '',
  remember: false
})

const focusedInput = ref('')
const showPassword = ref(false)
const isLoading = ref(false)

const features = [
  { icon: '<path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>', text: '实时互动' },
  { icon: '<rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/>', text: '多媒体分享' },
  { icon: '<path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>', text: '发现同好' }
]

const socials = [
  { name: 'Google', icon: '<path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/><path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/><path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/><path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>' },
  { name: 'GitHub', icon: '<path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>' },
  { name: 'Twitter', icon: '<path d="M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z"/>' }
]

const particleStyle = (n) => {
  return {
    left: `${Math.random() * 100}%`,
    animationDelay: `${Math.random() * 5}s`,
    animationDuration: `${5 + Math.random() * 5}s`
  }
}

const handleLogin = async () => {
  isLoading.value = true
  try {
    await userStore.login({
      phone: form.value.phone,
      password: form.value.password
    })
    // 登录成功后预加载帖子数据
    await postsStore.fetchPosts()
    router.push('/')
  } catch (error) {
    alert('登录失败，请检查手机号和密码')
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
  width: 4px;
  height: 4px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  opacity: 0;
  animation: particleFloat linear infinite;
  will-change: transform, opacity;
}

@keyframes particleFloat {
  0% {
    opacity: 0;
    transform: translateY(100vh) scale(0);
  }
  10% {
    opacity: 0.4;
  }
  90% {
    opacity: 0.4;
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
  gap: 24px;
}

.input-group {
  position: relative;
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

/* Form Options */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.forgot-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.2s;
}

.forgot-link:hover {
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

/* Social Login */
.social-login {
  margin-top: 32px;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: 24px;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(102, 126, 234, 0.2), transparent);
}

.divider span {
  position: relative;
  background: rgba(255, 255, 255, 0.8);
  padding: 0 16px;
  font-size: 13px;
  color: #718096;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.social-btn {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  color: #4a5568;
  cursor: pointer;
  transition: all 0.2s;
}

.social-btn:hover {
  background: rgba(255, 255, 255, 0.9);
  border-color: #667eea;
  color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.2);
}

.social-btn svg {
  width: 20px;
  height: 20px;
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
