package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.QuestionJson;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.7.0
 * @description: 题目内容
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface QuestionJsonMapper extends BaseMapper<QuestionJson> {

}
