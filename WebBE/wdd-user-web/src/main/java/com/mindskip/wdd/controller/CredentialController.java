package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.mapping.CredentialMapping;
import com.mindskip.wdd.service.CredentialService;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.credential.CredentialPageRequestVM;
import com.mindskip.wdd.viewmodel.credential.CredentialPageResponseVM;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @version 1.7.0
 * @description: 合格证书
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/credential")
public class CredentialController extends BaseApiController {

    private final CredentialService credentialService;
    private final CredentialMapping credentialMapping;
    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    /**
     * 证书分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<CredentialPageResponseVM>> pageList(@RequestBody CredentialPageRequestVM model) {
        User user = getCurrentUser();
        model.setUserId(user.getId());
        PageInfo<UserCredential> pageInfo = credentialService.page(model);
        PageInfo<CredentialPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, a -> {
            CredentialPageResponseVM item = credentialMapping.toCredentialPageResponseVM(a);
            if (null == item.getCredentialImagePath()) {
                String credentialPath = null;
                try {
                    credentialPath = credentialService.buildCredential(a);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
                item.setCredentialImagePath(credentialPath);
            }
            return item;
        });
        return RestResponse.ok(page);
    }


}
