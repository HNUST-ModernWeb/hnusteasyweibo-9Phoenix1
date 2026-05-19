import { get, post, del } from './request'

// 点赞相关 API
export const likeApi = {
  // 点赞动态
  likePost: (postId, userId) => post('/likes', { postId, userId }),

  // 取消点赞（使用查询参数，避免 DELETE 请求体问题）
  unlikePost: (postId, userId) => del(`/likes/unlike?userId=${userId}&postId=${postId}`),

  // 检查用户是否点赞了动态
  checkLikeStatus: (postId, userId) => get('/likes/check', { postId, userId }),

  // 获取动态的点赞数
  getPostLikesCount: (postId) => get(`/likes/post/${postId}/count`),

  // 获取用户点赞的所有动态
  getUserLikes: (userId) => get(`/likes/user/${userId}`)
}
