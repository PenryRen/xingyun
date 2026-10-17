<template>
  <div v-loading="qLoading" class="wdd-flex-column">
    <div v-if="qType === 1">
      <div v-html="`${question.title} （ ${question.trickScore}分 )`"
           class="wdd-question-content wdd-question-title wdd-content-line-height"/>
      <div class="wdd-question-item-marin">
        <el-radio-group v-model="answer.contentKey" @change="answerChange(answer)" class="wdd-flex-column">
          <el-radio v-for="item in question.questionItemFrames" :key="item.key" :label="item.key"
                    class="wdd-flex-row wdd-question-choice-marin wdd-form-question-select">
            <span class="wdd-question-choice-prefix-marin">{{ item.prefix }}.</span>
            <span v-html="item.content" class="wdd-question-content"></span>
          </el-radio>
        </el-radio-group>
      </div>
    </div>
    <div v-else-if="qType === 2 || qType === 6">
      <div v-html="`${question.title} （ ${question.trickScore}分 )`"
           class="wdd-question-content wdd-question-title wdd-content-line-height"/>
      <div class="wdd-question-item-marin">
        <el-checkbox-group v-model="answer.contentArrayKey" @change="answerChange(answer)"
                           class="wdd-flex-column">
          <el-checkbox v-for="item in question.questionItemFrames" :label="item.key" :key="item.key"
                       class="wdd-flex-row wdd-question-choice-marin">
            <span class="wdd-question-choice-prefix-marin">{{ item.prefix }}.</span>
            <span v-html="item.content" class="wdd-question-content"></span>
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
    <div v-else-if="qType === 3" class="wdd-content-line-height">
            <span v-html="`${question.title} （ ${question.trickScore}分 )`"
                  class="wdd-question-content wdd-question-title"/>
      <span>( </span>
      <el-radio-group v-model="answer.contentKey" @change="answerChange(answer)">
        <el-radio v-for="item in question.questionItemFrames" :key="item.key" :label="item.key"
                  class="wdd-question-choice-prefix-marin">
          <span v-html="item.content" class="wdd-question-content"></span>
        </el-radio>
      </el-radio-group>
      <span> )</span>
    </div>
    <div v-else-if="qType === 4">
      <div v-html="`${question.title} （ ${question.trickScore}分 )`"
           class="wdd-question-content wdd-question-title wdd-content-line-height"/>
      <div v-if="answer.contentArray !== null" class="wdd-question-item-marin2">
        <div v-for="(item,index) in question.questionItemFrames" :key="item.prefix"
             class="wdd-flex-row wdd-gap-filling-items-content">
          <div class="wdd-question-flex-property-left">
            {{ item.prefix }}
          </div>
          <div class="wdd-question-flex-property-right">
            <el-input v-model="answer.contentArray[index]" placeholder="填写答案"
                      @input="inputClick(answer)"/>
          </div>
        </div>
      </div>
    </div>
    <div v-else-if="qType === 5">
      <div v-html="`${question.title} （ ${question.trickScore}分 )`"
           class="wdd-question-content wdd-question-title wdd-content-line-height"/>
      <div class="wdd-form-question-short-answer wdd-question-item-marin2">
        <editor v-model="answer.content" :zIndex="1" @change="inputClick(answer)"></editor>
      </div>
    </div>
    <div v-else-if="qType === 7">
      <div v-html="`${question.title} （ ${question.trickScore}分 )`"
           class="wdd-question-content wdd-question-title wdd-content-line-height"/>
    </div>
  </div>
</template>


<script setup lang="ts">
import {computed} from 'vue';
import Editor from '@/components/Editor/index.vue'

const props = defineProps({
  question: {
    type: Object,
    default: function () {
      return {}
    }
  },
  answer: {
    type: Object,
    default: function () {
      return {id: null, content: '', contentArray: [], doRight: false}
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

const answer = computed<object | undefined>({
  get() {
    return props.answer;
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

function answerChange(answer) {
  if (answer.questionType === 2 || answer.questionType === 6) {
    answer.completed = answer.contentArrayKey.length !== 0
  } else {
    answer.completed = true
  }
}

function inputClick(answer) {
  if (answer.questionType === 4) {
    let gapCompleted = true
    for (const aItem of answer.contentArray) {
      if (aItem.length === 0) {
        gapCompleted = false
        break
      }
    }
    answer.completed = gapCompleted
  } else if (answer.questionType === 5) {
    answer.completed = answer.content.length !== 0
  }
}
</script>

