package cn.iocoder.yudao.module.wms.controller.admin.stock.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;

import java.util.*;
import java.util.*;
import java.math.BigDecimal;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 库存 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockRespVO {

    @Schema(description = "Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20387")
    @ExcelProperty("Id")
    private Integer id;

    private String categoryId;

    private String categoryName;

    @Schema(description = "所属规格Id", example = "29700")
    @ExcelProperty("所属规格Id")
    private String specId;

    private String specName;

    @Schema(description = "所属货架", requiredMode = Schema.RequiredMode.REQUIRED, example = "28149")
    private String shelveId;

    @ExcelProperty("仓库")
    private String warehouseName;

    @ExcelProperty("货架")
    private String shelveName;

    private String warehouseId;

    @Schema(description = "所属货位", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("货位")
    private String storage;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数量")
    private BigDecimal stock;

    private String unit;

    @ExcelProperty("单位")
    private String unitName;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    private String picUrl;
}
