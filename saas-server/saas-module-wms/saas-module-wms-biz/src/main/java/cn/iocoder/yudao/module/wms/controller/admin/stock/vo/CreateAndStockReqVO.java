package cn.iocoder.yudao.module.wms.controller.admin.stock.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAndStockReqVO {

    /**
     * 商品名
     */
    private String categoryName;

    /**
     * 规格名
     */
    private String specName;

    /**
     * 所属仓库
     */
    private String warehouseId;

    /**
     * 所属货架
     */
    private String shelveId;

    /**
     * 所属货位
     */
    private String storage;

    /**
     * 数量
     */
    private BigDecimal stock;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 关联用户
     */
    private String requestDeptId;

    /**
     * 关联用户
     */
    private String requestUserId;
}
