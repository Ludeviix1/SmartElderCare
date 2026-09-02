<script setup>
  import examPackageApi from '@/api/examPackage.js'
  import {onMounted, ref} from 'vue'
  import {useRoute, useRouter} from 'vue-router'
  const route = useRoute()
  const router = useRouter()

  const examPackage = ref({})
  const loading = ref(true)

  //头图渐变背景（后端套餐没有gradient字段，前端给一个默认值）
  const DEFAULT_GRADIENT = 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'

  onMounted(() => {
    examPackageApi.selectById(route.params.id).then(result => {
      if (result.code == 1) {
        //VO已拍平套餐和项目明细，补上展示用的渐变背景即可
        examPackage.value = {
          ...result.data,
          gradient: DEFAULT_GRADIENT
        }
      }
    }).finally(() => {
      loading.value = false
    })
  })

  //参考范围展示
  const referenceRangeText = (examItem) => {
    if (examItem.referenceMin == null && examItem.referenceMax == null) {
      return '-'
    }
    const min = examItem.referenceMin != null ? examItem.referenceMin : ''
    const max = examItem.referenceMax != null ? examItem.referenceMax : ''
    return `${min} ~ ${max} ${examItem.referenceUnit || ''}`
  }

  //单项原价合计
  const totalPrice = () => {
    const examItemList = examPackage.value.examItemList || []
    if (!examItemList.length) {
      return 0
    }
    return examItemList.reduce((sum, item) => sum + item.price, 0)
  }

  //去预约
  const goAppointment = () => {
    router.push('/appointment-form/' + examPackage.value.id)
  }
</script>

<template>
  <div class="detail-page">
    <van-nav-bar title='套餐详情' left-arrow fixed placeholder @click-left="router.back()"/>

    <van-loading v-if="loading" class="page-loading" size="24" vertical>加载中...</van-loading>

    <template v-else>
      <!--套餐头图-->
      <div class="banner" :style="{background: examPackage.gradient}">
        <div class="banner-name">{{ examPackage.name }}</div>
        <div class="banner-price">
          <span class="price">¥{{ examPackage.price }}</span>
          <span class="origin-price">单项合计 ¥{{ totalPrice() }}</span>
        </div>
      </div>

      <!--套餐说明-->
      <div class="desc-card">
        <div class="desc-title">套餐说明</div>
        <div class="desc-content">{{ examPackage.description }}</div>
      </div>

      <!--包含项目-->
      <div class="item-card">
        <div class="item-card-title">包含体检项目（{{ (examPackage.examItemList || []).length }}项）</div>
        <van-cell-group inset>
          <van-cell v-for="examItem in examPackage.examItemList" :key="examItem.id" center>
            <template #title>
              <span class="exam-item-name">{{ examItem.name }}</span>
              <span class="exam-item-unit" v-if="examItem.referenceUnit">({{ examItem.referenceUnit }})</span>
            </template>
            <template #label>
              <div class="exam-item-desc">{{ examItem.description }}</div>
            </template>
            <template #value>
              <div class="exam-item-range">
                <div class="range">{{ referenceRangeText(examItem) }}</div>
                <div class="single-price">¥{{ examItem.price }}</div>
              </div>
            </template>
          </van-cell>
        </van-cell-group>
      </div>
    </template>

    <!--底部预约栏-->
    <van-action-bar>
      <van-action-bar-button type="primary" text="立即预约" @click="goAppointment"/>
    </van-action-bar>
  </div>
</template>

<style scoped lang="scss">
  .detail-page {
    padding-bottom: 80px;
  }

  .page-loading {
    padding: 100px 0;
  }

  .banner {
    padding: 24px 20px;
    color: #fff;

    .banner-name {
      font-size: 20px;
      font-weight: bold;
    }

    .banner-price {
      margin-top: 10px;

      .price {
        font-size: 28px;
        font-weight: bold;
      }

      .origin-price {
        margin-left: 10px;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
        text-decoration: line-through;
      }
    }
  }

  .desc-card {
    margin: 12px 16px 0;
    padding: 14px 16px;
    border-radius: 12px;
    background-color: #fff;

    .desc-title {
      font-size: 15px;
      font-weight: bold;
      color: #323233;
      margin-bottom: 8px;
    }

    .desc-content {
      font-size: 13px;
      color: #646566;
      line-height: 1.7;
    }
  }

  .item-card {
    margin-top: 16px;

    .item-card-title {
      margin: 0 16px 10px;
      font-size: 15px;
      font-weight: bold;
      color: #323233;
    }
  }

  .exam-item-name {
    font-size: 14px;
    color: #323233;
    font-weight: bold;
  }

  .exam-item-unit {
    margin-left: 4px;
    font-size: 11px;
    color: #969799;
  }

  .exam-item-desc {
    font-size: 11px;
    color: #969799;
  }

  .exam-item-range {
    text-align: right;

    .range {
      font-size: 11px;
      color: #969799;
    }

    .single-price {
      margin-top: 2px;
      font-size: 13px;
      color: #323233;
    }
  }
</style>
