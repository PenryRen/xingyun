package com.mindskip.wdd.viewmodel.ueit;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mindskip.wdd.viewmodel.convert.StatusConverter;
import com.mindskip.wdd.viewmodel.convert.VmTypeConverter;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 虚拟机分页返回
 * Copyright (C), 2024, 麒技团队
 *
 */
@ColumnWidth(20)
public class VmWarePageResponseVM {

    @ExcelProperty(value = "Id", index = 0)
    private Long id;

    /**
     * 虚拟机类型 主机 子机
     */
    @ExcelProperty(value = "虚拟机类型", index = 1, converter = VmTypeConverter.class)
    private String vmType;

    /**
     * UUID
     */
    @ExcelIgnore
    private String guid;

    /**
     * 虚拟机父ID
     */
    @ExcelProperty(value = "虚拟机父ID", index = 2)
    private String vmParentId;

    /**
     * 虚拟机名称
     */
    @ExcelProperty(value = "虚拟机名称", index = 3)
    private String vmName;

    /**
     * 虚拟机账户名
     */
    @ExcelProperty(value = "虚拟机账户名", index = 4)
    private String vmUsername;

    /**
     * 虚拟机密码
     */
    @ExcelProperty(value = "虚拟机密码", index = 5)
    private String vmPassword;

    /**
     * ip
     */
    @ExcelProperty(value = "ip", index = 6)
    private String vmIp;

    /**
     * 虚拟机cpu
     */
    @ExcelProperty(value = "虚拟机cpu", index = 7)
    private Integer vmCpu;

    /**
     * 虚拟机内存
     */
    @ExcelProperty(value = "虚拟机内存", index = 8)
    private String vmStorage;

    /**
     * 硬盘名称
     */
    @ExcelProperty(value = "硬盘名称", index = 9)
    private String diskName;

    /**
     * 硬盘大小
     */
    @ExcelProperty(value = "硬盘大小", index = 10)
    private String diskSize;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id", index = 14)
    private String vmUserId;
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 15)
    private String realName;

    /**
     * 虚拟机路径
     */
    @ExcelProperty(value = "虚拟机路径", index = 11)
    private String url;

    /**
     * 状态   00：Running 启动  01：shutdown 关机  02：Scheduled 启动中
     */
    @ExcelProperty(value = "状态", index = 12, converter = StatusConverter.class)
    private String status;

    /**
     * 分类   00：实训   01：考试
     */
    @ExcelProperty(value = "分类", index = 16)
    private String classes;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 13)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;


    /**
     * 创建人
     */
    @ExcelIgnore
    private String createUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ExcelIgnore
    private Date updateTime;

    /**
     * 修改人
     */
    @ExcelIgnore
    private String updateUser;

    /**
     * 有效创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ExcelIgnore
    private Date validCreateTime;

    /**
     * 有效结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ExcelIgnore
    private Date validEndTime;

    /**
     * 不可分配
     */
    @ExcelIgnore
    private Boolean disabled;

    /**
     * 试卷Id
     */
    @ExcelIgnore
    private Long examPaperId;

    /**
     * 是否删除
     */
    @ExcelIgnore
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVmType() {
        return vmType;
    }

    public void setVmType(String vmType) {
        this.vmType = vmType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getVmParentId() {
        return vmParentId;
    }

    public void setVmParentId(String vmParentId) {
        this.vmParentId = vmParentId;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getVmUsername() {
        return vmUsername;
    }

    public void setVmUsername(String vmUsername) {
        this.vmUsername = vmUsername;
    }

    public String getVmPassword() {
        return vmPassword;
    }

    public void setVmPassword(String vmPassword) {
        this.vmPassword = vmPassword;
    }

    public String getVmIp() {
        return vmIp;
    }

    public void setVmIp(String vmIp) {
        this.vmIp = vmIp;
    }

    public Integer getVmCpu() {
        return vmCpu;
    }

    public void setVmCpu(Integer vmCpu) {
        this.vmCpu = vmCpu;
    }

    public String getVmStorage() {
        return vmStorage;
    }

    public void setVmStorage(String vmStorage) {
        this.vmStorage = vmStorage;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
    }

    public String getVmUserId() {
        return vmUserId;
    }

    public void setVmUserId(String vmUserId) {
        this.vmUserId = vmUserId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getValidCreateTime() {
        return validCreateTime;
    }

    public void setValidCreateTime(Date validCreateTime) {
        this.validCreateTime = validCreateTime;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Long examPaperId) {
        this.examPaperId = examPaperId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
