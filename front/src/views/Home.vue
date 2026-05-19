<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-badge">
          <span>✨ 欢迎来到 ShareSpace</span>
        </div>
        <h1 class="hero-title">
          <span class="gradient-text">分享</span>
          <span class="outline-text">生活</span>
        </h1>
        <p class="hero-subtitle">记录美好时刻，连接每一个精彩瞬间</p>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-number">10K+</span>
            <span class="stat-label">创作者</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">50K+</span>
            <span class="stat-label">动态</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">1M+</span>
            <span class="stat-label">互动</span>
          </div>
        </div>
      </div>
      <div class="hero-visual">
        <div class="floating-card card-1">
          <img src="https://picsum.photos/seed/hero1/200/250" alt="">
          <div class="card-overlay">
            <span>🌟</span>
          </div>
        </div>
        <div class="floating-card card-2">
          <img src="https://picsum.photos/seed/hero2/180/220" alt="">
          <div class="card-overlay">
            <span>💫</span>
          </div>
        </div>
        <div class="floating-card card-3">
          <img src="https://picsum.photos/seed/hero3/160/200" alt="">
          <div class="card-overlay">
            <span>🎨</span>
          </div>
        </div>
      </div>
    </section>

    <!-- Hot Tags Section -->
    <section class="hot-tags-section" v-if="postsStore.hotTags.length">
      <div class="section-header">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
          <line x1="7" y1="7" x2="7.01" y2="7"/>
        </svg>
        <span>热门话题</span>
      </div>
      <div class="hot-tags-list">
        <button 
          v-for="{ tag, count } in postsStore.hotTags" 
          :key="tag"
          class="hot-tag"
          :class="{ active: postsStore.selectedTag === tag }"
          @click="selectTag(tag)"
        >
          <span class="tag-name">#{{ tag }}</span>
          <span class="tag-count">{{ count }}</span>
        </button>
      </div>
    </section>

    <!-- Active Tag Filter -->
    <section class="filter-banner" v-if="postsStore.selectedTag">
      <div class="filter-info">
        <span>正在查看标签:</span>
        <span class="filter-tag">#{{ postsStore.selectedTag }}</span>
      </div>
      <button class="clear-filter" @click="clearTagFilter">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="18" y1="6" x2="6" y2="18"/>
          <line x1="6" y1="6" x2="18" y2="18"/>
        </svg>
        清除筛选
      </button>
    </section>

    <!-- Search Results Banner -->
    <section class="filter-banner search-banner" v-if="searchQuery">
      <div class="filter-info">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <path d="m21 21-4.35-4.35"/>
        </svg>
        <span>搜索结果:</span>
        <span class="filter-tag">"{{ searchQuery }}"</span>
        <span class="result-count">({{ filteredPosts.length }} 条)</span>
      </div>
      <button class="clear-filter" @click="clearSearch">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="18" y1="6" x2="6" y2="18"/>
          <line x1="6" y1="6" x2="18" y2="18"/>
        </svg>
        清除搜索
      </button>
    </section>

    <!-- Quick Post -->
    <section class="quick-post-section">
      <div class="quick-post glass-card">
        <div class="quick-post-avatar">
          <img :src="userStore.userInfo?.avatar" alt="avatar">
          <div class="online-indicator"></div>
        </div>
        <div class="quick-post-input" @click="$router.push('/create')">
          <span class="placeholder">分享你的想法...</span>
          <div class="input-hint">点击开始创作 ✍️</div>
        </div>
        <div class="quick-post-actions">
          <button class="action-btn" title="图片">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="18" height="18" rx="2"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <path d="M21 15l-5-5L5 21"/>
            </svg>
          </button>
          <button class="action-btn" title="视频">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polygon points="23 7 16 12 23 17 23 7"/>
              <rect x="1" y="5" width="15" height="14" rx="2" ry="2"/>
            </svg>
          </button>
          <button class="action-btn" title="表情">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <path d="M8 14s1.5 2 4 2 4-2 4-2"/>
              <line x1="9" y1="9" x2="9.01" y2="9"/>
              <line x1="15" y1="9" x2="15.01" y2="9"/>
            </svg>
          </button>
        </div>
      </div>
    </section>

    <!-- Filter Tabs -->
    <section class="filter-section">
      <div class="filter-tabs">
        <button 
          v-for="tab in tabs" 
          :key="tab.key"
          class="filter-tab"
          :class="{ active: activeTab === tab.key }"
          @click="activeTab = tab.key"
        >
          <span class="tab-icon" v-html="tab.icon"></span>
          <span class="tab-text">{{ tab.label }}</span>
          <div class="tab-glow"></div>
        </button>
      </div>
    </section>

    <!-- Masonry Grid -->
    <section class="content-section">
      <div class="masonry-grid">
        <div 
          v-for="(post, index) in filteredPosts" 
          :key="post.id"
          class="masonry-item"
          :style="{ animationDelay: `${index * 0.1}s` }"
        >
          <div class="post-card glass-card">
            <!-- Author Header -->
            <div class="post-header">
              <div class="author-info" @click="openPost(post.id)">
                <img :src="post.author.avatar" :alt="post.author.nickname" class="post-avatar">
                <div class="post-meta">
                  <span class="author-name">{{ post.author.nickname }}</span>
                  <span class="post-time">{{ post.createdAt }}</span>
                </div>
              </div>
              <!-- 更多操作按钮 -->
              <div class="post-menu">
                <button 
                  v-if="post.author.id === userStore.userInfo?.id" 
                  class="menu-btn"
                  @click.stop="showDeleteConfirm(post)"
                  title="删除动态"
                >
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="3 6 5 6 21 6"/>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                  </svg>
                </button>
              </div>
            </div>
            
            <!-- Content -->
            <div class="post-content-wrapper" @click="openPost(post.id)">

            <!-- Content -->
            <div class="post-body">
              <p class="post-text">{{ post.content }}</p>
              <div v-if="post.tags.length" class="post-tags">
                <span 
                  v-for="tag in post.tags.slice(0, 3)" 
                  :key="tag" 
                  class="tag"
                  @click.stop="selectTag(tag)"
                >
                  #{{ tag }}
                </span>
              </div>
            </div>

            <!-- Images -->
            <div v-if="post.images.length" class="post-images">
              <div class="image-grid" :class="`layout-${Math.min(post.images.length, 4)}`">
                <div 
                  v-for="(img, idx) in post.images.slice(0, 4)" 
                  :key="idx"
                  class="image-item"
                >
                  <img :src="img" :alt="`图片${idx + 1}`">
                  <div v-if="idx === 3 && post.images.length > 4" class="more-overlay">
                    <span>+{{ post.images.length - 4 }}</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Actions -->
            <div class="post-footer">
              <button 
                class="footer-btn"
                :class="{ active: post.isLiked }"
                @click.stop="toggleLike(post.id)"
              >
                <svg viewBox="0 0 24 24" :fill="post.isLiked ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                  <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                </svg>
                <span>{{ formatNumber(post.likes) }}</span>
              </button>
              <button class="footer-btn" @click.stop="openPost(post.id)">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>
                </svg>
                <span>{{ post.comments.length }}</span>
              </button>
              <button class="footer-btn" @click.stop>
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="18" cy="5" r="3"/>
                  <circle cx="6" cy="12" r="3"/>
                  <circle cx="18" cy="19" r="3"/>
                  <line x1="8.59" y1="13.51" x2="15.42" y2="17.49"/>
                  <line x1="15.41" y1="6.51" x2="8.59" y2="10.49"/>
                </svg>
                <span>分享</span>
              </button>
            </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Empty State -->
      <div v-if="filteredPosts.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
          <line x1="9" y1="9" x2="15" y2="15"/>
          <line x1="15" y1="9" x2="9" y2="15"/>
        </svg>
        <p>{{ emptyStateText }}</p>
        <button class="btn-clear" @click="activeTab = 'all'">查看全部</button>
      </div>
    </section>

    <!-- Load More -->
    <div class="load-more" v-if="filteredPosts.length > 0">
      <button class="load-btn" @click="loadMore">
        <span>加载更多</span>
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="6 9 12 15 18 9"/>
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostsStore } from '../stores/posts'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const postsStore = usePostsStore()

// 搜索关键词
const searchQuery = computed(() => route.query.search || '')

// 加载动态数据
const loadPosts = async () => {
  try {
    await postsStore.fetchPosts()
  } catch (error) {
    console.error('加载动态失败:', error)
  }
}

onMounted(() => {
  loadPosts()
})

// 监听登录状态变化，登录后自动刷新
watch(() => userStore.isLoggedIn, (newValue, oldValue) => {
  if (newValue && !oldValue) {
    // 用户刚登录，刷新动态
    loadPosts()
  }
})

// 内容分类配置
const tabs = [
  {
    key: 'all',
    label: '全部',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>'
  },
  {
    key: 'food',
    label: '美食',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8h1a4 4 0 0 1 0 8h-1"/><path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"/><line x1="6" y1="1" x2="6" y2="4"/><line x1="10" y1="1" x2="10" y2="4"/><line x1="14" y1="1" x2="14" y2="4"/></svg>',
    tags: ['美食', 'food', '吃货', '料理', '餐厅', '甜品']
  },
  {
    key: 'travel',
    label: '旅行',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polygon points="16.24 7.76 14.12 14.12 7.76 16.24 9.88 9.88 16.24 7.76"/></svg>',
    tags: ['旅行', '旅游', '风景', '景点', '出行', 'travelling']
  },
  {
    key: 'photo',
    label: '摄影',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>',
    tags: ['摄影', '拍照', '相机', 'photo', 'photography']
  },
  {
    key: 'reading',
    label: '读书',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>',
    tags: ['读书', '阅读', '书籍', '文学', 'book', 'reading']
  },
  {
    key: 'life',
    label: '生活',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>',
    tags: ['生活', '日常', 'lifestyle', '家居', '分享']
  },
  {
    key: 'media',
    label: '图片',
    icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>'
  }
]

const activeTab = ref('all')

const filteredPosts = computed(() => {
  let posts = postsStore.posts || []

  // 如果有搜索关键词，优先按搜索筛选
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    posts = posts.filter(p => {
      // 搜索内容
      const contentMatch = p.content && p.content.toLowerCase().includes(query)
      // 搜索标签
      const tagMatch = p.tags && p.tags.some(tag => tag.toLowerCase().includes(query))
      // 搜索作者昵称
      const authorMatch = p.author && p.author.nickname && p.author.nickname.toLowerCase().includes(query)
      return contentMatch || tagMatch || authorMatch
    })
  } else {
    // 获取当前选中的分类标签
    const currentTab = tabs.find(tab => tab.key === activeTab.value)

    if (activeTab.value === 'media') {
      // 图片分类 - 只显示有图片的动态
      posts = posts.filter(p => p.images && p.images.length > 0)
    } else if (currentTab && currentTab.tags) {
      // 其他分类 - 根据标签筛选
      const categoryTags = currentTab.tags.map(t => t.toLowerCase())
      posts = posts.filter(p => {
        if (!p.tags || p.tags.length === 0) return false
        return p.tags.some(tag => categoryTags.includes(tag.toLowerCase()))
      })
    }
  }

  return posts
})

// 空状态提示文字
const emptyStateText = computed(() => {
  if (searchQuery.value) {
    return `未找到与 "${searchQuery.value}" 相关的内容`
  }
  const currentTab = tabs.find(tab => tab.key === activeTab.value)
  if (currentTab && currentTab.key !== 'all') {
    return `暂无"${currentTab.label}"分类的动态`
  }
  return '暂无相关动态'
})

const formatNumber = (num) => {
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num
}

const toggleLike = (id) => {
  postsStore.toggleLike(id)
}

const openPost = (id) => {
  router.push(`/post/${id}`)
}

// 显示删除确认
const showDeleteConfirm = async (post) => {
  if (!confirm('确定要删除这条动态吗？此操作不可恢复。')) {
    return
  }

  try {
    await postsStore.deletePost(post.id, userStore.userInfo?.id)
    alert('删除成功')
  } catch (error) {
    alert('删除失败：' + (error.message || '未知错误'))
  }
}

const selectTag = (tag) => {
  postsStore.setSelectedTag(tag)
}

const clearTagFilter = () => {
  postsStore.clearTagFilter()
}

const clearSearch = () => {
  router.push({ path: '/' })
}

const loadMore = () => {
  console.log('加载更多')
}
</script>

<style scoped>
.home-page {
  padding-top: 100px;
  max-width: 1200px;
  margin: 0 auto;
  padding-left: 20px;
  padding-right: 20px;
}

/* Hero Section */
.hero-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  padding: 60px 0 80px;
  min-height: 400px;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  padding: 8px 16px;
  background: rgba(102, 126, 234, 0.1);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 20px;
  margin-bottom: 20px;
}

.hero-badge span {
  font-size: 14px;
  color: var(--primary);
  font-weight: 500;
}

.hero-title {
  font-size: 72px;
  font-weight: 900;
  line-height: 1;
  margin-bottom: 20px;
}

.gradient-text {
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: block;
}

.outline-text {
  color: transparent;
  -webkit-text-stroke: 2px var(--primary);
  display: block;
}

.hero-subtitle {
  font-size: 20px;
  color: var(--text-secondary);
  margin-bottom: 40px;
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.stat-item {
  display: flex;
  flex-direction: column;
}

.stat-number {
  font-size: 32px;
  font-weight: 800;
  background: var(--gradient-secondary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 14px;
  color: var(--text-tertiary);
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: rgba(102, 126, 234, 0.2);
}

/* Hero Visual */
.hero-visual {
  position: relative;
  height: 400px;
}

.floating-card {
  position: absolute;
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-card);
  animation: float 8s ease-in-out infinite;
  will-change: transform;
}

.floating-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-overlay {
  position: absolute;
  bottom: 12px;
  right: 12px;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  font-size: 18px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.card-1 {
  width: 200px;
  height: 250px;
  top: 0;
  left: 20%;
  animation-delay: 0s;
  transform: rotate(-5deg);
}

.card-2 {
  width: 180px;
  height: 220px;
  top: 80px;
  right: 10%;
  animation-delay: -2s;
  transform: rotate(5deg);
}

.card-3 {
  width: 160px;
  height: 200px;
  bottom: 20px;
  left: 40%;
  animation-delay: -4s;
  transform: rotate(-3deg);
}

/* Hot Tags Section */
.hot-tags-section {
  margin-bottom: 32px;
  padding: 24px;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: var(--radius-md);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
}

.section-header svg {
  width: 18px;
  height: 18px;
  color: var(--primary);
}

.hot-tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.hot-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s;
}

.hot-tag:hover {
  background: rgba(102, 126, 234, 0.1);
  border-color: var(--primary);
  transform: translateY(-2px);
}

.hot-tag.active {
  background: var(--gradient-primary);
  border-color: transparent;
  color: white;
}

.hot-tag .tag-name {
  font-size: 14px;
  font-weight: 500;
}

.hot-tag .tag-count {
  font-size: 12px;
  opacity: 0.7;
  background: rgba(0, 0, 0, 0.1);
  padding: 2px 6px;
  border-radius: 10px;
}

.hot-tag.active .tag-count {
  background: rgba(255, 255, 255, 0.2);
}

/* Filter Banner */
.filter-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  margin-bottom: 24px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: var(--radius-md);
}

.filter-banner.search-banner {
  background: linear-gradient(135deg, rgba(250, 112, 154, 0.1), rgba(254, 225, 64, 0.1));
  border-color: rgba(250, 112, 154, 0.2);
}

.filter-banner.search-banner .filter-info svg {
  width: 18px;
  height: 18px;
  color: var(--accent-pink);
}

.filter-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}

.filter-tag {
  font-weight: 600;
  color: var(--primary);
}

.result-count {
  color: var(--text-tertiary);
  font-size: 13px;
}

.clear-filter {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-sm);
  font-size: 13px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
}

.clear-filter:hover {
  background: rgba(229, 62, 62, 0.1);
  border-color: #e53e3e;
  color: #e53e3e;
}

.clear-filter svg {
  width: 14px;
  height: 14px;
}

/* Quick Post */
.quick-post-section {
  margin-bottom: 40px;
}

.quick-post {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
}

.quick-post-avatar {
  position: relative;
  flex-shrink: 0;
}

.quick-post-avatar img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.online-indicator {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: #48dbfb;
  border-radius: 50%;
  border: 2px solid white;
}

.quick-post-input {
  flex: 1;
  position: relative;
  padding: 14px 20px;
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.quick-post-input:hover {
  background: rgba(255, 255, 255, 0.8);
  border-color: var(--primary);
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.placeholder {
  color: var(--text-tertiary);
  font-size: 15px;
}

.input-hint {
  font-size: 12px;
  color: var(--text-light);
  margin-top: 4px;
}

.quick-post-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-sm);
  color: var(--text-tertiary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.action-btn svg {
  width: 20px;
  height: 20px;
}

.action-btn:hover {
  background: rgba(102, 126, 234, 0.1);
  border-color: var(--primary);
  color: var(--primary);
  transform: translateY(-2px);
}

/* Filter Tabs */
.filter-section {
  margin-bottom: 32px;
  position: relative;
}

.filter-tabs {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 8px;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.filter-tabs::-webkit-scrollbar {
  display: none;
}

.filter-tab {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.5);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-lg);
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
  overflow: hidden;
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

.tab-glow {
  position: absolute;
  inset: 0;
  background: var(--gradient-primary);
  opacity: 0;
  transition: opacity var(--transition-fast);
}

.filter-tab:hover {
  background: rgba(255, 255, 255, 0.7);
  color: var(--text-primary);
}

.filter-tab.active {
  color: white;
  border-color: transparent;
}

.filter-tab.active .tab-glow {
  opacity: 1;
}

.filter-tab.active .tab-text,
.filter-tab.active .tab-icon {
  position: relative;
  z-index: 1;
}

/* Masonry Grid */
.content-section {
  margin-bottom: 40px;
}

.masonry-grid {
  column-count: 3;
  column-gap: 20px;
}

.masonry-item {
  break-inside: avoid;
  margin-bottom: 20px;
  animation: fadeInUp 0.6s ease-out backwards;
}

.post-card {
  cursor: pointer;
  overflow: hidden;
}

.post-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid rgba(102, 126, 234, 0.1);
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
  cursor: pointer;
}

.post-menu {
  display: flex;
  align-items: center;
}

.menu-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  color: var(--text-tertiary);
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all 0.2s ease;
}

.menu-btn:hover {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.menu-btn svg {
  width: 18px;
  height: 18px;
}

.post-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.post-meta {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 600;
  font-size: 14px;
  color: var(--text-primary);
}

.post-time {
  font-size: 12px;
  color: var(--text-tertiary);
}

.post-content-wrapper {
  cursor: pointer;
}

.post-body {
  padding: 16px;
}

.post-text {
  font-size: 14px;
  line-height: 1.7;
  color: var(--text-secondary);
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.post-tags .tag {
  font-size: 13px;
  color: var(--primary);
  background: rgba(102, 126, 234, 0.1);
  padding: 4px 10px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.post-tags .tag:hover {
  background: var(--primary);
  color: white;
}

/* Images */
.post-images {
  padding: 0 16px 16px;
}

.image-grid {
  display: grid;
  gap: 4px;
  border-radius: var(--radius-sm);
  overflow: hidden;
}

.layout-1 {
  grid-template-columns: 1fr;
}

.layout-2 {
  grid-template-columns: 1fr 1fr;
}

.layout-3 {
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}

.layout-3 .image-item:first-child {
  grid-row: span 2;
}

.layout-4 {
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}

.image-item {
  position: relative;
  aspect-ratio: 1;
  overflow: hidden;
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.post-card:hover .image-item img {
  transform: scale(1.05);
}

.more-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.more-overlay span {
  font-size: 24px;
  font-weight: 700;
  color: white;
}

/* Footer */
.post-footer {
  display: flex;
  padding: 12px 16px;
  border-top: 1px solid rgba(102, 126, 234, 0.1);
  gap: 8px;
}

.footer-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  background: rgba(255, 255, 255, 0.5);
  border: none;
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all var(--transition-fast);
  flex: 1;
  justify-content: center;
}

.footer-btn svg {
  width: 16px;
  height: 16px;
}

.footer-btn:hover {
  background: rgba(102, 126, 234, 0.1);
  color: var(--primary);
}

.footer-btn.active {
  color: var(--accent-pink);
  background: rgba(250, 112, 154, 0.1);
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: var(--text-secondary);
}

.empty-state svg {
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
  opacity: 0.3;
}

.empty-state p {
  font-size: 16px;
  margin-bottom: 20px;
}

.btn-clear {
  padding: 10px 24px;
  background: var(--gradient-primary);
  border: none;
  border-radius: var(--radius-sm);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-clear:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

/* Load More */
.load-more {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}

.load-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-lg);
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.load-btn svg {
  width: 18px;
  height: 18px;
}

.load-btn:hover {
  border-color: var(--primary);
  color: var(--primary);
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.2);
}

/* Responsive */
@media (max-width: 1024px) {
  .masonry-grid {
    column-count: 2;
  }
  
  .hero-section {
    grid-template-columns: 1fr;
    text-align: center;
  }
  
  .hero-visual {
    display: none;
  }
  
  .hero-stats {
    justify-content: center;
  }
}

@media (max-width: 640px) {
  .home-page {
    padding-top: 80px;
  }
  
  .hero-title {
    font-size: 48px;
  }
  
  .masonry-grid {
    column-count: 1;
  }
  
  .filter-tabs {
    overflow-x: auto;
    flex-wrap: nowrap;
    padding-bottom: 8px;
  }
  
  .filter-tab {
    flex-shrink: 0;
  }
  
  .quick-post {
    flex-wrap: wrap;
  }
  
  .quick-post-input {
    order: 3;
    width: 100%;
    margin-top: 8px;
  }
  
  .hot-tags-section {
    padding: 16px;
  }
  
  .hot-tags-list {
    gap: 8px;
  }
  
  .hot-tag {
    padding: 6px 12px;
    font-size: 13px;
  }
}
</style>
