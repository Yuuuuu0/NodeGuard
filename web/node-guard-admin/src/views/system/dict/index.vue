<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="queryParams.dictType"
        placeholder="字典类型"
        style="width: 200px"
        class="filter-item"
        @keyup.enter="handleQuery"
      />
      <el-button type="primary" @click="handleQuery">查询</el-button>
      <el-button type="primary" @click="handleAdd">新增字典</el-button>
    </div>

    <el-table v-loading="loading" :data="list" border>
      <el-table-column prop="dictType" label="字典类型" />
      <el-table-column prop="dictLabel" label="字典标签" />
      <el-table-column prop="dictValue" label="字典值" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 字典表单弹窗 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入字典标签" />
        </el-form-item>
        <el-form-item label="字典值" prop="dictValue">
          <el-input v-model="form.dictValue" placeholder="请输入字典值" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getDictList, saveDict, deleteDict } from '@/api/system/dict'
import type { DictItem } from '@/types'

const loading = ref(false)
const list = ref<DictItem[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()

const queryParams = reactive({
  dictType: ''
})

const form = reactive({
  id: '',
  dictType: '',
  dictLabel: '',
  dictValue: '',
  sort: 0,
  status: 1,
  remark: ''
})

const rules = {
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }],
  dictLabel: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
  dictValue: [{ required: true, message: '请输入字典值', trigger: 'blur' }]
}

// 查询字典列表
const getList = async () => {
  loading.value = true
  try {
    const { data } = await getDictList(queryParams)
    list.value = data
  } finally {
    loading.value = false
  }
}

// 查询按钮点击
const handleQuery = () => {
  getList()
}

// 新增字典
const handleAdd = () => {
  dialogTitle.value = '新增字典'
  dialogVisible.value = true
}

// 编辑字典
const handleEdit = (row: DictItem) => {
  dialogTitle.value = '编辑字典'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除字典
const handleDelete = (row: DictItem) => {
  ElMessageBox.confirm('确认删除该字典？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteDict(row.id)
    ElMessage.success('删除成功')
    getList()
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  
  await saveDict(form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  getList()
}

// 重置表单
const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  Object.assign(form, {
    id: '',
    dictType: '',
    dictLabel: '',
    dictValue: '',
    sort: 0,
    status: 1,
    remark: ''
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
  .filter-item {
    margin-right: 10px;
  }
}
</style> 