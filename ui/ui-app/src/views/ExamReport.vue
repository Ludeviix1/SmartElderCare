<script setup>
import {onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {showToast} from 'vant'
import appointmentApi from '@/api/appointment.js'

const route = useRoute()
const router = useRouter()
const report = ref({appointment: {}, items: []})

onMounted(() => appointmentApi.report(route.params.id).then(result => {
  if (result.code === 1) report.value = result.data
  else { showToast(result.msg); router.back() }
}))

const resultText = item => {
  const value = item.resultValue == null ? '' : item.resultValue
  const unit = item.resultUnit || ''
  return item.resultText || `${value}${unit}` || '未填写'
}
</script>

<template>
  <div class="report-page">
    <van-nav-bar title="体检报告" left-arrow fixed placeholder @click-left="router.back()"/>
    <section class="report-head">
      <div class="package">{{ report.appointment.packageName || '体检报告' }}</div>
      <div>{{ report.appointment.appointmentDate }} {{ report.appointment.appointmentTime }}</div>
    </section>
    <van-cell-group inset title="检查结果">
      <van-cell v-for="item in report.items" :key="item.id" :title="item.itemName" :label="item.remark || ''">
        <template #value><span :class="item.abnormal === 1 ? 'abnormal' : 'normal'">{{ resultText(item) }}</span></template>
      </van-cell>
    </van-cell-group>
    <van-cell-group v-if="report.appointment.remark" inset title="执行备注" class="remark"><van-cell :value="report.appointment.remark"/></van-cell-group>
  </div>
</template>

<style scoped lang="scss">
.report-page { min-height: 100vh; background: #f6f7f9; }
.report-head { margin: 12px 16px; padding: 18px; border-radius: 10px; color: #fff; background: #16736a; font-size: 13px; }
.package { margin-bottom: 8px; font-size: 18px; font-weight: 700; }
.abnormal { color: #ee0a24; font-weight: 600; }.normal { color: #1989fa; }.remark { margin-top: 12px; }
</style>
