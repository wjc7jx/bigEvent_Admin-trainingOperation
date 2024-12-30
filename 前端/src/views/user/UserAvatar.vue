<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore();

import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore();

//用户头像地址
const imgUrl = ref(userInfoStore.info.userPic)

//图片上传成功的回调函数
const uploadSuccess = (result) => {
    imgUrl.value = result.data;
}

import { userAvatarUpdateService } from '@/api/user.js'
import { ElMessage } from 'element-plus'
//头像修改
const updateAvatar = async () => {
    //调用接口
    let result = await userAvatarUpdateService(imgUrl.value);

    ElMessage.success(result.msg ? result.msg : '修改成功')

    //修改pinia中的数据
    userInfoStore.info.userPic = imgUrl.value
}

// 选择图片的参考对象
const fileInput = ref(null)

// 预览图片
const handleFileChange = async (e) => {
    const file = e.target.files[0]
    if (file) {
        // 验证文件类型
        if (!['image/jpeg', 'image/png', 'image/gif'].includes(file.type)) {
            ElMessage.error('只能上传jpg/png/gif格式的图片')
            return
        }
        // 验证文件大小
        if (file.size > 2 * 1024 * 1024) {
            ElMessage.error('图片大小不能超过2MB')
            return
        }

        // 创建预览
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = async () => {
            // 预览图片
            imgUrl.value = reader.result
            
            try {
                // 直接调用接口更新头像
                const result = await userAvatarUpdateService(reader.result)
                if (result.code === 0) {
                    ElMessage.success('头像更新成功')
                    // 更新pinia中的用户头像
                    userInfoStore.info.userPic = reader.result
                } else {
                    ElMessage.error(result.message || '头像更新失败')
                }
            } catch (error) {
                ElMessage.error('头像更新失败')
                console.error(error)
            }
        }
    }
}

// 选择图片的方法
const handleChooseImage = () => {
  // 这里通过 fileInput.value 获取DOM元素
  if (fileInput.value) {
    fileInput.value.click()
  }
}

// 网络链接相关
const imageUrl = ref('')
const handleUrlUpload = async () => {
    if (!imageUrl.value) {
        ElMessage.warning('请输入图片链接')
        return
    }
    
    // 验证URL是否是图片链接
    // if (!/\.(jpg|jpeg|png|gif)$/i.test(imageUrl.value)) {
    //     ElMessage.error('请输入有效的图片链接(jpg/jpeg/png/gif)')
    //     return
    // }

    try {
        // 直接调用接口更新头像
        const result = await userAvatarUpdateService(imageUrl.value)
        if (result.code === 0) {
            imgUrl.value = imageUrl.value
            ElMessage.success('头像更新成功')
            // 更新pinia中的用户头像
            userInfoStore.info.userPic = imageUrl.value
            // 清空输入框
            imageUrl.value = ''
        } else {
            ElMessage.error(result.message || '头像更新失败')
        }
    } catch (error) {
        ElMessage.error('头像更新失败')
        console.error(error)
    }
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <div class="avatar-uploader">
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else src="avatar" width="278" />
                </div>
                <br />
                <!-- 本地上传 -->
                <input ref="fileInput" type="file" accept="image/jpeg,image/png,image/gif" style="display: none"
                    @change="handleFileChange">
                <el-button type="primary" :icon="Plus" size="large" @click="handleChooseImage">
                    本地上传
                </el-button>
                
                <!-- 网络链接上传 -->
                <div class="url-upload" style="margin-top: 20px;">
                    <el-input 
                        v-model="imageUrl" 
                        placeholder="请输入图片链接" 
                        style="width: 300px; margin-right: 10px;"
                    />
                    <el-button 
                        type="success" 
                        :icon="Upload" 
                        size="default" 
                        @click="handleUrlUpload"
                    >
                        链接上传
                    </el-button>
                </div>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        .avatar {
            width: 278px;
            height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}

.url-upload {
    display: flex;
    align-items: center;
}
</style>