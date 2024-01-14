import request from '@/utils/request'

// 获取库存下产品信息
export function getDictData(params:any) {
	return request({
		url: '/system/dict-data/type',
		method: 'GET',
        params: params
	})
}

