/// <reference types="vite/client" />

declare module '*.vue' {
    import {DefineComponent} from 'vue';
    // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
    const component: DefineComponent<{}, {}, any>;
    export default component;
}

// 环境变量 TypeScript的智能提示
interface ImportMetaEnv {
    VITE_APP_TOKEN_KEY: string;
    VITE_APP_SYSTEM_NAME: string;
    VITE_APP_COPYRIGHT: string;
    VITE_APP_ICP: string;
    VITE_APP_PAIR_ONE_PUBLIC_KEY: string;
    VITE_APP_RESOURCE_URL: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}
