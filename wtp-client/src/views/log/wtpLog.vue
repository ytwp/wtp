<template>
  <div class="app-container">
    <div class="table-query">
      <span class="mr10">
        <el-select v-model="pageQuery.appId" filterable :placeholder="$t('wtpLog.appId_searchable')" @change="appIdOptionsChange">
          <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
        </el-select>
      </span>
      <span class="mr10">
        <el-select
          v-model="pageQuery.clusterId"
          :disabled="clusterIdOptions.length <= 0"
          filterable
          :placeholder="$t('wtpLog.clusterId_searchable')"
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
          :placeholder="$t('wtpLog.name')"
          clearable
        />
      </span>
      <span class="mr10">
        <el-input v-model.trim="pageQuery.ip" style="width: 150px;" class="filter-item" :placeholder="$t('wtpLog.ip')" clearable />
      </span>
      <span class="mr10">
        <el-date-picker
          v-model="queryTime"
          type="datetimerange"
          :picker-options="pickerOptions"
          :start-placeholder="$t('wtpLog.start_date')"
          :end-placeholder="$t('wtpLog.end_date')"
          align="right"
          @change="handleChangeDate"
        />
      </span>
      <el-button class="filter-item mr10" type="primary" icon="el-icon-search" @click="page">
        {{ $t('wtpLog.search') }}
      </el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;margin-top: 20px;"
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item :label="$t('wtpLog.appId')" label-width="200px">
              <span>{{ props.row.appId }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.clusterId')" label-width="200px">
              <span>{{ props.row.clusterId }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.ip')" label-width="200px">
              <span>{{ props.row.ip }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.name')" label-width="200px">
              <span>{{ props.row.name }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.logTime')" label-width="200px">
              <span>{{ props.row.logTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.created')" label-width="200px">
              <span>{{ props.row.created | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
            </el-form-item>
            <el-divider />
            <el-form-item :label="$t('wtpLog.corePoolSize')" label-width="200px">
              <span>{{ props.row.corePoolSize }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.maximumPoolSize')" label-width="200px">
              <span>{{ props.row.maximumPoolSize }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.keepAliveSeconds')" label-width="200px">
              <span>{{ props.row.keepAliveSeconds }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.activeCount')" label-width="200px">
              <span>{{ props.row.activeCount }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.threadRate')" label-width="200px">
              <span>{{ numberPercentDiv(props.row.activeCount, props.row.maximumPoolSize, 2) }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.completedTaskCount')" label-width="200px">
              <span>{{ props.row.completedTaskCount }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.queueLength')" label-width="200px">
              <span>{{ props.row.queueSize + props.row.queueRemainingCapacity }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.queueSize')" label-width="200px">
              <span>{{ props.row.queueSize }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.queueRemainingCapacity')" label-width="200px">
              <span>{{ props.row.queueRemainingCapacity }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.queueRate')" label-width="200px">
              <span>{{
                numberPercentDiv(props.row.queueSize, props.row.queueSize + props.row.queueRemainingCapacity, 2)
              }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.largestPoolSize')" label-width="200px">
              <span>{{ props.row.largestPoolSize }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.rejectedExecutionCount')" label-width="200px">
              <span>{{ props.row.rejectedExecutionCount }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.poolSize')" label-width="200px">
              <span>{{ props.row.poolSize }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.taskCount')" label-width="200px">
              <span>{{ props.row.taskCount }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.totalTime')" label-width="200px">
              <span>{{ props.row.totalTime }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.maximumTime')" label-width="200px">
              <span>{{ props.row.maximumTime }}</span>
            </el-form-item>
            <el-form-item :label="$t('wtpLog.averageTime')" label-width="200px">
              <span>{{ props.row.totalTime === 0 ? 0 : numberDiv(props.row.totalTime, props.row.completedTaskCount, 0) }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('wtpLog.appId')">
        <template slot-scope="scope">
          <span>{{ scope.row.appId }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtpLog.clusterId')">
        <template slot-scope="scope">
          <span>{{ scope.row.clusterId }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtpLog.name')">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtpLog.ip')">
        <template slot-scope="scope">
          <span>{{ scope.row.ip }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtpLog.poolSize')">
        <template slot-scope="scope">
          <span>{{ scope.row.poolSize }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtpLog.queueRemainingCapacity')">
        <template slot-scope="scope">
          <span>{{ scope.row.queueRemainingCapacity }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtpLog.rejectedExecutionCount')">
        <template slot-scope="scope">
          <span>{{ scope.row.rejectedExecutionCount }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('wtpLog.created')">
        <template slot-scope="scope">
          <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
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
  page
} from '@/api/wtpLog'
import Pagination from '@/components/Pagination'

export default {
  name: 'ArticleList',
  components: {
    Pagination
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
      listLoading: true,
      pageQuery: {
        appId: null,
        clusterId: null,
        name: null,
        ip: null,
        startTime: null,
        endTime: null,
        page: 1,
        size: 20
      },
      appIdOptions: [],
      clusterIdOptions: [],
      pickerOptions: {
        shortcuts: [{
          text: this.$t('wtpLog.latest_week'),
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: this.$t('wtpLog.last_month'),
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: this.$t('wtpLog.last_three_months'),
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      queryTime: []
    }
  },
  computed: {
    ...mapGetters([
      'roles',
      'permissions'
    ])
  },
  watch: {
    '$route': 'getParams'
  },
  created() {
    if (this.$route.query.appId) {
      this.pageQuery.appId = this.$route.query.appId
    }
    if (this.$route.query.clusterId) {
      this.pageQuery.clusterId = this.$route.query.clusterId
    }
    if (this.$route.query.name) {
      this.pageQuery.name = this.$route.query.name
    }
    this.appOptions()
    this.page()
  },
  methods: {
    handleChangeDate() {
      if (this.queryTime && this.queryTime.length === 2) {
        this.pageQuery.startTime = this.queryTime[0].getTime()
        this.pageQuery.endTime = this.queryTime[1].getTime()
        console.log(this.pageQuery.startTime)
      } else {
        this.pageQuery.startTime = null
        this.pageQuery.endTime = null
      }
    },
    appOptions() {
      appOptions().then((response) => {
        this.appIdOptions = response.data.list
      })
    },
    appIdOptionsChange(value) {
      this.pageQuery.clusterId = null
      this.clusterOptions(value)
    },
    clusterOptions(appId) {
      clusterOptions(appId).then((response) => {
        this.clusterIdOptions = response.data.list
      })
    },
    clusterIdOptionsChange(value) {
      this.page()
    },
    page() {
      this.listLoading = true
      page(this.pageQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    numberPercentDiv(arg1, arg2, digit) {
      return ((arg1 / arg2) * 100).toFixed(digit)
    },
    numberDiv(arg1, arg2, digit) {
      return (arg1 / arg2).toFixed(digit)
    }
  }
}
</script>
<style>
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
