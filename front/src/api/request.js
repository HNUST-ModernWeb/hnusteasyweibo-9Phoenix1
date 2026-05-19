// API 请求基础配置
// 开发环境使用相对路径，通过 Vite 代理
const BASE_URL = '/api'

// 请求拦截
const request = async (url, options = {}) => {
  // 获取 token
  const token = localStorage.getItem('token')
  
  // 判断是否是 FormData
  const isFormData = options.body instanceof FormData
  
  // 默认配置
  const config = {
    headers: {
      ...(token && { 'Authorization': `Bearer ${token}` }),
      ...(!isFormData && { 'Content-Type': 'application/json' }),
      ...options.headers
    },
    ...options
  }
  
  // 发送请求
  const response = await fetch(`${BASE_URL}${url}`, config)
  
  // 处理响应
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || `请求失败: ${response.status}`)
  }
  
  // 204 No Content
  if (response.status === 204) {
    return null
  }
  
  return response.json()
}

// GET 请求
export const get = (url, params = {}) => {
  const queryString = new URLSearchParams(params).toString()
  const fullUrl = queryString ? `${url}?${queryString}` : url
  return request(fullUrl, { method: 'GET' })
}

// POST 请求
export const post = (url, data = {}) => {
  return request(url, {
    method: 'POST',
    body: JSON.stringify(data)
  })
}

// PUT 请求
export const put = (url, data = {}) => {
  return request(url, {
    method: 'PUT',
    body: JSON.stringify(data)
  })
}

// DELETE 请求
export const del = (url, data = {}) => {
  return request(url, {
    method: 'DELETE',
    body: JSON.stringify(data)
  })
}

export default request
