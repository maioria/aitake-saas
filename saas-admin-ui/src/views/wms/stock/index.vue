<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
    <el-form-item label="类目" prop="categoryId">
        <el-select
          v-model="queryParams.categoryId"
          remote
          filterable
          clearable
          :remote-method="initCategorySelect"
          @focus="initCategorySelect"
          @change="handleCategoryChange"
          reserve-keyword
          :loading="categoryLoading"
          remote-show-suffix
          placeholder="请选择类目"
          class="!w-240px"
        >
          <el-option
            v-for="item in categoryList"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="规格" prop="specId">
        <el-select
          v-model="queryParams.specId"
          filterable
          placeholder="请选择规格"
          @change="handleQuery"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="item in specList"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseId">
        <el-select
          v-model="queryParams.warehouseId"
          remote
          filterable
          clearable
          :remote-method="initWarehouseSelect"
          @focus="initWarehouseSelect"
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
      <el-form-item label="货架" prop="warehouseShelveId">
        <el-select
          v-model="queryParams.shelveId"
          filterable
          @change="handleQuery"
          clearable
          placeholder="请选择货架"
        >
          <el-option
            v-for="item in shelveList"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="货位" prop="storage">
        <el-input
          v-model="queryParams.storage"
          placeholder="请输入所属货位"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
          @change="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-date-picker
          v-model="queryParams.updateTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          @change="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openCreateAndStockForm()"
          v-hasPermi="['wms:stock:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 快捷创建
        </el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['wms:stock:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['wms:stock:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="类目" align="center" prop="categoryName" >
        <template #default="scope">
          <el-popover
          @show="categoryNameEditValue = scope.row.categoryName"
          placement="bottom" title="更改类目" :width="200" trigger="click">
            <template #reference>
              <div class="click-able-column">
                {{ scope.row.categoryName }}
              </div>
            </template>
            <div>
              <div class="input-box">
                <el-input v-model="categoryNameEditValue" />
              </div>
              <div class="btn-box">
                <el-button @click="handleChangeCategoryName(scope.row)"> 确 认 </el-button>
              </div>
            </div>
          </el-popover>
        </template>
        </el-table-column>
      <el-table-column label="规格" align="center" prop="specName" >
        <template #default="scope">
          <el-popover 
          placement="bottom"
          @show="specNameEditValue = scope.row.specName"
          title="更改规格" :width="200" trigger="click">
            <template #reference>
              <div class="click-able-column">
                {{ scope.row.specName }}
              </div>
            </template>
            <div>
              <div class="input-box">
                <el-input v-model="specNameEditValue" />
              </div>
              <div class="btn-box">
                <el-button @click="handleChangeSpecName(scope.row)"> 确 认 </el-button>
              </div>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="图片" align="center">
        <template #default="scope">
          <el-image
           style="width: 30px; height: 20px"
           :src="scope.row.picUrl" :fit="'contain'" :preview-src-list="[scope.row.picUrl]"
           :z-index="100"
           :preview-teleported="true" />
        </template>
      </el-table-column>
      <el-table-column label="仓库" align="center" prop="warehouseName" />
      <el-table-column label="货架" align="center" prop="shelveName" />
      <el-table-column label="货位" align="center" prop="storage" >
        <template #default="scope">
          <el-popover
placement="bottom" 
          @show="storageEditValue = scope.row.storage"
          title="更改货位" :width="200" trigger="click">
            <template #reference>
              <div class="click-able-column">
                {{ scope.row.storage }}
              </div>
            </template>
            <div>
              <div class="input-box">
                <el-input v-model="storageEditValue" />
              </div>
              <div class="btn-box">
                <el-button @click="handleChangeStorage(scope.row)"> 确 认 </el-button>
              </div>
            </div>
          </el-popover>
        </template>
        </el-table-column>
      <el-table-column label="数量" align="center" prop="stock" >
        <template #default="scope">
          <el-popover
placement="bottom" 
          @show="stockEditValue = scope.row.stock"
          title="更改数量" :width="200" trigger="click">
            <template #reference>
              <div class="click-able-column">
                {{ scope.row.stock }}
              </div>
            </template>
            <div>
              <div class="input-box">
                <el-input v-model="stockEditValue" />
              </div>
              <div class="btn-box">
                <el-button @click="handleChangeStock(scope.row)"> 确 认 </el-button>
              </div>
            </div>
          </el-popover>
        </template>
        </el-table-column>
      <el-table-column
        label="更新时间"
        align="center"
        prop="updateTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handleInboundStock(scope.row)"
            v-hasPermi="['wms:stock:update']"
          >
            入库
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleOutboundStock(scope.row)"
            v-hasPermi="['wms:stock:update']"
          >
            出库
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['wms:stock:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <StockForm ref="formRef" @success="getList" />
  <!-- 创建类目、规格并且录入库存 -->
  <CreateAndStock ref="createAndStockRef" @success="getList" />
  <!-- 库存操作弹窗 -->
  <StockExecute ref="stockExecuteRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as StockApi from '@/api/wms/stock'
import * as WarehouseApi from '@/api/wms/warehouse'
import * as CategoryApi from '@/api/wms/category'
import StockForm from './StockForm.vue'
import StockExecute from './StockExecute.vue'
import CreateAndStock from './CreateAndStock.vue'

defineOptions({ name: 'Stock' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  categoryId: undefined,
  specId: undefined,
  warehouseId: undefined,
  shelveId: undefined,
  storage: undefined,
  updateTime: []
})
const queryFormRef = ref() // 搜索的表单
const stockExecuteRef = ref() // 库存操作的弹窗
const createAndStockRef = ref() // 创建类目、规格并且录入库存的弹窗

const exportLoading = ref(false) // 导出的加载中
const categoryLoading = ref(false)
const categoryList = ref([])
const specLoading = ref(false)
const specList = ref([])
const warehouseLoading = ref(false)
const warehouseList = ref([])
const shelveLoading = ref(false)
const shelveList = ref([])

const categoryNameEditValue = ref('')
const specNameEditValue = ref('')
const storageEditValue = ref('')
const stockEditValue = ref('')

/**
 * 列表直接更新类目
 */
 const handleChangeCategoryName = (row) => {
  // 如果 row的name与spuNameEditValue 一致则不更新直接返回
  if (!categoryNameEditValue.value) {
    return
  }
  if (row.categoryName.trim() == categoryNameEditValue.value) {
    // 提示值相同
    message.warning('类目名未变化');
    return
  }
  updateColumn({
    stockId: row.id,
    column: 'CATEGORY',
    value: categoryNameEditValue.value
  })
}

const handleChangeSpecName = (row) => {
  // 如果 row的name与spuNameEditValue 一致则不更新直接返回
  if (!specNameEditValue.value) {
    return
  }
  if (row.specName.trim() == specNameEditValue.value) {
    // 提示值相同
    message.warning('规格名未变化');
    return
  }
  updateColumn({
    stockId: row.id,
    column: 'SPEC',
    value: specNameEditValue.value
  })
}

const handleChangeStorage = (row) => {
  // 如果 row的name与spuNameEditValue 一致则不更新直接返回
  if (!storageEditValue.value) {
    return
  }
  if (row.storage.trim() == storageEditValue.value) {
    // 提示值相同
    message.warning('货位未变化');
    return
  }
  updateColumn({
    stockId: row.id,
    column: 'STORAGE',
    value: storageEditValue.value
  })
}

const handleChangeStock = (row) => {
  // 如果 row的name与spuNameEditValue 一致则不更新直接返回
  if (!stockEditValue.value) {
    return
  }
  // stockEditValue.value 需要是能数字
  if (isNaN(stockEditValue.value)) {
    message.warning('请输入数字');
    return
  }
  if (row.stock == parseInt(stockEditValue.value)) {
    // 提示值相同
    message.warning('数量未变化');
    return
  }
  updateColumn({
    stockId: row.id,
    column: 'STOCK',
    value: stockEditValue.value
  })
}

const updateColumn = (paramData) => {
  StockApi.updateSingleColumn(paramData).then((data) => {
    // 提示成功 并且重新查询数据
    message.success('更新成功')
    getList()
  })
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockApi.getStockPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const openCreateAndStockForm = () => {
  createAndStockRef.value.open()
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await StockApi.deleteStock(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await StockApi.exportStock(queryParams)
    download.excel(data, '库存.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/**
 * 入库操作
 */
const handleInboundStock = async (row: any) => {
  stockExecuteRef.value.open(row.id, 'IN', row.categoryName, row.specName)
}

/**
 * 出库操作
 */
const handleOutboundStock = async (row: any) => {
  stockExecuteRef.value.open(row.id, 'OUT', row.categoryName, row.specName)
}

const initCategorySelect = async (query: string) => {
  // 获取商品列表
  categoryLoading.value = true
  categoryList.value = await CategoryApi.getCategorySelect(query)
  categoryLoading.value = false
}

const handleCategoryChange = async (query: string) => {
  // 获取商品列表
  specLoading.value = true
  specList.value = await CategoryApi.getSpecSelect({categoryId: queryParams.categoryId, query});
  specLoading.value = false
  handleQuery()
}

const initWarehouseSelect = async (query: string) => {
  // 获取仓库列表
  warehouseLoading.value = true
  warehouseList.value = await WarehouseApi.getWarehouseSelect(query)
  warehouseLoading.value = false
}

const handleWarehouseChange = async () => {
  // 获取货架列表
  shelveLoading.value = true
  const data = await WarehouseApi.getShelveSelect({warehouseId: queryParams.warehouseId})
  shelveList.value = data
  shelveLoading.value = true
  handleQuery()
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
<style scoped>
.click-able-column {
  cursor: pointer;
  width: 100%;
  height: 100%;
}
.btn-box {
  margin-top: 10px;
  text-align: right;
}
</style>
