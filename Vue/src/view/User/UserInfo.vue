<template>
  <div id="UserInfo">
    <div class="UserInfo">
      <div class="UserInfoUpper">
        <div class="UserInfoTitle">基本信息</div>
        <button class="relative-button" @click="changeButton">{{ buttonText }}</button>
      </div>
      <hr class="line" />
      <div class="UserInfoText">
        <div class="UserInfoTextRow">
          <p class="UserInfoTextTitle">姓名</p>
          <p class="UserInfoTextContent">{{ name }}</p>
        </div>
        <div class="UserInfoTextRow">
          <p class="UserInfoTextTitle">手机号</p>
          <p class="UserInfoTextContent" v-if="!isEdit">{{ newTelephone }}</p>
          <el-input v-model="newTelephone" placeholder="请输入您的电话" v-if="isEdit" class="UserInfoTextContent"></el-input>
          <p class='UserInfoTextError' v-if="isEdit && !isTelephone">输入有误</p>
        </div>
        <div class="UserInfoTextRow">
          <p class="UserInfoTextTitle">证件</p>
          <p class="UserInfoTextContent">{{ ID_number }}</p>
        </div>
        <div class="UserInfoTextRow">
          <p class="UserInfoTextTitle">ID</p>
          <p class="UserInfoTextContent">{{ ID }}</p>
        </div>
        <div class="UserInfoTextRow">
          <p class="UserInfoTextTitle">性别</p>
          <p class="UserInfoTextContent">{{ gender }}</p>
        </div>
        <div class="UserInfoTextRow">
          <p class="UserInfoTextTitle">年龄</p>
          <p class="UserInfoTextContent" v-if="!isEdit && newAge !== ''">{{ newAge }}</p>
          <el-input v-model="newAge" placeholder="请输入您的年龄" v-if="isEdit" class="UserInfoTextContent"></el-input>
          <p class='UserInfoTextError' v-if="isEdit && !isAge">输入有误</p>
        </div>
        <div class="UserInfoTextRow" style="height: 75px;">
          <p class="UserInfoTextTitle">地区</p>
          <p class="UserInfoTextContent" v-if="!isEdit && newRegion !== ''">{{ newRegion }}</p>
          <div class="UserInfoTextContent" v-if="isEdit">
            <div style="margin: 10px 0;">
              <label for="province" class="UserInfoTextForProvince">省份：</label>
              <el-select v-model="selectedProvince" @change="updateCities" placeholder="请选择" size="mini">
                <el-option v-for="province in provinces" :key="province" :value="province"></el-option>
              </el-select>
            </div>
            <div>
              <label for="province" class="UserInfoTextForCity">城市：</label>
              <el-select v-model="selectedCity" placeholder="请选择" size="mini">
                <el-option v-for="city in cities" :key="city" :value="city"></el-option>
              </el-select>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="UserInfoImg">
      <img src="@/assets/images/KeepRunning.jpg" alt="">
    </div>
  </div>
</template>

<script>
import { completeInfor, getInfor } from '@/api/UserCenter.js'
export default {
  name: 'UserInfo',
  data() {
    return {
      buttonText: "编辑",
      isEdit: false,
      isTelephone: true,
      isAge: true,
      name: '',
      oldTelephone: '',
      newTelephone: '',
      ID_number: '',
      ID: '',
      gender: '',
      oldAge: '',
      newAge: '',
      oldRegion: '',
      newRegion: '',
      Password: '',
      provinces: [
        '北京市', '天津市', '上海市', '重庆市', '河北省', '山西省', '辽宁省', '吉林省', '黑龙江省',
        '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省',
        '广东省', '海南省', '四川省', '贵州省', '云南省', '陕西省', '甘肃省', '青海省',
        '内蒙古自治区', '广西壮族自治区', '西藏自治区', '宁夏回族自治区', '新疆维吾尔自治区'
      ],
      allCities: {
        北京市: ['北京'],
        天津市: ['天津'],
        上海市: ['上海'],
        重庆市: ['重庆'],
        河北省: ['石家庄', '唐山', '秦皇岛', '邯郸', '邢台', '保定', '张家口', '承德', '沧州', '廊坊', '衡水'],
        山西省: ['太原', '大同', '阳泉', '长治', '晋城', '朔州', '晋中', '运城', '忻州', '临汾', '吕梁'],
        辽宁省: ['沈阳', '大连', '鞍山', '抚顺', '本溪', '丹东', '锦州', '营口', '阜新', '辽阳', '盘锦', '铁岭', '朝阳', '葫芦岛'],
        吉林省: ['长春', '吉林', '四平', '辽源', '通化', '白山', '松原', '白城', '延边朝鲜族自治州'],
        黑龙江省: ['哈尔滨', '齐齐哈尔', '鸡西', '鹤岗', '双鸭山', '大庆', '伊春', '佳木斯', '七台河', '牡丹江', '黑河', '绥化', '大兴安岭'],
        江苏省: ['南京', '无锡', '徐州', '常州', '苏州', '南通', '连云港', '淮安', '盐城', '扬州', '镇江', '泰州', '宿迁'],
        浙江省: ['杭州', '宁波', '温州', '嘉兴', '湖州', '绍兴', '金华', '衢州', '舟山', '台州', '丽水'],
        安徽省: ['合肥', '芜湖', '蚌埠', '淮南', '马鞍山', '淮北', '铜陵', '安庆', '黄山', '滁州', '阜阳', '宿州', '巢湖', '六安', '亳州', '池州', '宣城'],
        福建省: ['福州', '厦门', '莆田', '三明', '泉州', '漳州', '南平', '龙岩', '宁德'],
        江西省: ['南昌', '景德镇', '萍乡', '九江', '新余', '鹰潭', '赣州', '吉安', '宜春', '抚州', '上饶'],
        山东省: ['济南', '青岛', '淄博', '枣庄', '东营', '烟台', '潍坊', '济宁', '泰安', '威海', '日照', '莱芜', '临沂', '德州', '聊城', '滨州', '菏泽'],
        河南省: ['郑州', '开封', '洛阳', '平顶山', '安阳', '鹤壁', '新乡', '焦作', '濮阳', '许昌', '漯河', '三门峡', '南阳', '商丘', '信阳', '周口', '驻马店', '济源'],
        湖北省: ['武汉', '黄石', '十堰', '宜昌', '襄阳', '鄂州', '荆门', '孝感', '荆州', '黄冈', '咸宁', '随州', '恩施土家族苗族自治州', '仙桃', '潜江', '天门', '神农架'],
        湖南省: ['长沙', '株洲', '湘潭', '衡阳', '邵阳', '岳阳', '常德', '张家界', '益阳', '郴州', '永州', '怀化', '娄底', '湘西土家族苗族自治州'],
        广东省: ['广州', '深圳', '珠海', '汕头', '韶关', '佛山', '江门', '湛江', '茂名', '肇庆', '惠州', '梅州', '汕尾', '河源', '阳江', '清远', '东莞', '中山', '潮州', '揭阳', '云浮'],
        海南省: ['海口', '三亚', '三沙', '儋州'],
        四川省: ['成都', '自贡', '攀枝花', '泸州', '德阳', '绵阳', '广元', '遂宁', '内江', '乐山', '南充', '眉山', '宜宾', '广安', '达州', '雅安', '巴中', '资阳', '阿坝藏族羌族自治州', '甘孜藏族自治州', '凉山彝族自治州'],
        贵州省: ['贵阳', '六盘水', '遵义', '安顺', '毕节', '铜仁', '黔西南布依族苗族自治州', '黔东南苗族侗族自治州', '黔南布依族苗族自治州'],
        云南省: ['昆明', '曲靖', '玉溪', '保山', '昭通', '丽江', '普洱', '临沧', '楚雄彝族自治州', '红河哈尼族彝族自治州', '文山壮族苗族自治州', '西双版纳傣族自治州', '大理白族自治州', '德宏傣族景颇族自治州', '怒江傈僳族自治州', '迪庆藏族自治州'],
        陕西省: ['西安', '铜川', '宝鸡', '咸阳', '渭南', '延安', '汉中', '榆林', '安康', '商洛'],
        甘肃省: ['兰州', '嘉峪关', '金昌', '白银', '天水', '武威', '张掖', '平凉', '酒泉', '庆阳', '定西', '陇南', '临夏回族自治州', '甘南藏族自治州'],
        青海省: ['西宁', '海东', '海北藏族自治州', '黄南藏族自治州', '海南藏族自治州', '果洛藏族自治州', '玉树藏族自治州', '海西蒙古族藏族自治州'],
        内蒙古自治区: ['呼和浩特', '包头', '乌海', '赤峰', '通辽', '鄂尔多斯', '呼伦贝尔', '巴彦淖尔', '乌兰察布', '兴安盟', '锡林郭勒盟', '阿拉善盟'],
        广西壮族自治区: ['南宁', '柳州', '桂林', '梧州', '北海', '防城港', '钦州', '贵港', '玉林', '百色', '贺州', '河池', '来宾', '崇左'],
        西藏自治区: ['拉萨', '日喀则', '昌都', '林芝', '山南', '那曲', '阿里'],
        宁夏回族自治区: ['银川', '石嘴山', '吴忠', '固原', '中卫'],
        新疆维吾尔自治区: ['乌鲁木齐', '克拉玛依', '吐鲁番', '哈密', '昌吉回族自治州', '博尔塔拉蒙古自治州', '巴音郭楞蒙古自治州', '阿克苏地区', '克孜勒苏柯尔克孜自治州', '喀什地区', '和田地区', '伊犁哈萨克自治州', '塔城地区', '阿勒泰地区', '石河子', '阿拉尔', '图木舒克', '五家渠', '铁门关']
      },
      selectedProvince: '',
      selectedCity: ''
    }
  },
  mounted() {
    this.ID = localStorage.getItem('UserId')
    getInfor(this.ID)
      .then((res) => {
        this.name = res.Name;
        this.oldTelephone = res.Telephone_Number;
        this.newTelephone = res.Telephone_Number;
        this.ID_number = res.Id_Number;
        this.gender = res.Gender;
        this.oldAge = res.Age;
        this.oldRegion = res.Region;
        this.newAge = res.Age;
        this.newRegion = res.Region;
        this.Password = res.Password
      })
      .catch(error => {
        console.error('查询失败:', error);
        alert('查询失败');
      });
  },
  methods: {
    changeButton() {
      //一层：现在是展示状态
      if (!this.isEdit) {
        this.buttonText = '保存'
        this.isEdit = true
      }
      //一层：现在是编辑状态
      else if (this.isEdit) {
        //二层：编辑的内容合规
        if (this.isTelephone && this.isAge) {
          //三层：没有改变数据
          if (this.oldTelephone === this.telephone && this.oldRegion === this.region && this.old_age === this.age) {
            this.buttonText = '编辑'
            this.isEdit = false
          }
          //三层：改变了数据
          else {
            this.$confirm("是否保存数据?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消 ",
              type: "warning",
              distinguishCancelAndClose: true,    // 重要，设置为true才会把右上角X和取消区分开来
              closeOnClickModal: false
            }).then(() => {
              // 确认通过执行逻辑        A按钮逻辑
              this.oldTelephone = this.newTelephone;
              this.oldRegion = this.newRegion;
              this.oldAge = this.newAge;
              /* 调用js */
              this.submit();
              this.buttonText = '编辑'
              this.isEdit = false
            }).catch((e) => {
              if (e == 'cancel') {
                // 确认不通过执行逻辑   B按钮逻辑
              } else if (e == 'close') {
                // 右上角X的执行逻辑 
              }
            })
          }
        }
        //二层：编辑的内容不合规
        else {
          this.$confirm("数据有误", "提示", {
            confirmButtonText: "返回更改",
            cancelButtonText: "取消",
            type: "warning",
            distinguishCancelAndClose: true,    // 重要，设置为true才会把右上角X和取消区分开来
            closeOnClickModal: false
          }).then(() => {
            // 确认通过执行逻辑        A按钮逻辑
            // 保存当前编辑的数据
            this.newTelephone = this.oldTelephone;
            this.newRegion = this.oldRegion;
            this.newAge = this.oldAge;
            this.buttonText = '编辑'
            this.isEdit = false
          }).catch((e) => {
            if (e == 'cancel') {
              // 确认不通过执行逻辑   B按钮逻辑
            } else if (e == 'close') {
              // 右上角X的执行逻辑 
            }
          })
        }
      }
    },
    submit() {
      try {
        completeInfor({ Id: this.ID, Name: this.name, Gender: this.gender, Age: this.newAge, Region: this.newRegion, Telephone_Number: this.newTelephone, Id_Number: this.ID_number, Password: this.Password }).then((res) => {
          if (res.data === false) {
            this.$message.error("提交失败，请重试");
          } else {
            this.$message.success("提交成功");
          }
        })
      } catch (error) {
        console.error(error);
        this.$message.error('提交失败，请重试');
      }
    },
    updateCities() {
      if (this.cities.length > 0) {
        this.selectedCity = this.cities[0]
      } else {
        this.selectedCity = ''
      }
    },
  },
  computed: {
    cities() {
      return this.allCities[this.selectedProvince] || []
    },
  },
  watch: {
    newTelephone(telephone) {
      var reg = /^1\d{10}$/;
      this.isTelephone = reg.test(telephone);
    },
    newAge(age) {
      if (this.age != '') {
        // 检查每一位是否为数字
        if (/^\d+$/.test(age)) {
          const ageNum = parseInt(age, 10);
          if (ageNum >= 18 && ageNum <= 100)
            this.isAge = true;
          else
            this.isAge = false;
        }
        else
          this.isAge = false;
      }
      else
        this.isAge = true;
    },
    selectedProvince() {
      this.newRegion = this.selectedProvince + '    ' + this.selectedCity;
    },
    selectedCity() {
      this.newRegion = this.selectedProvince + '    ' + this.selectedCity;
    }
  },
}
</script>

<style scoped>
@import 'element-ui/lib/theme-chalk/index.css';
@import "@/assets/css/Base.css";
@import "@/assets/css/UserInfo.css";
</style>