<template>
  <div id="SupplypointManagement">
    <div class="management-container">
      <div style="margin-bottom: 30px;font-weight: bold;font-size: 26px;">{{ event.Name }}</div>
      <el-form :model="supplypointForm" label-width="120px">
        <el-form-item>
          <el-input v-model="supplypointForm.supplypointId" placeholder="请输入补给点ID"></el-input>
          <!-- 红色搜索按钮 -->
          <el-button icon="el-icon-search" @click="loadSupplypointDetails">搜索</el-button>
        </el-form-item>
      </el-form>
      <!-- 使用 v-if 确保 existingItems 和 newItems 有数据时才渲染表格 -->
      <div class="existing-items">
        <h3>已有的物品</h3>
        <el-table v-if="existingItems.length" :data="existingItems" max-height="175">
          <el-table-column prop="id" label="物品ID"></el-table-column>
          <el-table-column prop="name" label="物品名"></el-table-column>
          <el-table-column prop="amount" label="物品数量"></el-table-column>
        </el-table>
        <div v-else style="text-align: center;height: 15vh;line-height: 10vh;font-size: 18px;">暂无数据</div>
      </div>
      <div class="new-items">
        <h3>新添加的物品</h3>
        <el-table v-if="newItems.length" :data="newItems" max-height="175">
          <el-table-column prop="id" label="物品ID"></el-table-column>
          <el-table-column prop="name" label="物品名"></el-table-column>
          <el-table-column prop="amount" label="物品数量"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="danger" @click="removeNewItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-else style="text-align: center;height: 15vh;line-height: 10vh;font-size: 18px;">暂无数据</div>
      </div>
      <div class="button-group">
        <el-button type="primary" @click="showItemManagement" :disabled="this.Flag">添加</el-button>
        <el-button type="primary" @click="saveSupplypointDetails" :disabled="this.Flag">保存</el-button>
      </div>
      <ItemManagement v-if="dialogVisible" ref="itemManagement" :show-category-count="true" @save="handleSaveItems" />
    </div>
  </div>

</template>

<script>
import ItemManagement from './ItemManagement.vue';
import { getSupplypointDetails, saveSupplypointDetails, fetchEventName } from '@/api/SupplypointManagement';

export default {
  name: 'SupplypointManagement',
  components: {
    ItemManagement
  },
  data() {
    return {
      supplypointForm: {
        eventName: '',
        supplypointId: ''
      },
      existingItems: [],  // 已有的物品
      newItems: [],       // 新添加的物品
      dialogVisible: false,
      event: [],
      Flag: true
    };
  },
  computed: {
    items() {
      // 合并已有的和新添加的物品
      return [...this.existingItems, ...this.newItems];
    }
  },
  mounted() {
    this.loadEvent(); // 在组件挂载时加载赛事详情
  },
  methods: {
    async loadEvent() {
      const eventId = this.$route.params.event_id; // 使用指定的赛事ID
      try {
        const res = await fetchEventName(eventId);

        if (res) {
          this.event = res.Event; // 获取赛事详情
          console.log('Loaded event:', this.event);
        } else {
          throw new Error('未收到有效响应数据');
        }
      } catch (error) {
        console.error('加载赛事详情失败:', error);
        this.$message.error('加载赛事详情失败');
      }
    },
    async loadSupplypointDetails() {
      const eventId = this.$route.params.event_id; // 使用指定的赛事ID
      const { supplypointId } = this.supplypointForm;
      if (supplypointId) {
        try {
          const response = await getSupplypointDetails(eventId, eventId + supplypointId);
          if (response) {
            this.existingItems = response.map(item => ({
              ...item,
            }));
            console.log('Loaded supply point details:', this.existingItems);
            this.Flag = false
          }
        } catch (error) {
          console.error('加载补给点详情失败:', error);
          this.$message.error('加载补给点详情失败');
        }
      }
    },
    showItemManagement() {
      this.dialogVisible = true;
    },
    handleSaveItems(selectedItems) {
      console.log('Selected Items from ItemManagement:', selectedItems);

      selectedItems.forEach(selectedItem => {
        console.log('Processing item:', selectedItem);
        const existingItem = this.newItems.find(item => item.id === selectedItem.id);
        if (existingItem) {
          // 如果物品已经存在于 newItems 中，累加数量
          existingItem.amount = parseInt(existingItem.amount) + parseInt(selectedItem.quantity);
          console.log('Updated existing item amount:', existingItem);
        } else {
          // 如果物品是第一次添加，直接推入 newItems 并设置数量
          this.newItems.push({
            id: selectedItem.id,
            name: selectedItem.name,
            amount: selectedItem.quantity,  // 确保设置了数量
          });
          console.log('Added new item:', selectedItem);
        }
      });
      console.log('Updated newItems:', this.newItems);
      this.dialogVisible = false;
    },
    async saveSupplypointDetails() {
      const { supplypointId } = this.supplypointForm;
      const supplypointData = this.items.map(item => ({
        item_Id: item.id,
        supplypoint_Id: this.$route.params.event_id + supplypointId,
        amount: item.amount
      }));

      try {
        console.log('Saving supply point details with items:', supplypointData);
        await saveSupplypointDetails(supplypointData);
        this.existingItems = [...this.existingItems, ...this.newItems];
        this.newItems = [];
        this.$message.success('保存成功');
      } catch (error) {
        console.error('保存补给点详情失败:', error);
        this.$message.error('保存失败');
      }
    },
    removeNewItem(index) {
      this.newItems.splice(index, 1);
    },
    resetItemManagement() {
      this.$refs.itemManagement.resetItems();
      console.log('ItemManagement component reset');
    },
  }
};
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';

#SupplypointManagement {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 105vh;
  height: 78vh;
  margin-right: 50px;
  margin-bottom: 75px;
  font-size: 15px;
}

#SupplypointManagement .el-input {
  width: 75%;
}

#SupplypointManagement .el-button {
  font-size: 14px !important;
  background-color: #c63939;
  /*important是为了覆盖原有的Element UI 的按钮组件有默认的样式优先级*/
  color: white;
  border: none !important;
}

#SupplypointManagement .el-button:hover {
  background-color: #a62828;
  /* 悬停时的深红色背景 */
}

#SupplypointManagement .management-container {
  width: 100%;
  height: 85%;
  margin: 0 auto;
  padding: 30px 45px;
  padding-top: 0;
}

#SupplypointManagement .button-group {
  text-align: right;
  margin-top: 20px;
}

#SupplypointManagement .new-items .el-button {
  background-color: transparent;
  color: rgb(64, 158, 255);
  font-weight: bold;
  padding: 5px;
}

#SupplypointManagement .new-items .el-button:hover {
  background-color: transparent;
}
</style>
