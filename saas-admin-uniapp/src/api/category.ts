import request from '@/utils/request'

// 获取库存下产品信息
export function getCategoryList(params:any) {
	return request({
		url: '/system/auth/category/spu-list',
		method: 'GET',
        params: params
	})
}

/**
 * 获取类目详情
 */
export function getCategoryDetail(params:any) {
	return request({
		url: '/zkzg/product-spu/get',
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
		url: '/zkzg/product-spu/create',
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
		url: '/zkzg/product-spu/update',
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
		url: '/zkzg/product-spu/delete',
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
		url: '/zkzg/product-spu/product-sku/create',
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
		url: '/zkzg/product-spu/product-sku/update',
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
		url: '/zkzg/product-spu/product-sku/get',
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
		url: '/zkzg/product-spu/product-sku/delete',
		method: 'DELETE',
		params: data,
		showLoading: true
	})
}