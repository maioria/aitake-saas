package cn.iocoder.yudao.module.wms.controller.admin.stockrecord;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.stockrecord.StockRecordDO;
import cn.iocoder.yudao.module.wms.service.stockrecord.StockRecordService;

@Tag(name = "管理后台 - 出入库记录")
@RestController
@RequestMapping("/wms/stock-record")
@Validated
public class StockRecordController {

    @Resource
    private StockRecordService stockRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建出入库记录")
    @PreAuthorize("@ss.hasPermission('wms:stock-record:create')")
    public CommonResult<Integer> createStockRecord(@Valid @RequestBody StockRecordSaveReqVO createReqVO) {
        return success(stockRecordService.createStockRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新出入库记录")
    @PreAuthorize("@ss.hasPermission('wms:stock-record:update')")
    public CommonResult<Boolean> updateStockRecord(@Valid @RequestBody StockRecordSaveReqVO updateReqVO) {
        stockRecordService.updateStockRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除出入库记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:stock-record:delete')")
    public CommonResult<Boolean> deleteStockRecord(@RequestParam("id") Integer id) {
        stockRecordService.deleteStockRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得出入库记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:stock-record:query')")
    public CommonResult<StockRecordRespVO> getStockRecord(@RequestParam("id") Integer id) {
        StockRecordDO stockRecord = stockRecordService.getStockRecord(id);
        return success(BeanUtils.toBean(stockRecord, StockRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得出入库记录分页")
    @PreAuthorize("@ss.hasPermission('wms:stock-record:query')")
    public CommonResult<PageResult<StockRecordRespVO>> getStockRecordPage(@Valid StockRecordPageReqVO pageReqVO) {
        PageResult<StockRecordDO> pageResult = stockRecordService.getStockRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, StockRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出出入库记录 Excel")
    @PreAuthorize("@ss.hasPermission('wms:stock-record:export')")
    @OperateLog(type = EXPORT)
    public void exportStockRecordExcel(@Valid StockRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<StockRecordDO> list = stockRecordService.getStockRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "出入库记录.xls", "数据", StockRecordRespVO.class,
                        BeanUtils.toBean(list, StockRecordRespVO.class));
    }

}
