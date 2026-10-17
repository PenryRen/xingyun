<template>
  <div class="dashboard-container">
    <div class="system-info-row">
      <el-card header="服务器信息" class="system-card">
        <p><span>操作系统：</span><span>{{ systemInfo.serverName }}</span></p>
        <p><span>内存占用：</span><span>{{ systemInfo.physicalMemory }}</span></p>
        <p><span>内存使用率：</span><span>{{ systemInfo.physicalMemoryPercent }}</span></p>
        <p><span>处理器核数：</span><span>{{ systemInfo.cpuCore }}</span></p>
        <p><span>Cpu使用率：</span><span>{{ systemInfo.cpuLoad }}</span></p>
        <p><span>硬盘存储：</span><span>{{ systemInfo.fileSystem }}</span></p>
        <p><span>硬盘使用率：</span><span>{{ systemInfo.fileSystemPercent }}</span></p>
      </el-card>
      <el-card class="system-card" header="JVM信息">
        <p><span>名称：</span><span>{{ systemInfo.jvmName }}</span></p>
        <p><span>版本：</span><span>{{ systemInfo.jvmVersion }}</span></p>
        <p><span>内存占用：</span><span>{{ systemInfo.jvmMemory }}</span></p>
        <p><span>内存占比：</span><span>{{ systemInfo.jvmPercent }}</span></p>
        <p><span>运行时长：</span><span>{{ systemInfo.jvmRunTime }}</span></p>
        <p><span>启动时间：</span><span>{{ systemInfo.jvmStartTime }}</span></p>
        <p><span>运行目录：</span><span>{{ systemInfo.jvmLocation }}</span></p>
      </el-card>
      <el-card class="system-card" header="Redis信息">
        <p><span>版本：</span><span>{{ systemInfo.redisVersion }}</span></p>
        <p><span>内存占用：</span><span>{{ systemInfo.redisMemory }}</span></p>
        <p><span>内存占比：</span><span>{{ systemInfo.redisPercent }}</span></p>
        <p><span>运行时长：</span><span>{{ systemInfo.redisRunTime }}</span></p>
        <p><span>Key过期数量：</span><span>{{ systemInfo.redisExpiredKey }}</span></p>
        <p><span>Key总数量：</span><span>{{ systemInfo.redisKeyCount }}</span></p>
        <p><span>客户端连接数：</span><span>{{ systemInfo.redisConnectionCount }}</span></p>
      </el-card>
      <el-card class="system-card" header="Mysql信息">
        <p><span>版本：</span><span>{{ systemInfo.mysqlVersion }}</span></p>
        <p><span>线程连接数：</span><span>{{ systemInfo.mysqlThreadsConnected }}</span></p>
        <p><span>线程创建数：</span><span>{{ systemInfo.mysqlThreadsCreated }}</span></p>
        <p><span>线程运行数：</span><span>{{ systemInfo.mysqlThreadsRunning }}</span></p>
        <p><span>表锁立即释放数：</span><span>{{ systemInfo.mysqlTableLocksImmediate }}</span></p>
        <p><span>打开表数量：</span><span>{{ systemInfo.mysqlOpenTables }}</span></p>
        <p><span>打开过表数量：</span><span>{{ systemInfo.mysqlOpenedTables }}</span></p>
      </el-card>
    </div>
    <div class="dashboard-echarts">
      <el-card header="用户注册量" class="echarts-card">
        <div id="echarts-user" style="width: 100%;height:400px;"/>
      </el-card>
      <el-card header="试卷提交量" class="echarts-card">
        <div id="echarts-paper" style="width: 100%;height:400px;"/>
      </el-card>
    </div>
  </div>
</template>

<script>
import {index, systemInfo} from '@/api/dashboard'
import {resize} from '@/utils/resize'
import * as echarts from 'echarts'

export default {
  mixins: [resize],
  name: 'Dashboard',
  data() {
    return {
      systemInfo: {}
    }
  },
  created() {
    systemInfo().then(re => {
      this.systemInfo = re.response
    })
  },
  mounted() {
    this.dataLoad()
  },
  methods: {
    dataLoad() {
      let echartsUser = echarts.init(document.getElementById('echarts-user'), 'light')
      let echartsPaper = echarts.init(document.getElementById('echarts-paper'), 'light')
      index().then(re => {
        let response = re.response
        echartsUser.setOption(this.option(response.userEchartVM.title, response.userEchartVM.x, response.userEchartVM.y))
        echartsPaper.setOption(this.option(response.paperEchartVM.title, response.paperEchartVM.x, response.paperEchartVM.y))
        resize.initChat([echartsUser, echartsPaper])
      })
    },
    option(title, x, y) {
      return {
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            name: '日期',
            type: 'category',
            data: x,
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            name: '数量',
            type: 'value'
          }
        ],
        series: [
          {
            name: '数量',
            type: 'bar',
            barWidth: '60%',
            data: y
          }
        ]
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  margin-bottom: 40px;
}
</style>
