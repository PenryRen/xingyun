package com.mindskip.wdd.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.domain.ExamPaperBuild;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperBuildStatusEnum;
import com.mindskip.wdd.domain.enums.ExamPaperBuildTypeEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.enums.RangeTypeEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.listener.UserVMListener;
import com.mindskip.wdd.mapping.ExamPaperBuildMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.exam.build.*;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import com.mindskip.wdd.viewmodel.excel.UserVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionRandom;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 组卷规则接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/exam/paper/build")
public class ExamPaperBuildController extends BaseApiController {


    private final ExamPaperBuildService examPaperBuildService;
    private final ExamPaperArchiveService examPaperArchiveService;
    private final ExamPaperBuildMapping examPaperBuildMapping;
    private final QuestionService questionService;
    private final QuestionArchiveService questionArchiveService;
    private final ExamPaperService examPaperService;
    private final UserService userService;
    private final BeanValidator beanValidator;
    private final FileUploadService fileUploadService;

    /**
     * 构建组卷规则分页
     *
     * @param model the model
     * @return rest response
     */
    @PostMapping("/page")
    @PreAuthorize("exam:paper:build:page")
    public RestResponse<PageInfo<ExamPaperBuildPageResponseVM>> page(@RequestBody ExamPaperBuildPageRequestVM model) {
        initPermission(model);
        PageInfo<ExamPaperBuild> pageInfo = examPaperBuildService.page(model);
        PageInfo<ExamPaperBuildPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, examPaperBuild -> {
            ExamPaperBuildPageResponseVM examPaperPageResponseVM = examPaperBuildMapping.toExamPaperBuildPageResponseVM(examPaperBuild);
            examPaperPageResponseVM.setBuildStatusStr(ExamPaperBuildStatusEnum.fromCode(examPaperBuild.getBuildStatus()).getName());
            examPaperPageResponseVM.setBuildTypeStr(ExamPaperBuildTypeEnum.fromCode(examPaperBuild.getBuildType()).getName());
            ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(examPaperBuild.getExamPaperArchiveId());
            if (null != examPaperArchive) {
                examPaperPageResponseVM.setExamPaperArchive(examPaperArchive.getLevel());
            }
            return examPaperPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 组卷查询
     *
     * @param id the id
     * @return rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize({"exam:paper:build:manual:update", "exam:paper:build:operate:update", "exam:paper:build:extract:update", "exam:paper:build:random:update,exam:paper:build:copy"})
    public RestResponse<ExamPaperBuildEditRequestVM> select(@PathVariable Long id) {
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(id);
        ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM = examPaperBuildMapping.toExamPaperBuildEditRequestVM(examPaperBuild);
        List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()), DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()));
        examPaperBuildEditRequestVM.setLimitDateTime(limitDateTime);
        examPaperBuildEditRequestVM.setBuildConfig(examPaperBuild.getBuildConfig());
        List<ExamPaperTitleItemVM> titleItems = examPaperBuild.getBuildConfig().getExamPaperBuildTitleList().stream()
                .map(bt -> {
                    ExamPaperTitleItemVM examPaperTitleItemVM = examPaperBuildMapping.toExamPaperTitleItemVM(bt);
                    List<QuestionEditRequestVM> questionItems = bt.getQuestionItems().stream()
                            .map(qi -> {
                                QuestionEditRequestVM questionEditRequestVM = questionService.selectQuestionEditRequestVM(qi.getId());
                                questionEditRequestVM.setScore(qi.getScore());
                                return questionEditRequestVM;
                            }).collect(Collectors.toList());
                    examPaperTitleItemVM.setQuestionItems(questionItems);
                    return examPaperTitleItemVM;
                }).collect(Collectors.toList());
        examPaperBuildEditRequestVM.setTitleItems(titleItems);
        return RestResponse.ok(examPaperBuildEditRequestVM);
    }


    /**
     * 人工组卷、实训组卷、抽题组卷 创建
     *
     * @param model the model
     * @return rest response
     */
    @PostMapping("/create")
    @PreAuthorize({"exam:paper:build:manual:create", "exam:paper:build:operate:create", "exam:paper:build:extract:create", "exam:paper:build:copy"})
    public RestResponse create(@RequestBody @Valid ExamPaperBuildEditRequestVM model) {
        RestResponse valid = paperBuildValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }
        examPaperBuildService.insertExamPaperBuild(model, getCurrentUser());
        return RestResponse.ok();
    }


    /**
     * 人工组卷、实训组卷、抽题组卷 更新
     *
     * @param model the model
     * @return rest response
     */
    @PostMapping("/update")
    @PreAuthorize({"exam:paper:build:manual:update", "exam:paper:build:operate:update", "exam:paper:build:extract:update"})
    public RestResponse update(@RequestBody @Valid ExamPaperBuildEditRequestVM model) {
        RestResponse valid = paperBuildValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }
        ExamPaperBuildStatusEnum examPaperBuildStatusEnum = ExamPaperBuildStatusEnum.fromCode(model.getBuildStatus());
        if (examPaperBuildStatusEnum != ExamPaperBuildStatusEnum.WaitPublish) {
            return RestResponse.fail(2, "该试卷状态不能编辑");
        }
        examPaperBuildService.updateExamPaperBuild(model, getCurrentUser().getId());
        return RestResponse.ok();
    }


    /**
     * 抽题组卷
     *
     * @param model the model
     * @return rest response
     */
    @PostMapping("/check/extract")
    public RestResponse checkExtract(@RequestBody ExamPaperBuildExtract model) {
        if (model.getDifficult().equals(0)) {
            model.setDifficult(null);
        }

        if (!randomNumberValid(model)) {
            return RestResponse.fail(2, "请输入抽题数量");
        }

        List<ExamPaperTitleItemVM> titleItems = new ArrayList<>();
        Integer limit = model.getSingleChoice();
        if (limit != null && limit != 0) {
            ExamPaperTitleItemVM examPaperTitleItemVM = randomTitle(new QuestionRandom(model.getQuestionArchiveIdList(), QuestionTypeEnum.SingleChoice.getCode(), limit, model.getDifficult()));
            if (examPaperTitleItemVM.getQuestionItems().size() != 0) {
                titleItems.add(examPaperTitleItemVM);
            }
        }

        limit = model.getMultipleChoice();
        if (limit != null && limit != 0) {
            ExamPaperTitleItemVM examPaperTitleItemVM = randomTitle(new QuestionRandom(model.getQuestionArchiveIdList(), QuestionTypeEnum.MultipleChoice.getCode(), limit, model.getDifficult()));
            if (examPaperTitleItemVM.getQuestionItems().size() != 0) {
                titleItems.add(examPaperTitleItemVM);
            }
        }

        limit = model.getUncertainMultipleChoice();
        if (limit != null && limit != 0) {
            ExamPaperTitleItemVM examPaperTitleItemVM = randomTitle(new QuestionRandom(model.getQuestionArchiveIdList(), QuestionTypeEnum.UncertainMultipleChoice.getCode(), limit, model.getDifficult()));
            if (examPaperTitleItemVM.getQuestionItems().size() != 0) {
                titleItems.add(examPaperTitleItemVM);
            }
        }

        limit = model.getTrueFalse();
        if (limit != null && limit != 0) {
            ExamPaperTitleItemVM examPaperTitleItemVM = randomTitle(new QuestionRandom(model.getQuestionArchiveIdList(), QuestionTypeEnum.TrueFalse.getCode(), limit, model.getDifficult()));
            if (examPaperTitleItemVM.getQuestionItems().size() != 0) {
                titleItems.add(examPaperTitleItemVM);
            }
        }

        limit = model.getGapFilling();
        if (limit != null && limit != 0) {
            ExamPaperTitleItemVM examPaperTitleItemVM = randomTitle(new QuestionRandom(model.getQuestionArchiveIdList(), QuestionTypeEnum.GapFilling.getCode(), limit, model.getDifficult()));
            if (examPaperTitleItemVM.getQuestionItems().size() != 0) {
                titleItems.add(examPaperTitleItemVM);
            }
        }

        limit = model.getShortAnswer();
        if (limit != null && limit != 0) {
            ExamPaperTitleItemVM examPaperTitleItemVM = randomTitle(new QuestionRandom(model.getQuestionArchiveIdList(), QuestionTypeEnum.ShortAnswer.getCode(), limit, model.getDifficult()));
            if (examPaperTitleItemVM.getQuestionItems().size() != 0) {
                titleItems.add(examPaperTitleItemVM);
            }
        }

        return RestResponse.ok(titleItems);
    }


    /**
     * 随机组卷题目数量查询
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/check/random")
    public RestResponse checkRandom(@RequestBody @Valid RandomQuestionCheckVM model) {
        QuestionRandom questionRandom = new QuestionRandom(Arrays.asList(model.getQuestionArchiveId()), model.getQuestionType(), model.getDifficult());
        initPermission(questionRandom);
        Long randomCount = questionService.randomQuestionCount(questionRandom);
        if (model.getNumber() > randomCount) {
            return RestResponse.fail(2, "数量不足，可用题数为：" + randomCount);
        } else {
            QuestionArchive questionArchive = questionArchiveService.getById(model.getQuestionArchiveId());
            return RestResponse.ok(questionArchive.getName());
        }
    }


    /**
     * 创建随机组卷
     *
     * @param model the model
     * @return rest response
     */
    @PostMapping("/random/create")
    @PreAuthorize({"exam:paper:build:random:create", "exam:paper:build:copy"})
    public RestResponse randomCreate(@RequestBody ExamPaperBuildEditRequestVM model) {
        RestResponse valid = paperBuildValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }
        examPaperBuildService.insertExamPaperBuild(model, getCurrentUser());
        return RestResponse.ok();
    }


    /**
     * 更新随机组卷
     *
     * @param model the model
     * @return rest response
     */
    @PostMapping("/random/update")
    @PreAuthorize("exam:paper:build:random:update")
    public RestResponse randomUpdate(@RequestBody ExamPaperBuildEditRequestVM model) {
        RestResponse valid = paperBuildValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }
        ExamPaperBuildStatusEnum examPaperBuildStatusEnum = ExamPaperBuildStatusEnum.fromCode(model.getBuildStatus());
        if (examPaperBuildStatusEnum != ExamPaperBuildStatusEnum.WaitPublish) {
            return RestResponse.fail(2, "该试卷状态不能编辑");
        }
        examPaperBuildService.updateExamPaperBuild(model, getCurrentUser().getId());
        return RestResponse.ok();
    }


    /**
     * 试卷发布
     *
     * @param id the id
     * @return rest response
     */
    @PostMapping("/publish/{id}")
    @PreAuthorize("exam:paper:build:publish")
    public RestResponse publish(@PathVariable Long id) {
        User createUser = getCurrentUser();
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(id);
        ExamPaperBuildTypeEnum buildTypeEnum = ExamPaperBuildTypeEnum.fromCode(examPaperBuild.getBuildType());
        switch (buildTypeEnum) {
            case MANUAL:
            case EXTRACT:
            case OPERATE:
                examPaperBuildService.publishExamPaper(examPaperBuild, createUser);
                break;
            case RANDOM:
                examPaperBuildService.publishRandomExamPaper(examPaperBuild, createUser);
                break;
        }
        return RestResponse.ok();
    }

    /**
     * 试卷撤回
     *
     * @param id the id
     * @return rest response
     */
    @PostMapping("/recall/{id}")
    @PreAuthorize("exam:paper:build:recall")
    public RestResponse recall(@PathVariable Long id) {
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(id);
        examPaperBuild.setBuildStatus(ExamPaperBuildStatusEnum.Recall.getCode());
        examPaperBuildService.updateById(examPaperBuild);
        examPaperService.deleteByExamPaperBuildId(examPaperBuild.getId());
        examPaperService.deleteChildByExamPaperBuildId(examPaperBuild.getId());
        return RestResponse.ok();
    }


    /**
     * 试卷删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("exam:paper:build:delete")
    public RestResponse delete(@PathVariable Long id) {
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(id);
        examPaperBuild.setDeleted(true);
        examPaperBuildService.updateById(examPaperBuild);
        examPaperService.deleteByExamPaperBuildId(examPaperBuild.getId());
        examPaperService.deleteChildByExamPaperBuildId(examPaperBuild.getId());
        return RestResponse.ok();
    }


    /**
     * 用户导入
     *
     * @param request the request
     * @return the rest response
     * @throws IOException the io exception
     */
    @RequestMapping("/employee/import")
    @ResponseBody
    public RestResponse userUploadAndReadExcel(HttpServletRequest request) throws IOException {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        List<Integer> departmentFilter = getRole().getDataFilter().getDepartmentIdList();
        List<UserVM> userVMList = new ArrayList<>();
        EasyExcel.read(file.getInputStream(), UserVM.class, new UserVMListener(userVMList, beanValidator, userService, getCurrentUser(), departmentFilter)).sheet().doRead();
        File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
        EasyExcel.write(excelTemp, UserVM.class).sheet("员工").doWrite(userVMList);
        String filePath = fileUploadService.fileUpload(excelTemp, String.format("员工导入结果 - %s.xlsx", DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
        PaperUserImportResponseVM paperUserImportResponseVM = new PaperUserImportResponseVM();
        List<PaperUserImportVM> userList = userVMList.stream().filter(u -> u.getSuccess())
                .map(u -> new PaperUserImportVM(u.getId(), u.getUserName(), u.getRealName(), u.getDepartmentId()))
                .collect(Collectors.toList());
        paperUserImportResponseVM.setUserList(userList);
        paperUserImportResponseVM.setFilePath(filePath);
        return RestResponse.ok(paperUserImportResponseVM);
    }


    /**
     * 试卷随机抽题，生成试卷标题
     *
     * @param questionRandom
     * @return
     */
    private ExamPaperTitleItemVM randomTitle(QuestionRandom questionRandom) {
        initPermission(questionRandom);
        if (questionRandom.getDifficult() != null && questionRandom.getDifficult().equals(0)) {
            questionRandom.setDifficult(null);
        }
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionRandom.getQuestionType());
        ExamPaperTitleItemVM examPaperTitleItemVM = new ExamPaperTitleItemVM();
        examPaperTitleItemVM.setName(questionTypeEnum.getName());
        questionRandom.setQuestionType(questionTypeEnum.getCode());
        List<QuestionEditRequestVM> questionItems = questionService.randomQuestion(questionRandom).stream()
                .map(id -> questionService.selectQuestionEditRequestVM(id))
                .collect(Collectors.toList());
        examPaperTitleItemVM.setQuestionItems(questionItems);
        return examPaperTitleItemVM;
    }


    /**
     * 试卷参数检查
     *
     * @param model
     * @return
     */
    private RestResponse paperBuildValid(ExamPaperBuildEditRequestVM model) {
        if (null != model.getBuildConfig().getCheat() && model.getBuildConfig().getCheat()) {
            if (null == model.getBuildConfig().getMaxCheatCount()) {
                return RestResponse.fail(2, "防作弊次数不能为空");
            }
        } else {
            model.getBuildConfig().setMaxCheatCount(null);
        }

        model.getBuildConfig().setDepartmentIdList(selectDepartmentFilter(model.getBuildConfig().getDepartmentIdList()));
        List<ExamPaperBuildTitle> examPaperBuildTitleList = model.getTitleItems().stream()
                .map(ti -> {
                    List<ExamPaperBuildQuestion> questionItems = ti.getQuestionItems().stream()
                            .map(qu -> examPaperBuildMapping.toExamPaperBuildQuestion(qu))
                            .collect(Collectors.toList());
                    ExamPaperBuildTitle examPaperBuildTitle = examPaperBuildMapping.toExamPaperBuildTitle(ti);
                    examPaperBuildTitle.setName(ti.getName());
                    examPaperBuildTitle.setQuestionItems(questionItems);
                    return examPaperBuildTitle;
                }).collect(Collectors.toList());
        model.getBuildConfig().setExamPaperBuildTitleList(examPaperBuildTitleList);

        RangeTypeEnum rangeTypeEnum = RangeTypeEnum.fromCode(model.getRangeType());
        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(model.getBuildType());
        ExamPaperBuildConfig buildConfig = model.getBuildConfig();

        if(examPaperBuildTypeEnum.getCode() == ExamPaperBuildTypeEnum.OPERATE.getCode()){
            if(StringUtils.isBlank(model.getVmType())){
                return RestResponse.fail(2, "实训环境不能为空");
            }
        }

        if (RangeTypeEnum.Apply == rangeTypeEnum) {
            if (null == buildConfig.getExamPaperApplySelect()) {
                return RestResponse.fail(2, "考试报名不能为空");
            }
            buildConfig.setDepartmentIdList(new ArrayList<>());
            buildConfig.setExamPaperUserSelectList(new ArrayList<>());
        } else if (RangeTypeEnum.Customize == rangeTypeEnum) {
            if (null == model.getLimitDateTime() || 2 != model.getLimitDateTime().size()) {
                return RestResponse.fail(2, "考试时间不能为空");
            } else {
                if (StringUtils.isEmpty(model.getLimitDateTime().get(0)) || StringUtils.isEmpty(model.getLimitDateTime().get(1))) {
                    return RestResponse.fail(2, "考试时间不能为空");
                }
            }
            Date end = DateTimeUtil.parse(model.getLimitDateTime().get(1));
            if (new Date().after(end)) {
                return RestResponse.fail(2, "考试结束时间不能小于当前时间");
            }

            List<ExamPaperUserSelect> useSelectList = buildConfig.getExamPaperUserSelectList();
            List<Integer> departmentIdList = buildConfig.getDepartmentIdList();

            if (departmentIdList.size() == 0 && useSelectList.size() == 0) {
                return RestResponse.fail(2, "考试部门和考试员工不能同时为空");
            } else if (departmentIdList.size() > 0 && useSelectList.size() > 0) {
                //处理同时选择部门和员工的重复数据，清理掉重复员工
                List<Integer> userSelectIdList = useSelectList.stream().map(u -> u.getId()).collect(Collectors.toList());
                List<ExamPaperUserSelect> userSelectFilterList = userService.getUserByExcludeDepartment(userSelectIdList, departmentIdList);
                buildConfig.setExamPaperUserSelectList(userSelectFilterList);
            }
            buildConfig.setExamPaperApplySelect(null);
        }

        switch (examPaperBuildTypeEnum) {
            case MANUAL:
            case EXTRACT:
            case OPERATE:
                List<ExamPaperBuildTitle> titleList = buildConfig.getExamPaperBuildTitleList();
                if (titleList.size() == 0) {
                    return RestResponse.fail(3, "请添加试卷标题");
                }
                for (ExamPaperBuildTitle title : titleList) {
                    if (StringUtils.isBlank(title.getName())) {
                        return RestResponse.fail(4, "试卷标题不能为空");
                    }
                    if (title.getQuestionItems().size() == 0) {
                        return RestResponse.fail(5, "试卷题目不能为空");
                    }
                }
                Integer questionCount = titleList.stream().mapToInt(title -> title.getQuestionItems().size()).sum();
                model.setQuestionCount(questionCount);
                break;
            case RANDOM:
                if (buildConfig.getExamPaperBuildRandomList().size() == 0) {
                    return RestResponse.fail(2, "出题策略不能为空");
                }
                Integer success = SystemCode.OK.getCode();
                for (ExamPaperBuildRandom examPaperBuildRandom : buildConfig.getExamPaperBuildRandomList()) {
                    RestResponse restResponse = randomCheck(examPaperBuildRandom);
                    if (success != restResponse.getCode()) {
                        return restResponse;
                    }
                }
        }
        return RestResponse.ok();
    }


    /**
     * 抽题数有效
     *
     * @param examPaperBuildExtract 试卷建立提取
     * @return {@link Boolean}
     */
    private Boolean randomNumberValid(ExamPaperBuildExtract examPaperBuildExtract) {
        if (examPaperBuildExtract.getSingleChoice() != null && !examPaperBuildExtract.getSingleChoice().equals(0)) {
            return true;
        }
        if (examPaperBuildExtract.getMultipleChoice() != null && !examPaperBuildExtract.getMultipleChoice().equals(0)) {
            return true;
        }
        if (examPaperBuildExtract.getUncertainMultipleChoice() != null && !examPaperBuildExtract.getUncertainMultipleChoice().equals(0)) {
            return true;
        }
        if (examPaperBuildExtract.getTrueFalse() != null && !examPaperBuildExtract.getTrueFalse().equals(0)) {
            return true;
        }
        if (examPaperBuildExtract.getGapFilling() != null && !examPaperBuildExtract.getGapFilling().equals(0)) {
            return true;
        }
        if (examPaperBuildExtract.getShortAnswer() != null && !examPaperBuildExtract.getShortAnswer().equals(0)) {
            return true;
        }
        return false;
    }

    /**
     * 随机组卷，参数检查
     *
     * @param examPaperBuildRandom
     * @return
     */
    private RestResponse randomCheck(ExamPaperBuildRandom examPaperBuildRandom) {
        if (examPaperBuildRandom.getDifficult() != null && examPaperBuildRandom.getDifficult().equals(0)) {
            examPaperBuildRandom.setDifficult(null);
        }
        Integer number = examPaperBuildRandom.getNumber();
        String score = examPaperBuildRandom.getScore();

        QuestionArchive questionArchive = questionArchiveService.getById(examPaperBuildRandom.getQuestionArchiveId());
        String errorMsg = String.format("【%s】【%s】【%d】", questionArchive.getName(), QuestionTypeEnum.fromCode(examPaperBuildRandom.getQuestionType()).getName(), examPaperBuildRandom.getDifficult());
        if (number == null || number.equals(0)) {
            if (StringUtils.isEmpty(score) || ExamUtil.scoreFromVM(score).equals(0)) {
                return RestResponse.ok();
            } else {
                return RestResponse.fail(6, errorMsg + "数量输入不正确");
            }
        } else {
            if (StringUtils.isEmpty(score) || ExamUtil.scoreFromVM(score).equals(0)) {
                return RestResponse.fail(6, errorMsg + "分数输入不正确");
            } else {
                QuestionRandom questionRandom = new QuestionRandom(Arrays.asList(examPaperBuildRandom.getQuestionArchiveId()), examPaperBuildRandom.getQuestionType(), examPaperBuildRandom.getDifficult());
                initPermission(questionRandom);
                Long randomCount = questionService.randomQuestionCount(questionRandom);
                if (examPaperBuildRandom.getNumber() > randomCount) {
                    return RestResponse.fail(7, errorMsg + "数量不足，可用题数为：" + randomCount);
                } else {
                    return RestResponse.ok();
                }
            }
        }
    }

}
