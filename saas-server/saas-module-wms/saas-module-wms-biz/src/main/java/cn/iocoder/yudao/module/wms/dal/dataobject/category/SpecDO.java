package cn.iocoder.yudao.module.wms.dal.dataobject.category;

import lombok.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 规格 DO
 *
 * @author maioria
 */
@TableName("wms_spec")
@KeySequence("wms_spec_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecDO extends BaseDO {

    /**
     * Id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 规格名
     */
    private String name;
    /**
     * 别名
     */
    private String otherName;
    /**
     * 图片
     */
    private String picUrl;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 自定义属性
     */
    private String properties;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 体积
     */
    private BigDecimal volume;
    /**
     * 单位
     */
    private String unit;
    /**
     * 状态
     */
    private String status;
    /**
     * 库存
     */
    private BigDecimal stock;
    /**
     * 序列
     */
    private Integer sequence;
    /**
     * 所属类目
     */
    private String categoryId;

}
