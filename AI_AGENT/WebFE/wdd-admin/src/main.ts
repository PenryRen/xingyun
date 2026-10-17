import {createApp, Directive} from 'vue';
import App from './App.vue';
import router from '@/router';

import {createPinia} from 'pinia';

import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import Pagination from '@/components/Pagination/index.vue';
import TreeSelect from '@/components/TreeSelect/index.vue';
import '@/permission';

// 引入svg注册脚本
import 'virtual:svg-icons-register';


// 自定义样式
import '@/styles/index.scss';


const app = createApp(App);

// 自定义指令
import * as directive from '@/directive';

Object.keys(directive).forEach((key) => {
    app.directive(key, (directive as { [key: string]: Directive })[key]);
});


// 注册全局组件
app
    .component('Pagination', Pagination)
    .component('TreeSelect', TreeSelect)
    .use(createPinia())
    .use(router)
    .use(ElementPlus)
    .mount('#app');
