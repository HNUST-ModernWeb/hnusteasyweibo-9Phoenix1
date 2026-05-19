import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useUserStore } from './user'
import { postApi } from '../api/post'
import { postImageApi } from '../api/postImage'
import { postTagApi } from '../api/postTag'
import { tagApi } from '../api/tag'
import { userApi } from '../api/user'
import { likeApi } from '../api/like'

export const usePostsStore = defineStore('posts', () => {
  // State
  const posts = ref([])
  const isLoading = ref(false)
  const currentPost = ref(null)
  const selectedTag = ref(null)
  const hotTags = ref([
    { tag: '生活', count: 128 },
    { tag: '美食', count: 96 },
    { tag: '旅行', count: 84 },
    { tag: '摄影', count: 72 },
    { tag: '音乐', count: 65 },
    { tag: '读书', count: 58 },
    { tag: '运动', count: 52 },
    { tag: '科技', count: 48 }
  ])

  // 转换后端数据为前端需要的格式
  const transformPost = async (post) => {
    const userStore = useUserStore()

    // 获取作者信息
    let author = {
      nickname: '未知用户',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'
    }
    try {
      const user = await userApi.getUserById(post.userId)
      if (user) {
        author = {
          nickname: user.nickname || user.phone,
          avatar: user.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${user.phone}`
        }
      }
    } catch (e) {
      console.error('获取用户信息失败:', e)
    }

    // 获取动态图片
    let images = []
    try {
      const postImages = await postImageApi.getImagesByPostId(post.id)
      images = postImages.map(img => img.imageUrl)
    } catch (e) {
      console.error('获取图片失败:', e)
    }

    // 获取动态标签
    let tags = []
    try {
      const postTags = await postTagApi.getPostTags(post.id)
      // 根据tagId获取标签名称
      for (const pt of postTags) {
        try {
          const tag = await tagApi.getTagById(pt.tagId)
          if (tag && tag.name) {
            tags.push(tag.name)
          }
        } catch (e) {
          console.error(`获取标签${pt.tagId}失败:`, e)
        }
      }
    } catch (e) {
      console.error('获取标签失败:', e)
    }

    // 获取点赞数 - 直接使用 post 对象中的 likesCount（来自 posts 表）
    let likesCount = post.likesCount || 0

    // 获取当前用户的点赞状态
    let isLiked = false
    if (userStore.userInfo?.id) {
      try {
        const response = await likeApi.checkLikeStatus(post.id, userStore.userInfo.id)
        // 后端返回 { liked: true/false } 格式
        if (response && typeof response === 'object' && 'liked' in response) {
          isLiked = response.liked
        } else {
          isLiked = Boolean(response)
        }
      } catch (e) {
        console.error('获取点赞状态失败:', e)
      }
    }

    return {
      id: post.id,
      content: post.content,
      userId: post.userId,
      author: {
        ...author,
        id: post.userId
      },
      images: images,
      tags: tags,
      likes: likesCount,
      comments: [], // 简化处理
      isLiked: isLiked,
      createdAt: post.createdAt
    }
  }

  // Actions
  // 获取所有动态
  const fetchPosts = async () => {
    isLoading.value = true
    try {
      const response = await postApi.getAllPosts()
      // 后端直接返回数组
      const data = Array.isArray(response) ? response : []
      // 转换数据格式
      const transformedPosts = await Promise.all(data.map(transformPost))
      posts.value = transformedPosts
      return posts.value
    } catch (error) {
      console.error('获取动态失败:', error)
      posts.value = []
      throw error
    } finally {
      isLoading.value = false
    }
  }

  // 获取公开动态
  const fetchPublicPosts = async () => {
    isLoading.value = true
    try {
      const response = await postApi.getPublicPosts()
      const data = Array.isArray(response) ? response : []
      const transformedPosts = await Promise.all(data.map(transformPost))
      posts.value = transformedPosts
      return posts.value
    } catch (error) {
      console.error('获取公开动态失败:', error)
      posts.value = []
      throw error
    } finally {
      isLoading.value = false
    }
  }

  // 获取动态详情
  const fetchPostById = async (id) => {
    try {
      const data = await postApi.getPostById(id)
      currentPost.value = await transformPost(data)
      return currentPost.value
    } catch (error) {
      console.error('获取动态详情失败:', error)
      throw error
    }
  }

  // 创建动态
  const createPost = async (postData) => {
    try {
      const userStore = useUserStore()
      
      console.log('创建动态 - 用户ID:', userStore.userInfo?.id)
      console.log('创建动态 - 传入数据:', postData)
      
      // 检查用户是否登录
      if (!userStore.userInfo?.id) {
        throw new Error('用户未登录')
      }
      
      // 转换可见性
      const visibilityMap = {
        'public': 1,
        'followers': 2,
        'private': 0
      }

      // 构建请求数据
      const requestData = {
        userId: userStore.userInfo.id,
        content: postData.content,
        visibility: visibilityMap[postData.visibility] || 1,
        status: 1
      }
      
      console.log('创建动态 - 请求数据:', requestData)

      // 创建动态
      const response = await postApi.createPost(requestData)

      if (response.code === 200 && response.data) {
        const postId = response.data.id
        console.log('动态创建成功，ID:', postId)
        console.log('图片数据:', postData.images)
        console.log('标签数据:', postData.tags)

        // 上传图片（如果有）
        if (postData.images && postData.images.length > 0) {
          console.log('开始上传图片，数量:', postData.images.length)
          for (let i = 0; i < postData.images.length; i++) {
            try {
              const imageData = {
                postId: postId,
                imageUrl: postData.images[i],
                sortOrder: i
              }
              console.log('上传图片 - postId:', postId)
              console.log('上传图片 - imageUrl长度:', postData.images[i]?.length)
              console.log('上传图片 - imageUrl前100字符:', postData.images[i]?.substring(0, 100))
              const imgResponse = await postImageApi.addImage(imageData)
              console.log('图片上传结果:', imgResponse)
            } catch (err) {
              console.error('上传图片失败:', err)
            }
          }
        }

        // 添加标签（如果有）
        if (postData.tags && postData.tags.length > 0) {
          console.log('开始添加标签，数量:', postData.tags.length)
          for (const tagName of postData.tags) {
            try {
              console.log('处理标签:', tagName)
              // 先查找标签是否存在
              let tag = null
              try {
                tag = await tagApi.getTagByName(tagName)
                console.log('找到已存在标签:', tag)
              } catch (e) {
                console.log('标签不存在，创建新标签:', tagName)
                // 标签不存在，创建新标签
                const newTag = await tagApi.createTag({ name: tagName, usageCount: 0 })
                console.log('创建标签结果:', newTag)
                if (newTag.code === 200) {
                  tag = { id: newTag.data?.id }
                }
              }
              
              // 如果标签存在，添加到动态
              if (tag && tag.id) {
                console.log('添加标签到动态:', postId, tag.id)
                const tagResponse = await postTagApi.addTagToPost(postId, tag.id)
                console.log('添加标签结果:', tagResponse)
              } else {
                console.error('标签ID不存在:', tag)
              }
            } catch (err) {
              console.error('添加标签失败:', err)
            }
          }
        }

        return response.data
      } else {
        throw new Error(response.message || '创建动态失败')
      }
    } catch (error) {
      console.error('创建动态失败:', error)
      throw error
    }
  }

  // 删除动态
  const deletePost = async (id, userId) => {
    try {
      const response = await postApi.deletePost(id, userId)
      if (response.code === 200) {
        // 从列表中移除
        posts.value = posts.value.filter(p => p.id !== id)
        return true
      }
      return false
    } catch (error) {
      console.error('删除动态失败:', error)
      throw error
    }
  }

  // 点赞动态
  const likePost = async (postId) => {
    try {
      const response = await postApi.likePost(postId)
      return response.code === 200
    } catch (error) {
      console.error('点赞失败:', error)
      throw error
    }
  }

  // 取消点赞
  const unlikePost = async (postId) => {
    try {
      const response = await postApi.unlikePost(postId)
      return response.code === 200
    } catch (error) {
      console.error('取消点赞失败:', error)
      throw error
    }
  }

  // 设置选中的标签
  const setSelectedTag = (tag) => {
    selectedTag.value = tag
  }

  // 清除标签筛选
  const clearTagFilter = () => {
    selectedTag.value = null
  }

  // 切换点赞状态
  const toggleLike = async (postId) => {
    const userStore = useUserStore()
    const post = posts.value.find(p => p.id === postId)
    
    if (!post) {
      console.error('动态不存在:', postId)
      return
    }
    
    if (!userStore.userInfo?.id) {
      console.error('用户未登录')
      throw new Error('请先登录')
    }
    
    const userId = userStore.userInfo.id
    
    try {
      if (post.isLiked) {
        // 取消点赞
        await likeApi.unlikePost(postId, userId)
        post.isLiked = false
        post.likes = Math.max(0, post.likes - 1)
        console.log('取消点赞成功:', postId)
      } else {
        // 点赞
        await likeApi.likePost(postId, userId)
        post.isLiked = true
        post.likes += 1
        console.log('点赞成功:', postId)
      }
    } catch (error) {
      console.error('点赞操作失败:', error)
      throw error
    }
  }

  // 获取指定用户的动态
  const getPostsByUser = (userId) => {
    return posts.value.filter(post => post.author?.id === userId || post.userId === userId)
  }

  return {
    posts,
    isLoading,
    currentPost,
    selectedTag,
    hotTags,
    fetchPosts,
    fetchPublicPosts,
    fetchPostById,
    createPost,
    deletePost,
    likePost,
    unlikePost,
    setSelectedTag,
    clearTagFilter,
    toggleLike,
    getPostsByUser
  }
})