package cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出入库记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockRecordPageReqVO extends PageParam {

    @Schema(description = "仓库Id", example = "7687")
    private String warehouseId;

    @Schema(description = "仓库名", example = "张三")
    private String warehouseName;

    @Schema(description = "货架Id", example = "22274")
    private String shelveId;

    @Schema(description = "货架", example = "赵六")
    private String shelveName;

    @Schema(description = "货位")
    private String storage;

    @Schema(description = "类目id", example = "22627")
    private String categoryId;

    @Schema(description = "类目名", example = "芋艿")
    private String categoryName;

    @Schema(description = "规格id", example = "23956")
    private String specId;

    @Schema(description = "规格名", example = "李四")
    private String specName;

    @Schema(description = "类型", example = "2")
    private String type;

    @Schema(description = "变动数量")
    private BigDecimal stock;

    @Schema(description = "旧库存")
    private BigDecimal oldStock;

    @Schema(description = "新库存")
    private BigDecimal newStock;

    @Schema(description = "旧总库存")
    private BigDecimal oldTotalStock;

    @Schema(description = "新总库存")
    private BigDecimal newTotalStock;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "单价", example = "17478")
    private BigDecimal price;

    @Schema(description = "总价", example = "20726")
    private BigDecimal totalPrice;

    @Schema(description = "用户Id", example = "8335")
    private String requestUserId;

    @Schema(description = "用户", example = "李四")
    private String requestUserName;

    @Schema(description = "部门Id", example = "16951")
    private String requestDeptId;

    @Schema(description = "部门", example = "王五")
    private String requestDeptName;

    @Schema(description = "创建人", example = "王五")
    private String creatorName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "事件时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] eventTime;

    @Schema(description = "备注", example = "随便")
    private String remark;

}