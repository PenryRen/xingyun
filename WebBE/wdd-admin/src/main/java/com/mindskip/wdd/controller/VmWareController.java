package com.mindskip.wdd.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.context.WebContext;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.allocation;
import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.listener.VmWareListener;
import com.mindskip.wdd.mapping.VmWareMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.service.impl.VmWareServiceImpl;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.ueit.VmWareImport;
import com.mindskip.wdd.viewmodel.ueit.VmWarePageRequestVM;
import com.mindskip.wdd.viewmodel.ueit.VmWarePageResponseVM;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 9.5.0
 * @description: 虚拟机管理
 * Copyright (C), 2025, 麟航团队
 * @date 2025/09/11 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/vmWare")
public class VmWareController {
    private static final Logger logger = LoggerFactory.getLogger(VmWareController.class);
    private final TVmwareService tVmwareService;
    private static RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private final VmWareService vmWareService;

    private final VmWareMapping vmWareMapping;

    private final SystemService systemService;

    private final BeanValidator beanValidator;

    private final FileUploadService fileUploadService;
    @Autowired
    protected WebContext webContext;
    /**
     * 获取当前用户
     *
     * @return the current user
     */
    protected User getCurrentUser() {
        return webContext.getCurrentUser();
    }

    @PostMapping("/page")
    @PreAuthorize("vmWare:page")
    public RestResponse<PageInfo<VmWare>> page(@RequestBody @Valid VmWarePageRequestVM requestVM) {
        return RestResponse.ok(vmWareService.page(requestVM));
    }

    /**
     * 按主键查询虚拟机（用于管理端编辑页）
     */
    @PostMapping("/select/{id}")
    @PreAuthorize({"vmWare:page", "vmWare:import"})
    public RestResponse selectById(@PathVariable("id") Long id) {
        VmWare row = vmWareService.getById(id);
        if (row == null) {
            return RestResponse.fail(500, "记录不存在");
        }
        return RestResponse.ok(row);
    }

    /**
     * 更新主机模板行（无影场景下 url 字段存云电脑镜像 BundleId）。子机由云端按需创建，不允许在此修改。
     */
    @PostMapping("/edit")
    @PreAuthorize({"vmWare:page", "vmWare:import"})
    public RestResponse vmWareEdit(@RequestBody VmWare incoming) {
        if (incoming == null || incoming.getId() == null) {
            return RestResponse.fail(500, "参数错误");
        }
        VmWare existing = vmWareService.getById(incoming.getId());
        if (existing == null) {
            return RestResponse.fail(500, "记录不存在");
        }
        if (!"00".equals(existing.getVmType()) || StringUtils.isNotEmpty(existing.getVmParentId())) {
            return RestResponse.fail(500, "仅可编辑主机模板（类型为主机且无父级），子机由云端按需创建，请勿在此修改");
        }
        existing.setVmName(incoming.getVmName());
        existing.setVmPassword(incoming.getVmPassword());
        existing.setVmUsername(incoming.getVmUsername());
        existing.setVmCpu(incoming.getVmCpu());
        existing.setVmStorage(incoming.getVmStorage());
        existing.setDiskName(incoming.getDiskName());
        existing.setDiskSize(incoming.getDiskSize());
        existing.setClasses(incoming.getClasses());
        existing.setUrl(incoming.getUrl());
        existing.setVmIp(incoming.getVmIp());
        existing.setValidCreateTime(incoming.getValidCreateTime());
        existing.setValidEndTime(incoming.getValidEndTime());
        existing.setDisabled(incoming.getDisabled());
        existing.setUpdateUser(getCurrentUser().getUserName());
        vmWareService.updateById(existing);
        return RestResponse.okMessage("保存成功");
    }

    /**
     * 分页查询虚拟机列表，不要主机
     * @param requestVM
     * @return
     */
    public RestResponse<PageInfo<VmWare>> page1(@RequestBody @Valid VmWarePageRequestVM requestVM) {
        return RestResponse.ok(vmWareService.page1(requestVM));
    }
    /**
     * 虚拟机关机
     *
     * @param
     * @return the rest response
     */
    @PostMapping("/shutdown")
    @PreAuthorize("vmWare:shutdown")
    public RestResponse shutdown(@RequestBody VmWare vmWare) {
        return vmWareService.shutdown(vmWare);
    }

    /**
     * 虚拟机开机
     *
     * @param
     * @return the rest response
     */
    @PostMapping("/start")
    @PreAuthorize("vmWare:start")
    public RestResponse start(@RequestBody VmWare vmWare) {
        return vmWareService.start(vmWare);
    }

    /**
     * 虚拟机重启
     *
     * @param
     * @return the rest response
     */
    @PostMapping("/reStart")
    @PreAuthorize("vmWare:reStart")
    public RestResponse reStart(@RequestBody VmWare vmWare) {
        return vmWareService.reStart(vmWare);
    }

    /**
     * 克隆虚拟机
     *
     * @param vmWare 虚拟机
     */
    @PostMapping("/clone")
    @PreAuthorize("vmWare:clone")
    public RestResponse clone(@RequestBody VmWare vmWare) {
        return vmWareService.clone(vmWare);
    }
    /**
     * 还能克隆虚拟机的台数返回前端
     */
    @PostMapping("/cloneNumber")
    @PreAuthorize("vmWare:cloneNumber")
    public RestResponse cloneNumber() {
        return vmWareService.cloneNumber();
    }
    /**
     * 导入虚拟机excel
     *
     * @param request the request
     * @return the rest response
     * @throws IOException the io exception
     */
    @RequestMapping("/import")
    @ResponseBody
    @PreAuthorize("vmWare:import")
    public RestResponse vmWareUploadAndReadExcel(HttpServletRequest request) throws IOException {
        //通过request获取上传的文件，并将其转换为MultipartFile对象
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        //创建一个空的List<VmWareImport>对象用于存储导入的数据
        List<VmWareImport> vmWareList = new ArrayList<>();
        //使用EasyExcel库的read方法，将上传文件的输入流、VmWareImport类和自定义的VmWareListener（用于处理数据导入）传递给EasyExcel.read方法，实现对上传文件的读取和解析,导入逻辑
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(), VmWareImport.class, new VmWareListener(vmWareList, vmWareService));
        read.sheet().doRead();
        //返回错误的数据行
        List<VmWareImport> fairVMList = vmWareList.stream().filter(u -> !u.getSuccess()).collect(Collectors.toList());
        File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
        EasyExcel.write(excelTemp, VmWareImport.class).sheet("虚拟机").doWrite(fairVMList);
        String filePath = fileUploadService.fileUpload(excelTemp, String.format("虚拟机导入结果 - %s.xlsx", DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
        return RestResponse.ok(filePath);
    }
    /**
     * 导出虚拟机excel
     *
     * @param model
     * @return
     * @throws IOException
     */
    @PostMapping("/export")
    @ResponseBody
    @PreAuthorize("vmWare:export")
    public RestResponse export(@RequestBody VmWarePageRequestVM model) throws IOException {
        // 设置分页参数，将页码设置为1，将每页大小设置为最大值，目的是获取全部数据
        model.setPageIndex(1);
        model.setPageSize(Integer.MAX_VALUE);
        // 获取分页数据，并将结果封装到RestResponse对象中
        RestResponse<PageInfo<VmWare>> vmWarePage = page1(model);
        // 创建一个临时文件，用于存储导出的Excel数据
        File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
        // 创建Excel单元格样式，设置标题单元格样式为默认样式，设置内容单元格样式为左对齐样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 创建单元格样式策略对象，将标题和内容单元格样式传入
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 使用EasyExcel库进行Excel写操作，将数据写入Excel文件
        EasyExcel.write(excelTemp, VmWarePageResponseVM.class).sheet("虚拟机列表").registerWriteHandler(horizontalCellStyleStrategy).doWrite(vmWarePage.getResponse().getList());
        // 调用文件上传服务将导出的Excel文件上传，获取上传后的文件路径
        String filePath = fileUploadService.fileUpload(excelTemp, String.format("虚拟机列表导出 - %s.xlsx", DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
        // 返回一个RestResponse对象，其中包含导出的Excel文件的路径
        return RestResponse.ok(filePath);
    }

    /**
     * 分配用户接口
     * @param vmWare
     * @return
     */
    @PostMapping("/distribute")
    @PreAuthorize("vmWare:distribute")
    public RestResponse distribute(@RequestBody VmWare vmWare) {
        if (ObjectUtil.isEmpty(vmWare)) {
            return RestResponse.fail(500, "要分配的机器为空");
        }
        if (StringUtils.isEmpty(vmWare.getVmUserId())) {
            return RestResponse.fail(500, "用户id为空");
        }
        return vmWareService.distribute(vmWare);
    }

    /**
     * 异步释放虚拟机
     *
     * @param
     * @return the rest response
     */
    @PostMapping("/release")
    @PreAuthorize("vmWare:release")
    public RestResponse release(@RequestBody VmWare vmWare) {
        vmWareService.releaseVmWare(vmWare);
        return RestResponse.okMessage("释放成功");
    }

    /**
     * 获取实训环境列表
     *
     * @return 结果
     */
    @PostMapping("/vmTypeList")
    public RestResponse<List<VmWareVM>> vmTypeList() {
        //查询当前没有绑定用户的虚拟机
        List<VmWare> list = vmWareService.vmTypeList();
        List<VmWareVM> vmList = list.stream().map(vmWareMapping::toVmWareVM).collect(Collectors.toList());
        return RestResponse.ok(vmList);
    }

    /**
     * 查询用户是否存在正在使用的虚拟化
     *
     * @param
     * @return the rest response
     */
    @PostMapping("/wareSelectOne")
//    @PreAuthorize("user:employee:delete")
    public RestResponse wareSelectOne(@RequestBody allocation allocation) {
        if (ObjectUtils.isEmpty(allocation.getUserName())) {
            return RestResponse.fail(500, null);
        }
        String uid = tVmwareService.userByName(allocation.getUserName());  //查找用户id
        VmWare selectexam = tVmwareService.selectexam(uid, "00");
        if (ObjectUtils.isNotEmpty(selectexam)) {
            return RestResponse.ok(tVmwareService.selectexam(uid, "00"));
        }
        return RestResponse.ok(null);
    }


    /**
     * 检查授权过期时间
     * 使用拦截器进行校验
     *
     * @return 结果
     */
    @PostMapping("/refreshExpiration")
    public RestResponse refreshExpiration() {
        return RestResponse.ok();
    }

    /**
     * 获取授权过期时间
     *
     * @return 结果
     */
    @PostMapping("/getExpiration")
    public RestResponse getExpiration() {
        return vmWareService.getExpiration();
    }

    /**
     * 加密
     *
     * @return 结果
     */
    @PostMapping("/pairOneEncode")
    public RestResponse pairOneEncode(@RequestBody String content) {
        return RestResponse.ok(systemService.pairOneEncode(content));
    }

    /**
     * 加密
     *
     * @return 结果
     */
    @PostMapping("/pairOneDecode")
    public RestResponse pairOneDecode(@RequestBody String content) {
        return RestResponse.ok(systemService.pairOneDecode(content));
    }

    /**
     * 测试接口
     * @return
     */
    @PostMapping("/test")
    public RestResponse test(@RequestParam String str) {
        System.out.println("============================" + str);
        return RestResponse.ok(str);
    }
}
