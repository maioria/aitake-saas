package cn.iocoder.yudao.module.wms.service.category;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import cn.iocoder.yudao.module.wms.controller.admin.category.vo.*;
import cn.iocoder.yudao.module.wms.core.vo.SelectVO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;
import cn.iocoder.yudao.module.wms.dal.mysql.category.CategoryMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.category.SpecMapper;
import cn.iocoder.yudao.module.wms.util.IdUtil;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wms.enums.ErrorCodeConstants.*;

/**
 * 类目 Service 实现类
 *
 * @author maioria
 */
@Service
@Validated
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SpecMapper specMapper;

    @Override
    public String createCategory(CategorySaveReqVO createReqVO) {
        // 插入
        CategoryDO category = BeanUtils.toBean(createReqVO, CategoryDO.class);
        category.setId("CT" + IdUtil.getShortId());
        categoryMapper.insert(category);
        // 返回
        return category.getId();
    }

    @Override
    public void updateCategory(CategorySaveReqVO updateReqVO) {
        // 校验存在
        validateCategoryExists(updateReqVO.getId());
        // 更新
        CategoryDO updateObj = BeanUtils.toBean(updateReqVO, CategoryDO.class);
        categoryMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(String id) {
        // 校验存在
        validateCategoryExists(id);
        // 删除
        categoryMapper.deleteById(id);

        // 删除子表
        deleteSpecByCategoryId(id);
    }

    private void validateCategoryExists(String id) {
        if (categoryMapper.selectById(id) == null) {
            throw exception(CATEGORY_NOT_EXISTS);
        }
    }

    @Override
    public CategoryDO getCategory(String id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public PageResult<CategoryDO> getCategoryPage(CategoryPageReqVO pageReqVO) {
        return categoryMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（规格） ====================

    @Override
    public PageResult<SpecDO> getSpecPage(PageParam pageReqVO, String categoryId) {
        return specMapper.selectPage(pageReqVO, categoryId);
    }

    @Override
    public String createSpec(SpecDO spec) {
        spec.setId("SP" + IdUtil.getShortId());
        specMapper.insert(spec);
        return spec.getId();
    }

    @Override
    public void updateSpec(SpecDO spec) {
        // 校验存在
        validateSpecExists(spec.getId());
        // 更新
        specMapper.updateById(spec);
    }

    @Override
    public void deleteSpec(String id) {
        // 校验存在
        validateSpecExists(id);
        // 删除
        specMapper.deleteById(id);
    }

    @Override
    public SpecDO getSpec(String id) {
        return specMapper.selectById(id);
    }

    @Override
    public List<SelectVO> getCategorySimpleList(String keyword) {
        // 通过mybatis-plus方法根据关键字查询前100条数据，只需要查询name与id字段
        List<CategoryDO> productSpuList = categoryMapper.selectListByKeyword(keyword, 0, 300);
        List<SelectVO> result = new ArrayList<>();
        for (CategoryDO item : productSpuList) {
            SelectVO select = new SelectVO();
            select.setKey(item.getId());
            select.setLabel(item.getName());
            result.add(select);
        }
        return result;
    }

    @Override
    public List<SelectVO> getSpecSimpleList(String categoryId, String keyword) {
        // 通过mybatis-plus方法根据关键字查询前100条数据，只需要查询name与id字段
        List<SpecDO> list = specMapper.selectListByKeyword(categoryId, keyword, 0, 300);
        List<SelectVO> result = new ArrayList<>();
        for (SpecDO item : list) {
            SelectVO selector = new SelectVO();
            selector.setKey(item.getId());
            selector.setLabel(item.getName());
            result.add(selector);
        }
        return result;
    }

    private void validateSpecExists(String id) {
        if (specMapper.selectById(id) == null) {
            throw exception(SPEC_NOT_EXISTS);
        }
    }

    private void deleteSpecByCategoryId(String categoryId) {
        specMapper.deleteByCategoryId(categoryId);
    }

}
