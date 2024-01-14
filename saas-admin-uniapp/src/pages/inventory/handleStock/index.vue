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
                    <image class="sku-img" :src="skuImage || '/static/img/img/inventory-default-pic.png'">
                    </image>
                </view>
                <view class="info-box">
                    <view class="label-box">
                        <view class="title">
                            规格：
                        </view>
                        <view class="value">
                            {{ spuName }} - {{ skuName }}
                        </view>
                    </view>
                    <view class="label-box">
                        <view class="title">
                            库位：
                        </view>
                        <view class="value">
                            {{ warehouseName }} - {{ warehouseShelveName }} - {{ warehouseStorage }}
                        </view>
                    </view>
                    <view class="label-box">
                        <view class="title">
                            数量：
                        </view>
                        <view class="value">
                            {{ stock }} {{ unitName }}
                        </view>
                    </view>
                </view>
            </view>
            <view class="form-box">
                <view class="form-item">
                    <text class="label">数量（必填）</text>
                    <view class="form-item-input-box">
                        <input class="input" type="number" v-model="formData.stock" placeholder="请选择" />
                    </view>
                </view>
                <view class="form-item">
                    <text class="label">实际出库时间（默认当前时间）</text>
                    <view class="form-item-input-box">
                        <!-- <input class="input" v-model="formData.datetime" readonly placeholder="请选择" /> -->
                        <uni-datetime-picker type="datetime" v-model="formData.datetime" placeholder="日期时间" />
                    </view>
                </view>
                <view @click="handleSelectUser" class="form-item">
                    <text class="label">关联用户</text>
                    <view class="form-item-input-box">
                        <input :readonly="true" :value="selectedUserName" class="input" placeholder="请选择" />
                    </view>
                </view>
                <view class="form-item">
                    <text class="label">备注</text>
                    <view class="form-item-input-box">
                        <input v-model="formData.remark" class="input" placeholder="请输入" />
                    </view>
                </view>
            </view>
        </view>
        <BottomTool btnLabel="确认" :btnClick="handleBottomClick" class="bottom-tool-component" />
    </view>
</template>
<script setup lang="ts">
import {
    ref,
    onMounted,
} from "vue";
import CommonHeader from "@/components/CommonHeader.vue";
import BottomTool from "@/components/BottomTool.vue";
import { onLoad } from "@dcloudio/uni-app";
import { handleWarehouseStock } from "@/api/inventory";

const pages = getCurrentPages();
const page = pages[pages.length - 1];
const eventChannel = page.getOpenerEventChannel();

const skuImage = ref("");
const spuName = ref("");
const skuName = ref("");
const unitName = ref("");
const warehouseName = ref("");
const warehouseShelveName = ref("");
const warehouseStorage = ref("");
const stock = ref(0);
const pageTitle = ref("");
const selectedUserName = ref("");

const formData = ref({
    stock: null,
    datetime: null,
    requestDepartmentId: null,
    requestUserId: null,
    remark: null,
    warehouseStockId: null,
    type: null
});


onLoad((option) => {
    skuImage.value = option.skuImage;
    spuName.value = option.spuName;
    skuName.value = option.skuName;
    unitName.value = option.unitName;
    warehouseName.value = option.warehouseName;
    warehouseShelveName.value = option.warehouseShelveName;
    warehouseStorage.value = option.warehouseStorage;
    stock.value = option.stock;
    formData.value.warehouseStockId = option.warehouseStockId;
    formData.value.type = option.type;

    if (option.type === "IN") {
        pageTitle.value = "入库";
    } else {
        pageTitle.value = "出库";
    }

    uni.setNavigationBarTitle({
        title: pageTitle.value
    });

});

const handleBottomClick = () => {
    if (!formData.value.stock) {
        uni.showToast({
            title: "请输入有效数量",
            icon: "none"
        });
        return;
    }
    handleWarehouseStock(formData.value).then((res) => {
        uni.showToast({
            title: "操作成功",
            icon: "none"
        });
        eventChannel.emit('submit');
        uni.navigateBack();
    });
};

const handleSelectUser = () => {
    console.log("handleSelectUser");
    uni.navigateTo({
        url: "/pages/inventory/createInventory/selector?type=user",
        success: (res) => {
            res.eventChannel.on("selected", (data) => {
                selectedUserName.value = data.label;
                formData.value.requestUserId = data.key;
            });
        },
    });
};
</script>
<style lang="scss" scoped>
.content {
    background-color: #F6F7F9;

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
        margin-top: 30rpx;
        background-color: #fff;
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
}
</style>