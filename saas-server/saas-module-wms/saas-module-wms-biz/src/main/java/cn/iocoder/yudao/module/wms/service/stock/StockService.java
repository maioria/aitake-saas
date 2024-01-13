package cn.iocoder.yudao.module.wms.service.stock;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.UPDATE;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.wms.controller.admin.stock.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.stock.StockDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 库存 Service 接口
 *
 * @author maioria
 */
public interface StockService {

    /**
     * 创建库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createStock(@Valid StockSaveReqVO createReqVO);

    /**
     * 更新库存
     *
     * @param updateReqVO 更新信息
     */
    void updateStock(@Valid StockExecuteVO updateReqVO);

    /**
     * 删除库存
     *
     * @param id 编号
     */
    void deleteStock(Integer id);

    /**
     * 获得库存
     *
     * @param id 编号
     * @return 库存
     */
    StockDO getStock(Integer id);

    PageResult<StockRespVO> getStockPage(StockPageReqVO pageReqVO);

    Integer createAndStock(CreateAndStockReqVO createReqVO);

    void updateColumn(UpdateColumnVO updateReqVO);
}
