package com.mindskip.wdd.domain.ueit;

import lombok.Data;

/**
 * 实训虚拟机时间限制
 *
 * @author libl
 * @date 2025-04-18
 */
@Data
public class RemainingTime {

    /**
     * 剩余时间
     */
    private int remainTime;

    /**
     * 单次续期时间限制
     */
    private int singleSecond;
}
