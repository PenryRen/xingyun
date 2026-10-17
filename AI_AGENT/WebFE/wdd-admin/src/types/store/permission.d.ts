/**
 * 权限类型声明
 */
export interface PermissionState {
    perms: string[];
    routes: RouteRecordRaw[];
    addRoutes: RouteRecordRaw[];
}
