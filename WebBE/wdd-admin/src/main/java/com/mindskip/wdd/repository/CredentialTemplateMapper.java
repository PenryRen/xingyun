package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.base.BaseFilter;
import com.mindskip.wdd.domain.CredentialTemplate;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.credential.CredentialPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 证书模板
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface CredentialTemplateMapper extends BaseMapper<CredentialTemplate> {

    /**
     * 获取证书列表
     *
     * @param baseFilter the base filter
     * @return the list
     */
    List<KeyValue> list(BaseFilter baseFilter);

    /**
     * 证书模板分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<CredentialTemplate> page(CredentialPageRequestVM requestVM);
}