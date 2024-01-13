<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item v-show="false" label="类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择类型">
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.WMS_STOCK_CHANGE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="类目">
        {{ editCategoryName }}
      </el-form-item>
      <el-form-item label="规格">
        {{ editSpecName }}
      </el-form-item>
      <!-- 数量 -->
      <el-form-item label="数量" prop="stock">
        <el-input
          v-model="formData.stock"
          :min="1"
          :max="99999999"
          :precision="0"
          controls-position="right"
          placeholder="请输入数量"
        />
      </el-form-item>
      <!-- 单价 -->
      <el-form-item label="单价" prop="price">
        <el-input
          v-model="formData.price"
          :min="1"
          :max="99999999"
          :precision="0"
          controls-position="right"
          placeholder="请输入单价"
        />
      </el-form-item>
      <!-- 关联部门 -->
      <el-form-item label="关联部门" prop="requestDepartmentId">
        <el-tree-select
          v-model="formData.requestDepartmentId"
          :data="deptList"
          :props="defaultProps"
          check-strictly
          node-key="id"
          placeholder="请选择归属部门"
          :default-expanded-keys="[118, 120]"
        />
      </el-form-item>
      <!-- 关联用户 -->
      <el-form-item label="关联用户" prop="userId">
        <el-select
          v-model="formData.requestUserId"
          filterable
          reserve-keyword
          placeholder="请选择用户"
          remote-show-suffix
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.nickname"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <!-- 操作日期 -->
      <el-form-item label="操作日期" prop="createTime">
        <el-date-picker
          v-model="formData.eventTime"
          type="datetime"
          value-format="x"
          placeholder="请选择操作日期"
          clearable
        />
      </el-form-item>
      <!-- 备注 -->
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" :rows="2" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as UserApi from '@/api/system/user'
import { getStrDictOptions, DICT_TYPE } from '@/utils/dict'
const message = useMessage() // 消息弹窗
import * as StockApi from '@/api/wms/stock'
import { defaultProps, handleTree } from '@/utils/tree'
import * as DeptApi from '@/api/system/dept'

const deptList = ref<Tree[]>([]) // 树形结构
const userList = ref<UserApi.UserVO[]>([]) // 用户列表
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('操作库存') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formRules = reactive({
  price: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback()
        }
        if (value <= 0) {
          callback(new Error('单价必须大于0'))
        } else {
          callback()
        }
      }
    }
  ],
  type: [{ required: true, message: '类型不能为空' }],
  stock: [
    { required: true, message: '数量不能为空' },
    // 必须大于0
    {
      validator: (rule, value, callback) => {
        if (value <= 0) {
          callback(new Error('库存必须大于0'))
        } else {
          callback()
        }
      }
    }
  ],
  eventTime: [{ required: true, message: '操作日期不能为空' }]
})
const editId = ref('') // 编辑的 ID
const editCategoryName = ref('')
const editSpecName = ref('')

const formData = ref({
  stockId: undefined,
  type: undefined,
  stock: undefined,
  price: undefined,
  requestDepartmentId: undefined,
  requestUserId: undefined,
  eventTime: undefined,
  remark: undefined
})

const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (id: string, type: string, spuName: string, skuName: string) => {
  editId.value = id
  editCategoryName.value = spuName;
  editSpecName.value = skuName;
  dialogVisible.value = true
  // 获得用户列表
  userList.value = await UserApi.getSimpleUserList()

  // 加载部门树
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())

  resetForm()

  if (type && type != null) {
    formData.value.type = type
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    stockId: undefined,
    type: undefined,
    stock: undefined,
    requestUserId: undefined,
    requestDepartmentId: undefined,
    eventTime: undefined,
    remark: undefined,
    price: undefined
  }
  formRef.value?.resetFields()
}
/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调

const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    formData.value.stockId = editId.value
    await StockApi.executeStock(formData.value)
    message.success('调整库存成功')
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
