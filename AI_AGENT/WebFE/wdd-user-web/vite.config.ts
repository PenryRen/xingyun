import {UserConfig, ConfigEnv, loadEnv} from 'vite';
import vue from '@vitejs/plugin-vue';
import {createSvgIconsPlugin} from 'vite-plugin-svg-icons';
import path from 'path';
import {createHtmlPlugin} from "vite-plugin-html";

// @see: https://gitee.com/holysheng/vite2-config-description/blob/master/vite.config.ts
export default ({mode}: ConfigEnv): UserConfig => {
  // 获取 .env 环境配置文件
  const env = loadEnv(mode, process.cwd());

  return {
    build: {
      outDir: 'ueit-user-web',
      // 通过手动拆分 chunk 优化首屏体积与缓存
      rollupOptions: {
        output: {
          manualChunks: {
            // Vue 核心相关依赖
            vue: ['vue', 'vue-router', 'pinia', '@vueuse/core'],
            // UI 组件库单独拆包
            'element-plus': ['element-plus', '@element-plus/icons-vue']
          }
        }
      },
      // 适当调高警告阈值，避免频繁告警（仅影响提示，不影响实际构建）
      chunkSizeWarningLimit: 1500
    },
    server: {
      host: '0.0.0.0',
      port: 16001,
      open: true, // 运行自动打开浏览器
      proxy: {
        '/api': {
          target: 'http://192.168.1.210:16000',
          changeOrigin: true
        }
      }
    },
    plugins: [
      vue(),
      createHtmlPlugin(
        {
          inject: {
            data: {
              VITE_APP_SYSTEM_NAME: env.VITE_APP_SYSTEM_NAME
            }
          }
        }
      ),
      createSvgIconsPlugin({
        // 指定需要缓存的图标文件夹
        iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
        // 指定symbolId格式
        symbolId: 'icon-[dir]-[name]'
      })
    ],
    resolve: {
      // Vite路径别名配置
      alias: {
        '@': path.resolve('./src')
      }
    }
  };
};
