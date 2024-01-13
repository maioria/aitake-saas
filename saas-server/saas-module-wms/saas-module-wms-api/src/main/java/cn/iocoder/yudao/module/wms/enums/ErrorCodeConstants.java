package cn.iocoder.yudao.module.wms.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public class ErrorCodeConstants {
    public static ErrorCode CATEGORY_NOT_EXISTS = new ErrorCode(40001, "类目不存在");
    public static ErrorCode SPEC_NOT_EXISTS = new ErrorCode(40002, "规格不存在");
    public static ErrorCode WAREHOUSE_NOT_EXISTS = new ErrorCode(40003, "仓库不存在");
    public static ErrorCode SHELVE_NOT_EXISTS = new ErrorCode(40004, "货架不存在");
    public static ErrorCode STOCK_NOT_EXISTS = new ErrorCode(40005, "库存不存在");
    public static ErrorCode STOCK_RECORD_NOT_EXISTS = new ErrorCode(40006, "出入库记录不存在");
    public static ErrorCode WMS_WAREHOUSE_STORAGE_EXISTS = new ErrorCode(40007, "规格在货位上已有库存");
    public static ErrorCode WMS_WAREHOUSE_STOCK_NOT_ENOUGH = new ErrorCode(40008, "库存不足");
    public static ErrorCode WMS_WAREHOUSE_STOCK_RECORD_TYPE_ERROR = new ErrorCode(40009, "出入库记录类型错误");
}
