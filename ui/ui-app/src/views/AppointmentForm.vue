<script setup>
  import examPackageApi from '@/api/examPackage.js'
  import appointmentApi from '@/api/appointment.js'
  import {onMounted, ref} from 'vue'
  import {useRoute, useRouter} from 'vue-router'
  import {showToast} from 'vant'
  import {useElderInfoStore} from '@/store/elderInfo.js'
  import {useAppointmentStore} from '@/store/appointment.js'
  const route = useRoute()
  const router = useRouter()
  const elderInfoStore = useElderInfoStore();
  const appointmentStore = useAppointmentStore();

  const examPackage = ref({})

  //预约表单
  const appointment = ref({
    elderName: '',
    phone: '',
    date: '',
    time: '' //预约时间（HH:mm）
  })

  onMounted(() => {
    examPackageApi.selectById(route.params.packageId).then(result => {
      if (result.code == 1) {
        //VO已拍平套餐和项目明细，补上展示用的渐变背景即可
        examPackage.value = {
          ...result.data,
          gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
        }
      }
    })
    //默认填登录老人的信息
    appointment.value.elderName = elderInfoStore.elder.name || ''
    appointment.value.phone = elderInfoStore.elder.phone || ''
  })

  //日期选择弹层
  const dateShow = ref(false)
  const minDate = new Date(Date.now() + 24 * 60 * 60 * 1000) //最早明天
  const maxDate = new Date(Date.now() + 90 * 24 * 60 * 60 * 1000) //最晚90天后

  const onDateConfirm = ({selectedValues}) => {
    appointment.value.date = selectedValues.join('-')
    dateShow.value = false
  }

  //时间选择弹层
  const timeShow = ref(false)
  const onTimeConfirm = ({selectedValues}) => {
    //selectedValues形如['08', '30']，拼成HH:mm
    appointment.value.time = selectedValues.join(':')
    timeShow.value = false
  }

  //提交预约
  const submitting = ref(false)
  const submit = () => {
    if (!appointment.value.elderName) {
      showToast('请输入体检人姓名')
      return
    }
    if (!/^1\d{10}$/.test(appointment.value.phone)) {
      showToast('请输入正确的手机号')
      return
    }
    if (!appointment.value.date) {
      showToast('请选择预约日期')
      return
    }
    if (appointment.value.time === '') {
      showToast('请选择预约时间')
      return
    }
    submitting.value = true
    //提交到后端，落库到exam_appointment表
    appointmentApi.add({
      packageId: examPackage.value.id,
      elderName: appointment.value.elderName,
      phone: appointment.value.phone,
      date: appointment.value.date,
      time: appointment.value.time
    }).then(result => {
      if (result.code == 1) {
        showToast('预约成功')
        //清掉本地store里的假数据，下次进列表页重新从后端拉取
        appointmentStore.clearAppointments()
        router.replace('/appointment')
      } else {
        showToast(result.msg)
      }
    }).finally(() => {
      submitting.value = false
    })
  }
</script>

<template>
  <div class="form-page">
    <van-nav-bar title="预约体检" left-arrow fixed placeholder @click-left="router.back()"/>

    <!--套餐信息-->
    <div class="package-bar">
      <div class="thumb" :style="{background: examPackage.gradient}">{{ (examPackage.name || '').slice(0, 2) }}</div>
      <div class="info">
        <div class="name">{{ examPackage.name }}</div>
        <div class="count">{{ (examPackage.examItemList || []).length }}项检查</div>
      </div>
      <div class="price">¥{{ examPackage.price }}</div>
    </div>

    <!--预约表单-->
    <van-cell-group inset class="form-card">
      <van-field
          v-model="appointment.elderName"
          label="体检人"
          placeholder="请输入体检人姓名"
          left-icon="friends-o"
      />
      <van-field
          v-model="appointment.phone"
          type="tel"
          label="联系电话"
          placeholder="请输入手机号"
          left-icon="phone-o"
      />
      <van-field
          v-model="appointment.date"
          label="预约日期"
          placeholder="请选择日期"
          left-icon="calendar-o"
          readonly
          is-link
          @click="dateShow = true"
      />
      <van-field
          v-model="appointment.time"
          label="预约时间"
          placeholder="请选择时间"
          left-icon="clock-o"
          readonly
          is-link
          @click="timeShow = true"
      />
    </van-cell-group>

    <!--说明-->
    <div class="tips">
      <div class="tips-title">温馨提示</div>
      <div class="tips-content">
        1. 体检前一天请清淡饮食，晚上8点后禁食<br>
        2. 体检当天早晨需空腹（可少量饮水）<br>
        3. 请携带身份证按时到达社区体检中心<br>
        4. 如需改期请提前一天在"我的预约"中取消
      </div>
    </div>

    <!--提交栏-->
    <van-action-bar>
      <van-action-bar-button type="primary" text="提交预约" :loading="submitting" @click="submit"/>
    </van-action-bar>

    <!--日期选择弹层-->
    <van-popup v-model:show="dateShow" position="bottom" round>
      <van-date-picker
          title="选择日期"
          :min-date="minDate"
          :max-date="maxDate"
          @confirm="onDateConfirm"
          @cancel="dateShow = false"
      />
    </van-popup>

    <!--时间选择弹层（体检中心营业时间 8:00-17:00）-->
    <van-popup v-model:show="timeShow" position="bottom" round>
      <van-time-picker
          title="选择时间"
          min-time="08:00:00"
          max-time="17:00:00"
          @confirm="onTimeConfirm"
          @cancel="timeShow = false"
      />
    </van-popup>
  </div>
</template>

<style scoped lang="scss">
  .form-page {
    padding-bottom: 90px;
  }

  .package-bar {
    display: flex;
    align-items: center;
    margin: 12px 16px 0;
    padding: 12px;
    border-radius: 12px;
    background-color: #fff;

    .thumb {
      width: 56px;
      height: 56px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 14px;
      font-weight: bold;
    }

    .info {
      flex: 1;
      margin: 0 12px;

      .name {
        font-size: 15px;
        font-weight: bold;
        color: #323233;
      }

      .count {
        margin-top: 4px;
        font-size: 11px;
        color: #969799;
      }
    }

    .price {
      font-size: 18px;
      font-weight: bold;
      color: #ee0a24;
    }
  }

  .form-card {
    margin-top: 16px;
  }

  .tips {
    margin: 16px 16px 0;
    padding: 14px 16px;
    border-radius: 12px;
    background-color: #fff7ed;

    .tips-title {
      font-size: 13px;
      font-weight: bold;
      color: #ed6a0c;
      margin-bottom: 8px;
    }

    .tips-content {
      font-size: 12px;
      color: #ed6a0c;
      line-height: 1.8;
    }
  }
</style>
