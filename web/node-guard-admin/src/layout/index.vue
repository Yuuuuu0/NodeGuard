<template>
  <el-container class="layout">
    <el-aside width="200px">
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        :router="true"
      >
        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/sys/user">用户管理</el-menu-item>
          <el-menu-item index="/sys/dict">字典管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="node">
          <template #title>
            <el-icon><Connection /></el-icon>
            <span>节点管理</span>
          </template>
          <el-menu-item index="/node/vless">Vless节点</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/token">
          <el-icon><Key /></el-icon>
          <span>Token管理</span>
        </el-menu-item>
        <el-menu-item index="/rule">
          <el-icon><Files /></el-icon>
          <span>规则管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{ username }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>

    <el-dialog
      title="修改密码"
      v-model="passwordDialogVisible"
      width="400px"
      @close="resetPasswordForm"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            show-password
            placeholder="请输入原密码"
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            show-password
            placeholder="请输入新密码"
          />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入新密码"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdatePassword">确定</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Connection, Key, Files, Setting } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { updatePassword } from '@/api/user'

const route = useRoute()
const router = useRouter()
const username = ref('admin')

const activeMenu = computed(() => route.path)

const passwordDialogVisible = ref(false)
const passwordFormRef = ref<FormInstance>()
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 添加自定义验证规则类型
interface ValidateRule {
  required?: boolean
  message?: string
  trigger?: 'blur' | 'change'
  validator?: (rule: ValidateRule, value: string, callback: (error?: Error) => void) => void
}

const validateConfirmPassword = (rule: ValidateRule, value: string, callback: (error?: Error) => void) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== passwordForm.value.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleCommand = (command: string) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确认退出登录？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      localStorage.removeItem('token')
      router.push('/login')
    })
  } else if (command === 'password') {
    passwordDialogVisible.value = true
  }
}

const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return
  
  try {
    await passwordFormRef.value.validate()
    await updatePassword({
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })
    
    ElMessage.success('密码修改成功，请重新登录')
    localStorage.removeItem('token')
    router.push('/login')
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '修改失败')
  }
}

const resetPasswordForm = () => {
  if (!passwordFormRef.value) return
  passwordFormRef.value.resetFields()
}
</script>

<style lang="scss" scoped>
.layout {
  height: 100vh;
  
  .el-aside {
    background-color: #304156;
  }
  
  .el-header {
    background-color: #fff;
    border-bottom: 1px solid #dcdfe6;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 0 20px;
  }
  
  .header-right {
    color: #333;
    cursor: pointer;
  }
}
</style> 