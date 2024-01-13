package cn.iocoder.yudao.module.wms.controller.admin.stock.vo;

import lombok.*;

import java.util.*;

import io.swagger.v3.oas.annotations.media.Schema;

import cn.iocoder.yudao.framework.common.pojo.PageParam;

import java.math.BigDecimal;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库存分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockPageReqVO extends PageParam {

    private String categoryId;

    @Schema(description = "所属规格Id", example = "29700")
    private String specId;

    private String warehouseId;

    @Schema(description = "所属货架", example = "28149")
    private String shelveId;

    @Schema(description = "所属货位")
    private String storage;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] updateTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
