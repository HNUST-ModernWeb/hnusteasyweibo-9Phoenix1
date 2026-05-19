import { get, post, put, del } from './request'

// 评论相关 API
export const commentApi = {
  // 获取动态的所有评论
  getCommentsByPostId: (postId) => get(`/comments/post/${postId}`),
  
  // 获取动态的一级评论（不包含回复）
  getTopLevelComments: (postId) => get(`/comments/post/${postId}/top`),
  
  // 获取评论的回复
  getRepliesByParentId: (parentId) => get(`/comments/parent/${parentId}`),
  
  // 创建评论
  createComment: (data) => post('/comments', data),
  
  // 更新评论
  updateComment: (id, data) => put(`/comments/${id}`, data),
  
  // 删除评论
  deleteComment: (id) => del(`/comments/${id}`),
  
  // 点赞评论
  likeComment: (id) => post(`/comments/${id}/like`),
  
  // 取消点赞评论
  unlikeComment: (id) => post(`/comments/${id}/unlike`),
  
  // 获取动态评论数
  getCommentCountByPost: (postId) => get(`/comments/post/${postId}/count`)
}
