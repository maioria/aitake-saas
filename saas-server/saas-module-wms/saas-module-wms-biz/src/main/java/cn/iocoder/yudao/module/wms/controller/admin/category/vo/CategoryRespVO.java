package cn.iocoder.yudao.module.wms.controller.admin.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 类目 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CategoryRespVO {

    @Schema(description = "Id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Id")
    private String id;

    @Schema(description = "类目名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("类目名")
    private String name;

    @Schema(description = "别名")
    @ExcelProperty("别名")
    private String otherName;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "单位", converter = DictConvert.class)
    @DictFormat("wms_unit") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String unit;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("wms_category_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String type;

    @Schema(description = "图片")
    @ExcelProperty("图片")
    private String picUrl;

    @Schema(description = "介绍")
    @ExcelProperty("介绍")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("wms_entity_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sequence;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}