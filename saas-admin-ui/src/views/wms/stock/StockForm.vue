<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="类目" prop="categoryId">
        <el-select
          v-model="formData.categoryId"
          remote
          filterable
          :remote-method="initCategorySelector"
          @focus="initCategorySelector"
          reserve-keyword
          :loading="categoryLoading"
          @change="handleCategoryChange"
          remote-show-suffix
          placeholder="请选择类目"
        >
          <el-option
            v-for="item in categoryList"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属规格" prop="specId">
        <el-select v-model="formData.specId" filterable placeholder="请选择规格">
          <el-option
            v-for="item in specList"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
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
import * as stockApi from '@/api/wms/stock'
import * as CategoryApi from '@/api/wms/category'
import * as WarehouseApi from '@/api/wms/warehouse'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  categoryId: undefined,
  specId: undefined,
  warehouseId: undefined,
  shelveId: undefined,
  storage: undefined,
  stock: undefined,
  price: undefined
})
const categoryList = ref([]) // 商品列表
const categoryLoading = ref(false) // 商品列表的加载中
const warehouseLoading = ref(false) // 仓库列表的加载中
const specList = ref([]) // 规格列表
const warehouseList = ref([]) // 仓库列表
const shelveList = ref([]) // 货架列表

const formRules = reactive({
  categoryId: [{ required: true, message: '类目不能为空', trigger: 'blur' }],
  specId: [{ required: true, message: '规格不能为空', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '仓库不能为空', trigger: 'blur' }],
  shelveId: [{ required: true, message: '货架不能为空', trigger: 'blur' }],
  storage: [{ required: true, message: '货位不能为空', trigger: 'blur' }],
  stock: [{ required: true, message: '数量不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
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
    const data = formData.value as unknown as WmsWarehouseStockApi.WmsWarehouseStockVO
    await stockApi.createStock(data)
    message.success(t('common.createSuccess'))
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
    categoryId: undefined,
    specId: undefined,
    warehouseId: undefined,
    shelveId: undefined,
    storage: undefined,
    stock: undefined
  }
  formRef.value?.resetFields()
}

const initCategorySelector = async (query: string) => {
  // 获取商品列表
  categoryLoading.value = true
  categoryList.value = await CategoryApi.getCategorySelect(query)
  categoryLoading.value = false
}

const initWarehouseSelector = async (query: string) => {
  // 获取仓库列表
  warehouseLoading.value = true
  warehouseList.value = await WarehouseApi.getWarehouseSelect(query)
  warehouseLoading.value = false
}

const handleCategoryChange = async (value: string) => {
  // 获取规格列表
  specList.value = await CategoryApi.getSpecSelect({categoryId:value})
}

const handleWarehouseChange = async (value: string) => {
  // 获取货架列表
  shelveList.value = await WarehouseApi.getShelveSelect({warehouseId:value})
}

onMounted(async () => {
})
</script>
