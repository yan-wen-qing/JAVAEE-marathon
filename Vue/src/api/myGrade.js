import request from '@/utils/request'
/*刚打开页面，页面显示该选手的所有比赛成绩*/
export function getMyResults(Player_ID) {
    return request({
        url: '/Result/search_history_result',
        method: 'get',
        params: { 
            Player_ID 
        }
    })
}

/*获取半马成绩*/
export function getHalfResults(Player_ID) {
    return request({
        url: '/Result/search_history_half_result',
        method: 'get',
        params: { 
            Player_ID 
        }
    })
}
/*获取全马成绩*/
export function getFullResults(Player_ID) {
    return request({
        url: '/Result/search_history_full_result',
        method: 'get',
        params: { 
            Player_ID
        }
    })
}

