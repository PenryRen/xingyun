package com.mindskip.wdd.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
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
        Date now = new Date();
        Date startDate = DateUtil.beginOfMonth(now);
        Date endDate = DateUtil.endOfMonth(now);
        int nowDay = DateUtil.calendar(now).get(Calendar.DAY_OF_MONTH);
        int endDay = DateUtil.calendar(endDate).get(Calendar.DAY_OF_MONTH);
        DashboardVM dashboardVM = new DashboardVM();

        EchartVM userEchartVM = new EchartVM();
        userEchartVM.setTitle("用户月注册量");
        List<KeyValue> userKeyValueList = userService.selectMothCount(startDate, endDate);

        EchartVM paperEchartVM = new EchartVM();
        paperEchartVM.setTitle("试卷月提交量");
        List<KeyValue> paperKeyValueList = examPaperAnswerService.selectMothCount(startDate, endDate);

        for (int i = 0; i < endDay; i++) {
            Integer x = i + 1;
            String currentFormat = DateUtil.offsetDay(startDate, i).toString(DatePattern.NORM_DATE_PATTERN);

            userEchartVM.getX().add(x);
            KeyValue userKeyValue = userKeyValueList.stream().filter(d -> d.getName().equals(currentFormat)).findFirst().orElse(null);
            if (null == userKeyValue) {
                if (i < nowDay) {
                    userEchartVM.getY().add(0);
                }
            } else {
                userEchartVM.getY().add(userKeyValue.getValue());
            }

            paperEchartVM.getX().add(x);
            KeyValue paperKeyValue = paperKeyValueList.stream().filter(d -> d.getName().equals(currentFormat)).findFirst().orElse(null);
            if (null == paperKeyValue) {
                if (i < nowDay) {
                    paperEchartVM.getY().add(0);
                }
            } else {
                paperEchartVM.getY().add(paperKeyValue.getValue());
            }
        }

        dashboardVM.setUserEchartVM(userEchartVM);
        dashboardVM.setPaperEchartVM(paperEchartVM);
        return RestResponse.ok(dashboardVM);
    }


    /**
     * 系统信息展示
     *
     * @return the rest response
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/systemInfo", method = RequestMethod.POST)
    public RestResponse<SystemInfoVM> systemInfo() throws InterruptedException {
        SystemInfoVM systemInfoVM = new SystemInfoVM();
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        Properties props = System.getProperties();
        GlobalMemory memory = hal.getMemory();

        systemInfoVM.setServerName(props.getProperty("os.name"));

        //内存
        systemInfoVM.setPhysicalMemory(String.format("%s / %s", FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()), FormatUtil.formatBytes(memory.getTotal())));
        systemInfoVM.setPhysicalMemoryPercent(String.format("%.1f%%", (100d * (memory.getTotal() - memory.getAvailable()) / memory.getTotal())));

        //cpu信息
        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        systemInfoVM.setCpuCore(processor.getLogicalProcessorCount());
        systemInfoVM.setCpuLoad(String.format("%.1f%%", processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100));

        //硬盘存储信息
        OperatingSystem os = si.getOperatingSystem();
        FileSystem fileSystem = os.getFileSystem();
        long usable = 0;
        long total = 0;
        List<String> mountList = new ArrayList<>();
        for (OSFileStore fs : fileSystem.getFileStores()) {
            if (!mountList.contains(fs.getMount())) {
                usable += fs.getUsableSpace();
                total += fs.getTotalSpace();
                mountList.add(fs.getMount());
            }
        }
        systemInfoVM.setFileSystem(String.format("%s / %s", FormatUtil.formatBytes(total - usable), FormatUtil.formatBytes(total)));
        systemInfoVM.setFileSystemPercent(String.format("%.1f%%", 100d * (total - usable) / total));


        //jvm信息
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long jvmStartTime = runtimeMXBean.getStartTime();
        systemInfoVM.setJvmRunTime(ExamUtil.minSecondToVM(System.currentTimeMillis() - jvmStartTime));
        systemInfoVM.setJvmMemory(String.format("%s / %s", FormatUtil.formatBytes(runtime.totalMemory() - runtime.freeMemory()), FormatUtil.formatBytes(runtime.totalMemory())));
        systemInfoVM.setJvmPercent(String.format("%.1f%%", 100d * (runtime.totalMemory() - runtime.freeMemory()) / runtime.totalMemory()));
        systemInfoVM.setJvmVersion(props.getProperty("java.version"));
        systemInfoVM.setJvmName(runtimeMXBean.getVmName());
        systemInfoVM.setJvmStartTime(DateTimeUtil.dateTimeFullFormat(new Date(jvmStartTime)));
        systemInfoVM.setJvmLocation(props.getProperty("java.home"));


        //redis信息
        Properties redisProperties = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());
        systemInfoVM.setRedisRunTime(String.format("%s天", redisProperties.getProperty("uptime_in_days")));
        systemInfoVM.setRedisVersion(redisProperties.getProperty("redis_version"));
        systemInfoVM.setRedisMemory(String.format("%s / %s", redisProperties.getProperty("used_memory_human"), redisProperties.getProperty("total_system_memory_human")));
        long redisTotal = Long.parseLong(redisProperties.getProperty("total_system_memory"));
        long redisUse = Long.parseLong(redisProperties.getProperty("used_memory"));
        systemInfoVM.setRedisPercent(String.format("%.1f%%", 100d * redisUse / redisTotal));
        systemInfoVM.setRedisKeyCount(dbSize.toString());
        systemInfoVM.setRedisConnectionCount(redisProperties.getProperty("connected_clients"));
        systemInfoVM.setRedisExpiredKey(redisProperties.getProperty("expired_keys"));


        //mysql信息
        String mysqlVersion = systemService.getMysqlVersion();
        systemInfoVM.setMysqlVersion(mysqlVersion);
        systemService.getStatus("'Thread%'").forEach(item -> {
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
        systemService.getStatus("'table_locks%'").forEach(item -> {
            switch (item.getVariableName()) {
                case "Table_locks_immediate":
                    systemInfoVM.setMysqlTableLocksImmediate(item.getValue());
                    break;
                case "Table_locks_waited":
                    systemInfoVM.setMysqlTableLocksWaited(item.getValue());
                    break;
            }
        });
        systemService.getStatus("'open%tables%'").forEach(item -> {
            switch (item.getVariableName()) {
                case "Open_tables":
                    systemInfoVM.setMysqlOpenTables(item.getValue());
                    break;
                case "Opened_tables":
                    systemInfoVM.setMysqlOpenedTables(item.getValue());
                    break;
            }
        });

        return RestResponse.ok(systemInfoVM);
    }

}
