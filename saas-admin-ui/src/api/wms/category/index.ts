import request from '@/config/axios'

export interface CategoryVO {
  id: string
  name: string
  otherName: string
  unit: string
  type: string
  picUrl: string
  description: string
  status: string
  sequence: number
}

// 查询类目分页
export const getCategoryPage = async (params) => {
  return await request.get({ url: `/wms/category/page`, params })
}

// 查询类目详情
export const getCategory = async (id: number) => {
  return await request.get({ url: `/wms/category/get?id=` + id })
}

// 新增类目
export const createCategory = async (data: CategoryVO) => {
  return await request.post({ url: `/wms/category/create`, data })
}

// 修改类目
export const updateCategory = async (data: CategoryVO) => {
  return await request.put({ url: `/wms/category/update`, data })
}

// 删除类目
export const deleteCategory = async (id: number) => {
  return await request.delete({ url: `/wms/category/delete?id=` + id })
}

// 导出类目 Excel
export const exportCategory = async (params) => {
  return await request.download({ url: `/wms/category/export-excel`, params })
}

// ==================== 子表（规格） ====================

// 获得规格分页
export const getSpecPage = async (params) => {
  return await request.get({ url: `/wms/category/spec/page`, params })
}
// 新增规格
export const createSpec = async (data) => {
  return await request.post({ url: `/wms/category/spec/create`, data })
}

// 修改规格
export const updateSpec = async (data) => {
  return await request.put({ url: `/wms/category/spec/update`, data })
}

// 删除规格
export const deleteSpec = async (id: number) => {
  return await request.delete({ url: `/wms/category/spec/delete?id=` + id })
}

// 获得规格
export const getSpec = async (id: number) => {
  return await request.get({ url: `/wms/category/spec/get?id=` + id })
}

// 获取类目筛选框
export const getCategorySelect = async (params) => {
  return await request.get({ url: `/wms/category/select`, params })
}

// 获取筛选框
export const getSpecSelect = async (params) => {
  return await request.get({ url: `/wms/category/spec/select`, params })
}
