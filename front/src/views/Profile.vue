<template>
  <div class="profile-page">
    <!-- Background Effects -->
    <div class="profile-bg">
      <div class="bg-gradient"></div>
      <div class="bg-pattern"></div>
    </div>

    <!-- Profile Header -->
    <header class="profile-header">
      <div class="header-content">
        <div class="avatar-section">
          <div class="avatar-ring">
            <div class="avatar-ring-2">
              <img :src="profileUser.avatar" :alt="profileUser.nickname" class="profile-avatar">
            </div>
          </div>
          <div v-if="isCurrentUser" class="edit-avatar" @click="showEditModal = true">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
          </div>
        </div>

        <div class="profile-info">
          <h1 class="profile-name">{{ profileUser.nickname }}</h1>
          <p class="profile-handle">@{{ profileUser.phone }}</p>
          <p class="profile-bio">{{ profileUser.bio || '这个人很神秘，什么都没写' }}</p>

          <div class="profile-meta">
            <span class="meta-item">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                <line x1="16" y1="2" x2="16" y2="6"/>
                <line x1="8" y1="2" x2="8" y2="6"/>
                <line x1="3" y1="10" x2="21" y2="10"/>
              </svg>
              {{ joinDate }} 加入
            </span>
            <span class="meta-item">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              {{ profileUser.location || '未知位置' }}
            </span>
          </div>

          <div class="profile-stats">
            <div class="stat-box">
              <span class="stat-value">{{ formatNumber(profileUser.postsCount) }}</span>
              <span class="stat-label">动态</span>
            </div>
            <div class="stat-box clickable" @click="showFollowers = true">
              <span class="stat-value">{{ formatNumber(profileUser.followers) }}</span>
              <span class="stat-label">粉丝</span>
            </div>
            <div class="stat-box clickable" @click="showFollowing = true">
              <span class="stat-value">{{ formatNumber(profileUser.following) }}</span>
              <span class="stat-label">关注</span>
            </div>
            <div class="stat-box">
              <span class="stat-value">{{ formatNumber(profileUser.totalLikes || totalLikes) }}</span>
              <span class="stat-label">获赞</span>
            </div>
          </div>

          <div class="profile-actions">
            <template v-if="isCurrentUser">
              <button class="btn btn-secondary" @click="showEditModal = true">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
                编辑资料
              </button>
            </template>
            <template v-else>
              <button class="btn btn-primary" :class="{ 'following': isFollowing }" @click="toggleFollow">
                <svg v-if="!isFollowing" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="12" y1="5" x2="12" y2="19"/>
                  <line x1="5" y1="12" x2="19" y2="12"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="20 6 9 17 4 12"/>
                </svg>
                {{ isFollowing ? '已关注' : '关注' }}
              </button>
              <button class="btn btn-secondary">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>
                </svg>
                私信
              </button>
            </template>
          </div>
        </div>
      </div>
    </header>

    <!-- Success Toast -->
    <Transition name="toast">
      <div v-if="saveSuccess" class="success-toast">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
          <polyline points="22 4 12 14.01 9 11.01"/>
        </svg>
        <span>资料更新成功</span>
      </div>
    </Transition>

    <!-- Content Tabs -->
    <nav class="content-nav">
      <div class="nav-container">
        <button 
          v-for="tab in tabs" 
          :key="tab.key"
          class="nav-tab"
          :class="{ active: activeTab === tab.key }"
          @click="activeTab = tab.key"
        >
          <span class="tab-icon" v-html="tab.icon"></span>
          <span class="tab-label">{{ tab.label }}</span>
          <div class="tab-indicator"></div>
        </button>
      </div>
    </nav>

    <!-- Content Area -->
    <main class="content-area">
      <!-- Posts Timeline -->
      <div v-if="activeTab === 'posts'" class="timeline">
        <div v-if="userPosts.length" class="timeline-list">
          <div 
            v-for="(post, index) in userPosts" 
            :key="post.id"
            class="timeline-item"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <div class="timeline-dot"></div>
            <div class="timeline-line" v-if="index < userPosts.length - 1"></div>
            <div class="timeline-card glass-card" @click="openPost(post.id)">
              <div class="card-header">
                <span class="post-date">{{ formatDate(post.createdAt) }}</span>
                <div class="post-stats">
                  <span><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg> {{ post.likes }}</span>
                  <span><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/></svg> {{ post.comments.length }}</span>
                </div>
              </div>
              <p class="post-content">{{ post.content }}</p>
              <div v-if="post.images.length" class="post-images">
                <img :src="post.images[0]" alt="">
                <div v-if="post.images.length > 1" class="image-count">+{{ post.images.length - 1 }}</div>
              </div>
              <div v-if="post.tags.length" class="post-tags">
                <span v-for="tag in post.tags" :key="tag" class="tag">#{{ tag }}</span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
            </svg>
          </div>
          <h3>还没有动态</h3>
          <p>开始分享你的第一条动态吧</p>
          <button v-if="isCurrentUser" class="btn btn-primary" @click="$router.push('/create')">
            发布动态
          </button>
        </div>
      </div>

      <!-- Media Grid -->
      <div v-else-if="activeTab === 'media'" class="media-grid">
        <div v-if="mediaPosts.length" class="grid-container">
          <div 
            v-for="post in mediaPosts" 
            :key="post.id"
            class="media-item"
            @click="openPost(post.id)"
          >
            <img :src="post.images[0]" alt="">
            <div class="media-overlay">
              <div class="overlay-stats">
                <span><svg viewBox="0 0 24 24" fill="currentColor"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg> {{ post.likes }}</span>
                <span><svg viewBox="0 0 24 24" fill="currentColor"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/></svg> {{ post.comments.length }}</span>
              </div>
            </div>
            <div v-if="post.images.length > 1" class="multi-badge">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                <line x1="3" y1="9" x2="21" y2="9"/>
                <line x1="9" y1="21" x2="9" y2="9"/>
              </svg>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <h3>还没有图片</h3>
          <p>发布的图片会显示在这里</p>
        </div>
      </div>

      <!-- Likes -->
      <div v-else-if="activeTab === 'likes'" class="likes-section">
        <div class="empty-state">
          <div class="empty-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
            </svg>
          </div>
          <h3>点赞的内容</h3>
          <p>你点赞的动态会显示在这里</p>
        </div>
      </div>
    </main>

    <!-- Edit Modal -->
    <Teleport to="body">
      <Transition name="modal">
        <div v-if="showEditModal" class="modal-overlay" @click.self="showEditModal = false">
          <div class="modal-content">
            <div class="modal-header">
              <h2>编辑资料</h2>
              <button class="close-btn" @click="showEditModal = false">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="18" y1="6" x2="6" y2="18"/>
                  <line x1="6" y1="6" x2="18" y2="18"/>
                </svg>
              </button>
            </div>
            <div class="modal-body">
              <!-- Avatar Upload -->
              <div class="avatar-upload-section">
                <div class="avatar-preview" @click="triggerAvatarUpload">
                  <img :src="editForm.avatar || profileUser.avatar" alt="avatar">
                  <div class="avatar-overlay">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/>
                      <circle cx="12" cy="13" r="4"/>
                    </svg>
                    <span>更换头像</span>
                  </div>
                </div>
                <input
                  ref="avatarInput"
                  type="file"
                  accept="image/*"
                  style="display: none"
                  @change="handleAvatarChange"
                >
              </div>
              
              <div class="form-group">
                <label>昵称</label>
                <input 
                  v-model="editForm.nickname" 
                  type="text" 
                  class="edit-input"
                  placeholder="请输入昵称"
                >
              </div>
              
              <div class="form-group">
                <label>邮箱</label>
                <input 
                  v-model="editForm.email" 
                  type="email" 
                  class="edit-input"
                  placeholder="请输入邮箱"
                >
              </div>
              
              <div class="form-group">
                <label>简介</label>
                <textarea 
                  v-model="editForm.bio" 
                  class="edit-textarea" 
                  rows="3"
                  placeholder="介绍一下自己..."
                ></textarea>
              </div>
              
              <div class="form-group">
                <label>位置</label>
                <input 
                  v-model="editForm.location" 
                  type="text" 
                  class="edit-input" 
                  placeholder="你的位置"
                >
              </div>
            </div>
            <div class="modal-footer">
              <button class="edit-btn cancel" @click="showEditModal = false">取消</button>
              <button class="edit-btn save" @click="saveProfile">保存</button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostsStore } from '../stores/posts'
import { useRouter } from 'vue-router'
import { followApi } from '../api/follow'
import { userApi } from '../api/user'
import { postApi } from '../api/post'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const postsStore = usePostsStore()

const tabs = [
  { 
    key: 'posts', 
    label: '动态',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>'
  },
  { 
    key: 'media', 
    label: '图片',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>'
  },
  { 
    key: 'likes', 
    label: '点赞',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>'
  }
]

const activeTab = ref('posts')
const showEditModal = ref(false)
const isFollowing = ref(false)
const userStats = ref({
  followersCount: 0,
  followingCount: 0,
  postsCount: 0,
  totalLikes: 0
})

const isCurrentUser = computed(() => {
  return !route.params.id || Number(route.params.id) === userStore.userInfo?.id
})

const currentUserId = computed(() => {
  return isCurrentUser.value ? userStore.userInfo?.id : Number(route.params.id)
})

const profileUser = computed(() => {
  if (isCurrentUser.value) {
    return {
      ...userStore.userInfo,
      id: userStore.userInfo?.id,
      location: userStore.userInfo?.location || '未知位置',
      followers: userStats.value.followersCount,
      following: userStats.value.followingCount,
      postsCount: userStats.value.postsCount,
      totalLikes: userStats.value.totalLikes
    }
  }
  return {
    id: Number(route.params.id),
    phone: '138****' + route.params.id,
    nickname: '用户' + route.params.id,
    avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${route.params.id}`,
    bio: '这是其他用户的简介',
    location: '未知位置',
    followers: userStats.value.followersCount,
    following: userStats.value.followingCount,
    postsCount: userStats.value.postsCount,
      totalLikes: userStats.value.totalLikes
  }
})

// 获取用户统计数据 - 直接从 users 表读取
const fetchUserStats = async () => {
  const userId = currentUserId.value
  if (!userId) return
  
  try {
    // 直接从 userApi 获取用户信息（包含 followers_count、following_count、posts_count）
    const userRes = await userApi.getUserById(userId)
    if (userRes) {
      userStats.value.followersCount = userRes.followersCount || 0
      userStats.value.followingCount = userRes.followingCount || 0
      userStats.value.postsCount = userRes.postsCount || 0
    }
    
    // 获取用户总获赞数（该用户发布的所有动态的点赞数之和）
    const likesRes = await postApi.getUserTotalLikes(userId)
    if (likesRes && typeof likesRes === 'object' && 'totalLikes' in likesRes) {
      userStats.value.totalLikes = likesRes.totalLikes
    }
  } catch (e) {
    console.error('获取用户统计失败:', e)
  }
}

onMounted(() => {
  fetchUserStats()
  postsStore.fetchPosts()
})

const userPosts = computed(() => {
  return postsStore.getPostsByUser(profileUser.value.id)
})

const mediaPosts = computed(() => {
  return userPosts.value.filter(post => post.images.length > 0)
})

const totalLikes = computed(() => {
  return userPosts.value.reduce((sum, post) => sum + post.likes, 0)
})

const joinDate = computed(() => {
  return '2024年1月'
})

const editForm = ref({
  nickname: userStore.userInfo?.nickname || '',
  email: userStore.userInfo?.email || '',
  bio: userStore.userInfo?.bio || '',
  location: userStore.userInfo?.location || '',
  avatar: userStore.userInfo?.avatar || ''
})

const avatarInput = ref(null)

const formatNumber = (num) => {
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  // 处理 ISO 格式时间 (2026-05-06T17:40:38)
  const date = new Date(dateStr)
  if (isNaN(date.getTime())) return dateStr
  
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

const toggleFollow = () => {
  isFollowing.value = !isFollowing.value
}

const openPost = (id) => {
  router.push(`/post/${id}`)
}

const triggerAvatarUpload = () => {
  avatarInput.value?.click()
}

const handleAvatarChange = (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  // 验证文件类型
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }
  
  // 读取文件并预览 (Base64)
  const reader = new FileReader()
  reader.onload = async (e) => {
    const base64 = e.target.result
    // 压缩图片
    try {
      const compressedBase64 = await compressImage(base64, 200, 200, 0.7)
      editForm.value.avatar = compressedBase64
      console.log('头像压缩前长度:', base64.length)
      console.log('头像压缩后长度:', compressedBase64.length)
    } catch (err) {
      console.error('压缩头像失败:', err)
      editForm.value.avatar = base64
    }
  }
  reader.readAsDataURL(file)
}

// 压缩图片
const compressImage = (base64, maxWidth = 200, maxHeight = 200, quality = 0.7) => {
  return new Promise((resolve) => {
    const img = new Image()
    img.src = base64
    img.onload = () => {
      let width = img.width
      let height = img.height
      
      // 计算缩放比例
      if (width > maxWidth || height > maxHeight) {
        const ratio = Math.min(maxWidth / width, maxHeight / height)
        width = width * ratio
        height = height * ratio
      }
      
      // 创建 canvas
      const canvas = document.createElement('canvas')
      canvas.width = width
      canvas.height = height
      const ctx = canvas.getContext('2d')
      ctx.drawImage(img, 0, 0, width, height)
      
      // 压缩为 JPEG
      const compressedBase64 = canvas.toDataURL('image/jpeg', quality)
      resolve(compressedBase64)
    }
  })
}

const isSaving = ref(false)
const saveSuccess = ref(false)

const saveProfile = async () => {
  if (isSaving.value) return
  
  isSaving.value = true
  try {
    await userStore.updateProfile({
      nickname: editForm.value.nickname,
      email: editForm.value.email,
      bio: editForm.value.bio,
      location: editForm.value.location,
      avatar: editForm.value.avatar
    })
    showEditModal.value = false
    saveSuccess.value = true
    // 3秒后隐藏成功提示
    setTimeout(() => {
      saveSuccess.value = false
    }, 3000)
  } catch (error) {
    alert('保存失败：' + error.message)
  } finally {
    isSaving.value = false
  }
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  padding-top: 80px;
  position: relative;
}

/* Background */
.profile-bg {
  position: fixed;
  inset: 0;
  z-index: -1;
  overflow: hidden;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse at 30% 20%, rgba(176, 38, 255, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 70% 60%, rgba(0, 212, 255, 0.1) 0%, transparent 50%);
}

.bg-pattern {
  position: absolute;
  inset: 0;
  background-image: 
    radial-gradient(circle at 1px 1px, rgba(255,255,255,0.03) 1px, transparent 0);
  background-size: 40px 40px;
}

/* Header */
.profile-header {
  padding: 60px 20px 40px;
  text-align: center;
}

.header-content {
  max-width: 600px;
  margin: 0 auto;
}

/* Avatar */
.avatar-section {
  position: relative;
  display: inline-block;
  margin-bottom: 24px;
}

.avatar-ring {
  padding: 4px;
  border-radius: 50%;
  background: var(--gradient-primary);
}

.avatar-ring-2 {
  padding: 3px;
  border-radius: 50%;
  background: var(--bg-deep);
}

.profile-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  display: block;
}

.edit-avatar {
  position: absolute;
  bottom: 8px;
  right: 8px;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-primary);
  border-radius: 50%;
  color: white;
  cursor: pointer;
  transition: all var(--transition-fast);
  box-shadow: 0 4px 15px rgba(176, 38, 255, 0.4);
}

.edit-avatar:hover {
  transform: scale(1.1);
}

.edit-avatar svg {
  width: 18px;
  height: 18px;
}

/* Profile Info */
.profile-name {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 4px;
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.profile-handle {
  font-size: 16px;
  color: var(--text-tertiary);
  margin-bottom: 16px;
}

.profile-bio {
  font-size: 15px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 20px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

.profile-meta {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 24px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-tertiary);
}

.meta-item svg {
  width: 14px;
  height: 14px;
}

/* Stats */
.profile-stats {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 28px;
}

.stat-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 12px 24px;
  background: var(--bg-glass);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.stat-box.clickable {
  cursor: pointer;
}

.stat-box.clickable:hover {
  background: var(--bg-glass-hover);
  border-color: var(--border-glow);
  transform: translateY(-2px);
}

.stat-value {
  font-size: 24px;
  font-weight: 800;
  color: var(--text-primary);
}

.stat-label {
  font-size: 13px;
  color: var(--text-tertiary);
}

/* Actions */
.profile-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.profile-actions .btn {
  display: flex;
  align-items: center;
  gap: 8px;
}

.profile-actions .btn svg {
  width: 18px;
  height: 18px;
}

.btn.following {
  background: var(--bg-glass);
  border: 1px solid var(--border-glass);
}

/* Content Nav */
.content-nav {
  position: sticky;
  top: 80px;
  z-index: 100;
  padding: 0 20px;
  margin-bottom: 32px;
}

.nav-container {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  gap: 8px;
  padding: 8px;
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-lg);
}

.nav-tab {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: none;
  border: none;
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tab-icon {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tab-icon :deep(svg) {
  width: 100%;
  height: 100%;
}

.tab-indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 2px;
  background: var(--gradient-primary);
  border-radius: 2px;
  transition: all var(--transition-fast);
}

.nav-tab:hover {
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.03);
}

.nav-tab.active {
  color: var(--neon-purple);
}

.nav-tab.active .tab-indicator {
  width: 24px;
}

/* Content Area */
.content-area {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px 60px;
}

/* Timeline */
.timeline {
  position: relative;
}

.timeline-list {
  position: relative;
}

.timeline-item {
  position: relative;
  padding-left: 40px;
  margin-bottom: 24px;
  animation: fadeInUp 0.5s ease-out backwards;
}

.timeline-dot {
  position: absolute;
  left: 0;
  top: 24px;
  width: 12px;
  height: 12px;
  background: var(--gradient-primary);
  border-radius: 50%;
  box-shadow: 0 0 10px rgba(176, 38, 255, 0.5);
}

.timeline-line {
  position: absolute;
  left: 5px;
  top: 40px;
  width: 2px;
  height: calc(100% + 8px);
  background: linear-gradient(180deg, var(--neon-purple), transparent);
}

.timeline-card {
  cursor: pointer;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.post-date {
  font-size: 13px;
  color: var(--text-tertiary);
}

.post-stats {
  display: flex;
  gap: 16px;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-secondary);
}

.post-stats svg {
  width: 14px;
  height: 14px;
}

.post-content {
  padding: 16px 20px;
  font-size: 14px;
  line-height: 1.7;
  color: var(--text-secondary);
}

.timeline-card .post-images {
  position: relative;
  margin: 0 20px 16px;
  border-radius: var(--radius-sm);
  overflow: hidden;
}

.timeline-card .post-images img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.image-count {
  position: absolute;
  bottom: 8px;
  right: 8px;
  padding: 4px 12px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 20px;
  font-size: 12px;
  color: white;
}

.timeline-card .post-tags {
  padding: 0 20px 16px;
}

/* Media Grid */
.media-grid .grid-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}

.media-item {
  position: relative;
  aspect-ratio: 1;
  border-radius: var(--radius-sm);
  overflow: hidden;
  cursor: pointer;
}

.media-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.media-item:hover img {
  transform: scale(1.1);
}

.media-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity var(--transition-fast);
}

.media-item:hover .media-overlay {
  opacity: 1;
}

.overlay-stats {
  display: flex;
  gap: 20px;
  color: white;
}

.overlay-stats span {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 600;
}

.overlay-stats svg {
  width: 18px;
  height: 18px;
}

.multi-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  color: white;
  filter: drop-shadow(0 1px 2px rgba(0,0,0,0.5));
}

.multi-badge svg {
  width: 20px;
  height: 20px;
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-glass);
  border: 1px solid var(--border-glass);
  border-radius: 50%;
  margin-bottom: 20px;
}

.empty-icon svg {
  width: 40px;
  height: 40px;
  color: var(--text-tertiary);
}

.empty-state h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}

.empty-state p {
  color: var(--text-tertiary);
  margin-bottom: 24px;
}

/* Modal - 马卡龙粉蓝色系 */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 20px;
}

.modal-content {
  width: 100%;
  max-width: 420px;
  padding: 32px;
  background: linear-gradient(180deg, #fff5f7 0%, #f0f9ff 100%);
  border-radius: 24px;
  box-shadow: 
    0 25px 50px -12px rgba(255, 182, 193, 0.3),
    0 0 0 1px rgba(255, 182, 193, 0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid rgba(255, 182, 193, 0.3);
}

.modal-header h2 {
  font-size: 22px;
  font-weight: 700;
  color: #ff85a2;
  letter-spacing: -0.5px;
}

.close-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 182, 193, 0.15);
  border: none;
  border-radius: 10px;
  color: #ff85a2;
  cursor: pointer;
}

.close-btn:hover {
  background: rgba(255, 182, 193, 0.25);
  color: #ff6b8a;
}

.close-btn svg {
  width: 20px;
  height: 20px;
}

.modal-body {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 20px;
}

/* Avatar Upload */
.avatar-upload-section {
  display: flex;
  justify-content: center;
}

.avatar-preview {
  position: relative;
  width: 90px;
  height: 90px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  border: 3px solid #fff;
  box-shadow: 0 4px 12px rgba(255, 182, 193, 0.3), 0 0 0 3px #a2d2ff;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  opacity: 0;
  color: white;
}

.avatar-preview:hover .avatar-overlay {
  opacity: 1;
}

.avatar-overlay svg {
  width: 20px;
  height: 20px;
}

.avatar-overlay span {
  font-size: 10px;
}

/* Form Groups */
.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #7dd3fc;
}

.edit-input,
.edit-textarea {
  width: 100%;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.8);
  border: 2px solid rgba(162, 210, 255, 0.4);
  border-radius: 12px;
  font-size: 15px;
  color: #4a5568;
}

.edit-input::placeholder,
.edit-textarea::placeholder {
  color: #a0aec0;
}

.edit-input:focus,
.edit-textarea:focus {
  outline: none;
  border-color: #ff85a2;
  background: #fff;
}

.edit-textarea {
  resize: none;
  min-height: 90px;
}

/* Buttons */
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 8px;
}

.edit-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
}

.edit-btn.cancel {
  background: rgba(162, 210, 255, 0.2);
  color: #5fa8d3;
}

.edit-btn.cancel:hover {
  background: rgba(162, 210, 255, 0.3);
}

.edit-btn.save {
  background: linear-gradient(135deg, #ffb6c1 0%, #a2d2ff 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 182, 193, 0.4);
}

.edit-btn.save:hover {
  box-shadow: 0 6px 16px rgba(255, 182, 193, 0.5);
}

/* Success Toast */
.success-toast {
  position: fixed;
  top: 100px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 24px;
  background: linear-gradient(135deg, rgba(34, 197, 94, 0.9), rgba(22, 163, 74, 0.9));
  backdrop-filter: blur(10px);
  border-radius: 50px;
  color: white;
  font-weight: 500;
  box-shadow: 0 8px 32px rgba(34, 197, 94, 0.3);
  z-index: 3000;
}

.success-toast svg {
  width: 20px;
  height: 20px;
}

/* Toast Transition */
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(-20px);
}

/* Modal Transition */
.modal-enter-active,
.modal-leave-active {
  transition: all var(--transition-normal);
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-content,
.modal-leave-to .modal-content {
  transform: scale(0.95);
}

/* Responsive */
@media (max-width: 768px) {
  .profile-header {
    padding: 40px 20px 30px;
  }
  
  .profile-avatar {
    width: 100px;
    height: 100px;
  }
  
  .profile-name {
    font-size: 24px;
  }
  
  .profile-stats {
    gap: 12px;
  }
  
  .stat-box {
    padding: 10px 16px;
  }
  
  .stat-value {
    font-size: 18px;
  }
  
  .media-grid .grid-container {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .timeline-item {
    padding-left: 30px;
  }
}
</style>
