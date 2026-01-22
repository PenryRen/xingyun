package com.mindskip.wdd.controller;

import cn.hutool.http.HttpUtil;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.BaseFilter;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.CredentialTemplate;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.domain.enums.CredentialItemFontEnum;
import com.mindskip.wdd.domain.frame.CredentialItemFrame;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.mapping.CredentialMapping;
import com.mindskip.wdd.service.CredentialService;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.utility.certificate.ImageUtil;
import com.mindskip.wdd.viewmodel.credential.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @version 1.7.0
 * @description: 证书模板接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/credential")
public class CredentialController extends BaseApiController {


    private final UserService userService;
    private final DepartmentService departmentService;
    private final CredentialService credentialService;
    private final CredentialMapping credentialMapping;
    private final FileUploadService fileUploadService;
    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    /**
     * 证书模板列表
     *
     * @return the rest response
     */
    @PostMapping("/list")
    public RestResponse<List<KeyValue>> list() {
        BaseFilter baseFilter = new BaseFilter();
        List<KeyValue> credentialList = credentialService.list(baseFilter);
        return RestResponse.ok(credentialList);
    }


    /**
     * 证书模板分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("credential:page")
    public RestResponse<PageInfo<CredentialPageResponseVM>> page(@RequestBody CredentialPageRequestVM model) {
        initPermission(model);
        PageInfo<CredentialTemplate> pageInfo = credentialService.page(model);
        PageInfo<CredentialPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> credentialMapping.toCredentialPageResponseVM(d));
        return RestResponse.ok(page);
    }


    /**
     * 证书创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("credential:create")
    public RestResponse create(@RequestBody @Valid CredentialEditRequestVM model) {
        User user = getCurrentUser();
        CredentialTemplate credentialTemplate = credentialMapping.toCredentialTemplate(model);
        credentialTemplate.setDeleted(false);
        credentialTemplate.setCreateTime(new Date());
        credentialTemplate.setCreateUser(user.getId());
        credentialTemplate.setCreateDepartmentId(user.getDepartmentId());
        List<CredentialItemFrame> credentialItemFrameList = credentialMapping.toCredentialItemFrameList(model.getCredentialItemVMList());
        credentialTemplate.setConfiguration(credentialItemFrameList);
        credentialService.insertCredentialTemplate(credentialTemplate);
        return RestResponse.ok();
    }


    /**
     * 证书查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    public RestResponse<CredentialEditResponseVM> select(@PathVariable Integer id) {
        CredentialTemplate credentialTemplate = credentialService.getCredentialTemplateById(id);
        CredentialEditResponseVM credentialEditResponseVM = credentialMapping.toCredentialEditResponseVM(credentialTemplate);
        List<CredentialItemVM> credentialItemVMList = credentialMapping.toCredentialItemVMList(credentialTemplate.getConfiguration());
        credentialEditResponseVM.setCredentialItemVMList(credentialItemVMList);
        return RestResponse.ok(credentialEditResponseVM);
    }


    /**
     * 证书更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("credential:update")
    public RestResponse update(@RequestBody @Valid CredentialEditRequestVM model) {
        CredentialTemplate oldCredentialTemplate = credentialService.getCredentialTemplateById(model.getId());
        credentialMapping.mapCredentialTemplate(model, oldCredentialTemplate);
        List<CredentialItemFrame> credentialItemFrameList = credentialMapping.toCredentialItemFrameList(model.getCredentialItemVMList());
        oldCredentialTemplate.setConfiguration(credentialItemFrameList);
        credentialService.updateCredentialTemplate(oldCredentialTemplate);
        return RestResponse.ok();
    }


    /**
     * 证书删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("credential:delete")
    public RestResponse delete(@PathVariable Integer id) {
        CredentialTemplate credentialTemplate = credentialService.getCredentialTemplateById(id);
        credentialTemplate.setDeleted(true);
        credentialService.updateCredentialTemplate(credentialTemplate);
        return RestResponse.ok();
    }


    /**
     * 用户证书列表
     *
     * @param model 模型
     * @return {@link RestResponse}<{@link PageInfo}<{@link CredentialUserPageResponseVM}>>
     */
    @PostMapping("/user/page")
    @PreAuthorize("credential:user:page")
    public RestResponse userPage(@RequestBody CredentialUserPageRequestVM model) {
        if (StringUtils.isNotBlank(model.getNo())) {
            if (model.getNo().contains("-")) {
                String id = model.getNo().split("-")[1];
                model.setId(Long.parseLong(id));
            } else {
                return RestResponse.fail(2, "证书编号不正确");
            }
        }
        initPermission(model);
        PageInfo<UserCredential> pageInfo = credentialService.userPage(model);
        PageInfo<CredentialUserPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, userCredential -> {
            String imagePath = null;
            try {
                if (userCredential.getCredentialImagePath() == null) {
                    imagePath = credentialService.buildCredential(userCredential);
                } else {
                    imagePath = userCredential.getCredentialImagePath();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            CredentialTemplate credentialTemplate = credentialService.getCredentialTemplateById(userCredential.getCredentialTemplateId());
            User user = userService.getUserById(userCredential.getUserId());
            Department department = departmentService.getById(user.getDepartmentId());
            CredentialUserPageResponseVM credentialUserPageResponseVM = credentialMapping.toCredentialUserPageResponseVM(userCredential);
            credentialUserPageResponseVM.setCredentialImagePath(imagePath);
            credentialUserPageResponseVM.setCredentialName(credentialTemplate.getName());
            credentialUserPageResponseVM.setCompany(credentialTemplate.getCompany());
            credentialUserPageResponseVM.setRealName(user.getRealName());
            credentialUserPageResponseVM.setUserName(user.getUserName());
            credentialUserPageResponseVM.setWorkNo(user.getWorkNo());
            credentialUserPageResponseVM.setDepartmentName(department.getName());
            credentialUserPageResponseVM.setNo(userCredential.getCredentialNo());
            return credentialUserPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 预览
     *
     * @param model 模型
     * @return {@link RestResponse}
     * @throws IOException ioexception
     */
    @PostMapping("/preview")
    public RestResponse preview(@RequestBody @Valid CredentialEditRequestVM model) throws IOException {

        File templateFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        File credentialFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        HttpUtil.downloadFile(model.getTemplateImagePath(), templateFile);

        List<ImageUtil.FontLocation> fontLocationList = model.getCredentialItemVMList().stream().map(item -> {
            ImageUtil.FontLocation fontLocation = new ImageUtil.FontLocation();
            fontLocation.setType(item.getType());
            fontLocation.setX(item.getElementX());
            fontLocation.setY(item.getElementY());
            fontLocation.setWeight(item.getFontWeight());
            fontLocation.setColor(item.getFontColor());
            fontLocation.setSize(item.getFontSize());
            CredentialItemFontEnum credentialItemFontEnum = CredentialItemFontEnum.fromCode(item.getType());
            switch (credentialItemFontEnum) {
                case NO:
                    fontLocation.setText("100-100");
                    break;
                case CREATE_TIME:
                    fontLocation.setText(String.format("%s", DateTimeUtil.dateChineseFormat(new Date())));
                    break;
                case VALIDITY:
                    fontLocation.setText(String.format("%s", ExamUtil.monToVM(item.getValidityMonth())));
                    break;
                case SEAL:
                    fontLocation.setText(model.getCompany());
                    break;
                default:
                    fontLocation.setText(item.getText());
                    break;
            }
            return fontLocation;
        }).collect(Collectors.toList());

        ImageUtil.pressText(templateFile, credentialFile, fontLocationList);
        String credentialUrl = fileUploadService.fileUpload(credentialFile.getAbsoluteFile(), credentialFile.getName(), "credential", true, false);
        return RestResponse.ok(credentialUrl);
    }


}
