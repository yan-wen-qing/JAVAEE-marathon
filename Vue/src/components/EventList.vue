<template>
  <div id="EventList">
    <div style="margin-left: 6%;margin-top: 20px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>赛历</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-main>
      <div class="EventFilterContainer">
        <el-select v-model="selectedType" placeholder="请选择赛事类型" class="EventFilterItem">
          <el-option label="全部" value="马"></el-option>
          <el-option label="半马" value="半马"></el-option>
          <el-option label="全马" value="全马"></el-option>
        </el-select>
        <el-date-picker v-model="selectedDate" type="date" placeholder="请选择开赛时间"
          class="EventFilterItem"></el-date-picker>
        <el-input placeholder="请输入赛事名称" v-model="searchName" class="EventFilterItem"></el-input>
      </div>
      <div v-if="filteredEvents.length > 0">
        <el-table :data="filteredEvents" class="EventInfoTable" @row-click="handleRowClick"
          row-class-name="clickable-row">
          <el-table-column prop="name" label="比赛名称" width="250"></el-table-column>
          <el-table-column prop="start_date" label="报名开始时间" width="125"></el-table-column>
          <el-table-column prop="end_date" label="报名结束时间" width="125"></el-table-column>
          <el-table-column prop="date" label="开赛时间" width="125"></el-table-column>
          <el-table-column prop="type" label="赛事类型" width="100"></el-table-column>
          <el-table-column prop="scale" label="赛事规模" width="100"></el-table-column>
          <el-table-column label="赛事总成绩" width="125">
            <template slot-scope="scope">
              <el-button class="EventTableDetail" :class="{ 'disabled-btn': JudgeEventEndDate(scope.row.date) }"
                style="background-color: transparent;padding: 0;border: 0;font-size: 16px;"
                @click.prevent="handleLeaderBoard(scope.row, $event)">查看</el-button>
            </template>
          </el-table-column>
          <el-table-column label="赛事管理" width="100" v-if=JudgeAdmin()>
            <template slot-scope="scope">
              <div class="EventTableDetail" @click.prevent="handleEventManagement(scope.row, $event)">详情</div>
            </template>
          </el-table-column>
          
          <el-table-column label="前往报名" width="100" v-if=JudgeAthlete()>
            <template slot-scope="scope">
              <el-button class="EventTableDetail"
                :class="{ 'disabled-btn': JudgeEventSignupDate(scope.row.start_date, scope.row.end_date) }"
                style="color: rgb(168, 27, 31);background-color: transparent;padding: 0;border: 0;font-size: 16px;"
                @click.prevent="handleRegistration(scope.row, $event)">报名</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination class="Pagination" background layout="prev, pager, next" :total="totalEvents"
          :page-size="pageSize" @current-change="handlePageChange"></el-pagination>
      </div>
      <div v-else class="EventEmpty">
        暂无数据
      </div>
    </el-main>
  </div>
</template>

<script>
import { fetchAllEvents } from '@/api/Event'
export default {
  name: 'EventList',
  data() {
    return {
      activeMenu: '2',
      selectedLocation: '',
      selectedType: '',
      selectedDate: null,
      searchName: '',
      events: [],
      currentPage: 1,
      pageSize: 10,
      my_id: '',

    };
  },
  created() {
    this.loadAllEvents()
  },
  computed: {
    filteredEvents() {
      let filtered = this.events;
      if (this.searchName) {
        filtered = filtered.filter(event => event.name.includes(this.searchName));
      }
      if (this.selectedType) {
        filtered = filtered.filter(event => event.type.includes(this.selectedType));
      }
      if (this.selectedDate) {
        // 将前端选择的日期转换为只有年月日的格式
        const selectedDateString = this.formatDate(this.selectedDate);
        // 筛选日期
        filtered = filtered.filter(event => {
          const eventDateString = this.formatDate(event.date);
          return eventDateString === selectedDateString;
        });
      }
      return filtered.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
    totalEvents() {
      let filtered = this.events;
      if (this.searchName) {
        filtered = filtered.filter(event => event.name.includes(this.searchName));
      }
      if (this.selectedType) {
        filtered = filtered.filter(event => event.type.includes(this.selectedType));
      }
      if (this.selectedDate) {
        // 将前端选择的日期转换为只有年月日的格式
        const selectedDateString = this.formatDate(this.selectedDate);
        // 筛选日期
        filtered = filtered.filter(event => {
          const eventDateString = this.formatDate(event.date);
          return eventDateString === selectedDateString;
        });
      }
      return filtered.length;
    }
  },

  methods: {
    formatDate(dateString) {
      const date = new Date(dateString); // 创建一个Date对象
      const year = date.getFullYear(); // 获取年份
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 获取月份，月份从0开始计数，所以加1
      const day = date.getDate().toString().padStart(2, '0'); // 获取日期

      return `${year}-${month}-${day}`; // 返回格式化的日期字符串
    },
    loadAllEvents() {
      fetchAllEvents().then(response => {
        console.log(response);
        // 映射 eventList 数组
        this.events = response.map(Event => ({
          id: Event.id,
          date: this.formatDate(Event.event_Date),
          name: Event.name,
          type: Event.category,
          scale: Event.scale,
          start_date: this.formatDate(Event.start_Date),
          end_date: this.formatDate(Event.end_Date),
        }))
      }).catch(error => {
        // 错误处理
        this.$message.error('加载赛事失败，请稍后重试。');
        console.error('Failed to load events:', error);
      });
    },
    formatDate(dateString) {
      const date = new Date(dateString); // 创建一个Date对象
      const year = date.getFullYear(); // 获取年份
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 获取月份，月份从0开始计数，所以加1
      const day = date.getDate().toString().padStart(2, '0'); // 获取日期

      return `${year}-${month}-${day}`; // 返回格式化的日期字符串
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    handleEventManagement(row, event) {
      event.stopPropagation();
      this.$router.push({ name: 'EventManagementTab', params: { event_id: row.id, name: row.name } });
    },
    handleEventVolunteer(row, event) {
      event.stopPropagation();
      this.$router.push({ name: 'EventVolunteerTab', params: { event_id: row.id, name: row.name } });
    },
    handleRegistration(row, event) {
      event.stopPropagation();
      if (this.JudgeEventSignupDate(row.start_date, row.end_date))
        this.$message.warning('现在不是报名时间')
      else
        this.$router.push({ name: 'EventDetail', params: { event_id: row.id, name: row.name } });
    },
    handleLeaderBoard(row, event) {
      event.stopPropagation();
      if (this.JudgeEventEndDate(row.date))
        this.$message.warning('赛事还未开始，暂无成绩列表')
      else
        this.$router.push({ name: 'LeaderBoard', params: { event_id: row.id, name: row.name } });
    },
    handleRowClick(event) {
      console.log(event)
      this.$router.push({ name: 'EventDetail', params: { event_id: event.id } });
    },
    JudgeAdmin() {
      return localStorage.getItem('UserRole') === 'Admin'
    },
    JudgeAthlete() {
      return localStorage.getItem('UserRole') === 'Athlete'
    },
    JudgeEventSignupDate(startDate, endDate) {
      const now = new Date(); // 获取当前时间
      const start = new Date(startDate); // 报名开始时间
      const end = new Date(endDate); // 报名结束时间
      // 判断当前时间是否在报名时间范围内
      if (now >= start && now <= end) {
        return false; // 可报名，按钮不禁用
      } else {
        return true; // 不可报名，按钮禁用
      }
    },
    JudgeEventEndDate(time) {
      const now = new Date(); // 获取当前时间
      const start = new Date(time); // 报名开始时间
      // 判断当前时间是否在报名时间范围内
      if (now >= start) {
        return false; // 可报名，按钮不禁用
      } else {
        return true; // 不可报名，按钮禁用
      }
    }
  }
};
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/EventList.css";
</style>