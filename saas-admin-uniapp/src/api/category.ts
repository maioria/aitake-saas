import request from '@/utils/request'

/**
 * 获取类目详情
 */
export function getCategoryDetail(params:any) {
	return request({
		url: '/wms/category/get',
		method: 'GET',
		params: params,
		showLoading: true
	})
}

/**
 * 创建类目
 */
export function createCategory(data:any) {
	return request({
		url: '/wms/category/create',
		method: 'POST',
		data: data,
		showLoading: true
	})
}

/**
 * 编辑类目
 */
export function updateCategory(data:any) {
	return request({
		url: '/wms/category/update',
		method: 'PUT',
		data: data,
		showLoading: true
	})
}

/**
 * 删除类目
 */
export function deleteCategory(data:any) {
	return request({
		url: '/wms/category/delete',
		method: 'DELETE',
		params: data,
		showLoading: true
	})
}

/**
 * 创建规格
 */
export function createSku(data:any) {
	return request({
		url: '/wms/category/spec/create',
		method: 'POST',
		data: data,
		showLoading: true
	})
}

/**
 * 编辑规格
 */
export function updateSku(data:any) {
	return request({
		url: '/wms/category/spec/update',
		method: 'PUT',
		data: data,
		showLoading: true
	})
}

/**
 * 获取规格详情
 */
export function getSkuDetail(params:any) {
	return request({
		url: '/wms/category/spec/get',
		method: 'GET',
		params: params,
		showLoading: true
	})
}

/**
 * 删除规格
 */
export function deleteSku(data:any) {
	return request({
		url: '/wms/category/spec/delete',
		method: 'DELETE',
		params: data,
		showLoading: true
	})
}