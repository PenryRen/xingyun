package com.mindskip.wdd.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.SystemStatus;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.dashboard.DashboardVM;
import com.mindskip.wdd.viewmodel.dashboard.EchartVM;
import com.mindskip.wdd.viewmodel.dashboard.SystemInfoVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;

/**
 * @version 1.7.0
 * @description: 首页接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@RequestMapping(value = "/api/dashboard")
@AllArgsConstructor
@Slf4j
public class DashboardController extends BaseApiController {

    private final UserService userService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final RedisTemplate<String, String> redisTemplate;
    private final SystemService systemService;


    /**
     * 试卷、用户月统计
     *
     * @return the rest response
     */
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public RestResponse<DashboardVM> Index() {
        try {
            Date now = new Date();
            Date startDate = DateUtil.beginOfMonth(now);
            Date endDate = DateUtil.endOfMonth(now);
            int nowDay = DateUtil.calendar(now).get(Calendar.DAY_OF_MONTH);
            int endDay = DateUtil.calendar(endDate).get(Calendar.DAY_OF_MONTH);
            DashboardVM dashboardVM = new DashboardVM();

            EchartVM userEchartVM = new EchartVM();
            userEchartVM.setTitle("用户月注册量");
            List<KeyValue> userKeyValueList = userService.selectMothCount(startDate, endDate);
            if (null == userKeyValueList) {
                userKeyValueList = Collections.emptyList();
            }

            EchartVM paperEchartVM = new EchartVM();
            paperEchartVM.setTitle("试卷月提交量");
            List<KeyValue> paperKeyValueList = examPaperAnswerService.selectMothCount(startDate, endDate);
            if (null == paperKeyValueList) {
                paperKeyValueList = Collections.emptyList();
            }

            for (int i = 0; i < endDay; i++) {
                Integer x = i + 1;
                String currentFormat = DateUtil.offsetDay(startDate, i).toString(DatePattern.NORM_DATE_PATTERN);

                userEchartVM.getX().add(x);
                KeyValue userKeyValue = userKeyValueList.stream().filter(d -> null != d && null != d.getName() && d.getName().equals(currentFormat)).findFirst().orElse(null);
                if (null == userKeyValue) {
                    if (i < nowDay) {
                        userEchartVM.getY().add(0);
                    }
                } else {
                    userEchartVM.getY().add(null != userKeyValue.getValue() ? userKeyValue.getValue() : 0);
                }

                paperEchartVM.getX().add(x);
                KeyValue paperKeyValue = paperKeyValueList.stream().filter(d -> null != d && null != d.getName() && d.getName().equals(currentFormat)).findFirst().orElse(null);
                if (null == paperKeyValue) {
                    if (i < nowDay) {
                        paperEchartVM.getY().add(0);
                    }
                } else {
                    paperEchartVM.getY().add(null != paperKeyValue.getValue() ? paperKeyValue.getValue() : 0);
                }
            }

            dashboardVM.setUserEchartVM(userEchartVM);
            dashboardVM.setPaperEchartVM(paperEchartVM);
            return RestResponse.ok(dashboardVM);
        } catch (Exception e) {
            log.warn("dashboard index failed: {}", e.getMessage());
            DashboardVM empty = new DashboardVM();
            EchartVM userEchartVM = new EchartVM();
            userEchartVM.setTitle("用户月注册量");
            EchartVM paperEchartVM = new EchartVM();
            paperEchartVM.setTitle("试卷月提交量");
            empty.setUserEchartVM(userEchartVM);
            empty.setPaperEchartVM(paperEchartVM);
            return RestResponse.ok(empty);
        }
    }


    /**
     * 系统信息展示
     *
     * @return the rest response
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/systemInfo", method = RequestMethod.POST)
    public RestResponse<SystemInfoVM> systemInfo() {
        SystemInfoVM systemInfoVM = new SystemInfoVM();
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            Properties props = System.getProperties();
            GlobalMemory memory = hal.getMemory();

            systemInfoVM.setServerName(null != props ? props.getProperty("os.name", "-") : "-");

            try {
                long memTotal = memory.getTotal();
                long memAvail = memory.getAvailable();
                systemInfoVM.setPhysicalMemory(String.format("%s / %s", FormatUtil.formatBytes(memTotal - memAvail), FormatUtil.formatBytes(memTotal)));
                systemInfoVM.setPhysicalMemoryPercent(memTotal > 0 ? String.format("%.1f%%", (100d * (memTotal - memAvail) / memTotal)) : "-");
            } catch (Exception e) {
                log.warn("systemInfo memory failed: {}", e.getMessage());
                systemInfoVM.setPhysicalMemory("- / -");
                systemInfoVM.setPhysicalMemoryPercent("-");
            }

            try {
                CentralProcessor processor = hal.getProcessor();
                long[] prevTicks = processor.getSystemCpuLoadTicks();
                Util.sleep(500);
                systemInfoVM.setCpuCore(processor.getLogicalProcessorCount());
                double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks);
                systemInfoVM.setCpuLoad(cpuLoad >= 0 ? String.format("%.1f%%", cpuLoad * 100) : "计算中");
            } catch (Exception e) {
                log.warn("systemInfo cpu failed: {}", e.getMessage());
                systemInfoVM.setCpuCore(0);
                systemInfoVM.setCpuLoad("-");
            }

            try {
                OperatingSystem os = si.getOperatingSystem();
                FileSystem fileSystem = os.getFileSystem();
                long usable = 0;
                long total = 0;
                List<String> mountList = new ArrayList<>();
                for (OSFileStore fs : fileSystem.getFileStores()) {
                    if (null != fs && !mountList.contains(fs.getMount())) {
                        usable += fs.getUsableSpace();
                        total += fs.getTotalSpace();
                        mountList.add(fs.getMount());
                    }
                }
                systemInfoVM.setFileSystem(String.format("%s / %s", FormatUtil.formatBytes(total - usable), FormatUtil.formatBytes(total)));
                systemInfoVM.setFileSystemPercent(total > 0 ? String.format("%.1f%%", 100d * (total - usable) / total) : "-");
            } catch (Exception e) {
                log.warn("systemInfo filesystem failed: {}", e.getMessage());
                systemInfoVM.setFileSystem("- / -");
                systemInfoVM.setFileSystemPercent("-");
            }

            try {
                Runtime runtime = Runtime.getRuntime();
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                long jvmStartTime = runtimeMXBean.getStartTime();
                systemInfoVM.setJvmRunTime(ExamUtil.minSecondToVM(System.currentTimeMillis() - jvmStartTime));
                long jvmTotal = runtime.totalMemory();
                long jvmFree = runtime.freeMemory();
                systemInfoVM.setJvmMemory(String.format("%s / %s", FormatUtil.formatBytes(jvmTotal - jvmFree), FormatUtil.formatBytes(jvmTotal)));
                systemInfoVM.setJvmPercent(jvmTotal > 0 ? String.format("%.1f%%", 100d * (jvmTotal - jvmFree) / jvmTotal) : "-");
                systemInfoVM.setJvmVersion(null != props ? props.getProperty("java.version", "-") : "-");
                systemInfoVM.setJvmName(runtimeMXBean.getVmName());
                systemInfoVM.setJvmStartTime(DateTimeUtil.dateTimeFullFormat(new Date(jvmStartTime)));
                systemInfoVM.setJvmLocation(null != props ? props.getProperty("java.home", "-") : "-");
            } catch (Exception e) {
                log.warn("systemInfo jvm failed: {}", e.getMessage());
                systemInfoVM.setJvmRunTime("-");
                systemInfoVM.setJvmMemory("- / -");
                systemInfoVM.setJvmPercent("-");
            }

            try {
                Properties redisProperties = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
                Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());
                if (null != redisProperties) {
                    systemInfoVM.setRedisRunTime(nullSafeProp(redisProperties, "uptime_in_days", "-") + "天");
                    systemInfoVM.setRedisVersion(nullSafeProp(redisProperties, "redis_version", "-"));
                    systemInfoVM.setRedisMemory(String.format("%s / %s", nullSafeProp(redisProperties, "used_memory_human", "-"), nullSafeProp(redisProperties, "maxmemory_human", nullSafeProp(redisProperties, "total_system_memory_human", "-"))));
                    try {
                        String totalStr = redisProperties.getProperty("total_system_memory") != null ? redisProperties.getProperty("total_system_memory") : redisProperties.getProperty("maxmemory", "0");
                        String usedStr = redisProperties.getProperty("used_memory", "0");
                        long redisTotal = Long.parseLong(totalStr);
                        long redisUse = Long.parseLong(usedStr);
                        systemInfoVM.setRedisPercent(redisTotal > 0 ? String.format("%.1f%%", 100d * redisUse / redisTotal) : "-");
                    } catch (Exception ex) {
                        systemInfoVM.setRedisPercent("-");
                    }
                    systemInfoVM.setRedisKeyCount(null != dbSize ? dbSize.toString() : "0");
                    systemInfoVM.setRedisConnectionCount(nullSafeProp(redisProperties, "connected_clients", "-"));
                    systemInfoVM.setRedisExpiredKey(nullSafeProp(redisProperties, "expired_keys", "-"));
                } else {
                    systemInfoVM.setRedisRunTime("-");
                    systemInfoVM.setRedisVersion("-");
                    systemInfoVM.setRedisMemory("-");
                    systemInfoVM.setRedisPercent("-");
                    systemInfoVM.setRedisKeyCount("0");
                    systemInfoVM.setRedisConnectionCount("-");
                    systemInfoVM.setRedisExpiredKey("-");
                }
            } catch (Exception e) {
                log.warn("systemInfo redis failed: {}", e.getMessage());
                systemInfoVM.setRedisRunTime("-");
                systemInfoVM.setRedisVersion("-");
                systemInfoVM.setRedisMemory("-");
                systemInfoVM.setRedisPercent("-");
                systemInfoVM.setRedisKeyCount("0");
                systemInfoVM.setRedisConnectionCount("-");
                systemInfoVM.setRedisExpiredKey("-");
            }

            try {
                String mysqlVersion = systemService.getMysqlVersion();
                systemInfoVM.setMysqlVersion(null != mysqlVersion ? mysqlVersion : "-");
                List<SystemStatus> threadStatus = systemService.getStatus("'Thread%'");
                if (null != threadStatus) {
                    threadStatus.forEach(item -> {
                        if (null == item || null == item.getVariableName()) return;
                        switch (item.getVariableName()) {
                            case "Threads_connected":
                                systemInfoVM.setMysqlThreadsConnected(item.getValue());
                                break;
                            case "Threads_created":
                                systemInfoVM.setMysqlThreadsCreated(item.getValue());
                                break;
                            case "Threads_running":
                                systemInfoVM.setMysqlThreadsRunning(item.getValue());
                                break;
                        }
                    });
                }
                List<SystemStatus> lockStatus = systemService.getStatus("'table_locks%'");
                if (null != lockStatus) {
                    lockStatus.forEach(item -> {
                        if (null == item || null == item.getVariableName()) return;
                        switch (item.getVariableName()) {
                            case "Table_locks_immediate":
                                systemInfoVM.setMysqlTableLocksImmediate(item.getValue());
                                break;
                            case "Table_locks_waited":
                                systemInfoVM.setMysqlTableLocksWaited(item.getValue());
                                break;
                        }
                    });
                }
                List<SystemStatus> openTableStatus = systemService.getStatus("'open%tables%'");
                if (null != openTableStatus) {
                    openTableStatus.forEach(item -> {
                        if (null == item || null == item.getVariableName()) return;
                        switch (item.getVariableName()) {
                            case "Open_tables":
                                systemInfoVM.setMysqlOpenTables(item.getValue());
                                break;
                            case "Opened_tables":
                                systemInfoVM.setMysqlOpenedTables(item.getValue());
                                break;
                        }
                    });
                }
            } catch (Exception e) {
                log.warn("systemInfo mysql failed: {}", e.getMessage());
                systemInfoVM.setMysqlVersion("-");
            }
        } catch (Exception e) {
            log.warn("systemInfo global failed: {}", e.getMessage());
        }
        return RestResponse.ok(systemInfoVM);
    }

    private String nullSafeProp(Properties props, String key, String defaultValue) {
        String v = props.getProperty(key);
        return null != v && !v.isEmpty() ? v : defaultValue;
    }

}
