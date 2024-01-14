<template>
    <view class="container">
        <CommonHeader :leftIcon="true" backgroundColor="#fff">
            <template v-slot:content>
                <text>{{ pageTitle }}</text>
            </template>
        </CommonHeader>
        <HeaderSearch class="header-search-box" :inputChange="handleInputSearch" inputPlaceholder="搜索产品名称" />
        <view class="content">
            <scroll-view :scroll-y="true" @scrolltolower="handleScrollToLower" class="record-item-list">
                <view v-for="(item, index) in recordList" :key="index" class="record-item">
                    <view class="record-item-datetime">
                        {{ formatTime(item.createTime) }}
                    </view>
                    <view class="record-item-header">
                        <text class="title">{{ item.productSpuName }}</text>
                    </view>
                    <view class="record-item-content">
                        <view class="label-box">
                            <text class="label">规格：</text>
                            <text class="value">{{ item.productSkuName }}</text>
                            <text class="label">数量：</text>
                            <text class="value">{{ item.stock }}</text>
                        </view>
                        <view class="label-box">
                            <text class="label">货位：</text>
                            <text class="value">{{ item.warehouseStorage }}</text>
                        </view>
                        <view class="label-box">
                            <text class="label">
                                {{ item.stock > 0 ? "实际入库：" : "实际出库：" }}
                            </text>
                            <text class="value">{{ formatTime(item.createTime) }}</text>
                        </view>
                        <view class="label-box">
                            <text class="label">备注：</text>
                            <text class="value">{{ item.remark }} </text>
                        </view>
                    </view>
                </view>
                <view v-if="total === recordList.length" class="no-more-data">没有更多数据了~</view>
            </scroll-view>
        </view>
    </view>
</template>
<script setup lang="ts">
import {
    ref
} from "vue";
import CommonHeader from "@/components/CommonHeader.vue";
import { onLoad } from "@dcloudio/uni-app";
import { getWarehouseStockRecord } from "@/api/inventory";
import { formatTime } from "@/utils/utils";
import HeaderSearch from "@/components/HeaderSearch.vue";
const pageTitle = ref('出入库记录');
const recordList = ref([]);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
const keyword = ref('');

onLoad(() => {
    uni.setNavigationBarTitle({
        title: pageTitle.value
    });
    // 加载初始数据
    getWarehouseStockRecord({
        pageNo: pageNo.value,
        pageSize: pageSize.value
    }).then((res) => {
        recordList.value = res.data.list;
        total.value = res.data.total;
    });
});

const handleScrollToLower = async () => {
    if (recordList.value.length < total.value) {
        pageNo.value++;
        await getWarehouseStockRecord({
            pageNo: pageNo.value,
            pageSize: pageSize.value,
            productSpuName: keyword.value
        }).then((res) => {
            recordList.value = recordList.value.concat(res.data.list);
            total.value = res.data.total;
        });
    }
};

/**
 * 用户搜索
 */
const handleInputSearch = async (value: string) => {
    keyword.value = value;
    pageNo.value = 1;
    await getWarehouseStockRecord({
        pageNo: pageNo.value,
        pageSize: pageSize.value,
        productSpuName: keyword.value
    }).then((res) => {
        recordList.value = res.data.list;
        total.value = res.data.total;
    });
}
</script>
<style lang="scss" scoped>
.container {

    .content {
        background-color: #F6F7F9;
        padding: 25rpx 30rpx;

        .record-item-list {
            height: calc(100vh - 300rpx);
        }

        .record-item {
            border-radius: 20rpx;
            background-color: #fff;
            padding: 25rpx 30rpx;
            margin-bottom: 20rpx;

            .record-item-datetime {
                height: 33rpx;
                font-size: 24rpx;
                font-weight: 400;
                color: #7A7A7A;
                line-height: 33rpx;
            }

            .record-item-header {
                margin-top: 10rpx;
            }

            .record-item-content {
                margin-top: 20rpx;

                .label-box {
                    margin-bottom: 10rpx;
                }

                .label {
                    height: 40rpx;
                    font-size: 28rpx;
                    font-weight: 400;
                    color: #7A7A7A;
                    line-height: 40rpx;
                }

                .value {
                    height: 40rpx;
                    font-size: 28rpx;
                    font-weight: 400;
                    color: #000000;
                    line-height: 40rpx;
                    margin-right: 30rpx;
                }
            }
        }
    }

    .no-more-data {
        width: 100%;
        text-align: center;
        padding: 30rpx 0;
        color: #7A7A7A;
        font-size: 28rpx;
    }

    .header-search-box {}
}
</style>
