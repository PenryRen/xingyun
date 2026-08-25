import router from '@/router';
import useStore from '@/store';

// 白名单路由
const whiteList = ['/login', '/register', "/403"];

router.beforeEach(async (to, from, next) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    if (window._hmt && to.path) {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        window._hmt.push(['_trackPageview', `/#${to.path}`])
    }

    const {user} = useStore();
    // 授权过期检查已绕过
    const hasToken = user.token;
    if (hasToken) {
        // 登录成功，跳转到首页
        if (to.path === '/login') {
            next({path: '/'});
        } else {
            next();
        }
    } else {
        // 未登录可以访问白名单页面(登录页面)
        if (whiteList.indexOf(to.path) !== -1) {
            next();
        } else {
            next(`/login?redirect=${to.path}`);
        }
    }
});
