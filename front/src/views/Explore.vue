<template>
  <div class="explore-page">
    <div class="search-section">
      <div class="search-box">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <path d="m21 21-4.35-4.35"/>
        </svg>
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="搜索动态、用户或话题..."
          class="search-input"
        >
      </div>
    </div>
    
    <div class="explore-content">
      <div class="section">
        <h2 class="section-title">热门话题</h2>
        <div v-if="isLoadingTopics" class="topics-loading">
          <div v-for="i in 4" :key="i" class="topic-card skeleton">
            <div class="topic-image skeleton-img"></div>
            <div class="topic-info">
              <div class="skeleton-text"></div>
              <div class="skeleton-text short"></div>
            </div>
          </div>
        </div>
        <div v-else class="topics-grid">
          <div
            v-for="topic in hotTopics"
            :key="topic.id"
            class="topic-card"
            @click="goToTopic(topic.name)"
          >
            <div class="topic-image">
              <img :src="topic.image" :alt="topic.name">
              <div class="topic-overlay"></div>
            </div>
            <div class="topic-info">
              <h3>#{{ topic.name }}</h3>
              <p>{{ topic.count }} 条动态</p>
            </div>
          </div>
        </div>
      </div>
      
      <div class="section">
        <h2 class="section-title">推荐用户</h2>
        <div v-if="isLoadingUsers" class="users-grid">
          <div v-for="i in 4" :key="i" class="user-card skeleton">
            <div class="avatar skeleton-avatar"></div>
            <div class="skeleton-text"></div>
            <div class="skeleton-text short"></div>
            <div class="skeleton-btn"></div>
          </div>
        </div>
        <div v-else-if="recommendedUsers.length === 0" class="empty-users">
          <p>暂无推荐用户</p>
        </div>
        <div v-else class="users-grid">
          <div v-for="user in recommendedUsers" :key="user.id" class="user-card">
            <img :src="user.avatar" :alt="user.nickname" class="avatar">
            <h4>{{ user.nickname }}</h4>
            <p>{{ user.bio }}</p>
            <button
              class="btn btn-sm"
              :class="user.isFollowing ? 'btn-secondary' : 'btn-primary'"
              :disabled="user.loading"
              @click="toggleFollow(user)"
            >
              {{ user.isFollowing ? '已关注' : '关注' }}
            </button>
          </div>
        </div>
      </div>
      
      <div class="section">
        <h2 class="section-title">热门动态</h2>
        <div class="posts-list">
          <PostCard 
            v-for="post in hotPosts" 
            :key="post.id" 
            :post="post"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { usePostsStore } from '../stores/posts'
import { useUserStore } from '../stores/user'
import { followApi } from '../api/follow'
import { tagApi } from '../api/tag'
import { userApi } from '../api/user'
import PostCard from '../components/PostCard.vue'

const router = useRouter()
const postsStore = usePostsStore()
const userStore = useUserStore()
const searchQuery = ref('')
const hotTopics = ref([])
const isLoadingTopics = ref(false)
const recommendedUsers = ref([])
const isLoadingUsers = ref(false)

onMounted(() => {
  postsStore.fetchPosts()
  fetchHotTopics()
  fetchRecommendedUsers()
})

// 获取热门话题
const fetchHotTopics = async () => {
  isLoadingTopics.value = true
  try {
    const tags = await tagApi.getPopularTags(4)
    // 为每个标签分配一个随机图片
    const topicImages = {
      '摄影': 'https://picsum.photos/seed/photo/400/250',
      '美食': 'https://picsum.photos/seed/food/400/250',
      '旅行': 'https://picsum.photos/seed/travel/400/250',
      '设计': 'https://picsum.photos/seed/design/400/250',
      '生活': 'https://picsum.photos/seed/life/400/250',
      '读书': 'https://picsum.photos/seed/book/400/250',
      '风景': 'https://picsum.photos/seed/scenery/400/250',
      '创意': 'https://picsum.photos/seed/creative/400/250'
    }

    hotTopics.value = tags.map((tag, index) => ({
      id: tag.id,
      name: tag.name,
      count: formatCount(tag.usageCount || 0),
      image: topicImages[tag.name] || `https://picsum.photos/seed/tag${tag.id}/400/250`
    }))
  } catch (error) {
    console.error('获取热门话题失败:', error)
    // 使用默认数据
    hotTopics.value = [
      { id: 1, name: '摄影', count: '12.5k', image: 'https://picsum.photos/seed/photo/400/250' },
      { id: 2, name: '美食', count: '8.9k', image: 'https://picsum.photos/seed/food/400/250' },
      { id: 3, name: '旅行', count: '7.2k', image: 'https://picsum.photos/seed/travel/400/250' },
      { id: 4, name: '设计', count: '5.8k', image: 'https://picsum.photos/seed/design/400/250' },
    ]
  } finally {
    isLoadingTopics.value = false
  }
}

// 格式化数量显示
const formatCount = (count) => {
  if (count >= 10000) {
    return (count / 10000).toFixed(1) + 'w'
  } else if (count >= 1000) {
    return (count / 1000).toFixed(1) + 'k'
  }
  return count.toString()
}

const hotPosts = computed(() => {
  return [...postsStore.posts].sort((a, b) => b.likes - a.likes).slice(0, 3)
})

// 点击话题跳转到首页并筛选
const goToTopic = (topicName) => {
  router.push({
    path: '/',
    query: { search: topicName }
  })
}

// 获取推荐用户（从数据库获取）
const fetchRecommendedUsers = async () => {
  // 未登录时显示空状态
  if (!userStore.userInfo?.id) {
    recommendedUsers.value = []
    return
  }

  isLoadingUsers.value = true
  try {
    const users = await userApi.getRecommendedUsers(userStore.userInfo.id, 4)
    console.log('从数据库获取的推荐用户:', users)
    recommendedUsers.value = users.map(user => ({
      id: user.id,
      nickname: user.nickname || '用户' + user.id,
      bio: user.bio || '暂无简介',
      avatar: user.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${user.id}`,
      isFollowing: false,
      loading: false
    }))
  } catch (error) {
    console.error('获取推荐用户失败:', error)
    recommendedUsers.value = []
  } finally {
    isLoadingUsers.value = false
  }
}

// 切换关注状态
const toggleFollow = async (user) => {
  if (!userStore.userInfo?.id) {
    alert('请先登录')
    return
  }
  
  user.loading = true
  try {
    if (user.isFollowing) {
      // 取消关注
      const response = await followApi.unfollowUser(userStore.userInfo.id, user.id)
      if (response.code === 200) {
        user.isFollowing = false
        console.log('取消关注成功:', user.nickname)
      }
    } else {
      // 关注
      const response = await followApi.followUser(userStore.userInfo.id, user.id)
      if (response.code === 200) {
        user.isFollowing = true
        console.log('关注成功:', user.nickname)
      }
    }
  } catch (error) {
    console.error('关注操作失败:', error)
    alert(error.message || '操作失败，请重试')
  } finally {
    user.loading = false
  }
}
</script>

<style scoped>
.explore-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.search-section {
  margin-bottom: 40px;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-box svg {
  position: absolute;
  left: 20px;
  width: 20px;
  height: 20px;
  color: var(--text-tertiary);
  transition: color 0.2s ease;
}

.search-input {
  width: 100%;
  padding: 16px 20px 16px 52px;
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-lg);
  font-size: 15px;
  background: rgba(255, 255, 255, 0.6);
  transition: all 0.2s ease;
  color: var(--text-primary);
}

.search-input:focus {
  outline: none;
  border-color: var(--primary);
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.search-input::placeholder {
  color: var(--text-tertiary);
}

.section {
  margin-bottom: 48px;
}

.section-title {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 24px;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title::before {
  content: '';
  width: 4px;
  height: 22px;
  background: var(--gradient-primary);
  border-radius: 2px;
}

.topics-grid,
.topics-loading {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.topics-loading {
  min-height: 200px;
}

.skeleton {
  pointer-events: none;
}

.skeleton-img {
  background: linear-gradient(90deg, rgba(255,255,255,0.4) 0%, rgba(255,255,255,0.6) 50%, rgba(255,255,255,0.4) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-text {
  height: 16px;
  background: linear-gradient(90deg, rgba(255,255,255,0.4) 0%, rgba(255,255,255,0.6) 50%, rgba(255,255,255,0.4) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  border-radius: 4px;
  margin-bottom: 8px;
}

.skeleton-text.short {
  width: 60%;
  height: 13px;
}

.skeleton-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(90deg, rgba(255,255,255,0.4) 0%, rgba(255,255,255,0.6) 50%, rgba(255,255,255,0.4) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  margin-bottom: 16px;
}

.skeleton-btn {
  width: 80px;
  height: 36px;
  border-radius: 20px;
  background: linear-gradient(90deg, rgba(255,255,255,0.4) 0%, rgba(255,255,255,0.6) 50%, rgba(255,255,255,0.4) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

@keyframes shimmer {
  0% {
    background-position: -200% 0;
  }
  100% {
    background-position: 200% 0;
  }
}

.topic-card {
  background: rgba(255, 255, 255, 0.6);
  border-radius: var(--radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.8);
}

.topic-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.15);
  background: rgba(255, 255, 255, 0.8);
}

.topic-image {
  aspect-ratio: 16/10;
  overflow: hidden;
  position: relative;
}

.topic-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.topic-card:hover .topic-image img {
  transform: scale(1.08);
}

.topic-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.3) 0%, transparent 50%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.topic-card:hover .topic-overlay {
  opacity: 1;
}

.topic-info {
  padding: 16px;
}

.topic-info h3 {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 6px;
  color: var(--text-primary);
}

.topic-info p {
  font-size: 13px;
  color: var(--text-tertiary);
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.user-card {
  background: rgba(255, 255, 255, 0.6);
  border-radius: var(--radius-md);
  padding: 24px 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.8);
}

.user-card:hover {
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.12);
  background: rgba(255, 255, 255, 0.8);
  transform: translateY(-4px);
}

.user-card .avatar {
  width: 72px;
  height: 72px;
  margin-bottom: 16px;
  border-radius: 50%;
  border: 3px solid white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.user-card h4 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 6px;
  color: var(--text-primary);
}

.user-card p {
  font-size: 13px;
  color: var(--text-tertiary);
  margin-bottom: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.btn-sm {
  padding: 8px 24px;
  font-size: 14px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 500;
}

.btn-primary {
  background: var(--gradient-primary);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(102, 126, 234, 0.2);
  color: var(--text-secondary);
}

.btn-secondary:hover {
  background: rgba(102, 126, 234, 0.1);
  border-color: var(--primary);
  color: var(--primary);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.empty-users {
  text-align: center;
  padding: 40px;
  color: var(--text-tertiary);
}

@media (max-width: 900px) {
  .topics-grid,
  .topics-loading,
  .users-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .explore-page {
    padding: 16px;
  }

  .topics-grid,
  .topics-loading,
  .users-grid {
    grid-template-columns: 1fr;
  }

  .section-title {
    font-size: 18px;
  }
}
</style>
