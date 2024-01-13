package cn.iocoder.yudao.module.wms.controller.admin.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 仓库新增/修改 Request VO")
@Data
public class WarehouseSaveReqVO {

    @Schema(description = "Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "仓库名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "仓库名不能为空")
    private String name;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "图片")
    private String picUrl;

    @Schema(description = "联系人")
    private String contract;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sequence;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "状态不能为空")
    private String status;

}
