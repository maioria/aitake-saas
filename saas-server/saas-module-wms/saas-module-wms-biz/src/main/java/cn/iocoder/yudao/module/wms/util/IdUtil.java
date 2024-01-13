package cn.iocoder.yudao.module.wms.util;

public class IdUtil {
    public static String getShortId() {
        String id = String.format("%016x", cn.hutool.core.util.IdUtil.getSnowflake(1, 1).nextId());
        // 转大写返回
        return id.toUpperCase();
    }
}
