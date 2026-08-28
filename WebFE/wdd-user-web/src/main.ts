import {createApp, Directive} from 'vue';
import App from './App.vue';
import router from '@/router';

import {createPinia} from 'pinia';

import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import NoneShow from '@/components/NoneShow/index.vue';
import Pagination from '@/components/Pagination/index.vue';
import '@/permission';
// 引入svg注册脚本
import 'virtual:svg-icons-register';
// 自定义样式
import '@/styles/index.scss';

const app = createApp(App);

// 注册全局组件
app
    .component('NoneShow', NoneShow)
    .component('Pagination', Pagination)
    .use(createPinia())
    .use(router)
    .use(ElementPlus)
    .mount('#app');