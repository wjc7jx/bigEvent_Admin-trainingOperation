<template>
  <el-breadcrumb separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item 
        v-for="item in breadcrumbs" 
        :key="item.path"
        :to="item.path"
      >
        {{ item.meta.title }}
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const breadcrumbs = ref([])

// 更新面包屑
const updateBreadcrumbs = () => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  breadcrumbs.value = matched
}

// 监听路由变化
watch(
  () => route.path,
  () => updateBreadcrumbs(),
  { immediate: true }
)
</script>

<style lang="scss" scoped>
.el-breadcrumb {
  line-height: 1;
  
  :deep(.el-breadcrumb__item) {
    .el-breadcrumb__inner {
      color: #666;
      font-weight: normal;
      
      &.is-link {
        color: #666;
        
        &:hover {
          color: var(--primary-color);
        }
      }
    }
    
    &:last-child {
      .el-breadcrumb__inner {
        color: #333;
        font-weight: 600;
      }
    }
  }
}

.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: all 0.3s;
}

.breadcrumb-enter-from,
.breadcrumb-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-leave-active {
  position: absolute;
}
</style>
