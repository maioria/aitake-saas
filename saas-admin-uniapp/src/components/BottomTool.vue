<template>
    <view class="bottom-tool-component">
        <view class="bottom-tool-box">
            <view @click="handleClick" class="btn-box">
                <view class="btn">
                    {{ props.btnLabel  }}
                </view>
            </view>
        </view>
    </view>
</template>
<script setup lang="ts">
import { ref, getCurrentInstance, computed, onMounted } from "vue";
const props = defineProps<{
  btnLabel: String;
  btnClick: Function;
}>();

const handleClick = () => {
    props.btnClick();
}
</script>
<style lang="less" scoped>
.bottom-tool-component {
    position: fixed;
    background-color: #fff;
    bottom: 0;
    width: 100%;
    padding-bottom: calc(env(safe-area-inset-bottom) / 2);
}
.bottom-tool-box {
    padding: 15rpx 40rpx;

    .btn-box {
        height: 88rpx;
        background: #4187FE;
        border-radius: 58rpx;
        display: flex;
        justify-content: center;
        align-items: center;

        .btn {
            height: 45rpx;
            font-size: 32rpx;
            font-weight: 400;
            color: #FFFFFF;
            line-height: 45rpx;
        }
    }
}
</style>