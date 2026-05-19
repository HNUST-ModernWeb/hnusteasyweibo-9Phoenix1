import { get, post, put, del } from './request'

// 动态相关 API
export const postApi = {
  // 获取所有动态
  getAllPosts: () => get('/posts'),
  
  // 获取公开动态
  getPublicPosts: () => get('/posts/public'),
  
  // 获取动态详情
  getPostById: (id) => get(`/posts/${id}`),
  
  // 获取用户动态
  getPostsByUserId: (userId) => get(`/posts/user/${userId}`),
  
  // 发布动态
  createPost: (data) => post('/posts', data),
  
  // 更新动态
  updatePost: (id, data) => put(`/posts/${id}`, data),
  
  // 删除动态（使用查询参数）
  deletePost: (id, userId) => del(`/posts/${id}?userId=${userId}`),
  
  // 点赞
  likePost: (id) => post(`/posts/${id}/like`),
  
  // 取消点赞
  unlikePost: (id) => post(`/posts/${id}/unlike`),
  
  // 搜索动态
  searchPosts: (keyword) => get('/posts/search', { keyword }),
  
  // 获取动态评论数
  getPostCountByUser: (userId) => get(`/posts/user/${userId}/count`),
  
  // 获取用户总获赞数
  getUserTotalLikes: (userId) => get(`/posts/user/${userId}/total-likes`)
}
