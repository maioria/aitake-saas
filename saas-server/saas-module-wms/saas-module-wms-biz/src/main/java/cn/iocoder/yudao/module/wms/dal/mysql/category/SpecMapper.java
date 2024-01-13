package cn.iocoder.yudao.module.wms.dal.mysql.category;


import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.iocoder.yudao.module.wms.controller.admin.category.vo.*;

import java.util.List;

/**
 * 规格 Mapper
 *
 * @author maioria
 */
@Mapper
public interface SpecMapper extends BaseMapperX<SpecDO> {

    default PageResult<SpecDO> selectPage(PageParam reqVO, String categoryId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SpecDO>()
            .eq(SpecDO::getCategoryId, categoryId)
            .orderByDesc(SpecDO::getId));
    }

    default int deleteByCategoryId(String categoryId) {
        return delete(SpecDO::getCategoryId, categoryId);
    }

    List<SpecDO> selectListByKeyword(@Param(value = "categoryId") String categoryId, @Param(value = "keyword") String keyword,
        @Param(value = "offset") Integer offset
        , @Param(value = "limit") Integer limit);
}
