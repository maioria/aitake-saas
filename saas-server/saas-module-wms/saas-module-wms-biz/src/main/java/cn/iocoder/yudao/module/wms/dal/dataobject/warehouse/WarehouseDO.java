package cn.iocoder.yudao.module.wms.dal.dataobject.warehouse;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 仓库 DO
 *
 * @author maioria
 */
@TableName("wms_warehouse")
@KeySequence("wms_warehouse_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDO extends BaseDO {

    /**
     * Id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 仓库名
     */
    private String name;
    /**
     * 地址
     */
    private String address;
    /**
     * 图片
     */
    private String picUrl;
    /**
     * 联系人
     */
    private String contract;
    /**
     * 排序
     */
    private Integer sequence;
    /**
     * 状态
     *
     * 枚举 {@link TODO wms_entity_status 对应的类}
     */
    private String status;

}