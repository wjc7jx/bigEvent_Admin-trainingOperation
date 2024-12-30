<!-- 文章列表组件 -->
<template>
  <div class="article-list">
    <el-table
      v-loading="loading"
      :data="articles"
      style="width: 100%"
      :header-cell-style="{
        background: '#f5f7fa',
        color: '#333',
        fontWeight: 600
      }"
      :row-class-name="tableRowClassName"
    >
      <el-table-column label="文章标题" min-width="300">
        <template #default="{ row }">
          <div class="article-title">
            <el-image
              v-if="row.coverImg"
              :src="row.coverImg"
              fit="cover"
              class="article-cover"
            />
            <div class="title-content">
              <h3>{{ row.title }}</h3>
              <p v-if="row.description" class="description">{{ row.description }}</p>
            </div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="分类" width="120" align="center">
        <template #default="{ row }">
          <article-category :category="row.categoryName" />
        </template>
      </el-table-column>
      
      <el-table-column label="发表时间" width="180" align="center">
        <template #default="{ row }">
          <article-time :time="row.createTime" />
        </template>
      </el-table-column>
      
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <article-status :status="row.state" />
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-tooltip content="编辑文章" placement="top">
              <el-button
                :icon="Edit"
                circle
                plain
                type="primary"
                @click="handleEdit(row)"
              />
            </el-tooltip>
            
            <el-tooltip content="删除文章" placement="top">
              <el-button
                :icon="Delete"
                circle
                plain
                type="danger"
                @click="handleDelete(row)"
              />
            </el-tooltip>
            
            <el-tooltip :content="row.state === '已发布' ? '预览文章' : '继续编辑'" placement="top">
              <el-button
                :icon="row.state === '已发布' ? View : EditPen"
                circle
                plain
                type="info"
                @click="handlePreview(row)"
              />
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
      
      <template #empty>
        <el-empty description="暂无文章数据" />
      </template>
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 50]"
      :background="true"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Edit, Delete, View, EditPen } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import ArticleCategory from './ArticleCategory.vue'
import ArticleStatus from './ArticleStatus.vue'
import ArticleTime from './ArticleTime.vue'

const props = defineProps({
  articles: {
    type: Array,
    default: () => []
  },
  total: {
    type: Number,
    default: 0
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:page', 'edit', 'delete', 'preview'])

const currentPage = ref(1)
const pageSize = ref(10)

const tableRowClassName = ({ row, rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}

const handleSizeChange = (size) => {
  pageSize.value = size
  emit('update:page', { page: currentPage.value, pageSize: size })
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  emit('update:page', { page, pageSize: pageSize.value })
}

const handleEdit = (row) => {
  emit('edit', row)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      '删除后将无法恢复，是否确认删除该文章？',
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        draggable: true
      }
    )
    emit('delete', row)
  } catch {
    // 用户取消删除操作
  }
}

const handlePreview = (row) => {
  emit('preview', row)
}
</script>

<style lang="scss" scoped>
.article-list {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  
  .article-title {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .article-cover {
      width: 60px;
      height: 60px;
      border-radius: 4px;
      object-fit: cover;
    }
    
    .title-content {
      flex: 1;
      min-width: 0;
      
      h3 {
        margin: 0;
        font-size: 16px;
        color: #333;
        line-height: 1.4;
        @include text-overflow;
      }
      
      .description {
        margin: 4px 0 0;
        font-size: 13px;
        color: #666;
        line-height: 1.4;
        @include text-overflow;
      }
    }
  }
  
  .action-buttons {
    display: flex;
    gap: 8px;
    justify-content: center;
    
    .el-button {
      &:hover {
        transform: translateY(-2px);
      }
    }
  }
  
  :deep(.el-table) {
    --el-table-border-color: #ebeef5;
    --el-table-header-bg-color: #f5f7fa;
    
    th {
      font-weight: 600;
    }
    
    .even-row {
      background-color: var(--el-table-row-hover-bg-color);
    }
  }
  
  .pagination {
    margin-top: 20px;
    justify-content: flex-end;
  }
}

// 添加文本溢出混入
@mixin text-overflow {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
