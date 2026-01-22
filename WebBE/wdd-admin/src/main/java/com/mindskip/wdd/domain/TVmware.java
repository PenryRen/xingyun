package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 *
 * @TableName t_vmware
 */
@TableName(value ="t_vmware")
@Data
public class TVmware extends BasePage {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 虚拟机类型 主机 子机
     */
    private String vmType;

    /**
     * UUID
     */
    private String guid;

    /**
     * 虚拟机父ID
     */
    private String vmParentId;

    /**
     * 虚拟机名称
     */
    private String vmName;

    /**
     * 虚拟机密码
     */
    private String vmPassword;

    /**
     * ip
     */
    private String vmIp;

    /**
     * 虚拟机cpu
     */
    private Integer vmCpu;

    /**
     * 虚拟机内存
     */
    private String vmStorage;

    /**
     * 用户id
     */
    private String vmUserId;

    /**
     * 虚拟机路径
     */
    private String url;

    /**
     * 状态   00：Running 启动  01：shutdown 关机  02：Scheduled 启动中
     */
    private String status;

    /**
     * 分类   00：实训   01：考试
     */
    private String classes;

    /**
     * 创建时间
     */
    private String createTime;


    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 有效创建时间
     */
    private String validCreateTime;

    /**
     * 有效结束时间
     */
    private String validEndTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TVmware other = (TVmware) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getVmType() == null ? other.getVmType() == null : this.getVmType().equals(other.getVmType()))
                && (this.getGuid() == null ? other.getGuid() == null : this.getGuid().equals(other.getGuid()))
                && (this.getVmParentId() == null ? other.getVmParentId() == null : this.getVmParentId().equals(other.getVmParentId()))
                && (this.getVmName() == null ? other.getVmName() == null : this.getVmName().equals(other.getVmName()))
                && (this.getVmPassword() == null ? other.getVmPassword() == null : this.getVmPassword().equals(other.getVmPassword()))
                && (this.getVmIp() == null ? other.getVmIp() == null : this.getVmIp().equals(other.getVmIp()))
                && (this.getVmCpu() == null ? other.getVmCpu() == null : this.getVmCpu().equals(other.getVmCpu()))
                && (this.getVmStorage() == null ? other.getVmStorage() == null : this.getVmStorage().equals(other.getVmStorage()))
                && (this.getVmUserId() == null ? other.getVmUserId() == null : this.getVmUserId().equals(other.getVmUserId()))
                && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getClasses() == null ? other.getClasses() == null : this.getClasses().equals(other.getClasses()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
                && (this.getValidCreateTime() == null ? other.getValidCreateTime() == null : this.getValidCreateTime().equals(other.getValidCreateTime()))
                && (this.getValidEndTime() == null ? other.getValidEndTime() == null : this.getValidEndTime().equals(other.getValidEndTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getVmType() == null) ? 0 : getVmType().hashCode());
        result = prime * result + ((getGuid() == null) ? 0 : getGuid().hashCode());
        result = prime * result + ((getVmParentId() == null) ? 0 : getVmParentId().hashCode());
        result = prime * result + ((getVmName() == null) ? 0 : getVmName().hashCode());
        result = prime * result + ((getVmPassword() == null) ? 0 : getVmPassword().hashCode());
        result = prime * result + ((getVmIp() == null) ? 0 : getVmIp().hashCode());
        result = prime * result + ((getVmCpu() == null) ? 0 : getVmCpu().hashCode());
        result = prime * result + ((getVmStorage() == null) ? 0 : getVmStorage().hashCode());
        result = prime * result + ((getVmUserId() == null) ? 0 : getVmUserId().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getClasses() == null) ? 0 : getClasses().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getValidCreateTime() == null) ? 0 : getValidCreateTime().hashCode());
        result = prime * result + ((getValidEndTime() == null) ? 0 : getValidEndTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", vmType=").append(vmType);
        sb.append(", guid=").append(guid);
        sb.append(", vmParentId=").append(vmParentId);
        sb.append(", vmName=").append(vmName);
        sb.append(", vmPassword=").append(vmPassword);
        sb.append(", vmIp=").append(vmIp);
        sb.append(", vmCpu=").append(vmCpu);
        sb.append(", vmStorage=").append(vmStorage);
        sb.append(", vmUserId=").append(vmUserId);
        sb.append(", url=").append(url);
        sb.append(", status=").append(status);
        sb.append(", classes=").append(classes);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", validCreateTime=").append(validCreateTime);
        sb.append(", validEndTime=").append(validEndTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}