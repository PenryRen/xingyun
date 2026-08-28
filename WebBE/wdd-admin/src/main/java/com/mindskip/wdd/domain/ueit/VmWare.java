package com.mindskip.wdd.domain.ueit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 虚拟机 t_vmware
 *
 * @author libl
 * @date 2025-04-07
 */
@Data
@TableName(value = "t_vmware")
public class VmWare implements Serializable{

    private static final long serialVersionUID = 8308822592705609079L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 虚拟机账户名
     */
    private String vmUsername;

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
     * 硬盘名称
     */
    private String diskName;

    /**
     * 硬盘大小
     */
    private String diskSize;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;


    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 有效创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date validCreateTime;

    /**
     * 有效结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date validEndTime;

    /**
     * 不可分配
     */
    private Boolean disabled;

    /**
     * 试卷Id
     */
    private Long examPaperId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 克隆机器的台数
     */
    @TableField(exist = false)
    private Integer number;
    /**
     * 用户真实姓名
     */
    @TableField(exist = false)
    private  String realName;
    public VmWare() {
    }

    public VmWare(String guid, String vmName, String vmUserId, String classes) {
        this.guid = guid;
        this.vmName = vmName;
        this.vmUserId = vmUserId;
        this.classes = classes;
    }

    public static VmWare getByVmName(String vmName) {
        return new VmWare(null, vmName, null, null);
    }

    public static VmWare getByVmUserId(String vmUserId) {
        return new VmWare(null, null, vmUserId, null);
    }

}
