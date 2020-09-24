import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */

export const constantRoutes = [{
  path: '/redirect',
  component: Layout,
  hidden: true,
  children: [{
    path: '/redirect/:path(.*)',
    component: () => import('@/views/redirect/index')
  }]
},
{
  path: '/login',
  component: () => import('@/views/login/index'),
  hidden: true
},
{
  path: '/auth-redirect',
  component: () => import('@/views/login/auth-redirect'),
  hidden: true
},
{
  path: '/404',
  component: () => import('@/views/error-page/404'),
  hidden: true
},
{
  path: '/401',
  component: () => import('@/views/error-page/401'),
  hidden: true
},
{
  path: '/',
  component: Layout,
  redirect: '/dashboard',
  children: [{
    path: 'dashboard',
    component: () => import('@/views/dashboard/index'),
    name: 'Dashboard',
    meta: {
      title: 'dashboard',
      icon: 'dashboard',
      affix: true
    }
  }]
},
{
  path: '/documentation',
  component: Layout,
  children: [{
    path: 'index',
    component: () => import('@/views/documentation/index'),
    name: 'Documentation',
    meta: {
      title: 'documentation',
      icon: 'documentation',
      affix: true
    }
  }]
}
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [{
  path: '/user',
  component: Layout,
  meta: {
    title: 'user',
    icon: 'user',
    roles: ['SUPPER-ADMIN', 'ADMIN'] // you can set roles in root nav
  },
  children: [{
    path: 'list',
    component: () => import('@/views/user/list'),
    name: '用户列表',
    meta: {
      title: 'user_list',
      roles: ['SUPPER-ADMIN', 'ADMIN'] // or you can only set roles in sub nav
    }
  },
  {
    path: 'permission',
    component: () => import('@/views/user/permission'),
    name: '用户权限',
    hidden: true,
    meta: {
      title: 'user_permission',
      roles: ['SUPPER-ADMIN', 'ADMIN'] // or you can only set roles in sub nav
    }
  }
  ]
},
{
  path: '/app',
  component: Layout,
  meta: {
    title: 'app',
    icon: 'component',
    roles: ['SUPPER-ADMIN', 'ADMIN'] // you can set roles in root nav
  },
  children: [{
    path: 'list',
    component: () => import('@/views/app/list'),
    name: '项目列表',
    meta: {
      title: 'app_list',
      roles: ['SUPPER-ADMIN', 'ADMIN'] // or you can only set roles in sub nav
    }
  }]
},
{
  path: '/cluster',
  component: Layout,
  meta: {
    title: 'cluster',
    icon: 'el-icon-s-platform',
    roles: ['SUPPER-ADMIN', 'ADMIN', 'USER'] // you can set roles in root nav
  },
  children: [{
    path: 'list',
    component: () => import('@/views/cluster/list'),
    name: '集群列表',
    meta: {
      title: 'cluster_list',
      roles: ['SUPPER-ADMIN', 'ADMIN', 'USER'] // or you can only set roles in sub nav
    }
  }]
},
{
  path: '/wtp',
  component: Layout,
  meta: {
    title: 'wtp',
    icon: 'tree',
    roles: ['SUPPER-ADMIN', 'ADMIN', 'USER'] // you can set roles in root nav
  },
  children: [{
    path: 'list',
    component: () => import('@/views/wtp/list'),
    name: '线程池列表',
    meta: {
      title: 'wtp_list',
      roles: ['SUPPER-ADMIN', 'ADMIN', 'USER'] // or you can only set roles in sub nav
    }
  }]
},
{
  path: '/log',
  component: Layout,
  meta: {
    title: 'log',
    icon: 'el-icon-date',
    roles: ['SUPPER-ADMIN', 'ADMIN', 'USER'] // you can set roles in root nav
  },
  children: [{
    path: 'wtpLog',
    component: () => import('@/views/log/wtpLog'),
    name: '线程池日志管理',
    meta: {
      title: 'wtpLog_list',
      roles: ['SUPPER-ADMIN', 'ADMIN', 'USER'] // or you can only set roles in sub nav
    }
  }]
},
// 404 page must be placed at the end !!!
{
  path: '*',
  redirect: '/404',
  hidden: true
}

]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
