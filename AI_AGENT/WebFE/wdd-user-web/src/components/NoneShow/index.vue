<template>
    <div :class="customerStyle">
        <img class="none-data-image" src="@/assets/none.png">
        <div class="none-data-label">暂无内容</div>
    </div>
</template>

<script setup lang="ts">

const props = defineProps({
    customerStyle: {
        type: String,
        default: 'no-data-layout-page'
    },
});
</script>
