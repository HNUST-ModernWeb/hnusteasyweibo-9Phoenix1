<template>
  <div class="post-detail-page">
    <button class="back-btn" @click="$router.back()">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <line x1="19" y1="12" x2="5" y2="12"/>
        <polyline points="12 19 5 12 12 5"/>
      </svg>
      返回
    </button>
    
    <!-- 加载状态 -->
    <div v-if="isLoading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>
    
    <!-- 错误状态 -->
    <div v-else-if="error" class="not-found">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
        <circle cx="12" cy="12" r="10"/>
        <line x1="12" y1="8" x2="12" y2="12"/>
        <line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
      <p>{{ error }}</p>
    </div>
    
    <div v-else-if="post" class="detail-card">
      <PostCard :post="post" />
      
      <div class="comments-section">
        <h3>全部评论 ({{ comments.length }})</h3>
        
        <div class="comment-input-area">
          <img :src="userStore.userInfo?.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'" alt="avatar" class="avatar">
          <div class="input-wrapper">
            <textarea 
              v-model="newComment" 
              placeholder="写下你的评论..."
              rows="3"
              class="textarea"
            ></textarea>
            <button 
              class="btn btn-primary"
              :disabled="!newComment.trim() || isSubmitting"
              @click="submitComment"
            >
              {{ isSubmitting ? '发表中...' : '发表评论' }}
            </button>
          </div>
        </div>
        
        <div v-if="isLoadingComments" class="loading-comments">
          <div class="loading-spinner"></div>
          <p>加载评论中...</p>
        </div>
        
        <div v-else-if="comments.length" class="comments-list">
          <!-- 一级评论 -->
          <div v-for="comment in topLevelComments" :key="comment.id" class="comment-thread">
            <!-- 主评论 -->
            <div class="comment-item">
              <div class="comment-avatar">
                <img :src="comment.userAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + comment.userId" class="avatar-img">
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="author">{{ comment.userNickname || '用户' + comment.userId }}</span>
                  <span class="time">{{ formatDate(comment.createdAt) }}</span>
                </div>
                <p class="text">{{ comment.content }}</p>
                <div class="comment-actions">
                  <button 
                    class="action-btn"
                    :class="{ 'is-liked': comment.isLiked }"
                    @click="comment.isLiked ? unlikeComment(comment) : likeComment(comment)"
                  >
                    <svg viewBox="0 0 24 24" :fill="comment.isLiked ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                      <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"/>
                    </svg>
                    <span v-if="comment.likesCount > 0">{{ comment.likesCount }}</span>
                    <span v-else>点赞</span>
                  </button>
                  <button class="action-btn" @click="startReply(comment)">回复</button>
                </div>
                
                <!-- 回复输入框 -->
                <div v-if="replyTo && replyTo.id === comment.id" class="reply-input-area">
                  <textarea
                    v-model="replyContent"
                    :placeholder="`回复 ${comment.userNickname || '用户' + comment.userId}...`"
                    rows="2"
                    class="textarea reply-textarea"
                  ></textarea>
                  <div class="reply-actions">
                    <button class="btn btn-secondary" @click="cancelReply">取消</button>
                    <button 
                      class="btn btn-primary"
                      :disabled="!replyContent.trim() || isSubmittingReply"
                      @click="submitReply"
                    >
                      {{ isSubmittingReply ? '发送中...' : '发送' }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 该评论的回复列表 -->
            <div v-if="getReplies(comment.id).length > 0" class="replies-list">
              <div v-for="reply in getReplies(comment.id)" :key="reply.id" class="comment-item reply-item">
                <div class="comment-avatar">
                  <img :src="reply.userAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + reply.userId" class="avatar-img">
                </div>
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="author">{{ reply.userNickname || '用户' + reply.userId }}</span>
                    <span class="time">{{ formatDate(reply.createdAt) }}</span>
                  </div>
                  <p class="text">{{ reply.content }}</p>
                  <div class="comment-actions">
                    <button 
                      class="action-btn"
                      :class="{ 'is-liked': reply.isLiked }"
                      @click="reply.isLiked ? unlikeComment(reply) : likeComment(reply)"
                    >
                      <svg viewBox="0 0 24 24" :fill="reply.isLiked ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                        <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"/>
                      </svg>
                      <span v-if="reply.likesCount > 0">{{ reply.likesCount }}</span>
                      <span v-else>点赞</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div v-else class="empty-comments">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>
          </svg>
          <p>暂无评论，来抢沙发吧！</p>
        </div>
      </div>
    </div>
    
    <div v-else class="not-found">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
        <circle cx="12" cy="12" r="10"/>
        <line x1="12" y1="8" x2="12" y2="12"/>
        <line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
      <p>动态不存在或已被删除</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostsStore } from '../stores/posts'
import { commentApi } from '../api/comment'
import PostCard from '../components/PostCard.vue'

const route = useRoute()
const userStore = useUserStore()
const postsStore = usePostsStore()

const post = ref(null)
const isLoading = ref(false)
const error = ref(null)
const comments = ref([])
const isSubmitting = ref(false)
const isLoadingComments = ref(false)

// 加载动态详情
const loadPostDetail = async () => {
  const postId = route.params.id
  if (!postId) return
  
  isLoading.value = true
  error.value = null
  
  try {
    // 先从 store 中查找
    const cachedPost = postsStore.posts.find(p => p.id == postId)
    if (cachedPost) {
      post.value = cachedPost
    } else {
      // 如果没有缓存，从后端获取
      post.value = await postsStore.fetchPostById(postId)
    }
    // 加载评论
    await loadComments()
  } catch (err) {
    console.error('加载动态详情失败:', err)
    error.value = '动态不存在或已被删除'
  } finally {
    isLoading.value = false
  }
}

// 加载评论
const loadComments = async () => {
  const postId = route.params.id
  if (!postId) return
  
  isLoadingComments.value = true
  try {
    const response = await commentApi.getCommentsByPostId(postId)
    console.log('加载评论返回:', response)
    if (Array.isArray(response)) {
      comments.value = response
    } else if (response && Array.isArray(response.data)) {
      comments.value = response.data
    }
  } catch (error) {
    console.error('加载评论失败:', error)
  } finally {
    isLoadingComments.value = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadPostDetail()
})

// 监听路由参数变化（切换动态时）
watch(() => route.params.id, () => {
  loadPostDetail()
})

const newComment = ref('')
const replyTo = ref(null) // 当前回复的评论
const replyContent = ref('') // 回复内容
const isSubmittingReply = ref(false)

// 计算属性：一级评论
const topLevelComments = computed(() => {
  return comments.value.filter(c => !c.parentId).sort((a, b) => {
    return new Date(b.createdAt) - new Date(a.createdAt)
  })
})

// 获取某个评论的回复
const getReplies = (parentId) => {
  return comments.value
    .filter(c => c.parentId === parentId)
    .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
}

// 提交评论
const submitComment = async () => {
  if (!newComment.value.trim() || !post.value) return
  if (!userStore.userInfo?.id) {
    alert('请先登录')
    return
  }
  
  isSubmitting.value = true
  try {
    const commentData = {
      postId: post.value.id,
      userId: userStore.userInfo.id,
      content: newComment.value.trim(),
      parentId: null,
      status: 1
    }
    console.log('提交评论数据:', commentData)
    
    const response = await commentApi.createComment(commentData)
    console.log('创建评论返回:', response)
    
    if (response && response.code === 200) {
      newComment.value = ''
      // 刷新评论列表
      await loadComments()
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

// 评论点赞
const likeComment = async (comment) => {
  if (!userStore.userInfo?.id) {
    alert('请先登录')
    return
  }
  
  try {
    const response = await commentApi.likeComment(comment.id)
    if (response && response.code === 200) {
      comment.likesCount = (comment.likesCount || 0) + 1
      comment.isLiked = true
    }
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

// 取消评论点赞
const unlikeComment = async (comment) => {
  if (!userStore.userInfo?.id) {
    alert('请先登录')
    return
  }
  
  try {
    const response = await commentApi.unlikeComment(comment.id)
    if (response && response.code === 200) {
      comment.likesCount = Math.max((comment.likesCount || 0) - 1, 0)
      comment.isLiked = false
    }
  } catch (error) {
    console.error('取消点赞失败:', error)
  }
}

// 开始回复评论
const startReply = (comment) => {
  if (!userStore.userInfo?.id) {
    alert('请先登录')
    return
  }
  replyTo.value = comment
  replyContent.value = ''
}

// 取消回复
const cancelReply = () => {
  replyTo.value = null
  replyContent.value = ''
}

// 提交回复
const submitReply = async () => {
  if (!replyContent.value.trim() || !post.value || !replyTo.value) return
  
  isSubmittingReply.value = true
  try {
    const commentData = {
      postId: post.value.id,
      userId: userStore.userInfo.id,
      content: replyContent.value.trim(),
      parentId: replyTo.value.id,
      status: 1
    }
    
    const response = await commentApi.createComment(commentData)
    
    if (response && response.code === 200) {
      replyContent.value = ''
      replyTo.value = null
      // 刷新评论列表
      await loadComments()
    } else {
      alert(response?.message || '回复失败')
    }
  } catch (error) {
    console.error('提交回复失败:', error)
    alert('回复失败，请重试')
  } finally {
    isSubmittingReply.value = false
  }
}
</script>

<style scoped>
.post-detail-page {
  max-width: 600px;
  margin: 0 auto;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
  padding: 8px 0;
  background: none;
  border: none;
  color: var(--text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: color var(--transition-fast);
}

.back-btn:hover {
  color: var(--text-primary);
}

.back-btn svg {
  width: 18px;
  height: 18px;
}

.detail-card {
  background: var(--bg-secondary);
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.comments-section {
  padding: 20px;
  border-top: 1px solid var(--border-color);
}

.comments-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
}

.comment-input-area {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.comment-input-area .avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.input-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.input-wrapper .textarea {
  min-height: 80px;
  resize: vertical;
}

.input-wrapper .btn {
  align-self: flex-end;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-thread {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.replies-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-left: 52px;
  padding-left: 12px;
  border-left: 2px solid rgba(102, 126, 234, 0.2);
}

.reply-item {
  opacity: 0.9;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-avatar .avatar-placeholder,
.comment-avatar .avatar-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-avatar .avatar-placeholder {
  background: linear-gradient(135deg, var(--color-primary), var(--color-secondary));
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 600;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.comment-header .author {
  font-weight: 600;
  font-size: 14px;
}

.comment-header .time {
  font-size: 12px;
  color: var(--text-tertiary);
}

.comment-content .text {
  font-size: 14px;
  line-height: 1.6;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.comment-actions .action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  color: var(--text-tertiary);
  font-size: 13px;
  cursor: pointer;
  transition: color var(--transition-fast);
}

.comment-actions .action-btn:hover {
  color: var(--color-primary);
}

.comment-actions .action-btn svg {
  width: 16px;
  height: 16px;
}

.comment-actions .action-btn.is-liked {
  color: var(--accent-pink);
}

.comment-actions .action-btn.is-liked:hover {
  color: var(--accent-pink);
}

/* 回复输入框样式 */
.reply-input-area {
  margin-top: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-md);
  border: 1px solid rgba(102, 126, 234, 0.1);
}

.reply-textarea {
  min-height: 60px;
  resize: vertical;
  margin-bottom: 8px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.reply-actions .btn {
  padding: 6px 16px;
  font-size: 13px;
}

.empty-comments,
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: var(--text-secondary);
}

.empty-comments svg,
.not-found svg {
  width: 64px;
  height: 64px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.not-found {
  background: var(--bg-secondary);
  border-radius: var(--border-radius);
  min-height: 400px;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 20px;
  color: var(--text-secondary);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--border-color);
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .post-detail-page {
    padding: 0;
  }
  
  .detail-card {
    border-radius: 0;
  }
  
  .comments-section {
    padding: 16px;
  }
}
</style>
