package com.mindskip.wdd.constant;

/**
 * 缓存的key 常量
 *
 * @author libl
 * @date 2025-04-15
 */
public class CacheConstants {

    /**
     * 登录用户编号 redis key
     */
    public static final String LOGIN_USERID_KEY = "ueit:login_userid";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "ueit:sys_config";

    /**
     * 实训虚拟机时间限制 单位(分钟) 默认值
     */
    public static final int TRAIN_TIME_LIMIT_DEFAULT = 60;

    /**
     * 实训虚拟机过期时间限制 单位(分钟) 默认值
     */
    public static final int EXPIRED_TIME_LIMIT_DEFAULT = 20;
}
