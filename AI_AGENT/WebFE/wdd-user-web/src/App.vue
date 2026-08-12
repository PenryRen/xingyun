<template>
    <el-config-provider :locale="locale" size="default">
        <router-view/>
    </el-config-provider>
</template>

<script setup>
import { ref } from 'vue';
import { ElConfigProvider } from 'element-plus';
import zhCn from 'element-plus/es/locale/lang/zh-cn';
const locale = ref(zhCn);
</script>
