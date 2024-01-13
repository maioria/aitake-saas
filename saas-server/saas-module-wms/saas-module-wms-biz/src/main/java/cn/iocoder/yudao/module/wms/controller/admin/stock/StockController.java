package cn.iocoder.yudao.module.wms.controller.admin.stock;

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
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import cn.iocoder.yudao.module.wms.controller.admin.stock.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.stock.StockDO;
import cn.iocoder.yudao.module.wms.service.stock.StockService;

@Tag(name = "管理后台 - 库存")
@RestController
@RequestMapping("/wms/stock")
@Validated
public class StockController {

    @Resource
    private StockService stockService;

    @Resource
    private DictDataApi dictDataApi;

    @PostMapping("/create")
    @Operation(summary = "创建库存")
    @PreAuthorize("@ss.hasPermission('wms:stock:create')")
    public CommonResult<Integer> createStock(@Valid @RequestBody StockSaveReqVO createReqVO) {
        createReqVO.setCreator(getLoginUserId().toString());
        return success(stockService.createStock(createReqVO));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:stock:delete')")
    public CommonResult<Boolean> deleteStock(@RequestParam("id") Integer id) {
        stockService.deleteStock(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存分页")
    @PreAuthorize("@ss.hasPermission('wms:stock:query')")
    public CommonResult<PageResult<StockRespVO>> getStockPage(@Valid StockPageReqVO pageReqVO) {
        return success(stockService.getStockPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存 Excel")
    @PreAuthorize("@ss.hasPermission('wms:stock:export')")
    @OperateLog(type = EXPORT)
    public void exportStockExcel(@Valid StockPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(100000);
        List<StockRespVO> list = stockService.getStockPage(pageReqVO).getList();
        // 先查出所有的unit Set
        Set<String> unitSet = new HashSet<>();
        list.forEach(item -> unitSet.add(item.getUnit()));
        // 再查出所有的unitName Map
        Map<String, String> unitNameMap = new HashMap<>();
        unitSet.forEach(unit ->{
            DictDataRespDTO dictDataRespDTO = dictDataApi.getDictData("zkzg_sku_unit", unit);
            if (dictDataRespDTO == null) {
                unitNameMap.put(unit, "");
            } else {
                unitNameMap.put(unit, dictDataRespDTO.getLabel());
            }
        } );
        // 导出需要额外补充单位
        list.forEach(item -> item.setUnitName(unitNameMap.get(item.getUnit())));
        // 导出 Excel
        ExcelUtils.write(response, "库存.xls", "数据", StockRespVO.class, list);
    }

    @PutMapping("/execute")
    @Operation(summary = "操作库存")
    @PreAuthorize("@ss.hasPermission('wms:stock:update')")
    @OperateLog(type = UPDATE)
    public CommonResult<Boolean> updateStock(@Valid @RequestBody StockExecuteVO stockExecuteVO) {
        stockExecuteVO.setCreator(getLoginUserId().toString());
        stockService.updateStock(stockExecuteVO);
        return success(true);
    }

    /**
     * 创建类目规格并入库
     */
    @PostMapping("/create-and-stock")
    @Operation(summary = "创建类目规格并入库")
    @PreAuthorize("@ss.hasPermission('wms:stock:create')")
    public CommonResult<Integer> createAndStock(@Valid @RequestBody CreateAndStockReqVO createReqVO) {
        createReqVO.setCreator(getLoginUserId().toString());
        if (createReqVO.getRequestUserId() == null) {
            createReqVO.setRequestUserId(createReqVO.getCreator());
        }
        return success(stockService.createAndStock(createReqVO));
    }

    /**
     * 列表中单个属性更新
     */
    @PostMapping("/update-column")
    @Operation(summary = "列表中单个属性更新")
    @PreAuthorize("@ss.hasPermission('zkzg:wms-warehouse-stock:update')")
    @OperateLog(type = UPDATE)
    public CommonResult<Boolean> updateColumn(@Valid @RequestBody UpdateColumnVO updateReqVO) {
        updateReqVO.setOperator(getLoginUserId().toString());
        stockService.updateColumn(updateReqVO);
        return success(true);
    }
}
