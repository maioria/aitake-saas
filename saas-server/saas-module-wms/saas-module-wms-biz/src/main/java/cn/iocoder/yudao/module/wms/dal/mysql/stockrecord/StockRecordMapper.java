package cn.iocoder.yudao.module.wms.dal.mysql.stockrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.stockrecord.StockRecordDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo.*;

/**
 * 出入库记录 Mapper
 *
 * @author maioria
 */
@Mapper
public interface StockRecordMapper extends BaseMapperX<StockRecordDO> {

    default PageResult<StockRecordDO> selectPage(StockRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StockRecordDO>()
                .eqIfPresent(StockRecordDO::getWarehouseId, reqVO.getWarehouseId())
                .likeIfPresent(StockRecordDO::getWarehouseName, reqVO.getWarehouseName())
                .eqIfPresent(StockRecordDO::getShelveId, reqVO.getShelveId())
                .likeIfPresent(StockRecordDO::getShelveName, reqVO.getShelveName())
                .eqIfPresent(StockRecordDO::getStorage, reqVO.getStorage())
                .eqIfPresent(StockRecordDO::getCategoryId, reqVO.getCategoryId())
                .likeIfPresent(StockRecordDO::getCategoryName, reqVO.getCategoryName())
                .eqIfPresent(StockRecordDO::getSpecId, reqVO.getSpecId())
                .likeIfPresent(StockRecordDO::getSpecName, reqVO.getSpecName())
                .eqIfPresent(StockRecordDO::getType, reqVO.getType())
                .eqIfPresent(StockRecordDO::getStock, reqVO.getStock())
                .eqIfPresent(StockRecordDO::getOldStock, reqVO.getOldStock())
                .eqIfPresent(StockRecordDO::getNewStock, reqVO.getNewStock())
                .eqIfPresent(StockRecordDO::getOldTotalStock, reqVO.getOldTotalStock())
                .eqIfPresent(StockRecordDO::getNewTotalStock, reqVO.getNewTotalStock())
                .eqIfPresent(StockRecordDO::getUnit, reqVO.getUnit())
                .eqIfPresent(StockRecordDO::getPrice, reqVO.getPrice())
                .eqIfPresent(StockRecordDO::getTotalPrice, reqVO.getTotalPrice())
                .eqIfPresent(StockRecordDO::getRequestUserId, reqVO.getRequestUserId())
                .likeIfPresent(StockRecordDO::getRequestUserName, reqVO.getRequestUserName())
                .eqIfPresent(StockRecordDO::getRequestDeptId, reqVO.getRequestDeptId())
                .likeIfPresent(StockRecordDO::getRequestDeptName, reqVO.getRequestDeptName())
                .likeIfPresent(StockRecordDO::getCreatorName, reqVO.getCreatorName())
                .betweenIfPresent(StockRecordDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(StockRecordDO::getEventTime, reqVO.getEventTime())
                .eqIfPresent(StockRecordDO::getRemark, reqVO.getRemark())
                .orderByDesc(StockRecordDO::getId));
    }

}