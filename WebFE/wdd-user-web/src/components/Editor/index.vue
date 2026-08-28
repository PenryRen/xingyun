<template>
    <div class="editor">
        <div :id="randomId">
        </div>
    </div>
</template>

<script setup lang="ts">
import {onMounted, watch, onBeforeUnmount, shallowRef} from 'vue';
import {v4} from 'uuid'

const emit = defineEmits(['update:modelValue', 'change']);
const editorRef = shallowRef();

const props = defineProps({
    modelValue: {
        type: String,
        default: ''
    },
    zIndex: {
        type: Number,
        default: 9999
    },
    eFocus: {
        type: Boolean,
        default: false
    }
});

const randomId = `editor_${v4()}`

function editorInit() {
    const E = (window as any).wangEditor
    const editor = new E('#' + randomId)
    editor.config.zIndex = props.zIndex
    editor.config.focus = props.eFocus

    // 上传图片配置
    editor.config.uploadFileName = 'imageFile'
    editor.config.uploadImgServer = `/api/upload/image`

    // 上传视频配置
    editor.config.uploadVideoServer = `/api/upload/video`
    editor.config.uploadVideoAccept = ['mp4']

    // 上传音频配置
    editor.config.uploadAudioServer = `/api/upload/audio`
    editor.config.uploadAudioAccept = ['mp3']

    // 上传附件配置
    editor.config.uploadAttachmentServer = `/api/upload/attachment`
    editor.config.uploadAttachmentAccept = ['doc', 'docx', 'xlsx', 'xls', 'ppt', 'pptx', 'pdf']

    editor.config.menus = [
        'head', // 标题
        'bold', // 粗体
        'fontSize', // 字号
        'fontName', // 字体
        'italic', // 斜体
        'underline', // 下划线
        'strikeThrough', // 删除线
        'foreColor', // 文字颜色
        'backColor', // 背景颜色
        'list', // 列表
        'justify', // 对齐方式
        'image', // 插入图片
        'video', // 插入视频
        'audio', // 插入音频
        'attachment', // 插入附件
        'table', // 表格
        'code', // 插入代码
        'gapFilling', // 插入填空
        'undo', // 撤销
        'redo' // 重复
    ]

    editor.config.onchange = (html) => {
        emit('update:modelValue', html);
        emit('change', html);
    }

    editor.create()
    editorRef.value = editor
}

watch(() => props.modelValue, (newValue, oldValue) => {
    const editor = editorRef.value;
    if (editor == null) return;
    if (newValue !== editor.txt.html()) {
        editor.txt.html(newValue)
    }
})

onMounted(() => {
    editorInit();
});

onBeforeUnmount(() => {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
});

</script>
