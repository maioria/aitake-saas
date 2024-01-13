package cn.iocoder.yudao.module.wms.service.stockrecord;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.stockrecord.StockRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 出入库记录 Service 接口
 *
 * @author maioria
 */
public interface StockRecordService {

    void addWmsWarehouseStockRecord(StockRecordUpdateStockVO createReqVO);

    /**
     * 创建出入库记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createStockRecord(@Valid StockRecordSaveReqVO createReqVO);

    /**
     * 更新出入库记录
     *
     * @param updateReqVO 更新信息
     */
    void updateStockRecord(@Valid StockRecordSaveReqVO updateReqVO);

    /**
     * 删除出入库记录
     *
     * @param id 编号
     */
    void deleteStockRecord(Integer id);

    /**
     * 获得出入库记录
     *
     * @param id 编号
     * @return 出入库记录
     */
    StockRecordDO getStockRecord(Integer id);

    /**
     * 获得出入库记录分页
     *
     * @param pageReqVO 分页查询
     * @return 出入库记录分页
     */
    PageResult<StockRecordDO> getStockRecordPage(StockRecordPageReqVO pageReqVO);

}
