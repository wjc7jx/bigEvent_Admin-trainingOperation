<!-- 文章搜索组件 -->
<template>
  <el-form inline>
    <el-form-item label="文章分类：">
      <el-select v-model="searchParams.categoryId" placeholder="请选择">
        <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id">
        </el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="发布状态：">
      <el-select v-model="searchParams.state" placeholder="请选择">
        <el-option label="已发布" value="已发布"></el-option>
        <el-option label="草稿" value="草稿"></el-option>
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, reactive } from 'vue'

const props = defineProps({
  categories: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['search'])

const searchParams = reactive({
  categoryId: '',
  state: ''
})

const handleSearch = () => {
  emit('search', { ...searchParams })
}

const handleReset = () => {
  searchParams.categoryId = ''
  searchParams.state = ''
  emit('search', { categoryId: null, state: null })
}
</script>
