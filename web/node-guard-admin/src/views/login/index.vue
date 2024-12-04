<template>
  <div class="login-container">
    <el-form
      ref="loginFormRef"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="off"
    >
      <div class="title-container">
        <h3 class="title">NodeGuard 管理系统</h3>
      </div>

      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          placeholder="用户名"
          type="text"
          tabindex="1"
          :prefix-icon="User"
        />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          placeholder="密码"
          :type="passwordVisible ? 'text' : 'password'"
          tabindex="2"
          :prefix-icon="Lock"
          :suffix-icon="passwordVisible ? View : Hide"
          @keyup.enter="handleLogin"
          @click-suffix="passwordVisible = !passwordVisible"
        />
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        style="width: 100%; margin-bottom: 30px"
        @click="handleLogin"
      >
        登录
      </el-button>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { User, Lock, View, Hide } from '@element-plus/icons-vue'
import { login } from '@/api/user'

const router = useRouter()
const store = useStore()
const loading = ref(false)
const passwordVisible = ref(false)
const loginFormRef = ref<FormInstance>()

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    loading.value = true
    await loginFormRef.value.validate()
    
    const { data } = await login(loginForm)
    localStorage.setItem('token', data.token)
    console.log('Token saved:', data.token)
    
    await store.dispatch('user/login', data)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #2d3a4b;

  .login-form {
    width: 400px;
    padding: 30px;
    background-color: #fff;
    border-radius: 4px;
  }

  .title-container {
    text-align: center;
    margin-bottom: 30px;

    .title {
      font-size: 26px;
      color: #333;
      margin: 0;
    }
  }

  .el-input {
    --el-input-height: 40px;
    
    :deep(.el-input__wrapper) {
      background-color: transparent;
    }
  }

  .el-button {
    height: 40px;
  }
}
</style> 