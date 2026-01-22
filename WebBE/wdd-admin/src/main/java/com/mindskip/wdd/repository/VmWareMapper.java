package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.ueit.VmWare;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 虚拟机 Mapper接口
 *
 * @author libl
 * @date 2024-04-08
 */
@Mapper
public interface VmWareMapper extends BaseMapper<VmWare> {
    /**
     * 根据id查询
     *
     * @param id the id
     * @return the user by id
     */
    VmWare getVmWareById(Long id);
    /**
     * 获取实训环境列表
     *
     * @return 结果
     */
    public List<VmWare> vmTypeList();

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
     * 修改UserId
     * @param vmWare
     * @return
     */
    public int updateVmWareUserId(VmWare vmWare);
    /**
     * 删除虚拟机
     *
     * @param id 虚拟机主键
     * @return 结果
     */
    public int deleteVmWareById(Long id);

    /**
     * 批量删除虚拟机
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVmWareByIds(Long[] ids);

    /**
     * 根据虚拟机父ID查询已经克隆的数量
     * @param vmParentId
     * @return
     */
    public int selectCloneCount(String vmParentId);

    /**
     * 查询虚拟机列表-->包含用户真实姓名字段
     * @param requestVMToVmWare
     * @return
     */
    List<VmWare> selectVmWareList1(VmWare requestVMToVmWare);

    /**
     * 查询虚拟机列表-->包含用户真实姓名字段，不包含主机
     * @param requestVMToVmWare
     * @return
     */
    List<VmWare> selectVmWareList2(VmWare requestVMToVmWare);

    /**
     * 修改虚拟机的用户id和分类
     * @param vmWare
     * @return
     */
    int updateVmWareUserIdAndClasses(VmWare vmWare);
}
