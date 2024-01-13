package cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 出入库记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockRecordRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17865")
    @ExcelProperty("主键")
    private Integer id;

    @Schema(description = "仓库Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7687")
    @ExcelProperty("仓库Id")
    private String warehouseId;

    @Schema(description = "仓库名", example = "张三")
    @ExcelProperty("仓库名")
    private String warehouseName;

    @Schema(description = "货架Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22274")
    @ExcelProperty("货架Id")
    private String shelveId;

    @Schema(description = "货架", example = "赵六")
    @ExcelProperty("货架")
    private String shelveName;

    @Schema(description = "货位")
    @ExcelProperty("货位")
    private String storage;

    @Schema(description = "类目id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22627")
    @ExcelProperty("类目id")
    private String categoryId;

    @Schema(description = "类目名", example = "芋艿")
    @ExcelProperty("类目名")
    private String categoryName;

    @Schema(description = "规格id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23956")
    @ExcelProperty("规格id")
    private String specId;

    @Schema(description = "规格名", example = "李四")
    @ExcelProperty("规格名")
    private String specName;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("类型")
    private String type;

    @Schema(description = "变动数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("变动数量")
    private BigDecimal stock;

    @Schema(description = "旧库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("旧库存")
    private BigDecimal oldStock;

    @Schema(description = "新库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("新库存")
    private BigDecimal newStock;

    @Schema(description = "旧总库存")
    @ExcelProperty("旧总库存")
    private BigDecimal oldTotalStock;

    @Schema(description = "新总库存")
    @ExcelProperty("新总库存")
    private BigDecimal newTotalStock;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "单价", example = "17478")
    @ExcelProperty("单价")
    private BigDecimal price;

    @Schema(description = "总价", example = "20726")
    @ExcelProperty("总价")
    private BigDecimal totalPrice;

    @Schema(description = "用户Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8335")
    @ExcelProperty("用户Id")
    private String requestUserId;

    @Schema(description = "用户", example = "李四")
    @ExcelProperty("用户")
    private String requestUserName;

    @Schema(description = "部门Id", example = "16951")
    @ExcelProperty("部门Id")
    private String requestDeptId;

    @Schema(description = "部门", example = "王五")
    @ExcelProperty("部门")
    private String requestDeptName;

    @Schema(description = "创建人", example = "王五")
    @ExcelProperty("创建人")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "事件时间")
    @ExcelProperty("事件时间")
    private LocalDateTime eventTime;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

}