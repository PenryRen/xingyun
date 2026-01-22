<template>
    <div :class="{ hidden: hidden }" class="pagination-container">
        <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :layout="layout"
                :page-sizes="pageSizes"
                :total="total"
                prev-text="上一页"
                next-text="下一页"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
        />
    </div>
</template>

<script setup lang="ts">
import {computed, PropType} from 'vue';
import {scrollTo} from '@/utils/scroll-to';

const props = defineProps({
    total: {
        required: true,
        type: Number as PropType<number>,
        default: 0
    },
    page: {
        type: Number,
        default: 1
    },
    limit: {
        type: Number,
        default: 10
    },
    pageSizes: {
        type: Array as PropType<number[]>,
        default() {
            return [10, 20, 30, 50];
        }
    },
    layout: {
        type: String,
        default: 'prev, pager, next'
    },
    autoScroll: {
        type: Boolean,
        default: true
    },
    hidden: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits(['update:page', 'update:limit', 'pagination']);

const currentPage = computed<number | undefined>({
    get: () => props.page,
    set: value => {
        emit('update:page', value);
    }
});

const pageSize = computed<number | undefined>({
    get() {
        return props.limit;
    },
    set(val) {
        emit('update:limit', val);
    }
});

function handleSizeChange(val: number) {
    emit('pagination', {page: currentPage, limit: val});
    if (props.autoScroll) {
        scrollTo(0, 800);
    }
}

function handleCurrentChange(val: number) {
    currentPage.value = val;
    emit('pagination', {page: val, limit: props.limit});
    if (props.autoScroll) {
        scrollTo(0, 800);
    }
}
</script>
