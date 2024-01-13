<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="仓库Id" prop="warehouseId">
        <el-input v-model="formData.warehouseId" placeholder="请输入仓库Id" />
      </el-form-item>
      <el-form-item label="仓库名" prop="warehouseName">
        <el-input v-model="formData.warehouseName" placeholder="请输入仓库名" />
      </el-form-item>
      <el-form-item label="货架Id" prop="shelveId">
        <el-input v-model="formData.shelveId" placeholder="请输入货架Id" />
      </el-form-item>
      <el-form-item label="货架" prop="shelveName">
        <el-input v-model="formData.shelveName" placeholder="请输入货架" />
      </el-form-item>
      <el-form-item label="货位" prop="storage">
        <el-input v-model="formData.storage" placeholder="请输入货位" />
      </el-form-item>
      <el-form-item label="类目id" prop="categoryId">
        <el-input v-model="formData.categoryId" placeholder="请输入类目id" />
      </el-form-item>
      <el-form-item label="类目名" prop="categoryName">
        <el-input v-model="formData.categoryName" placeholder="请输入类目名" />
      </el-form-item>
      <el-form-item label="规格id" prop="specId">
        <el-input v-model="formData.specId" placeholder="请输入规格id" />
      </el-form-item>
      <el-form-item label="规格名" prop="specName">
        <el-input v-model="formData.specName" placeholder="请输入规格名" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择类型">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="变动数量" prop="stock">
        <el-input v-model="formData.stock" placeholder="请输入变动数量" />
      </el-form-item>
      <el-form-item label="旧库存" prop="oldStock">
        <el-input v-model="formData.oldStock" placeholder="请输入旧库存" />
      </el-form-item>
      <el-form-item label="新库存" prop="newStock">
        <el-input v-model="formData.newStock" placeholder="请输入新库存" />
      </el-form-item>
      <el-form-item label="旧总库存" prop="oldTotalStock">
        <el-input v-model="formData.oldTotalStock" placeholder="请输入旧总库存" />
      </el-form-item>
      <el-form-item label="新总库存" prop="newTotalStock">
        <el-input v-model="formData.newTotalStock" placeholder="请输入新总库存" />
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-input v-model="formData.unit" placeholder="请输入单位" />
      </el-form-item>
      <el-form-item label="单价" prop="price">
        <el-input v-model="formData.price" placeholder="请输入单价" />
      </el-form-item>
      <el-form-item label="总价" prop="totalPrice">
        <el-input v-model="formData.totalPrice" placeholder="请输入总价" />
      </el-form-item>
      <el-form-item label="用户Id" prop="requestUserId">
        <el-input v-model="formData.requestUserId" placeholder="请输入用户Id" />
      </el-form-item>
      <el-form-item label="用户" prop="requestUserName">
        <el-input v-model="formData.requestUserName" placeholder="请输入用户" />
      </el-form-item>
      <el-form-item label="部门Id" prop="requestDeptId">
        <el-input v-model="formData.requestDeptId" placeholder="请输入部门Id" />
      </el-form-item>
      <el-form-item label="部门" prop="requestDeptName">
        <el-input v-model="formData.requestDeptName" placeholder="请输入部门" />
      </el-form-item>
      <el-form-item label="创建人" prop="creatorName">
        <el-input v-model="formData.creatorName" placeholder="请输入创建人" />
      </el-form-item>
      <el-form-item label="事件时间" prop="eventTime">
        <el-date-picker
          v-model="formData.eventTime"
          type="date"
          value-format="x"
          placeholder="选择事件时间"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as StockRecordApi from '@/api/wms/stockrecord'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  warehouseId: undefined,
  warehouseName: undefined,
  shelveId: undefined,
  shelveName: undefined,
  storage: undefined,
  categoryId: undefined,
  categoryName: undefined,
  specId: undefined,
  specName: undefined,
  type: undefined,
  stock: undefined,
  oldStock: undefined,
  newStock: undefined,
  oldTotalStock: undefined,
  newTotalStock: undefined,
  unit: undefined,
  price: undefined,
  totalPrice: undefined,
  requestUserId: undefined,
  requestUserName: undefined,
  requestDeptId: undefined,
  requestDeptName: undefined,
  creatorName: undefined,
  eventTime: undefined,
  remark: undefined
})
const formRules = reactive({
  warehouseId: [{ required: true, message: '仓库Id不能为空', trigger: 'blur' }],
  shelveId: [{ required: true, message: '货架Id不能为空', trigger: 'blur' }],
  categoryId: [{ required: true, message: '类目id不能为空', trigger: 'blur' }],
  specId: [{ required: true, message: '规格id不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '类型不能为空', trigger: 'change' }],
  stock: [{ required: true, message: '变动数量不能为空', trigger: 'blur' }],
  oldStock: [{ required: true, message: '旧库存不能为空', trigger: 'blur' }],
  newStock: [{ required: true, message: '新库存不能为空', trigger: 'blur' }],
  requestUserId: [{ required: true, message: '用户Id不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await StockRecordApi.getStockRecord(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as StockRecordApi.StockRecordVO
    if (formType.value === 'create') {
      await StockRecordApi.createStockRecord(data)
      message.success(t('common.createSuccess'))
    } else {
      await StockRecordApi.updateStockRecord(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    warehouseId: undefined,
    warehouseName: undefined,
    shelveId: undefined,
    shelveName: undefined,
    storage: undefined,
    categoryId: undefined,
    categoryName: undefined,
    specId: undefined,
    specName: undefined,
    type: undefined,
    stock: undefined,
    oldStock: undefined,
    newStock: undefined,
    oldTotalStock: undefined,
    newTotalStock: undefined,
    unit: undefined,
    price: undefined,
    totalPrice: undefined,
    requestUserId: undefined,
    requestUserName: undefined,
    requestDeptId: undefined,
    requestDeptName: undefined,
    creatorName: undefined,
    eventTime: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>