/* 提供用于发送的axios实例*/
import axios from 'axios'

// create an axios instance
const service = axios.create({
  baseURL: 'http://113.44.75.241:5158',
  timeout: 50000, // request timeout
  //withCredentials: true//携带cookie
  async: true,
  crossDomain: true,
  headers: {
    'Content-Type': 'application/json', // 设置内容类型
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在请求发送之前可以进行一些处理，比如添加 token
    return config;
  },
  error => {
    // 处理请求错误
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 处理响应数据
    return response.data;
  },
  error => {
    // 处理响应错误
    return Promise.reject(error);
  }
);

//暴露出去
export default service