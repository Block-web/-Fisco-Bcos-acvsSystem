<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import useUserInfoStore from '@/stores/userInfo'
//导入htmlToPdf函数
import htmlToPdf from "@/utils/htmlToPDF.js";

const userInfoStore = useUserInfoStore()
const ruleFormRef = ref()
const ruleForm = reactive({
  studentName: '',
  major: '',
  collegeName: userInfoStore.info.realName.split('&')[1],
  degreeType: '',
  graduationDate: ''
})
const downloadPDF = () => {
  htmlToPdf.getPdf(`${ruleForm.studentName}`, "#education-proof");
};

const locationOptions = ['Home', 'Company', 'School']

const rules = reactive({
  studentName: [
    { required: true, message: '请输入学生姓名', trigger: 'blur' }
  ],
  major: [
    { required: true, message: '请输入专业名称', trigger: 'blur' }
  ],
  collegeName: [
    { required: true, message: '请输入毕业院校', trigger: 'blur' }
  ],
  degreeType: [
    { required: true, message: '请选择学位类型', trigger: 'change' }
  ],
  graduationDate: [
    { required: true, message: '请选择毕业时间', trigger: 'change' }
  ]
})

const imageUrl = ref('')

const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型和大小
  if (file.type !== 'image/jpeg' && file.type !== 'image/png') {
    ElMessage.error('请选择JPG或PNG格式的图片！')
    return
  }

  if (file.size / 1024 / 1024 > 2) {
    ElMessage.error('图片大小不能超过2MB！')
    return
  }

  // 创建临时URL用于预览
  imageUrl.value = URL.createObjectURL(file)
}

// 清除图片
const clearImage = () => {
  imageUrl.value = ''
  // 释放URL对象
  if (imageUrl.value) {
    URL.revokeObjectURL(imageUrl.value)
  }
}

const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('上传图片必须为JPG格式！')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('上传图片大小不能超过2MB！')
    return false
  }
  return true
}

const submitForm = async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      downloadPDF();

      console.log('submit!')
      ElMessage.success('学历证明生成成功！')
    } else {
      console.log('error submit!', fields)
      ElMessage.error('请填写完整的表单信息')
    }
  })
}

const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
  imageUrl.value = ''
}

const options = Array.from({ length: 10000 }).map((_, idx) => ({
  value: `${idx + 1}`,
  label: `${idx + 1}`,
}))
</script>


<template>
  <div class="pdf-generation-container">
    <div class="page-header">
      <h2 class="title">PDF生成</h2>
      <p class="description">生成学生成绩单、学历证明等PDF文档</p>
    </div>

    <div class="content-area">
      <div class="pdf-preview-container">
        <!-- 学历证明PDF预览容器 -->
        <div class="education-proof-pdf">
          <div class="pdf-content">
            <!-- 设置id选择器 ，这里选择要生成的pdf内容-->
            <div id="education-proof">
              <h3 class="pdf-title">学历证明</h3>
              <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="auto">
                <el-form-item label="学生姓名" prop="studentName">
                  <el-input v-model="ruleForm.studentName" placeholder="请输入学生姓名" />
                </el-form-item>
                <el-form-item label="专业名称" prop="major">
                  <el-input v-model="ruleForm.major" placeholder="请输入专业名称" />
                </el-form-item>
                <el-form-item label="毕业院校" prop="collegeName">
                  <el-input v-model="ruleForm.collegeName" :disabled="true" placeholder="请输入毕业院校" />
                </el-form-item>
                <el-form-item label="学位类型" prop="degreeType">
                  <el-select v-model="ruleForm.degreeType" placeholder="请选择学位类型" prop="degreeType">
                    <el-option label="专科" value="专科" />
                    <el-option label="本科" value="本科" />
                    <el-option label="硕士" value="硕士" />
                    <el-option label="博士" value="博士" />
                  </el-select>
                </el-form-item>
                <el-form-item label="毕业时间" prop="graduationDate" required>
                  <el-col :span="11">
                    <el-form-item prop="graduationDate">
                      <el-date-picker v-model="ruleForm.graduationDate" type="date" aria-label="Pick a date"
                        placeholder="请选择毕业时间" style="width: 100%" />
                    </el-form-item>
                  </el-col>
                </el-form-item>
                <!-- 图片上传 -->
                <div class="image-upload-container">
                  <div v-if="imageUrl" class="image-preview">
                    <img :src="imageUrl" class="avatar" />
                    
                  </div>
                  <div v-else class="file-selector">
                    <input type="file" accept="image/jpeg,image/png" @change="handleFileSelect" style="display: none"
                      ref="fileInput" />
                    <el-button type="primary" @click="$refs.fileInput.click()" class="select-btn">
                      <el-icon>
                        <Plus />
                      </el-icon>
                      选择图片
                    </el-button>
                    <p class="file-hint">支持JPG、PNG格式，最大2MB</p>
                  </div>
                </div>
              </el-form>
            </div>
            <el-button type="danger" size="small" @click="clearImage" class="clear-btn">
                      清除图片
                    </el-button>
            <div class="pdf-actions">
              <el-button type="primary" class="generate-btn" @click="submitForm(ruleFormRef)">生成学历证明</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.pdf-generation-container {
  height: 100%;
  padding: 20px;
  background: #f5f7fa;
}

.image-upload-container {
  margin: 20px 0;
}

.image-preview {
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  margin: 0 auto 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.clear-btn {
  margin-top: 10px;
}

.file-selector {
  text-align: center;
  padding: 20px;
  border: 2px dashed #ddd;
  border-radius: 4px;
}

.select-btn {
  margin-bottom: 10px;
}

.file-hint {
  color: #999;
  font-size: 12px;
  margin: 0;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.title {
  color: #303133;
  margin-bottom: 10px;
  font-size: 24px;
}

.description {
  color: #606266;
  font-size: 16px;
}

.content-area {
  height: calc(100% - 100px);
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.pdf-preview-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  width: 100%;
  padding: 20px;
}

.education-proof-pdf {
  width: 595px;
  /* A4宽度 */
  height: 842px;
  /* A4高度 */
  background: white;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.pdf-content {
  padding: 40px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.pdf-title {
  text-align: center;
  color: #303133;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
  border-bottom: 2px solid #409eff;
  padding-bottom: 10px;
}

#education-proof {
  flex: 1;
  overflow-y: auto;
}

#education-proof .el-form {
  max-width: 100%;
}

.pdf-actions {
  padding: 20px;
  border-top: 1px solid #ebeef5;
  text-align: center;
}

.generate-btn {
  width: 200px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .education-proof-pdf {
    width: 100%;
    height: auto;
    min-height: 842px;
  }

  .pdf-preview-container {
    padding: 10px;
  }
}

/* 打印样式 */
@media print {
  .pdf-generation-container {
    background: white;
    padding: 0;
  }

  .page-header,
  .pdf-actions {
    display: none;
  }

  .education-proof-pdf {
    box-shadow: none;
    border: none;
    width: 100%;
    height: auto;
  }
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>