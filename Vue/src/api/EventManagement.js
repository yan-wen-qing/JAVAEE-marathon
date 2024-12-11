import request from '@/utils/request';

// 获取某赛事的所有选手的 ID
export function fetchPlayersByEvent(eventId) {
    return request({
        url: '/EventManage/get_players_by_event',
        method: 'get',
        params: { eventId: eventId }
    });
}

// 根据选手 ID 获取选手的个人信息
export function fetchPlayerDetails(playerId) {
    return request({
        url: '/Auth/get_by_playerid',
        method: 'get',
        params: { Id: playerId }
    });
}

// 更新赛事信息
export function updateEvent(eventData) {
    return request({
        url: '/Event/update_event',
        method: 'patch',
        data: eventData  // 使用 data 而不是 params 传递 body 数据
    });
}

export function uploadBallotResults(ballotData) {
    return request({
      url: '/EventManage/ballot',
      method: 'PATCH',
      data: ballotData
    });
  }

export function getNumberByEventIdAndPlayerId(playerId, eventId) {
  return request({
    url: '/Player/get_number_by_eventid_and_playerid',
    method: 'GET',
    params: {
      playerId: playerId,
      eventId: eventId
    }
  });
}

// 获取选手的历史比赛成绩
export function fetchPlayerHistory(playerId) {
  return request({
    url: '/Result/search_history_result',
    method: 'GET',
    params: {
      Player_Id: playerId,
    },
  });
}

// 选择配速员并提交到后端
export function choosePacer(payload) {
  return request({
    url: '/EventManage/choose_pacer',
    method: 'PATCH',
    data: payload,
  });
}

