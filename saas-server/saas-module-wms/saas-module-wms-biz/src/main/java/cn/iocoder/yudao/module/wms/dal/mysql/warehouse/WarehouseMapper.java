package cn.iocoder.yudao.module.wms.dal.mysql.warehouse;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.WarehouseDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wms.controller.admin.warehouse.vo.*;

/**
 * 仓库 Mapper
 *
 * @author maioria
 */
@Mapper
public interface WarehouseMapper extends BaseMapperX<WarehouseDO> {

    default PageResult<WarehouseDO> selectPage(WarehousePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarehouseDO>()
                .likeIfPresent(WarehouseDO::getName, reqVO.getName())
                .eqIfPresent(WarehouseDO::getAddress, reqVO.getAddress())
                .eqIfPresent(WarehouseDO::getPicUrl, reqVO.getPicUrl())
                .likeIfPresent(WarehouseDO::getContract, reqVO.getContract())
                .betweenIfPresent(WarehouseDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(WarehouseDO::getSequence, reqVO.getSequence())
                .eqIfPresent(WarehouseDO::getStatus, reqVO.getStatus())
                .orderByDesc(WarehouseDO::getId));
    }

}