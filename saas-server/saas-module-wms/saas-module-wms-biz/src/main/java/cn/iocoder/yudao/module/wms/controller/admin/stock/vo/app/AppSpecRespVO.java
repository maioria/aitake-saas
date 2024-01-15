package cn.iocoder.yudao.module.wms.controller.admin.stock.vo.app;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppSpecRespVO {
    private String id;
    private String name;
    private String picUrl;
    private BigDecimal stock;
    private String unit;
    private String unitName;
}
