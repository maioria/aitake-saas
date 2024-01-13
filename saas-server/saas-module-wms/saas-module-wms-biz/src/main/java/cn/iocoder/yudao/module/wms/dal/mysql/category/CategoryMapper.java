package cn.iocoder.yudao.module.wms.dal.mysql.category;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.CategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.iocoder.yudao.module.wms.controller.admin.category.vo.*;

/**
 * 类目 Mapper
 *
 * @author maioria
 */
@Mapper
public interface CategoryMapper extends BaseMapperX<CategoryDO> {

    default PageResult<CategoryDO> selectPage(CategoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CategoryDO>()
                .likeIfPresent(CategoryDO::getName, reqVO.getName())
                .likeIfPresent(CategoryDO::getOtherName, reqVO.getOtherName())
                .eqIfPresent(CategoryDO::getUnit, reqVO.getUnit())
                .eqIfPresent(CategoryDO::getType, reqVO.getType())
                .eqIfPresent(CategoryDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(CategoryDO::getDescription, reqVO.getDescription())
                .eqIfPresent(CategoryDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CategoryDO::getSequence, reqVO.getSequence())
                .betweenIfPresent(CategoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CategoryDO::getId));
    }

    List<CategoryDO> selectListByKeyword(@Param(value = "keyword") String keyword, @Param(value = "offset") Integer offset
        , @Param(value = "limit") Integer limit);
}
