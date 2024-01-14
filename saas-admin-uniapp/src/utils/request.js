// import store from '@/store'
import config from '@/config/env'
import { getAccessToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { toast, showConfirm, tansParams } from '@/utils/common'

let timeout = 10000
const baseUrl = config.baseUrl;
const tenantId = config.tenantId;

const request = config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  config.header = config.header || {}
  if (getAccessToken() && !isToken) {
    config.header['Authorization'] = 'Bearer ' + getAccessToken()
  }
  // 设置租户
  config.header['tenant-id'] = uni.getStorageSync('tenantId');;
  config.header['login_user_type'] = 1;
  // get请求映射params参数
  if (config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.url = url
  }
  if (config.showLoading) {
    uni.showLoading({
      title: '加载中',
      mask: true
    })
  }
  return new Promise((resolve, reject) => {
    uni.request({
        method: config.method || 'get',
        timeout: config.timeout ||  timeout,
        url: config.baseUrl || baseUrl + config.url,
        data: config.data,
        header: config.header,
        dataType: 'json'
      }).then(response => {
        console.log(response)
        const res = response
        // if (error) {
        //   toast('后端接口连接异常')
        //   reject('后端接口连接异常')
        //   return
        // }
        const code = res.data.code || 200
        const msg = errorCode[code] || res.data.msg || errorCode['default']
        if (code === 401) {
          showConfirm('登录状态已过期，您可以继续留在该页面，或者重新登录?').then(res => {
            if (res.confirm) {
              // store.dispatch('LogOut').then(res => {
                uni.reLaunch({ url: '/pages/login/index' })
              // })
            }
          })
          reject('无效的会话，或者会话已过期，请重新登录。')
        } else if (code === 500) {
          toast(msg)
          reject('500')
        } else if (code !== 200) {
          toast(msg)
          reject(code)
        }
        resolve(res.data)
      })
      .catch(error => {
        let { message } = error
        if (message === 'Network Error') {
          message = '后端接口连接异常'
        } else if (message.includes('timeout')) {
          message = '系统接口请求超时'
        } else if (message.includes('Request failed with status code')) {
          message = '系统接口' + message.substr(message.length - 3) + '异常'
        }
        toast(message)
        reject(error)
      }).finally(()=>{
        if (config.showLoading) {
          uni.hideLoading();
        }
      })
  })
}

export default request
