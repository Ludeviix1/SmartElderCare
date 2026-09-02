<script setup>
  import examPackageApi from '@/api/examPackage.js'
  import {onMounted, ref} from 'vue'
  import {useRouter} from 'vue-router'
  const router = useRouter()

  const list = ref([])
  const loading = ref(false)
  const finished = ref(false)

  //卡片渐变背景色板（mock数据里每个套餐带一个gradient，后端没有这个字段，前端按下标取）
  const gradients = [
    'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  ]

  const loadData = () => {
    loading.value = true
    examPackageApi.list().then(result => {
      if (result.code == 1) {
        list.value = result.data.map((examPackage, index) => ({
          ...examPackage,
          gradient: gradients[index % gradients.length]
        }))
      }
    }).finally(() => {
      loading.value = false
      finished.value = true
    })
  }

  onMounted(loadData)
</script>

<template>
  <div class="package-page">
    <van-nav-bar title="体检套餐" fixed placeholder/>
    <van-pull-refresh v-model="loading" @refresh="loadData">
      <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了">
        <div v-for="examPackage in list" :key="examPackage.id" class="package-card" @click="router.push('/package/' + examPackage.id)">
          <div class="card-thumb" :style="{background: examPackage.gradient}">{{ examPackage.name.slice(0, 2) }}</div>
          <div class="card-body">
            <div class="name">{{ examPackage.name }}</div>
            <div class="desc">{{ examPackage.description }}</div>
            <div class="card-footer">
              <span class="item-count">{{ examPackage.itemCount || 0 }}项检查</span>
              <span class="price">¥{{ examPackage.price }}</span>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<style scoped lang="scss">
  .package-page {
    padding-bottom: 20px;
  }

  .package-card {
    display: flex;
    margin: 12px 16px 0;
    padding: 12px;
    border-radius: 12px;
    background-color: #fff;

    .card-thumb {
      width: 84px;
      height: 84px;
      border-radius: 10px;
      flex-shrink: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 18px;
      font-weight: bold;
      letter-spacing: 1px;
    }

    .card-body {
      flex: 1;
      margin-left: 12px;
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .name {
        font-size: 16px;
        font-weight: bold;
        color: #323233;
      }

      .desc {
        flex: 1;
        margin-top: 4px;
        font-size: 11px;
        color: #969799;
        line-height: 1.5;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }

      .card-footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-top: 6px;

        .item-count {
          font-size: 11px;
          color: #969799;
        }

        .price {
          font-size: 18px;
          font-weight: bold;
          color: #ee0a24;
        }
      }
    }
  }
</style>
