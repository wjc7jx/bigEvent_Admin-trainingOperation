<template>
    <el-card class="page-container">
      <template #header>
        <div class="header">
          <span>修改密码</span>
        </div>
      </template>
      
      <el-form 
        ref="formRef" 
        :model="pwdForm" 
        :rules="rules" 
        label-width="100px"
        size="large"
      >
        <el-form-item label="原密码" prop="old_pwd">
          <el-input 
            v-model="pwdForm.old_pwd" 
            type="password" 
            show-password
            placeholder="请输入原密码"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="新密码" prop="new_pwd">
          <el-input 
            v-model="pwdForm.new_pwd" 
            type="password" 
            show-password
            placeholder="请输入新密码"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="re_pwd">
          <el-input 
            v-model="pwdForm.re_pwd" 
            type="password" 
            show-password
            placeholder="请确认新密码"
          ></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="updatePassword"
            :loading="loading"
          >修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  import { ElMessage } from 'element-plus'
  import { useRouter } from 'vue-router'
  import { useTokenStore } from '@/stores/token.js'
  import { userUpdatePwdService } from '@/api/user.js'
  
  const router = useRouter()
  const tokenStore = useTokenStore()
  const formRef = ref(null)
  const loading = ref(false)
  
  // 表单数据
  const pwdForm = reactive({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
  })
  
  // 表单校验规则
  const rules = {
    old_pwd: [
      { required: true, message: '请输入原密码', trigger: 'blur' },
      { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
    ],
    new_pwd: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
      { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' },
      { 
        validator: (rule, value, callback) => {
          if (value === pwdForm.old_pwd) {
            callback(new Error('新密码不能与原密码相同'))
          } else {
            callback()
          }
        },
        trigger: 'blur'
      }
    ],
    re_pwd: [
      { required: true, message: '请确认新密码', trigger: 'blur' },
      { 
        validator: (rule, value, callback) => {
          if (value !== pwdForm.new_pwd) {
            callback(new Error('两次输入的密码不一致'))
          } else {
            callback()
          }
        },
        trigger: 'blur'
      }
    ]
  }
  
  // 更新密码
  const updatePassword = async () => {
    // 表单校验
    await formRef.value.validate()
    
    loading.value = true
    try {
      await userUpdatePwdService(pwdForm)
      ElMessage.success('密码修改成功，请重新登录')
      // 退出登录
      tokenStore.removeToken()
      // 跳转到登录页
      router.push('/login')
    } catch (error) {
      ElMessage.error(error.response?.data?.message || '密码修改失败')
    } finally {
      loading.value = false
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .page-container {
    min-height: 100%;
    box-sizing: border-box;
    
    .header {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    
    :deep(.el-input) {
      width: 380px;
    }
  }
  </style>
  