package cn.iocoder.yudao.module.wms.service.stock;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.wms.controller.admin.stock.vo.*;
import cn.iocoder.yudao.module.wms.controller.admin.stockrecord.vo.StockRecordUpdateStockVO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.category.SpecDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.stock.StockDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wms.dal.dataobject.stockrecord.StockRecordDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.ShelveDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.iocoder.yudao.module.wms.dal.mysql.category.CategoryMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.category.SpecMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.stock.StockMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.stockrecord.StockRecordMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.warehouse.ShelveMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.warehouse.WarehouseMapper;
import cn.iocoder.yudao.module.wms.service.stockrecord.StockRecordService;
import cn.iocoder.yudao.module.wms.util.IdUtil;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wms.enums.ErrorCodeConstants.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 库存 Service 实现类
 *
 * @author maioria
 */
@Service
@Validated
@Slf4j
public class StockServiceImpl implements StockService {

    @Resource
    private StockMapper stockMapper;

    @Resource
    private StockRecordService stockRecordService;

    @Resource
    private SpecMapper specMapper;

    @Resource
    private WarehouseMapper warehouseMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ShelveMapper shelveMapper;

    @Resource
    private StockRecordMapper stockRecordMapper;

    @Override
    public Integer createStock(StockSaveReqVO createReqVO) {
        // 如果sku在库位上已经有数据，则不允许再录入
        List<StockDO> wmsWarehouseStockDOs = stockMapper.selectList(new LambdaQueryWrapper<StockDO>()
            .eq(StockDO::getShelveId, createReqVO.getShelveId())
            .eq(StockDO::getStorage, createReqVO.getStorage())
            .eq(StockDO::getSpecId, createReqVO.getSpecId())
            .eq(StockDO::getDeleted, 0));
        if (!wmsWarehouseStockDOs.isEmpty()) {
            throw exception(WMS_WAREHOUSE_STORAGE_EXISTS);
        }

        // 生成对应的出入库记录
        StockRecordUpdateStockVO stockRecordUpdateStockVO = new StockRecordUpdateStockVO();
        stockRecordUpdateStockVO.setShelveId(createReqVO.getShelveId());
        stockRecordUpdateStockVO.setSpecId(createReqVO.getSpecId());
        stockRecordUpdateStockVO.setStock(createReqVO.getStock());
        stockRecordUpdateStockVO.setPrice(createReqVO.getPrice());
        stockRecordUpdateStockVO.setRemark("初始化库存");
        stockRecordUpdateStockVO.setRequestUserId(createReqVO.getRequestUserId());
        stockRecordUpdateStockVO.setCreator(createReqVO.getCreator());
        stockRecordUpdateStockVO.setType("INIT");
        stockRecordUpdateStockVO.setStorage(createReqVO.getStorage());
        stockRecordService.addWmsWarehouseStockRecord(stockRecordUpdateStockVO);

        // 插入
        StockDO stock = BeanUtils.toBean(createReqVO, StockDO.class);
        stockMapper.insert(stock);


        // 更新对应规格的stock
        refreshTotalStock(createReqVO.getSpecId());
        return stock.getId();
    }

    @Override
    public void updateStock(StockExecuteVO updateReqVO) {
        // 校验存在
        validateStockExists(updateReqVO.getStockId());
        StockDO wmsWarehouseStockDO = stockMapper.selectById(updateReqVO.getStockId());
        StockRecordUpdateStockVO stockRecordUpdateStockVO = new StockRecordUpdateStockVO();
        stockRecordUpdateStockVO.setShelveId(wmsWarehouseStockDO.getShelveId());
        stockRecordUpdateStockVO.setSpecId(wmsWarehouseStockDO.getSpecId());
        String updateType = updateReqVO.getType();
        if (Objects.equals(updateType, "IN") || Objects.equals(updateType, "ADD")) {
            stockRecordUpdateStockVO.setStock(updateReqVO.getStock());
            wmsWarehouseStockDO.setStock(wmsWarehouseStockDO.getStock().add(updateReqVO.getStock()));
        } else if (Objects.equals(updateType, "OUT") || Objects.equals(updateType, "SUBTRACT")) {
//            updateReqVO.getStock() 转负数
            stockRecordUpdateStockVO.setStock(updateReqVO.getStock().negate());
            wmsWarehouseStockDO.setStock(wmsWarehouseStockDO.getStock().subtract(updateReqVO.getStock()));
            if (wmsWarehouseStockDO.getStock().compareTo(BigDecimal.ZERO) < 0) {
                throw exception(WMS_WAREHOUSE_STOCK_NOT_ENOUGH);
            }
        } else {
            throw exception(WMS_WAREHOUSE_STOCK_RECORD_TYPE_ERROR);
        }
        stockRecordUpdateStockVO.setRemark(updateReqVO.getRemark());
        stockRecordUpdateStockVO.setPrice(updateReqVO.getPrice());
        stockRecordUpdateStockVO.setCreator(wmsWarehouseStockDO.getCreator());
        stockRecordUpdateStockVO.setRequestDepartmentId(updateReqVO.getRequestDepartmentId());
        stockRecordUpdateStockVO.setRequestUserId(updateReqVO.getRequestUserId());
        stockRecordUpdateStockVO.setType(updateReqVO.getType());
        stockRecordUpdateStockVO.setStorage(wmsWarehouseStockDO.getStorage());
        stockRecordUpdateStockVO.setEventTime(updateReqVO.getEventTime());
        stockRecordService.addWmsWarehouseStockRecord(stockRecordUpdateStockVO);
        stockMapper.updateById(wmsWarehouseStockDO);

        // 更新对应sku的stock
        refreshTotalStock(wmsWarehouseStockDO.getSpecId());
    }

    @Override
    public void deleteStock(Integer id) {
        // 校验存在
        validateStockExists(id);
        // 减少对应库存
        StockDO stockDO = stockMapper.selectById(id);
        // 如果sku与对应货架存在，才进行记录操作
        SpecDO sku = specMapper.selectById(stockDO.getSpecId());
        ShelveDO shelve = shelveMapper.selectById(stockDO.getShelveId());
        if (sku != null && shelve != null) {
            StockRecordUpdateStockVO stockRecordUpdateStockVO = getStockRecordUpdateStockVO(stockDO);
            stockRecordService.addWmsWarehouseStockRecord(stockRecordUpdateStockVO);
        }
        // 删除
        stockMapper.deleteById(id);

        // 检查在仓库中是否还有数据
        refreshTotalStock(stockDO.getSpecId());
    }

    @NotNull
    private static StockRecordUpdateStockVO getStockRecordUpdateStockVO(StockDO stockDO) {
        StockRecordUpdateStockVO stockRecordUpdateStockVO = new StockRecordUpdateStockVO();
        stockRecordUpdateStockVO.setShelveId(stockDO.getShelveId());
        stockRecordUpdateStockVO.setSpecId(stockDO.getSpecId());
        stockRecordUpdateStockVO.setStock(stockDO.getStock().negate());
        stockRecordUpdateStockVO.setRemark("删除库存");
        stockRecordUpdateStockVO.setCreator(stockDO.getCreator());
        stockRecordUpdateStockVO.setRequestUserId(stockDO.getCreator());
        stockRecordUpdateStockVO.setType("DELETE");
        stockRecordUpdateStockVO.setStorage(stockDO.getStorage());
        return stockRecordUpdateStockVO;
    }

    private void validateStockExists(Integer id) {
        if (stockMapper.selectById(id) == null) {
            throw exception(STOCK_NOT_EXISTS);
        }
    }

    @Override
    public StockDO getStock(Integer id) {
        return stockMapper.selectById(id);
    }

    @Override
    public PageResult<StockRespVO> getStockPage(StockPageReqVO pageReqVO) {
        PageResult<StockDO> pageResult = stockMapper.selectPage(pageReqVO);
        if (pageResult.getTotal() == 0L) {
            return new PageResult<>(0L);
        }
        // 取出warehouseId、warehouseShelveId、productSpuId、productSkuId列表，统一查询，之后迭代赋值
        List<StockDO> list = pageResult.getList();
        Set<String> warehouseIdList = new HashSet<>();
        Set<String> shelveIdList = new HashSet<>();
        Set<String> categoryIdList = new HashSet<>();
        Set<String> productSkuIdList = new HashSet<>();
        for (StockDO stockDO : list) {
            shelveIdList.add(stockDO.getShelveId());
            productSkuIdList.add(stockDO.getSpecId());
        }
        List<ShelveDO> shelveList = shelveMapper.selectBatchIds(shelveIdList);
        List<SpecDO> specList = specMapper.selectBatchIds(productSkuIdList);

        // 根据shelveList，查询warehouseIdList
        for (ShelveDO shelveDO : shelveList) {
            warehouseIdList.add(shelveDO.getWarehouseId());
        }
        // 根据productSkuList，查询productSpuIdList
        for (SpecDO specDO : specList) {
            categoryIdList.add(specDO.getCategoryId());
        }
        List<WarehouseDO> warehouseList = warehouseMapper.selectBatchIds(warehouseIdList);
        List<CategoryDO> categoryList = categoryMapper.selectBatchIds(categoryIdList);
        // 转map
        Map<String, WarehouseDO> warehouseMap = new HashMap<>();
        for (WarehouseDO warehouseDO : warehouseList) {
            warehouseMap.put(warehouseDO.getId(), warehouseDO);
        }
        // 转map
        Map<String, ShelveDO> warehouseShelveMap = new HashMap<>();
        for (ShelveDO warehouseShelveDO : shelveList) {
            warehouseShelveMap.put(warehouseShelveDO.getId(), warehouseShelveDO);
        }
        // 转map
        Map<String, CategoryDO> productSpuMap = new HashMap<>();
        for (CategoryDO productSpuDO : categoryList) {
            productSpuMap.put(productSpuDO.getId(), productSpuDO);
        }
        // 转map
        Map<String, SpecDO> productSkuMap = new HashMap<>();
        for (SpecDO productSkuDO : specList) {
            productSkuMap.put(productSkuDO.getId(), productSkuDO);
        }
        // 赋值
        List<StockRespVO> respList = new ArrayList<>();
        for (StockDO stockDO : list) {
            StockRespVO respVO = BeanUtils.toBean(stockDO, StockRespVO.class);
            if (warehouseShelveMap.get(stockDO.getShelveId()) != null) {
                ShelveDO shelve = warehouseShelveMap.get(stockDO.getShelveId());
                respVO.setShelveName(shelve.getName());
                WarehouseDO warehouseDO = warehouseMap.get(shelve.getWarehouseId());
                respVO.setWarehouseId(warehouseDO.getId());
                respVO.setWarehouseName(warehouseDO.getName());
            } else {
                log.warn("warehouseShelveId:{} not found", stockDO.getShelveId());
            }
            if (productSkuMap.get(stockDO.getSpecId()) != null) {
                SpecDO specDO = productSkuMap.get(stockDO.getSpecId());
                respVO.setPicUrl(specDO.getPicUrl());
                respVO.setUnit(specDO.getUnit());
                respVO.setSpecName(specDO.getName());
                CategoryDO categoryDO = productSpuMap.get(specDO.getCategoryId());
                respVO.setCategoryId(categoryDO.getId());
                respVO.setCategoryName(categoryDO.getName());
            } else {
                log.warn("productSkuId:{} not found", stockDO.getSpecId());
            }

            respList.add(respVO);
        }
        // 生成结果page
        PageResult<StockRespVO> result = new PageResult<>();
        result.setList(respList);
        result.setTotal(pageResult.getTotal());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createAndStock(CreateAndStockReqVO createReqVO) {
        //检查类目名是否存在，如果存在使用现有类目，如果不存在则创建新类目
        CategoryDO categoryDO = categoryMapper.selectOne(new LambdaQueryWrapper<CategoryDO>()
            .eq(CategoryDO::getName, createReqVO.getCategoryName())
            .eq(CategoryDO::getDeleted, 0));
        SpecDO specDO;

        if (categoryDO == null) {
            categoryDO = new CategoryDO();
            categoryDO.setName(createReqVO.getCategoryName());
            categoryDO.setCreator(createReqVO.getCreator());
            categoryDO.setSequence(1);
            categoryDO.setType("GENERAL");
            categoryDO.setId("C" + IdUtil.getShortId());
            categoryMapper.insert(categoryDO);
        }

        specDO = specMapper.selectOne(new LambdaQueryWrapper<SpecDO>()
            .eq(SpecDO::getName, createReqVO.getSpecName())
            .eq(SpecDO::getCategoryId, categoryDO.getId())
            .eq(SpecDO::getDeleted, 0));
        if (specDO == null) {
            specDO = new SpecDO();
            specDO.setName(createReqVO.getSpecName());
            specDO.setCreator(createReqVO.getCreator());
            specDO.setCategoryId(categoryDO.getId());
            specDO.setId("SP" + IdUtil.getShortId());
            specMapper.insert(specDO);
        }

        StockSaveReqVO stockSaveReqVO = new StockSaveReqVO();
        stockSaveReqVO.setCreator(createReqVO.getCreator());
        stockSaveReqVO.setRequestUserId(createReqVO.getRequestUserId());
        stockSaveReqVO.setSpecId(specDO.getId());
        stockSaveReqVO.setStock(createReqVO.getStock());
        stockSaveReqVO.setShelveId(createReqVO.getShelveId());
        stockSaveReqVO.setStorage(createReqVO.getStorage());
        stockSaveReqVO.setPrice(createReqVO.getPrice());
        createStock(stockSaveReqVO);

        return 1;
    }

    @Override
    public void updateColumn(UpdateColumnVO updateReqVO) {
        StockDO warehouseStockDO = stockMapper.selectById(updateReqVO.getStockId());
        SpecDO specDO = specMapper.selectById(warehouseStockDO.getSpecId());

        String column = updateReqVO.getColumn();
        String value = updateReqVO.getValue();
        if (column.equals("CATEGORY")) {
            // 先检查类目名是否已经存在，如果存在则直接设置sku对应类目名为存在的类目，不再进行更新
            List<CategoryDO> productSpuDOs = categoryMapper.selectList(new LambdaQueryWrapper<CategoryDO>()
                .eq(CategoryDO::getName, value)
                .eq(CategoryDO::getDeleted, 0));
            // 如果类目名已经存在则直接修改外键id
            if (!productSpuDOs.isEmpty()) {
                SpecDO sku = specMapper.selectById(warehouseStockDO.getSpecId());
                sku.setCategoryId(productSpuDOs.get(0).getId());
                specMapper.updateById(sku);
                // 查询此sku下的所有出入库记录，统一更新spuId
                List<StockRecordDO> wmsWarehouseStockRecordDOList = stockRecordMapper.selectList(new LambdaQueryWrapper<StockRecordDO>()
                    .eq(StockRecordDO::getSpecId, warehouseStockDO.getSpecId())
                    .eq(StockRecordDO::getDeleted, 0));
                for (StockRecordDO wmsWarehouseStockRecordDO : wmsWarehouseStockRecordDOList) {
                    wmsWarehouseStockRecordDO.setCategoryId(productSpuDOs.get(0).getId());
                    wmsWarehouseStockRecordDO.setCategoryName(productSpuDOs.get(0).getName());
                    stockRecordMapper.updateById(wmsWarehouseStockRecordDO);
                }
            } else {
                CategoryDO productSpuDO = categoryMapper.selectById(specDO.getCategoryId());
                productSpuDO.setName(value);
                categoryMapper.updateById(productSpuDO);
            }
        } else if (column.equals("SPEC")) {
            // 先检查sku名是否已经存在，如果存在则直接设置sku对应sku名为存在的sku，不再进行更新
            List<SpecDO> productSkuDOs = specMapper.selectList(new LambdaQueryWrapper<SpecDO>()
                .eq(SpecDO::getCategoryId, specDO.getCategoryId())
                .eq(SpecDO::getName, value)
                .eq(SpecDO::getDeleted, 0));
            // 如果sku名已经存在则直接设置外键
            if (!productSkuDOs.isEmpty()) {
                // 查询此sku下的所有库存，统一更新skuId
                List<StockDO> wmsWarehouseStockDOList = stockMapper.selectList(new LambdaQueryWrapper<StockDO>()
                    .eq(StockDO::getSpecId, warehouseStockDO.getSpecId())
                    .eq(StockDO::getDeleted, 0));
                for (StockDO wmsWarehouseStockDO : wmsWarehouseStockDOList) {
                    wmsWarehouseStockDO.setSpecId(productSkuDOs.get(0).getId());
                    stockMapper.updateById(wmsWarehouseStockDO);
                }
                // 查询此sku下的所有出入库记录，统一更新skuId
                List<StockRecordDO> wmsWarehouseStockRecordDOList = stockRecordMapper.selectList(new LambdaQueryWrapper<StockRecordDO>()
                    .eq(StockRecordDO::getSpecId, warehouseStockDO.getSpecId())
                    .eq(StockRecordDO::getDeleted, 0));
                for (StockRecordDO wmsWarehouseStockRecordDO : wmsWarehouseStockRecordDOList) {
                    wmsWarehouseStockRecordDO.setSpecId(productSkuDOs.get(0).getId());
                    wmsWarehouseStockRecordDO.setSpecName(productSkuDOs.get(0).getName());
                    stockRecordMapper.updateById(wmsWarehouseStockRecordDO);
                }

                warehouseStockDO.setSpecId(productSkuDOs.get(0).getId());
                stockMapper.updateById(warehouseStockDO);
            } else {
                SpecDO productSkuDO = specMapper.selectById(warehouseStockDO.getSpecId());
                productSkuDO.setName(value);
                specMapper.updateById(productSkuDO);
            }
        } else if (column.equals("SHELVE")) {
            ShelveDO wmsWarehouseShelveDO = shelveMapper.selectById(value);
            if (wmsWarehouseShelveDO == null) {
                throw exception(SHELVE_NOT_EXISTS);
            }
            warehouseStockDO.setShelveId(value);
            stockMapper.updateById(warehouseStockDO);
        } else if (column.equals("STORAGE")) {
            // 检查这个sku是否已经在这个货位上存在
            List<StockDO> wmsWarehouseStockDOList = stockMapper.selectList(new LambdaQueryWrapper<StockDO>()
                .eq(StockDO::getSpecId, warehouseStockDO.getSpecId())
                .eq(StockDO::getShelveId, warehouseStockDO.getShelveId())
                .eq(StockDO::getStorage, value)
                .eq(StockDO::getDeleted, 0));
            if (!wmsWarehouseStockDOList.isEmpty()) {
                throw exception(WMS_WAREHOUSE_STORAGE_EXISTS);
            }
            warehouseStockDO.setStorage(value);
            stockMapper.updateById(warehouseStockDO);
        } else if (column.equals("STOCK")) {
            BigDecimal stock = new BigDecimal(value);
            BigDecimal oldStock = warehouseStockDO.getStock();
            BigDecimal diffStock = stock.subtract(oldStock);
            // 生成对应的出入库记录
            StockRecordUpdateStockVO wmsWarehouseStockRecordUpdateStockVO = new StockRecordUpdateStockVO();
            wmsWarehouseStockRecordUpdateStockVO.setShelveId(warehouseStockDO.getShelveId());
            wmsWarehouseStockRecordUpdateStockVO.setSpecId(warehouseStockDO.getSpecId());
            wmsWarehouseStockRecordUpdateStockVO.setStock(diffStock);
            wmsWarehouseStockRecordUpdateStockVO.setRemark("修改库存");
            wmsWarehouseStockRecordUpdateStockVO.setCreator(updateReqVO.getOperator());
            wmsWarehouseStockRecordUpdateStockVO.setRequestUserId(updateReqVO.getOperator());
            wmsWarehouseStockRecordUpdateStockVO.setType("UPDATE");
            wmsWarehouseStockRecordUpdateStockVO.setStorage(warehouseStockDO.getStorage());
            stockRecordService.addWmsWarehouseStockRecord(wmsWarehouseStockRecordUpdateStockVO);
            warehouseStockDO.setStock(stock);
            stockMapper.updateById(warehouseStockDO);
            // 更新对应sku的stock
            refreshTotalStock(warehouseStockDO.getSpecId());
        } else {
            throw exception(WMS_WAREHOUSE_STOCK_RECORD_TYPE_ERROR);
        }
    }


    /**
     * 更新总库存
     */
    private void refreshTotalStock(String specId) {
        SpecDO specDO = specMapper.selectById(specId);
        BigDecimal skuTotalStock = stockMapper.getSpecTotalStock(specDO.getId());
        specDO.setStock(skuTotalStock);
        specMapper.updateById(specDO);
    }
}
