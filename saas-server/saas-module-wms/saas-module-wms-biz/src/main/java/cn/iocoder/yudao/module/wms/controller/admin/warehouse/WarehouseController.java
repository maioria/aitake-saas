package cn.iocoder.yudao.module.wms.controller.admin.warehouse;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.wms.controller.admin.warehouse.vo.*;
import cn.iocoder.yudao.module.wms.core.vo.SelectVO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.ShelveDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.iocoder.yudao.module.wms.service.warehouse.WarehouseService;

@Tag(name = "管理后台 - 仓库")
@RestController
@RequestMapping("/wms/warehouse")
@Validated
public class WarehouseController {

    @Resource
    private WarehouseService warehouseService;

    @PostMapping("/create")
    @Operation(summary = "创建仓库")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:create')")
    public CommonResult<String> createWarehouse(@Valid @RequestBody WarehouseSaveReqVO createReqVO) {
        return success(warehouseService.createWarehouse(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新仓库")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:update')")
    public CommonResult<Boolean> updateWarehouse(@Valid @RequestBody WarehouseSaveReqVO updateReqVO) {
        warehouseService.updateWarehouse(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除仓库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:warehouse:delete')")
    public CommonResult<Boolean> deleteWarehouse(@RequestParam("id") String id) {
        warehouseService.deleteWarehouse(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得仓库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<WarehouseRespVO> getWarehouse(@RequestParam("id") String id) {
        WarehouseDO warehouse = warehouseService.getWarehouse(id);
        return success(BeanUtils.toBean(warehouse, WarehouseRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得仓库分页")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<PageResult<WarehouseRespVO>> getWarehousePage(@Valid WarehousePageReqVO pageReqVO) {
        PageResult<WarehouseDO> pageResult = warehouseService.getWarehousePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WarehouseRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出仓库 Excel")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:export')")
    @OperateLog(type = EXPORT)
    public void exportWarehouseExcel(@Valid WarehousePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WarehouseDO> list = warehouseService.getWarehousePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "仓库.xls", "数据", WarehouseRespVO.class,
                        BeanUtils.toBean(list, WarehouseRespVO.class));
    }

    // ==================== 子表（货架） ====================

    @GetMapping("/shelve/page")
    @Operation(summary = "获得货架分页")
    @Parameter(name = "warehouseId", description = "所属仓库Id")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<PageResult<ShelveDO>> getShelvePage(PageParam pageReqVO,
                                                                                        @RequestParam("warehouseId") String warehouseId) {
        return success(warehouseService.getShelvePage(pageReqVO, warehouseId));
    }

    @PostMapping("/shelve/create")
    @Operation(summary = "创建货架")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:create')")
    public CommonResult<String> createShelve(@Valid @RequestBody ShelveDO shelve) {
        return success(warehouseService.createShelve(shelve));
    }

    @PutMapping("/shelve/update")
    @Operation(summary = "更新货架")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:update')")
    public CommonResult<Boolean> updateShelve(@Valid @RequestBody ShelveDO shelve) {
        warehouseService.updateShelve(shelve);
        return success(true);
    }

    @DeleteMapping("/shelve/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除货架")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:delete')")
    public CommonResult<Boolean> deleteShelve(@RequestParam("id") String id) {
        warehouseService.deleteShelve(id);
        return success(true);
    }

	@GetMapping("/shelve/get")
	@Operation(summary = "获得货架")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
	public CommonResult<ShelveDO> getShelve(@RequestParam("id") String id) {
	    return success(warehouseService.getShelve(id));
	}

    /**
     * 获取仓库select
     */
    @GetMapping("/select")
    @Operation(summary = "获得仓库列表")
    public CommonResult<List<SelectVO>> getWarehouseSelect(@RequestParam(value = "keyword", required = false) String keyword) {
        List<SelectVO> list = warehouseService.getWarehouseSimpleList(keyword);
        return success(list);
    }
    /**
     * 获取仓库select
     */
    @GetMapping("/shelve/select")
    @Operation(summary = "获得货架列表")
    public CommonResult<List<SelectVO>> getShelveSelect(@RequestParam("warehouseId") String warehouseId,
        @RequestParam(value = "keyword", required = false) String keyword) {
        List<SelectVO> list = warehouseService.getShelveSimpleList(warehouseId, keyword);
        return success(list);
    }
}
