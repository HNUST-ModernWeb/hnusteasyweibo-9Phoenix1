import { get, post, del } from './request'

// 关注相关 API
export const followApi = {
  // 关注用户
  followUser: (followerId, followingId) => post('/follows', { followerId, followingId }),
  
  // 取消关注
  unfollowUser: (followerId, followingId) => del('/follows', { followerId, followingId }),
  
  // 检查是否已关注
  checkIsFollowing: (followerId, followingId) => get('/follows/check', { followerId, followingId }),
  
  // 获取用户的关注列表
  getFollowingList: (followerId) => get(`/follows/follower/${followerId}`),
  
  // 获取用户的粉丝列表
  getFollowersList: (followingId) => get(`/follows/following/${followingId}`),
  
  // 获取关注数
  getFollowingCount: (followerId) => get(`/follows/follower/${followerId}/count`),
  
  // 获取粉丝数
  getFollowersCount: (followingId) => get(`/follows/following/${followingId}/count`)
}
