import request from '@/utils/request'

// 上传图片
export function uploadImage(data:any) {
	return request({
		url: '/system/file/upload',
		method: 'POST',
		data: data
	})
}

