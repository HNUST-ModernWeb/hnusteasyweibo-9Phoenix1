import { get, post, put, del } from './request'

// 标签相关 API
export const tagApi = {
  // 获取所有标签
  getAllTags: () => get('/tags'),
  
  // 根据ID获取标签
  getTagById: (id) => get(`/tags/${id}`),
  
  // 根据名称获取标签
  getTagByName: (name) => get(`/tags/name/${name}`),
  
  // 获取热门标签
  getPopularTags: (limit = 10) => get('/tags/popular', { limit }),
  
  // 创建标签
  createTag: (data) => post('/tags', data),
  
  // 更新标签
  updateTag: (id, data) => put(`/tags/${id}`, data),
  
  // 删除标签
  deleteTag: (id) => del(`/tags/${id}`),
  
  // 检查标签是否存在
  checkTagExists: (name) => get('/tags/check', { name })
}
