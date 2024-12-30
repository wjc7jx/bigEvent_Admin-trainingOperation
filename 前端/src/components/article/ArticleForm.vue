<!-- 文章表单组件 -->
<template>
  <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
    <el-form-item label="文章标题" prop="title">
      <el-input v-model="formData.title" placeholder="请输入标题"></el-input>
    </el-form-item>
    
    <el-form-item label="文章分类" prop="categoryId">
      <el-select placeholder="请选择" v-model="formData.categoryId">
        <el-option
          v-for="c in categories"
          :key="c.id"
          :label="c.categoryName"
          :value="c.id"
        >
        </el-option>
      </el-select>
    </el-form-item>
    
    <el-form-item label="文章封面" prop="coverImg">
      <div class="cover-upload">
        <el-input 
          v-model="formData.coverImg"
          placeholder="请输入封面图片链接" 
          clearable
        >
          <template #append>
            <el-button @click="handleImageUpdate">确认</el-button>
          </template>
        </el-input>
        
        <!-- 预览图片 -->
        <div class="cover-preview" v-if="formData.coverImg">
          <img 
            :src="formData.coverImg" 
            alt="封面预览"
            @error="handleImageError"
          >
        </div>
      </div>
    </el-form-item>
    
    <el-form-item label="文章内容" prop="content">
      <div class="editor">
        <quill-editor
          theme="snow"
          v-model:content="formData.content"
          contentType="html"
        >
        </quill-editor>
      </div>
    </el-form-item>
    
    <el-form-item>
      <el-button 
        type="primary" 
        @click="handleSubmit('已发布')"
        :loading="loading"
      >发布</el-button>
      <el-button 
        type="info" 
        @click="handleSubmit('草稿')"
        :loading="loading"
      >草稿</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import { ElMessage } from 'element-plus'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

const props = defineProps({
  categories: {
    type: Array,
    default: () => []
  },
  initialData: {
    type: Object,
    default: () => ({})
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['submit'])

const formRef = ref(null)
const formData = reactive({
  title: '',
  categoryId: '',
  coverImg: '',
  content: '',
  ...props.initialData
})

const rules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 3, max: 50, message: '标题长度应在3-50个字符之间', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择文章分类', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ]
}

const validateImageUrl = (url) => {
  try {
    new URL(url)
    return true
  } catch {
    return false
  }
}

const handleImageUpdate = () => {
  if (!formData.coverImg) {
    ElMessage.warning('请输入图片链接')
    return
  }
  
  if (!validateImageUrl(formData.coverImg)) {
    ElMessage.error('请输入有效的图片URL地址')
    return
  }
}

const handleImageError = () => {
  ElMessage.error('图片加载失败，请检查链接是否有效')
}

const handleSubmit = async (state) => {
  if (props.loading) return
  
  try {
    await formRef.value.validate()
    emit('submit', { ...formData, state })
  } catch (error) {
    // 表单验证失败
  }
}
</script>

<style lang="scss" scoped>
.cover-preview {
  margin-top: 10px;
  
  img {
    max-width: 200px;
    max-height: 200px;
    object-fit: cover;
  }
}

.editor {
  width: 100%;
}
</style>
