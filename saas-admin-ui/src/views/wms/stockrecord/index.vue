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
      <el-form-item label="货架" prop="warehouseShelveName">
        <el-input
          v-model="queryParams.warehouseShelveName"
          placeholder="请输入货架"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="货位" prop="warehouseStorage">
        <el-input
          v-model="queryParams.warehouseStorage"
          placeholder="请输入货位"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="类目" prop="productSpuName">
        <el-input
          v-model="queryParams.productSpuName"
          placeholder="请输入类目"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="规格" prop="productSkuName">
        <el-input
          v-model="queryParams.productSkuName"
          placeholder="请输入规格"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="部门" prop="requestDeptId">
        <el-tree-select
          v-model="queryParams.requestDeptId"
          :data="deptList"
          :props="defaultProps"
          check-strictly
          node-key="id"
          clearable
          placeholder="请选择归属部门"
        />
      </el-form-item>
      <el-form-item label="关联用户" prop="requestUserName">
        <el-input
          v-model="queryParams.requestUserName"
          placeholder="请输入用户"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable class="!w-240px">
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.WMS_STOCK_CHANGE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="事件时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.eventTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <!-- <el-form-item label="仓库名" prop="warehouseName">
        <el-input
          v-model="queryParams.warehouseName"
          placeholder="请输入仓库名"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item> -->

      <el-form-item label="创建人" prop="creatorName">
        <el-input
          v-model="queryParams.creatorName"
          placeholder="请输入创建人"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <!-- <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['zkzg:wms-warehouse-stock-record:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button> -->
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['zkzg:wms-warehouse-stock-record:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="list"
      :stripe="true"
      height="450"
      :show-overflow-tooltip="true"
    >
      <!-- <el-table-column label="主键" align="center" prop="id" /> -->
      <el-table-column label="仓库名" align="center" prop="warehouseName" />
      <el-table-column label="货架" align="center" prop="shelveName" />
      <el-table-column label="货位" align="center" prop="storage" />
      <el-table-column label="类目" align="center" prop="categoryName" />
      <el-table-column label="规格" align="center" prop="specName" />
      <el-table-column label="类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_STOCK_CHANGE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="变动数量" align="center" prop="stock" />
      <el-table-column label="单价" align="center" prop="price" />
      <el-table-column label="总价" align="center" prop="totalPrice" />
      <el-table-column label="旧库存" align="center" prop="oldStock" />
      <el-table-column label="新库存" align="center" prop="newStock" />
      <el-table-column label="旧总库存" align="center" prop="oldTotalStock" />
      <el-table-column label="新总库存" align="center" prop="newTotalStock" />
      <el-table-column label="单位" align="center" prop="unit">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_UNIT" :value="scope.row.unit" />
        </template>
      </el-table-column>
      <el-table-column label="部门" align="center" prop="requestDeptName" />
      <el-table-column label="关联用户" align="center" prop="requestUserName" />
      <el-table-column label="创建人" align="center" prop="creatorName" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="事件时间"
        align="center"
        prop="eventTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="备注" align="center" prop="remark" width="150" />
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
</template>

<script setup lang="ts">
import { getStrDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as StockRecordApi from '@/api/wms/stockrecord'
import { defaultProps, handleTree } from '@/utils/tree'
import * as DeptApi from '@/api/system/dept'
import { ref, reactive, onMounted } from 'vue'

defineOptions({ name: 'WmsWarehouseStockRecord' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const deptList = ref<Tree[]>([]) // 树形结构
const queryParams = reactive({
  pageNo: 1,
  pageSize: 100,
  stock: null,
  type: null,
  oldStock: null,
  newStock: null,
  createTime: [],
  warehouseName: null,
  warehouseShelveName: null,
  warehouseStorage: null,
  productSpuName: null,
  productSkuName: null,
  requestDeptId: null,
  requestUserName: null,
  requestDepName: null,
  creatorName: null
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

onMounted(async () => {
  // 加载部门树
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockRecordApi.getStockRecordPage(queryParams)
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

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await StockRecordApi.exportStockRecord(queryParams)
    download.excel(data, '出入库记录.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
