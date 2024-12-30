<template>
  <el-tag
    :type="type"
    :effect="effect"
    class="status-tag"
    size="small"
  >
    {{ label }}
  </el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true
  },
  effect: {
    type: String,
    default: 'light'
  }
})

const type = computed(() => {
  switch (props.status) {
    case '已发布':
      return 'success'
    case '草稿':
      return 'info'
    default:
      return 'default'
  }
})

const label = computed(() => {
  switch (props.status) {
    case '已发布':
      return '已发布'
    case '草稿':
      return '草稿'
    default:
      return props.status
  }
})
</script>

<style lang="scss" scoped>
.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
  transition: all var(--transition-duration);
  
  &:hover {
    transform: translateY(-2px);
  }
}
</style>
