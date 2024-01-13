create table wms_category
(
    id          varchar(64)                           not null comment 'Id'
        primary key,
    tenant_id   bigint                                not null comment '租户',
    name        varchar(64)                           not null comment '类目名',
    other_name  varchar(64)                           null comment '别名',
    unit        varchar(64) default 'NORMAL'          not null comment '单位',
    type        varchar(64) default 'NORMAL'          not null comment '类型',
    pic_url     varchar(256)                          null comment '图片',
    description varchar(512)                          null comment '介绍',
    status      varchar(64) default 'NORMAL'          not null comment '状态',
    sequence    int         default 1                 not null comment '排序',
    creator     varchar(64)                           not null comment '创建人',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)                           not null comment '更新人',
    update_time datetime    default CURRENT_TIMESTAMP not null comment '更新时间',
    deleted     int         default 0                 not null comment '删除'
)
    comment '类目';

create table wms_shelve
(
    id           varchar(80)                  not null comment '主键'
        primary key,
    warehouse_id varchar(80)                  not null comment '所属仓库Id',
    name         varchar(80)                  not null comment '货架号',
    address      varchar(512)                 null comment '位置',
    creator      varchar(80)                  null comment '创建人',
    create_time  datetime                     null comment '创建时间',
    updater      varchar(80)                  null comment '更新人',
    update_time  datetime                     null comment '更新时间',
    status       varchar(64) default 'NORMAL' null comment '状态',
    deleted      int         default 0        null comment '删除',
    tenant_id    bigint                       not null comment '租户',
    sequence     int         default 1        null comment '排序'
)
    comment '货架';

create table wms_spec
(
    id          varchar(64)                           not null comment 'Id'
        primary key,
    tenant_id   bigint                                not null comment '租户',
    category_id varchar(64)                           not null comment '所属类目',
    name        varchar(64)                           not null comment '规格名',
    pic_url     varchar(512)                          null comment '图片',
    other_name  varchar(64)                           null comment '别名',
    properties  json                                  null comment '自定义属性',
    weight      decimal(22, 4)                        null comment '重量',
    volume      decimal(22, 4)                        null comment '体积',
    unit        varchar(64) default 'NORMAL'          not null comment '单位',
    status      varchar(64) default 'NORMAL'          not null comment '状态',
    stock       decimal(22, 4)                        null comment '库存',
    sequence    int         default 1                 not null comment '序列',
    creator     varchar(64)                           not null comment '创建人',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)                           not null comment '更新人',
    update_time datetime                              null comment '更新时间',
    deleted     int         default 0                 not null comment '删除',
    price       decimal(22, 4)                        null comment '单价'
)
    comment '规格';

create table wms_stock
(
    id          int auto_increment comment 'Id'
        primary key,
    tenant_id   bigint         default 0                 not null comment '租户编号',
    spec_id     varchar(80)                              null comment '所属规格Id',
    shelve_id   varchar(80)                              not null comment '所属货架',
    storage     varchar(80)                              not null comment '所属货位',
    stock       decimal(22, 4) default 0.0000            not null comment '数量',
    creator     varchar(64)                              null comment '创建人',
    create_time datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    updater     double(64, 0)                            null comment '更新人',
    update_time datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     int            default 0                 null comment '删除'
)
    comment '库存';

create table wms_stock_record
(
    id                int auto_increment comment '主键'
        primary key,
    tenant_id         bigint   default 0                 null comment '租户编号',
    warehouse_id      varchar(80)                        not null comment '仓库Id',
    warehouse_name    varchar(80)                        null comment '仓库名',
    shelve_id         varchar(80)                        not null comment '货架Id',
    shelve_name       varchar(80)                        null comment '货架',
    storage           varchar(80)                        null comment '货位',
    category_id       varchar(80)                        not null comment '类目id',
    category_name     varchar(80)                        null comment '类目名',
    spec_id           varchar(80)                        not null comment '规格id',
    spec_name         varchar(80)                        null comment '规格名',
    type              varchar(80)                        not null comment '类型',
    stock             decimal(20, 4)                     not null comment '变动数量',
    old_stock         decimal(20, 4)                     not null comment '旧库存',
    new_stock         decimal(20, 4)                     not null comment '新库存',
    old_total_stock   decimal(20, 4)                     null comment '旧总库存',
    new_total_stock   decimal(20, 4)                     null comment '新总库存',
    unit              varchar(64)                        null comment '单位',
    price             decimal(16, 2)                     null comment '单价',
    total_price       decimal(16, 2)                     null comment '总价',
    request_user_id   varchar(80)                        null comment '用户Id',
    request_user_name varchar(80)                        null comment '用户',
    request_dept_id   varchar(80)                        null comment '部门Id',
    request_dept_name varchar(80)                        null comment '部门',
    creator           varchar(64)                        not null comment '创建人Id',
    creator_name      varchar(80)                        null comment '创建人',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    event_time        datetime default CURRENT_TIMESTAMP null comment '事件时间',
    remark            varchar(512)                       null comment '备注',
    deleted           int      default 0                 null comment '删除'
)
    comment '出入库记录';

create table wms_warehouse
(
    id          varchar(64)                           not null comment 'Id'
        primary key,
    tenant_id   bigint                                null comment '租户Id',
    name        varchar(64)                           not null comment '仓库名',
    address     varchar(256)                          null comment '地址',
    pic_url     varchar(512)                          null comment '图片',
    contract    varchar(256)                          null comment '联系人',
    sequence    int         default 1                 not null comment '排序',
    status      varchar(64) default 'NORMAL'          not null comment '状态',
    creator     varchar(64)                           not null comment '创建人Id',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)                           not null comment '更新人',
    update_time datetime    default CURRENT_TIMESTAMP not null comment '更新时间',
    deleted     int         default 0                 not null comment '删除'
)
    comment '仓库';

