import {createRouter, createWebHashHistory, RouteRecordRaw} from 'vue-router';
import useStore from '@/store';

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
        path: '/login',
        component: () => import('@/views/login/index.vue'),
        meta: {title: '登录', hidden: true}
    },
    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('@/views/dashboard/index.vue'),
                meta: {title: '主页', icon: 'home', affix: true}
            }
        ]
    },
    {
        path: '/profile',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'ProfileIndex',
                component: () => import('@/views/profile/index.vue'),
                meta: {title: '个人简介', hidden: true, icon: 'user', noCache: true}
            }
        ]
    },
    {
        path: '/exam/paper/answer/edit',
        name: 'ExamPaperAnswerEdit',
        component: () => import('@/views/answer/edit.vue'),
        meta: {title: '试卷批改', hidden: true, noCache: true}
    },
    {
        path: '/exam/paper/answer/read',
        name: 'ExamPaperAnswerRead',
        component: () => import('@/views/answer/read.vue'),
        meta: {title: '试卷查看', hidden: true, noCache: true}
    },
    {
        path: '/exam/paper/answer/monitor/read',
        name: 'ExamPaperAnswerMonitorRead',
        component: () => import('@/views/answer/monitor/read.vue'),
        meta: {title: '试卷查看', hidden: true, noCache: true}
    },
    {
        path: '/train/exam/paper/show',
        name: 'TrainExamPaperShow',
        component: () => import('@/views/train/exam-paper/show.vue'),
        meta: {title: '试卷查看', hidden: true, noCache: true}
    },
    {
        path: '/train/paper/answer/edit',
        name: 'TrainExamPaperAnswerEdit',
        component: () => import('@/views/train/paper-answer/edit.vue'),
        meta: {title: '答卷批改', hidden: true, noCache: true}
    },
    {
        path: '/train/paper/answer/read',
        name: 'TrainExamPaperAnswerRead',
        component: () => import('@/views/train/paper-answer/read.vue'),
        meta: {title: '答卷查看', hidden: true, noCache: true}
    },
    {
        path: '/practice/answer/edit',
        name: 'TrainExamPaperAnswerEdit',
        component: () => import('@/views/practice/answer/edit.vue'),
        meta: {title: '模拟练习批改', hidden: true, noCache: true}
    },
    {
        path: '/practice/answer/read',
        name: 'PracticeAnswerRead',
        component: () => import('@/views/practice/answer/read.vue'),
        meta: {title: '模拟练习查看', hidden: true, noCache: true}
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
    {
        path: '/vmWare/edit',
        name: '/vmWare/edit',
        component: () => import('@/views/vmWare/edit.vue'),
        meta: {title: '编辑云桌面模板', hidden: true, noCache: true}
    },
];

// 创建路由
const router = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes as RouteRecordRaw[],
    // 刷新时，滚动条位置还原
    scrollBehavior: () => ({left: 0, top: 0})
});

// 重置路由
export function resetRouter() {
    const {permission} = useStore();
    permission.routes.forEach(route => {
        const name = route.name;
        if (name && router.hasRoute(name)) {
            router.removeRoute(name);
        }
    });
}

export default router;
