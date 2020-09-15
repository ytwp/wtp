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
        <el-button type="primary" v-if="check(pageQuery.appId)" @click="newClusterVisible = true">New Cluster</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%;margin-top: 20px;">
      <el-table-column align="center" label="ID" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
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

      <el-table-column align="center" label="ClusterName">
        <template slot-scope="scope">
          <span>{{ scope.row.clusterName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="OnLine 机器">
        <template slot-scope="scope">
          <el-tag type="success" @click="registryListBtn(scope.row.wtpRegistryList)">{{ '查看( ' + scope.row.wtpRegistryList.length + ' )' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="el-icon-edit" @click="updateClusterVisibleBtn(scope.row.id)">
            编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="pageQuery.page" :limit.sync="pageQuery.size" @pagination="pageCluster" />

    <el-dialog title="创建 Cluster" :visible.sync="newClusterVisible">
      <el-form :model="createForm">
        <el-form-item label="AppId" label-width="120px">
          <el-select v-model="createForm.appId" filterable placeholder="可搜索">
            <el-option v-for="item in appIdOptions" :key="item.appId" :label="item.appId" :value="item.appId" />
          </el-select>
        </el-form-item>
        <el-form-item label="ClusterId" label-width="120px">
          <el-input v-model="createForm.clusterId" autocomplete="off" />
        </el-form-item>
        <el-form-item label="ClusterName" label-width="120px">
          <el-input v-model="createForm.clusterName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newClusterVisible = false">取 消</el-button>
        <el-button type="primary" @click="createCluster">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改 Cluster" :visible.sync="updateClusterVisible">
      <el-form :model="cluster">
        <el-form-item label="AppId" label-width="120px">
          <el-input v-model="cluster.appId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item label="ClusterId" label-width="120px">
          <el-input v-model="cluster.clusterId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item label="ClusterName" label-width="120px">
          <el-input v-model="cluster.clusterName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateClusterVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateCluster">确 定</el-button>
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
  import {
    mapGetters
  } from 'vuex'
  import {
    checkSuperAdmin,
    checkAdmin,
    checkAppAdmin,
    checkAppPermission
  } from '@/utils/token-utils'
  import {
    appOptions
  } from '@/api/app'
  import {
    page,
    create,
    update,
    get
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
        updateClusterVisible: false
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
          this.$message.warning('参数不能为空')
          return
        }
        create(this.createForm).then((response) => {
          this.create = response.data
          if (create) {
            this.$message.success('添加成功')
            this.newClusterVisible = false
            this.createForm.clusterId = null
            this.createForm.clusterName = null
            this.page()
          } else {
            this.$message.error('添加失败')
          }
        })
      },
      updateCluster() {
        if (!this.cluster.id || !this.cluster.clusterName) {
          this.$message.warning('参数不能为空')
          return
        }
        update(this.cluster).then((response) => {
          this.update = response.data
          if (update) {
            this.$message.success('修改成功')
            this.updateClusterVisible = false
            this.page()
          } else {
            this.$message.error('修改失败')
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
      updateClusterVisibleBtn(id) {
        get(id).then((response) => {
          this.cluster = response.data
          this.updateClusterVisible = true
        })
      }
    }
  }
</script>

<style scoped>
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
</style>
