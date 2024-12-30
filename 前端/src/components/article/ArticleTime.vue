<template>
  <span class="time-text">{{ formattedTime }}</span>
</template>

<script setup>
import { computed } from 'vue'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import relativeTime from 'dayjs/plugin/relativeTime'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const props = defineProps({
  time: {
    type: String,
    required: true
  },
  format: {
    type: String,
    default: 'YYYY-MM-DD HH:mm:ss'
  },
  relative: {
    type: Boolean,
    default: true
  }
})

const formattedTime = computed(() => {
  if (!props.time) return ''
  
  const time = dayjs(props.time)
  if (props.relative) {
    return time.fromNow()
  }
  return time.format(props.format)
})
</script>

<style lang="scss" scoped>
.time-text {
  color: #666;
  font-size: 14px;
}
</style>
