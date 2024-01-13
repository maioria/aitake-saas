package cn.iocoder.yudao.module.wms.controller.admin.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 仓库 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarehouseRespVO {

    @Schema(description = "Id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Id")
    private String id;

    @Schema(description = "仓库名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("仓库名")
    private String name;

    @Schema(description = "地址")
    @ExcelProperty("地址")
    private String address;

    @Schema(description = "图片")
    @ExcelProperty("图片")
    private String picUrl;

    @Schema(description = "联系人")
    @ExcelProperty("联系人")
    private String contract;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sequence;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("wms_entity_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String status;

}