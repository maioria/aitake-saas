package cn.iocoder.yudao.module.wms.controller.admin.stock.vo.app;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppStockRespVO {
    private String id;

    private String name;

    private String picUrl;
    /**
     * 类型
     */
    private String type;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 详情
     */
    private String description;

    /**
     * 排序
     */
    private Integer sequence;

    private List<AppSpecRespVO> specList = new ArrayList<>();
}
