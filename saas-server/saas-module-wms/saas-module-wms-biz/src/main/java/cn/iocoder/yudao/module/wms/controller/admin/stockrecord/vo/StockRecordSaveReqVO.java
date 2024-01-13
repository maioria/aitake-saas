package cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 出入库记录新增/修改 Request VO")
@Data
public class StockRecordSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17865")
    private Integer id;

    @Schema(description = "仓库Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7687")
    @NotEmpty(message = "仓库Id不能为空")
    private String warehouseId;

    @Schema(description = "仓库名", example = "张三")
    private String warehouseName;

    @Schema(description = "货架Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22274")
    @NotEmpty(message = "货架Id不能为空")
    private String shelveId;

    @Schema(description = "货架", example = "赵六")
    private String shelveName;

    @Schema(description = "货位")
    private String storage;

    @Schema(description = "类目id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22627")
    @NotEmpty(message = "类目id不能为空")
    private String categoryId;

    @Schema(description = "类目名", example = "芋艿")
    private String categoryName;

    @Schema(description = "规格id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23956")
    @NotEmpty(message = "规格id不能为空")
    private String specId;

    @Schema(description = "规格名", example = "李四")
    private String specName;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotEmpty(message = "类型不能为空")
    private String type;

    @Schema(description = "变动数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "变动数量不能为空")
    private BigDecimal stock;

    @Schema(description = "旧库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "旧库存不能为空")
    private BigDecimal oldStock;

    @Schema(description = "新库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "新库存不能为空")
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

    @Schema(description = "用户Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8335")
    @NotEmpty(message = "用户Id不能为空")
    private String requestUserId;

    @Schema(description = "用户", example = "李四")
    private String requestUserName;

    @Schema(description = "部门Id", example = "16951")
    private String requestDeptId;

    @Schema(description = "部门", example = "王五")
    private String requestDeptName;

    @Schema(description = "创建人", example = "王五")
    private String creatorName;

    @Schema(description = "事件时间")
    private LocalDateTime eventTime;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
