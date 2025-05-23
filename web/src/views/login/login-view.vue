<script lang="ts" setup>
import {
  ElMessage,
  ElAvatar,
  ElButton,
  ElCard,
  ElCol,
  ElForm,
  ElFormItem,
  ElInput,
  ElRow,
  type FormInstance,
  type FormRules
} from 'element-plus'
import { onMounted, reactive, ref, Transition } from 'vue'
import logo from '@/assets/logo.png'
import router from '@/router'
import background from '@/assets/background-gree.jpeg'
import { api } from '@/utils/api-instance'
import type { UserLoginInput } from '@/apis/domain/model/static'

const loginForm = reactive<UserLoginInput>({ phone: '', password: '' })
const ruleFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof loginForm>>({
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { max: 16, min: 6, message: '密码长度介于6，16' }
  ]
})
const showPanel = ref(false)
onMounted(() => {
  setTimeout(() => {
    showPanel.value = true
  }, 1000)
})
const handleLogin = async () => {
  console.log('开始登录，提交数据:', loginForm)
  try {
    const res = await api.userController.login({ body: loginForm })
    console.log('登录响应完整数据:', res)

    localStorage.setItem('token', res.tokenValue)
    ElMessage.success('登录成功')
    await router.replace({ path: '/' })
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请检查账号密码')
  }
}
</script>
<template>
  <div>
    <img alt="背景图片" class="background" :src="background" />
    <el-row class="panel-wrapper" justify="center" align="middle">
      <el-col :xs="18" :sm="16" :md="14" :lg="10" :xl="10">
        <transition name="el-zoom-in-top">
          <el-card class="panel" v-if="showPanel">
            <div class="content">
              <div class="panel-left">
                <el-avatar alt="logo" :size="100" shape="square" :src="logo"></el-avatar>
                <div class="title">AI助手</div>
                <div class="description">构建你的AI助手</div>
              </div>
              <div class="panel-right">
                <div class="title">快速开始</div>
                <el-form
                  ref="ruleFormRef"
                  :model="loginForm"
                  :rules="rules"
                  class="form"
                  label-position="top"
                  label-width="100px"
                >
                  <el-form-item label="手机号码：">
                    <el-input v-model="loginForm.phone"></el-input>
                  </el-form-item>
                  <el-form-item label="密   码：">
                    <el-input v-model="loginForm.password" type="password"></el-input>
                  </el-form-item>
                </el-form>
                <div class="button-wrapper">
                  <el-button class="login" type="primary" @click="handleLogin"> 登录</el-button>
                  <el-button
                    class="register"
                    type="info"
                    size="small"
                    link
                    @click="() => router.push('/register')"
                  >
                    注册
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </transition>
      </el-col>
    </el-row>
  </div>
</template>
<style lang="scss" scoped>
.background {
  position: fixed;
  height: 100vh;
  width: 100vw;
  object-fit: cover;
  z-index: -10;
}

.panel-wrapper {
  height: 100vh;

  .panel {
    .content {
      display: flex;
      align-items: stretch;
      height: 60vh;
      min-height: 250px;
      max-height: 350px;

      .title {
        font-size: var(--el-font-size-extra-large);
        margin-top: 16px;
        font-weight: bold;
      }

      .description {
        margin-top: 20px;
        font-size: var(--el-font-size-base);
        color: var(--el-text-col);
      }

      .panel-left {
        box-sizing: border-box;
        padding: 30px;
        background-color: rgb(243, 245, 249);
        width: 50%;
        border-radius: 5px;
      }

      .panel-right {
        padding: 30px;
        width: 50%;

        .form {
          margin-top: 30px;
        }

        .button-wrapper {
          margin-top: 40px;
          display: flex;
          justify-content: center;
          position: relative;

          .login {
            width: 120px;
          }

          .register {
            width: 120px;
          }
        }
      }
    }
  }
}
</style>
