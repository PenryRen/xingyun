package com.mindskip.wdd.configuration.utility;

import com.mindskip.wdd.utility.ErrorUtil;
import com.mindskip.wdd.viewmodel.excel.ExcelResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 对象字段校验
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Component
public class BeanValidator implements InitializingBean {

    private Validator validator;

    public void afterPropertiesSet() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    /**
     * 对象自动验证结果
     *
     * @param target the target
     * @return the excel result
     */
    public ExcelResult validate(Object target) {
        ExcelResult excelResult = new ExcelResult();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
        if (constraintViolations.size() > 0) {
            String message = constraintViolations.stream().map(cv -> ErrorUtil.parameterErrorFormat(cv.getMessage())).collect(Collectors.joining("，"));
            excelResult.setSuccess(false);
            excelResult.setMessage(message);
        } else {
            excelResult.setSuccess(true);
        }
        return excelResult;
    }

}
