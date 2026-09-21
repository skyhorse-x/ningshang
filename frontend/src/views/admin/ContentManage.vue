<template>
  <div>
    <div class="page-header"><h3>{{ pageTitle }}</h3><span class="tip">{{ mode === 'settings' ? '管理与全站品牌信息' : '对应前台页面，修改保存后即时生效' }}</span></div>

    <el-tabs v-model="activeTab" class="content-tabs">
      <el-tab-pane v-if="mode === 'settings'" label="基本设置" name="basic">
        <el-form :model="forms.basic" label-width="100px" class="content-form">
          <el-divider content-position="left">备案信息</el-divider>
          <el-form-item label="备案号"><el-input v-model="forms.basic.icp_number" placeholder="皖ICP备2026XXXXXX号-1" /></el-form-item>
          <el-form-item label="底部品牌简介"><RichEditor v-if="activeTab === 'basic'" v-model="forms.basic.footer_brand_desc" height="220px" /></el-form-item>
          <el-divider content-position="left">首页轮播图设置</el-divider>
          <el-alert class="mapping-tip" type="info" show-icon :closable="false" title="首页顶部轮播图，最多 3 张，建议使用 1920×600 以上的宽幅横图；留空则显示系统默认图。" />
          <el-row :gutter="20">
            <el-col :span="8"><el-form-item label="轮播图一"><ImageUpload v-model="forms.basic.bg_hero_1" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="轮播图二"><ImageUpload v-model="forms.basic.bg_hero_2" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="轮播图三"><ImageUpload v-model="forms.basic.bg_hero_3" /></el-form-item></el-col>
          </el-row>
          <el-divider content-position="left">栏目背景图设置</el-divider>
          <el-alert class="mapping-tip" type="info" show-icon :closable="false" title="栏目背景图用于页面顶部横幅，页面大背景图用于正文区域背景。" />
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="集团概况栏目"><ImageUpload v-model="forms.basic.bg_about_banner" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="集团概况大背景"><ImageUpload v-model="forms.basic.bg_about_page" /></el-form-item></el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="新闻中心栏目"><ImageUpload v-model="forms.basic.bg_news_banner" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="新闻中心大背景"><ImageUpload v-model="forms.basic.bg_news_page" /></el-form-item></el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="集团产业栏目"><ImageUpload v-model="forms.basic.bg_industry_banner" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="集团产业大背景"><ImageUpload v-model="forms.basic.bg_industry_page" /></el-form-item></el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="联系宁商栏目"><ImageUpload v-model="forms.basic.bg_contact_banner" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="联系宁商大背景"><ImageUpload v-model="forms.basic.bg_contact_page" /></el-form-item></el-col>
          </el-row>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('basic')">保存基本设置</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('intro')" label="集团简介" name="intro">
        <el-form :model="forms.intro" label-width="120px" class="content-form">
          <el-form-item label="副标题"><el-input v-model="forms.intro.about_intro_meta" placeholder="一徽藏一城，一潮见格局" /></el-form-item>
          <el-divider content-position="left">第一段</el-divider>
          <el-form-item label="标题"><el-input v-model="forms.intro.about_intro_s1_title" placeholder="徽韵承城，扎根本土根基" /></el-form-item>
          <el-form-item label="正文"><RichEditor v-if="activeTab === 'intro'" v-model="forms.intro.about_intro_s1_body" height="260px" /></el-form-item>
          <el-divider content-position="left">第二段</el-divider>
          <el-form-item label="标题"><el-input v-model="forms.intro.about_intro_s2_title" placeholder="潮涌科创，锚定主业航向" /></el-form-item>
          <el-form-item label="正文"><RichEditor v-if="activeTab === 'intro'" v-model="forms.intro.about_intro_s2_body" height="260px" /></el-form-item>
          <el-divider content-position="left">第三段</el-divider>
          <el-form-item label="标题"><el-input v-model="forms.intro.about_intro_s3_title" placeholder="五子联动，共筑产业潮头" /></el-form-item>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('intro')">保存集团简介</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('stats')" label="首页内容" name="stats">
        <el-form :model="forms.stats" label-width="120px" class="content-form">
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="成立年份"><el-input v-model="forms.stats.stat_founded" placeholder="2018" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="成员企业"><el-input v-model="forms.stats.stat_companies" placeholder="4" /></el-form-item></el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="知识产权"><el-input v-model="forms.stats.stat_ip" placeholder="10+" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="业务领域"><el-input v-model="forms.stats.stat_fields" placeholder="4" /></el-form-item></el-col>
          </el-row>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('stats')">保存统计数字</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('speech')" label="董事长致词" name="speech">
        <el-form :model="forms.speech" label-width="100px" class="content-form">
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="姓名"><el-input v-model="forms.speech.speech_chairman_name" placeholder="王 力" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="职务"><el-input v-model="forms.speech.speech_chairman_title" placeholder="安徽宁商科技集团 董事长" /></el-form-item></el-col>
          </el-row>
          <el-form-item label="引言"><RichEditor v-if="activeTab === 'speech'" v-model="forms.speech.speech_quote" height="220px" /></el-form-item>
          <el-form-item label="正文"><RichEditor v-if="activeTab === 'speech'" v-model="forms.speech.speech_body" height="420px" /></el-form-item>
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="落款"><el-input v-model="forms.speech.speech_sign" placeholder="安徽宁商科技集团有限公司 董事长 王力" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="日期"><el-input v-model="forms.speech.speech_date" placeholder="2026年8月" /></el-form-item></el-col>
          </el-row>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('speech')">保存董事长致词</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('culture')" label="企业文化" name="culture">
        <el-form :model="forms.culture" label-width="120px" class="content-form">
          <el-form-item label="导语"><RichEditor v-if="activeTab === 'culture'" v-model="forms.culture.culture_lead" height="220px" /></el-form-item>
          <el-form-item label="使命"><RichEditor v-if="activeTab === 'culture'" v-model="forms.culture.culture_mission" height="180px" /></el-form-item>
          <el-form-item label="核心价值观"><RichEditor v-if="activeTab === 'culture'" v-model="forms.culture.culture_values" height="180px" /></el-form-item>
          <el-form-item label="愿景"><RichEditor v-if="activeTab === 'culture'" v-model="forms.culture.culture_vision" height="180px" /></el-form-item>
          <el-form-item label="精神"><RichEditor v-if="activeTab === 'culture'" v-model="forms.culture.culture_spirit" height="180px" /></el-form-item>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('culture')">保存企业文化</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('party')" label="党建工作" name="party">
        <el-form :model="forms.party" label-width="100px" class="content-form">
          <el-form-item label="标题"><el-input v-model="forms.party.party_title" placeholder="党建领航聚合力 实干奋进启新程" /></el-form-item>
          <el-form-item label="副标题"><el-input v-model="forms.party.party_meta" placeholder="安徽宁商科技集团筑牢红色根基引领高质量发展" /></el-form-item>
          <el-form-item label="正文"><RichEditor v-if="activeTab === 'party'" v-model="forms.party.party_body" height="460px" /></el-form-item>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('party')">保存党建工作</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('construction')" label="建筑工程" name="construction">
        <el-form :model="forms.construction" label-width="100px" class="content-form">
          <el-form-item label="页面正文"><RichEditor v-if="activeTab === 'construction'" v-model="forms.construction.industry_construction_body" height="420px" /></el-form-item>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('construction')">保存建筑工程内容</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('software')" label="软件科技" name="software">
        <el-form :model="forms.software" label-width="100px" class="content-form">
          <el-form-item label="页面正文"><RichEditor v-if="activeTab === 'software'" v-model="forms.software.industry_software_body" height="420px" /></el-form-item>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('software')">保存软件科技内容</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('recruit')" label="人才理念" name="recruit">
        <el-form :model="forms.recruit" label-width="100px" class="content-form">
          <el-form-item label="页面正文"><RichEditor v-if="activeTab === 'recruit'" v-model="forms.recruit.recruit_body" height="460px" /></el-form-item>
          <el-form-item><el-button v-if="hasPermission('content:update')" type="primary" :loading="saving" @click="saveGroup('recruit')">保存人才理念内容</el-button></el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-if="showSection('other')" label="扩展内容" name="other">
        <div class="extension-actions"><el-button v-if="hasPermission('content:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button></div>
        <el-table :data="otherList" stripe class="other-table" @selection-change="onSelectionChange">
          <el-table-column v-if="hasPermission('content:batch_delete')" type="selection" width="44" />
          <el-table-column prop="sortOrder" label="排序" width="70" />
          <el-table-column prop="title" label="名称" width="160" />
          <el-table-column prop="contentKey" label="键名" width="200" />
          <el-table-column label="内容" show-overflow-tooltip><template #default="{ row }">{{ richTextPreview(row.content) }}</template></el-table-column>
          <el-table-column label="操作" width="140">
            <template #default="{ row }">
              <el-button v-if="hasPermission('content:update')" size="small" @click="openDialog(row)">编辑</el-button>
              <el-button v-if="hasPermission('content:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="add-row"><el-button v-if="hasPermission('content:create')" type="primary" plain @click="openDialog()">+ 新增配置项</el-button></div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑配置' : '新增配置'" :width="isBody ? 'min(960px, 94vw)' : 'min(560px, 94vw)'" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="键名"><el-input v-model="form.contentKey" :disabled="!!form.id" placeholder="如 about_intro_s1_body" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.title" placeholder="显示用名称" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
        <el-form-item label="内容">
          <RichEditor v-if="isBody && dialogVisible" :key="form.id || form.contentKey" v-model="form.content" height="400px" />
          <el-input v-else v-model="form.content" type="textarea" :rows="8" />
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="saving" @click="onSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import RichEditor from '@/components/admin/RichEditor.vue'
import ImageUpload from '@/components/admin/ImageUpload.vue'
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'
import { isRichContentKey, richTextPreview } from '@/utils/richText'
import { clearContentCache } from '@/utils/content'
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'

const props = defineProps({ mode: { type: String, default: 'pages' }, section: { type: String, default: 'intro' } })
const mode = computed(() => props.mode)
const activeTab = ref(props.mode === 'settings' ? 'basic' : props.section)
const titles = { stats: '首页内容', intro: '集团简介', speech: '董事长致词', culture: '企业文化', party: '党建工作', construction: '建筑工程', software: '软件科技', recruit: '人才理念', other: '扩展内容' }
const pageTitle = computed(() => mode.value === 'settings' ? '网站设置' : (titles[props.section] || '页面内容'))
const showSection = name => mode.value === 'pages' && props.section === name
const saving = ref(false)
const dialogVisible = ref(false)
const form = ref({})
const otherList = ref([])
const allList = ref([])
const isBody = computed(() => isRichContentKey(form.value.contentKey || ''))
watch(() => [props.mode, props.section], ([value, section]) => { activeTab.value = value === 'settings' ? 'basic' : section })

const knownKeys = {
  basic: ['contact_address', 'contact_phone', 'contact_email', 'office_hours', 'icp_number', 'footer_brand_desc', 'bg_about_banner', 'bg_about_page', 'bg_news_banner', 'bg_news_page', 'bg_industry_banner', 'bg_industry_page', 'bg_contact_banner', 'bg_contact_page', 'bg_hero_1', 'bg_hero_2', 'bg_hero_3'],
  intro: ['about_intro_meta', 'about_intro_s1_title', 'about_intro_s1_body', 'about_intro_s2_title', 'about_intro_s2_body', 'about_intro_s3_title'],
  stats: ['stat_founded', 'stat_companies', 'stat_ip', 'stat_fields'],
  speech: ['speech_chairman_name', 'speech_chairman_title', 'speech_quote', 'speech_body', 'speech_sign', 'speech_date'],
  culture: ['culture_lead', 'culture_mission', 'culture_values', 'culture_vision', 'culture_spirit'],
  party: ['party_title', 'party_meta', 'party_body'],
  construction: ['industry_construction_body'],
  software: ['industry_software_body'],
  recruit: ['recruit_body']
}
const defaultTitles = {
  industry_construction_body: '建筑工程正文',
  industry_software_body: '软件科技正文',
  recruit_body: '人才理念正文',
  bg_hero_1: '首页轮播图一',
  bg_hero_2: '首页轮播图二',
  bg_hero_3: '首页轮播图三'
}
// 后台自动补建时的排序号（与已有 bg_* 键的 90~97 顺次衔接）
const defaultSortOrder = { bg_hero_1: 98, bg_hero_2: 99, bg_hero_3: 100 }

const forms = reactive({
  basic: {},
  intro: {},
  stats: {},
  speech: {},
  culture: {},
  party: {},
  construction: {},
  software: {},
  recruit: {}
})

const emptyForm = () => ({ id: null, contentKey: '', title: '', content: '', sortOrder: allList.value.length + 1 })

const load = async () => {
  const res = await api.adminList('content')
  if (res.code === 200) {
    allList.value = res.data
    const grouped = {}
    for (const key of Object.keys(knownKeys)) { grouped[key] = {} }
    for (const item of res.data) {
      for (const [group, keys] of Object.entries(knownKeys)) {
        if (keys.includes(item.contentKey)) {
          grouped[group][item.contentKey] = item.content
          break
        }
      }
    }
    // 后台还没创建过的键补空值：否则新增的配置项（如首页轮播图）不会进入表单，首次保存会丢数据
    for (const [group, keys] of Object.entries(knownKeys)) {
      for (const key of keys) {
        if (grouped[group][key] === undefined) grouped[group][key] = ''
      }
    }
    Object.assign(forms.basic, grouped.basic)
    Object.assign(forms.intro, grouped.intro)
    Object.assign(forms.stats, grouped.stats)
    Object.assign(forms.speech, grouped.speech)
    Object.assign(forms.culture, grouped.culture)
    Object.assign(forms.party, grouped.party)
    Object.assign(forms.construction, grouped.construction)
    Object.assign(forms.software, grouped.software)
    Object.assign(forms.recruit, grouped.recruit)

    const knownKeySet = new Set(Object.values(knownKeys).flat())
    otherList.value = res.data.filter(item => !knownKeySet.has(item.contentKey))
  }
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('content', load)
onMounted(load)

const findByKey = (key) => allList.value.find(item => item.contentKey === key)

const saveGroup = async (group) => {
  saving.value = true
  try {
    const keys = knownKeys[group]
    for (const [contentKey, content] of Object.entries(forms[group])) {
      if (keys.includes(contentKey)) {
        const item = findByKey(contentKey)
        if (item) {
          await api.adminUpdate('content', item.id, { ...item, content })
        } else if (content) {
          // 该键后台还没有记录（如新加的首页轮播图），首次填写时自动建
          await api.adminCreate('content', {
            contentKey,
            title: defaultTitles[contentKey] || contentKey,
            content,
            sortOrder: defaultSortOrder[contentKey] || allList.value.length + 1
          })
        }
      }
    }
    clearContentCache()
    ElMessage.success('保存成功')
    await load()
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const openDialog = (row) => {
  form.value = row ? { ...row } : emptyForm()
  dialogVisible.value = true
}

const onSave = async () => {
  if (!form.value.contentKey) { ElMessage.warning('请输入键名'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('content', form.value.id, form.value)
      : await api.adminCreate('content', form.value)
    if (res.code === 200) { clearContentCache(); ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}

const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除配置「${row.title || row.contentKey}」吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('content', row.id)
    if (res.code === 200) { clearContentCache(); ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: flex-end; gap: 16px; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.page-header .tip { font-size: 12px; color: #909399; }
.mapping-tip { margin-bottom: 14px; }
.content-tabs { background: #fff; padding: 8px 24px 24px; border-radius: 8px; box-shadow: 0 1px 3px rgba(13,58,114,.05); }
.content-tabs :deep(.el-tabs__header) { display: none; }
.content-tabs :deep(.el-tabs__content) { overflow: visible; }
.content-form { max-width: 1000px; }
.content-form { padding-top: 16px; }
.content-form :deep(.el-divider) { margin: 28px 0 24px; }
.content-form :deep(.el-form-item__content) { min-width: 0; }
.add-row { margin-top: 16px; }
.other-table { margin-top: 10px; }
.extension-actions { display: flex; justify-content: flex-end; margin: 14px 0; }
@media (max-width: 768px) {
  .page-header { align-items: flex-start; flex-direction: column; gap: 4px; }
  .content-tabs { padding: 6px 14px 18px; }
  .content-form { padding-top: 10px; }
}
</style>
