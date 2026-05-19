<template>
  <div class="post-card glass-card" :class="{ 'expanded': showComments }">
    <div class="card-glow"></div>
    
    <!-- Header -->
    <div class="post-header">
      <div class="author-info">
        <div class="avatar-wrapper">
          <img :src="post.author.avatar" :alt="post.author.nickname" class="author-avatar">
          <div class="avatar-glow"></div>
        </div>
        <div class="author-meta">
          <span class="author-name">{{ post.author.nickname }}</span>
          <span class="post-time">{{ formatDate(post.createdAt) }}</span>
        </div>
      </div>
      <button class="more-btn" @click="showMenu = !showMenu">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="5" r="2"/>
          <circle cx="12" cy="12" r="2"/>
          <circle cx="12" cy="19" r="2"/>
        </svg>
      </button>
      <div v-if="showMenu" class="dropdown-menu">
        <button class="menu-item" @click="copyLink">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"/>
            <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"/>
          </svg>
          复制链接
        </button>
        <button v-if="isAuthor" class="menu-item danger" @click="deletePost">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="3 6 5 6 21 6"/>
            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
          </svg>
          删除
        </button>
      </div>
    </div>

    <!-- Content -->
    <div class="post-content">
      <p class="content-text">{{ post.content }}</p>
      <div v-if="post.tags.length" class="content-tags">
        <span v-for="tag in post.tags" :key="tag" class="tag">#{{ tag }}</span>
      </div>
    </div>

    <!-- Images -->
    <div v-if="post.images.length" class="post-images">
      <div class="image-grid" :class="`layout-${Math.min(post.images.length, 4)}`">
        <div 
          v-for="(img, idx) in post.images.slice(0, 4)" 
          :key="idx"
          class="image-item"
          @click="openImage(idx)"
        >
          <img :src="img" :alt="`图片${idx + 1}`">
          <div v-if="idx === 3 && post.images.length > 4" class="more-overlay">
            <span class="more-count">+{{ post.images.length - 4 }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div class="post-actions">
      <button 
        class="action-btn"
        :class="{ 'active': post.isLiked }"
        @click="toggleLike"
      >
        <div class="btn-icon">
          <svg viewBox="0 0 24 24" :fill="post.isLiked ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
          </svg>
        </div>
        <span class="btn-text">{{ formatNumber(post.likes) }}</span>
        <div class="btn-glow"></div>
      </button>

      <button 
        class="action-btn"
        :class="{ 'active': showComments }"
        @click="toggleComments"
      >
        <div class="btn-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>
          </svg>
        </div>
        <span class="btn-text">{{ commentCount }}</span>
        <div class="btn-glow"></div>
      </button>

      <button class="action-btn" @click="sharePost">
        <div class="btn-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="18" cy="5" r="3"/>
            <circle cx="6" cy="12" r="3"/>
            <circle cx="18" cy="19" r="3"/>
            <line x1="8.59" y1="13.51" x2="15.42" y2="17.49"/>
            <line x1="15.41" y1="6.51" x2="8.59" y2="10.49"/>
          </svg>
        </div>
        <span class="btn-text">分享</span>
        <div class="btn-glow"></div>
      </button>
    </div>

    <!-- Comments Section -->
    <Transition name="slide">
      <div v-if="showComments" class="comments-section">
        <div class="comments-list">
          <div v-if="comments.length === 0" class="empty-comments">
            暂无评论，快来抢沙发吧~
          </div>
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <img :src="comment.userAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'" class="comment-avatar-img">
            <div class="comment-body">
              <div class="comment-header">
                <span class="comment-author">{{ comment.userNickname || '用户' + comment.userId }}</span>
                <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
              </div>
              <p class="comment-text">{{ comment.content }}</p>
            </div>
          </div>
        </div>
        <div class="comment-input-area">
          <div class="input-avatar">{{ userStore.userInfo?.nickname?.[0] || '我' }}</div>
          <div class="input-wrapper">
            <input 
              v-model="newComment"
              type="text"
              placeholder="写下你的评论..."
              @keyup.enter="submitComment"
            >
            <button 
              class="send-btn"
              :disabled="!newComment.trim() || isSubmitting"
              @click="submitComment"
            >
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="22" y1="2" x2="11" y2="13"/>
                <polygon points="22 2 15 22 11 13 2 9 22 2"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { usePostsStore } from '../stores/posts'
import { commentApi } from '../api/comment'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['deleted'])

const userStore = useUserStore()
const postsStore = usePostsStore()

// 组件挂载时获取评论数
onMounted(() => {
  loadCommentCount()
})

const showMenu = ref(false)
const showComments = ref(false)
const newComment = ref('')
const comments = ref([])
const commentCount = ref(0)
const isSubmitting = ref(false)
const isLoadingComments = ref(false)

// 获取评论数
const loadCommentCount = async () => {
  try {
    const response = await commentApi.getCommentCountByPost(props.post.id)
    if (response && typeof response === 'object' && 'count' in response) {
      commentCount.value = response.count
    } else if (typeof response === 'number') {
      commentCount.value = response
    }
  } catch (error) {
    console.error('获取评论数失败:', error)
  }
}

const isAuthor = computed(() => {
  return props.post.author.id === userStore.userInfo?.id
})

const formatNumber = (num) => {
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  if (isNaN(date.getTime())) return dateStr
  
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

const toggleLike = async () => {
  try {
    await postsStore.toggleLike(props.post.id)
  } catch (error) {
    alert(error.message || '点赞失败，请重试')
  }
}

const toggleComments = async () => {
  showComments.value = !showComments.value
  if (showComments.value && comments.value.length === 0) {
    await loadComments()
  }
}

const loadComments = async () => {
  isLoadingComments.value = true
  try {
    console.log('正在加载动态', props.post.id, '的评论...')
    const response = await commentApi.getCommentsByPostId(props.post.id)
    console.log('评论接口返回:', response)
    
    // 处理后端返回的数据
    if (response) {
      if (Array.isArray(response)) {
        comments.value = response
        console.log('加载评论成功:', comments.value.length, '条')
      } else if (response.data && Array.isArray(response.data)) {
        // 如果后端包装了 data 字段
        comments.value = response.data
        console.log('加载评论成功:', comments.value.length, '条')
      } else {
        console.error('评论数据格式错误:', response)
        comments.value = []
      }
    } else {
      comments.value = []
    }
  } catch (error) {
    console.error('加载评论失败:', error)
    comments.value = []
  } finally {
    isLoadingComments.value = false
  }
}

const submitComment = async () => {
  if (!newComment.value.trim() || isSubmitting.value) return
  if (!userStore.userInfo?.id) {
    alert('请先登录')
    return
  }

  isSubmitting.value = true
  try {
    const commentData = {
      postId: props.post.id,
      userId: userStore.userInfo.id,
      content: newComment.value.trim(),
      parentId: null,
      status: 1
    }
    console.log('提交评论数据:', commentData)
    console.log('动态ID:', props.post.id)
    console.log('用户ID:', userStore.userInfo.id)

    const response = await commentApi.createComment(commentData)
    console.log('创建评论返回:', response)
    if (response && response.code === 200) {
      newComment.value = ''
      // 刷新评论列表和评论数
      await loadComments()
      await loadCommentCount()
      console.log('评论发表成功')
    } else {
      console.error('评论失败:', response)
      alert(response?.message || '评论失败')
    }
  } catch (error) {
    console.error('提交评论失败:', error)
    alert('评论失败，请重试: ' + error.message)
  } finally {
    isSubmitting.value = false
  }
}

const copyLink = () => {
  navigator.clipboard.writeText(`${window.location.origin}/post/${props.post.id}`)
  showMenu.value = false
}

const deletePost = async () => {
  if (!confirm('确定要删除这条动态吗？此操作不可恢复。')) {
    showMenu.value = false
    return
  }
  
  try {
    await postsStore.deletePost(props.post.id, userStore.userInfo?.id)
    alert('删除成功')
    // 触发删除成功事件，让父组件刷新列表
    emit('deleted', props.post.id)
  } catch (error) {
    alert('删除失败：' + (error.message || '未知错误'))
  }
  showMenu.value = false
}

const sharePost = () => {
  if (navigator.share) {
    navigator.share({
      title: props.post.content.slice(0, 50),
      url: `${window.location.origin}/post/${props.post.id}`
    })
  } else {
    copyLink()
  }
}

const openImage = (idx) => {
  console.log('Open image', idx)
}
</script>

<style scoped>
.post-card {
  position: relative;
  overflow: hidden;
  transition: all var(--transition-normal);
}

.card-glow {
  position: absolute;
  inset: -2px;
  background: var(--gradient-primary);
  opacity: 0;
  filter: blur(20px);
  transition: opacity var(--transition-normal);
  z-index: -1;
}

.post-card:hover .card-glow {
  opacity: 0.3;
}

/* Header */
.post-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  position: relative;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 14px;
}

.avatar-wrapper {
  position: relative;
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid transparent;
  background: linear-gradient(var(--bg-deep), var(--bg-deep)) padding-box,
              var(--gradient-primary) border-box;
  transition: all var(--transition-fast);
}

.avatar-glow {
  position: absolute;
  inset: -4px;
  background: var(--gradient-primary);
  border-radius: 50%;
  opacity: 0;
  filter: blur(8px);
  transition: opacity var(--transition-fast);
  z-index: -1;
}

.post-card:hover .avatar-glow {
  opacity: 0.5;
}

.author-meta {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 600;
  font-size: 15px;
  color: var(--text-primary);
}

.post-time {
  font-size: 13px;
  color: var(--text-tertiary);
}

.more-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 50%;
  color: var(--text-tertiary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.more-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  color: var(--text-primary);
}

.more-btn svg {
  width: 18px;
  height: 18px;
}

.dropdown-menu {
  position: absolute;
  top: 60px;
  right: 20px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-sm);
  padding: 8px;
  min-width: 160px;
  box-shadow: var(--shadow-card);
  z-index: 10;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 10px 14px;
  background: none;
  border: none;
  border-radius: 8px;
  color: var(--text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-primary);
}

.menu-item.danger {
  color: var(--neon-pink);
}

.menu-item.danger:hover {
  background: rgba(255, 0, 110, 0.1);
}

.menu-item svg {
  width: 16px;
  height: 16px;
}

/* Content */
.post-content {
  padding: 0 20px 16px;
}

.content-text {
  font-size: 15px;
  line-height: 1.8;
  color: var(--text-secondary);
  white-space: pre-wrap;
  word-break: break-word;
}

.content-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

/* Images */
.post-images {
  padding: 0 20px 16px;
}

.image-grid {
  display: grid;
  gap: 4px;
  border-radius: var(--radius-sm);
  overflow: hidden;
}

.image-grid.layout-1 {
  grid-template-columns: 1fr;
}

.image-grid.layout-2 {
  grid-template-columns: 1fr 1fr;
}

.image-grid.layout-3 {
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}

.image-grid.layout-3 .image-item:first-child {
  grid-row: span 2;
}

.image-grid.layout-4 {
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}

.image-item {
  position: relative;
  aspect-ratio: 1;
  overflow: hidden;
  cursor: pointer;
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.image-item:hover img {
  transform: scale(1.05);
}

.more-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
}

.more-count {
  font-size: 28px;
  font-weight: 700;
  color: white;
}

/* Actions */
.post-actions {
  display: flex;
  padding: 12px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  gap: 8px;
}

.action-btn {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all var(--transition-fast);
  flex: 1;
  justify-content: center;
  overflow: hidden;
}

.btn-icon {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-icon svg {
  width: 100%;
  height: 100%;
}

.btn-glow {
  position: absolute;
  inset: 0;
  background: var(--gradient-primary);
  opacity: 0;
  transition: opacity var(--transition-fast);
}

.action-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  color: var(--text-primary);
}

.action-btn.active {
  color: var(--neon-pink);
  background: rgba(255, 0, 110, 0.1);
  border-color: rgba(255, 0, 110, 0.3);
}

/* Comments */
.comments-section {
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  padding: 16px 20px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 16px;
  max-height: 400px;
  overflow-y: auto;
}

.empty-comments {
  text-align: center;
  padding: 20px;
  color: var(--text-tertiary);
  font-size: 14px;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-avatar-img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.comment-body {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.comment-author {
  font-weight: 600;
  font-size: 14px;
  color: var(--text-primary);
}

.comment-time {
  font-size: 12px;
  color: var(--text-tertiary);
}

.comment-text {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
}

.comment-input-area {
  display: flex;
  gap: 12px;
}

.input-avatar {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-glass);
  border: 1px solid var(--border-glass);
  border-radius: 50%;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-secondary);
  flex-shrink: 0;
}

.input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 20px;
  padding: 4px 4px 4px 16px;
  transition: all var(--transition-fast);
}

.input-wrapper:focus-within {
  border-color: var(--neon-purple);
  background: rgba(255, 255, 255, 0.06);
}

.input-wrapper input {
  flex: 1;
  background: none;
  border: none;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
}

.input-wrapper input::placeholder {
  color: var(--text-tertiary);
}

.send-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-primary);
  border: none;
  border-radius: 50%;
  color: white;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.send-btn:hover:not(:disabled) {
  transform: scale(1.1);
  box-shadow: 0 0 20px rgba(176, 38, 255, 0.4);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.send-btn svg {
  width: 14px;
  height: 14px;
}

/* Transitions */
.slide-enter-active,
.slide-leave-active {
  transition: all var(--transition-normal);
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>