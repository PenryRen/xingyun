package com.mindskip.wdd.viewmodel.ueit;

import lombok.Data;

/**
 * 用户课件功能点 VM
 *
 * @author libl
 * @date 2025-04-18
 */
@Data
public class TrainItemUserQuestionVM {
    /** 问题ID */
    private Long questionId;

    /** 完成情况 */
    private Boolean completion;
}
