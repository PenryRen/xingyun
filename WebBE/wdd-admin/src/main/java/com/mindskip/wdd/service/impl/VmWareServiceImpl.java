package com.mindskip.wdd.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.ueit.VmWareApiConfig;
import com.mindskip.wdd.configuration.ueit.VmWareConfigKey;
import com.mindskip.wdd.constant.CacheConstants;
import com.mindskip.wdd.constant.HttpStatus;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ueit.VmClassesEnum;
import com.mindskip.wdd.domain.enums.ueit.VmStatusEnum;
import com.mindskip.wdd.domain.enums.ueit.VmTypeEnum;
import com.mindskip.wdd.domain.ueit.*;
import com.mindskip.wdd.mapping.VmWareMapping;
import com.mindskip.wdd.repository.UserMapper;
import com.mindskip.wdd.repository.VmWareMapper;
import com.mindskip.wdd.service.SysConfigService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.VmWareService;
import com.mindskip.wdd.utility.ueit.HttpUtils;
import com.mindskip.wdd.viewmodel.ueit.VmWareImport;
import com.mindskip.wdd.viewmodel.ueit.VmWarePageRequestVM;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 虚拟机 Service业务层处理
 *
 * @author libl
 * @date 2025-04-08
 */
@Service
@AllArgsConstructor
public class VmWareServiceImpl extends ServiceImpl<VmWareMapper, VmWare> implements VmWareService {

    private static final Logger logger = LoggerFactory.getLogger(VmWareServiceImpl.class);

    private final VmWareMapper vmWareMapper;

    private final VmWareMapping vmWareMapping;

    private final SysConfigService sysConfigService;

    private final VmWareConfigKey vmWareConfigKey;

    private final VmWareApiConfig vmWareApiConfig;

    private final SystemService systemService;

    private final UserMapper userMapper;
    /**
     * 获取实训环境列表
     *
     * @return 结果
     */
    @Override
    public List<VmWare> vmTypeList() {
        return vmWareMapper.vmTypeList();
    }

    /**
     * 获取授权过期时间
     *
     * @return 结果
     */
    @Override
    public RestResponse getExpiration() {
        String expirationConfigKey = vmWareConfigKey.getExpirationConfigKey();
        if (StringUtils.isNotEmpty(expirationConfigKey)) {
            String encryptStr = sysConfigService.selectConfigByKey(expirationConfigKey);
            if (StringUtils.isNotEmpty(encryptStr)) {
                return RestResponse.ok(systemService.pairOneDecode(encryptStr));
            }
        }
        return RestResponse.ok();
    }

    /**
     * 分页查询虚拟机列表
     *
     * @param requestVM 虚拟机参数
     * @return 虚拟机列表分页
     */
    @Override
    public PageInfo<VmWare> page(VmWarePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                vmWareMapper.selectVmWareList1(vmWareMapping.requestVMToVmWare(requestVM))
        );
    }

    /**
     * 分页查询虚拟机列表
     *
     * @param requestVM 虚拟机参数
     * @return 虚拟机列表分页
     */
    @Override
    public PageInfo<VmWare> page1(VmWarePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                vmWareMapper.selectVmWareList2(vmWareMapping.requestVMToVmWare(requestVM))
        );
    }
    /**
     * 查询虚拟机列表
     *
     * @param vmWare 虚拟机
     * @return 虚拟机列表
     */
    @Override
    public List<VmWare> selectVmWareList(VmWare vmWare) {
        return vmWareMapper.selectVmWareList(vmWare);
    }

    /**
     * 查询过期虚拟机列表
     *
     * @param vmWare 虚拟机
     * @return 虚拟机列表
     */
    @Override
    public List<VmWare> selectExpiredVmWareList(VmWare vmWare) {
        return vmWareMapper.selectExpiredVmWareList(vmWare);
    }

    /**
     * 查询虚拟机(子机)
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    @Override
    public VmWare selectVmWare(VmWare vmWare) {
        return vmWareMapper.selectVmWare(vmWare);
    }

    /**
     * 查询用户已分配的虚拟机 disabled不为1
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    @Override
    public VmWare selectBindVmWareEnabled(VmWare vmWare) {
        return vmWareMapper.selectBindVmWareEnabled(vmWare);
    }

    /**
     * 根据虚拟机父ID查询未绑定的虚拟机(子机)
     *
     * @param vmParentId 虚拟机父ID
     * @return 虚拟机
     */
    @Override
    public VmWare selectUnBindVmWareByParentId(String vmParentId) {
        return vmWareMapper.selectUnBindVmWareByParentId(vmParentId);
    }

    /**
     * 根据guid查询虚拟机(主机)
     *
     * @param guid 虚拟机guid
     * @return 虚拟机
     */
    @Override
    public VmWare selectParentVmWareByGuid(String guid) {
        return vmWareMapper.selectParentVmWareByGuid(guid);
    }

    /**
     * 创建虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public int insertVmWare(VmWare vmWare) {
        return vmWareMapper.insertVmWare(vmWare);
    }


    /**
     * 修改虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public int updateVmWare(VmWare vmWare) {
        vmWare.setUpdateTime(new Date());
        return vmWareMapper.updateVmWare(vmWare);
    }

    /**
     * 关闭虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public RestResponse shutdown(VmWare vmWare) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vmName", vmWare.getVmName());
            params.put("forseRestart", "true");

            HttpResponse httpResponse = HttpUtils.sendPut(vmWareApiConfig.getShutdown(), JSONObject.toJSONString(params));
            String body = httpResponse.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                logger.info("关闭虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                VmWare update = new VmWare();
                update.setId(vmWare.getId());
                update.setStatus(VmStatusEnum.Shutdown.getCode());
                this.updateVmWare(update);
                return RestResponse.okMessage("关机成功");
            }
        } catch (Exception e) {
            logger.error("关闭虚拟机,业务异常----------------------", e);
        }
        return RestResponse.failMessage("关机失败");
    }

    /**
     * 开启虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public RestResponse start(VmWare vmWare) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vmName", vmWare.getVmName());
            params.put("startPaused", "true");

            HttpResponse httpResponse = HttpUtils.sendPut(vmWareApiConfig.getStart(), JSONObject.toJSONString(params));
            String body = httpResponse.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                logger.info("开启虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                VmWare update = new VmWare();
                update.setId(vmWare.getId());
                update.setStatus(VmStatusEnum.Running.getCode());
                this.updateVmWare(update);
                return RestResponse.okMessage("开机成功");
            }
        } catch (Exception e) {
            logger.error("开启虚拟机,业务异常----------------------", e);
        }
        return RestResponse.failMessage("开机失败");
    }

    /**
     * 重启虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public RestResponse reStart(VmWare vmWare) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vmName", vmWare.getVmName());
            params.put("forseRestart", "true");

            HttpResponse httpResponse = HttpUtils.sendPut(vmWareApiConfig.getReStart(), JSONObject.toJSONString(params));
            String body = httpResponse.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                logger.info("重启虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                VmWare update = new VmWare();
                update.setId(vmWare.getId());
                update.setStatus(VmStatusEnum.Restart.getCode());
                this.updateVmWare(update);
                return RestResponse.okMessage("重启成功");
            }
        } catch (Exception e) {
            logger.error("重启虚拟机,业务异常----------------------", e);
        }
        return RestResponse.failMessage("重启失败");
    }

    /**
     * 克隆虚拟机
     *
     * @param query 虚拟机参数
     */
//    @Override
//    public RestResponse clone(VmWare query) {
//        //核验实训机器台数限制
//        String encryptStr = sysConfigService.selectConfigByKey(vmWareConfigKey.getMaxCountConfigKey());
//        if (StringUtils.isEmpty(encryptStr)) {
//            return RestResponse.failMessage("可创建的实训机器达到上限");
//        }
//        String maxCount = systemService.pairOneDecode(encryptStr);
//        int count = vmWareMapper.selectCount(null).intValue();
//        if (count >= Integer.parseInt(maxCount)) {
//            return RestResponse.failMessage("可创建的实训机器达到上限");
//        }
//        cloneVmWare(null, vmWareMapping.toVmWareVM(query));
//        return RestResponse.okMessage("克隆成功");
//    }
    @Override
    public RestResponse clone(VmWare query) {
        // 获取克隆的数量
        Integer number = query.getNumber();
        // 判空操作
        if (ObjectUtils.isEmpty(number)) {
            logger.error("您填写的克隆数量为空！");
            return RestResponse.fail(500, "您填写的克隆数量为空！");
        }
        // 循环number次，每次克隆一台机器
        for (int i = 1; i <= number; i++) {
            cloneVmWare(vmWareMapping.toVmWareVM(query));
        }
        return RestResponse.okMessage("克隆成功");
    }
    /**
     * 异步克隆虚拟机
     *
     * @param query    虚拟机参数
     */
//    @Async
//    @Override
//    public void cloneVmWare(String userUuid, VmWareVM query) {
//        try {
//            String guid = query.getGuid();
//            //根据guid查询虚拟机(主机)
//            VmWare parentVmWare = vmWareMapper.selectParentVmWareByGuid(guid);
//            if (ObjectUtils.isEmpty(parentVmWare)) {
//                logger.error("异步克隆虚拟机,获取主机信息失败");
//                return;
//            }
//            String childGuid = UUID.randomUUID().toString();
//            String nowStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
//            String childVmName = nowStr + "-" + parentVmWare.getVmName() + "-" + childGuid;
//
//            //获取配置信息
//            String rootConfigKey = vmWareConfigKey.getRootConfigKey();
//            String diskConfigKey = vmWareConfigKey.getDiskConfigKey();
//            if (StringUtils.isNotEmpty(rootConfigKey) && StringUtils.isNotEmpty(diskConfigKey)) {
//                String rootConfig = sysConfigService.selectConfigByKey(rootConfigKey);
//                String diskConfig = sysConfigService.selectConfigByKey(diskConfigKey);
//                if (StringUtils.isNotEmpty(rootConfig) && StringUtils.isNotEmpty(diskConfig)) {
//                    Root root = JSONObject.parseObject(rootConfig, Root.class);
//                    Disk disk = JSONObject.parseObject(diskConfig, Disk.class);
//                    boolean checked = checkVmConfig(root, disk);
//                    if (checked) {
//                        //封装请求参数
//                        root.setName(childVmName);
//                        root.setDataSource(parentVmWare.getDiskName());
//                        disk.setName(childVmName);
//                        List<Disk> diskList = new ArrayList<>();
//                        diskList.add(disk);
//                        root.setDisk(diskList);
//                        logger.info("异步克隆虚拟机,开始----------------------");
//                        HttpResponse httpResponse = HttpUtils.sendPost(vmWareApiConfig.getClone(), JSONObject.toJSONString(root));
//                        if (httpResponse.isOk()) {
//                            String body = httpResponse.body();
//                            JSONObject jsonObject = JSONObject.parseObject(body);
//                            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
//                                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
//                                logger.info("异步克隆虚拟机,接口返回成功信息:[{}]", httpResponse.body());
//                                //克隆成功保存虚拟化
//                                VmWareClone vmWareClone = vmWareMapping.toVmWareClone(parentVmWare);
//                                VmWare insert = vmWareMapping.toVmWare(vmWareClone);
//                                insert.setVmType(VmTypeEnum.Child.getCode());
//                                insert.setGuid(childGuid);
//                                insert.setVmParentId(guid);
//                                insert.setVmName(childVmName);
//                                insert.setVmCpu(root.getCpu());
//                                insert.setVmStorage(root.getMem());
//                                insert.setDiskName(disk.getName());
//                                insert.setDiskSize(disk.getSize());
//                                insert.setVmUserId(userUuid);
//                                insert.setStatus(VmStatusEnum.Init.getCode());
//                                insert.setClasses(query.getClasses());
//                                insert.setCreateTime(new Date());
//                                insert.setDisabled(false);
//                                insert.setExamPaperId(query.getExamPaperId());
//                                insert.setDeleted(false);
//                                //保存
//                                vmWareMapper.insertVmWare(insert);
//                            } else {
//                                logger.error("异步克隆虚拟机,接口返回失败信息:[{}]", httpResponse.body());
//                            }
//                        } else {
//                            logger.error("异步克隆虚拟机,接口请求失败:[{}]", httpResponse.body());
//                        }
//                    }
//                } else {
//                    logger.error("异步克隆虚拟机,获取虚拟机配置信息失败");
//                }
//            } else {
//                logger.error("异步克隆虚拟机,获取虚拟机配置键名失败");
//            }
//        } catch (Exception e) {
//            logger.error("异步克隆虚拟机,业务异常----------------------", e);
//        }
//    }
    @Async
    @Override
    public void cloneVmWare(VmWareVM query) {
        try {
            // 获取该台机器的guid 即 uuid
            String guid = query.getGuid();
            // 根据guid查询虚拟机(主机)
            VmWare parentVmWare = vmWareMapper.selectParentVmWareByGuid(guid);
            // 生成一个随机的子虚拟机guid
            String childGuid = UUID.randomUUID().toString();
            String nowStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
            // 根据当前时间生成子虚拟机的名称
            String childVmName = nowStr + "-" + parentVmWare.getVmName() + "-" + childGuid;

            //获取配置信息,包括根配置和磁盘配置
            String rootConfigKey = vmWareConfigKey.getRootConfigKey();
            String diskConfigKey = vmWareConfigKey.getDiskConfigKey();
            // 判空操作---根配置和磁盘配置
            if (StringUtils.isNotEmpty(rootConfigKey) && StringUtils.isNotEmpty(diskConfigKey)) {
                String rootConfig = sysConfigService.selectConfigByKey(rootConfigKey);
                String diskConfig = sysConfigService.selectConfigByKey(diskConfigKey);
                if (StringUtils.isNotEmpty(rootConfig) && StringUtils.isNotEmpty(diskConfig)) {
                    // 解析配置信息并进行配置检查(虚拟机基础信息、虚拟机硬盘信息)
                    Root root = JSONObject.parseObject(rootConfig, Root.class);
                    Disk disk = JSONObject.parseObject(diskConfig, Disk.class);
                    // 检验虚拟机基础信息、虚拟机硬盘信息
                    boolean checked = checkVmConfig(root, disk);
                    if (checked) {
                        //封装请求参数，包括子虚拟机名称、数据源、磁盘
                        root.setName(childVmName);
                        root.setDataSource(parentVmWare.getDiskName());
                        disk.setName(childVmName);
                        List<Disk> diskList = new ArrayList<>();
                        diskList.add(disk);
                        root.setDisk(diskList);
                        logger.info("异步克隆虚拟机,开始----------------------");
                        // 发送克隆请求
                        HttpResponse httpResponse = HttpUtils.sendPost(vmWareApiConfig.getClone(), JSONObject.toJSONString(root));
                        // 判断返回结果
                        if (httpResponse.isOk()) {
                            String body = httpResponse.body();
                            JSONObject jsonObject = JSONObject.parseObject(body);
                            // 判断请求是否成功
                            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                                logger.info("异步克隆虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                                //克隆成功保存虚拟化
                                VmWareClone vmWareClone = vmWareMapping.toVmWareClone(parentVmWare);
                                VmWare insert = vmWareMapping.toVmWare(vmWareClone);
                                insert.setVmType(VmTypeEnum.Child.getCode());
                                insert.setGuid(childGuid);
                                insert.setVmParentId(guid);
                                insert.setVmName(childVmName);
                                insert.setVmCpu(root.getCpu());
                                insert.setVmStorage(root.getMem());
                                insert.setDiskName(disk.getName());
                                insert.setDiskSize(disk.getSize());
                                insert.setStatus(VmStatusEnum.Init.getCode());
                                insert.setClasses(query.getClasses());
                                insert.setCreateTime(new Date());
                                insert.setDisabled(false);
                                insert.setExamPaperId(query.getExamPaperId());
                                insert.setDeleted(false);
                                //保存
                                vmWareMapper.insertVmWare(insert);
                            } else {
                                logger.error("异步克隆虚拟机,接口返回失败信息:[{}]", httpResponse.body());
                            }
                        } else {
                            logger.error("异步克隆虚拟机,接口请求失败:[{}]", httpResponse.body());
                        }
                    }
                } else {
                    logger.error("异步克隆虚拟机,获取虚拟机配置信息失败");
                }
            } else {
                logger.error("异步克隆虚拟机,获取虚拟机配置键名失败");
            }
        } catch (Exception e) {
            logger.error("异步克隆虚拟机,业务异常----------------------", e);
        }
    }
    /**
     * 校验请求参数
     *
     * @param root 虚拟机基础信息
     * @param disk 虚拟机硬盘信息
     * @return 是否通过
     */
    private boolean checkVmConfig(Root root, Disk disk) {
        if (ObjectUtils.isNotEmpty(root) && ObjectUtils.isNotEmpty(disk)) {
            if (StringUtils.isNotEmpty(root.getOsType()) && ObjectUtils.isNotEmpty(root.getCpu())
                    && StringUtils.isNotEmpty(root.getMem())) {
                if (ObjectUtils.isNotEmpty(disk.getBoot_order()) && StringUtils.isNotEmpty(disk.getType())
                        && StringUtils.isNotEmpty(disk.getBus()) && StringUtils.isNotEmpty(disk.getSize())
                        && StringUtils.isNotEmpty(disk.getMountType()) && StringUtils.isNotEmpty(disk.getStorageclassName())
                        && StringUtils.isNotEmpty(disk.getAccessMode())) {
                    return true;
                } else {
                    logger.error("异步克隆新虚拟机,虚拟机硬盘信息不全");
                }
            } else {
                logger.error("异步克隆新虚拟机,虚拟机基础信息不全");
            }
        }
        return false;
    }

    /**
     * 异步释放虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public void releaseVmWare(VmWare vmWare) {
        try {
            //设置删除状态
            vmWare.setVmUserId("");
            vmWare.setClasses("");
            vmWare.setValidCreateTime(null);
            vmWare.setValidEndTime(null);
            vmWare.setDisabled(true);
            vmWare.setDeleted(true);
            this.updateVmWare(vmWare);

            //封装请求参数
            VmWareDelete vmWareDelete = new VmWareDelete();
            vmWareDelete.setVmName(vmWare.getVmName());
            List<String> volumes = new ArrayList<>();
            volumes.add(vmWare.getVmStorage());
            vmWareDelete.setVolumes(volumes);

            //调用接口
            HttpResponse httpResponse = HttpUtils.sendDelete(vmWareApiConfig.getDelete(), JSONObject.toJSONString(vmWareDelete));
            if (httpResponse.isOk()) {
                String body = httpResponse.body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")) {
                    logger.info("异步删除虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                    int count = vmWareMapper.deleteVmWareById(vmWare.getId());
                    if (count > 0) {
                        logger.info("异步删除虚拟机,删除虚拟机成功");
                    }
                } else {
                    logger.error("异步删除虚拟机,接口返回失败信息:[{}]", httpResponse.body());
                }
            } else {
                logger.error("异步删除虚拟机,接口请求失败:[{}]", httpResponse.body());
            }
        } catch (Exception e) {
            logger.error("异步删除虚拟机,业务异常----------------------", e);
            e.printStackTrace();
        }
    }

    /**
     * 查询虚拟机剩余时间(实训)
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    @Override
    public RestResponse queryRemainingTime(User currentUser, VmWareVM query) {
        //参数校验
        if (ObjectUtils.isEmpty(currentUser) || StringUtils.isEmpty(currentUser.getUserUuid())) {
            return RestResponse.fail(SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
        }
        VmWare select = new VmWare();
        select.setGuid(query.getGuid());
        select.setVmUserId(currentUser.getUserUuid());
        select.setClasses(VmClassesEnum.Train.getCode());
        VmWare current = vmWareMapper.selectBindVmWareEnabled(select);
        if (ObjectUtils.isNotEmpty(current)) {
            if (ObjectUtils.isEmpty(current.getValidEndTime())) {
                if (ObjectUtils.isEmpty(current.getValidCreateTime())) {
                    current.setValidCreateTime(DateUtil.date());
                }
                current.setValidEndTime(DateUtil.offsetMinute(current.getValidCreateTime(), getTrainTimeLimit()));
                this.updateVmWare(current);
            }
            RemainingTime remainingTime = new RemainingTime();
            remainingTime.setRemainTime(Math.max((int) DateUtil.between(new Date(), current.getValidEndTime(), DateUnit.SECOND, false), 0));
            remainingTime.setSingleSecond(getTrainTimeLimit() * 60);
            return RestResponse.ok(remainingTime);
        }
        return RestResponse.ok();
    }

    /**
     * 虚拟机续期(实训)
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    @Override
    public RestResponse renewal(User currentUser, VmWareVM query) {
        //参数校验
        if (ObjectUtils.isEmpty(currentUser) || StringUtils.isEmpty(currentUser.getUserUuid())) {
            return RestResponse.fail(SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
        }
        VmWare select = new VmWare();
        select.setGuid(query.getGuid());
        select.setVmUserId(currentUser.getUserUuid());
        select.setClasses(VmClassesEnum.Train.getCode());
        VmWare current = vmWareMapper.selectBindVmWareEnabled(select);
        if (ObjectUtils.isNotEmpty(current)) {
            if (ObjectUtils.isEmpty(current.getValidEndTime())) {
                if (ObjectUtils.isEmpty(current.getValidCreateTime())) {
                    current.setValidCreateTime(DateUtil.date());
                }
                current.setValidEndTime(DateUtil.offsetMinute(current.getValidCreateTime(), getTrainTimeLimit()));
            } else {
                DateTime now = DateUtil.date();
                //过期
                long remainTime = DateUtil.between(now, current.getValidEndTime(), DateUnit.SECOND, false);
                if (remainTime < 0) {
                    current.setValidEndTime(DateUtil.offsetMinute(now, getTrainTimeLimit()));
                } else {
                    if (remainTime > getTrainTimeLimit() * 60) {
                        return RestResponse.okAlert("当前剩余时间充足，请勿频繁续期");
                    }
                    current.setValidEndTime(DateUtil.offsetMinute(current.getValidEndTime(), getTrainTimeLimit()));
                }
            }
            int count = this.updateVmWare(current);
            if (count > 0) {
                return RestResponse.okMessage("续期成功");
            }
            return RestResponse.failMessage("续期失败");
        }
        return RestResponse.okConfirm("当前实训机器已回收，是否重新申请资源");
    }

    /**
     * 实训虚拟机时间限制 单位(分钟)
     *
     * @return 分钟
     */
    private int getTrainTimeLimit() {
        try {
            String trainTimeLimitConfigKey = vmWareConfigKey.getTrainTimeLimitConfigKey();
            String trainTimeLimit = sysConfigService.selectConfigByKey(trainTimeLimitConfigKey);
            if (StringUtils.isNotEmpty(trainTimeLimit)) {
                return Integer.parseInt(trainTimeLimit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取实训虚拟机时间限制,业务异常----------------------", e);
        }
        return CacheConstants.TRAIN_TIME_LIMIT_DEFAULT;
    }

    /**
     * 实训虚拟机过期时间
     *
     * @return 分钟
     */
    public DateTime getExpiredValidEndTime() {
        int expiredTimeLimit = getExpiredTimeLimit();
        return DateUtil.offsetMinute(DateUtil.date(), -expiredTimeLimit);
    }

    /**
     * excel导入虚拟机
     * @param vmWareImport
     */
    @Override
    @Transactional
    public void vmWareImport(VmWareImport vmWareImport) {
        // 导入成功标识false
        vmWareImport.setSuccess(false);
        //判断用户id是否为空
        if (null != vmWareImport && StringUtils.isEmpty(vmWareImport.getVmUserId())) {
            vmWareImport.setResult("请填写用户id");
            return;
        }
        //判断用户id在用户数据表中是否存在
        int userId = Integer.parseInt(vmWareImport.getVmUserId());
        User user = userMapper.getUserById(userId);
        if (ObjectUtils.isEmpty(user)) {
            vmWareImport.setResult("用户id不存在");
            return;
        }
        //判断虚拟机id在虚拟机表中是否存在
        VmWare vmWare1 = vmWareMapper.getVmWareById(vmWareImport.getId());
        if (ObjectUtils.isEmpty(vmWare1)) {
            vmWareImport.setResult("虚拟机id不存在");
            return;
        }
        VmWare vmWare = vmWareMapping.toVmWareFromImport(vmWareImport);
        Date now = new Date();
        vmWare.setUpdateTime(now);
        // 修改类型
//        if ("主机".equals(vmWare.getVmType())) {
//            vmWare.setVmType("00");
//        } else if ("子机".equals(vmWare.getVmType())) {
//            vmWare.setVmType("01");
//        }
//        if ("启动".equals(vmWare.getStatus())) {
//            vmWare.setStatus("00");
//        } else if ("关机".equals(vmWare.getStatus())) {
//            vmWare.setStatus("01");
//        } else if ("启动中".equals(vmWare.getStatus())) {
//            vmWare.setStatus("02");
//        } else if ("克隆中".equals(vmWare.getStatus())) {
//            vmWare.setStatus("100");
//        }
        // 修改到数据库
        vmWareMapper.updateVmWareUserIdAndClasses(vmWare);
        // 导入失败标识
        vmWareImport.setSuccess(true);
    }

    /**
     * 分配用户接口
     * @param vmWare
     * @return
     */
    @Override
    public RestResponse distribute(VmWare vmWare) {
        //给机器添加用户id
        vmWare.setUpdateTime(new Date());
        // 更新
        vmWareMapper.updateVmWareUserId(vmWare);
        return RestResponse.okMessage("分配成功");
    }

    /**
     * 当前主机还能创建的机器数量
     * @return
     */
    @Override
    public RestResponse cloneNumber() {
//        //判空操作
//        if (ObjectUtil.isEmpty(vmWare)) {
//            return RestResponse.fail(500, "机器为空");
//        }
//        if (StringUtils.isEmpty(vmWare.getVmParentId())) {
//            return RestResponse.fail(500, "虚拟机父ID为空");
//        }
        //核验实训机器台数限制
        String encryptStr = sysConfigService.selectConfigByKey(vmWareConfigKey.getMaxCountConfigKey());
        if (StringUtils.isEmpty(encryptStr)) {
            return RestResponse.failMessage("可创建的实训机器达到上限");
        }
        //解析
        String maxCount = systemService.pairOneDecode(encryptStr);
        //查询机器已经创建的数量
//        int count = vmWareMapper.selectCloneCount(vmWare.getVmParentId());
        int count = vmWareMapper.selectCount(null).intValue();
        if (count >= Integer.parseInt(maxCount)) {
            return RestResponse.failMessage("可创建的实训机器达到上限");
        }
        // 返回还能创建的台数
        int number = Integer.parseInt(maxCount) - count;
        return RestResponse.ok(number);
    }

    /**
     * 实训虚拟机过期时间限制 单位(分钟)
     *
     * @return 分钟
     */
    private int getExpiredTimeLimit() {
        try {
            String expiredTimeLimitConfigKey = vmWareConfigKey.getExpiredTimeLimitConfigKey();
            String expiredTimeLimit = sysConfigService.selectConfigByKey(expiredTimeLimitConfigKey);
            if (StringUtils.isNotEmpty(expiredTimeLimit)) {
                return Integer.parseInt(expiredTimeLimit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取实训虚拟机时间限制,业务异常----------------------", e);
        }
        return CacheConstants.EXPIRED_TIME_LIMIT_DEFAULT;
    }
}




