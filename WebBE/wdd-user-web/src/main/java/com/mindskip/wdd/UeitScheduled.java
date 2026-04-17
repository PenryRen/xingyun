package com.mindskip.wdd;

import com.mindskip.wdd.constant.ExamErrorConstants;
import com.mindskip.wdd.domain.enums.ueit.VmTypeEnum;
import com.mindskip.wdd.domain.ueit.ExamPaperAnswerError;
import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.service.AsyncService;
import com.mindskip.wdd.service.ExamPaperAnswerErrorService;
import com.mindskip.wdd.service.VmWareService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 *
 * @author libl
 * @date 2025-04-10
 */
@Component
@AllArgsConstructor
public class UeitScheduled {

    private final static Logger logger = LoggerFactory.getLogger(UeitScheduled.class);

    /**
     * 定时执行 检查试卷核验记录
     */
//    @Scheduled(cron = "0 0/5 * * * ?")
    private void examErrorTask() {
        try {
            ExamPaperAnswerError select = new ExamPaperAnswerError();
            select.setStatus(ExamErrorConstants.status_init);
            List<ExamPaperAnswerError> list = examPaperAnswerErrorService.selectExamPaperAnswerErrorList(select);
            for (ExamPaperAnswerError error : list) {
                asyncService.checkAnswer(error);
            }
        } catch (Exception e) {
            logger.error("定时任务[{}],业务异常----------------------", "检查试卷核验记录", e);
            e.printStackTrace();
        }
    }

    /**
     * 定时执行 释放云电脑记录并调用阿里云删除
     */
//    @Scheduled(cron = "0 2/5 * * * ?")
    private void releaseTask() {
        VmWare selectDeleted = new VmWare();
        selectDeleted.setVmType(VmTypeEnum.Child.getCode());
        selectDeleted.setDeleted(true);
        List<VmWare> disabledList = vmWareService.selectVmWareList(selectDeleted);
        for (VmWare vmWare : disabledList) {
            vmWareService.releaseVmWare(vmWare);
        }

        VmWare selectExpired = new VmWare();
        selectExpired.setValidEndTime(vmWareService.getExpiredValidEndTime());
        List<VmWare> expiredList = vmWareService.selectExpiredVmWareList(selectExpired);
        for (VmWare vmWare : expiredList) {
            vmWareService.releaseVmWare(vmWare);
        }
    }

    private final AsyncService asyncService;

    private final ExamPaperAnswerErrorService examPaperAnswerErrorService;

    private final VmWareService vmWareService;
}
