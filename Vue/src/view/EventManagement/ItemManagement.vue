<template>
  <div id="ItemManagement">
    <el-dialog title="物资管理" :visible.sync="dialogVisible" width="50%" :before-close="handleClose">
      <el-table :data="items" max-height="350">
        <el-table-column prop="id" label="物品ID" width="125"></el-table-column>
        <el-table-column prop="name" label="物品名" width="300"></el-table-column>
        <el-table-column label="选择" width="200">
          <template slot-scope="scope">
            <div style="display: inline-block;">
              <el-checkbox v-model="scope.row.selected" @change="handleSelectChange(scope.row)"></el-checkbox>
            </div>
            <div v-if="scope.row.selected" class="input-container">
              <form :model="scope.row" ref="ruleForm" class="demo-ruleForm">
                <el-input v-if="showCategoryCount" v-model="scope.row.quantity" placeholder="输入数量"
                  style="margin-left: 20px; width: 150px;"></el-input>
                <el-select v-if="showCategorySelect" v-model="scope.row.category" placeholder="选择类别"
                  style="margin-left: 20px; width: 150px;">
                  <el-option label="参赛物资" value="参赛物资"></el-option>
                  <el-option label="完赛物资" value="完赛物资"></el-option>
                </el-select>
              </form>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div style=" text-align: right;margin-top: 35px;margin-right: 10px;">
        <button type="primary" @click="saveItems">保存</button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getItem } from '@/api/ItemManagement';

export default {
  name: 'ItemManagement',
  props: {
    showCategorySelect: {
      type: Boolean,
      default: false
    },
    showCategoryCount: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      items: [],
      dialogVisible: true,
    };
  },
  created() {
    this.dialogVisible = true;
    this.loadItems();
  },
  methods: {
    loadItems() {
      getItem()
        .then(response => {
          // 映射API返回的数据，并初始化 selected, quantity, category
          this.items = response.data.map(item => ({
            ...item,
            selected: false,
            quantity: '',
            category: ''
          }));
          console.log('加载的物品数据:', this.items); // 调试信息
        })
        .catch(error => {
          console.error('加载物品数据失败:', error);
        });
    },
    handleSelectChange(row) {
      if (!row.selected) {
        row.quantity = '';
        row.category = '';
      }
    },
    saveItems() {
      // 创建一个临时对象用于合并相同物品ID的数量
      const itemMap = {};

      this.items.forEach(item => {
        if (item.selected) {
          const quantity = parseInt(item.quantity) || 0; // 如果 quantity 不是有效数字，则使用 0
          if (itemMap[item.id]) {
            // 如果物品已经存在，则合并数量
            itemMap[item.id].quantity += quantity;
          } else {
            // 如果物品不存在，则添加到 itemMap
            itemMap[item.id] = {
              id: item.id,
              name: item.name,
              quantity: quantity,
              category: this.showCategorySelect ? item.category : ''
            };
          }
        }
      });

      // 将合并后的物品列表转换为数组
      const selectedItems = Object.values(itemMap);

      console.log('合并后的物品数据:', selectedItems); // 调试信息

      this.$emit('save', selectedItems);
    },
    resetItems() {
      this.items.forEach(item => {
        item.selected = false;
        item.quantity = '';
        item.category = '';
      });
    },
    handleClose() {
      this.dialogVisible = false;
    }
  }
};
</script>

<style>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';

.input-container {
  display: inline-block;
  text-align: center;
}

#ItemManagement .el-dialog__body {
  margin-bottom: 15px;
}

#ItemManagement .el-input {
  width: 90px !important;
}

#ItemManagement .el-select,
#ItemManagement .el-select .el-input {
  width: 120px !important;
}
</style>