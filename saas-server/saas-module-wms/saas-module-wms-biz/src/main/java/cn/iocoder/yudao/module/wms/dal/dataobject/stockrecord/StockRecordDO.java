package cn.iocoder.yudao.module.wms.dal.dataobject.stockrecord;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import org.apache.ibatis.type.JdbcType;

/**
 * 出入库记录 DO
 *
 * @author maioria
 */
@TableName("wms_stock_record")
@KeySequence("wms_stock_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRecordDO{

    /**
     * 主键
     */
    @TableId
    private Integer id;
    /**
     * 仓库Id
     */
    private String warehouseId;
    /**
     * 仓库名
     */
    private String warehouseName;
    /**
     * 货架Id
     */
    private String shelveId;
    /**
     * 货架
     */
    private String shelveName;
    /**
     * 货位
     */
    private String storage;
    /**
     * 类目id
     */
    private String categoryId;
    /**
     * 类目名
     */
    private String categoryName;
    /**
     * 规格id
     */
    private String specId;
    /**
     * 规格名
     */
    private String specName;
    /**
     * 类型
     */
    private String type;
    /**
     * 变动数量
     */
    private BigDecimal stock;
    /**
     * 旧库存
     */
    private BigDecimal oldStock;
    /**
     * 新库存
     */
    private BigDecimal newStock;
    /**
     * 旧总库存
     */
    private BigDecimal oldTotalStock;
    /**
     * 新总库存
     */
    private BigDecimal newTotalStock;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 总价
     */
    private BigDecimal totalPrice;
    /**
     * 用户Id
     */
    private String requestUserId;
    /**
     * 用户
     */
    private String requestUserName;
    /**
     * 部门Id
     */
    private String requestDeptId;
    /**
     * 部门
     */
    private String requestDeptName;
    /**
     * 创建人
     */
    private String creatorName;
    /**
     * 事件时间
     */
    private LocalDateTime eventTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creator;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
