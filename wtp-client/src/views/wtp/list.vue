<template>
  <div class="app-container">
    <div class="table-query">
      <span class="mr10">
        <el-select
          v-model="pageQuery.appId"
          filterable
          :placeholder="$t('wtp.appId_searchable')"
          @change="appIdOptionsChange"
        >
          <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
        </el-select>
      </span>
      <span class="mr10">
        <el-select
          v-model="pageQuery.clusterId"
          :disabled="clusterIdOptions.length <= 0"
          filterable
          :placeholder="$t('wtp.clusterId_searchable')"
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
          :placeholder="$t('wtp.name')"
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
        {{ $t('wtp.search') }}
      </el-button>
      <span class="mr10">
        <el-button
          v-if="check(pageQuery.appId,'INSERT')"
          :disabled="!pageQuery.clusterId"
          type="primary"
          @click="newWtpVisible = true"
        >{{ $t('wtp.add') }}
        </el-button>
      </span>
      <span class="mr10">
        <el-button :disabled="!pageQuery.clusterId" type="primary" @click="registryVisibleBtn">{{
          $t('wtp.onLine') + '( ' +
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
      <el-table-column align="center" :label="$t('wtp.wtpId')" width="60">
        <template slot-scope="scope">
          <span>{{ scope.row.wtpId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('wtp.appId')" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.appId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('wtp.clusterId')">
        <template slot-scope="scope">
          <span>{{ scope.row.clusterId }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.title')">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.name')">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.corePoolSize')" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.corePoolSize }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.maximumPoolSize')" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.maximumPoolSize }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.keepAliveSeconds')" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.keepAliveSeconds }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.queueCapacity')" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.queueCapacity }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.queueName')">
        <template slot-scope="scope">
          <span>{{ scope.row.queueName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtp.rejectedExecutionHandlerName')">
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
            >{{ $t('wtp.edit') }}
            </el-button>
            <el-tooltip class="item" effect="dark" :content="$t('wtp.sync_content')" placement="top-end">
              <el-button
                v-if="check(scope.row.appId,'UPDATE')"
                type="primary"
                size="small"
                @click="syncConfigVisibleBtn(scope.row)"
              >{{
                $t('wtp.sync')
              }}
              </el-button>
            </el-tooltip>
            <el-popconfirm
              :confirm-button-text="$t('wtp.del_confirm_button_text')"
              :cancel-button-text="$t('wtp.del_cancel_button_text')"
              icon="el-icon-info"
              icon-color="red"
              :title="$t('wtp.del_title')"
              @onConfirm="delWtpBtn(scope.row.wtpId)"
            >
              <el-button v-if="check(scope.row.appId,'DELETE')" slot="reference" type="danger" size="small">
                {{ $t('wtp.del') }}
              </el-button>
            </el-popconfirm>
          </el-button-group>
          <el-button-group style="margin-top: 8px;">
            <el-button
              type="primary"
              size="small"
              @click="toWtpLog(scope.row.name)"
            >{{ $t('wtp.log') }}
            </el-button>
            <el-tooltip class="item" effect="dark" :content="$t('wtp.last_log')" placement="bottom-end">
              <el-button
                type="primary"
                size="small"
                @click="realTime(scope.row.appId, scope.row.clusterId, scope.row.name)"
              >{{ $t('wtp.last_log') }}
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" :content="$t('wtp.report_content')" placement="bottom-end">
              <el-button
                type="primary"
                size="small"
                @click="report(scope.row.appId, scope.row.clusterId, scope.row.name)"
              >{{ $t('wtp.report') }}
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

    <el-dialog :title="$t('wtp.add')" :visible.sync="newWtpVisible">
      <el-form :model="createForm">
        <el-form-item :label="$t('wtp.appId')" label-width="140px">
          <el-select v-model="createForm.appId" filterable :placeholder="$t('wtp.searchable')">
            <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('wtp.clusterId')" label-width="140px">
          <el-select v-model="createForm.clusterId" filterable :placeholder="$t('wtp.searchable')">
            <el-option
              v-for="item in clusterIdOptions"
              :key="item.clusterId"
              :label="item.clusterId"
              :value="item.clusterId"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('wtp.title')" label-width="140px">
          <el-input v-model="createForm.title" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.name')" label-width="140px">
          <el-input v-model="createForm.name" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.corePoolSize')" label-width="140px">
          <el-input-number
            v-model="createForm.corePoolSize"
            controls-position="right"
            :min="0"
            :max="createForm.maximumPoolSize?createForm.maximumPoolSize:2147483647"
          />
        </el-form-item>
        <el-form-item :label="$t('wtp.maximumPoolSize')" label-width="140px">
          <el-input-number
            v-model="createForm.maximumPoolSize"
            controls-position="right"
            :min="createForm.corePoolSize? createForm.corePoolSize : 0"
            :max="2147483647"
          />
        </el-form-item>
        <el-form-item :label="$t('wtp.keepAliveSeconds')" label-width="140px">
          <el-input-number v-model="createForm.keepAliveSeconds" controls-position="right" :min="1" />
        </el-form-item>
        <el-alert
          v-if="SynchronousQueueAndLinkedTransferQueue"
          :closable="false"
          :title="$t('wtp.SynchronousQueueAndLinkedTransferQueue')"
          type="warning"
        />
        <el-alert
          v-if="PriorityBlockingQueue"
          :closable="false"
          :title="$t('wtp.PriorityBlockingQueue')"
          type="warning"
        />
        <el-form-item :label="$t('wtp.queueCapacity')" label-width="140px">
          <el-input-number
            v-model="createForm.queueCapacity"
            controls-position="right"
            :disabled="SynchronousQueueAndLinkedTransferQueue"
            :min="0"
            :max="SynchronousQueueAndLinkedTransferQueue ? 0 : 9223372036854775807"
          />
        </el-form-item>
        <el-form-item :label="$t('wtp.queueName')" label-width="140px">
          <el-select
            v-model="createForm.queueName"
            filterable
            :placeholder="$t('wtp.searchable')"
            @change="createQueueChange"
          >
            <el-option v-for="item in queueListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('wtp.rejectedExecutionHandlerName')" label-width="140px">
          <el-select v-model="createForm.rejectedExecutionHandlerName" filterable :placeholder="$t('wtp.searchable')">
            <el-option v-for="item in rejectedListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('wtp.ownerName')" label-width="140px">
          <el-input v-model="createForm.ownerName" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.alarmEmail')" label-width="140px">
          <el-input v-model="createForm.alarmEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.openAlarm')" label-width="140px">
          <el-switch v-model="createForm.openAlarm" active-color="#13ce66" inactive-color="#ff4949" />
        </el-form-item>
        <el-form-item v-if="createForm.openAlarm" :label="$t('wtp.poolAlarmThreshold')" label-width="140px">
          <el-input-number v-model="createForm.poolAlarmThreshold" controls-position="right" :min="0" :max="100" />
        </el-form-item>
        <el-alert
          v-if="createForm.openAlarm && createForm.queueName && (createForm.queueName === 'LinkedTransferQueue' || createForm.queueName === 'SynchronousQueue')"
          :closable="false"
          :title="$t('queueAlarmThreshold_title')"
          type="warning"
          close-text="知道了"
        />
        <el-form-item v-if="createForm.openAlarm" :label="$t('wtp.queueAlarmThreshold')" label-width="140px">
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
        <el-button @click="newWtpVisible = false">{{ $t('wtp.cancel') }}</el-button>
        <el-button type="primary" @click="createWtp">{{ $t('wtp.confirm') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('wtp.update')" :visible.sync="updateWtpVisible">
      <el-form :model="wtp">
        <el-form-item :label="$t('wtp.appId')" label-width="140px">
          <el-input v-model="wtp.appId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.clusterId')" label-width="140px">
          <el-input v-model="wtp.clusterId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.title')" label-width="140px">
          <el-input v-model="wtp.title" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.name')" label-width="140px">
          <el-input v-model="wtp.name" :disabled="true" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.corePoolSize')" label-width="140px">
          <el-input-number v-model="wtp.corePoolSize" controls-position="right" :min="0" :max="wtp.maximumPoolSize" />
        </el-form-item>
        <el-form-item :label="$t('wtp.maximumPoolSize')" label-width="140px">
          <el-input-number
            v-model="wtp.maximumPoolSize"
            controls-position="right"
            :min="wtp.corePoolSize"
            :max="2147483647"
          />
        </el-form-item>
        <el-form-item :label="$t('wtp.keepAliveSeconds')" label-width="140px">
          <el-input-number v-model="wtp.keepAliveSeconds" controls-position="right" :min="1" />
        </el-form-item>
        <el-alert
          v-if="wtp.queueName && wtp.queueName.indexOf('ResizableCapacity') < 0"
          :closable="false"
          :title="$t('wtp.queueName_title')"
          type="warning"
        />
        <el-alert
          v-if="SynchronousQueueAndLinkedTransferQueue"
          :closable="false"
          :title="$t('wtp.SynchronousQueueAndLinkedTransferQueue')"
          type="warning"
        />
        <el-alert
          v-if="PriorityBlockingQueue"
          :closable="false"
          :title="$t('wtp.PriorityBlockingQueue')"
          type="warning"
        />
        <el-form-item :label="$t('wtp.queueCapacity')" label-width="140px">
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
          :title="$t('wtp.queueName_title2')"
          type="warning"
          close-text="知道了"
        />
        <el-form-item :label="$t('wtp.queueName')" label-width="140px">
          <el-select v-model="wtp.queueName" filterable :placeholder="$t('wtp.searchable')" @change="updateQueueChange">
            <el-option v-for="item in queueListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('wtp.rejectedExecutionHandlerName')" label-width="140px">
          <el-select v-model="wtp.rejectedExecutionHandlerName" filterable :placeholder="$t('wtp.searchable')">
            <el-option v-for="item in rejectedListOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('wtp.ownerName')" label-width="140px">
          <el-input v-model="wtp.ownerName" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.alarmEmail')" label-width="140px">
          <el-input v-model="wtp.alarmEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('wtp.openAlarm')" label-width="140px">
          <el-switch v-model="wtp.openAlarm" active-color="#13ce66" inactive-color="#ff4949" />
        </el-form-item>
        <el-form-item v-if="wtp.poolAlarmThreshold" :label="$t('wtp.poolAlarmThreshold')" label-width="140px">
          <el-input-number v-model="wtp.poolAlarmThreshold" controls-position="right" :min="0" :max="100" />
        </el-form-item>
        <el-alert
          v-if="SynchronousQueueAndLinkedTransferQueue"
          :closable="false"
          :title="$t('wtp.queueAlarmThreshold_title')"
          type="warning"
        />
        <el-form-item v-if="wtp.openAlarm" :label="$t('wtp.queueAlarmThreshold')" label-width="140px">
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
        <el-button @click="updateWtpVisible = false,SynchronousQueueAndLinkedTransferQueue = false">{{
          $t('wtp.cancel')
        }}
        </el-button>
        <el-popconfirm
          :confirm-button-text="$t('wtp.del_confirm_button_text')"
          :cancel-button-text="$t('wtp.del_cancel_button_text')"
          icon="el-icon-info"
          icon-color="red"
          :title="$t('wtp.update_title')"
          @onConfirm="updateWtp"
        >
          <el-button slot="reference" type="primary">{{ $t('wtp.confirm') }}</el-button>
        </el-popconfirm>
      </div>
    </el-dialog>

    <el-dialog :title="$t('wtp.last_log')" :visible.sync="realTimeVisible" @close="closeRealTimeVisible">
      <el-form :model="wtpLog" label-position="left" class="demo-table-expand">
        <el-form-item :label="$t('wtp.appId')" label-width="200px">
          <span>{{ wtpLog.appId }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.clusterId')" label-width="200px">
          <span>{{ wtpLog.clusterId }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.name')" label-width="200px">
          <span>{{ wtpLog.name }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.ip')" label-width="200px">
          <span>{{ wtpLog.ip }}</span>
        </el-form-item>
        <el-divider />
        <el-form-item :label="$t('wtp.corePoolSize')" label-width="200px">
          <span>{{ wtpLog.corePoolSize }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.maximumPoolSize')" label-width="200px">
          <span>{{ wtpLog.maximumPoolSize }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.keepAliveSeconds')" label-width="200px">
          <span>{{ wtpLog.keepAliveSeconds }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.activeCount')" label-width="200px">
          <span>{{ wtpLog.activeCount }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.threadRate')" label-width="200px">
          <span>{{ numberPercentDiv(wtpLog.activeCount, wtpLog.maximumPoolSize, 2) }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.completedTaskCount')" label-width="200px">
          <span>{{ wtpLog.completedTaskCount }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.queueLength')" label-width="200px">
          <span>{{ wtpLog.queueSize + wtpLog.queueRemainingCapacity }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.queueSize')" label-width="200px">
          <span>{{ wtpLog.queueSize }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.queueRemainingCapacity')" label-width="200px">
          <span>{{ wtpLog.queueRemainingCapacity }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.queueRate')" label-width="200px">
          <span>{{ numberPercentDiv(wtpLog.queueSize, wtpLog.queueSize + wtpLog.queueRemainingCapacity, 2) }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.rejectedExecutionCount')" label-width="200px">
          <span>{{ wtpLog.rejectedExecutionCount }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.totalTime')" label-width="200px">
          <span>{{ wtpLog.totalTime }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.averageTime')" label-width="200px">
          <span>{{ wtpLog.totalTime === 0 ? 0 : numberDiv(wtpLog.totalTime, wtpLog.completedTaskCount, 0) }}</span>
        </el-form-item>
        <el-form-item :label="$t('wtp.logTime')" label-width="200px">
          <span>{{ wtpLog.logTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="" :visible.sync="reportVisible" fullscreen>
      <div class="el-dialog-div">
        <chart id="usageRateChart" :chart-option="usageRateOptions" height="90%" width="100%" />
      </div>
      <div class="el-dialog-div">
        <chart id="poolChart" :chart-option="poolOptions" height="90%" width="100%" />
      </div>
      <div class="el-dialog-div">
        <chart id="queueChart" :chart-option="queueOptions" height="90%" width="100%" />
      </div>
    </el-dialog>

    <el-dialog :title="$t('wtp.onLine')" :visible.sync="registryVisible">
      <el-table :data="wtpRegistryList">
        <el-table-column align="center" :label="$t('wtp.ip')">
          <template slot-scope="scope">
            <span>{{ scope.row.ip }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('wtp.last_pull_time')">
          <template slot-scope="scope">
            <span>{{ scope.row.lastPullTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('wtp.registration_time')">
          <template slot-scope="scope">
            <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog :title="$t('wtp.select')" :visible.sync="wtpRegistryVisible" width="30%" center>
      <el-select v-model="reportForm.ip" filterable placeholder="$t('wtp.searchable')">
        <el-option v-for="item in wtpRegistryList" :key="item.ip" :label="item.ip" :value="item.ip" />
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="wtpRegistryVisible = false">{{ $t('wtp.cancel') }}</el-button>
        <el-button type="primary" @click="doReport">{{ $t('wtp.confirm') }}</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="$t('wtp.sync')" :visible.sync="syncConfigVisible">
      <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">
        {{ $t('wtp.check_all') }}
      </el-checkbox>
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
        <el-button @click="syncConfigVisible = false">{{ $t('wtp.cancel') }}</el-button>
        <el-popconfirm
          :confirm-button-text="$t('wtp.del_confirm_button_text')"
          :cancel-button-text="$t('wtp.del_cancel_button_text')"
          icon="el-icon-info"
          icon-color="red"
          :title="$t('wtp.sync_title')"
          @onConfirm="syncConfigBtn()"
        >
          <el-button slot="reference" type="primary">{{ $t('wtp.confirm') }}</el-button>
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
      poolOptions: {},
      queueOptions: {},
      usageRateOptions: {},
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
    this.queryQueueOptions()
    this.rejectedOptions()
  },
  methods: {
    updateQueueChange(value) {
      if (value === 'LinkedTransferQueue' || value === 'SynchronousQueue') {
        this.wtp.queueCapacity = 0
        this.wtp.queueAlarmThreshold = 0
        this.SynchronousQueueAndLinkedTransferQueue = true
      } else {
        this.SynchronousQueueAndLinkedTransferQueue = false
      }
      this.PriorityBlockingQueue = value === 'PriorityBlockingQueue'
    },
    createQueueChange(value) {
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
    queryQueueOptions() {
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
          this.$message.success(this.$t('wtp.success'))
          this.newWtpVisible = false
          this.createForm = {
            appId: this.createForm.appId,
            clusterId: this.createForm.clusterId
          }
          this.SynchronousQueueAndLinkedTransferQueue = false
          this.PriorityBlockingQueue = false
          this.page()
        } else {
          this.$message.error(this.$t('wtp.fail'))
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
          this.$message.warning(this.$t('wtp.no_log'))
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
          this.$message.warning(this.$t('wtp.no_online'))
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
      this.initReportLang()
      this.poolOptions = poolSeries
      this.queueOptions = queueSeries
      this.usageRateOptions = usageRateSeries
      report(this.reportForm).then((response) => {
        const list = response.data
        if (!list || list.length === 0) {
          this.$message.warning(this.$t('wtp.no_log'))
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
        this.$message.error(this.$t('wtp.appId') + this.$t('wtp.not_empty'))
        return true
      }
      if (!wtp.clusterId) {
        this.$message.error(this.$t('wtp.clusterId') + this.$t('wtp.not_empty'))
        return true
      }
      if (!wtp.name) {
        this.$message.error(this.$t('wtp.name') + this.$t('wtp.not_empty'))
        return true
      }
      if (!wtp.corePoolSize) {
        this.$message.error(this.$t('wtp.corePoolSize') + this.$t('wtp.not_empty'))
        return true
      }
      if (!wtp.maximumPoolSize) {
        this.$message.error(this.$t('wtp.maximumPoolSize') + this.$t('wtp.not_empty'))
        return true
      }
      if (wtp.corePoolSize > wtp.maximumPoolSize) {
        this.$message.error(this.$t('wtp.corePoolSize') + this.$t('wtp.not_greater') + this.$t('wtp.maximumPoolSize'))
        return true
      }
      if (!wtp.keepAliveSeconds) {
        this.$message.error(this.$t('wtp.keepAliveSeconds') + this.$t('wtp.not_empty'))
        return true
      }
      if (wtp.queueCapacity == null) {
        this.$message.error(this.$t('wtp.queueCapacity') + this.$t('wtp.not_empty'))
        return true
      }
      if (!wtp.queueName) {
        this.$message.error(this.$t('wtp.queueName') + this.$t('wtp.not_empty'))
        return true
      }
      if (!wtp.ownerName) {
        this.$message.error(this.$t('wtp.ownerName') + this.$t('wtp.not_empty'))
        return true
      }
      if (wtp.openAlarm) {
        if (!wtp.alarmEmail) {
          this.$message.error(this.$t('wtp.alarmEmail') + this.$t('wtp.not_empty'))
          return true
        }
        if (!wtp.poolAlarmThreshold) {
          this.$message.error(this.$t('wtp.poolAlarmThreshold') + this.$t('wtp.not_empty'))
          return true
        }
        if (wtp.queueAlarmThreshold == null) {
          this.$message.error(this.$t('wtp.queueAlarmThreshold') + this.$t('wtp.not_empty'))
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
          this.$message.success(this.$t('wtp.success'))
          this.updateWtpVisible = false
          this.SynchronousQueueAndLinkedTransferQueue = false
          this.PriorityBlockingQueue = false
          this.page()
        } else {
          this.$message.error(this.$t('wtp.fail'))
        }
      })
    },
    updateWtpBtn(wtpId) {
      get(wtpId).then((response) => {
        this.wtp = response.data
        this.SynchronousQueueAndLinkedTransferQueue = false
        this.PriorityBlockingQueue = false
        this.updateQueueChange(this.wtp.queueName)
        this.updateWtpVisible = true
      })
    },
    pollChart(timeList, corePoolSizeList, maximumPoolSizeList, activeCountList) {
      this.poolOptions.xAxis[0].data = timeList
      this.poolOptions.series[0].data = corePoolSizeList
      this.poolOptions.series[1].data = maximumPoolSizeList
      this.poolOptions.series[2].data = activeCountList
    },
    queueChart(timeList, queueSizeList, queueResidueList, queueUseList) {
      this.queueOptions.xAxis[0].data = timeList
      this.queueOptions.series[0].data = queueResidueList
      this.queueOptions.series[1].data = queueSizeList
      this.queueOptions.series[2].data = queueUseList
    },
    usageRateChart(timeList, poolUsageRateList, queueUsageRateList) {
      this.usageRateOptions.xAxis[0].data = timeList
      this.usageRateOptions.series[0].data = poolUsageRateList
      this.usageRateOptions.series[1].data = queueUsageRateList
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
          this.$message.success(this.$t('wtp.success'))
          this.page()
        } else {
          this.$message.error(this.$t('wtp.fail'))
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
        this.$message.error(this.$t('wtp.unselected') + this.$t('wtp.sync') + this.$t('wtp.clusterId'))
        return
      }
      const form = JSON.parse(JSON.stringify(this.syncWtp))
      form.clusterIds = encodeURIComponent(JSON.stringify(this.syncConfigCheckedCities))
      syncConfig(form).then((response) => {
        const sync = response.data
        if (sync) {
          this.$message.success(this.$t('wtp.success'))
          this.syncConfigVisible = false
          this.page()
        } else {
          this.$message.error(this.$t('wtp.fail'))
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
    },
    initReportLang() {
      poolSeries.title.text = this.$t('wtp.thread')
      poolSeries.legend.data = [this.$t('wtp.corePoolSize'), this.$t('wtp.maximumPoolSize'), this.$t('wtp.activeCount')]
      poolSeries.series[0].name = this.$t('wtp.corePoolSize')
      poolSeries.series[1].name = this.$t('wtp.maximumPoolSize')
      poolSeries.series[2].name = this.$t('wtp.activeCount')

      queueSeries.title.text = this.$t('wtp.queue')
      queueSeries.legend.data = [this.$t('wtp.queueRemainingCapacity'), this.$t('wtp.queueLength'), this.$t('wtp.queueSize')]
      queueSeries.series[0].name = this.$t('wtp.queueRemainingCapacity')
      queueSeries.series[1].name = this.$t('wtp.queueLength')
      queueSeries.series[2].name = this.$t('wtp.queueSize')

      usageRateSeries.title.text = this.$t('wtp.use_rate')
      usageRateSeries.legend.data = [this.$t('wtp.threadRate'), this.$t('wtp.queueRate')]
      usageRateSeries.series[0].name = this.$t('wtp.threadRate')
      usageRateSeries.series[1].name = this.$t('wtp.queueRate')
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
