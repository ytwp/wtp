<template>
  <div class="app-container">
    <el-button type="primary" @click="newAppVisible = true">New App</el-button>

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
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="appId">
        <template slot-scope="scope">
          <span>{{ scope.row.appId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="appName">
        <template slot-scope="scope">
          <span>{{ scope.row.appName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="updateAppVisibleBtn(scope.row.id)">
            编辑
          </el-button>
          <el-popconfirm
            confirm-button-text="好的"
            cancel-button-text="不用了"
            icon="el-icon-info"
            icon-color="red"
            title="删除后不可恢复,确定删除吗？"
            @onConfirm="delApp(scope.row.id)"
          >
            <el-button v-if="delBtn" slot="reference" type="danger" size="small">删除</el-button>
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

    <el-dialog title="创建 App" :visible.sync="newAppVisible">
      <el-form :model="createForm">
        <el-form-item label="AppId" label-width="120px">
          <el-input v-model="createForm.appId" autocomplete="off" />
        </el-form-item>
        <el-form-item label="AppName" label-width="120px">
          <el-input v-model="createForm.appName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newAppVisible = false">取 消</el-button>
        <el-button type="primary" @click="createApp">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改 App" :visible.sync="updateAppVisible">
      <el-form :model="app">
        <el-form-item label="AppId" label-width="120px">
          <el-input v-model="app.appId" disabled autocomplete="off" />
        </el-form-item>
        <el-form-item label="AppName" label-width="120px">
          <el-input v-model="app.appName" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateAppVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateApp">确 定</el-button>
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
        this.$message.warning('参数不能为空')
        return
      }
      update(this.app).then(response => {
        this.update = response.data
        if (update) {
          this.$message.success('修改成功')
          this.updateAppVisible = false
          this.page()
        } else {
          this.$message.error('修改失败')
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
          this.$message.success('删除成功')
          this.page()
        } else {
          this.$message.error('删除失败')
        }
      })
    },
    createApp() {
      if (!this.createForm.appId || !this.createForm.appName) {
        this.$message.warning('参数不能为空')
        return
      }
      create(this.createForm).then(response => {
        if (response.data) {
          this.$message.success('添加成功')
          this.newAppVisible = false
          this.createForm = {}
          this.page()
        } else {
          this.$message.error('添加失败')
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
