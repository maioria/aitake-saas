import request from '@/utils/request'

// 获取库存下产品信息
export function getInventoryList(params:any) {
	return request({
		url: '/system/auth/inventory/spu-list',
		method: 'GET',
        params: params,
		showLoading: true
	})
}
/**
 * 获取sku下库存明细
 */
export function getSkuStockList(params:any) {
	return request({
		url: '/system/auth/inventory/sku-stock-list',
		method: 'GET',
		params: params
	})
}

/**
 * 获取仓库selector
 */
export function getWarehouseSelector(params: any) {
	return request({
		url: '/zkzg/wms-warehouse/selector',
		method: 'GET',
		params: params
	})
}

/**
 * 获取货架selector
 */
export function getWarehouseShelveSelector(params: any) {
	return request({
		url: '/zkzg/wms-warehouse/wms-warehouse-shelve/selector',
		method: 'GET',
		params: params
	})
}

/**
 * 获取类目selector
 */
export function getCategorySelector(params: any) {
	return request({
		url: '/zkzg/product-spu/selector',
		method: 'GET',
		params: params
	})
}

/**
 * 获取sku selector
 */
export function getSkuSelector(params: any) {
	return request({
		url: '/zkzg/product-spu/product-sku/selector',
		method: 'GET',
		params: params
	})
}

/**
 * 获取用户selector
 */
export function getUserSelector(params: any) {
	return request({
		url: '/system/user/list-all-simple',
		method: 'GET',
		params: params
	})
}

/**
 * 创建库存
 */
export function createInventory(data: any) {
	return request({
		url: '/zkzg/wms-warehouse-stock/create',
		method: 'POST',
		data: data,
		showLoading: true
	})
}

/**
 * 获取库存详情信息
 */
export function getWarehouseStockDetail(params: any) {
	return request({
		url: '/zkzg/wms-warehouse-stock/get',
		method: 'GET',
		params: params
	})
}

/**
 * 更新库存
 */
export function handleWarehouseStock(data: any) {
	return request({
		url: '/zkzg/wms-warehouse-stock/update-stock',
		method: 'POST',
		data: data,
		showLoading: true
	})
}

/**
 * 删除库存
 */
export function deleteWarehouseStock({id}: any) {
	return request({
		url: '/zkzg/wms-warehouse-stock/delete',
		method: 'DELETE',
		params: {id},
		showLoading: true
	})
}

/**
 * 获取库存操作记录
 */
export function getWarehouseStockRecord(params: any) {
	return request({
		url: '/zkzg/wms-warehouse-stock-record/page',
		method: 'GET',
		params: params
	})
}