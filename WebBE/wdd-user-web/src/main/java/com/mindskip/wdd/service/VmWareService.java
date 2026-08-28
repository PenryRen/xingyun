package com.mindskip.wdd.service;

import cn.hutool.core.date.DateTime;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 虚拟机 Service接口
 *
 * @author libl
 * @date 2025-04-07
 */
public interface VmWareService {

    /**
     * 获取实训环境列表
     *
     * @return 结果
     */
    public List<VmWare> vmTypeList();

    /**
     * 获取授权过期时间
     *
     * @return 结果
     */
    public RestResponse getExpiration();

    /**
     * 查询虚拟机列表
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    public List<VmWare> selectVmWareList(VmWare vmWare);

    /**
     * 查询过期虚拟机列表
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    public List<VmWare> selectExpiredVmWareList(VmWare vmWare);

    /**
     * 查询虚拟机(子机)
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    public VmWare selectVmWare(VmWare vmWare);

    /**
     * 查询用户已分配的虚拟机 disabled不为1
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    public VmWare selectBindVmWareEnabled(VmWare vmWare);

    /**
     * 根据虚拟机父ID查询未绑定的虚拟机(子机)
     *
     * @param vmParentId 虚拟机父ID
     * @return 虚拟机
     */
    public VmWare selectUnBindVmWareByParentId(String vmParentId);

    /**
     * 根据guid查询虚拟机(主机)
     *
     * @param guid 虚拟机guid
     * @return 虚拟机
     */
    public VmWare selectParentVmWareByGuid(String guid);

    /**
     * 创建虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    public int insertVmWare(VmWare vmWare);

    /**
     * 修改虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    public int updateVmWare(VmWare vmWare);

    /**
     * 关闭虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    public RestResponse shutdown(VmWare vmWare);

    /**
     * 开启虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    public RestResponse start(VmWare vmWare);

    /**
     * 重启虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    public RestResponse reStart(VmWare vmWare);

    /**
     * 检查虚拟机分配
     * 类型 00实训 01考试
     *
     * @param query 虚拟机参数
     * @return 结果
     */
    public RestResponse checkVmWare(User currentUser, VmWareVM query);

    /**
     * 分配虚拟机
     * 类型 00实训 01考试
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    public RestResponse getVmWare(User currentUser, VmWareVM query);

    /**
     * 异步克隆虚拟机
     *
     * @param userUuid 用户Uuid
     * @param query    虚拟机参数
     */
    @Async
    public void cloneVmWare(String userUuid, VmWareVM query);

    /**
     * 释放虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    public int releaseVmWareSync(VmWare vmWare);

    /**
     * 异步释放虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Async
    public void releaseVmWare(VmWare vmWare);

    /**
     * 查询虚拟机剩余时间(实训)
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    public RestResponse queryRemainingTime(User currentUser, VmWareVM query);

    /**
     * 虚拟机续期(实训)
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    public RestResponse renewal(User currentUser, VmWareVM query);

    /**
     * 实训虚拟机过期时间
     *
     * @return 分钟
     */
    public DateTime getExpiredValidEndTime();
}
