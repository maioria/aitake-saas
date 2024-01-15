package cn.iocoder.yudao.module.wms.controller.admin.stock.vo.app;

import lombok.Data;

@Data
public class AppStockPageReqVO {
    private String keyword;

    private Integer pageNo;

    private Integer pageSize;
}
