package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.credential.CredentialPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


/**
 * @version 1.7.0
 * @description: The interface Credential mapping.
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class})
public interface CredentialMapping {
    /**
     * To credential page response vm credential page response vm.
     *
     * @param userCredential the user credential
     * @return the credential page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(userCredential.getCreateTime()))"),
            @Mapping(target = "credentialBuildTime", expression = "java(DateTimeUtil.dateTimeFullFormat(userCredential.getCredentialBuildTime()))")
    })
    CredentialPageResponseVM toCredentialPageResponseVM(UserCredential userCredential);
}
