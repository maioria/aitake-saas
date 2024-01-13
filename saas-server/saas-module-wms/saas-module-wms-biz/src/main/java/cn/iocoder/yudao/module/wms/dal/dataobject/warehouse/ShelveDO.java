package cn.iocoder.yudao.module.wms.dal.dataobject.warehouse;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 货架 DO
 *
 * @author maioria
 */
@TableName("wms_shelve")
@KeySequence("wms_shelve_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelveDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 所属仓库Id
     */
    private String warehouseId;
    /**
     * 货架号
     */
    private String name;
    /**
     * 位置
     */
    private String address;
    /**
     * 状态
     */
    private String status;
    /**
     * 排序
     */
    private Integer sequence;

}
