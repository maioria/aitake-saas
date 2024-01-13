package cn.iocoder.yudao.module.wms.dal.dataobject.stock;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存 DO
 *
 * @author maioria
 */
@TableName("wms_stock")
@KeySequence("wms_stock_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDO extends BaseDO {

    /**
     * Id
     */
    @TableId
    private Integer id;
    /**
     * 所属规格Id
     */
    private String specId;
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

}