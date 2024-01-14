import request from "@/utils/request";

// 使用租户名，获得租户编号
export function getTenantIdByName(name: string) {
  return request({
    method: "GET",
    url: "/system/tenant/get-id-by-name?name=" + name,
  });
}

// 登录方法
export function login(tenantName, username, password, captchaVerification) {
  const data = {
    tenantName,
    username,
    password,
    captchaVerification,
  };
  return request({
    url: "/system/auth/login",
    headers: {
      isToken: false,
    },
    method: "POST",
    data: data,
  });
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: "/system/auth/get-permission-info",
    method: "GET",
  });
}

// 退出方法
export function logout() {
  return request({
    url: "/system/auth/logout",
    method: "POST",
  });
}
