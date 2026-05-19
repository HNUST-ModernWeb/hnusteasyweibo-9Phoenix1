import { get, post, del } from './request'

// 动态标签关联相关 API
export const postTagApi = {
  // 获取动态的所有标签关联
  getPostTags: (postId) => get(`/post-tags/post/${postId}`),
  
  // 为动态添加标签
  addTagToPost: (postId, tagId) => post('/post-tags', { postId, tagId }),
  
  // 移除动态的标签
  removeTagFromPost: (postId, tagId) => del('/post-tags', { postId, tagId }),
  
  // 检查动态是否有某标签
  checkTagExists: (postId, tagId) => get('/post-tags/check', { postId, tagId }),
  
  // 获取动态标签数量
  getTagCount: (postId) => get(`/post-tags/post/${postId}/count`)
}
