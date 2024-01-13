package cn.iocoder.yudao.module.wms.dal.dataobject.category;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 类目 DO
 *
 * @author maioria
 */
@TableName("wms_category")
@KeySequence("wms_category_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDO extends BaseDO {

    /**
     * Id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 类目名
     */
    private String name;
    /**
     * 别名
     */
    private String otherName;
    /**
     * 单位
     *
     * 枚举 {@link TODO wms_unit 对应的类}
     */
    private String unit;
    /**
     * 类型
     *
     * 枚举 {@link TODO wms_category_type 对应的类}
     */
    private String type;
    /**
     * 图片
     */
    private String picUrl;
    /**
     * 介绍
     */
    private String description;
    /**
     * 状态
     *
     * 枚举 {@link TODO wms_entity_status 对应的类}
     */
    private String status;
    /**
     * 排序
     */
    private Integer sequence;

}