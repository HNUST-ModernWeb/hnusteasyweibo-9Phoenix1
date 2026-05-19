import { get, post, put, del } from './request'

// 动态图片相关 API
export const postImageApi = {
  // 获取动态的所有图片
  getImagesByPostId: (postId) => get(`/post-images/post/${postId}`),
  
  // 添加单张图片
  addImage: (data) => post('/post-images', data),
  
  // 批量添加图片
  addImages: (postId, imageUrls) => post('/post-images/batch', { postId, imageUrls }),
  
  // 删除单张图片
  deleteImage: (id) => del(`/post-images/${id}`),
  
  // 删除动态的所有图片
  deleteImagesByPostId: (postId) => del(`/post-images/post/${postId}`),
  
  // 获取动态图片数量
  getImageCount: (postId) => get(`/post-images/post/${postId}/count`)
}
