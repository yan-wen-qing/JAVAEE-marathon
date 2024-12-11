<template>
  <div id="PacerSelection">
    <div style="display: flex;  width: 100%;">
      <el-main class="Content" v-if="paginatedParticipants.length > 0">
        <div style="margin-top: 10px; margin-bottom: 25px;margin-left: 15px;;font-weight: bold;font-size: 26px;">{{
          this.$route.params.name }}</div>
        <div class="Button" v-if="pacer_is_chosen === '否'">
          <div style="margin-left: 65%;">
            <el-button type="primary" @click="openConfirmDialog" style="margin: 0 10px;"
              :disabled="pacer_is_chosen === '是'">提交</el-button>
            <el-button type="primary" @click="cancel" :disabled="pacer_is_chosen === '是'">取消</el-button>
          </div>
        </div>
        <div v-else style="text-align: left;">
          <div style="margin-bottom: 20px; margin-left: 15px;font-weight: bold;font-size: 26px;">配速员名单</div>
        </div>
        <div>
          <el-table :data="paginatedParticipants" class="Table" style="width: 100%" max-height="400">
            <el-table-column prop="id" label="ID" width="100"></el-table-column>
            <el-table-column prop="name" label="姓名" width="120"></el-table-column>
            <el-table-column prop="sex" label="性别" width="100"></el-table-column>
            <el-table-column prop="age" label="年龄" width="100"></el-table-column>
            <el-table-column prop="number" label="参赛号码" width="120"></el-table-column>
            <el-table-column prop="history" label="历史成绩" width="150">
              <template slot-scope="scope">
                <el-button @click="showHistory(scope.row)">查看详情</el-button>
              </template>
            </el-table-column>
            <el-table-column label="选为配速员" width="120" v-if="pacer_is_chosen === '否'">
              <template slot-scope="scope">
                <el-checkbox v-model="scope.row.isPacer" :disabled="pacer_is_chosen === '是'"></el-checkbox>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination class="Pagination" background layout="prev, pager, next" :total="totalParticipantsCount"
            :page-size="pageSize" @current-change="handlePageChange"></el-pagination>
        </div>
      </el-main>
      <div v-else class="Empty">暂无数据</div>

      <el-dialog :visible.sync="confirmDialogVisible" title="确定配速员名单为" width="50%">
        <div>
          <el-table :data="selectedPacers" style="width: 100%">
            <el-table-column prop="id" label="ID" width="100"></el-table-column>
            <el-table-column prop="name" label="姓名" width="120"></el-table-column>
            <el-table-column prop="sex" label="性别" width="80"></el-table-column>
            <el-table-column prop="age" label="年龄" width="80"></el-table-column>
            <el-table-column prop="number" label="参赛号码" width="110"></el-table-column>
          </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="confirmDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmSelection">确定</el-button>
        </span>
      </el-dialog>

      <el-dialog :visible.sync="historyDialogVisible" title="历史成绩" width="60%">
        <div v-if="selectedParticipantHistory.length > 0">
          <el-table :data="selectedParticipantHistory" class="HistoryTable" style="width: 100%" max-height="350">
            <el-table-column prop="index" label="序号" width="100"></el-table-column>
            <el-table-column prop="eventDate" label="开赛时间" width="125"></el-table-column>
            <el-table-column prop="eventName" label="赛事名称" width="250"></el-table-column>
            <el-table-column prop="eventType" label="赛事类型" width="125"></el-table-column>
            <el-table-column prop="result" label="成绩" width="125">
              <template slot-scope="scope">
                <div style="font-weight: bold;">{{ formatSeconds(scope.row.result) }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="ranking" label="名次" width="100">
              <template slot-scope="scope">
                <div style="font-weight: bold;color: rgb(168, 27, 31);">{{ scope.row.ranking }}</div>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else class="Empty">
          暂无数据
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="historyDialogVisible = false">关闭</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  fetchPlayersByEvent,
  fetchPlayerDetails,
  fetchPlayerHistory,
  choosePacer,
} from '@/api/EventManagement';
import { fetchEventById } from '@/api/Event';
export default {
  name: 'PacerSelection',
  data() {
    return {
      totalParticipants: '',
      historyDialogVisible: false,
      confirmDialogVisible: false, // 新增确认对话框的显示状态
      selectedParticipantHistory: '',
      selectedPacers: [], // 保存选中的配速员信息
      participants: [],
      currentPage: 1,
      pageSize: 5,
      pacer_is_chosen: '否',
    };
  },
  computed: {
    totalParticipantsCount() {
      return this.participants.length;
    },
    paginatedParticipants() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      return this.participants.slice(start, end);
    },
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    showHistory(participant) {
      this.selectedParticipantHistory = participant.history || [];
      this.historyDialogVisible = true;
    },
    openConfirmDialog() {
      this.selectedPacers = this.participants.filter(p => p.isPacer);
      if (this.selectedPacers.length > 0) {
        this.confirmDialogVisible = true; // 打开确认对话框
      } else {
        this.$message.warning('请至少选择一个配速员');
      }
    },
    async confirmSelection() {
      try {
        const notselected = this.participants.filter(p => !p.isPacer);
        const payload = notselected.map(p => ({
          Number_: p.number,
          Role_: 'pacer',
          Player_Id: p.id,
          Event_Id: this.$route.params.event_id,
        }));

        payload.push({
          Number_: null,
          Role_: 'pacer',
          Player_Id: 0,
          Event_Id: this.$route.params.event_id,
        });

        const response = await choosePacer(payload);

        if (response === true) {
          this.$message.success('提交成功');
        } else {
          this.$message.error('提交失败，请稍后重试');
        }

        this.confirmDialogVisible = false; // 关闭确认对话框
        this.loadInitialData();
        this.loadEvent();
      } catch (error) {
        this.$message.error('提交过程中发生错误');
      }
    },
    cancel() {
      this.participants.forEach(p => p.isPacer = false);
      this.$message.info('已取消所有勾选');
    },
    loadEvent() {
      const eventId = this.$route.params.event_id;
      return fetchEventById(eventId)
        .then(event => {
          console.log('Event fetched:', event);
          this.pacer_is_chosen = event.Event.Pacer_Is_Chosen;
          if (this.pacer_is_chosen === '是') {
            this.$message.warning('该赛事已经进行过配速员选拔')
          }
        })
        .catch(error => {
          console.error('Failed to load event:', error);
          this.$message.error('加载赛事信息失败，请稍后重试。');
        });
    },
    async loadInitialData() {
      try {
        const eventId = this.$route.params.event_id;
        const players = await fetchPlayersByEvent(eventId);
        const pacers = players.filter(player => player.role_ === 'pacer' && player.number_ !== null);

        const pacerDetailsPromises = pacers.map(async (pacer) => {
          const details = await fetchPlayerDetails(pacer.player_Id);
          let history = await fetchPlayerHistory(pacer.player_Id);
          history = Array.isArray(history) ? history : [];

          const historyWithEventDetailsPromises = history.map(async (h, index) => {
            const eventDetails = await fetchEventById(h.event_Id);
            if (!eventDetails || !eventDetails.Event) {
              console.error(`Failed to fetch event details for event_Id: ${h.event_Id}`);
              return {
                index: index + 1,
                eventDate: 'N/A',
                eventName: 'N/A',
                eventType: 'N/A',
                result: h.net_Result,
                ranking: h.rank,
              };
            }

            const event = eventDetails.Event;
            return {
              index: index + 1,
              eventDate: this.formatDate(event.Event_Date),
              eventName: event.Name,
              eventType: event.Category,
              result: h.net_Result,
              ranking: h.rank,
            };
          });

          const detailedHistory = await Promise.all(historyWithEventDetailsPromises);

          return {
            id: details.Id,
            name: details.Name,
            sex: details.Gender,
            age: details.Age,
            number: pacer.number_,
            isPacer: false,
            history: detailedHistory,
          };
        });

        this.participants = await Promise.all(pacerDetailsPromises);
      } catch (error) {
        console.error('Failed to load initial pacers data:', error);
      }
    },
    formatSeconds(seconds) {
      // 计算小时数
      const hours = Math.floor(seconds / 3600);
      // 计算剩余的分钟数
      const minutes = Math.floor((seconds % 3600) / 60);
      // 剩余的秒数
      const secs = seconds % 60;

      // 格式化为HH:MM:SS形式，确保个位数前面补0
      return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
    }
  },
  created() {
    this.loadEvent();
    this.loadInitialData().then(() => {
      console.log('Rendering pacers:', this.participants);
    });
  }
};
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/PacerAndEmergencyTable.css";

#PacerSelection {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 105vh;
  height: 72vh;
  margin-right: 35px;
  margin-bottom: 75px;
  font-size: 15px;
}
</style>