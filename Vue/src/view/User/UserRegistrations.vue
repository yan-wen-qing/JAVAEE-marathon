<template>
  <div id="UserRegistrations">
    <div class='NotTable' v-if="(this.loading && !this.Flag) || !events.length">
      <div style=" display: table-cell;vertical-align: middle;width: 100vh;height: 300px;">
        未查询到报名比赛！
      </div>
    </div>
    <div style="width: 100%;" v-else>
      <el-main>
        <div style="width: 100%; overflow: auto ;height: 100%;margin-top: 20px;">
          <el-table :data="paginatedResults" v-loading='loading' class="table" row-class-name="clickable-row" stripe>
            <el-table-column prop="name" label="比赛名称" width="300"></el-table-column>
            <el-table-column prop="event_Date" label="开赛时间" width="175"></el-table-column>
            <el-table-column prop="category" label="赛事类型" width="125"></el-table-column>
            <el-table-column prop="result" label="结果" width="150">
              <template slot-scope="scope">
                <div v-if="scope.row.result.response === '已中签'" style="color: #c81623;font-weight: bold;">
                  {{ scope.row.result.response }}
                </div>
                <div v-else-if="scope.row.result.response === '未中签'" style="color:rgb(175,175,175);font-weight: bold;">
                  {{ scope.row.result.response }}</div>
                <div v-else style="color:rgb(230, 162, 60);font-weight: bold;">
                  {{ scope.row.result.response }}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template slot-scope="scope">
                <span style="font-size: 14px;color: rgb(64, 158, 255);font-weight: bold;"
                  @click="handleRowClick(scope.row)">详情</span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-if="events.length > 0" background layout="prev, pager, next" :total="events.length"
            :page-size="pageSize" :current-page.sync="currentPage" @current-change="handlePageChange"
            class="pagination">
          </el-pagination>
        </div>
      </el-main>
    </div>
    <UserRegistrationsDetail :event="selectedEvent" v-if="dialogVisible" @close="dialogVisible = false" />
  </div>
</template>
<script>
import UserRegistrationsDetail from './UserRegistrationsDetail.vue';
import { getMyRegistrations } from '@/api/UserCenter.js'
import { getEventInfo } from '@/api/UserCenter.js'
export default {
  name: 'UserRegistrations',
  components: {
    UserRegistrationsDetail
  },
  mounted() {
    this.ID = localStorage.getItem('UserId')
    getMyRegistrations(this.ID)
      .then((res) => {
        // 调用处理函数，将每个赛事的result字段进行相应处理
        this.processEvents(res);
        this.loading = false
      })
      .catch(error => {
        console.error(error)
        this.$message.error('查询失败');
        this.Flag = false
      });

  },
  data() {
    return {
      ID: '',
      activeMenu: '2',
      searchName: '',
      events: [],
      selectedEvent: null,
      currentPage: 1,
      pageSize: 8,
      loading: true,
      Flag: true,
      dialogVisible: false
    }
  },
  methods: {
    handleRowClick(event) {
      this.selectedEvent = event;  // Set the selected event
      this.dialogVisible = false;  // 先关闭对话框
      this.$nextTick(() => {       // 等待 DOM 完全更新后再打开对话框
        this.dialogVisible = true; // 重新显示子组件
      });
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    async processEvents(events) {
      this.events = await Promise.all(events.map(async (event) => {
        /*
        if (event.is_Drawn === "否") {
          // 如果 is_Drawn 为 "否"，直接设置 result 为 "未抽签"
          event.result = "未抽签";
        } else {
          // 否则从数据库获取 result 值
          event.result = await this.getResultFromAPI(this.ID, event.id);
        }
        */
        event.result = await this.getResultFromAPI(this.ID, event.id);
        return event;
      }));

      // 打印或使用处理后的数据
      console.log('处理后的赛事数据:', this.events);
    },
    getResultFromAPI(playerId, eventId) {
      // 使用 API 查询具体的 result 值
      return getEventInfo(playerId, eventId)
        .then(response => {
          // 根据实际的 API 返回结构解析 result 值
          if (response === "未中签" || response === "尚未抽签")
            return {
              response,
              eventNumber: '未中签'
            };
          else {
            return {
              response: '已中签',
              eventNumber: response
            };
          }
        })
        .catch(error => {
          console.error(`获取赛事 ${eventId} 的结果失败:`, error);
          return "查询失败"; // 处理错误时返回一个默认值
        });
    }
  },
  computed: {
    paginatedResults() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.events.slice(start, end);
    }
  },
}
</script>

<style scoped>
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/Base.css";
@import "@/assets/css/UserTable.css";

#UserRegistrations {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 16px;
  margin-right: 7%;
  width: 102vh;
  height: 65vh;
  margin-top: -25px;
  margin-bottom: 75px;
}
</style>