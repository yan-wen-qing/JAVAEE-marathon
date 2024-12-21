<template>
  <div id="UserRegistrationsDetail">
    <el-dialog title="赛事报名详情" :visible.sync="dialogTableVisible" :before-close="handleClose" width="35%">
      <div class="AidStation" v-if="event">
        <div class="AidStationContent">
          <div style="width: 100%;">
            <div class="AidStationItem">
              <span style="width: 3cm;font-weight: bold;">赛事名称：</span><span>
                {{ event.name }}</span>
            </div>
            <div class="AidStationItem">
              <span style="width: 30%;font-weight: bold;">赛事类型：</span><span>
                {{ event.category }}</span>
            </div>
            <div class="AidStationItem">
              <span style="width: 30%;font-weight: bold;">开赛时间：</span><span>
                {{ event.event_Date }}</span>
            </div>
            <div class="AidStationItem">
              <span style="width: 30%;font-weight: bold;">赛事规模：</span><span>
                {{ event.scale }}</span>
            </div>
            <div class="AidStationItem" style="width: 250px;">
              <span style="width: 100%;font-weight: bold;">参赛号码：
                <span v-if="event.result.response === '已中签'" style="margin-left: 15px;">
                  {{ event.result.eventNumber }}</span>
                <span v-else style="margin-left: 15px;font-weight: bold;color:rgb(175,175,175);">
                  {{ event.result.eventNumber }}</span>
              </span>
            </div>
            <div class="AidStationItem">
              <span style="width: 60%;font-weight: bold;">是否被选拔为配速员：</span><span>
                {{ event.pacer_Is_Chosen }}</span>
            </div>
            <div class="AidStationItem">
              <span style="width: 60%;font-weight: bold;">是否被选拔为急救跑者：</span><span>
                {{ event.aid_Is_Chosen }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserRegistrationsDetail',
  props: {
    event: Object
  },
  data() {
    return {
      dialogTableVisible: true
    };
  },

  methods: {
    handleClose() {
      this.dialogTableVisible = false
    },
    aidStationChange(res) {
      if (res === 1)
        return "已分工"
      else
        return "未分工"
    },
    isScheduledChange(res) {
      if (res)
        return '已排班'
      else
        return '未排班'
    },
    jobCategoryChange(res) {
      if (res === '未排班')
        return '未分配'
      else
        return res
    }
  }
}
</script>

<style scoped>
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/Base.css";
@import "@/assets/css/VolunteerDetailForAddStation.css";
</style>
