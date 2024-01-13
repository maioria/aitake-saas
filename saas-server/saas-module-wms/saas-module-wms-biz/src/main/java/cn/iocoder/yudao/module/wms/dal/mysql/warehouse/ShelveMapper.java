package cn.iocoder.yudao.module.wms.dal.mysql.warehouse;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.ShelveDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 货架 Mapper
 *
 * @author maioria
 */
@Mapper
public interface ShelveMapper extends BaseMapperX<ShelveDO> {

    default PageResult<ShelveDO> selectPage(PageParam reqVO, String warehouseId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShelveDO>()
            .eq(ShelveDO::getWarehouseId, warehouseId)
            .orderByDesc(ShelveDO::getId));
    }

    default int deleteByWarehouseId(String warehouseId) {
        return delete(ShelveDO::getWarehouseId, warehouseId);
    }

}
