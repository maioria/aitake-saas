<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="类目名" prop="categoryName">
        <el-input v-model="formData.categoryName" placeholder="请输入类目名" />
      </el-form-item>
      <el-form-item label="规格名" prop="specName">
        <el-input v-model="formData.specName" placeholder="请输入规格名" />
      </el-form-item>
      <el-form-item label="所属仓库" prop="warehouseId">
        <el-select
          v-model="formData.warehouseId"
          remote
          filterable
          :remote-method="initWarehouseSelector"
          @focus="initWarehouseSelector"
          @change="handleWarehouseChange"
          reserve-keyword
          :loading="warehouseLoading"
          placeholder="请选择仓库"
          remote-show-suffix
        >
          <el-option
            v-for="item in warehouseList"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属货架" prop="shelveId">
        <el-select v-model="formData.shelveId" filterable placeholder="请选择货架">
          <el-option
            v-for="item in shelveList"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属货位" prop="storage">
        <el-input v-model="formData.storage" placeholder="请输入所属货位" />
      </el-form-item>
      <el-form-item label="数量" prop="stock">
        <el-input v-model="formData.stock" placeholder="请输入数量" />
      </el-form-item>
      <el-form-item label="单价" prop="price">
        <el-input v-model="formData.price" placeholder="请输入单价" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as StockApi from '@/api/wms/stock'
import * as CategoryApi from '@/api/wms/category'
import * as WarehouseApi from '@/api/wms/warehouse'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  categoryName: undefined,
  specName: undefined,
  warehouseId: undefined,
  shelveId: undefined,
  storage: undefined,
  stock: undefined,
  price: undefined
})
const categoryList = ref([]) // 商品列表
const categoryLoading = ref(false) // 商品列表的加载中
const warehouseLoading = ref(false) // 仓库列表的加载中
const warehouseList = ref([]) // 仓库列表
const specList = ref([]) // 规格列表
const shelveList = ref([]) // 货架列表

const formRules = reactive({
  categoryName: [{ required: true, message: '类目名不能为空', trigger: 'blur' }],
  specName: [{ required: true, message: '规格名不能为空', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '仓库不能为空', trigger: 'blur' }],
  shelveId: [{ required: true, message: '货架不能为空', trigger: 'blur' }],
  storage: [{ required: true, message: '货位不能为空', trigger: 'blur' }],
  stock: [{ required: true, message: '数量不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  dialogTitle.value = '创建规格和库存'
  resetForm()
}

onMounted(async () => {})

defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value
    await StockApi.createAndStock(data)
    message.success('录入成功')
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    categoryName: undefined,
    specName: undefined,
    warehouseId: undefined,
    shelveId: undefined,
    storage: undefined,
    stock: undefined,
    price: undefined
  }
  formRef.value?.resetFields()
}

const initWarehouseSelector = async (query: string) => {
  // 获取仓库列表
  warehouseLoading.value = true
  warehouseList.value = await WarehouseApi.getWarehouseSelect(query)
  warehouseLoading.value = false
}

const handleWarehouseChange = async (value: string) => {
  if (!value) {
    shelveList.value = [] 
  }
  // 获取货架列表
  shelveList.value = await WarehouseApi.getShelveSelect({ warehouseId: value })
}
</script>
