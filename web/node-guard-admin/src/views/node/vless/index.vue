<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button type="primary" @click="handleAdd">新增节点</el-button>
      <el-button type="success" @click="handleImport">导入节点</el-button>
      <el-button 
        type="danger" 
        :disabled="selectedIds.length === 0"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>

    <el-table 
      v-loading="loading" 
      :data="list" 
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="节点名称" />
      <el-table-column prop="server" label="服务器地址" />
      <el-table-column prop="port" label="端口" width="100" />
      <el-table-column prop="type" label="传输类型" width="100" />
      <el-table-column prop="network" label="网络类型" width="100" />
      <el-table-column prop="tls" label="TLS" width="80">
        <template #default="{ row }">
          <el-tag :type="row.tls === 1 ? 'success' : 'info'">
            {{ row.tls === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 节点表单弹窗 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="节点名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入节点名称" />
        </el-form-item>
        <el-form-item label="UUID" prop="uuid">
          <el-input v-model="form.uuid" placeholder="请输入UUID" />
        </el-form-item>
        <el-form-item label="服务器地址" prop="server">
          <el-input v-model="form.server" placeholder="请输入服务器地址" />
        </el-form-item>
        <el-form-item label="端口" prop="port">
          <el-input-number v-model="form.port" :min="1" :max="65535" />
        </el-form-item>
        <el-form-item label="传输类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择传输类型">
            <el-option label="TCP" value="tcp" />
            <el-option label="HTTP" value="http" />
            <el-option label="gRPC" value="grpc" />
          </el-select>
        </el-form-item>
        <el-form-item label="网络类型" prop="network">
          <el-select v-model="form.network" placeholder="请选择网络类型">
            <el-option label="TCP" value="tcp" />
            <el-option label="HTTP" value="http" />
            <el-option label="gRPC" value="grpc" />
          </el-select>
        </el-form-item>
        <el-form-item label="UDP支持">
          <el-switch
            v-model="form.udp"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
        <el-form-item label="TLS">
          <el-switch
            v-model="form.tls"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
        <el-form-item label="SNI" v-if="form.tls">
          <el-input v-model="form.serverName" placeholder="请输入SNI" />
        </el-form-item>
        <el-form-item label="gRPC服务名称" v-if="form.type === 'grpc'">
          <el-input v-model="form.grpcServiceName" placeholder="请输入gRPC服服务名称" />
        </el-form-item>
        <el-form-item label="加密方式">
          <el-select v-model="form.security" placeholder="请选择加密方式">
            <el-option label="无" value="" />
            <el-option label="REALITY" value="reality" />
          </el-select>
        </el-form-item>
        <template v-if="form.security === 'reality'">
          <el-form-item label="公钥">
            <el-input v-model="form.publicKey" placeholder="请输入公钥" />
          </el-form-item>
          <el-form-item label="短ID">
            <el-input v-model="form.shortId" placeholder="请输入短ID" />
          </el-form-item>
        </template>
        <el-form-item label="客户端指纹">
          <el-select v-model="form.clientFingerprint" placeholder="请选择客户端指纹">
            <el-option label="Chrome" value="chrome" />
            <el-option label="Firefox" value="firefox" />
            <el-option label="Safari" value="safari" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>

    <!-- 添加导入节点弹窗 -->
    <el-dialog
      title="导入节点"
      v-model="importDialogVisible"
      width="600px"
    >
      <el-form>
        <el-form-item>
          <el-input
            v-model="importContent"
            type="textarea"
            :rows="10"
            placeholder="请输入vless://开头的节点链接（如有多个链接，只会导入第一个）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportConfirm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getNodeList, saveNode, deleteNode, importNode, batchDelete } from '@/api/node/vless'
import type { VlessNode } from '@/types'

const loading = ref(false)
const list = ref<VlessNode[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')

const formRef = ref<FormInstance>()
const form = ref<Partial<VlessNode>>({
  name: '',
  uuid: '',
  server: '',
  port: 443,
  type: 'tcp',
  network: 'tcp',
  udp: 0,
  tls: 0,
  serverName: '',
  grpcServiceName: '',
  security: '',
  publicKey: '',
  shortId: '',
  clientFingerprint: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入节点名称', trigger: 'blur' }],
  uuid: [{ required: true, message: '请输入UUID', trigger: 'blur' }],
  server: [{ required: true, message: '请输入服务器地址', trigger: 'blur' }],
  port: [{ required: true, message: '请输入端口', trigger: 'blur' }],
  type: [{ required: true, message: '请选择传输类型', trigger: 'change' }]
}

// 导入相关的响应式变量
const importDialogVisible = ref(false)
const importContent = ref('')

// 批量删除相关
const selectedIds = ref<number[]>([])

// 表格选择变化
const handleSelectionChange = (selection: VlessNode[]) => {
  selectedIds.value = selection.map(item => item.id)
}

// 获节点列表
const getList = async () => {
  try {
    loading.value = true
    const { data } = await getNodeList()
    list.value = data
  } catch (error) {
    ElMessage.error('获取节点列表失败')
  } finally {
    loading.value = false
  }
}

// 新增节点
const handleAdd = () => {
  dialogTitle.value = '新增节点'
  dialogVisible.value = true
}

// 编辑节点
const handleEdit = (row: VlessNode) => {
  dialogTitle.value = '编辑节点'
  Object.assign(form.value, row)
  dialogVisible.value = true
}

// 保存节点
const handleSave = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    await saveNode(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 删除节点
const handleDelete = (row: VlessNode) => {
  ElMessageBox.confirm('确认删除该节点？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteNode(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 重置表单
const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  Object.assign(form.value, {
    name: '',
    uuid: '',
    server: '',
    port: 443,
    type: 'tcp',
    network: 'tcp',
    udp: 0,
    tls: 0,
    serverName: '',
    grpcServiceName: '',
    security: '',
    publicKey: '',
    shortId: '',
    clientFingerprint: '',
    status: 1
  })
}

// 打开导入弹窗
const handleImport = () => {
  importContent.value = ''
  importDialogVisible.value = true
}

// 确认导入
const handleImportConfirm = async () => {
  // 移除前后空格和换行，并处理多行输入
  const links = importContent.value
    .split('\n')
    .map(link => link.trim())
    .filter(link => link)  // 过滤空行
    .filter(link => link.startsWith('vless://'))  // 只保留有效的vless链接

  if (links.length === 0) {
    ElMessage.warning('请输入正确的vless节点链接')
    return
  }

  try {
    // 直接使用原始链接，不进行编码
    const vlessUrl = links[0]
    await importNode(vlessUrl)
    ElMessage.success('导入成功')
    importDialogVisible.value = false
    getList() // 刷新列表
  } catch (error) {
    ElMessage.error('导入失败')
  }
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) return

  ElMessageBox.confirm(
    `确认删除选中的 ${selectedIds.value.length} 个节点？`, 
    '提示', 
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await batchDelete(selectedIds.value)
      ElMessage.success('删除成功')
      selectedIds.value = [] // 清空选择
      getList() // 刷新列表
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;

  .el-button {
    margin-right: 10px;
  }
}
</style>
