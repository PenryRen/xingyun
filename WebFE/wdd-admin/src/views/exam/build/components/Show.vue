<template>
  <div class="wdd-flex-column">
    <div v-if="qType === 1" v-loading="qLoading">
      <div class="wdd-question-content wdd-content-line-height" v-html="question.title"/>
      <div class="wdd-flex-column wdd-question-item-marin">
        <div :key="item.id" v-for="item in question.items" class="wdd-flex-row wdd-question-choice-marin">
          <span class="wdd-question-choice-prefix-marin">{{ item.prefix }}</span>
          <span v-html="item.content" class="wdd-question-content"></span>
        </div>
      </div>
    </div>
    <div v-else-if="qType === 2 || qType === 6" v-loading="qLoading">
      <div class="wdd-question-content wdd-content-line-height" v-html="question.title"/>
      <div class="wdd-flex-column wdd-question-item-marin">
          <span :key="item.id" v-for="item in question.items" class="wdd-flex-row wdd-question-choice-marin">
            <span class="wdd-question-choice-prefix-marin">{{ item.prefix }}</span>
            <span v-html="item.content" class="wdd-question-content"></span>
          </span>
      </div>
    </div>
    <div v-else-if="qType === 3" v-loading="qLoading" class="wdd-content-line-height">
      <span class="wdd-question-content" v-html="question.title"/>
      <span>（</span>
      <span :key="item.id" v-for="item in question.items" class="wdd-question-choice-prefix-marin">
        <span v-html="item.content" class="wdd-question-content"></span>
       </span>
      <span>）</span>
    </div>
    <div v-else-if="qType === 4" v-loading="qLoading">
      <div class="wdd-question-content wdd-content-line-height" v-html="question.title"/>
    </div>
    <div v-else-if="qType === 5|| qType === 7" v-loading="qLoading">
      <div class="wdd-question-content wdd-content-line-height" v-html="question.title"/>
    </div>
    <div v-else>
    </div>

    <div class="wdd-line-height"></div>

    <div class="wdd-flex-row wdd-question-property-marin">
      <span class="wdd-question-flex-property-left">分数：</span>
      <span class="wdd-question-flex-property-right">{{ question.score }}</span>
    </div>
    <div class="wdd-flex-row wdd-question-property-marin wdd-flex-align">
      <span class="wdd-question-flex-property-left">难度：</span>
      <el-rate v-model="question.difficult" show-text :max="3" :texts="['简单','较难','困难']" disabled
               class="wdd-question-flex-property-right"></el-rate>
    </div>
    <div v-if="qType === 7" class="wdd-flex-row wdd-question-property-marin" style="align-items: center">
      <span class="wdd-question-flex-property-left" style="width: auto;">规则类型：</span>
      <el-radio-group v-model="question.commandType" class="wdd-question-flex-property-right" disabled>
        <el-radio v-for="item in radioItems" :key="item.value" :label="item.value">
          {{ item.label}}
        </el-radio>
      </el-radio-group>
    </div>
    <div v-if="qType === 7" class="wdd-flex-row wdd-question-property-marin">
      <span class="wdd-question-flex-property-left" style="width: auto">执行命令：</span>
      <span v-html="question.command"
            class="wdd-question-content wdd-question-flex-property-right"/>
    </div>

    <div v-if="qType === 7" class="wdd-flex-row wdd-question-property-marin wdd-flex-align">
      <span class="wdd-question-flex-property-left" style="width: auto">正确结果：</span>
      <span v-html="question.correct"
            class="wdd-question-flex-property-right wdd-question-content"/>
    </div>
    <div v-else class="wdd-flex-row wdd-question-property-marin">
      <span v-if="qType === 7" class="wdd-question-flex-property-left" style="width: auto">正确结果：</span>
      <span v-else class="wdd-question-flex-property-left">标答：</span>

      <span v-if="qType === 1" v-html="question.correct" class="wdd-question-flex-property-right"/>
      <span v-if="qType === 2 || qType === 6" v-html="question.correctArray"
            class="wdd-question-flex-property-right"/>
      <span v-if="qType === 3" v-html="question.correct" class="wdd-question-flex-property-right"/>
      <span v-if="qType === 4" class="wdd-question-flex-property-right">
           <div v-for="item in question.items" :key="item.prefix" v-html="`${item.prefix}. ${item.content}`"
                class="wdd-question-gap-answer"/>
      </span>
      <span v-if="qType === 5" v-html="question.correct"
            class="wdd-question-content wdd-question-flex-property-right"/>
    </div>

    <div v-if="qType === 7">
    </div>
    <div v-else class="wdd-flex-row wdd-question-property-marin">
      <span class="wdd-question-flex-property-left">解析：</span>
      <span v-html="question.analyze" class="wdd-question-content wdd-question-flex-property-right"/>
    </div>
  </div>

</template>

<script setup lang="ts">
import {computed} from 'vue';

const props = defineProps({
  question: {
    type: Object,
    default: function () {
      return {}
    }
  },
  qLoading: {
    type: Boolean,
    default: false
  },
  qType: {
    type: Number,
    default: 0
  }
});

const question = computed<object | undefined>({
  get() {
    return props.question;
  },
  set(val) {
  }
});


const qLoading = computed<boolean | undefined>({
  get() {
    return props.qLoading;
  },
  set(val) {
  }
});


const qType = computed<number | undefined>({
  get() {
    return props.qType;
  },
  set(val) {
  }
});

// 定义数组字典
const radioItems = [
  {label: '执行命令', value: '1'},
  {label: '匹配文本内容', value: '2'}
];

</script>
