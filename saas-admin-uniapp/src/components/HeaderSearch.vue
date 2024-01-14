<template>
    <view class="header">
        <input class="search-box" @input="handleSearch" :placeholder="inputPlaceholder">
        <view class="filter-tox">

        </view>
    </view>
</template>
<script setup lang="ts">
const props = defineProps<{
    inputPlaceholder: string;
    inputChange: Function;
}>();
const handleSearch = (e: any) => {
    props.inputChange(e.detail.value);
};
</script>
<style lang="scss" scoped>
.header {
    padding: 30rpx 40rpx;

    .search-box {
        height: 40rpx;
        background: #FFFFFF;
        border-radius: 30rpx;
        border: 2rpx solid #C0C0C0;
        padding: 10rpx 30rpx;
    }
}
</style>