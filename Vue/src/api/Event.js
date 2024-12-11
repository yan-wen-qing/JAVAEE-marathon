import request from '@/utils/request';

// 获取所有赛事信息（一次性）
export function fetchAllEvents() {
    return request({
        url: '/Event/get_all_event',  // 使用正确的后端 API 端点
        method: 'get'
    });
}

// 获取某赛事的信息
export function fetchEventById(eventId) {
    return request({
        url: '/Event/get_by_id',
        method: 'get',
        params: { Id: eventId }
    });
}
