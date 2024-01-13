package cn.iocoder.yudao.module.wms.controller.admin.category.vo;

import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 类目新增/修改 Request VO")
@Data
public class CategorySaveReqVO {

    @Schema(description = "Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "类目名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "类目名不能为空")
    private String name;

    @Schema(description = "别名")
    private String otherName;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "单位不能为空")
    private String unit;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "类型不能为空")
    private String type;

    @Schema(description = "图片")
    private String picUrl;

    @Schema(description = "介绍")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "状态不能为空")
    private String status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sequence;

    @Schema(description = "规格列表")
    private List<SpecDO> specs;

}
