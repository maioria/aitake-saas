package cn.iocoder.yudao.module.wms.controller.admin.stock.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockExecuteVO {
    private Integer stockId;
    private BigDecimal stock;
    private String type;
    private String remark;
    private String requestUserId;
    private String requestDepartmentId;
    private String creator;
    private BigDecimal price;
    private LocalDateTime eventTime;
}
