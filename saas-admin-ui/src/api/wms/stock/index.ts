import request from '@/config/axios'

export interface StockVO {
  id: number
  specId: string
  shelveId: string
  storage: string
  stock: number
}

// 查询库存分页
export const getStockPage = async (params) => {
  return await request.get({ url: `/wms/stock/page`, params })
}

// 查询库存详情
export const getStock = async (id: number) => {
  return await request.get({ url: `/wms/stock/get?id=` + id })
}

// 新增库存
export const createStock = async (data: StockVO) => {
  return await request.post({ url: `/wms/stock/create`, data })
}

// 修改库存
export const updateStock = async (data: StockVO) => {
  return await request.put({ url: `/wms/stock/update`, data })
}

// 删除库存
export const deleteStock = async (id: number) => {
  return await request.delete({ url: `/wms/stock/delete?id=` + id })
}

// 导出库存 Excel
export const exportStock = async (params) => {
  return await request.download({ url: `/wms/stock/export-excel`, params })
}

// 操作库存
export const executeStock = async (data: StockVO) => {
  return await request.put({ url: `/wms/stock/execute`, data })
}

// 快捷录入
export const createAndStock = async (data: any) => {
  return await request.post({ url: `/wms/stock/create-and-stock`, data })
}

export const updateSingleColumn = async (data) => {
  return await request.post({ url: `/wms/stock/update-column`, data })
}
