import request from '@/config/axios'

export interface StockRecordVO {
  id: number
  warehouseId: string
  warehouseName: string
  shelveId: string
  shelveName: string
  storage: string
  categoryId: string
  categoryName: string
  specId: string
  specName: string
  type: string
  stock: number
  oldStock: number
  newStock: number
  oldTotalStock: number
  newTotalStock: number
  unit: string
  price: number
  totalPrice: number
  requestUserId: string
  requestUserName: string
  requestDeptId: string
  requestDeptName: string
  creatorName: string
  eventTime: Date
  remark: string
}

// 查询出入库记录分页
export const getStockRecordPage = async (params) => {
  return await request.get({ url: `/wms/stock-record/page`, params })
}

// 查询出入库记录详情
export const getStockRecord = async (id: number) => {
  return await request.get({ url: `/wms/stock-record/get?id=` + id })
}

// 新增出入库记录
export const createStockRecord = async (data: StockRecordVO) => {
  return await request.post({ url: `/wms/stock-record/create`, data })
}

// 修改出入库记录
export const updateStockRecord = async (data: StockRecordVO) => {
  return await request.put({ url: `/wms/stock-record/update`, data })
}

// 删除出入库记录
export const deleteStockRecord = async (id: number) => {
  return await request.delete({ url: `/wms/stock-record/delete?id=` + id })
}

// 导出出入库记录 Excel
export const exportStockRecord = async (params) => {
  return await request.download({ url: `/wms/stock-record/export-excel`, params })
}