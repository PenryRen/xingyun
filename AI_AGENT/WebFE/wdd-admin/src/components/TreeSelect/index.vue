<template>
  <el-tree-select
      v-model="modelValue" :data="data"
      :render-after-expand="true"
      show-checkbox multiple
      default-expand-all
      :placeholder="placeholder"
      :style="style" ref="treeSelect" :clearable="clearable"
      @clear="clear" class="component-tree-select"/>
</template>

<script setup lang="ts">
import {computed, ref} from 'vue';

const props = defineProps({
  modelValue: {
    type: Object,
    default: function () {
      return {}
    }
  },
  data: {
    type: Object,
    default: function () {
      return {}
    }
  },
  placeholder: {
    type: String,
    default: ''
  },
  style: {
    type: Object,
    default: function () {
      return {}
    }
  },
  clearable: {
    type: Boolean,
    default: true
  }
});

const treeSelect = ref(null);

const emit = defineEmits(['update:modelValue']);

const modelValue = computed<object | undefined>({
  get: () => props.modelValue,
  set: value => {
    let tree = (treeSelect as any).value
    if (null != tree) {
      let keys = tree.getCheckedKeys()
      emit('update:modelValue', keys);
    }
  }
});

const data = computed<object | undefined>({
  get: () => props.data,
  set: value => {
  }
});

const clear = function () {
  emit('update:modelValue', []);
}
</script>

