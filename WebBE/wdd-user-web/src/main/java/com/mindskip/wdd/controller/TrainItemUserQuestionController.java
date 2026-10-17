package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import com.mindskip.wdd.mapping.TrainItemUserQuestionMapping;
import com.mindskip.wdd.service.TrainItemUserQuestionService;
import com.mindskip.wdd.viewmodel.ueit.TrainItemUserQuestionVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户课件功能点Controller
 *
 * @author libl
 * @date 2025-04-09
 */
@RestController
@RequestMapping("/api/train/item/user/question")
public class TrainItemUserQuestionController extends BaseApiController {
    @Autowired
    private TrainItemUserQuestionService trainItemUserQuestionService;

    @Autowired
    private TrainItemUserQuestionMapping trainItemUserQuestionMapping;

    /**
     * 获取用户课件功能点列表
     * @param query
     * @return
     */
    @PostMapping("/list")
    public RestResponse list(@RequestBody TrainItemUserQuestion query) {
        User currentUser = getCurrentUser();
        if (null == currentUser) {
            return RestResponse.fail(SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
        }
        if (null == query || null == query.getTrainId() || null == query.getCourseWareId()) {
            return RestResponse.fail(SystemCode.ParameterValidError.getCode(), SystemCode.ParameterValidError.getMessage());
        }
        query.setUserId(currentUser.getId());
        List<TrainItemUserQuestion> list = trainItemUserQuestionService.selectTrainItemUserQuestionList(query);
        List<TrainItemUserQuestionVM> vmList = new ArrayList<>();
        for (TrainItemUserQuestion trainItemUserQuestion : list) {
            TrainItemUserQuestionVM vm = trainItemUserQuestionMapping.toTrainItemUserQuestionVM(trainItemUserQuestion);
            vmList.add(vm);
        }
        return RestResponse.ok(vmList);
    }
}
