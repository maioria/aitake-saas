package cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockRecordUpdateStockVO {
    private String specId;
    private String shelveId;
    private String storage;
    private BigDecimal stock;
    private String type;
    private String remark;
    private String requestDepartmentId;
    private String requestUserId;
    private String creator;
    private String tenantId;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private LocalDateTime eventTime;
}
