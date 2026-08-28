package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.CredentialTemplate;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.domain.frame.CredentialItemFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.credential.*;
import org.mapstruct.*;

import java.util.List;


/**
 * @version 1.7.0
 * @description: CredentialMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface CredentialMapping {

    /**
     * To credential page response vm credential page response vm.
     *
     * @param credentialTemplate the credential template
     * @return the credential page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(credentialTemplate.getCreateTime()))")
    })
    CredentialPageResponseVM toCredentialPageResponseVM(CredentialTemplate credentialTemplate);

    /**
     * To credential edit response vm credential edit response vm.
     *
     * @param credentialTemplate the credential template
     * @return the credential edit response vm
     */
    CredentialEditResponseVM toCredentialEditResponseVM(CredentialTemplate credentialTemplate);

    /**
     * To credential template credential template.
     *
     * @param credentialEditRequestVM the credential edit request vm
     * @return the credential template
     */
    CredentialTemplate toCredentialTemplate(CredentialEditRequestVM credentialEditRequestVM);

    /**
     * Map credential template.
     *
     * @param credentialEditRequestVM the credential edit request vm
     * @param credentialTemplate      the credential template
     */
    @InheritConfiguration
    void mapCredentialTemplate(CredentialEditRequestVM credentialEditRequestVM, @MappingTarget CredentialTemplate credentialTemplate);

    CredentialItemFrame toCredentialItemFrame(CredentialItemVM credentialItemVM);

    List<CredentialItemFrame> toCredentialItemFrameList(List<CredentialItemVM> credentialItemVMList);

    CredentialItemVM toCredentialItemVM(CredentialItemFrame credentialItemFrame);

    List<CredentialItemVM> toCredentialItemVMList(List<CredentialItemFrame> credentialItemFrameList);

    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(userCredential.getCredentialBuildTime()))")
    })
    CredentialUserPageResponseVM toCredentialUserPageResponseVM(UserCredential userCredential);
}
