import { get, post, put, del } from './request'

// 用户相关 API
export const userApi = {
  // 登录（使用手机号）
  login: (credentials) => post('/users/login', credentials),
  
  // 注册
  register: (data) => post('/users/register', data),
  
  // 获取用户信息
  getUserById: (id) => get(`/users/${id}`),
  
  // 根据手机号获取用户
  getUserByPhone: (phone) => get(`/users/phone/${phone}`),
  
  // 更新用户信息
  updateUser: (id, data) => put(`/users/${id}`, data),
  
  // 上传头像（支持 base64 或文件）
  uploadAvatar: (id, avatarData) => post(`/users/${id}/avatar`, { avatar: avatarData }),
  
  // 修改密码
  updatePassword: (id, password) => put(`/users/${id}/password`, { password }),
  
  // 检查手机号是否存在
  checkPhone: (phone) => get('/users/check-phone', { phone }),
  
  // 检查邮箱是否存在
  checkEmail: (email) => get('/users/check-email', { email }),
  
  // 关注用户
  followUser: (userId, targetUserId) => post(`/users/${userId}/follow/${targetUserId}`),
  
  // 取消关注
  unfollowUser: (userId, targetUserId) => post(`/users/${userId}/unfollow/${targetUserId}`),

  // 获取推荐用户（未关注的用户）
  getRecommendedUsers: (userId, limit = 4) => get('/users/recommendations', { userId, limit })
}
