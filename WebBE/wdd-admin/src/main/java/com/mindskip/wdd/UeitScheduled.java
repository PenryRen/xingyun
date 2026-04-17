package com.mindskip.wdd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务（原虚拟机列表/授权同步已移除，改为无影云桌面 API）。
 *
 * @author libl
 * @date 2025-09-10
 */
@Component
public class UeitScheduled {

    private final static Logger logger = LoggerFactory.getLogger(UeitScheduled.class);

    public static void main(String[] args) {
        logger.debug("UeitScheduled stub");
    }
}
