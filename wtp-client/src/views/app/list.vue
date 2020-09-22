<template>
  <div class="app-container">
    <div class="table-query">
      <span class="mr10">
        <el-input
          v-model.trim="pageQuery.appId"
          style="width: 150px;"
          class="filter-item"
          :placeholder="$t('app.appId')"
          clearable
        />
      </span>
      <el-button class="filter-item mr10" type="primary" icon="el-icon-search" @click="page">
        {{ $t('app.search') }}
      </el-button>
      <span class="mr10">
        <el-button type="primary" icon="el-icon-plus" @click="newAppVisible = true">{{ $t('app.add') }}</el-button>
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
      <el-table-column align="center" :label="$t('app.id')" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('app.appId')">
        <template slot-scope="scope">
          <span>{{ scope.row.appId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('app.appName')">
        <template slot-scope="scope">
          <span>{{ scope.row.appName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('app.created')">
        <template slot-scope="scope">
          <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="updateAppVisibleBtn(scope.row.id)">
            {{ $t('app.edit') }}
          </el-button>
          <el-popconfirm
            :confirm-button-text="$t('app.del_confirm_button_text')"
            :cancel-button-text="$t('app.del_cancel_button_text')"
            icon="el-icon-info"
            icon-color="red"
            :title="$t('app.del_title')"
            @onConfirm="delApp(scope.row.id)"
          >
            <el-button v-if="delBtn" slot="reference" type="danger" size="small">{{ $t('app.del') }}</el-button>
          </el-popconfirm>
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

    <el-dialog :title="$t('app.add')" :visible.sync="newAppVisible">
      <el-form :model="createForm">
        <el-form-item :label="$t('app.appId')" label-width="120px">
          <el-input v-model="createForm.appId" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('app.appName')" label-width="120px">
          <el-input v-model="createForm.appName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newAppVisible = false">{{ $t('app.cancel') }}</el-button>
        <el-button type="primary" @click="createApp">{{ $t('app.confirm') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('app.update')" :visible.sync="updateAppVisible">
      <el-form :model="app">
        <el-form-item :label="$t('app.appId')" label-width="120px">
          <el-input v-model="app.appId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('app.appName')" label-width="120px">
          <el-input v-model="app.appName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateAppVisible = false">{{ $t('app.cancel') }}</el-button>
        <el-button type="primary" @click="updateApp">{{ $t('app.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  mapGetters
} from 'vuex'
import {
  page,
  create,
  get,
  update,
  del
} from '@/api/app'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import {
  checkSuperAdmin
} from '@/utils/token-utils'

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
      newAppVisible: false,
      pageQuery: {
        page: 1,
        size: 20
      },
      createForm: {},
      updateAppVisible: false,
      app: {},
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
    this.page()
    this.delBtn = checkSuperAdmin(this.roles)
  },
  methods: {
    updateAppVisibleBtn(id) {
      get(id).then((response) => {
        this.app = response.data
        this.updateAppVisible = true
      })
    },
    updateApp() {
      if (!this.app.appId || !this.app.id) {
        this.$message.warning(this.$t('app.parameter'))
        return
      }
      update(this.app).then(response => {
        this.update = response.data
        if (update) {
          this.$message.success(this.$t('app.success'))
          this.updateAppVisible = false
          this.page()
        } else {
          this.$message.error(this.$t('app.fail'))
        }
      })
    },
    page() {
      this.listLoading = true
      page(this.pageQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    delApp(id) {
      del({
        id: id
      }).then((response) => {
        if (response.data) {
          this.$message.success(this.$t('app.success'))
          this.page()
        } else {
          this.$message.error(this.$t('app.fail'))
        }
      })
    },
    createApp() {
      if (!this.createForm.appId || !this.createForm.appName) {
        this.$message.warning(this.$t('app.parameter'))
        return
      }
      create(this.createForm).then(response => {
        if (response.data) {
          this.$message.success(this.$t('app.success'))
          this.newAppVisible = false
          this.createForm = {}
          this.page()
        } else {
          this.$message.error(this.$t('app.fail'))
        }
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
