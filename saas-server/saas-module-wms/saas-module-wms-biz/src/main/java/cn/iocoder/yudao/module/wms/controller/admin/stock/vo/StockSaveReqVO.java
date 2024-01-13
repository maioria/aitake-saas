package cn.iocoder.yudao.module.wms.controller.admin.stock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 库存新增/修改 Request VO")
@Data
public class StockSaveReqVO {

    @Schema(description = "Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(description = "所属规格Id")
    private String specId;

    @Schema(description = "所属货架", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "所属货架不能为空")
    private String shelveId;

    @Schema(description = "所属货位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "所属货位不能为空")
    private String storage;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    private BigDecimal stock;

    /**
     * 单价
     */
    @Schema(description = "单价")
    private BigDecimal price;

    @Schema(description = "关联部门")
    private String requestDeptId;

    @Schema(description = "关联用户")
    private String requestUserId;

    private String creator;

}
