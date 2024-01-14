<template>
    <view class="container">
        <CommonHeader :leftIcon="true" backgroundColor="#fff">
            <template v-slot:content>
                <text>{{ pageTitle }}</text>
            </template>
        </CommonHeader>
        <view class="content">
            <view class="header">
                <view class="img-box">
                    <image class="sku-img" :src="skuPicUrl || '/static/img/img/inventory-default-pic.png'">
                    </image>
                </view>
                <view class="info-box">
                    <view class="label-box">
                        <view class="value">
                            {{ spuName }}
                        </view>
                    </view>
                    <view class="label-box">
                        <view class="title">
                            规格：
                        </view>
                        <view class="value">
                            {{ skuName }}
                        </view>
                    </view>
                </view>
            </view>
            <view class="form-box">
                <view class="form-item">
                    <text class="label">仓库（必填）</text>
                    <view @click="handleSelectWarehouse" class="form-item-input-box">
                        <input class="input" :readonly="true" :value="warehouseName" placeholder="请选择" />
                    </view>
                </view>
                <view class="form-item">
                    <text class="label">货架（必填）</text>
                    <view @click="handleSelectWarehouseShelve" class="form-item-input-box">
                        <input class="input" :readonly="true" :value="warehouseShelveName" placeholder="请选择" />
                    </view>
                </view>
                <view class="form-item">
                    <text class="label">货位（必填）</text>
                    <view class="form-item-input-box">
                        <input v-model="formData.warehouseStorage" class="input" placeholder="请输入" />
                    </view>
                </view>
                <view class="form-item">
                    <text class="label">数量（必填）</text>
                    <view class="form-item-input-box">
                        <input v-model="formData.stock" class="input" type="number" placeholder="请填写" />
                    </view>
                </view>
            </view>
        </view>
        <BottomTool btnLabel="确认" :btnClick="handleCreate" />
    </view>
</template>
<script setup lang="ts">
import {
    ref,
    onMounted,
} from "vue";
import CommonHeader from "@/components/CommonHeader.vue";
import BottomTool from "@/components/BottomTool.vue";
import { createInventory } from "@/api/inventory";
import { onLoad } from "@dcloudio/uni-app";
import { getWarehouseSelector } from "@/api/inventory";
const pageTitle = ref('库存管理');

const pages = getCurrentPages();
const page = pages[pages.length - 1];
const eventChannel = page.getOpenerEventChannel();

const warehouseName = ref("");
const warehouseShelveName = ref("");
const spuName = ref("");
const skuName = ref("");
const skuPicUrl = ref("");
const skuUnitName = ref("");
const loading = ref(false);

const formData = ref({
    productSpuId: null,
    productSkuId: null,
    warehouseId: null,
    warehouseShelveId: null,
    warehouseStorage: null,
    stock: null
});

onMounted(() => {
    uni.setNavigationBarTitle({
        title: pageTitle.value,
    });
});
onLoad((option) => {
    spuName.value = option.spuName;
    skuName.value = option.skuName;
    skuPicUrl.value = option.skuPicUrl;
    skuUnitName.value = option.skuUnitName;
    
    formData.value.productSpuId = option.spuId;
    formData.value.productSkuId = option.skuId;

    // 获取仓库选择列表，并默认选择第一个
    getWarehouseSelector().then(data => {
        data = data.data;
        console.log(data)
        if (data.length > 0) {
            warehouseName.value = data[0].label;
            formData.value.warehouseId = data[0].key;
        }
    });
});

const handleCreate = () => {
    if (loading.value) {
        return;
    }
    if (!formData.value.warehouseId) {
        uni.showToast({
            title: "请选择仓库",
            icon: "none",
            duration: 2000,
        });
        return;
    }
    if (!formData.value.warehouseShelveId) {
        uni.showToast({
            title: "请选择货架",
            icon: "none",
            duration: 2000,
        });
        return;
    }
    if (!formData.value.warehouseStorage) {
        uni.showToast({
            title: "请输入货位",
            icon: "none",
            duration: 2000,
        });
        return;
    }
    if (!formData.value.stock) {
        uni.showToast({
            title: "请输入数量",
            icon: "none",
            duration: 2000,
        });
        return;
    }
    createInventory(formData.value).then(data => {
        uni.showToast({
            title: "创建成功",
            icon: "success",
            duration: 2000,
        });
        setTimeout(() => {
            eventChannel.emit('submit');
            uni.navigateBack();
        }, 2000);
    }).finally(() => {
        loading.value = false;
    })
};

const handleSelectWarehouse = () => {
    uni.navigateTo({
        url: "/pages/inventory/createInventory/selector?type=warehouse",
        success: (res) => {
            res.eventChannel.on("selected", (data) => {
                warehouseName.value = data.label;
                formData.value.warehouseId = data.key;
            });
        },
    });
}

const handleSelectWarehouseShelve = () => {
    // 需要先选中仓库
    if (!formData.value.warehouseId) {
        uni.showToast({
            title: "请先选择仓库",
            icon: "none",
            duration: 2000,
        });
        return;
    }
    uni.navigateTo({
        url: "/pages/inventory/createInventory/selector?type=warehouseShelve&params=" + JSON.stringify({
            warehouseId: formData.value.warehouseId
        }),
        success: (res) => {
            res.eventChannel.on("selected", (data) => {
                warehouseShelveName.value = data.label;
                formData.value.warehouseShelveId = data.key;
            });
        },
    });
}
</script>
<style lang="less" scoped>
.header {
    display: flex;
    padding: 30rpx;
    background-color: #fff;
    border-top: 1rpx solid #EDEDED;

    .img-box {
        width: 140rpx;
        height: 140rpx;

        .sku-img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
    }

    .info-box {
        flex: 1;
        padding-left: 30rpx;

        .label-box {
            display: flex;
            margin-bottom: 10upx;

            .title {
                color: #7A7A7A;
            }

            .value {
                flex: 1;
                color: #000;
            }
        }
    }
}

.form-box {
    .form-item {
        border-top: 1rpx solid #EDEDED;
        padding: 30rpx 40rpx;

        .form-item-label {
            height: 40rpx;
            font-size: 28rpx;
            font-weight: 400;
            color: #000000;
            line-height: 40rpx;
        }

        .form-item-input-box {
            margin-top: 20rpx;
        }

        .picker-default-label {
            height: 46rpx;
            font-size: 32rpx;
            font-weight: 400;
            color: #7A7A7A;
            line-height: 46rpx;
        }
    }
}
</style>