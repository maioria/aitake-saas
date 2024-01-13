package cn.iocoder.yudao.module.wms.controller.admin.category;

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

import cn.iocoder.yudao.module.wms.controller.admin.category.vo.*;
import cn.iocoder.yudao.module.wms.core.vo.SelectVO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;
import cn.iocoder.yudao.module.wms.service.category.CategoryService;

@Tag(name = "管理后台 - 类目")
@RestController
@RequestMapping("/wms/category")
@Validated
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建类目")
    @PreAuthorize("@ss.hasPermission('wms:category:create')")
    public CommonResult<String> createCategory(@Valid @RequestBody CategorySaveReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新类目")
    @PreAuthorize("@ss.hasPermission('wms:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody CategorySaveReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除类目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:category:delete')")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") String id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得类目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:category:query')")
    public CommonResult<CategoryRespVO> getCategory(@RequestParam("id") String id) {
        CategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, CategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得类目分页")
    @PreAuthorize("@ss.hasPermission('wms:category:query')")
    public CommonResult<PageResult<CategoryRespVO>> getCategoryPage(@Valid CategoryPageReqVO pageReqVO) {
        PageResult<CategoryDO> pageResult = categoryService.getCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CategoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出类目 Excel")
    @PreAuthorize("@ss.hasPermission('wms:category:export')")
    @OperateLog(type = EXPORT)
    public void exportCategoryExcel(@Valid CategoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CategoryDO> list = categoryService.getCategoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "类目.xls", "数据", CategoryRespVO.class,
                        BeanUtils.toBean(list, CategoryRespVO.class));
    }

    // ==================== 子表（规格） ====================

    @GetMapping("/spec/page")
    @Operation(summary = "获得规格分页")
    @Parameter(name = "categoryId", description = "所属类目")
    @PreAuthorize("@ss.hasPermission('wms:category:query')")
    public CommonResult<PageResult<SpecDO>> getSpecPage(PageParam pageReqVO,
                                                                                        @RequestParam("categoryId") String categoryId) {
        return success(categoryService.getSpecPage(pageReqVO, categoryId));
    }

    @PostMapping("/spec/create")
    @Operation(summary = "创建规格")
    @PreAuthorize("@ss.hasPermission('wms:category:create')")
    public CommonResult<String> createSpec(@Valid @RequestBody SpecDO spec) {
        return success(categoryService.createSpec(spec));
    }

    @PutMapping("/spec/update")
    @Operation(summary = "更新规格")
    @PreAuthorize("@ss.hasPermission('wms:category:update')")
    public CommonResult<Boolean> updateSpec(@Valid @RequestBody SpecDO spec) {
        categoryService.updateSpec(spec);
        return success(true);
    }

    @DeleteMapping("/spec/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除规格")
    @PreAuthorize("@ss.hasPermission('wms:category:delete')")
    public CommonResult<Boolean> deleteSpec(@RequestParam("id") String id) {
        categoryService.deleteSpec(id);
        return success(true);
    }

	@GetMapping("/spec/get")
	@Operation(summary = "获得规格")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:category:query')")
	public CommonResult<SpecDO> getSpec(@RequestParam("id") String id) {
	    return success(categoryService.getSpec(id));
	}

    /**
     * 筛选select
     */
    @GetMapping("/select")
    @Operation(summary = "获得类目列表")
    @PreAuthorize("@ss.hasPermission('wms:category:query')")
    public CommonResult<List<SelectVO>> getCategoryList(@RequestParam(value = "keyword", required = false) String keyword) {
        List<SelectVO> list = categoryService.getCategorySimpleList(keyword);
        return success(list);
    }

    /**
     * 规格select
     */
    @GetMapping("/spec/select")
    @Operation(summary = "获得规格列表")
    public CommonResult<List<SelectVO>> getSpecList(
        @RequestParam("categoryId") String categoryId,
        @RequestParam(value = "keyword", required = false) String keyword) {
        List<SelectVO> list = categoryService.getSpecSimpleList(categoryId, keyword);
        return success(list);
    }
}
