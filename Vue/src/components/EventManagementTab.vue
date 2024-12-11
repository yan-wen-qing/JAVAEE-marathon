<template>
  <div id="EventManagementTab">
    <div class="Event">
      <div style="margin-bottom: 50px;">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/EventList' }">赛历</el-breadcrumb-item>
          <el-breadcrumb-item>赛事管理</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="TabRight" style="margin-right: 15%;">
        <div class="TabFlex">
          <div class="Tab" style="margin-top: 0;">
            <el-menu ref="EventManagementTab" :default-active="ActiveIndexForEventManagementTab"
              class="el-menu-vertical-demo" @select="ActiveIndex" active-text-color="rgb(168, 27, 31)">
              <el-menu-item index="1">
                <span>选手抽签</span>
              </el-menu-item>
              <el-menu-item index="2">
                <span>配速员选拔</span>
              </el-menu-item>
              <el-menu-item index="3">
                <span>急救跑者选拔</span>
              </el-menu-item>
              
            </el-menu>
          </div>
        </div>
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchEventById } from '@/api/Event.js';
export default {
  name: 'EventManagementTab',
  data() {
    return {
      ActiveIndexForEventManagementTab: '',
      IsLottery: false,
      EventManagementTabTitle: ['ParticipantLottery', 'PacerSelection', 'EmergencyRunnerSelection'],
    };
  },
  methods: {
    ActiveIndex(index) {
      if (this.IsLottery || (index !== '2' && index !== '3')) {
        this.ActiveIndexForEventManagementTab = index; // 设置当前激活的菜单项
        this.$router.push({ name: this.EventManagementTabTitle[index - 1], params: { event_id: this.$route.params.event_id, name: this.$route.params.name } });
      }
      else {
        this.$message.warning('请先完成抽签');
        this.$bus.$emit('ActiveIndexForEventManagementTab', '1')
        this.$refs.EventManagementTab.activeIndex = '1';
        this.$router.push({ name: 'ParticipantLottery', params: { event_id: this.$route.params.event_id, name: this.$route.params.name } });
      }
    },
    LotteryPermission() {
      this.IsLottery = true
    },
    checkIsLottery() {
      const eventId = this.$route.params.event_id; // 您需要根据实际情况使用 eventId
      fetchEventById(eventId)
        .then(event => {
          // 根据 is_Drawn 的状态设置 IsLottery
          if (event.Event.Is_Drawn === '是') {
            this.IsLottery = true;
          }
          console.log(event)
        })
        .catch(error => {
          console.error('Failed to load event:', error);
          this.$message.error('加载赛事信息失败，请稍后重试。');
        });
    }
  },
  created() {
    this.checkIsLottery();
  },
  mounted() {
    this.$bus.$on('IsLottery', this.LotteryPermission);
    // 全局总线备用
    this.$bus.$on('ActiveIndexForEventManagementTab', this.ActiveIndex);
  },
  beforeDestroy() {
    this.$bus.$off('ActiveIndexForEventManagementTab', this.ActiveIndex);
    this.$bus.$off('IsLottery', this.LotteryPermission);
  }
};
</script>

<style>
@import 'element-ui/lib/theme-chalk/index.css';
@import "../assets/css/Base.css";
@import "../assets/css/LeftTab.css";

.Event {
  width: 100%;
  height: 100%;
  background-color: rgb(244, 244, 244);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0 100px;
  padding-top: 20px;
  margin-top: 100px;
}
</style>
