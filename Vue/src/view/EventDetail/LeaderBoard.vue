<template>
  <div id="LeaderBoard">
    <div style="margin-left: 6%;margin-bottom: 50px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/EventList' }">赛历</el-breadcrumb-item>
        <el-breadcrumb-item>赛事成绩</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="LeaderBoardContent">
      <div>
        <div style="margin-bottom:35px;">
          <div style="font-size: 28px;font-weight: bold;margin: 25px;">{{ this.$route.params.name
            }}</div>
        </div>
        <div v-if="results.length">
          <el-row :gutter="20" class="filter-container">
            <el-col :span="6">
              <el-select v-model="selectedGender" placeholder="性别" @change="filterResults">
                <el-option label="全部" value="全部"></el-option>
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="sortType" placeholder="排序方式" @change="sortResults">
                <el-option label="顺序排列" value="顺序排列"></el-option>
                <el-option label="逆序排列" value="逆序排列"></el-option>
              </el-select>
            </el-col>
          </el-row>

          <!-- 成绩排行榜 -->
          <el-table :data="paginatedResults" :style="{ width: '100%' }" max-height="45vh">
            <el-table-column label="姓名" header-align="center" align="center">
              <template v-slot:default="scope">
                <span>{{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="性别" header-align="center" align="center">
              <template v-slot:default="scope">
                <span>{{ scope.row.gender }}</span>
              </template>
            </el-table-column>
            <el-table-column label="比赛成绩" header-align="center" align="center">
              <template v-slot:default="scope">
                <span style="font-weight: bold;">{{ formatSeconds(scope.row.net_Result) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="比赛排名" header-align="center" align="center">
              <template v-slot:default="scope">
                <span style=" color: rgb(168, 27, 31);font-weight: bold;">{{ scope.row.rank }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else class="Empty" style="height: 50vh;">暂无数据</div>
        <!-- 分页组件 -->
        <el-pagination background layout="prev, pager, next" :total="filteredResults.length" :page-size="10"
          @current-change="handlePageChange" class="Pagination"></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { getRank } from '@/api/rank'; // 导入API函数*/
export default {
  name: 'LeaderBoard',
  data() {
    return {
      selectedGender: '全部',
      sortType: '顺序排列',
      searchQuery: '', // 用于输入 event ID 的搜索框
      currentPage: 1,
      results: [], // 存储比赛成绩的数组
      filteredResults: [], // 筛选后的比赛成绩数组
      itemsPerPage: 10, // 每页显示的结果数
    };
  },
  created() {
    this.fetchRaceResults(); // 页面加载时获取默认数据
  },
  computed: {
    paginatedResults() {
      // 根据当前页和每页显示的数量，计算分页后的数据
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredResults.slice(startIndex, endIndex);
    },
  },
  methods: {
    async fetchRaceResults() {
      try {
        const Event_id = this.$route.params.event_id;
        console.log(this.$route.params)
        const topN = 50;
        const gender = this.selectedGender === '全部' ? '全部' : this.selectedGender;

        // 调用 API 获取数据
        this.results = (await getRank(Event_id, topN, gender));
        this.filterResults(this.results); // 获取数据后进行初步筛选
      } catch (error) {
        console.error('获取比赛成绩失败:', error);
      }
    },
    filterResults() {
      let filtered = [...this.results];

      // 根据性别筛选
      if (this.selectedGender !== '全部') {
        filtered = filtered.filter(item => item.gender === this.selectedGender);
      }

      // 更新筛选后的数据
      this.filteredResults = filtered;

      // 进行排序
      this.sortResults();
    },
    sortResults() {
      // 根据排序方式进行排序
      if (this.sortType === '顺序排列') {
        this.filteredResults.sort((a, b) => a.rank - b.rank);
      } else {
        this.filteredResults.sort((a, b) => b.rank - a.rank);
      }
    },
    handlePageChange(currentPage) {
      this.currentPage = currentPage;
    },
    search() {
      this.fetchRaceResults(); // 在用户输入 event ID 后重新获取数据
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
  }
};
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/LeaderBoard.css";
</style>