package com.mindskip.wdd.domain.ueit;

import lombok.Data;

import java.util.Date;

/**
 * 试卷核验对象 t_exam_paper_answer_error
 *
 * @author libl
 * @date 2025-04-10
 */
@Data
public class ExamPaperAnswerError {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 虚拟机UUID
     */
    private String vmGuid;

    /**
     * 虚拟机父ID
     */
    private String vmParentId;

    /**
     * 试卷ID
     */
    private Long examPaperId;

    /**
     * 答卷ID
     */
    private Long answerId;

    /**
     * 答卷内容ID
     */
    private String answerFrameId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态 0待执行 1成功 2失败
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 初始化试卷核验对象
     *
     * @param userId        用户ID
     * @param examPaperId   试卷ID
     * @param answerId      答卷ID
     * @param answerFrameId 答卷内容ID
     * @return 试卷核验对象
     */
    public static ExamPaperAnswerError initExamPaperAnswerError(Integer userId, String vmGuid, String vmParentId, Long examPaperId, Long answerId, String answerFrameId) {
        ExamPaperAnswerError error = new ExamPaperAnswerError();
        error.setUserId(userId);
        error.setVmGuid(vmGuid);
        error.setVmParentId(vmParentId);
        error.setExamPaperId(examPaperId);
        error.setAnswerId(answerId);
        error.setAnswerFrameId(answerFrameId);
        return error;
    }
}
