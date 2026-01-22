package com.mindskip.wdd.controller;

import cn.hutool.core.util.ReUtil;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.Question;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.domain.QuestionJson;
import com.mindskip.wdd.domain.enums.DifficultEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.mapping.QuestionMapping;
import com.mindskip.wdd.service.QuestionArchiveService;
import com.mindskip.wdd.service.QuestionImportService;
import com.mindskip.wdd.service.QuestionService;
import com.mindskip.wdd.utility.ErrorUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.question.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

/**
 * @version 1.7.0
 * @description: 题目接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/question")
public class QuestionController extends BaseApiController {

    private final QuestionService questionService;
    private final QuestionMapping questionMapping;
    private final QuestionArchiveService questionArchiveService;
    private final QuestionImportService questionImportService;

    /**
     * 题目分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("question:page")
    public RestResponse<PageInfo<QuestionPageResponseVM>> page(@RequestBody QuestionPageRequestVM model) {
        initPermission(model);
        PageInfo<Question> pageInfo = questionService.page(model);
        PageInfo<QuestionPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, question -> {
            QuestionPageResponseVM questionPageResponseVM = questionMapping.toQuestionResponseVM(question);
            questionPageResponseVM.setTypeEnumStr(QuestionTypeEnum.fromCode(question.getQuestionType()).getName());
            questionPageResponseVM.setDifficultStr(DifficultEnum.fromCode(question.getDifficult()).getName());
            if (null != question.getQuestionArchiveId()) {
                QuestionArchive questionArchive = questionArchiveService.getById(question.getQuestionArchiveId());
                questionPageResponseVM.setQuestionArchive(questionArchive.getName());
            }
            QuestionJson questionJson = questionService.getQuestionJsonById(question.getQuestionFrameId());
            if (null != questionJson) {
                questionPageResponseVM.setTitle(questionJson.getContent().getTitle());
            }
            return questionPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 题目预览
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    public RestResponse<QuestionEditRequestVM> previewSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 单选题查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/singleChoice/{id}")
    @PreAuthorize("question:update:singleChoice")
    public RestResponse<QuestionEditRequestVM> singleChoiceSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 单选题创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create/singleChoice")
    @PreAuthorize("question:create:singleChoice")
    public RestResponse singleChoiceCreate(@RequestBody @Valid QuestionEditRequestVM model) {
        return create(model);
    }


    /**
     * 单选题更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update/singleChoice")
    @PreAuthorize("question:update:singleChoice")
    public RestResponse singleChoiceUpdate(@RequestBody @Valid QuestionEditRequestVM model) {
        return update(model);
    }


    /**
     * 多选题查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/multipleChoice/{id}")
    @PreAuthorize("question:update:multipleChoice")
    public RestResponse<QuestionEditRequestVM> multipleChoiceSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 多选题创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create/multipleChoice")
    @PreAuthorize("question:create:multipleChoice")
    public RestResponse multipleChoiceCreate(@RequestBody @Valid QuestionEditRequestVM model) {
        return create(model);
    }


    /**
     * 多选题更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update/multipleChoice")
    @PreAuthorize("question:update:multipleChoice")
    public RestResponse multipleChoiceUpdate(@RequestBody @Valid QuestionEditRequestVM model) {
        return update(model);
    }


    /**
     * 不定项选择题查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/uncertainMultipleChoice/{id}")
    @PreAuthorize("question:update:uncertainMultipleChoice")
    public RestResponse<QuestionEditRequestVM> uncertainMultipleChoiceSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 不定项选择题创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create/uncertainMultipleChoice")
    @PreAuthorize("question:create:uncertainMultipleChoice")
    public RestResponse uncertainMultipleChoiceCreate(@RequestBody @Valid QuestionEditRequestVM model) {
        return create(model);
    }


    /**
     * 不定项选择题更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update/uncertainMultipleChoice")
    @PreAuthorize("question:update:uncertainMultipleChoice")
    public RestResponse uncertainMultipleChoiceUpdate(@RequestBody @Valid QuestionEditRequestVM model) {
        return update(model);
    }

    /**
     * 判断题查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/trueFalse/{id}")
    @PreAuthorize("question:update:trueFalse")
    public RestResponse<QuestionEditRequestVM> trueFalseSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 判断题创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create/trueFalse")
    @PreAuthorize("question:create:trueFalse")
    public RestResponse trueFalseCreate(@RequestBody @Valid QuestionEditRequestVM model) {
        return create(model);
    }


    /**
     * 判断题更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update/trueFalse")
    @PreAuthorize("question:update:trueFalse")
    public RestResponse trueFalseUpdate(@RequestBody @Valid QuestionEditRequestVM model) {
        return update(model);
    }

    /**
     * 填空题查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/gapFilling/{id}")
    @PreAuthorize("question:update:gapFilling")
    public RestResponse<QuestionEditRequestVM> gapFillingSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 填空题创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create/gapFilling")
    @PreAuthorize("question:create:gapFilling")
    public RestResponse gapFillingCreate(@RequestBody @Valid QuestionEditRequestVM model) {
        return create(model);
    }


    /**
     * 填空题更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update/gapFilling")
    @PreAuthorize("question:update:gapFilling")
    public RestResponse gapFillingUpdate(@RequestBody @Valid QuestionEditRequestVM model) {
        return update(model);
    }


    /**
     * 解答题查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/shortAnswer/{id}")
    @PreAuthorize("question:update:shortAnswer")
    public RestResponse<QuestionEditRequestVM> shortAnswerSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 解答题创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create/shortAnswer")
    @PreAuthorize("question:create:shortAnswer")
    public RestResponse shortAnswerCreate(@RequestBody @Valid QuestionEditRequestVM model) {
        return create(model);
    }


    /**
     * 解答题更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update/shortAnswer")
    @PreAuthorize("question:update:shortAnswer")
    public RestResponse shortAnswerUpdate(@RequestBody @Valid QuestionEditRequestVM model) {
        return update(model);
    }


    /**
     * 实训题查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/trainOperate/{id}")
    @PreAuthorize("question:update:shortAnswer")
    public RestResponse<QuestionEditRequestVM> trainOperateSelect(@PathVariable Long id) {
        return select(id);
    }


    /**
     * 实训题创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create/trainOperate")
    @PreAuthorize("question:create:shortAnswer")
    public RestResponse trainOperateCreate(@RequestBody @Valid QuestionEditRequestVM model) {
        return create(model);
    }


    /**
     * 实训题更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update/trainOperate")
    @PreAuthorize("question:update:shortAnswer")
    public RestResponse trainOperateUpdate(@RequestBody @Valid QuestionEditRequestVM model) {
        return update(model);
    }

    /**
     * 题目删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("question:delete")
    public RestResponse delete(@PathVariable Long id) {
        Question question = questionService.getById(id);
        question.setDeleted(true);
        questionService.updateById(question);
        return RestResponse.ok();
    }


    /**
     * 题目excel导入
     *
     * @param request the request
     * @return the rest response
     * @throws IOException the io exception
     */
    @RequestMapping("/upload/excel")
    @PreAuthorize("question:upload:excel")
    @ResponseBody
    public RestResponse questionUploadAndReadExcel(HttpServletRequest request) throws IOException {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        String filePath = null;
        try (InputStream stream = file.getInputStream()) {
            filePath = questionImportService.fromExcel(getCurrentUser(), stream);
        }
        if (filePath == null) {
            return RestResponse.fail(2, "Excel文档导入失败");
        } else {
            return RestResponse.ok(filePath);
        }
    }


    /**
     * 题目word上传
     *
     * @param request the request
     * @return the rest response
     * @throws IOException the io exception
     */
    @RequestMapping("/upload/word")
    @PreAuthorize("question:upload:word")
    @ResponseBody
    public RestResponse questionUploadAndReadWord(HttpServletRequest request) throws IOException {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        String filePath = null;
        try (InputStream stream = file.getInputStream()) {
            filePath = questionImportService.fromWord(getCurrentUser(), stream);
        }
        if (filePath == null) {
            return RestResponse.fail(2, "Word文档导入失败");
        } else {
            return RestResponse.ok(filePath);
        }
    }

    /**
     * 题目查询
     *
     * @param id
     * @return
     */
    private RestResponse<QuestionEditRequestVM> select(Long id) {
        QuestionEditRequestVM questionEditRequestVM = questionService.selectQuestionEditRequestVM(id);
        return RestResponse.ok(questionEditRequestVM);
    }


    /**
     * 题目创建
     *
     * @param model
     * @return
     */
    private RestResponse create(QuestionEditRequestVM model) {
        RestResponse validQuestionEditRequestResult = validQuestionEditRequestVM(model);
        if (validQuestionEditRequestResult.getCode() != SystemCode.OK.getCode()) {
            return validQuestionEditRequestResult;
        }
        Question question = questionService.insertQuestion(model, getCurrentUser());
        return RestResponse.ok(question.getId());
    }


    /**
     * 题目更新
     *
     * @param model
     * @return
     */
    private RestResponse update(QuestionEditRequestVM model) {
        RestResponse validQuestionEditRequestResult = validQuestionEditRequestVM(model);
        if (validQuestionEditRequestResult.getCode() != SystemCode.OK.getCode()) {
            return validQuestionEditRequestResult;
        }
        questionService.updateQuestion(model);
        return RestResponse.ok(model.getId());
    }


    /**
     * 题目参数校验
     *
     * @param model
     * @return
     */
    private RestResponse validQuestionEditRequestVM(QuestionEditRequestVM model) {
        Integer fillSumScore = 0;
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(model.getQuestionType());
        if (questionTypeEnum == QuestionTypeEnum.SingleChoice || questionTypeEnum == QuestionTypeEnum.MultipleChoice
                || questionTypeEnum == QuestionTypeEnum.UncertainMultipleChoice || questionTypeEnum == QuestionTypeEnum.TrueFalse
                || questionTypeEnum == QuestionTypeEnum.GapFilling) {
            if (model.getItems().size() == 0) {
                String errorMsg = ErrorUtil.parameterErrorFormat("getItems", "选项或者空不能为空");
                return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
            }
        }

        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                if (StringUtils.isBlank(model.getCorrect())) {
                    String errorMsg = ErrorUtil.parameterErrorFormat("correct", "答案不能为空");
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }
                break;
            case MultipleChoice:
                if (model.getCorrectArray().size() == 0) {
                    String errorMsg = ErrorUtil.parameterErrorFormat("correctArray", "答案不能为空");
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }
                break;
            case UncertainMultipleChoice: //不定项选择题
                if (model.getCorrectArray().size() == 0) {
                    String errorMsg = ErrorUtil.parameterErrorFormat("correctArray", "答案不能为空");
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }

                for (QuestionEditItemVM choiceItem : model.getItems()) {
                    Boolean isCorrectItem = model.getCorrectArray().stream().anyMatch(correctItem -> correctItem.equals(choiceItem.getPrefix()));
                    if (isCorrectItem) {
                        if (StringUtils.isEmpty(choiceItem.getScore())) {
                            String errorMsg = ErrorUtil.parameterErrorFormat("score", "正确选项的分数不能为空");
                            return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                        } else {
                            fillSumScore += ExamUtil.scoreFromVM(choiceItem.getScore());
                        }
                    } else {
                        choiceItem.setScore(null);
                    }
                }
                model.setScore(ExamUtil.scoreToVM(fillSumScore));
                break;
            case GapFilling: //设置题目总分
                if (model.getItems().stream().anyMatch(d -> StringUtils.isEmpty(d.getScore()))) {
                    String errorMsg = ErrorUtil.parameterErrorFormat("score", "空的分数不能为空");
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }
                fillSumScore = model.getItems().stream().mapToInt(d -> ExamUtil.scoreFromVM(d.getScore())).sum();
                model.setScore(ExamUtil.scoreToVM(fillSumScore));
                break;
            case TrainOperate: //实训题
                if (StringUtils.isBlank(model.getVmType())){
                    String errorMsg = "实训环境不能为空";
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }
                if (StringUtils.isBlank(model.getCommandType())){
                    String errorMsg = "规则类型不能为空";
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }
                if (StringUtils.isBlank(model.getCommand())){
                    String errorMsg = "执行命令不能为空";
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }
                if (StringUtils.isBlank(model.getCorrect())){
                    String errorMsg = "匹配结果不能为空";
                    return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
                }
                break;
        }


        //分数校验
        if (StringUtils.isEmpty(model.getScore())) {
            String errorMsg = ErrorUtil.parameterErrorFormat("score", "分数不能为空");
            return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
        }

        String title = questionTypeEnum == QuestionTypeEnum.GapFilling
                ? ReUtil.replaceAll(model.getTitle(), "<span class=\"gapfilling-span\" data-gap-key=\"([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\">(.*?)<\\/span>", "")
                : model.getTitle();
        Long questionId = questionService.checkLikeQuestion(new QuestionLike(model.getId(), model.getQuestionArchiveId(), questionTypeEnum.getCode(), HtmlUtil.escape(title)));
        if (null != questionId) {
            return new RestResponse<>(3, "已存在重复题目");
        }

        return RestResponse.ok();
    }
}
