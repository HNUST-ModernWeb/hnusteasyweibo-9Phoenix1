<template>
  <div class="create-page">
    <!-- Background Effects -->
    <div class="bg-effects">
      <div class="glow-orb orb-1"></div>
      <div class="glow-orb orb-2"></div>
      <div class="glow-orb orb-3"></div>
    </div>

    <div class="create-container">
      <!-- Left Panel - Preview -->
      <div class="preview-panel">
        <div class="preview-card" :class="{ 'has-content': hasContent }">
          <div class="preview-header">
            <img :src="userStore.userInfo?.avatar" alt="avatar" class="preview-avatar">
            <div class="preview-meta">
              <span class="preview-name">{{ userStore.userInfo?.nickname }}</span>
              <span class="preview-time">预览模式</span>
            </div>
          </div>
          
          <div class="preview-body">
            <p v-if="content" class="preview-text">{{ content }}</p>
            <p v-else class="preview-placeholder">你的内容将显示在这里...</p>
            
            <div v-if="tags.length" class="preview-tags">
              <span v-for="tag in tags" :key="tag" class="tag">#{{ tag }}</span>
            </div>
          </div>

          <div v-if="selectedImages.length" class="preview-images">
            <div class="preview-grid" :class="`grid-${Math.min(selectedImages.length, 4)}`">
              <div 
                v-for="(img, idx) in selectedImages.slice(0, 4)" 
                :key="idx"
                class="preview-img-item"
              >
                <img :src="img" alt="">
              </div>
            </div>
          </div>

          <div class="preview-footer">
            <div class="preview-stat">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
              </svg>
              <span>0</span>
            </div>
            <div class="preview-stat">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>
              </svg>
              <span>0</span>
            </div>
          </div>
        </div>

        <div class="preview-tips">
          <div class="tip-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="16" x2="12" y2="12"/>
              <line x1="12" y1="8" x2="12.01" y2="8"/>
            </svg>
            <span>支持 Markdown 格式</span>
          </div>
          <div class="tip-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
            <span>最多上传 9 张图片</span>
          </div>
        </div>
      </div>

      <!-- Right Panel - Editor -->
      <div class="editor-panel">
        <div class="editor-header">
          <h1>
            <span class="gradient-text">创作</span>
            <span>新动态</span>
          </h1>
          <button class="close-btn" @click="$router.back()">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>

        <div class="editor-body">
          <!-- Content Input -->
          <div class="input-section">
            <label class="section-label">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
              内容
            </label>
            <div class="textarea-wrapper">
              <textarea
                ref="contentTextarea"
                v-model="content"
                placeholder="分享你的想法、故事或灵感..."
                class="textarea content-textarea"
                rows="8"
                maxlength="1000"
              ></textarea>
              <div class="char-counter" :class="{ 'warning': content.length > 800 }">
                {{ content.length }}/1000
              </div>
            </div>
            <!-- Emoji Picker -->
            <div class="content-tools">
              <EmojiPicker @select="insertEmoji" />
            </div>
          </div>

          <!-- Tags Input -->
          <div class="input-section">
            <label class="section-label">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
                <line x1="7" y1="7" x2="7.01" y2="7"/>
              </svg>
              话题标签
            </label>
            <div class="tags-input-container">
              <div class="tags-list">
                <span v-for="(tag, index) in tags" :key="index" class="tag-item">
                  #{{ tag }}
                  <button @click="removeTag(index)" class="remove-tag">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <line x1="18" y1="6" x2="6" y2="18"/>
                      <line x1="6" y1="6" x2="18" y2="18"/>
                    </svg>
                  </button>
                </span>
                <input
                  v-if="tags.length < 5"
                  v-model="tagInput"
                  type="text"
                  placeholder="添加标签..."
                  class="tag-input"
                  @keyup.enter="addTag"
                  @keydown.backspace="handleTagBackspace"
                >
              </div>
            </div>
          </div>

          <!-- Images Upload -->
          <div class="input-section">
            <label class="section-label">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                <circle cx="8.5" cy="8.5" r="1.5"/>
                <polyline points="21 15 16 10 5 21"/>
              </svg>
              图片
              <span class="label-hint">{{ selectedImages.length }}/9</span>
            </label>
            <div class="images-upload">
              <div v-if="selectedImages.length" class="uploaded-images">
                <div 
                  v-for="(img, index) in selectedImages" 
                  :key="index"
                  class="uploaded-image"
                >
                  <img :src="img" alt="">
                  <button class="remove-image" @click="removeImage(index)">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <line x1="18" y1="6" x2="6" y2="18"/>
                      <line x1="6" y1="6" x2="18" y2="18"/>
                    </svg>
                  </button>
                </div>
              </div>
              <button 
                v-if="selectedImages.length < 9"
                class="upload-btn"
                @click="triggerUpload"
              >
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="12" y1="5" x2="12" y2="19"/>
                  <line x1="5" y1="12" x2="19" y2="12"/>
                </svg>
                <span>添加图片</span>
              </button>
            </div>
          </div>

          <!-- Visibility -->
          <div class="input-section">
            <label class="section-label">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
              可见范围
            </label>
            <div class="visibility-options">
              <label class="visibility-option" :class="{ active: visibility === 'public' }">
                <input type="radio" v-model="visibility" value="public">
                <div class="option-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="2" y1="12" x2="22" y2="12"/>
                    <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
                  </svg>
                </div>
                <div class="option-info">
                  <span class="option-title">公开</span>
                  <span class="option-desc">所有人可见</span>
                </div>
              </label>
              <label class="visibility-option" :class="{ active: visibility === 'followers' }">
                <input type="radio" v-model="visibility" value="followers">
                <div class="option-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                    <circle cx="9" cy="7" r="4"/>
                    <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                    <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                  </svg>
                </div>
                <div class="option-info">
                  <span class="option-title">粉丝</span>
                  <span class="option-desc">仅粉丝可见</span>
                </div>
              </label>
              <label class="visibility-option" :class="{ active: visibility === 'private' }">
                <input type="radio" v-model="visibility" value="private">
                <div class="option-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                  </svg>
                </div>
                <div class="option-info">
                  <span class="option-title">私密</span>
                  <span class="option-desc">仅自己可见</span>
                </div>
              </label>
            </div>
          </div>
        </div>

        <div class="editor-footer">
          <button class="btn btn-secondary" @click="$router.back()">取消</button>
          <button 
            class="btn btn-primary"
            :disabled="!canSubmit || isSubmitting"
            @click="submitPost"
          >
            <span v-if="isSubmitting">
              <svg class="spinner" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2v4m0 12v4M4.93 4.93l2.83 2.83m8.48 8.48l2.83 2.83M2 12h4m12 0h4M4.93 19.07l2.83-2.83m8.48-8.48l2.83-2.83"/>
              </svg>
              发布中...
            </span>
            <span v-else>发布动态</span>
          </button>
        </div>
      </div>
    </div>

    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      multiple
      style="display: none"
      @change="handleFileSelect"
    >
  </div>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostsStore } from '../stores/posts'
import EmojiPicker from '../components/EmojiPicker.vue'

const router = useRouter()
const userStore = useUserStore()
const postsStore = usePostsStore()

const content = ref('')
const tags = ref([])
const tagInput = ref('')
const selectedImages = ref([])
const visibility = ref('public')
const isSubmitting = ref(false)
const fileInput = ref(null)
const contentTextarea = ref(null)

const hasContent = computed(() => content.value.trim().length > 0)

const canSubmit = computed(() => {
  return content.value.trim().length > 0 || selectedImages.value.length > 0
})

const triggerUpload = () => {
  fileInput.value?.click()
}

// 压缩图片
const compressImage = (base64, maxWidth = 600, maxHeight = 600, quality = 0.5, maxSizeKB = 500) => {
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
      
      // 限制最小尺寸，避免图片太小
      width = Math.max(width, 200)
      height = Math.max(height, 200)
      
      // 创建 canvas
      const canvas = document.createElement('canvas')
      canvas.width = width
      canvas.height = height
      const ctx = canvas.getContext('2d')
      
      // 填充白色背景（避免透明图片变黑）
      ctx.fillStyle = '#FFFFFF'
      ctx.fillRect(0, 0, width, height)
      ctx.drawImage(img, 0, 0, width, height)
      
      // 压缩为 JPEG，循环降低质量直到满足大小要求
      let currentQuality = quality
      let compressedBase64 = canvas.toDataURL('image/jpeg', currentQuality)
      
      // 如果图片仍然太大，继续降低质量
      while (compressedBase64.length > maxSizeKB * 1024 && currentQuality > 0.2) {
        currentQuality -= 0.1
        compressedBase64 = canvas.toDataURL('image/jpeg', currentQuality)
      }
      
      console.log(`压缩完成: ${width}x${height}, 质量: ${currentQuality.toFixed(1)}, 大小: ${(compressedBase64.length / 1024).toFixed(1)}KB`)
      resolve(compressedBase64)
    }
  })
}

const handleFileSelect = async (e) => {
  const files = Array.from(e.target.files)
  
  if (selectedImages.value.length + files.length > 9) {
    alert('最多只能上传9张图片')
    return
  }
  
  // 显示压缩中提示
  const compressingMsg = files.length > 1 ? '压缩图片中...' : '压缩图片中...'
  
  // 使用 Promise 等待所有图片读取并压缩完成
  const processFiles = files.map(async (file) => {
    return new Promise((resolve) => {
      const reader = new FileReader()
      reader.onload = async (e) => {
        const originalBase64 = e.target.result
        console.log('原图大小:', originalBase64.length, '字符')
        
        // 压缩图片
        const compressedBase64 = await compressImage(originalBase64)
        console.log('压缩后大小:', compressedBase64.length, '字符')
        
        resolve(compressedBase64)
      }
      reader.readAsDataURL(file)
    })
  })
  
  const results = await Promise.all(processFiles)
  selectedImages.value.push(...results)
}

const removeImage = (index) => {
  selectedImages.value.splice(index, 1)
}

const addTag = () => {
  const tag = tagInput.value.trim()
  if (tag && !tags.value.includes(tag) && tags.value.length < 5) {
    tags.value.push(tag)
    tagInput.value = ''
  }
}

const removeTag = (index) => {
  tags.value.splice(index, 1)
}

const handleTagBackspace = (e) => {
  if (!tagInput.value && tags.value.length > 0) {
    tags.value.pop()
  }
}

const submitPost = async () => {
  if (!canSubmit.value) return
  
  isSubmitting.value = true
  
  try {
    // 获取当前用户ID
    const userId = userStore.userInfo?.id
    if (!userId) {
      alert('请先登录')
      router.push('/login')
      return
    }
    
    await postsStore.createPost({
      userId: userId,
      content: content.value.trim(),
      images: selectedImages.value,
      tags: tags.value,
      visibility: visibility.value
    })
    
    // 发布成功，跳转到首页
    router.push('/')
  } catch (error) {
    alert('发布失败：' + (error.message || '请重试'))
  } finally {
    isSubmitting.value = false
  }
}

// 插入表情
const insertEmoji = (emoji) => {
  const textarea = contentTextarea.value
  if (!textarea) return
  
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const text = content.value
  
  // 在光标位置插入表情
  content.value = text.substring(0, start) + emoji + text.substring(end)
  
  // 恢复光标位置
  nextTick(() => {
    textarea.focus()
    const newPos = start + emoji.length
    textarea.setSelectionRange(newPos, newPos)
  })
}
</script>

<style scoped>
.create-page {
  min-height: 100vh;
  padding: 100px 20px 40px;
  position: relative;
  overflow: hidden;
}

/* Background Effects */
.bg-effects {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.glow-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.3;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  top: -100px;
  left: -100px;
  animation: float 8s ease-in-out infinite;
}

.orb-2 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #fa709a, #fee140);
  bottom: -50px;
  right: -50px;
  animation: float 10s ease-in-out infinite reverse;
}

.orb-3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #4facfe, #00f2fe);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: float 12s ease-in-out infinite;
}

/* Container */
.create-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 40px;
  position: relative;
  z-index: 1;
}

/* Preview Panel */
.preview-panel {
  position: sticky;
  top: 100px;
  height: fit-content;
}

.preview-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: all var(--transition-normal);
  box-shadow: var(--shadow-card);
}

.preview-card.has-content {
  border-color: rgba(102, 126, 234, 0.3);
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.15);
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border-bottom: 1px solid rgba(102, 126, 234, 0.1);
}

.preview-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.preview-meta {
  display: flex;
  flex-direction: column;
}

.preview-name {
  font-weight: 600;
  font-size: 14px;
  color: var(--text-primary);
}

.preview-time {
  font-size: 12px;
  color: var(--text-tertiary);
}

.preview-body {
  padding: 16px;
}

.preview-text {
  font-size: 14px;
  line-height: 1.7;
  color: var(--text-secondary);
  white-space: pre-wrap;
}

.preview-placeholder {
  font-size: 14px;
  color: var(--text-light);
  font-style: italic;
}

.preview-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.preview-images {
  padding: 0 16px 16px;
}

.preview-grid {
  display: grid;
  gap: 4px;
  border-radius: var(--radius-sm);
  overflow: hidden;
}

.preview-grid.grid-1 {
  grid-template-columns: 1fr;
}

.preview-grid.grid-2 {
  grid-template-columns: 1fr 1fr;
}

.preview-grid.grid-3 {
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}

.preview-grid.grid-3 .preview-img-item:first-child {
  grid-row: span 2;
}

.preview-grid.grid-4 {
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}

.preview-img-item {
  aspect-ratio: 1;
  overflow: hidden;
}

.preview-img-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-footer {
  display: flex;
  padding: 12px 16px;
  border-top: 1px solid rgba(102, 126, 234, 0.1);
  gap: 16px;
}

.preview-stat {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--text-tertiary);
  font-size: 13px;
}

.preview-stat svg {
  width: 16px;
  height: 16px;
}

.preview-tips {
  margin-top: 20px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-md);
  backdrop-filter: blur(10px);
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: var(--text-tertiary);
  margin-bottom: 10px;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-item svg {
  width: 16px;
  height: 16px;
}

/* Editor Panel */
.editor-panel {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-lg);
  padding: 32px;
  box-shadow: var(--shadow-card);
}

.editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32px;
}

.editor-header h1 {
  font-size: 28px;
  font-weight: 700;
}

.gradient-text {
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.close-btn {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.close-btn:hover {
  background: rgba(250, 112, 154, 0.1);
  border-color: rgba(250, 112, 154, 0.3);
  color: var(--accent-pink);
  transform: rotate(90deg);
}

.close-btn svg {
  width: 20px;
  height: 20px;
}

.editor-body {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.input-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.section-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.section-label svg {
  width: 18px;
  height: 18px;
  color: var(--primary);
}

.label-hint {
  margin-left: auto;
  font-size: 12px;
  font-weight: 400;
  color: var(--text-tertiary);
}

.textarea-wrapper {
  position: relative;
}

.content-textarea {
  min-height: 160px;
  font-size: 15px;
  line-height: 1.8;
  resize: vertical;
}

.char-counter {
  position: absolute;
  bottom: 12px;
  right: 12px;
  font-size: 12px;
  color: var(--text-tertiary);
  background: rgba(255, 255, 255, 0.8);
  padding: 4px 10px;
  border-radius: 20px;
}

.char-counter.warning {
  color: var(--accent-orange);
}

/* Content Tools */
.content-tools {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
}

/* Tags */
.tags-input-container {
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-sm);
  padding: 12px;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  background: rgba(102, 126, 234, 0.1);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 20px;
  font-size: 13px;
  color: var(--primary);
}

.remove-tag {
  background: none;
  border: none;
  padding: 2px;
  cursor: pointer;
  color: inherit;
  opacity: 0.6;
  transition: opacity var(--transition-fast);
}

.remove-tag:hover {
  opacity: 1;
}

.remove-tag svg {
  width: 14px;
  height: 14px;
}

.tag-input {
  background: none;
  border: none;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  flex: 1;
  min-width: 100px;
}

.tag-input::placeholder {
  color: var(--text-light);
}

/* Images Upload */
.images-upload {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.uploaded-images {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.uploaded-image {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: var(--radius-sm);
  overflow: hidden;
}

.uploaded-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  border: none;
  border-radius: 50%;
  color: white;
  cursor: pointer;
  opacity: 0;
  transition: all var(--transition-fast);
}

.uploaded-image:hover .remove-image {
  opacity: 1;
}

.remove-image svg {
  width: 14px;
  height: 14px;
}

.upload-btn {
  width: 100px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.6);
  border: 2px dashed rgba(102, 126, 234, 0.3);
  border-radius: var(--radius-sm);
  color: var(--text-tertiary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.upload-btn:hover {
  border-color: var(--primary);
  color: var(--primary);
  background: rgba(102, 126, 234, 0.05);
}

.upload-btn svg {
  width: 24px;
  height: 24px;
}

.upload-btn span {
  font-size: 12px;
}

/* Visibility Options */
.visibility-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.visibility-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.visibility-option input {
  display: none;
}

.option-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  color: var(--text-secondary);
  transition: all var(--transition-fast);
}

.option-icon svg {
  width: 20px;
  height: 20px;
}

.option-info {
  text-align: center;
}

.option-title {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 2px;
}

.option-desc {
  font-size: 12px;
  color: var(--text-tertiary);
}

.visibility-option:hover {
  background: rgba(255, 255, 255, 0.8);
  border-color: rgba(102, 126, 234, 0.3);
}

.visibility-option.active {
  background: rgba(102, 126, 234, 0.1);
  border-color: var(--primary);
}

.visibility-option.active .option-icon {
  background: var(--gradient-primary);
  color: white;
}

/* Footer */
.editor-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid rgba(102, 126, 234, 0.1);
}

.spinner {
  width: 18px;
  height: 18px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Responsive */
@media (max-width: 1024px) {
  .create-container {
    grid-template-columns: 1fr;
  }
  
  .preview-panel {
    position: relative;
    top: 0;
    order: 2;
  }
  
  .editor-panel {
    order: 1;
  }
}

@media (max-width: 640px) {
  .create-page {
    padding: 80px 16px 20px;
  }
  
  .editor-panel {
    padding: 20px;
  }
  
  .visibility-options {
    grid-template-columns: 1fr;
  }
  
  .visibility-option {
    flex-direction: row;
    justify-content: flex-start;
    padding: 16px;
  }
  
  .option-info {
    text-align: left;
  }
}
</style>
