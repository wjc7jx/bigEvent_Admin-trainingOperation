<template>
  <el-tag
    :type="type"
    :effect="effect"
    class="category-tag"
    size="small"
  >
    {{ category }}
  </el-tag>
</template>

<script setup>
const props = defineProps({
  category: {
    type: String,
    required: true
  },
  type: {
    type: String,
    default: 'warning'
  },
  effect: {
    type: String,
    default: 'plain'
  }
})
</script>

<style lang="scss" scoped>
.category-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
  transition: all var(--transition-duration);
  
  &:hover {
    transform: translateY(-2px);
  }
}
</style>
