<template>
  <div id="ParticipantLottery">
    <el-main class="ParticipantLotteryContent">
      <div class="ParticipantLotteryInput">
        <el-input v-model="totalParticipants" placeholder="请输入总人数" class="InputTotalParticipants"
          :disabled="isDrawn === '是'"></el-input>
        <el-button type="primary" @click="drawLots" :disabled="isDrawn === '是'">抽签</el-button>
      </div>
      <div v-if="paginatedParticipants.length > 0 || isLoading">
        <el-table :data="paginatedParticipants" class="ParticipantLotteryTable" max-height="420" v-loading="isLoading">
          <el-table-column prop="id" label="ID" width="100"></el-table-column>
          <el-table-column prop="name" label="姓名" width="120"></el-table-column>
          <el-table-column prop="sex" label="性别" width="100"></el-table-column>
          <el-table-column prop="age" label="年龄" width="100"></el-table-column>
          <el-table-column prop="role" label="参赛身份" width="150"></el-table-column>
          <el-table-column prop="state" label="报名状态" width="120">
            <template slot-scope="scope">
              <div v-if="scope.row.state === '已中签'" style="font-weight: bold;color: rgb(168, 27, 31);">
                {{ scope.row.state }}</div>
              <div v-else-if="scope.row.state === '未中签'" style="font-weight: bold;color:rgb(175,175,175)">
                {{ scope.row.state }}</div>
              <div v-else style="font-weight: bold;color:rgb(230, 162, 60);">{{ scope.row.state }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="number" label="参赛号码" width="150"></el-table-column>
        </el-table>
        <el-pagination class="Pagination" background layout="prev, pager, next" :total="totalParticipantsCount"
          :page-size="pageSize" @current-change="handlePageChange"></el-pagination>
      </div>
      <div v-else class="Empty" style="margin-top: 50px;">
        暂无数据
      </div>
    </el-main>
  </div>
</template>

<script>
import { fetchEventById } from '@/api/Event.js';
import { fetchPlayersByEvent, fetchPlayerDetails, updateEvent, uploadBallotResults, getNumberByEventIdAndPlayerId } from '@/api/EventManagement.js';
export default {
  name: 'ParticipantLottery',
  data() {
    return {
      totalParticipants: '',
      participants: [],
      currentPage: 1,
      pageSize: 8,
      isDrawn: '否',
      isLoading: false
    }
  },
  computed: {
    totalParticipantsCount() {
      if (!Array.isArray(this.participants) || this.participants.length === 0) {
        return 1; // 如果participants为空或不是数组，返回空数组
      }
      else
        return this.participants.length;
    },
    paginatedParticipants() {
      if (!Array.isArray(this.participants) || this.participants.length === 0) {
        return []; // 如果participants为空或不是数组，返回空数组
      }
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      return this.participants.slice(start, end);
    },
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString);

      const day = date.getDate().toString().padStart(2, '0');
      const month = (date.getMonth() + 1).toString(); // 月份从0开始
      const year = date.getFullYear().toString().slice(2); // 只取年份的后两位

      return `${day}-${month}月-${year}`;
    },
    async loadEvent() {
      const eventId = this.$route.params.event_id;
      try {
        this.isLoading = true;  // 开始加载时
        const event = await fetchEventById(eventId);
        console.log('Event fetched:', event);
        this.isDrawn = event.Event.Is_Drawn;
        if (this.isDrawn === '是') {
          this.participants.forEach(p => {
            p.state = '已抽签';
          });
          this.$message.warning('该赛事已经进行过选手抽签');
        }
      } catch (error) {
        console.error('Failed to load event:', error);
        this.$message.error('加载赛事信息失败，请稍后重试。');
      } finally {
        this.isLoading = false;  // 加载完成后
      }
    },
    loadParticipants() {
      this.isLoading = true;  // 开始加载
      const eventId = this.$route.params.event_id;
      fetchPlayersByEvent(eventId)
        .then(response => {
          if (Array.isArray(response) && response.length > 0) {
            const playerPromises = response.map(player =>
              fetchPlayerDetails(player.player_Id).then(detail => {
                let numberPromise;
                if (this.isDrawn === '是') {
                  this.isLoading = true;
                  numberPromise = getNumberByEventIdAndPlayerId(player.player_Id, eventId)
                    .then(res => {
                      return res;
                    })
                    .catch(err => {
                      if (err.response) {
                        console.error('Server response error:', err.response.data);
                      } else {
                        console.error('Failed to get number:', err.message);
                      }
                      return null; // 如果获取失败，返回 null
                    });
                } else {
                  numberPromise = Promise.resolve(null); // 如果未抽签，直接返回 null
                }

                return numberPromise.then(number => {
                  const state = number != '未中签' ? '已中签' : '未中签'; // 根据是否有号码决定报名状态
                  return {
                    id: detail.Id,
                    name: detail.Name,
                    sex: detail.Gender,
                    age: detail.Age,
                    role: player.role_ === 'charity' ? '慈善跑者' : '普通跑者', // 根据 role_ 字段显示身份
                    originalRole: player.role_,
                    state: player.role_ === 'charity' ? '-' : (this.isDrawn === '是' ? (number === '未中签' ? '未中签' : '已中签') : '待抽签'),
                    number: number && number !== '未中签' ? number : '-',
                  };
                });
              })
            );
            return Promise.all(playerPromises);
          } else {
            this.$message.warning('未找到选手数据')
            this.isDrawn = '是'
          }
        })
        .then(players => {
          this.participants = players;
          if (!(!Array.isArray(this.participants) || this.participants.length === 0)) {
            this.participants.sort((a, b) => a.id - b.id);
          }
        })
        .catch(error => {
          console.error('Failed to load participants:', error);
          this.$message.error('加载选手信息失败，请稍后重试。');
        })
        .finally(() => {
          this.isLoading = false;  // 加载结束
        });
    },
    async drawLots() {  // 将这里标记为 async
      try {
        const total = parseInt(this.totalParticipants);
        if (isNaN(total) || total <= 0) {
          this.$message.error('请输入有效的总人数');
          return;
        }

        const lotteryParticipants = this.participants.filter(p => p.role !== '慈善跑者' && p.state === '待抽签');
        const charityParticipants = this.participants.filter(p => p.role === '慈善跑者');

        if (total > lotteryParticipants.length) {
          this.$message.error('输入的总人数超过了待抽签的选手数量');
          return;
        }

        // 随机打乱数组
        for (let i = lotteryParticipants.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [lotteryParticipants[i], lotteryParticipants[j]] = [lotteryParticipants[j], lotteryParticipants[i]];
        }

        const selectedParticipants = lotteryParticipants.slice(0, total);
        const unselectedParticipants = lotteryParticipants.slice(total);
        const totalParticipants = selectedParticipants.length + charityParticipants.length;

        // 生成唯一的比赛号码并平均分配给字母
        const letters = ['A', 'B', 'C', 'D', 'E'];
        const letterCounts = { 'A': 0, 'B': 0, 'C': 0, 'D': 0, 'E': 0 };
        const totalLetters = letters.length;
        const maxPerLetter = Math.floor(totalParticipants / totalLetters);
        const usedNumbers = new Set();

        const generateUniqueNumber = (letter) => {
          let uniqueNumber;
          do {
            const randomNumber = Math.floor(1000 + Math.random() * 9000);
            uniqueNumber = `${letter}${randomNumber}`;
          } while (usedNumbers.has(uniqueNumber));
          usedNumbers.add(uniqueNumber);
          return uniqueNumber;
        };

        const assignNumberToParticipant = (participant) => {
          let assigned = false;
          for (const letter of letters) {
            if (letterCounts[letter] < maxPerLetter) {
              participant.number = generateUniqueNumber(letter);
              letterCounts[letter]++;
              assigned = true;
              break;
            }
          }
          if (!assigned) {
            const minUsedLetter = letters.reduce((prev, curr) => letterCounts[prev] < letterCounts[curr] ? prev : curr);
            participant.number = generateUniqueNumber(minUsedLetter);
            letterCounts[minUsedLetter]++;
          }
        };

        selectedParticipants.forEach((p) => {
          p.state = '已中签';
          assignNumberToParticipant(p);
        });

        unselectedParticipants.forEach((p) => {
          p.state = '未中签';
          p.number = '-';
        });

        charityParticipants.forEach(p => {
          assignNumberToParticipant(p);
        });
        this.$message.success('抽签完成');

        // 构建 ballotData 数据
        const ballotData = [
          ...selectedParticipants,
          ...unselectedParticipants,
          ...charityParticipants
        ].map(p => ({
          number_: p.number === '-' ? '' : p.number,
          role_: p.originalRole,
          player_Id: p.id,
          event_Id: this.$route.params.event_id  // 固定的 event_Id
        }));
        console.log('Ballot data being sent:', ballotData);

        // 调用 API 上传抽签结果
        await uploadBallotResults(ballotData)
        this.$message.success('中签结果上传成功');

        // 获取当前赛事信息
        const event = await fetchEventById(this.$route.params.event_id);

        if (event && event.Event) {
          console.log('Event fetched:', event);
          console.log('Event fetched:', event.Event.Name);
          const updatedEvent = {
            id: event.Event.Id,
            category: event.Event.Category,
            name: event.Event.Name,
            start_Date: this.formatDate(event.Event.Start_Date),
            end_Date: this.formatDate(event.Event.End_Date),
            event_Date: this.formatDate(event.Event.Event_Date),
            is_Drawn: "是",
            Pacer_Is_Chosen: "否",
            Aid_Is_Chosen: "否",
            scale: event.Event.Scale
          };
          updatedEvent.sort((a, b) => a.id - b.id);
          // 更新赛事信息
          const response = await updateEvent(updatedEvent);

          if (response === true) {
            this.$message.success('赛事信息更新成功');
          } else {
            this.$message.error('赛事信息更新失败');
          }
        } else {
          throw new Error('无法获取赛事信息');
        }
      } catch (error) {
        if (error.response) {
          console.error('Failed to update event:', error.response.data);
        } else {
          console.error('Failed to update event:', error.message);
        }
        this.$message.error('更新赛事信息时出错，请稍后重试。');
      }

      this.$bus.$emit('IsLottery');
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
  },
  created() {
    this.loadEvent();
    this.loadParticipants();
  }
}
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/ParticipantLottery.css";
</style>