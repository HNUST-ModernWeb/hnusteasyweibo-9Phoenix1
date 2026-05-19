<template>
  <nav class="navbar" :class="{ 'scrolled': isScrolled }">
    <div class="nav-glow"></div>
    <div class="nav-container">
      <router-link to="/" class="logo">
        <div class="logo-icon">
          <svg viewBox="0 0 24 24" fill="none">
            <defs>
              <linearGradient id="logoGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#667eea"/>
                <stop offset="100%" style="stop-color:#764ba2"/>
              </linearGradient>
            </defs>
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" stroke="url(#logoGradient)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <span class="logo-text">ShareSpace</span>
      </router-link>
      
      <div class="nav-center">
        <div class="search-box">
          <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" @click="handleSearch">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="搜索内容、标签..."
            class="search-input"
            @keydown="handleKeydown"
          >
          <button v-if="searchQuery" class="clear-search" @click="searchQuery = ''; handleSearch()">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
      </div>
      
      <div class="nav-links">
        <router-link to="/" class="nav-link" :class="{ active: $route.path === '/' }">
          <div class="link-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
          </div>
          <span class="link-text">首页</span>
          <div class="link-glow"></div>
        </router-link>
        
        <router-link to="/explore" class="nav-link" :class="{ active: $route.path === '/explore' }">
          <div class="link-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
            </svg>
          </div>
          <span class="link-text">发现</span>
          <div class="link-glow"></div>
        </router-link>
        
        <router-link to="/create" class="nav-link create" :class="{ active: $route.path === '/create' }">
          <div class="create-btn">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <line x1="12" y1="5" x2="12" y2="19"/>
              <line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
          </div>
        </router-link>
        
        <!-- User Dropdown -->
        <div class="user-dropdown" ref="dropdownRef">
          <button class="nav-link profile" :class="{ active: $route.path.startsWith('/profile'), 'dropdown-open': showDropdown }" @click="toggleDropdown">
            <div class="avatar-wrapper">
              <img :src="userStore.userInfo?.avatar" alt="avatar" class="nav-avatar">
              <div class="avatar-ring"></div>
            </div>
          </button>
          
          <!-- Dropdown Menu -->
          <transition name="dropdown">
            <div v-if="showDropdown" class="dropdown-menu glass-card">
              <div class="dropdown-header">
                <img :src="userStore.userInfo?.avatar" alt="avatar" class="dropdown-avatar">
                <div class="dropdown-info">
                  <span class="dropdown-name">{{ userStore.userInfo?.nickname }}</span>
                  <span class="dropdown-username">@{{ userStore.userInfo?.username }}</span>
                </div>
              </div>
              
              <div class="dropdown-divider"></div>
              
              <router-link to="/profile" class="dropdown-item" @click="showDropdown = false">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
                <span>个人主页</span>
              </router-link>
              
              <router-link to="/profile/edit" class="dropdown-item" @click="showDropdown = false">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
                <span>编辑资料</span>
              </router-link>
              
              <div class="dropdown-divider"></div>
              
              <button class="dropdown-item logout" @click="handleLogout">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                  <polyline points="16 17 21 12 16 7"/>
                  <line x1="21" y1="12" x2="9" y2="12"/>
                </svg>
                <span>退出登录</span>
              </button>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()
const isScrolled = ref(false)
const showDropdown = ref(false)
const dropdownRef = ref(null)
const searchQuery = ref('')

// 搜索功能
const handleSearch = () => {
  const query = searchQuery.value.trim()
  if (query) {
    // 跳转到首页并带上搜索参数
    router.push({
      path: '/',
      query: { search: query }
    })
  } else {
    // 清空搜索，返回全部
    router.push({ path: '/' })
  }
}

// 处理回车键
const handleKeydown = (e) => {
  if (e.key === 'Enter') {
    handleSearch()
  }
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 20
}

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value
}

const handleLogout = () => {
  userStore.logout()
  showDropdown.value = false
  router.push('/login')
}

// 点击外部关闭下拉菜单
const handleClickOutside = (event) => {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target)) {
    showDropdown.value = false
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: calc(100% - 40px);
  max-width: 1200px;
  z-index: 1000;
  transition: all var(--transition-normal);
}

.navbar.scrolled {
  top: 10px;
}

.nav-glow {
  position: absolute;
  inset: -2px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3), rgba(118, 75, 162, 0.3));
  border-radius: var(--radius-xl);
  filter: blur(8px);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.navbar.scrolled .nav-glow {
  opacity: 1;
}

.nav-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-xl);
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.1);
  transition: background var(--transition-fast), box-shadow var(--transition-fast);
  will-change: transform;
}

.navbar.scrolled .nav-container {
  background: rgba(255, 255, 255, 0.95);
  border-color: rgba(102, 126, 234, 0.2);
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.15);
}

/* Logo */
.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
}

.logo-icon {
  width: 40px;
  height: 40px;
  position: relative;
}

.logo-icon::before {
  content: '';
  position: absolute;
  inset: -4px;
  background: var(--gradient-primary);
  border-radius: 12px;
  opacity: 0.2;
  filter: blur(8px);
}

.logo-icon svg {
  width: 100%;
  height: 100%;
  position: relative;
}

.logo-text {
  font-size: 22px;
  font-weight: 800;
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.5px;
}

/* Search Box */
.nav-center {
  flex: 1;
  max-width: 400px;
  margin: 0 40px;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 16px;
  width: 18px;
  height: 18px;
  color: var(--text-tertiary);
  transition: color var(--transition-fast);
}

.search-box:focus-within .search-icon {
  color: var(--primary);
}

.search-input {
  width: 100%;
  padding: 12px 16px 12px 44px;
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-lg);
  font-size: 14px;
  color: var(--text-primary);
  transition: all var(--transition-fast);
}

.search-input::placeholder {
  color: var(--text-light);
}

.search-input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.9);
  border-color: var(--primary);
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.clear-search {
  position: absolute;
  right: 12px;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(102, 126, 234, 0.1);
  border: none;
  border-radius: 50%;
  color: var(--text-tertiary);
  cursor: pointer;
  transition: all 0.2s;
  padding: 0;
}

.clear-search:hover {
  background: rgba(102, 126, 234, 0.2);
  color: var(--primary);
}

.clear-search svg {
  width: 14px;
  height: 14px;
}

/* Nav Links */
.nav-links {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-link {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: var(--radius-sm);
  text-decoration: none;
  color: var(--text-tertiary);
  font-size: 14px;
  font-weight: 500;
  transition: all var(--transition-fast);
  overflow: hidden;
  background: none;
  border: none;
  cursor: pointer;
}

.link-icon {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.link-icon svg {
  width: 100%;
  height: 100%;
}

.link-text {
  position: relative;
}

.link-glow {
  position: absolute;
  inset: 0;
  background: var(--gradient-primary);
  opacity: 0;
  transition: opacity var(--transition-fast);
  border-radius: var(--radius-sm);
}

.nav-link:hover {
  color: var(--text-primary);
  background: rgba(102, 126, 234, 0.1);
}

.nav-link.active {
  color: var(--primary);
  background: rgba(102, 126, 234, 0.15);
}

.nav-link.active .link-glow {
  opacity: 0.1;
}

/* Create Button */
.nav-link.create {
  padding: 0;
  margin: 0 8px;
}

.create-btn {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-primary);
  border-radius: 50%;
  transition: all var(--transition-fast);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.create-btn svg {
  width: 20px;
  height: 20px;
  color: white;
}

.create-btn:hover {
  transform: scale(1.1) rotate(90deg);
  box-shadow: 0 6px 25px rgba(102, 126, 234, 0.5);
}

/* Profile & Dropdown */
.user-dropdown {
  position: relative;
}

.nav-link.profile {
  padding: 4px;
}

.nav-link.profile.dropdown-open {
  background: rgba(102, 126, 234, 0.15);
}

.avatar-wrapper {
  position: relative;
  width: 40px;
  height: 40px;
}

.nav-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all var(--transition-fast);
}

.avatar-ring {
  position: absolute;
  inset: -3px;
  border-radius: 50%;
  background: var(--gradient-primary);
  opacity: 0;
  filter: blur(4px);
  transition: opacity var(--transition-fast);
  z-index: -1;
}

.nav-link.profile:hover .avatar-ring,
.nav-link.profile.active .avatar-ring,
.nav-link.profile.dropdown-open .avatar-ring {
  opacity: 0.5;
}

/* Dropdown Menu */
.dropdown-menu {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  min-width: 240px;
  padding: 12px;
  z-index: 1001;
}

.dropdown-menu::before {
  content: '';
  position: absolute;
  top: -6px;
  right: 20px;
  width: 12px;
  height: 12px;
  background: rgba(255, 255, 255, 0.45);
  backdrop-filter: blur(20px);
  border-left: 1px solid rgba(255, 255, 255, 0.8);
  border-top: 1px solid rgba(255, 255, 255, 0.8);
  transform: rotate(45deg);
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
}

.dropdown-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.dropdown-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.dropdown-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.dropdown-username {
  font-size: 13px;
  color: var(--text-tertiary);
}

.dropdown-divider {
  height: 1px;
  background: rgba(102, 126, 234, 0.1);
  margin: 8px 0;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  text-decoration: none;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  transition: all var(--transition-fast);
  background: none;
  border: none;
  cursor: pointer;
  width: 100%;
  text-align: left;
}

.dropdown-item svg {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

.dropdown-item:hover {
  background: rgba(102, 126, 234, 0.1);
  color: var(--primary);
}

.dropdown-item.logout {
  color: #e53e3e;
}

.dropdown-item.logout:hover {
  background: rgba(229, 62, 62, 0.1);
  color: #e53e3e;
}

/* Dropdown Animation */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

/* Responsive */
@media (max-width: 900px) {
  .nav-center {
    display: none;
  }
  
  .link-text {
    display: none;
  }
  
  .nav-link {
    padding: 10px;
  }
}

@media (max-width: 600px) {
  .navbar {
    top: auto;
    bottom: 20px;
    width: calc(100% - 32px);
  }
  
  .navbar.scrolled {
    top: auto;
    bottom: 10px;
  }
  
  .nav-container {
    padding: 10px 16px;
    justify-content: center;
  }
  
  .logo-text {
    display: none;
  }
  
  .nav-links {
    gap: 4px;
  }
  
  .dropdown-menu {
    position: fixed;
    top: auto;
    bottom: 90px;
    right: 16px;
    left: 16px;
    min-width: auto;
  }
  
  .dropdown-menu::before {
    display: none;
  }
}
</style>
