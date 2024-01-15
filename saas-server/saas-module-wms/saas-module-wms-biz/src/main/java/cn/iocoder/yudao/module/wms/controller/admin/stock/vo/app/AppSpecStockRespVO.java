package cn.iocoder.yudao.module.wms.controller.admin.stock.vo.app;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppSpecStockRespVO {
    private Integer id;
    private String categoryName;
    private String categoryId;
    private String specName;
    private String specId;
    private String warehouseId;
    private String warehouseName;
    private String shelveId;
    private String shelveName;
    private String storage;
    private BigDecimal stock;
    private String unit;
    private String unitName;
}
