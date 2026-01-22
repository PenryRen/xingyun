<template>
    <el-config-provider :locale="locale" size="default">
        <router-view/>
    </el-config-provider>
</template>

<script setup>
import { ref, watch } from 'vue';
import { ElConfigProvider } from 'element-plus';
// 导入 Element Plus 语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn';
import { loadScript } from "vue-plugin-load-script";
import useStore from '@/store'
import { getCurrentUser } from '@/api/user'
// Core AI 实例对象
const coze = ref(null);
// 语言
const locale = ref(zhCn);
// 用户信息
const { user } = useStore()
// 用户详细信息
const userInfo = ref(null);

/**
 * 初始化 WebChatClient
 */
const initWebChatClient = () => {
    return new window.CozeWebSDK.WebChatClient({
        config: {
            bot_id: '7480843218692292645',
        },
        auth: {
            type: 'token',
            token: 'pat_yWpScGFi0VkKlLFgb1NtLJpbNiTrdHCjyLbV26lhtuE5ajKYRhM7RaLF9UeueT84',
            onRefreshToken: function () {
                return 'pat_yWpScGFi0VkKlLFgb1NtLJpbNiTrdHCjyLbV26lhtuE5ajKYRhM7RaLF9UeueT84'
            }
        },
        userInfo: {
            id: userInfo.value?.userName,
            url: userInfo.value?.imagePath,
            nickname: userInfo.value?.realName,
        },
        ui: {
            chatBot: {
                title: "智能客服",
                width: "560px"
            },
            footer: {
                isShow: false
            }
        }
    })
}

/**
 * 获取用户信息
 */
 const init = async (token) => {
    if (token) {
        if (!(userInfo.value && userInfo.value.workNo)) {
            const res = await getCurrentUser();
            userInfo.value = res.response;
        }

        // 初始化 Core AI
        if (window.CozeWebSDK && window.CozeWebSDK.WebChatClient) {
            coze.value = initWebChatClient();
        } else {
            loadScript("https://lf-cdn.coze.cn/obj/unpkg/flow-platform/chat-app-sdk/1.2.0-beta.5/libs/cn/index.js")
                .then(() => (coze.value = initWebChatClient()));
        }
    } else {
        // 销毁 Core AI
        coze.value && coze.value.destroy();
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
