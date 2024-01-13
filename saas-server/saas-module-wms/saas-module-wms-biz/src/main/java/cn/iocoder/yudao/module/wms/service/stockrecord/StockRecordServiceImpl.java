package cn.iocoder.yudao.module.wms.service.stockrecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.stock.StockDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.stockrecord.StockRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.ShelveDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.iocoder.yudao.module.wms.dal.mysql.category.CategoryMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.category.SpecMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.stock.StockMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.stockrecord.StockRecordMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.warehouse.ShelveMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.warehouse.WarehouseMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wms.enums.ErrorCodeConstants.*;

/**
 * 出入库记录 Service 实现类
 *
 * @author maioria
 */
@Service
@Validated
public class StockRecordServiceImpl implements StockRecordService {

    @Resource
    private StockRecordMapper stockRecordMapper;

    @Resource
    private WarehouseMapper warehouseMapper;

    @Resource
    private ShelveMapper shelveMapper;

    @Resource
    private StockMapper stockMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SpecMapper specMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private DeptApi deptApi;

    @Override
    public void addWmsWarehouseStockRecord(StockRecordUpdateStockVO createReqVO) {
        StockRecordDO stockRecord = new StockRecordDO();
        stockRecord.setSpecId(createReqVO.getSpecId());
        stockRecord.setRemark(createReqVO.getRemark());
        stockRecord.setCreator(createReqVO.getCreator());
        stockRecord.setRequestUserId(createReqVO.getRequestUserId());
        stockRecord.setType(createReqVO.getType());
        LocalDateTime now = LocalDateTime.now();
        stockRecord.setCreateTime(now);
        if (createReqVO.getEventTime() != null) {
            stockRecord.setEventTime(createReqVO.getEventTime());
        } else {
            stockRecord.setEventTime(now);
        }

        // 补充额外仓库信息
        stockRecord.setShelveId(createReqVO.getShelveId());
        ShelveDO shelve =
            shelveMapper.selectById(createReqVO.getShelveId());
        stockRecord.setShelveName(shelve.getName());
        String warehouseId = shelve.getWarehouseId();
        stockRecord.setWarehouseId(warehouseId);
        WarehouseDO wmsWarehouse = warehouseMapper.selectById(warehouseId);
        stockRecord.setWarehouseName(wmsWarehouse.getName());
        stockRecord.setStorage(createReqVO.getStorage());

        // 补充额外产品信息
        SpecDO specDO = specMapper.selectById(createReqVO.getSpecId());
        stockRecord.setSpecName(specDO.getName());

        // 补充库存与价格信息
        // 如果用户没有输入价格，则检查sku是否有设置价格，如果有设置，则使用sku价格
        if (createReqVO.getPrice() != null) {
            stockRecord.setPrice(createReqVO.getPrice());
        } else if (specDO.getPrice() != null) {
            stockRecord.setPrice(specDO.getPrice());
        }
        stockRecord.setStock(createReqVO.getStock());
        if (stockRecord.getPrice() != null
            && stockRecord.getStock() != null) {
            stockRecord.setTotalPrice(stockRecord.getPrice()
                .multiply(stockRecord.getStock()));
        }
        stockRecord.setUnit(specDO.getUnit());

        CategoryDO categoryDO = categoryMapper.selectById(specDO.getCategoryId());
        stockRecord.setCategoryId(categoryDO.getId());
        stockRecord.setCategoryName(categoryDO.getName());

        // 补充额外用户、部门信息
        AdminUserRespDTO createUser = adminUserApi.getUser(Long.parseLong(createReqVO.getCreator()));
        stockRecord.setCreatorName(createUser.getNickname());
        AdminUserRespDTO requestUser;
        if (createReqVO.getRequestUserId() == null) {
            createReqVO.setRequestUserId(createReqVO.getCreator());
        }
        if (Objects.equals(createReqVO.getCreator(), createReqVO.getRequestUserId())) {
            stockRecord.setRequestUserName(createUser.getNickname());
            requestUser = createUser;
        } else {
            requestUser = adminUserApi.getUser(Long.parseLong(createReqVO.getRequestUserId()));
        }

        stockRecord.setRequestUserName(requestUser.getNickname());
        if (createReqVO.getRequestDepartmentId() != null) {
            DeptRespDTO dept = deptApi.getDept(Long.parseLong(createReqVO.getRequestDepartmentId()));
            stockRecord.setRequestDeptId(dept.getId().toString());
            stockRecord.setRequestDeptName(dept.getName());
        } else {
            if (createUser.getDeptId() != null) {
                DeptRespDTO dept = deptApi.getDept(createUser.getDeptId());
                stockRecord.setRequestDeptId(dept.getId().toString());
                stockRecord.setRequestDeptName(dept.getName());
            }
        }

        // 计算SKU旧的总库存与新库存
        BigDecimal oldStock;
        StockDO wmsWarehouseStock = stockMapper.selectBySpecAndStorage(createReqVO.getSpecId(),
            createReqVO.getShelveId(), createReqVO.getStorage());
        if (wmsWarehouseStock == null) {
            oldStock = new BigDecimal(0);
        } else {
            oldStock = wmsWarehouseStock.getStock();
        }
        stockRecord.setOldStock(oldStock);
        stockRecord.setNewStock(oldStock.add(createReqVO.getStock()));
        BigDecimal oldTotalStock = stockMapper.getSpecTotalStock(createReqVO.getSpecId());
        if (oldTotalStock == null) {
            oldTotalStock = new BigDecimal(0);
        }
        stockRecord.setOldTotalStock(oldTotalStock);
        stockRecord.setNewTotalStock(oldTotalStock.add(createReqVO.getStock()));

        stockRecordMapper.insert(stockRecord);
    }

    @Override
    public Integer createStockRecord(StockRecordSaveReqVO createReqVO) {
        // 插入
        StockRecordDO stockRecord = BeanUtils.toBean(createReqVO, StockRecordDO.class);
        stockRecordMapper.insert(stockRecord);
        // 返回
        return stockRecord.getId();
    }

    @Override
    public void updateStockRecord(StockRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateStockRecordExists(updateReqVO.getId());
        // 更新
        StockRecordDO updateObj = BeanUtils.toBean(updateReqVO, StockRecordDO.class);
        stockRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteStockRecord(Integer id) {
        // 校验存在
        validateStockRecordExists(id);
        // 删除
        stockRecordMapper.deleteById(id);
    }

    private void validateStockRecordExists(Integer id) {
        if (stockRecordMapper.selectById(id) == null) {
            throw exception(STOCK_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public StockRecordDO getStockRecord(Integer id) {
        return stockRecordMapper.selectById(id);
    }

    @Override
    public PageResult<StockRecordDO> getStockRecordPage(StockRecordPageReqVO pageReqVO) {
        return stockRecordMapper.selectPage(pageReqVO);
    }

}
