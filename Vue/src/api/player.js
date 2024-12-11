import request from '@/utils/request';

// 选手注册
export function registerPlayer(data) {
  return request({
    url: '/Auth/add_player',
    method: 'post',
    data: data
  });
}

// 选手登录
export function loginPlayer(data) {
  return request({
    url: '/Auth/login_player',
    method: 'post',
    data: data
  });
}

// 摄影师登录
export function loginPhotographer(data) {
  return request({
    url: '/Auth/login_photographer',
    method: 'post',
    data: data
  });
}

//查找选手信息
export function getPlayerById(Id) {
  return request({
    url: '/Auth/get_by_playerid',
    method: 'get',
    params: { Id } // 通过params传递查询参数Id
  });
}
