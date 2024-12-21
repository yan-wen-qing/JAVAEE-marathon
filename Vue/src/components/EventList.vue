<template>
  <div id="EventList">
    <div style="margin-left: 6%;margin-top: 20px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>赛历</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-main>
      <!-- 筛选条件 -->
      <div class="EventFilterContainer">
        <el-select v-model="selectedType" placeholder="请选择赛事类型" class="EventFilterItem">
          <el-option label="全部" value=""></el-option>
          <el-option label="半马" value="半马"></el-option>
          <el-option label="全马" value="全马"></el-option>
        </el-select>
        <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="请选择开赛时间"
          class="EventFilterItem"
        ></el-date-picker>
        <el-input
          placeholder="请输入赛事名称"
          v-model="searchName"
          class="EventFilterItem"
        ></el-input>
        <el-button type="primary" @click="clearFilters">清空筛选</el-button>
      </div>

      <!-- 表格数据 -->
      <div v-if="filteredEvents.length > 0">
        <el-table
          :data="filteredEvents"
          class="EventInfoTable"
          @row-click="handleRowClick"
          row-class-name="clickable-row"
        >
          <el-table-column prop="name" label="比赛名称" width="250"></el-table-column>
          <el-table-column prop="start_date" label="报名开始时间" width="125"></el-table-column>
          <el-table-column prop="end_date" label="报名结束时间" width="125"></el-table-column>
          <el-table-column prop="date" label="开赛时间" width="125"></el-table-column>
          <el-table-column prop="type" label="赛事类型" width="100"></el-table-column>
          <el-table-column prop="scale" label="赛事规模" width="100"></el-table-column>
          <el-table-column label="赛事总成绩" width="125">
            <template #default="scope">
              <el-button
                class="EventTableDetail"
                :disabled="!isEventStarted(scope.row.date)"
                @click.stop="handleLeaderBoard(scope.row)"
              >
                查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="赛事管理" width="100" v-if="isAdmin">
            <template #default="scope">
              <el-button
                type="text"
                @click.stop="handleEventManagement(scope.row)"
              >
                详情
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="前往报名" width="100" v-if="isAthlete">
            <template #default="scope">
              <el-button
                class="EventTableDetail"
                :disabled="!isSignupOpen(scope.row.start_date, scope.row.end_date)"
                @click.stop="handleRegistration(scope.row)"
              >
                报名
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="Pagination"
          background
          layout="prev, pager, next"
          :total="totalEvents"
          :page-size="pageSize"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
      <div v-else class="EventEmpty">
        暂无数据
      </div>
    </el-main>
  </div>
</template>

<script>
import { fetchAllEvents } from '@/api/Event';

export default {
  name: 'EventList',
  data() {
    return {
      selectedType: '', // 筛选条件：赛事类型
      selectedDate: null, // 筛选条件：赛事日期
      searchName: '', // 筛选条件：赛事名称
      events: [], // 全部赛事数据
      currentPage: 1, // 当前页码
      pageSize: 10, // 每页显示数量
    };
  },
  computed: {
    // 筛选后的赛事数据
    filteredEvents() {
      let filtered = this.events;

      if (this.searchName) {
        filtered = filtered.filter(event =>
          event.name.includes(this.searchName)
        );
      }
      if (this.selectedType) {
        filtered = filtered.filter(event =>
          event.type.includes(this.selectedType)
        );
      }
      if (this.selectedDate) {
        filtered = filtered.filter(event =>
          this.formatDate(event.date) === this.selectedDate
        );
      }

      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      return filtered.slice(start, end);
    },
    // 筛选后总赛事数量
    totalEvents() {
      return this.filteredEvents.length;
    },
    // 是否为管理员
    isAdmin() {
      return localStorage.getItem('UserRole') === 'Admin';
    },
    // 是否为运动员
    isAthlete() {
      return localStorage.getItem('UserRole') === 'Athlete';
    },
  },
  methods: {
    // 加载所有赛事
    loadAllEvents() {
      fetchAllEvents()
        .then(response => {
          this.events = response.map(Event => ({
            id: Event.id,
            date: Event.eventDate,
            name: Event.name,
            type: Event.category,
            scale: Event.scale,
            start_date: Event.startDate,
            end_date: Event.endDate,
          }));
        })
        .catch(error => {
          this.$message.error('加载赛事失败，请稍后重试。');
          console.error('Failed to load events:', error);
        });
    },
    // 清空筛选条件
    clearFilters() {
      this.selectedType = '';
      this.selectedDate = null;
      this.searchName = '';
    },
    // 判断是否可以报名
    isSignupOpen(startDate, endDate) {
      const now = new Date();
      return now >= new Date(startDate) && now <= new Date(endDate);
    },
    // 判断赛事是否已开始
    isEventStarted(eventDate) {
      const now = new Date();
      return now >= new Date(eventDate);
    },
    // 处理分页
    handlePageChange(page) {
      this.currentPage = page;
    },
    // 赛事管理
    handleEventManagement(row) {
      this.$router.push({
        name: 'EventManagementTab',
        params: {
          event_id: row.id,
          name: row.name,
        },
      });
    },
    // 赛事报名
    handleRegistration(row) {
      this.$router.push({
        name: 'EventDetail',
        params: { event_id: row.id, name: row.name },
      });
    },
    // 查看成绩
    handleLeaderBoard(row) {
      this.$router.push({
        name: 'LeaderBoard',
        params: { event_id: row.id, name: row.name },
      });
    },
    // 行点击跳转
    handleRowClick(event) {
      this.$router.push({
        name: 'EventDetail',
        params: { event_id: event.id },
      });
    },
  },
  created() {
    this.loadAllEvents();
  },
};
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/EventList.css";

.EventFilterContainer {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.EventEmpty {
  text-align: center;
  color: gray;
  font-size: 16px;
}

.clickable-row {
  cursor: pointer;
}

.Pagination {
  margin-top: 20px;
}
</style>
