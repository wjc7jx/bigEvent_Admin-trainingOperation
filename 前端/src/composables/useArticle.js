import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  articleCategoryListService, 
  articleListService,
  articleAddService,
  articleDeleteService,
  articleGetDetailService, 
  articleUpdateService 
} from '@/api/article.js'

export function useArticle() {
  const articles = ref([])
  const categories = ref([])
  const loading = ref(false)
  const total = ref(0)

  // 获取文章分类列表
  const fetchCategories = async () => {
    try {
      const result = await articleCategoryListService()
      categories.value = result.data
    } catch (error) {
      handleError(error)
    }
  }

  // 获取文章列表
  const fetchArticles = async (params) => {
    loading.value = true
    try {
      const result = await articleListService(params)
      articles.value = result.data.items
      total.value = result.data.total

      // 处理分类名称
      articles.value = articles.value.map(article => ({
        ...article,
        categoryName: categories.value.find(c => c.id === article.categoryId)?.categoryName
      }))
    } catch (error) {
      handleError(error)
    } finally {
      loading.value = false
    }
  }

  // 添加文章
  const addArticle = async (articleData) => {
    loading.value = true
    try {
      const result = await articleAddService(articleData)
      ElMessage.success(result.msg || '添加成功')
      return true
    } catch (error) {
      handleError(error)
      return false
    } finally {
      loading.value = false
    }
  }

  // 更新文章
  const updateArticle = async (articleData) => {
    loading.value = true
    try {
      const result = await articleUpdateService(articleData)
      ElMessage.success(result.msg || '更新成功')
      return true
    } catch (error) {
      handleError(error)
      return false
    } finally {
      loading.value = false
    }
  }

  // 删除文章
  const deleteArticle = async (id) => {
    loading.value = true
    try {
      const result = await articleDeleteService(id)
      ElMessage.success(result.msg || '删除成功')
      return true
    } catch (error) {
      handleError(error)
      return false
    } finally {
      loading.value = false
    }
  }

  // 获取文章详情
  const getArticleDetail = async (id) => {
    loading.value = true
    try {
      const result = await articleGetDetailService(id)
      return result.data
    } catch (error) {
      handleError(error)
      return null
    } finally {
      loading.value = false
    }
  }

  // 统一错误处理
  const handleError = (error) => {
    if (error.response) {
      ElMessage.error(error.response.data.message || '操作失败')
    } else if (error.request) {
      ElMessage.error('网络请求失败，请检查网络连接')
    } else {
      ElMessage.error('操作失败，请稍后重试')
    }
  }

  return {
    articles,
    categories,
    loading,
    total,
    fetchCategories,
    fetchArticles,
    addArticle,
    updateArticle,
    deleteArticle,
    getArticleDetail
  }
}
