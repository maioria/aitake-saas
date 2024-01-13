package cn.iocoder.yudao.module.wms.service.warehouse;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.wms.controller.admin.warehouse.vo.*;
import cn.iocoder.yudao.module.wms.core.vo.SelectVO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.ShelveDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 仓库 Service 接口
 *
 * @author maioria
 */
public interface WarehouseService {

    /**
     * 创建仓库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarehouse(@Valid WarehouseSaveReqVO createReqVO);

    /**
     * 更新仓库
     *
     * @param updateReqVO 更新信息
     */
    void updateWarehouse(@Valid WarehouseSaveReqVO updateReqVO);

    /**
     * 删除仓库
     *
     * @param id 编号
     */
    void deleteWarehouse(String id);

    /**
     * 获得仓库
     *
     * @param id 编号
     * @return 仓库
     */
    WarehouseDO getWarehouse(String id);

    /**
     * 获得仓库分页
     *
     * @param pageReqVO 分页查询
     * @return 仓库分页
     */
    PageResult<WarehouseDO> getWarehousePage(WarehousePageReqVO pageReqVO);

    // ==================== 子表（货架） ====================

    /**
     * 获得货架分页
     *
     * @param pageReqVO 分页查询
     * @param warehouseId 所属仓库Id
     * @return 货架分页
     */
    PageResult<ShelveDO> getShelvePage(PageParam pageReqVO, String warehouseId);

    /**
     * 创建货架
     *
     * @param shelve 创建信息
     * @return 编号
     */
    String createShelve(@Valid ShelveDO shelve);

    /**
     * 更新货架
     *
     * @param shelve 更新信息
     */
    void updateShelve(@Valid ShelveDO shelve);

    /**
     * 删除货架
     *
     * @param id 编号
     */
    void deleteShelve(String id);

	/**
	 * 获得货架
	 *
	 * @param id 编号
     * @return 货架
	 */
    ShelveDO getShelve(String id);

    List<SelectVO> getWarehouseSimpleList(String keyword);

    List<SelectVO> getShelveSimpleList(String warehouseId, String keyword);
}
