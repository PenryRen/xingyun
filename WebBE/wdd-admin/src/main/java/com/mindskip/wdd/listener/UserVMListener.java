package com.mindskip.wdd.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.viewmodel.excel.ExcelResult;
import com.mindskip.wdd.viewmodel.excel.UserVM;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户导入
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@AllArgsConstructor
public class UserVMListener extends AnalysisEventListener<UserVM> {

    private List<UserVM> userVMList;
    private BeanValidator beanValidator;
    private UserService userService;
    private User createUser;
    private List<Integer> departmentFilter;


    @Override
    public void invoke(UserVM userVM, AnalysisContext context) {
        if (null == userVM) return;
        ExcelResult excelResult = beanValidator.validate(userVM);
        if (excelResult.getSuccess()) {
            userService.userImport(userVM, createUser, departmentFilter);
            userVMList.add(userVM);
        } else {
            userVMList.add(userVM);
            userVM.setResult(excelResult.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
