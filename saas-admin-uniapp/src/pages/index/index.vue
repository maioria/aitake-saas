<template>
  <view class="container" :style="style">
    <view class="content">
      <view class="title-box">
        <view class="title">Hello</view>
        <view class="sub-title">
          欢迎使用爱塔可管理系统
        </view>
      </view>
      <view class="menu-box">
        <view class="menu-group" v-for="groupItem in menuGroupArray">
          <view class="menu-group-title">
            {{ groupItem.title }}
          </view>
          <view class="menu-item-list">
            <view class="menu-item" v-for="menuItem in groupItem.list"
            @click="goPage(menuItem.url)"
            >
              <image class="menu-item-image" :src="menuItem.image" />
              <view class="menu-item-title">
                {{ menuItem.title }}
              </view>
            </view>
          </view>
        </view>
      </view>
      <view class="tip-box">
        其他功能持续更新中～
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import {
  ref,
  getCurrentInstance,
  computed,
  onMounted,
} from "vue";
const StatusBar: any = getCurrentInstance()?.appContext.config.globalProperties.StatusBar;
const style = computed(
  () => `padding-top:${StatusBar}px;`
);
const menuGroupArray = ref([
  {
    title: "库存",
    list: [
      {
        title: "库存管理",
        image: "/static/img/img/menu-inventory.png",
        url: "/pages/inventory/index",
      },
      {
        title: "变更记录",
        image: "/static/img/img/menu-inventory-record.png",
        url: "/pages/inventory/record/index",
      },
    ],
  },
  // {
  //   title: "产品",
  //   list: [
  //     {
  //       title: "产品管理",
  //       image: "/static/img/img/menu-product.png",
  //       url: "/pages/product/index",
  //     },
  //   ],
  // },
]);
onMounted(() => {
  uni.setNavigationBarTitle({
    title: "首页",
  });
});

const goPage = (url: string) => {
  uni.navigateTo({
    url,
  });
};

</script>
<style lang="less" scoped>
.container {
  height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #BCD7FF 0%, #FFFFFF 100%);

  .content {
    padding: 30rpx 30rpx 0 30rpx;
  }

  .title-box {
    .title {
      height: 56rpx;
      font-size: 40rpx;
      font-weight: 600;
      color: #000000;
      line-height: 56rpx;
    }

    .sub-title {
      margin-top: 10rpx;
      height: 40rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #555555;
      line-height: 40rpx;
    }
  }

  .menu-box {
    margin-top: 30rpx;
    background: #FFFFFF;
    box-shadow: 0rpx 0rpx 10rpx 0rpx rgba(202, 202, 202, 0.5);
    border-radius: 20rpx;
    padding: 40rpx;

    .menu-group {
      margin-bottom: 40rpx;

      .menu-group-title {
        font-size: 28rpx;
        font-weight: 600;
        color: #000000;
        line-height: 40rpx;
      }

      .menu-item-list {
        margin-top: 30rpx;
        display: flex;
        flex-wrap: wrap;
        gap: 30rpx 120rpx;

        .menu-item {
          text-align: center;
          // width: 120rpx;

          .menu-item-image {
            width: 120rpx;
            height: 120rpx;
          }

          .menu-item-title {
            font-size: 30rpx;
          }
        }
      }
    }
  }

  .tip-box {
    margin-top: 182rpx;
    width: 100%;
    text-align: center;
    height: 42rpx;
    font-size: 30rpx;
    font-weight: 400;
    color: #4187FE;
    line-height: 42rpx;
  }
}
</style>
