package cn.iocoder.yudao.module.wms.service.category;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.wms.controller.admin.category.vo.*;
import cn.iocoder.yudao.module.wms.core.vo.SelectVO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;

/**
 * 类目 Service 接口
 *
 * @author maioria
 */
public interface CategoryService {

    /**
     * 创建类目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCategory(@Valid CategorySaveReqVO createReqVO);

    /**
     * 更新类目
     *
     * @param updateReqVO 更新信息
     */
    void updateCategory(@Valid CategorySaveReqVO updateReqVO);

    /**
     * 删除类目
     *
     * @param id 编号
     */
    void deleteCategory(String id);

    /**
     * 获得类目
     *
     * @param id 编号
     * @return 类目
     */
    CategoryDO getCategory(String id);

    /**
     * 获得类目分页
     *
     * @param pageReqVO 分页查询
     * @return 类目分页
     */
    PageResult<CategoryDO> getCategoryPage(CategoryPageReqVO pageReqVO);

    // ==================== 子表（规格） ====================

    /**
     * 获得规格分页
     *
     * @param pageReqVO 分页查询
     * @param categoryId 所属类目
     * @return 规格分页
     */
    PageResult<SpecDO> getSpecPage(PageParam pageReqVO, String categoryId);

    /**
     * 创建规格
     *
     * @param spec 创建信息
     * @return 编号
     */
    String createSpec(@Valid SpecDO spec);

    /**
     * 更新规格
     *
     * @param spec 更新信息
     */
    void updateSpec(@Valid SpecDO spec);

    /**
     * 删除规格
     *
     * @param id 编号
     */
    void deleteSpec(String id);

	/**
	 * 获得规格
	 *
	 * @param id 编号
     * @return 规格
	 */
    SpecDO getSpec(String id);

    List<SelectVO> getCategorySimpleList(String keyword);

    List<SelectVO> getSpecSimpleList(String categoryId, String keyword);
}
