import {PermissionState} from '@/types/store/permission';
import {RouteRecordRaw} from 'vue-router';
import {defineStore} from 'pinia';
import {constantRoutes} from '@/router';
import {menu} from '@/api/role';

const modules = import.meta.glob('../../views/**/**/**.vue');
export const Layout = () => import('@/layout/index.vue');


export const generateAsyncRoutes = (
    routes: any
) => {
    const res: RouteRecordRaw[] = [];
    routes.forEach((root: any) => {
        const route: RouteRecordRaw = {
            path: root.path,
            name: root.name,
            component: Layout,
            redirect: 'noredirect',
            meta: {
                title: root.metaTitle,
                icon: root.metaIcon,
                alwaysShow: true
            },
            children: root.child.map((child: any) => {
                return {
                    path: child.path,
                    name: child.name,
                    component: modules[`../../views${child.component}.vue`],
                    meta: {
                        title: child.metaTitle,
                        keepAlive: !child.metaNoCache,
                        activeMenu: child.metaActiveMenu,
                        hidden: child.hidden
                    }
                }
            })
        };
        res.push(route);
    });
    return res;
};

const usePermissionStore = defineStore({
    id: 'permission',
    state: (): PermissionState => ({
        routes: [],
        addRoutes: [],
        perms: [],
    }),
    actions: {
        async getUserRoutePermission() {
            const res = await menu()
            if (res.code === 1) {
                const response = res.response
                this.perms = response.permissionList;
                const accessedRoutes = generateAsyncRoutes(response.routerItemVMList);
                this.setRoutes(accessedRoutes);
                return {code: 1, data: accessedRoutes}
            } else {
                return {code: res.code, data: '权限获取失败'}
            }
        },
        setRoutes(routes: RouteRecordRaw[]) {
            this.addRoutes = routes;
            this.routes = constantRoutes.concat(routes);
        },
        clearRoutes() {
            this.routes = []
            this.perms = []
        },
    },
});

export default usePermissionStore;
