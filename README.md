# <img src="https://aitake-saas-qiniu.sciotech.cn/b9b4dba4e0bee4975c075b82280cb7ff276cad8155ae58c7ee3bd238c674eee3.png" width="60px" align="center" alt="ChatGPT-TalkieAI icon"> 爱塔可仓库管理系统

## 简介
[爱塔可仓库管理系统](https://github.com/maioria/aitake-saas) 支持多租户，操作简单，基于springboot，vue3

## 在线预览

- [预览地址](https://aitake-saas-admin.sciotech.cn/)


## 后端
- 使用java开发,springboot框架，基于ruoyi-vue-pro，数据库使用mysql，缓存使用redis，前后端分离，支持多租户
## 前端
- vue3，同样是基于ruoyi-vue-pro的vue3前端框架
## 手机端
- 使用uniapp，基于vue3

## 项目示例图
![](https://aitake-saas-qiniu.sciotech.cn/demo/login.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/home-black.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/home-white.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/category-edit.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/warehouse.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/stock-add.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/stock-outbound.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/stock-record.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/app-login.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/app-stock.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/app-category-create.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/app-category-execute.jpg)
![](https://aitake-saas-qiniu.sciotech.cn/demo/app-spec-execute.jpg)

## 本地启动
```bash
# 数据库，仓库数据库后执行saas-server下sql目录中的saas.sql文件（包含ruoyi的基础sql与爱塔可的建表sql）

# saas-server
mvn clean package -Dmaven.test.skip=true

# saas-admin-ui

# 1. 安装依赖
npm install

# 2. 本地启动
npm run dev

# saas-admin-uniapp使用HBuilder直接web或者小程序运行 (准备中...)

```

## nginx配置(Web)
```bash
# uniapp可以直接跨域请求服务端地址，也可通过nginx来配置反向代理
server {
        listen       80;
        listen       [::]:80;
        server_name  {server_name};
        rewrite ^(.*) https://$server_name$1 permanent;
      }

server {
        listen       443 ssl http2;
        listen       [::]:443 ssl http2;
        server_name  {server_name};
        root         {前端编译完后的路径};
        ssl_certificate "{crt}";
        ssl_certificate_key "{key}";
        ssl_session_cache shared:SSL:1m;
        ssl_session_timeout  10m;
        ssl_ciphers HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers on;

        # Load configuration files for the default server block.
        location ^~ /api/ {
           proxy_pass http://localhost:48080/admin-api/;
           proxy_set_header Host $http_host;
           proxy_connect_timeout 15s;
           proxy_send_timeout 300s;
           proxy_read_timeout 300s;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
        location / {
           try_files $uri $uri/ /index.html;
        }
    }
```

## 贡献
如果您有任何建议或意见，欢迎提出 [Issues](https://github.com/maioria/aitake-saas/issues) 或 [ Pull Request](https://github.com/maioria/aitake-saas/pulls)。
