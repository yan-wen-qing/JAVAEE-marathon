<template>
  <div id="SupplyPointDeleteManagement">
    <div class="ManagementContainer">
      <div style="margin-bottom: 50px;font-weight: bold;font-size: 26px;">{{ this.$route.params.name }}</div>
      <el-table v-if="supplyPoints.length" :data="supplyPoints" class='DeleteButton' max-height="350">
        <el-table-column prop="id" label="补给点ID"></el-table-column>
        <el-table-column prop="place" label="补给位置"></el-table-column>
        <el-table-column prop="kind" label="类型"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="confirmDelete(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-else style="text-align: center;height: 10vh;line-height: 10vh;font-size: 18px;">暂无数据</div>
      <div class="button-group">
        <el-button type="primary" @click="showSupplyPointForm">添加</el-button>
      </div>
      <el-dialog :visible.sync="dialogVisible" width="40%" title="添加补给点" class="Dialog">
        <el-form :model="newSupplyPoint" label-width="150px">
          <el-form-item label="补给点ID">
            <el-input v-model="newSupplyPoint.id" placeholder="输入补给点ID"></el-input>
          </el-form-item>
          <el-form-item label="补给位置">
            <el-input v-model="newSupplyPoint.place" placeholder="输入补给位置"></el-input>
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="newSupplyPoint.kind" placeholder="选择类型">
              <el-option label="水站" value="水站"></el-option>
              <el-option label="功能饮料站" value="功能饮料站"></el-option>
              <el-option label="能量补给点" value="能量补给点"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addSupplyPoint">添加</el-button>
        </div>
      </el-dialog>

      <el-dialog :visible.sync="confirmDialogVisible" width="30%" title="确认删除">
        <span>确定要删除此补给点？</span>
        <div slot="footer" class="dialog-footer">
          <el-button @click="confirmDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteSupplyPoint">确定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>

</template>

<script>
import { get_supply_point, add_supply_point, delete_supply_point } from '@/api/supply'; // 导入API函数

export default {
  name: 'SupplyPointDeleteManagement',
  data() {
    return {
      packageForm: {
        Event_id: this.$route.params.event_id // 默认Event_id为10001
      },
      supplyPoints: [
        // { id: '1', place: '起点', kind: '水站' },
        // { id: '2', place: '终点', kind: '功能饮料站' }
      ],
      dialogVisible: false,
      confirmDialogVisible: false,
      newSupplyPoint: {
        id: '',      // 添加补给点时的ID
        place: '',   // 添加补给点时的位置
        kind: ''     // 添加补给点时的类型
      }
    };
  },
  methods: {
    async fetchSupplyPoints() {
      // 使用默认的 Event_id 来获取补给点信息
      try {
        const response = await get_supply_point(this.packageForm.Event_id);
        console.log(response)
        this.supplyPoints = response; // 假设API返回的数据在data属性中
      } catch (error) {
        console.error('获取补给点信息失败:', error);
      }
    },
    showSupplyPointForm() {
      this.dialogVisible = true;
    },
    async addSupplyPoint() {
      // 检查补给点ID是否已存在
      const existingPoint = this.supplyPoints.find(point => point.id === (this.packageForm.Event_id + this.newSupplyPoint.id));
      if (existingPoint) {
        this.$message.error('补给点ID已存在，请使用唯一ID');
        return;
      }

      try {
        this.newSupplyPoint.id = this.packageForm.Event_id + this.newSupplyPoint.id
        // 调用API添加补给点
        await add_supply_point({
          id: this.newSupplyPoint.id,
          place: this.newSupplyPoint.place,
          kind: this.newSupplyPoint.kind
        });
        this.supplyPoints.push({ ...this.newSupplyPoint });
        this.dialogVisible = false;
        this.$message.success('添加补给点成功!');
      } catch (error) {
        console.error('添加补给点失败:', error);
        this.$message.error('添加补给点失败');
      }

      // 清空表单字段
      this.newSupplyPoint = { id: '', place: '', kind: '' };
    },
    confirmDelete(index) {
      console.log(111, this.supplyPoints[index].id + "")
      this.$confirm('确定要删除此补给点？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await this.deleteSupplyPoint(this.supplyPoints[index].id + "");
        await this.fetchSupplyPoints();
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    async deleteSupplyPoint(id) {
      try {
        await delete_supply_point(id);
        this.confirmDialogVisible = false;
        this.$message.success('删除成功!');
      } catch (error) {
        console.error('删除补给点失败:', error);
        this.$message.error('删除补给点失败');
      }
    }
  },
  created() {
    this.fetchSupplyPoints(); // 页面加载时默认获取补给点信息
  }
};
</script>

<style scoped>
@import "@/assets/css/Base.css";
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/Management.css";
</style>
