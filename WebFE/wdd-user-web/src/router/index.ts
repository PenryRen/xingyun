import {createRouter, createWebHashHistory, RouteRecordRaw} from 'vue-router';

export const Layout = () => import('@/layout/index.vue');

export const constantRoutes: Array<RouteRecordRaw> = [
    {
        path: '/redirect',
        component: Layout,
        meta: {hidden: true},
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/redirect/index.vue')
            }
        ]
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/register/index.vue'),
        meta: {title: '注册'}
    },
    {
        path: '/login',
        component: () => import('@/views/login/index.vue'),
        meta: {title: '登录', hidden: true}
    },
    {
        path: '/',
        component: Layout,
        redirect: '/index',
        children: [
            {
                path: 'index',
                name: 'DashboardIndex',
                component: () => import('@/views/dashboard/index.vue'),
                meta: {title: '首页'}
            }
        ]
    },
    {
        path: '/announcement',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'AnnouncementIndex',
                component: () => import('@/views/announcement/index.vue'),
                meta: {title: '通知公告'}
            },
            {
                path: 'detail',
                name: 'AnnouncementDetail',
                component: () => import('@/views/announcement/detail.vue'),
                meta: {title: '公告详情', activeMenu: '/announcement/index', hidden: true}
            }
        ]
    },
    {
        path: '/forum',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'ForumIndex',
                component: () => import('@/views/forum/index.vue'),
                meta: {title: '交流圈'}
            },
            {
                path: 'add',
                name: 'ForumAdd',
                component: () => import('@/views/forum/add.vue'),
                meta: {title: '发布文章'}
            },
            {
                path: 'select',
                name: 'ForumSelect',
                component: () => import('@/views/forum/select.vue'),
                meta: {title: '文章查看'}
            }
        ]
    },
    {
        path: '/apply',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'ApplyIndex',
                component: () => import('@/views/apply/index.vue'),
                meta: {title: '报名中心'}
            }
        ]
    },
    {
        path: '/paper',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'PaperIndex',
                component: () => import('@/views/paper/index.vue'),
                meta: {title: '试卷中心'}
            }
        ]
    },
    {
        path: '/train',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'TrainIndex',
                component: () => import('@/views/train/index.vue'),
                meta: {title: '培训中心'}
            },
            {
                path: 'detail',
                name: 'TrainDetail',
                component: () => import('@/views/train/detail.vue'),
                meta: {title: '培训详情'}
            }
        ]
    },
    {
        path: '/record',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'RecordIndex',
                component: () => import('@/views/record/index.vue'),
                meta: {title: '考试记录'}
            }
        ]
    },
    {
        path: '/course/ware',
        component: Layout,
        children: [
            {
                path: 'video',
                name: 'CourseWareVideo',
                component: () => import('@/views/course-ware/video.vue'),
                meta: {title: '课件视频观看'}
            }
        ]
    },
    {
        path: '/user',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'UserIndex',
                component: () => import('@/views/user-info/index.vue'),
                meta: {title: '个人信息'}
            },
            {
                path: 'update',
                name: 'UserUpdate',
                component: () => import('@/views/user-info/update.vue'),
                meta: {title: '更新信息'}
            },
            {
                path: 'passwordChange',
                name: 'UserChangePassword',
                component: () => import('@/views/user-info/change-password.vue'),
                meta: {title: '修改密码'}
            },
            {
                path: 'apply',
                name: 'UserApply',
                component: () => import('@/views/user-info/apply.vue'),
                meta: {title: '我的报名'}
            },
            {
                path: 'comment',
                name: 'UserComment',
                component: () => import('@/views/user-info/comment.vue'),
                meta: {title: '我的评论'}
            },
            {
                path: 'train',
                name: 'MyTrain',
                component: () => import('@/views/user-info/train.vue'),
                meta: {title: '我的培训'}
            },
            {
                path: 'credential',
                name: 'MyCredential',
                component: () => import('@/views/user-info/credential.vue'),
                meta: {title: '我的证书'}
            },
            {
                path: 'event',
                name: 'UserEvent',
                component: () => import('@/views/user-info/event.vue'),
                meta: {title: '个人动态'}
            }
        ]
    },
    {
        path: '/do',
        name: 'Do',
        component: () => import('@/views/exam/paper/do.vue'),
        meta: {title: '试卷答题'}
    },
    {
        path: '/face',
        name: 'Face',
        component: () => import('@/views/exam/paper/face.vue'),
        meta: {title: '人脸识别'}
    },
    {
        path: '/read',
        name: 'Read',
        component: () => import('@/views/exam/paper/read.vue'),
        meta: {title: '试卷查看'}
    },
    {
        path: '/course/ware/pdf',
        name: 'CourseWarePdf',
        component: () => import('@/views/course-ware/pdf.vue'),
        meta: {title: '课件文档观看'}
    },
    {
        path: '/course/ware/pdfAndWare',
        name: 'pdfAndWare',
        component: () => import('@/views/course-ware/pdfAndWare.vue'),
        meta: {title: '课件文档观看'}
    },
    {
      path: '/course/ware/videoAndWare',
      name: 'videoAndWare',
      component: () => import('@/views/course-ware/videoAndWare.vue'),
      meta: {title: '课件视频观看'}
    },
    {
        path: '/train/paper/do',
        name: 'TrainPaperDo',
        component: () => import('@/views/train/paper.vue'),
        meta: {title: '试卷答题'}
    },
    {
        path: '/practice/paper/do',
        name: 'PracticePaperDo',
        component: () => import('@/views/practice/do.vue'),
        meta: {title: '模拟练习答题'}
    },
    {
        path: '/practice/paper/edit',
        name: 'PracticePaperEdit',
        component: () => import('@/views/practice/edit.vue'),
        meta: {title: '模拟练习批改'}
    },
    {
        path: '/practice/paper/read',
        name: 'PracticePaperRead',
        component: () => import('@/views/practice/read.vue'),
        meta: {title: '模拟练习查看'}
    },
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/error-page/404.vue'),
        meta: {title: '页面未找到', hidden: true}
    },
    {
        path: '/403',
        component: () => import('@/views/error-page/403.vue'),
        meta: {title: '系统授权过期', hidden: true}
    },
];

// 创建路由
const router = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes as RouteRecordRaw[],
    // 刷新时，滚动条位置还原
    scrollBehavior: () => ({left: 0, top: 0})
});


export default router;
