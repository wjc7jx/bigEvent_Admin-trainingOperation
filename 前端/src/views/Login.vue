<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <img src="@/assets/login_bg.jpg" alt="Login Background" class="bg-image">
        <div class="overlay"></div>
        <div class="left-content">
          <img src="@/assets/logo2.png" alt="Logo" class="logo">
          <h2>欢迎使用后台管理系统</h2>
          <p>Professional Backend Management Solution</p>
        </div>
      </div>
      
      <div class="login-right">
        <transition name="fade" mode="out-in">
          <!-- 注册表单 -->
          <el-form
            v-if="isRegister"
            ref="formRef"
            :model="formData"
            :rules="rules"
            class="login-form"
            size="large"
          >
            <h2>注册账号</h2>
            <p class="form-desc">请填写以下信息创建您的账号</p>
            
            <el-form-item prop="username">
              <el-input
                v-model="formData.username"
                :prefix-icon="User"
                placeholder="请输入用户名"
              />
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input
                v-model="formData.password"
                :prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item prop="rePassword">
              <el-input
                v-model="formData.rePassword"
                :prefix-icon="Lock"
                type="password"
                placeholder="请确认密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item>
              <el-button
                type="primary"
                class="submit-btn"
                :loading="loading"
                @click="handleRegister"
              >
                注册
              </el-button>
            </el-form-item>
            
            <div class="form-footer">
              已有账号？
              <el-link
                type="primary"
                :underline="false"
                @click="switchForm(false)"
              >
                立即登录
              </el-link>
            </div>
          </el-form>
          
          <!-- 登录表单 -->
          <el-form
            v-else
            ref="formRef"
            :model="formData"
            :rules="rules"
            class="login-form"
            size="large"
          >
            <h2>账号登录</h2>
            <p class="form-desc">欢迎回来！请输入您的账号密码</p>
            
            <el-form-item prop="username">
              <el-input
                v-model="formData.username"
                :prefix-icon="User"
                placeholder="请输入用户名"
              />
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input
                v-model="formData.password"
                :prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
                show-password
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            
            <el-form-item class="remember-row">
              <el-checkbox v-model="rememberMe">记住我</el-checkbox>
              <el-link type="primary" :underline="false">忘记密码？</el-link>
            </el-form-item>
            
            <el-form-item>
              <el-button
                type="primary"
                class="submit-btn"
                :loading="loading"
                @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>
            
            <div class="form-footer">
              没有账号？
              <el-link
                type="primary"
                :underline="false"
                @click="switchForm(true)"
              >
                立即注册
              </el-link>
            </div>
          </el-form>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/token.js'
import { userRegisterService, userLoginService } from '@/api/user.js'

const router = useRouter()
const tokenStore = useTokenStore()
const formRef = ref(null)
const loading = ref(false)
const isRegister = ref(false)
const rememberMe = ref(false)

// 表单数据
const formData = reactive({
  username: '',
  password: '',
  rePassword: ''
})

// 校验密码的函数
const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== formData.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
  ],
  rePassword: [
    { validator: checkRePassword, trigger: 'blur' }
  ]
}

// 切换表单
const switchForm = (isReg) => {
  isRegister.value = isReg
  formData.username = ''
  formData.password = ''
  formData.rePassword = ''
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 处理注册
const handleRegister = async () => {
  if (loading.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    const result = await userRegisterService(formData)
    ElMessage.success(result.msg || '注册成功')
    switchForm(false)
  } catch (error) {
    if (error.response) {
      ElMessage.error(error.response.data.message || '注册失败')
    }
  } finally {
    loading.value = false
  }
}

// 处理登录
const handleLogin = async () => {
  if (loading.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    const result = await userLoginService(formData)
    ElMessage.success(result.msg || '登录成功')
    tokenStore.setToken(result.data)
    router.push('/')
  } catch (error) {
    if (error.response) {
      ElMessage.error(error.response.data.message || '登录失败')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
  
  .login-box {
    width: 1000px;
    height: 600px;
    border-radius: 20px;
    box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
    display: flex;
    overflow: hidden;
    background-color: #fff;
    
    .login-left {
      position: relative;
      width: 500px;
      overflow: hidden;
      
      .bg-image {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
      
      .overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(to right, rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.6));
      }
      
      .left-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        text-align: center;
        color: #fff;
        
        .logo {
          width: 120px;
          margin-bottom: 20px;
        }
        
        h2 {
          font-size: 28px;
          margin-bottom: 10px;
        }
        
        p {
          font-size: 16px;
          opacity: 0.8;
        }
      }
    }
    
    .login-right {
      flex: 1;
      padding: 40px;
      display: flex;
      align-items: center;
      
      .login-form {
        width: 100%;
        max-width: 380px;
        margin: 0 auto;
        
        h2 {
          font-size: 28px;
          color: #333;
          margin-bottom: 10px;
          text-align: center;
        }
        
        .form-desc {
          color: #666;
          text-align: center;
          margin-bottom: 30px;
        }
        
        .el-input {
          height: 50px;
          
          :deep(.el-input__wrapper) {
            background-color: #f5f7fa;
            box-shadow: none;
            border: 2px solid transparent;
            
            &:hover,
            &.is-focus {
              border-color: var(--primary-color);
              background-color: #fff;
            }
          }
        }
        
        .remember-row {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        }
        
        .submit-btn {
          width: 100%;
          height: 50px;
          font-size: 16px;
          font-weight: bold;
          
          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
          }
        }
        
        .form-footer {
          margin-top: 20px;
          text-align: center;
          color: #666;
        }
      }
    }
  }
}

// 响应式设计
@media screen and (max-width: 1024px) {
  .login-container {
    .login-box {
      width: 90%;
      height: auto;
      flex-direction: column;
      
      .login-left {
        width: 100%;
        height: 200px;
      }
      
      .login-right {
        padding: 30px 20px;
      }
    }
  }
}

// 动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>