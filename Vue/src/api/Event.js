import request from '@/utils/request';

// 获取所有赛事信息（一次性）
export function fetchAllEvents() {
    return request({
        url: '/event/detail/all',
        method: 'get'
    });
}

// 根据 eventId 获取赛事信息
export function fetchEventById(eventId) {
    return request({
        url: `/event/detail/${eventId}`,
        method: 'get'
    });;
  }
