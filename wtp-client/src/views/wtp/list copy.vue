<template>
  <div class="app-container">
    <el-row :gutter="3">
      <el-col :span="5">
        <span class="demonstration">AppId: </span>
        <el-select v-model="pageQuery.appId" filterable placeholder="可搜索" @change="appIdOptionsChange">
          <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
        </el-select>
      </el-col>
      <el-col :span="5">
        <span class="demonstration">ClusterId: </span>
        <el-select v-model="pageQuery.clusterId" :disabled="clusterIdOptions.length <= 0" filterable placeholder="可搜索" @change="clusterIdOptionsChange">
          <el-option v-for="item in clusterIdOptions" :key="item.clusterId" :label="item.clusterId" :value="item.clusterId" />
        </el-select>
      </el-col>
      <el-col :span="5">
        <el-button :disabled="!pageQuery.clusterId" type="primary" @click="newWtpVisible = true">New Wtp</el-button>
      </el-col>
      <el-col :span="5">
        <el-button :disabled="!pageQuery.clusterId" type="primary" @click="registryVisible = true">{{ 'OnLine 机器( ' + wtpRegistryList.length + ' )' }}</el-button>
      </el-col>
    </el-row>
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;margin-top: 20px;"
    >
      <el-table-column align="center" label="ID" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.wtpId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="AppId">
        <template slot-scope="scope">
          <span>{{ scope.row.appId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="ClusterId">
        <template slot-scope="scope">
          <span>{{ scope.row.clusterId }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="title">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="PoolName">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="corePoolSize" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.corePoolSize }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="maximumPoolSize" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.maximumPoolSize }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="keepAliveSeconds" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.keepAliveSeconds }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="queueCapacity" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.queueCapacity }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="queueName">
        <template slot-scope="scope">
          <span>{{ scope.row.queueName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions">
        <template slot-scope="scope">
          <el-button-group>
            <el-button type="primary" @click="updateWtpBtn(scope.row.wtpId)">编辑</el-button>
            <el-button type="primary" @click="realTime(scope.row.appId, scope.row.clusterId, scope.row.name)">实时</el-button>
            <el-button type="primary" @click="report(scope.row.appId, scope.row.clusterId, scope.row.name)">报表</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="pageQuery.page"
      :limit.sync="pageQuery.size"
      @pagination="page"
    />

    <el-dialog title="创建 Wtp" :visible.sync="newWtpVisible">
      <el-form :model="createForm">
        <el-form-item label="AppId" label-width="140px">
          <el-select v-model="createForm.appId" filterable placeholder="可搜索">
            <el-option
              v-for="item in appIdOptions"
              :key="item.appId"
              :label="item.appId"
              :value="item.appId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="ClusterId" label-width="140px">
          <el-select v-model="createForm.clusterId" filterable placeholder="可搜索">
            <el-option
              v-for="item in clusterIdOptions"
              :key="item.clusterId"
              :label="item.clusterId"
              :value="item.clusterId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="title" label-width="140px">
          <el-input v-model="createForm.title" autocomplete="off" />
        </el-form-item>
        <el-form-item label="PoolName" label-width="140px">
          <el-input v-model="createForm.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="corePoolSize" label-width="140px">
          <el-input v-model="createForm.corePoolSize" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item label="maximumPoolSize" label-width="140px">
          <el-input v-model="createForm.maximumPoolSize" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item label="keepAliveSeconds" label-width="140px">
          <el-input v-model="createForm.keepAliveSeconds" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item label="queueCapacity" label-width="140px">
          <el-input v-model="createForm.queueCapacity" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item label="queueName" label-width="140px">
          <el-select v-model="createForm.queueName" filterable placeholder="可搜索">
            <el-option v-for="item in queueListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" label-width="140px">
          <el-input v-model="createForm.ownerName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="告警邮箱" label-width="140px">
          <el-input v-model="createForm.alarmEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item label="是否告警" label-width="140px">
          <el-switch v-model="createForm.openAlarm" active-color="#13ce66" inactive-color="#ff4949" />
        </el-form-item>
        <el-form-item v-if="createForm.openAlarm" label="活跃告警" label-width="140px">
          <el-input v-model="createForm.poolAlarmThreshold" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item v-if="createForm.openAlarm" label="队列容量告警" label-width="140px">
          <el-input v-model="createForm.queueAlarmThreshold" type="number" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newWtpVisible = false">取 消</el-button>
        <el-button type="primary" @click="createWtp">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改 Wtp" :visible.sync="updateWtpVisible">
      <el-form :model="createForm">
        <el-form-item label="AppId" label-width="140px">
          <el-input v-model="wtp.appId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item label="ClusterId" label-width="140px">
          <el-input v-model="wtp.clusterId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item label="title" label-width="140px">
          <el-input v-model="wtp.title" autocomplete="off" />
        </el-form-item>
        <el-form-item label="PoolName" label-width="140px">
          <el-input v-model="wtp.name" :disabled="true" autocomplete="off" />
        </el-form-item>
        <el-form-item label="corePoolSize" label-width="140px">
          <el-input v-model="wtp.corePoolSize" :max="wtp.maximumPoolSize" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item label="maximumPoolSize" label-width="140px">
          <el-input v-model="wtp.maximumPoolSize" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item label="keepAliveSeconds" label-width="140px">
          <el-input v-model="wtp.keepAliveSeconds" type="number" autocomplete="off" />
        </el-form-item>
        <el-alert v-if="wtp.queueName && wtp.queueName.indexOf('ResizableCapacity') < 0" :closable="false" title="（谨慎）当前 queueName 不支持动态修改队列长度 ，需重启项目，才能生效" type="warning" close-text="知道了" />
        <el-form-item label="queueCapacity" label-width="140px">
          <el-input v-model="wtp.queueCapacity" type="number" autocomplete="off" />
        </el-form-item>
        <el-alert v-if="wtpRegistryList.length > 0" :closable="false" title="（谨慎）修改 queueName 需重启项目，才能生效" type="warning" close-text="知道了" />
        <el-form-item label="queueName" label-width="140px">
          <el-select v-model="wtp.queueName" filterable placeholder="可搜索">
            <el-option v-for="item in queueListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" label-width="140px">
          <el-input v-model="wtp.ownerName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="告警邮箱" label-width="140px">
          <el-input v-model="wtp.alarmEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item label="是否告警" label-width="140px">
          <el-switch v-model="wtp.openAlarm" active-color="#13ce66" inactive-color="#ff4949" />
        </el-form-item>
        <el-form-item v-if="wtp.openAlarm" label="活跃告警" label-width="140px">
          <el-input v-model="wtp.poolAlarmThreshold" type="number" autocomplete="off" />
        </el-form-item>
        <el-form-item v-if="wtp.openAlarm" label="队列容量告警" label-width="140px">
          <el-input v-model="wtp.queueAlarmThreshold" type="number" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateWtpVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateWtp">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="实时数据" :visible.sync="realTimeVisible">
      <el-form :model="wtpLog" label-position="left" inline class="demo-table-expand">
        <el-form-item label="AppId" label-width="120px">
          <span>{{ wtpLog.appId }}</span>
        </el-form-item>
        <el-form-item label="ClusterId" label-width="120px">
          <span>{{ wtpLog.clusterId }}</span>
        </el-form-item>
        <el-form-item label="PoolName" label-width="120px">
          <span>{{ wtpLog.name }}</span>
        </el-form-item>
        <el-form-item label="" label-width="120px">
          <span />
        </el-form-item>
        <el-form-item label="核心线程数" label-width="120px">
          <span>{{ wtpLog.corePoolSize }}</span>
        </el-form-item>
        <el-form-item label="最大线程数" label-width="120px">
          <span>{{ wtpLog.maximumPoolSize }}</span>
        </el-form-item>
        <el-form-item label="回收时间(秒)" label-width="120px">
          <span>{{ wtpLog.keepAliveSeconds }}</span>
        </el-form-item>
        <el-form-item label="线程活跃数" label-width="120px">
          <span>{{ wtpLog.activeCount }}</span>
        </el-form-item>
        <el-form-item label="线程活跃率" label-width="120px">
          <span>{{ numberDiv(wtpLog.activeCount, wtpLog.maximumPoolSize, 2) }}</span>
        </el-form-item>
        <el-form-item label="完成任务数" label-width="120px">
          <span>{{ wtpLog.completedTaskCount }}</span>
        </el-form-item>
        <el-form-item label="队列长度" label-width="120px">
          <span>{{ wtpLog.queueSize + wtpLog.queueRemainingCapacity }}</span>
        </el-form-item>
        <el-form-item label="排队数" label-width="120px">
          <span>{{ wtpLog.queueSize }}</span>
        </el-form-item>
        <el-form-item label="队列中剩余容量" label-width="120px">
          <span>{{ wtpLog.queueRemainingCapacity }}</span>
        </el-form-item>
        <el-form-item label="队列使用率" label-width="120px">
          <span>{{ numberDiv(wtpLog.queueSize, wtpLog.queueSize + wtpLog.queueRemainingCapacity, 2) }}</span>
        </el-form-item>
        <el-form-item label="更新时间" label-width="120px">
          <span>{{ wtpLog.logTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="" :visible.sync="reportVisible" fullscreen>
      <div class="el-dialog-div">
        <chart :chartOption="usageRateOptionc" id="usageRateChart" height="90%" width="100%" />
      </div>
      <div class="el-dialog-div">
        <chart :chartOption="poolOptionc" id="poolChart" height="90%" width="100%" />
      </div>
      <div class="el-dialog-div">
        <chart :chartOption="queueOptionc" id="queueChart" height="90%" width="100%" />
      </div>
    </el-dialog>

    <el-dialog title="OnLine 机器" :visible.sync="registryVisible">
      <el-table :data="wtpRegistryList">
        <el-table-column align="center" label="IP">
          <template slot-scope="scope">
            <span>{{ scope.row.ip }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="上次拉取配置时间">
          <template slot-scope="scope">
            <span>{{ scope.row.lastPullTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="注册时间">
          <template slot-scope="scope">
            <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { appOptions } from '@/api/app'
import { clusterOptions } from '@/api/cluster'
import { page, create, queueOptions, get, update } from '@/api/wtp'
import { realTime, report } from '@/api/wtpLog'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'
import Chart from '@/components/Charts/Line'
// require('echarts')

export default {
  name: 'ArticleList',
  components: { Pagination, Chart },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: false,
      newWtpVisible: false,
      realTimeVisible: false,
      reportVisible: false,
      updateWtpVisible: false,
      registryVisible: false,
      pageQuery: {
        appId: null,
        clusterId: null,
        page: 1,
        size: 20
      },
      createForm: {
        appId: null
      },
      appIdOptions: [],
      clusterIdOptions: [],
      queueListOptions: [],
      wtpLog: {},
      wtp: {},
      poolOptionc: {},
      queueOptionc: {},
      usageRateOptionc: {},
      wtpRegistryList: []
    }
  },
  created() {
    this.appOptions()
    this.queueOptions()
  },
  methods: {
    queueOptions() {
      queueOptions().then((response) => {
        this.queueListOptions = response.data.list
      })
    },
    empty() {
      this.pageQuery.clusterId = null
      this.list = null
      this.wtpRegistryList = []
    },
    appOptions() {
      appOptions().then((response) => {
        this.appIdOptions = response.data.list
      })
    },
    appIdOptionsChange(value) {
      this.clusterOptions(value)
      this.createForm.appId = value
      this.empty()
    },
    clusterOptions(appId) {
      clusterOptions(appId).then((response) => {
        this.clusterIdOptions = response.data.list
      })
    },
    clusterIdOptionsChange(value) {
      this.createForm.clusterId = value
      this.page()
    },
    page() {
      this.listLoading = true
      page(this.pageQuery).then((response) => {
        this.list = response.data.list
        this.total = response.data.total
        this.wtpRegistryList = response.data.data
        this.listLoading = false
      })
    },
    formOptionsChange(value) {
      this.createForm.appId = value
    },
    createWtp() {
      if(!this.createForm.appId) {
        this.$message.error('appId 不能为空')
        return
      }
      if(!this.createForm.clusterId) {
        this.$message.error('clusterId 不能为空')
        return
      }
      if(!this.createForm.name) {
        this.$message.error('poolName 不能为空')
        return
      }
      if(!this.createForm.corePoolSize) {
        this.$message.error('corePoolSize 不能为空')
        return
      }
      if(!this.createForm.maximumPoolSize) {
        this.$message.error('maximumPoolSize 不能为空')
        return
      }
      if(!this.createForm.keepAliveSeconds) {
        this.$message.error('keepAliveSeconds 不能为空')
        return
      }
      if(!this.createForm.queueCapacity) {
        this.$message.error('queueCapacity 不能为空')
        return
      }
      if(!this.createForm.queueName) {
        this.$message.error('queueName 不能为空')
        return
      }
      if(!this.createForm.ownerName) {
        this.$message.error('负责人 不能为空')
        return
      }
      if(this.createForm.openAlarm) {
        if(!this.createForm.alarmEmail) {
          this.$message.error('告警邮箱 不能为空')
          return
        }
        if(!this.createForm.poolAlarmThreshold) {
          this.$message.error('活跃告警 不能为空')
          return
        }
        if(!this.createForm.queueAlarmThreshold) {
          this.$message.error('队列容量告警 不能为空')
          return
        }

      }

      create(this.createForm).then((response) => {
        this.create = response.data
        if (create) {
          this.$message.success('添加成功')
          this.newWtpVisible = false
          this.createForm = {
            appId: this.createForm.appId,
            clusterId: this.createForm.clusterId
            }
          this.page()
        } else {
          this.$message.error('添加失败')
        }
      })
    },
    realTime(appId, clusterId, name) {
      const realTimeForm = {
        appId: appId,
        clusterId: clusterId,
        name: name
      }
      realTime(realTimeForm).then((response) => {
        const wtpLog = response.data
        if (wtpLog) {
          this.wtpLog = wtpLog
          this.realTimeVisible = true
        } else {
          this.$message.warning('无实时数据')
        }
      })
    },
    report(appId, clusterId, name) {
      const reportForm = {
        appId: appId,
        clusterId: clusterId,
        name: name
      }
      this.poolOptionc = poolSeries
      this.queueOptionc = queueSeries
      this.usageRateOptionc = usageRateSeries
      report(reportForm).then((response) => {
        const list = response.data
        if (!list || list.length === 0) {
          this.$message.warning('无日志数据')
        }
        const timeList = []
        const corePoolSizeList = []
        const maximumPoolSizeList = []
        const activeCountList = []
        const queueSizeList = []
        const queueResidueList = []
        const queueUseList = []
        const poolUsageRateList = []
        const queueUsageRateList = []
        for (let i = 0; i < list.length; i++) {
          const wtpLog = list[i]
          timeList.push(parseTime(wtpLog.logTime, '{y}-{m}-{d} {h}:{i}'))
          corePoolSizeList.push(wtpLog.corePoolSize)
          maximumPoolSizeList.push(wtpLog.maximumPoolSize)
          activeCountList.push(wtpLog.activeCount)
          queueSizeList.push(wtpLog.queueSize + wtpLog.queueRemainingCapacity)
          queueResidueList.push(wtpLog.queueRemainingCapacity)
          queueUseList.push(wtpLog.queueSize)
          poolUsageRateList.push(this.numberDiv(wtpLog.activeCount, wtpLog.maximumPoolSize, 2))
          queueUsageRateList.push(this.numberDiv(wtpLog.queueSize, wtpLog.queueSize + wtpLog.queueRemainingCapacity, 2))
        }
        this.pollChart(timeList, corePoolSizeList, maximumPoolSizeList, activeCountList)
        this.queueChart(timeList, queueSizeList, queueResidueList, queueUseList)
        this.usageRateChart(timeList, poolUsageRateList, queueUsageRateList)
        this.reportVisible = true
      })

    },
    updateWtp() {
      update(this.wtp).then((response) => {
        this.update = response.data
        if (update) {
          this.$message.success('修改成功')
          this.updateWtpVisible = false
          this.page()
        } else {
          this.$message.error('修改失败')
        }
      })
    },
    updateWtpBtn(wtpId) {
      get(wtpId).then((response) => {
        this.wtp = response.data
        this.updateWtpVisible = true
      })
    },
    pollChart(timeList, corePoolSizeList, maximumPoolSizeList, activeCountList) {

      this.poolOptionc.xAxis[0].data = timeList
      this.poolOptionc.series[0].data = corePoolSizeList
      this.poolOptionc.series[1].data = maximumPoolSizeList
      this.poolOptionc.series[2].data = activeCountList

    },
    queueChart(timeList, queueSizeList, queueResidueList, queueUseList) {
      this.queueOptionc.xAxis[0].data = timeList
      this.queueOptionc.series[0].data = queueResidueList
      this.queueOptionc.series[1].data = queueSizeList
      this.queueOptionc.series[2].data = queueUseList
    },
    usageRateChart(timeList, poolUsageRateList, queueUsageRateList){
      this.usageRateOptionc.xAxis[0].data = timeList
      this.usageRateOptionc.series[0].data = poolUsageRateList
      this.usageRateOptionc.series[1].data = queueUsageRateList
    },

    numberDiv (arg1, arg2, digit) {
      return ((arg1 / arg2) * 100).toFixed(digit)
    }
  }
}

const poolSeries= {"backgroundColor":"#394056","title":{"top":20,"text":"线程","textStyle":{"fontWeight":"normal","fontSize":16,"color":"#F1F1F3"},"left":"1%"},"tooltip":{"trigger":"axis","axisPointer":{"lineStyle":{"color":"#57617B"}}},"legend":{"top":20,"icon":"rect","itemWidth":14,"itemHeight":5,"itemGap":13,"data":["核心数","最大数","活跃数"],"right":"4%","textStyle":{"fontSize":12,"color":"#F1F1F3"}},"grid":{"top":100,"left":"2%","right":"2%","bottom":"2%","containLabel":true},"xAxis":[{"type":"category","boundaryGap":false,"axisLine":{"lineStyle":{"color":"#57617B"}},"data":[1596086931334,1596086870942,1596086810128,1596086749757,1596086689170,1596086628385,1596086567051,1596086506079,1596086445250,1596086384794,1596086324180,1596086263434,1596086197236,1596086136786,1596086076191,1596086015792,1596085954903,1596085894503,1596085834127,1596085773539,1596085712716,1596085652030,1596085591593,1596085529080,1596085467564,1596085406534,1596085345818,1596085276424,1596085209879]}],"yAxis":[{"type":"value","name":"","axisTick":{"show":false},"axisLine":{"lineStyle":{"color":"#57617B"}},"axisLabel":{"margin":10,"textStyle":{"fontSize":14}},"splitLine":{"lineStyle":{"color":"#57617B"}}}],"series":[{"name":"核心数","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(137, 189, 27, 0.3)"},{"offset":0.8,"color":"rgba(137, 189, 27, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(137,189,27)","borderColor":"rgba(137,189,2,0.27)","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{"name":"最大数","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(219, 50, 51, 0.3)"},{"offset":0.8,"color":"rgba(219, 50, 51, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(219,50,51)","borderColor":"rgba(219,50,51,0.2)'","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{"name":"活跃数","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(0, 136, 212, 0.3)"},{"offset":0.8,"color":"rgba(0, 136, 212, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(0,136,212)","borderColor":"rgba(0,136,212,0.2)","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]}
const queueSeries= {"backgroundColor":"#394056","title":{"top":20,"text":"队列","textStyle":{"fontWeight":"normal","fontSize":16,"color":"#F1F1F3"},"left":"1%"},"tooltip":{"trigger":"axis","axisPointer":{"lineStyle":{"color":"#57617B"}}},"legend":{"top":20,"icon":"rect","itemWidth":14,"itemHeight":5,"itemGap":13,"data":["剩余长度","最大长度","排队长度"],"right":"4%","textStyle":{"fontSize":12,"color":"#F1F1F3"}},"grid":{"top":100,"left":"2%","right":"2%","bottom":"2%","containLabel":true},"xAxis":[{"type":"category","boundaryGap":false,"axisLine":{"lineStyle":{"color":"#57617B"}},"data":[1596086931334,1596086870942,1596086810128,1596086749757,1596086689170,1596086628385,1596086567051,1596086506079,1596086445250,1596086384794,1596086324180,1596086263434,1596086197236,1596086136786,1596086076191,1596086015792,1596085954903,1596085894503,1596085834127,1596085773539,1596085712716,1596085652030,1596085591593,1596085529080,1596085467564,1596085406534,1596085345818,1596085276424,1596085209879]}],"yAxis":[{"type":"value","name":"","axisTick":{"show":false},"axisLine":{"lineStyle":{"color":"#57617B"}},"axisLabel":{"margin":10,"textStyle":{"fontSize":14}},"splitLine":{"lineStyle":{"color":"#57617B"}}}],"series":[{"name":"剩余长度","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(137, 189, 27, 0.3)"},{"offset":0.8,"color":"rgba(137, 189, 27, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(137,189,27)","borderColor":"rgba(137,189,2,0.27)","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{"name":"最大长度","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(219, 50, 51, 0.3)"},{"offset":0.8,"color":"rgba(219, 50, 51, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(219,50,51)","borderColor":"rgba(219,50,51,0.2)'","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{"name":"排队长度","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(0, 136, 212, 0.3)"},{"offset":0.8,"color":"rgba(0, 136, 212, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(0,136,212)","borderColor":"rgba(0,136,212,0.2)","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]}
const usageRateSeries = {"backgroundColor":"#394056","title":{"top":20,"text":"使用率","textStyle":{"fontWeight":"normal","fontSize":16,"color":"#F1F1F3"},"left":"1%"},"tooltip":{"trigger":"axis","axisPointer":{"lineStyle":{"color":"#57617B"}}},"legend":{"top":20,"icon":"rect","itemWidth":14,"itemHeight":5,"itemGap":13,"data":["线程使用率","队列使用率"],"right":"4%","textStyle":{"fontSize":12,"color":"#F1F1F3"}},"grid":{"top":100,"left":"2%","right":"2%","bottom":"2%","containLabel":true},"xAxis":[{"type":"category","boundaryGap":false,"axisLine":{"lineStyle":{"color":"#57617B"}},"data":[1596086931334,1596086870942,1596086810128,1596086749757,1596086689170,1596086628385,1596086567051,1596086506079,1596086445250,1596086384794,1596086324180,1596086263434,1596086197236,1596086136786,1596086076191,1596086015792,1596085954903,1596085894503,1596085834127,1596085773539,1596085712716,1596085652030,1596085591593,1596085529080,1596085467564,1596085406534,1596085345818,1596085276424,1596085209879]}],"yAxis":[{"type":"value","name":"","axisTick":{"show":false},"axisLine":{"lineStyle":{"color":"#57617B"}},"axisLabel":{"margin":10,"textStyle":{"fontSize":14}},"splitLine":{"lineStyle":{"color":"#57617B"}}}],"series":[{"name":"线程使用率","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(255, 106, 106, 0.3)"},{"offset":0.8,"color":"rgba(255, 106, 106, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(255, 106, 106)","borderColor":"rgba(255, 106, 106,0.27)","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{"name":"队列使用率","type":"line","smooth":true,"symbol":"circle","symbolSize":5,"showSymbol":false,"lineStyle":{"normal":{"width":1}},"areaStyle":{"normal":{"color":{"x":0,"y":0,"x2":0,"y2":1,"type":"linear","global":false,"colorStops":[{"offset":0,"color":"rgba(255, 130, 71, 0.3)"},{"offset":0.8,"color":"rgba(255, 130, 71, 0)"}]},"shadowColor":"rgba(0, 0, 0, 0.1)","shadowBlur":10}},"itemStyle":{"normal":{"color":"rgb(255, 130, 71)","borderColor":"rgba(255, 130, 71,0.2)","borderWidth":12}},"data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]}

</script>

<style scoped>
.el-dialog-div{
  height: 500px;
  overflow: auto;
}
.chart-container{
  position: relative;
  width: 100%;
  height: calc(100vh - 84px);
}
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
<style>
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
