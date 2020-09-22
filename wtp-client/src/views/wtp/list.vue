<template>
  <div class="app-container">
    <div class="table-query">
      <span class="mr10">
        <el-select v-model="pageQuery.appId" filterable placeholder="appId (可搜索)" @change="appIdOptionsChange">
          <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
        </el-select>
      </span>
      <span class="mr10">
        <el-select
          v-model="pageQuery.clusterId"
          :disabled="clusterIdOptions.length <= 0"
          filterable
          placeholder="clusterId (可搜索)"
          @change="clusterIdOptionsChange"
        >
          <el-option
            v-for="item in clusterIdOptions"
            :key="item.clusterId"
            :label="item.clusterId"
            :value="item.clusterId"
          />
        </el-select>
      </span>
      <span class="mr10">
        <el-input
          v-model.trim="pageQuery.name"
          style="width: 150px;"
          class="filter-item"
          placeholder="线程池名称"
          clearable
          :disabled="!pageQuery.clusterId"
        />
      </span>
      <el-button
        class="filter-item mr10"
        type="primary"
        icon="el-icon-search"
        :disabled="!pageQuery.clusterId"
        @click="page"
      >
        搜索
      </el-button>
      <span class="mr10">
        <el-button
          v-if="check(pageQuery.appId,'INSERT')"
          :disabled="!pageQuery.clusterId"
          type="primary"
          @click="newWtpVisible = true"
        >New
          Wtp
        </el-button>
      </span>
      <span class="mr10">
        <el-button :disabled="!pageQuery.clusterId" type="primary" @click="registryVisibleBtn">{{
          'OnLine 机器( ' +
            wtpRegistryList.length + ' )'
        }}
        </el-button>
      </span>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;margin-top: 20px;"
    >
      <el-table-column align="center" label="ID" width="60">
        <template slot-scope="scope">
          <span>{{ scope.row.wtpId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="AppId" width="100">
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
      <el-table-column align="center" label="rejectedName">
        <template slot-scope="scope">
          <span>{{ scope.row.rejectedExecutionHandlerName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="Actions" width="300">
        <template slot-scope="scope">
          <el-button-group>
            <el-button
              v-if="check(scope.row.appId,'UPDATE')"
              type="primary"
              size="small"
              @click="updateWtpBtn(scope.row.wtpId)"
            >编辑
            </el-button>
            <el-tooltip class="item" effect="dark" content="同步配置到其他Cluster" placement="top-end">
              <el-button type="primary" size="small" @click="syncConfigVisibleBtn(scope.row)">同步</el-button>
            </el-tooltip>
            <el-popconfirm
              confirm-button-text="好的"
              cancel-button-text="不用了"
              icon="el-icon-info"
              icon-color="red"
              title="删除后不可恢复,确定删除吗？"
              @onConfirm="delWtpBtn(scope.row.wtpId)"
            >
              <el-button v-if="check(scope.row.appId,'DELETE')" slot="reference" type="danger" size="small">删除
              </el-button>
            </el-popconfirm>
          </el-button-group>
          <el-button-group style="margin-top: 8px;">
            <el-button
              v-if="check(scope.row.appId,'UPDATE')"
              type="primary"
              size="small"
              @click="toWtpLog(scope.row.name)"
            >日志
            </el-button>
            <el-tooltip class="item" effect="dark" content="最后一条日志" placement="bottom-end">
              <el-button
                type="primary"
                size="small"
                @click="realTime(scope.row.appId, scope.row.clusterId, scope.row.name)"
              >lastLog
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="最近30分钟图表" placement="bottom-end">
              <el-button
                type="primary"
                size="small"
                @click="report(scope.row.appId, scope.row.clusterId, scope.row.name)"
              >报表
              </el-button>
            </el-tooltip>
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
            <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
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
          <el-input-number
            v-model="createForm.corePoolSize"
            controls-position="right"
            :min="0"
            :max="createForm.maximumPoolSize?createForm.maximumPoolSize:2147483647"
          />
        </el-form-item>
        <el-form-item label="maximumPoolSize" label-width="140px">
          <el-input-number
            v-model="createForm.maximumPoolSize"
            controls-position="right"
            :min="createForm.corePoolSize? createForm.corePoolSize : 0"
            :max="2147483647"
          />
        </el-form-item>
        <el-form-item label="keepAliveSeconds" label-width="140px">
          <el-input-number v-model="createForm.keepAliveSeconds" controls-position="right" :min="1" />
        </el-form-item>
        <el-alert
          v-if="SynchronousQueueAndLinkedTransferQueue"
          :closable="false"
          title="当前 queueName 不支持设置queueCapacity,也不会生效."
          type="warning"
        />
        <el-alert
          v-if="PriorityBlockingQueue"
          :closable="false"
          title="PriorityBlockingQueue 的 queueCapacity 是队列初始长度, 并非最大长度."
          type="warning"
        />
        <el-form-item label="queueCapacity" label-width="140px">
          <el-input-number
            v-model="createForm.queueCapacity"
            controls-position="right"
            :disabled="SynchronousQueueAndLinkedTransferQueue"
            :min="0"
            :max="SynchronousQueueAndLinkedTransferQueue ? 0 : 9223372036854775807"
          />
        </el-form-item>
        <el-form-item label="queueName" label-width="140px">
          <el-select v-model="createForm.queueName" filterable placeholder="可搜索" @change="createUueueChange">
            <el-option v-for="item in queueListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="rejectedName" label-width="140px">
          <el-select v-model="createForm.rejectedExecutionHandlerName" filterable placeholder="可搜索">
            <el-option v-for="item in rejectedListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" label-width="140px">
          <el-input v-model="createForm.ownerName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="负责人邮箱" label-width="140px">
          <el-input v-model="createForm.alarmEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item label="是否告警" label-width="140px">
          <el-switch v-model="createForm.openAlarm" active-color="#13ce66" inactive-color="#ff4949" />
        </el-form-item>
        <el-form-item v-if="createForm.openAlarm" label="活跃告警(%)" label-width="140px">
          <el-input-number v-model="createForm.poolAlarmThreshold" controls-position="right" :min="0" :max="100" />
        </el-form-item>
        <el-alert
          v-if="createForm.openAlarm && createForm.queueName && (createForm.queueName === 'LinkedTransferQueue' || createForm.queueName === 'SynchronousQueue')"
          :closable="false"
          title="当前 queueName 不支持设置队列容量告警."
          type="warning"
          close-text="知道了"
        />
        <el-form-item v-if="createForm.openAlarm" label="队列容量告警(%)" label-width="140px">
          <el-input-number
            v-model="createForm.queueAlarmThreshold"
            :disabled="SynchronousQueueAndLinkedTransferQueue"
            controls-position="right"
            :min="0"
            :max="100"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newWtpVisible = false">取 消</el-button>
        <el-button type="primary" @click="createWtp">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改 Wtp" :visible.sync="updateWtpVisible">
      <el-form :model="wtp">
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
          <el-input-number v-model="wtp.corePoolSize" controls-position="right" :min="0" :max="wtp.maximumPoolSize" />
        </el-form-item>
        <el-form-item label="maximumPoolSize" label-width="140px">
          <el-input-number
            v-model="wtp.maximumPoolSize"
            controls-position="right"
            :min="wtp.corePoolSize"
            :max="2147483647"
          />
        </el-form-item>
        <el-form-item label="keepAliveSeconds" label-width="140px">
          <el-input-number v-model="wtp.keepAliveSeconds" controls-position="right" :min="1" />
        </el-form-item>
        <el-alert
          v-if="wtp.queueName && wtp.queueName.indexOf('ResizableCapacity') < 0"
          :closable="false"
          title="（谨慎）当前 queueName 不支持动态修改队列长度 ，需重启项目，才能生效"
          type="warning"
          close-text="知道了"
        />
        <el-alert
          v-if="SynchronousQueueAndLinkedTransferQueue"
          :closable="false"
          title="当前 queueName 不支持设置queueCapacity,也不会生效."
          type="warning"
          close-text="知道了"
        />
        <el-alert
          v-if="PriorityBlockingQueue"
          :closable="false"
          title="PriorityBlockingQueue 的 queueCapacity 是队列初始长度, 并非最大长度."
          type="warning"
          close-text="知道了"
        />
        <el-form-item label="queueCapacity" label-width="140px">
          <el-input-number
            v-model="wtp.queueCapacity"
            controls-position="right"
            :disabled="SynchronousQueueAndLinkedTransferQueue"
            :min="0"
            :max="SynchronousQueueAndLinkedTransferQueue?0:9223372036854775807"
          />
        </el-form-item>
        <el-alert
          v-if="wtpRegistryList.length > 0"
          :closable="false"
          title="（谨慎）修改 queueName 需重启项目，才能生效"
          type="warning"
          close-text="知道了"
        />
        <el-form-item label="queueName" label-width="140px">
          <el-select v-model="wtp.queueName" filterable placeholder="可搜索" @change="updateUueueChange">
            <el-option v-for="item in queueListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="rejectedName" label-width="140px">
          <el-select v-model="wtp.rejectedExecutionHandlerName" filterable placeholder="可搜索">
            <el-option v-for="item in rejectedListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" label-width="140px">
          <el-input v-model="wtp.ownerName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="负责人邮箱" label-width="140px">
          <el-input v-model="wtp.alarmEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item label="是否告警" label-width="140px">
          <el-switch v-model="wtp.openAlarm" active-color="#13ce66" inactive-color="#ff4949" />
        </el-form-item>
        <el-form-item v-if="wtp.openAlarm" label="活跃告警(%)" label-width="140px">
          <el-input-number v-model="wtp.poolAlarmThreshold" controls-position="right" :min="0" :max="100" />
        </el-form-item>
        <el-alert
          v-if="SynchronousQueueAndLinkedTransferQueue"
          :closable="false"
          title="当前 queueName 不支持设置队列容量告警."
          type="warning"
          close-text="知道了"
        />
        <el-form-item v-if="wtp.openAlarm" label="队列容量告警(%)" label-width="140px">
          <el-input-number
            v-model="wtp.queueAlarmThreshold"
            :disabled="SynchronousQueueAndLinkedTransferQueue"
            controls-position="right"
            :min="0"
            :max="100"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateWtpVisible = false,SynchronousQueueAndLinkedTransferQueue = false">取 消</el-button>
        <el-popconfirm
          confirm-button-text="好的"
          cancel-button-text="不用了"
          icon="el-icon-info"
          icon-color="red"
          title="确定修改吗？"
          @onConfirm="updateWtp"
        >
          <el-button slot="reference" type="primary">确 定</el-button>
        </el-popconfirm>
      </div>
    </el-dialog>

    <el-dialog title="实时数据" :visible.sync="realTimeVisible" @close="closeRealTimeVisible">
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
        <el-form-item label="ip" label-width="120px">
          <span>{{ wtpLog.ip }}</span>
        </el-form-item>
        <el-divider />
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
          <span>{{ numberPercentDiv(wtpLog.activeCount, wtpLog.maximumPoolSize, 2) }}</span>
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
          <span>{{ numberPercentDiv(wtpLog.queueSize, wtpLog.queueSize + wtpLog.queueRemainingCapacity, 2) }}</span>
        </el-form-item>
        <el-form-item label="rejected次数" label-width="120px">
          <span>{{ wtpLog.rejectedExecutionCount }}</span>
        </el-form-item>
        <el-form-item label="总执行时间" label-width="120px">
          <span>{{ wtpLog.totalTime }}</span>
        </el-form-item>
        <el-form-item label="平均执行时间" label-width="120px">
          <span>{{ wtpLog.totalTime === 0 ? 0 : numberDiv(wtpLog.totalTime, wtpLog.completedTaskCount, 0) }}</span>
        </el-form-item>
        <el-form-item label="更新时间" label-width="120px">
          <span>{{ wtpLog.logTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="" :visible.sync="reportVisible" fullscreen>
      <div class="el-dialog-div">
        <chart id="usageRateChart" :chart-option="usageRateOptionc" height="90%" width="100%" />
      </div>
      <div class="el-dialog-div">
        <chart id="poolChart" :chart-option="poolOptionc" height="90%" width="100%" />
      </div>
      <div class="el-dialog-div">
        <chart id="queueChart" :chart-option="queueOptionc" height="90%" width="100%" />
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
    <el-dialog title="选择机器" :visible.sync="wtpRegistryVisible" width="30%" center>
      <el-select v-model="reportForm.ip" filterable placeholder="可搜索">
        <el-option v-for="item in wtpRegistryList" :key="item.ip" :label="item.ip" :value="item.ip" />
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="wtpRegistryVisible = false">取 消</el-button>
        <el-button type="primary" @click="doReport">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="同步配置" :visible.sync="syncConfigVisible">
      <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
      <div style="margin: 15px 0;" />
      <el-checkbox-group v-model="syncConfigCheckedCities" @change="handleCheckedCitiesChange">
        <el-checkbox
          v-for="cluster in clusterIdOptions"
          :key="cluster.clusterId"
          :label="cluster.clusterId"
          :disabled="pageQuery.clusterId!=null && cluster.clusterId === pageQuery.clusterId"
        >{{
          cluster.clusterId
        }}
        </el-checkbox>
      </el-checkbox-group>
      <span slot="footer" class="dialog-footer">
        <el-button @click="syncConfigVisible = false">取 消</el-button>
        <el-popconfirm
          confirm-button-text="好的"
          cancel-button-text="不用了"
          icon="el-icon-info"
          icon-color="red"
          title="同步后当前配置将覆盖被同步的cluster,确定同步吗？"
          @onConfirm="syncConfigBtn()"
        >
          <el-button slot="reference" type="primary">确 定</el-button>
        </el-popconfirm>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  mapGetters
} from 'vuex'
import {
  appOptions
} from '@/api/app'
import {
  clusterOptions
} from '@/api/cluster'
import {
  page,
  create,
  queueOptions,
  rejectedOptions,
  get,
  update,
  del,
  syncConfig
} from '@/api/wtp'
import {
  realTime,
  report
} from '@/api/wtpLog'
import {
  findWtpRegistry
} from '@/api/wtp-registry'
import {
  parseTime
} from '@/utils'
import {
  checkSuperAdmin,
  checkAdmin,
  checkAppAdmin,
  checkAppPermission
} from '@/utils/token-utils'
import Pagination from '@/components/Pagination'
import Chart from '@/components/Charts/Line'

export default {
  name: 'WtpList',
  components: {
    Pagination,
    Chart
  },
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
      rejectedListOptions: [],
      wtpLog: {},
      wtp: {},
      poolOptionc: {},
      queueOptionc: {},
      usageRateOptionc: {},
      wtpRegistryList: [],
      intervalId: null,
      wtpRegistryVisible: false,
      reportForm: {},
      queueCapacity: false,
      syncConfigVisible: false,
      syncConfigCheckedCities: [],
      checkAll: false,
      isIndeterminate: true,
      syncWtp: null,
      SynchronousQueueAndLinkedTransferQueue: false,
      PriorityBlockingQueue: false
    }
  },
  computed: {
    ...mapGetters([
      'roles',
      'permissions'
    ])
  },
  created() {
    this.appOptions()
    this.queueOptions()
    this.rejectedOptions()
  },
  methods: {
    updateUueueChange(value) {
      if (value === 'LinkedTransferQueue' || value === 'SynchronousQueue') {
        this.wtp.queueCapacity = 0
        this.wtp.queueAlarmThreshold = 0
        this.SynchronousQueueAndLinkedTransferQueue = true
      } else {
        this.SynchronousQueueAndLinkedTransferQueue = false
      }
      this.PriorityBlockingQueue = value === 'PriorityBlockingQueue'
    },
    createUueueChange(value) {
      if (value === 'LinkedTransferQueue' || value === 'SynchronousQueue') {
        this.createForm.queueCapacity = 0
        this.createForm.queueAlarmThreshold = 0
        this.SynchronousQueueAndLinkedTransferQueue = true
      } else {
        this.SynchronousQueueAndLinkedTransferQueue = false
      }
      this.PriorityBlockingQueue = value === 'PriorityBlockingQueue'
    },
    handleCheckAllChange(val) {
      const temp = []
      if (val) {
        for (var i = 0; i < this.clusterIdOptions.length; i++) {
          temp.push(this.clusterIdOptions[i].clusterId)
        }
      }
      this.syncConfigCheckedCities = temp
      console.log(this.syncConfigCheckedCities)
      this.isIndeterminate = false
    },
    handleCheckedCitiesChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.clusterIdOptions.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.clusterIdOptions.length
      console.log(this.syncConfigCheckedCities)
    },
    queueOptions() {
      queueOptions().then((response) => {
        this.queueListOptions = response.data.list
      })
    },
    rejectedOptions() {
      rejectedOptions().then((response) => {
        this.rejectedListOptions = response.data.list
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
      if (this.checkEmpty(this.createForm)) {
        return
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
          this.SynchronousQueueAndLinkedTransferQueue = false
          this.PriorityBlockingQueue = false
          this.page()
        } else {
          this.$message.error('添加失败')
        }
      })
    },
    closeRealTimeVisible() {
      window.clearInterval(this.intervalId)
      this.realTimeVisible = false
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
      this.reportForm.appId = appId
      this.reportForm.clusterId = clusterId
      this.reportForm.name = name
      findWtpRegistry(this.pageQuery).then((response) => {
        const wtpRegistryList = response.data
        this.wtpRegistryList = wtpRegistryList
        if (wtpRegistryList === null || wtpRegistryList.length === 0) {
          this.$message.warning('无在线机器')
          return
        }
        if (wtpRegistryList.length === 1) {
          this.doReport()
          return
        }
        if (wtpRegistryList.length > 1) {
          //
          this.wtpRegistryVisible = true
        }
      })
    },
    doReport() {
      this.poolOptionc = poolSeries
      this.queueOptionc = queueSeries
      this.usageRateOptionc = usageRateSeries
      report(this.reportForm).then((response) => {
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
          poolUsageRateList.push(this.numberPercentDiv(wtpLog.activeCount, wtpLog.maximumPoolSize, 2))
          queueUsageRateList.push(this.numberPercentDiv(wtpLog.queueSize, wtpLog.queueSize + wtpLog.queueRemainingCapacity,
            2))
        }
        this.pollChart(timeList, corePoolSizeList, maximumPoolSizeList, activeCountList)
        this.queueChart(timeList, queueSizeList, queueResidueList, queueUseList)
        this.usageRateChart(timeList, poolUsageRateList, queueUsageRateList)
        this.reportVisible = true
      })
    },
    checkEmpty(wtp) {
      if (!wtp.appId) {
        this.$message.error('appId 不能为空')
        return true
      }
      if (!wtp.clusterId) {
        this.$message.error('clusterId 不能为空')
        return true
      }
      if (!wtp.name) {
        this.$message.error('poolName 不能为空')
        return true
      }
      if (!wtp.corePoolSize) {
        this.$message.error('corePoolSize 不能为空')
        return true
      }
      if (!wtp.maximumPoolSize) {
        this.$message.error('maximumPoolSize 不能为空')
        return true
      }
      if (wtp.corePoolSize > wtp.maximumPoolSize) {
        this.$message.error('corePoolSize 不能大于 maximumPoolSize')
        return true
      }
      if (!wtp.keepAliveSeconds) {
        this.$message.error('keepAliveSeconds 不能为空')
        return true
      }
      if (wtp.queueCapacity == null) {
        this.$message.error('queueCapacity 不能为空')
        return true
      }
      if (!wtp.queueName) {
        this.$message.error('queueName 不能为空')
        return true
      }
      if (!wtp.ownerName) {
        this.$message.error('负责人 不能为空')
        return true
      }
      if (wtp.openAlarm) {
        if (!wtp.alarmEmail) {
          this.$message.error('告警邮箱 不能为空')
          return true
        }
        if (!wtp.poolAlarmThreshold) {
          this.$message.error('活跃告警 不能为空')
          return true
        }
        if (wtp.queueAlarmThreshold == null) {
          this.$message.error('队列容量告警 不能为空')
          return true
        }
      }
    },
    updateWtp: function() {
      if (this.checkEmpty(this.wtp)) {
        return
      }
      update(this.wtp).then((response) => {
        if (response.data) {
          this.$message.success('修改成功')
          this.updateWtpVisible = false
          this.SynchronousQueueAndLinkedTransferQueue = false
          this.PriorityBlockingQueue = false
          this.page()
        } else {
          this.$message.error('修改失败')
        }
      })
    },
    updateWtpBtn(wtpId) {
      get(wtpId).then((response) => {
        this.wtp = response.data
        this.SynchronousQueueAndLinkedTransferQueue = false
        this.PriorityBlockingQueue = false
        this.updateUueueChange(this.wtp.queueName)
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
    usageRateChart(timeList, poolUsageRateList, queueUsageRateList) {
      this.usageRateOptionc.xAxis[0].data = timeList
      this.usageRateOptionc.series[0].data = poolUsageRateList
      this.usageRateOptionc.series[1].data = queueUsageRateList
    },
    numberPercentDiv(arg1, arg2, digit) {
      return ((arg1 / arg2) * 100).toFixed(digit)
    },
    numberDiv(arg1, arg2, digit) {
      return (arg1 / arg2).toFixed(digit)
    },
    check(appId, permission) {
      return (checkSuperAdmin(this.roles) || checkAdmin(this.roles) || checkAppAdmin(appId, this.permissions) ||
        checkAppPermission(appId, permission, this.permissions))
    },
    delWtpBtn(wtpId) {
      del({
        wtpId: wtpId
      }).then((response) => {
        if (response.data) {
          this.$message.success('删除成功')
          this.page()
        } else {
          this.$message.error('删除失败')
        }
      })
    },
    registryVisibleBtn() {
      findWtpRegistry(this.pageQuery).then((response) => {
        this.wtpRegistryList = response.data
      })
      this.registryVisible = true
    },
    syncConfigVisibleBtn(wtp) {
      this.syncWtp = wtp
      this.syncConfigVisible = true
    },
    syncConfigBtn() {
      if (this.syncConfigCheckedCities.length < 1) {
        this.$message.error('未选择同步目标')
        return
      }
      const form = JSON.parse(JSON.stringify(this.syncWtp))
      form.clusterIds = encodeURIComponent(JSON.stringify(this.syncConfigCheckedCities))
      syncConfig(form).then((response) => {
        const sync = response.data
        if (sync) {
          this.$message.success('同步成功')
          this.syncConfigVisible = false
          this.page()
        } else {
          this.$message.error('同步失败')
        }
      })
    },
    toWtpLog(name) {
      const query = {
        appId: this.pageQuery.appId,
        clusterId: this.pageQuery.clusterId,
        name: name
      }
      this.$router.push({
        path: '/log/wtpLog',
        query: query
      })
    }
  }
}

const poolSeries = {
  'backgroundColor': '#394056',
  'title': {
    'top': 20,
    'text': '线程',
    'textStyle': {
      'fontWeight': 'normal',
      'fontSize': 16,
      'color': '#F1F1F3'
    },
    'left': '1%'
  },
  'tooltip': {
    'trigger': 'axis',
    'axisPointer': {
      'lineStyle': {
        'color': '#57617B'
      }
    }
  },
  'legend': {
    'top': 20,
    'icon': 'rect',
    'itemWidth': 14,
    'itemHeight': 5,
    'itemGap': 13,
    'data': ['核心数', '最大数', '活跃数'],
    'right': '4%',
    'textStyle': {
      'fontSize': 12,
      'color': '#F1F1F3'
    }
  },
  'grid': {
    'top': 100,
    'left': '2%',
    'right': '2%',
    'bottom': '2%',
    'containLabel': true
  },
  'xAxis': [{
    'type': 'category',
    'boundaryGap': false,
    'axisLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    },
    'data': [1596086931334, 1596086870942, 1596086810128, 1596086749757, 1596086689170, 1596086628385,
      1596086567051, 1596086506079, 1596086445250, 1596086384794, 1596086324180, 1596086263434, 1596086197236,
      1596086136786, 1596086076191, 1596086015792, 1596085954903, 1596085894503, 1596085834127, 1596085773539,
      1596085712716, 1596085652030, 1596085591593, 1596085529080, 1596085467564, 1596085406534, 1596085345818,
      1596085276424, 1596085209879
    ]
  }],
  'yAxis': [{
    'type': 'value',
    'name': '',
    'axisTick': {
      'show': false
    },
    'axisLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    },
    'axisLabel': {
      'margin': 10,
      'textStyle': {
        'fontSize': 14
      }
    },
    'splitLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    }
  }],
  'series': [{
    'name': '核心数',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(137, 189, 27, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(137, 189, 27, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(137,189,27)',
        'borderColor': 'rgba(137,189,2,0.27)',
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }, {
    'name': '最大数',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(219, 50, 51, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(219, 50, 51, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(219,50,51)',
        'borderColor': "rgba(219,50,51,0.2)'",
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }, {
    'name': '活跃数',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(0, 136, 212, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(0, 136, 212, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(0,136,212)',
        'borderColor': 'rgba(0,136,212,0.2)',
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }]
}
const queueSeries = {
  'backgroundColor': '#394056',
  'title': {
    'top': 20,
    'text': '队列',
    'textStyle': {
      'fontWeight': 'normal',
      'fontSize': 16,
      'color': '#F1F1F3'
    },
    'left': '1%'
  },
  'tooltip': {
    'trigger': 'axis',
    'axisPointer': {
      'lineStyle': {
        'color': '#57617B'
      }
    }
  },
  'legend': {
    'top': 20,
    'icon': 'rect',
    'itemWidth': 14,
    'itemHeight': 5,
    'itemGap': 13,
    'data': ['剩余长度', '最大长度', '排队长度'],
    'right': '4%',
    'textStyle': {
      'fontSize': 12,
      'color': '#F1F1F3'
    }
  },
  'grid': {
    'top': 100,
    'left': '2%',
    'right': '2%',
    'bottom': '2%',
    'containLabel': true
  },
  'xAxis': [{
    'type': 'category',
    'boundaryGap': false,
    'axisLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    },
    'data': [1596086931334, 1596086870942, 1596086810128, 1596086749757, 1596086689170, 1596086628385,
      1596086567051, 1596086506079, 1596086445250, 1596086384794, 1596086324180, 1596086263434, 1596086197236,
      1596086136786, 1596086076191, 1596086015792, 1596085954903, 1596085894503, 1596085834127, 1596085773539,
      1596085712716, 1596085652030, 1596085591593, 1596085529080, 1596085467564, 1596085406534, 1596085345818,
      1596085276424, 1596085209879
    ]
  }],
  'yAxis': [{
    'type': 'value',
    'name': '',
    'axisTick': {
      'show': false
    },
    'axisLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    },
    'axisLabel': {
      'margin': 10,
      'textStyle': {
        'fontSize': 14
      }
    },
    'splitLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    }
  }],
  'series': [{
    'name': '剩余长度',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(137, 189, 27, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(137, 189, 27, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(137,189,27)',
        'borderColor': 'rgba(137,189,2,0.27)',
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }, {
    'name': '最大长度',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(219, 50, 51, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(219, 50, 51, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(219,50,51)',
        'borderColor': "rgba(219,50,51,0.2)'",
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }, {
    'name': '排队长度',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(0, 136, 212, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(0, 136, 212, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(0,136,212)',
        'borderColor': 'rgba(0,136,212,0.2)',
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }]
}
const usageRateSeries = {
  'backgroundColor': '#394056',
  'title': {
    'top': 20,
    'text': '使用率',
    'textStyle': {
      'fontWeight': 'normal',
      'fontSize': 16,
      'color': '#F1F1F3'
    },
    'left': '1%'
  },
  'tooltip': {
    'trigger': 'axis',
    'axisPointer': {
      'lineStyle': {
        'color': '#57617B'
      }
    }
  },
  'legend': {
    'top': 20,
    'icon': 'rect',
    'itemWidth': 14,
    'itemHeight': 5,
    'itemGap': 13,
    'data': ['线程使用率', '队列使用率'],
    'right': '4%',
    'textStyle': {
      'fontSize': 12,
      'color': '#F1F1F3'
    }
  },
  'grid': {
    'top': 100,
    'left': '2%',
    'right': '2%',
    'bottom': '2%',
    'containLabel': true
  },
  'xAxis': [{
    'type': 'category',
    'boundaryGap': false,
    'axisLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    },
    'data': [1596086931334, 1596086870942, 1596086810128, 1596086749757, 1596086689170, 1596086628385,
      1596086567051, 1596086506079, 1596086445250, 1596086384794, 1596086324180, 1596086263434, 1596086197236,
      1596086136786, 1596086076191, 1596086015792, 1596085954903, 1596085894503, 1596085834127, 1596085773539,
      1596085712716, 1596085652030, 1596085591593, 1596085529080, 1596085467564, 1596085406534, 1596085345818,
      1596085276424, 1596085209879
    ]
  }],
  'yAxis': [{
    'type': 'value',
    'name': '',
    'axisTick': {
      'show': false
    },
    'axisLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    },
    'axisLabel': {
      'margin': 10,
      'textStyle': {
        'fontSize': 14
      }
    },
    'splitLine': {
      'lineStyle': {
        'color': '#57617B'
      }
    }
  }],
  'series': [{
    'name': '线程使用率',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(255, 106, 106, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(255, 106, 106, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(255, 106, 106)',
        'borderColor': 'rgba(255, 106, 106,0.27)',
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }, {
    'name': '队列使用率',
    'type': 'line',
    'smooth': true,
    'symbol': 'circle',
    'symbolSize': 5,
    'showSymbol': false,
    'lineStyle': {
      'normal': {
        'width': 1
      }
    },
    'areaStyle': {
      'normal': {
        'color': {
          'x': 0,
          'y': 0,
          'x2': 0,
          'y2': 1,
          'type': 'linear',
          'global': false,
          'colorStops': [{
            'offset': 0,
            'color': 'rgba(255, 130, 71, 0.3)'
          }, {
            'offset': 0.8,
            'color': 'rgba(255, 130, 71, 0)'
          }]
        },
        'shadowColor': 'rgba(0, 0, 0, 0.1)',
        'shadowBlur': 10
      }
    },
    'itemStyle': {
      'normal': {
        'color': 'rgb(255, 130, 71)',
        'borderColor': 'rgba(255, 130, 71,0.2)',
        'borderWidth': 12
      }
    },
    'data': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  }]
}
</script>

<style scoped>
.el-dialog-div {
  height: 500px;
  overflow: auto;
}
</style>
<style>
.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
</style>
<style rel="stylesheet/scss" lang="scss" scoped>
.query-box {
  display: flex;
  align-items: center;
}

.mr10 {
  margin-right: 10px;
}

.mtb10 {
  margin: 10px 0;
}

audio {
  width: 50%;
}
</style>
