import request from '@/utils/request';

export function getWeatherDetails(Id) {
  console.log('发起请求 - Event ID:', Id);
  
  return request.get(`http://113.44.75.241:5158/Weather/get_weather?event_id=${Id}`)
    .then(response => {
      console.log('请求成功 - 响应数据:', response);
      return response;
    })
    .catch(error => {
      console.error('请求失败 - 错误信息:', error);
      throw error;
    });
}