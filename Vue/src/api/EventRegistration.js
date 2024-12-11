import request from '@/utils/request';

export function getParticipantInfo(Id) {
  console.log('发起请求 - Player ID:', Id);
  
  return request.get(`http://113.44.75.241:5158/Auth/get_by_playerid?Id=${Id}`)
    .then(response => {
      console.log('请求成功 - 响应数据:', response);
      return response;
    })
    .catch(error => {
      console.error('请求失败 - 错误信息:', error);
      throw error;
    });
}

export function registerParticipant(registrationData) {
 
  
  return request({
    url: `http://113.44.75.241:5158/Player/add_participate`,
    method: 'post',
    data: registrationData
  })
  .then(response => {
    console.log('请求成功 - 响应数据:', response);
    return response;
  })
  .catch(error => {
    console.error('请求失败 - 错误信息:', error);
    throw error;
  });
}