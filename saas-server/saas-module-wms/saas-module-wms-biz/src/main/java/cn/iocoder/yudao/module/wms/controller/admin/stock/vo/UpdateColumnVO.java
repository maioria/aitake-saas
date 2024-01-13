package cn.iocoder.yudao.module.wms.controller.admin.stock.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateColumnVO {
    @NotEmpty
    private String stockId;
    @NotEmpty
    private String column;
    @NotEmpty
    private String value;
    private String operator;
}
