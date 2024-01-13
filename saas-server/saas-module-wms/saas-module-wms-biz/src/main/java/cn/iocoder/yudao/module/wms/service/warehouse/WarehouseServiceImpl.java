package cn.iocoder.yudao.module.wms.service.warehouse;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.wms.controller.admin.warehouse.vo.*;
import cn.iocoder.yudao.module.wms.core.vo.SelectVO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.ShelveDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wms.dal.mysql.warehouse.ShelveMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.warehouse.WarehouseMapper;
import cn.iocoder.yudao.module.wms.util.IdUtil;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wms.enums.ErrorCodeConstants.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 仓库 Service 实现类
 *
 * @author maioria
 */
@Service
@Validated
public class WarehouseServiceImpl implements WarehouseService {

    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private ShelveMapper shelveMapper;

    @Override
    public String createWarehouse(WarehouseSaveReqVO createReqVO) {
        // 插入
        WarehouseDO warehouse = BeanUtils.toBean(createReqVO, WarehouseDO.class);
        warehouse.setId("WH" + IdUtil.getShortId());
        warehouseMapper.insert(warehouse);
        // 返回
        return warehouse.getId();
    }

    @Override
    public void updateWarehouse(WarehouseSaveReqVO updateReqVO) {
        // 校验存在
        validateWarehouseExists(updateReqVO.getId());
        // 更新
        WarehouseDO updateObj = BeanUtils.toBean(updateReqVO, WarehouseDO.class);
        warehouseMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWarehouse(String id) {
        // 校验存在
        validateWarehouseExists(id);
        // 删除
        warehouseMapper.deleteById(id);

        // 删除子表
        deleteShelveByWarehouseId(id);
    }

    private void validateWarehouseExists(String id) {
        if (warehouseMapper.selectById(id) == null) {
            throw exception(WAREHOUSE_NOT_EXISTS);
        }
    }

    @Override
    public WarehouseDO getWarehouse(String id) {
        return warehouseMapper.selectById(id);
    }

    @Override
    public PageResult<WarehouseDO> getWarehousePage(WarehousePageReqVO pageReqVO) {
        return warehouseMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（货架） ====================

    @Override
    public PageResult<ShelveDO> getShelvePage(PageParam pageReqVO, String warehouseId) {
        return shelveMapper.selectPage(pageReqVO, warehouseId);
    }

    @Override
    public String createShelve(ShelveDO shelve) {
        shelve.setId("WS" + IdUtil.getShortId());
        shelveMapper.insert(shelve);
        return shelve.getId();
    }

    @Override
    public void updateShelve(ShelveDO shelve) {
        // 校验存在
        validateShelveExists(shelve.getId());
        // 更新
        shelveMapper.updateById(shelve);
    }

    @Override
    public void deleteShelve(String id) {
        // 校验存在
        validateShelveExists(id);
        // 删除
        shelveMapper.deleteById(id);
    }

    @Override
    public ShelveDO getShelve(String id) {
        return shelveMapper.selectById(id);
    }

    @Override
    public List<SelectVO> getWarehouseSimpleList(String keyword) {
        LambdaQueryWrapper<WarehouseDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(WarehouseDO::getName, keyword);
        }
        List<WarehouseDO> list = warehouseMapper.selectList(wrapper);
        List<SelectVO> result = new ArrayList<>();
        for (WarehouseDO item : list) {
            SelectVO select = new SelectVO();
            select.setKey(item.getId());
            select.setLabel(item.getName());
            result.add(select);
        }
        return result;
    }

    @Override
    public List<SelectVO> getShelveSimpleList(String warehouseId, String keyword) {
        LambdaQueryWrapper<ShelveDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShelveDO::getWarehouseId, warehouseId);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(ShelveDO::getName, keyword);
        }
        wrapper.orderByAsc(ShelveDO::getSequence);
        wrapper.orderByDesc(ShelveDO::getCreateTime);
        List<ShelveDO> list = shelveMapper.selectList(wrapper);
        List<SelectVO> result = new ArrayList<>();
        for (ShelveDO item : list) {
            SelectVO select = new SelectVO();
            select.setKey(item.getId());
            select.setLabel(item.getName());
            result.add(select);
        }
        return result;
    }

    private void validateShelveExists(String id) {
        if (shelveMapper.selectById(id) == null) {
            throw exception(SHELVE_NOT_EXISTS);
        }
    }

    private void deleteShelveByWarehouseId(String warehouseId) {
        shelveMapper.deleteByWarehouseId(warehouseId);
    }

}
