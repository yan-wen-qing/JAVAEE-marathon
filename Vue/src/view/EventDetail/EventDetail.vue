<template>
  <div id="EventDetail" :key="$route.fullPath">
    <div style="margin-left: 6%;margin-top: 20px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/EventList' }">赛历</el-breadcrumb-item>
        <el-breadcrumb-item>赛事详情</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="EventDetailContainer" v-if="event">
      <div style="display: flex;justify-content: space-between;">
        <div class="EventDetailTitle">
          {{ event.Name }}
        </div>
        <div class="EventDetailStatistic">
          <el-statistic ref="statistic" @finish="hilarity" format="DD天HH小时mm分钟ss秒" :value="EventCount" title="距离报名结束还有："
            time-indices>
          </el-statistic>
        </div>
      </div>
      <div class="EventDetailContainerInfo">
        <div class="EventDetailContainerInfoOne">
          <div class="EventDetailContainerInfoItem">
            <img src="@/assets/images/location.png" alt="Event Image" class="EventDetailContainerIcon">
            <span><span>赛事名称：</span>{{ event.Name }}</span>
          </div>
          <div class="EventDetailContainerInfoItem">
            <img src="@/assets/images/location.png" alt="Event Image" class="EventDetailContainerIcon">
            <span><span>赛事类型：</span>{{ event.Category }}</span>
          </div>
          <div class="EventDetailContainerInfoItem">
            <img src="@/assets/images/location.png" alt="Event Image" class="EventDetailContainerIcon">
            <span><span>报名开始时间：</span>{{ event.Start_Date }}</span>
          </div>
          <div class="EventDetailContainerInfoItem">
            <img src="@/assets/images/location.png" alt="Event Image" class="EventDetailContainerIcon">
            <span><span>报名结束时间：</span>{{ event.End_Date }}</span>
          </div>
          <div class="EventDetailContainerInfoItem">
            <img src="@/assets/images/location.png" alt="Event Image" class="EventDetailContainerIcon">
            <span><span>正式比赛时间：</span>{{ event.Event_Date }}</span>
          </div>
        </div>
        <div class="EventDetailContainerInfoTwo">
          <div class="EventDetailContainerInfoItem">
            <img src="@/assets/images/location.png" alt="Event Image" class="EventDetailContainerIcon">
            <span>
              <span>天气：</span>
              <a @click="openWeatherModal">详情>></a>
            </span>
          </div>
        </div>
      </div>
      <div class="EventDetailContainerButton">
        <el-button @click="GoToEventRegistration" :disabled="Disabled">选手报名</el-button>
      </div>
      <!-- 详情弹窗 -->
      <WeatherDetailsModal v-if="showWeatherModal" :eventId="this.eventId" @close="closeWeatherModal" />
    </div>
  </div>

</template>

<script>
import { fetchEventDetails } from '@/api/EventDetails';
import WeatherDetailsModal from './WeatherDetail.vue';

export default {
  name: 'EventDetail',
  data() {
    return {
      event: null,
      eventId: '',
      showWeatherModal: false,
      
      EventCount: 0,
      Disabled: false
    };
  },
  components: {
    WeatherDetailsModal,
    
  },
  watch: {
    '$route.params.id': {
      handler: 'loadEvent',
      immediate: true
    }
  },
  mounted() {
    this.loadEvent();
  },
  methods: {
    async loadEvent() {
      this.eventId = this.$route.params.event_id;
      try {
        console.log('请求的 URL:', `http://113.44.75.241:5158/Event/get_by_id?Id=${this.eventId}`);
        const res = await fetchEventDetails(this.eventId);
        console.log('接口响应:', res);

        if (res) {
          this.event = res.Event; // 获取赛事详情
          this.weatherDetails = res.weather; // 获取天气详情
          this.EventCount = Date.now() + (new Date(this.event.End_Date) - Date.now())
        } else {
          this.$message.error('未收到有效响应数据')
          throw new Error('未收到有效响应数据');
        }
      } catch (error) {
        console.error('加载赛事详情失败:', error);
        this.$message.error('加载赛事详情失败');
      }
    },
    openWeatherModal() {
      this.showWeatherModal = true;
    },
    closeWeatherModal() {
      this.showWeatherModal = false;
    },
    openMedicalModal() {
      this.showMedicalModal = false;  // 先设置为 false
      this.$nextTick(() => {
        this.showMedicalModal = true; // 再设置为 true，确保弹窗能正确打开
      });
    },
    closeMedicalModal() {
      this.showMedicalModal = false;
    },
    openPackageModal() {
      // 加载物资详情逻辑
      this.showPackageModal = false;  // 先设置为 false
      this.$nextTick(() => {
        this.showPackageModal = true; // 再设置为 true，确保弹窗能正确打开
      });
    },
    closePackageModal() {
      this.showPackageModal = false;
    },
    openShuttleModal() {
      // 加载接驳车详情逻辑
      this.showShuttleModal = false;  // 先设置为 false
      this.$nextTick(() => {
        this.showShuttleModal = true; // 再设置为 true，确保弹窗能正确打开
      });
    },
    closeShuttleModal() {
      this.showShuttleModal = false;
    },
    openSupplypointModal() {
      // 加载补给点详情逻辑
      this.showSupplypointModal = false;  // 先设置为 false
      this.$nextTick(() => {
        this.showSupplypointModal = true; // 再设置为 true，确保弹窗能正确打开
      });
    },
    closeSupplypointModal() {
      this.showSupplypointModal = false;
    },
    openVolunteerSignupModal() {
      const userRole = localStorage.getItem('UserRole');
      if (userRole !== 'Athlete') {
        this.$message.warning('您的身份无法报名志愿者');
        return;
      }
      this.showVolunteerSignupModal = false
      this.$nextTick(() => {
        this.showVolunteerSignupModal = true; // 再设置为 true，确保弹窗能正确打开
      });
    },
    closeVolunteerSignupModal() {
      this.showVolunteerSignupModal = false
    },
    GoToEventRegistration() {
      this.$router.push({ name: 'EventRegistration', params: { event_id: this.eventId } });
    },
    hilarity() {
      this.$message.warning({
        title: "提示",
        message: "报名已结束",
      });
      this.Disabled = true
    },
  },
}
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/EventDetail.css";
</style>