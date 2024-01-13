import request from '@/config/axios'

export interface WarehouseVO {
  id: string
  name: string
  address: string
  picUrl: string
  contract: string
  sequence: number
  status: string
}

// 查询仓库分页
export const getWarehousePage = async (params) => {
  return await request.get({ url: `/wms/warehouse/page`, params })
}

// 查询仓库详情
export const getWarehouse = async (id: number) => {
  return await request.get({ url: `/wms/warehouse/get?id=` + id })
}

// 新增仓库
export const createWarehouse = async (data: WarehouseVO) => {
  return await request.post({ url: `/wms/warehouse/create`, data })
}

// 修改仓库
export const updateWarehouse = async (data: WarehouseVO) => {
  return await request.put({ url: `/wms/warehouse/update`, data })
}

// 删除仓库
export const deleteWarehouse = async (id: number) => {
  return await request.delete({ url: `/wms/warehouse/delete?id=` + id })
}

// 导出仓库 Excel
export const exportWarehouse = async (params) => {
  return await request.download({ url: `/wms/warehouse/export-excel`, params })
}

// ==================== 子表（货架） ====================

// 获得货架分页
export const getShelvePage = async (params) => {
  return await request.get({ url: `/wms/warehouse/shelve/page`, params })
}
// 新增货架
export const createShelve = async (data) => {
  return await request.post({ url: `/wms/warehouse/shelve/create`, data })
}

// 修改货架
export const updateShelve = async (data) => {
  return await request.put({ url: `/wms/warehouse/shelve/update`, data })
}

// 删除货架
export const deleteShelve = async (id: number) => {
  return await request.delete({ url: `/wms/warehouse/shelve/delete?id=` + id })
}

// 获得货架
export const getShelve = async (id: number) => {
  return await request.get({ url: `/wms/warehouse/shelve/get?id=` + id })
}

// 获取仓库select
export const getWarehouseSelect = async (params) => {
  return await request.get({ url: `/wms/warehouse/select`, params })
}

// 获取货架select
export const getShelveSelect = async (params) => {
  return await request.get({ url: `/wms/warehouse/shelve/select`, params })
}
