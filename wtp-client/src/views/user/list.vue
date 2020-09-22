<template>
  <div class="app-container">
    <div class="table-query">
      <span class="mr10">
        <el-input
          v-model.trim="pageQuery.nickname"
          style="width: 150px;"
          class="filter-item"
          placeholder="nickname"
          clearable
        />
      </span>
      <span class="mr10">
        <el-input
          v-model.trim="pageQuery.username"
          style="width: 150px;"
          class="filter-item"
          placeholder="username"
          clearable
        />
      </span>
      <span class="mr10">
        <el-input
          v-model.trim="pageQuery.phone"
          style="width: 150px;"
          class="filter-item"
          placeholder="phone"
          clearable
        />
      </span>
      <span class="mr10">
        <el-input
          v-model.trim="pageQuery.email"
          style="width: 150px;"
          class="filter-item"
          placeholder="email"
          clearable
        />
      </span>
      <el-button class="filter-item mr10" type="primary" icon="el-icon-search" @click="page">
        搜索
      </el-button>
      <span class="mr10">
        <el-button type="primary" @click="newUserVisibleBtn">New User</el-button>
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
      <el-table-column align="center" label="UserId" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.userId }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="nickname">
        <template slot-scope="scope">
          <span>{{ scope.row.nickname }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="avatar">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" alt="" class="emptyGif">
        </template>
      </el-table-column>

      <el-table-column align="center" label="username">
        <template slot-scope="scope">
          <span>{{ scope.row.username }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="phone">
        <template slot-scope="scope">
          <span>{{ scope.row.phone }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="email">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="role">
        <template slot-scope="scope">
          <span>{{ scope.row.role }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="status">
        <template slot-scope="scope">
          <span>{{ scope.row.status }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.created | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions">
        <template slot-scope="scope">
          <el-button-group>
            <el-button type="primary" size="small" @click="updateUserVisibleBtn(scope.row)">编辑</el-button>
            <el-button
              v-if="scope.row.role !== 'SUPPER-ADMIN'"
              type="primary"
              size="small"
              @click="toPermission(scope.row)"
            >授权
            </el-button>
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

    <el-dialog title="创建用户" :visible.sync="newUserVisible">
      <el-form :model="createForm">
        <el-form-item label="nickname" label-width="120px">
          <el-input v-model="createForm.nickname" autocomplete="off" />
        </el-form-item>
        <el-form-item label="username" label-width="120px">
          <el-input v-model="createForm.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="password" label-width="120px">
          <el-input v-model="createForm.password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="phone" label-width="120px">
          <el-input v-model="createForm.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="email" label-width="120px">
          <el-input v-model="createForm.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="role" label-width="120px">
          <el-select v-model="createForm.role" placeholder="role">
            <el-option v-for="item in roleOptions" :key="item.role" :label="item.role" :value="item.role" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newUserVisible = false">取 消</el-button>
        <el-button type="primary" @click="create">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改用户" :visible.sync="updateUserVisible">
      <el-form :model="updateForm">
        <el-form-item label="nickname" label-width="120px">
          <el-input v-model="updateForm.nickname" autocomplete="off" />
        </el-form-item>
        <el-form-item v-if="updateForm.checkUpdate" label="password" label-width="120px">
          <el-input v-model="updateForm.password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="phone" label-width="120px">
          <el-input v-model="updateForm.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="email" label-width="120px">
          <el-input v-model="updateForm.email" autocomplete="off" />
        </el-form-item>
        <el-form-item v-if="updateForm.checkUpdate" label="role" label-width="120px">
          <el-select v-model="updateForm.role" placeholder="role">
            <el-option v-for="item in roleOptions" :key="item.role" :label="item.role" :value="item.role" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateUserVisible = false">取 消</el-button>
        <el-button type="primary" @click="update">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  mapGetters
} from 'vuex'
import {
  checkSuperAdmin,
  checkAdmin
} from '@/utils/token-utils'
import {
  page,
  create,
  update
} from '@/api/user'
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
      newUserVisible: false,
      pageQuery: {
        username: null,
        nickname: null,
        phone: null,
        email: null,
        role: null,
        page: 1,
        size: 20
      },
      createForm: {},
      roleOptions: [{
        role: 'ADMIN'
      },
      {
        role: 'USER'
      }
      ],
      updateUserVisible: false,
      updateForm: {}
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
  },
  methods: {
    checkUpdate() {
      if (this.updateForm.role === 'ADMIN') {
        this.updateForm.checkUpdate = checkSuperAdmin(this.roles)
      } else if (this.updateForm.role === 'USER') {
        this.updateForm.checkUpdate = checkSuperAdmin(this.roles) || checkAdmin(this.roles)
      } else {
        this.updateForm.checkUpdate = false
      }
    },
    page() {
      this.listLoading = true
      page(this.pageQuery).then((response) => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    create() {
      if (!this.createForm.username || !this.createForm.password) {
        this.$message.warning('参数不能为空')
        return
      }
      create(this.createForm).then((response) => {
        this.create = response.data
        if (create) {
          this.$message.success('添加成功')
          this.newUserVisible = false
          this.page()
        } else {
          this.$message.error('添加失败')
        }
      })
    },
    update() {
      if (!this.updateForm.userId) {
        this.$message.warning('修改失败')
        return
      }
      update(this.updateForm).then((response) => {
        this.update = response.data
        if (update) {
          this.$message.success('修改成功')
          this.updateUserVisible = false
          this.page()
        } else {
          this.$message.error('修改失败')
        }
      })
    },
    newUserVisibleBtn() {
      this.newUserVisible = true
      console.log('roleOptions' + this.roleOptions)
    },
    updateUserVisibleBtn(user) {
      user.status = null
      this.updateForm = user
      this.checkUpdate()
      this.updateUserVisible = true
    },
    toPermission(user) {
      this.$router.push({
        path: './permission',
        query: user
      })
    }
  }
}
</script>

<style scoped>
.emptyGif {
  display: block;
  width: 45%;
  margin: 0 auto;
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
