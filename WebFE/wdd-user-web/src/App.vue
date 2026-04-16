<template>
    <el-config-provider :locale="locale" size="default">
        <router-view/>
        <AiAssistant :enabled="!!user.token" :session-id="aiSessionId" />
    </el-config-provider>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import { ElConfigProvider } from 'element-plus';
// 导入 Element Plus 语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn';
import useStore from '@/store'
import { getCurrentUser } from '@/api/user'
import AiAssistant from '@/components/AiAssistant/index.vue';
// 语言
const locale = ref(zhCn);
// 用户信息
const { user } = useStore()
// 用户详细信息
const userInfo = ref(null);

const aiSessionId = computed(() => {
    return userInfo.value?.workNo || userInfo.value?.userName || 'default';
});

/**
 * 初始化 AI 会话身份
 */
 const init = async (token) => {
    if (token) {
        if (!(userInfo.value && userInfo.value.workNo)) {
            const res = await getCurrentUser();
            userInfo.value = res.response;
        }
    } else {
        userInfo.value = null;
    }
}

// 监听用户登录状态
watch(
    () => user.token,
    (newVal) => {
        init(newVal);
    }, 
    { immediate: true }
);
</script>
