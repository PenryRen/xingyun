package com.mindskip.wdd.configuration.utility;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

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
    public void validate(Object target) throws BindException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
        if (constraintViolations.size() > 0) {
            String className = target.getClass().getName();
            BindingResult bindingResult = new BeanPropertyBindingResult(target, className);
            constraintViolations.forEach(cv -> bindingResult.addError(new FieldError(className, cv.getPropertyPath().toString(), cv.getMessage())));
            throw new BindException(bindingResult);
        }
    }

}
