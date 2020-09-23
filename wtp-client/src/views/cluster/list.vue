<template>
  <div class="app-container">
    <div class="table-query">
      <span class="mr10">
        <el-select
          v-model="pageQuery.appId"
          filterable
          :placeholder="$t('cluster.searchable')"
          @change="appIdOptionsChange"
        >
          <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
        </el-select>
      </span>
      <span class="mr10">
        <el-input
          v-model.trim="pageQuery.clusterId"
          style="width: 150px;"
          class="filter-item"
          :placeholder="$t('cluster.clusterId')"
          clearable
          :disabled="!pageQuery.appId"
        />
      </span>
      <el-button
        class="filter-item mr10"
        type="primary"
        icon="el-icon-search"
        :disabled="!pageQuery.appId"
        @click="pageCluster"
      >
        {{ $t('cluster.search') }}
      </el-button>
      <span class="mr10">
        <el-button
          v-if="check(pageQuery.appId)"
          type="primary"
          :disabled="!pageQuery.appId"
          @click="newClusterVisible = true"
        >
          {{ $t('cluster.add') }}
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
      <el-table-column align="center" :label="$t('cluster.id')" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('cluster.appId')">
        <template slot-scope="scope">
          <span>{{ scope.row.appId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('cluster.clusterId')">
        <template slot-scope="scope">
          <span>{{ scope.row.clusterId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('cluster.clusterName')">
        <template slot-scope="scope">
          <span>{{ scope.row.clusterName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('cluster.onLine')">
        <template slot-scope="scope">
          <el-tag type="success" @click="registryListBtn(scope.row.wtpRegistryList)">
            {{ $t('cluster.look') + '( ' + scope.row.wtpRegistryList.length + ' )' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('cluster.created')">
        <template slot-scope="scope">
          <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="updateClusterVisibleBtn(scope.row.id)">
            {{ $t('cluster.edit') }}
          </el-button>
          <el-popconfirm
            :confirm-button-text="$t('cluster.del_confirm_button_text')"
            :cancel-button-text="$t('cluster.del_cancel_button_text')"
            icon="el-icon-info"
            icon-color="red"
            :title="$t('cluster.del_title')"
            @onConfirm="delCluster(scope.row.id)"
          >
            <el-button v-if="delBtn" slot="reference" type="danger" size="small">{{ $t('cluster.del') }}</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="pageQuery.page"
      :limit.sync="pageQuery.size"
      @pagination="pageCluster"
    />

    <el-dialog :title="$t('cluster.add')" :visible.sync="newClusterVisible">
      <el-form :model="createForm">
        <el-form-item :label="$t('cluster.appId')" label-width="120px">
          <el-select v-model="createForm.appId" filterable>
            <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('cluster.clusterId')" label-width="120px">
          <el-input v-model="createForm.clusterId" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('cluster.clusterName')" label-width="120px">
          <el-input v-model="createForm.clusterName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newClusterVisible = false">{{ $t('cluster.cancel') }}</el-button>
        <el-button type="primary" @click="createCluster">{{ $t('cluster.confirm') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('cluster.update')" :visible.sync="updateClusterVisible">
      <el-form :model="cluster">
        <el-form-item :label="$t('cluster.appId')" label-width="120px">
          <el-input v-model="cluster.appId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('cluster.clusterId')" label-width="120px">
          <el-input v-model="cluster.clusterId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('cluster.clusterName')" label-width="120px">
          <el-input v-model="cluster.clusterName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateClusterVisible = false">{{ $t('cluster.cancel') }}</el-button>
        <el-button type="primary" @click="updateCluster">{{ $t('cluster.confirm') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('cluster.onLine')" :visible.sync="registryVisible">
      <el-table :data="wtpRegistryList">
        <el-table-column align="center" :label="$t('cluster.ip')">
          <template slot-scope="scope">
            <span>{{ scope.row.ip }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('cluster.last_pull_time')">
          <template slot-scope="scope">
            <span>{{ scope.row.lastPullTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('cluster.registration_time')">
          <template slot-scope="scope">
            <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import {
  mapGetters
} from 'vuex'
import {
  checkSuperAdmin,
  checkAdmin,
  checkAppAdmin
} from '@/utils/token-utils'
import {
  appOptions
} from '@/api/app'
import {
  page,
  create,
  update,
  get,
  del
} from '@/api/cluster'
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
      listLoading: false,
      newClusterVisible: false,
      pageQuery: {
        appId: null,
        page: 1,
        size: 20
      },
      createForm: {
        appId: ''
      },
      appIdOptions: [],
      registryVisible: false,
      wtpRegistryList: null,
      cluster: {},
      updateClusterVisible: false,
      delBtn: false
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
    this.delBtn = checkSuperAdmin(this.roles) || checkAdmin(this.roles)
  },
  methods: {
    empty() {
      this.list = null
    },
    appOptions() {
      appOptions().then((response) => {
        this.appIdOptions = response.data.list
      })
    },
    appIdOptionsChange(value) {
      this.pageCluster()
      this.createForm.appId = value
    },
    pageCluster() {
      this.listLoading = true
      page(this.pageQuery).then((response) => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    formOptionsChange(value) {
      this.createForm.appId = value
    },
    createCluster() {
      if (!this.createForm.appId || !this.createForm.clusterId || !this.createForm.clusterName) {
        this.$message.warning(this.$t('cluster.parameter'))
        return
      }
      create(this.createForm).then((response) => {
        this.create = response.data
        if (create) {
          this.$message.success(this.$t('cluster.success'))
          this.newClusterVisible = false
          this.createForm.clusterId = null
          this.createForm.clusterName = null
          this.pageCluster()
        } else {
          this.$message.error(this.$t('cluster.fail'))
        }
      })
    },
    updateCluster() {
      if (!this.cluster.id || !this.cluster.clusterName) {
        this.$message.warning(this.$t('cluster.parameter'))
        return
      }
      update(this.cluster).then((response) => {
        this.update = response.data
        if (update) {
          this.$message.success(this.$t('cluster.success'))
          this.updateClusterVisible = false
          this.pageCluster()
        } else {
          this.$message.error(this.$t('cluster.fail'))
        }
      })
    },
    registryListBtn(wtpRegistryList) {
      this.wtpRegistryList = wtpRegistryList
      this.registryVisible = true
    },
    check(appId) {
      return (checkSuperAdmin(this.roles) || checkAdmin(this.roles) || checkAppAdmin(appId, this.permissions))
    },
    delCluster(id) {
      del({
        id: id
      }).then((response) => {
        if (response.data) {
          this.$message.success(this.$t('cluster.success'))
          this.pageCluster()
        } else {
          this.$message.error(this.$t('cluster.fail'))
        }
      })
    },
    updateClusterVisibleBtn(id) {
      get(id).then((response) => {
        this.cluster = response.data
        this.updateClusterVisible = true
      })
    }
  }
}
</script>
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
