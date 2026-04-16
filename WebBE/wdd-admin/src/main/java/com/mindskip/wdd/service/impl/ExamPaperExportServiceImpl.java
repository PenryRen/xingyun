package com.mindskip.wdd.service.impl;

import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.service.ExamPaperExportService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import oshi.PlatformEnum;
import oshi.SystemInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 5.9.0
 * @description: 试卷导出
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/3 11:45
 */
@Service
@AllArgsConstructor
public class ExamPaperExportServiceImpl implements ExamPaperExportService {


    private final UserService userService;


    @Override
    public String answerExport(ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM) {
        ExamPaperDoPaperVM examPaper = examPaperAnswerEditResponseVM.getPaper();
        ExamPaperAnswerInfoResponseVM answer = examPaperAnswerEditResponseVM.getAnswer();
        User user = userService.getById(answer.getCreateUser());
        StringBuilder paperTitleSB = new StringBuilder();
        paperTitleSB.append(String.format("<h1 style='text-align: center;'>%s</h1>", examPaper.getName()));
        paperTitleSB.append(String.format("<h4 style='text-align: center;'>姓名：%s%s得分：%s/%s%s耗时：%s%s提交时间：%s</h4>", user.getRealName(), buildWhite(8),
                answer.getUserScore(), answer.getPaperScore(), buildWhite(8), answer.getDoTimeStr(), buildWhite(8), DateTimeUtil.dateTimeFullFormat(answer.getCreateTime())));

        Integer index = 1;
        for (ExamPaperDoTitle title : examPaper.getExamPaperDoTitleList()) {
            paperTitleSB.append(String.format("<h4>%s</h3>", title.getName()));
            StringBuilder questionItemSB = new StringBuilder();
            List<QuestionFrame> questionFrameList = title.getQuestionFrameList();
            for (int i = 0; i < questionFrameList.size(); i++) {
                QuestionFrame questionFrame = questionFrameList.get(i);
                QuestionAnswerFrame questionAnswerFrame = answer.getQuestionAnswerFrameList().get(index - 1);
                questionItemSB.append(questionAnswerBuild(questionFrame, questionAnswerFrame, index++));
            }
            paperTitleSB.append(questionItemSB.toString());
            paperTitleSB.append("<br/>");
        }
        return String.format("<html lang='en'>" +
                "<head><meta charset='utf-8'></head>" +
                "<body style='font-family: YouYuan;font-size:14px;line-height:23px'>%s</body>" +
                "</html>", paperTitleSB.toString());
    }


    /**
     * 题目构建
     *
     * @param questionFrame
     * @param questionAnswerFrame
     * @param index
     * @return {@link String}
     */
    private String questionAnswerBuild(QuestionFrame questionFrame, QuestionAnswerFrame questionAnswerFrame, Integer index) {
        StringBuilder questionSB = new StringBuilder();
        questionSB.append(String.format("%d、 %s（%s分)", index, questionFrame.getTitle(), questionFrame.getTrickScore()));
        questionSB.append("<br/>");

        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        String userContent = "";
        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                questionFrame.getQuestionItemFrames().forEach(item -> {
                    questionSB.append(String.format("%s %s<br/>", item.getPrefix(), item.getContent()));
                });
                if (null != questionAnswerFrame.getContentKey()) {
                    userContent = questionFrame.getQuestionItemFrames().stream()
                            .filter(item -> item.getKey().equals(questionAnswerFrame.getContentKey()))
                            .findFirst().get().getPrefix();
                }
                questionSB.append(String.format("用户答案： %s<br/>", userContent));
                questionSB.append(String.format("标答： %s<br/>", questionAnswerFrame.getCorrectPrefix()));
                break;
            case UncertainMultipleChoice:
            case MultipleChoice:
                questionFrame.getQuestionItemFrames().forEach(item -> {
                    questionSB.append(String.format("%s %s<br/>", item.getPrefix(), item.getContent()));
                });
                if (questionAnswerFrame.getContentArrayKey().size() > 0) {
                    userContent = questionFrame.getQuestionItemFrames().stream()
                            .filter(item -> questionAnswerFrame.getContentArrayKey().stream()
                                    .anyMatch(contentItem -> item.getKey().equals(contentItem)))
                            .map(item -> item.getPrefix())
                            .collect(Collectors.joining(" "));
                }
                questionSB.append(String.format("用户答案： %s<br/>", userContent));
                questionSB.append(String.format("标答： %s<br/>", questionAnswerFrame.getCorrectPrefix()));
                break;
            case GapFilling:
                questionSB.append("用户答案： <br/>");
                List<String> contentArray = questionAnswerFrame.getContentArray();
                for (int i = 0; i < contentArray.size(); i++) {
                    questionSB.append(buildWhite(12));
                    questionSB.append(String.format("%d. %s<br/>", i + 1, contentArray.get(i)));
                }
                questionSB.append("标答： <br/>");
                questionFrame.getQuestionItemFrames().forEach(item -> {

                    questionSB.append(buildWhite(7));
                    questionSB.append(String.format("%s %s<br/>", item.getPrefix(), item.getContent()));
                });
                break;
            case ShortAnswer:
                questionSB.append(String.format("用户答案： %s<br/>", questionAnswerFrame.getContent() == null ? "" : questionAnswerFrame.getContent()));
                questionSB.append(String.format("标答： %s<br/>", questionFrame.getCorrect()));
                break;
        }
        questionSB.append(String.format("结果： %s<br/>", questionAnswerFrame.getDoRight() ? "正确" : "错误"));
        questionSB.append(String.format("得分： %s<br/>", questionAnswerFrame.getCustomerScoreVM()));
        questionSB.append(String.format("难度： %s<br/>", questionFrame.getDifficult()));
        questionSB.append(String.format("解析： %s<br/>", questionFrame.getAnalyze()));


        questionSB.append("<br/>");
        return questionSB.toString();
    }

    /**
     * 空格
     *
     * @param number
     * @return {@link String}
     */
    private String buildWhite(Integer number) {
        if (SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWS) {
            number = number / 3;
        }
        String white = "";
        for (int i = 0; i < number; i++) {
            white += "&nbsp;";
        }
        return white;
    }

}
