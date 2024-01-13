package cn.iocoder.yudao.module.wms.dal.mysql.stock;

import java.math.BigDecimal;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.stock.StockDO;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.iocoder.yudao.module.wms.controller.admin.stock.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.ShelveDO;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * 库存 Mapper
 *
 * @author maioria
 */
@Mapper
public interface StockMapper extends BaseMapperX<StockDO> {

    default PageResult<StockDO> selectPage(StockPageReqVO reqVO) {
        MPJLambdaWrapper<StockDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(StockDO.class);
        if (StringUtils.isNotBlank(reqVO.getCategoryId())) {
            wrapper.leftJoin(SpecDO.class, SpecDO::getId, StockDO::getSpecId);
            wrapper.eq(SpecDO::getCategoryId, reqVO.getCategoryId());
        }
        if (StringUtils.isNotBlank(reqVO.getWarehouseId())) {
            wrapper.leftJoin(ShelveDO.class, ShelveDO::getId, StockDO::getShelveId);
            wrapper.eq(ShelveDO::getWarehouseId, reqVO.getWarehouseId());
        }

        wrapper.eq(StringUtils.isNotBlank(reqVO.getSpecId()), StockDO::getSpecId, reqVO.getSpecId());
        wrapper.eq(StringUtils.isNotBlank(reqVO.getShelveId()), StockDO::getShelveId, reqVO.getShelveId());
        wrapper.eq(StringUtils.isNotBlank(reqVO.getStorage()), StockDO::getStorage, reqVO.getStorage());

        if (reqVO.getCreateTime() != null && reqVO.getCreateTime().length == 2) {
            wrapper.between(StockDO::getCreateTime, reqVO.getCreateTime()[0], reqVO.getCreateTime()[1]);
        }
        if (reqVO.getUpdateTime() != null && reqVO.getUpdateTime().length == 2) {
            wrapper.between(StockDO::getUpdateTime, reqVO.getUpdateTime()[0], reqVO.getUpdateTime()[1]);
        }

        wrapper.orderByDesc(StockDO::getUpdateTime);
        return selectPage(reqVO, wrapper);
    }

    StockDO selectBySpecAndStorage(@Param(value = "specId") String specId,
        @Param(value = "shelveId") String shelveId,
        @Param(value = "storage") String storage);

    BigDecimal getSpecTotalStock(@Param(value = "specId") String specId);
}
