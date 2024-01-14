<template>
    <view class="container">
        <CommonHeader :leftIcon="true" backgroundColor="#fff">
            <template v-slot:content>
                <text>{{ title }}</text>
            </template>
        </CommonHeader>
        <view class="content">
            <HeaderSearch :inputChange="handleInputSearch" inputPlaceholder="关键字搜索" />
            <view class="item-box">
                <view class="item" v-for="item in itemList" @click="handleSelected(item)">
                    {{ item.label }}
                </view>
            </view>
        </view>
    </view>
</template>
<script setup lang="ts">
import {
    ref,
    onMounted,
} from "vue";
import CommonHeader from "@/components/CommonHeader.vue";
import { onLoad } from "@dcloudio/uni-app";
import { getWarehouseSelector, getWarehouseShelveSelector, getCategorySelector, getSkuSelector, getUserSelector } from "@/api/inventory";
import HeaderSearch from "@/components/HeaderSearch.vue";
const pages = getCurrentPages();
const page = pages[pages.length - 1];

const eventChannel = page.getOpenerEventChannel();

const typeLabelDict = { 'warehouse': '仓库', 'warehouseShelve': '货架', 'category': '类目', 'sku': '规格' };

const title = ref("");
const selectType = ref(null);
const selectParams = ref({});
const keyword = ref("");
const itemList = ref([]);
onLoad((option) => {
    selectType.value = option.type;
    title.value = typeLabelDict[selectType.value];
    if (option.params) {
        selectParams.value = JSON.parse(option.params);
    }
    initData();
});
const initData = () => {
    selectParams.value.keyword = keyword.value;
    if (selectType.value === 'warehouse') {
        getWarehouseSelector(selectParams.value).then(data => {
            itemList.value = data.data;
        });
    } else if (selectType.value === 'warehouseShelve') {
        getWarehouseShelveSelector(selectParams.value).then(data => {
            itemList.value = data.data;
        })
    } else if (selectType.value === 'category') {
        getCategorySelector(selectParams.value).then(data => {
            itemList.value = data.data;
        })
    } else if (selectType.value === 'sku') {
        getSkuSelector(selectParams.value).then(data => {
            itemList.value = data.data;
        })
    } else if (selectType.value === 'user') {
        getUserSelector(selectParams.value).then(data => {
            const userList = data.data;
            itemList.value = userList.map((item: any) => {
                return {
                    label: item.nickname,
                    value: item.id
                }
            });
        })
    }
}

const handleInputSearch = (value: string) => {
    keyword.value = value;
    // 如果类型是用户，则不需要搜索，直接在本地过滤
    if (selectType.value === 'user') {
        itemList.value = itemList.value.filter((item: any) => {
            return item.label.indexOf(keyword.value) > -1;
        });
    } else {
        initData();
    }
}

const handleSelected = (item: any) => {
    eventChannel.emit('selected', item);
    uni.navigateBack();
}
</script>
<style lang="less" scoped>
.item-box {
    padding: 0 30rpx;

    .item {
        height: 40rpx;
        font-size: 28rpx;
        font-weight: 400;
        color: #000000;
        line-height: 40rpx;
        margin-bottom: 30rpx;
    }
}
</style>