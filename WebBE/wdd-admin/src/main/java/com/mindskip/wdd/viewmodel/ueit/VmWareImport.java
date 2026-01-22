package com.mindskip.wdd.viewmodel.ueit;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;

@ColumnWidth(20)
public class VmWareImport {

    @ExcelProperty(value = "Id", index = 0)
    @NotBlank(message = "id不能为空")
    private Long id;

    /**
     * 虚拟机类型 主机 子机
     */
    @ExcelProperty(value = "虚拟机类型", index = 1)
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
     * 虚拟机路径
     */
    @ExcelProperty(value = "虚拟机路径", index = 11)
    private String url;

    /**
     * 状态   00：Running 启动  01：shutdown 关机  02：Scheduled 启动中
     */
    @ExcelProperty(value = "状态", index = 12)
    private String status;

    /**
     * 分类   00：实训   01：考试
     */
    @ExcelProperty(value = "分类", index = 15)
    private String classes;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 13)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    @ExcelProperty(value = "用户id", index = 14)
    private String vmUserId;

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

    /**
     * 是否导入成功
     */
    @ExcelIgnore
    private Boolean success;

    /**
     * 导入结果
     */
    @ExcelProperty(value = "导入结果", index = 16)
    private String result;

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

    public String getVmUserId() {
        return vmUserId;
    }

    public void setVmUserId(String vmUserId) {
        this.vmUserId = vmUserId;
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VmWareImport that = (VmWareImport) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(vmType, that.vmType) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(vmParentId, that.vmParentId) &&
                Objects.equals(vmName, that.vmName) &&
                Objects.equals(vmUsername, that.vmUsername) &&
                Objects.equals(vmPassword, that.vmPassword) &&
                Objects.equals(vmIp, that.vmIp) &&
                Objects.equals(vmCpu, that.vmCpu) &&
                Objects.equals(vmStorage, that.vmStorage) &&
                Objects.equals(diskName, that.diskName) &&
                Objects.equals(diskSize, that.diskSize) &&
                Objects.equals(url, that.url) &&
                Objects.equals(status, that.status) &&
                Objects.equals(classes, that.classes) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(vmUserId, that.vmUserId) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(updateUser, that.updateUser) &&
                Objects.equals(validCreateTime, that.validCreateTime) &&
                Objects.equals(validEndTime, that.validEndTime) &&
                Objects.equals(disabled, that.disabled) &&
                Objects.equals(examPaperId, that.examPaperId) &&
                Objects.equals(deleted, that.deleted) &&
                Objects.equals(success, that.success) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vmType, guid, vmParentId, vmName, vmUsername, vmPassword, vmIp, vmCpu, vmStorage, diskName, diskSize, url, status, classes, createTime, vmUserId, createUser, updateTime, updateUser, validCreateTime, validEndTime, disabled, examPaperId, deleted, success, result);
    }

    @Override
    public String toString() {
        return "VmWareImport{" +
                "id=" + id +
                ", vmType='" + vmType + '\'' +
                ", guid='" + guid + '\'' +
                ", vmParentId='" + vmParentId + '\'' +
                ", vmName='" + vmName + '\'' +
                ", vmUsername='" + vmUsername + '\'' +
                ", vmPassword='" + vmPassword + '\'' +
                ", vmIp='" + vmIp + '\'' +
                ", vmCpu=" + vmCpu +
                ", vmStorage='" + vmStorage + '\'' +
                ", diskName='" + diskName + '\'' +
                ", diskSize='" + diskSize + '\'' +
                ", url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", classes='" + classes + '\'' +
                ", createTime=" + createTime +
                ", vmUserId='" + vmUserId + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateTime=" + updateTime +
                ", updateUser='" + updateUser + '\'' +
                ", validCreateTime=" + validCreateTime +
                ", validEndTime=" + validEndTime +
                ", disabled=" + disabled +
                ", examPaperId=" + examPaperId +
                ", deleted=" + deleted +
                ", success=" + success +
                ", result='" + result + '\'' +
                '}';
    }
}
