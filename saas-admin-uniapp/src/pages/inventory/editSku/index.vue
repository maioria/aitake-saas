<template>
    <view class="container">
        <CommonHeader :leftIcon="true" backgroundColor="#fff">
            <template v-slot:content>
                <text>{{ pageTitle }}</text>
            </template>
        </CommonHeader>
        <view class="content">
            <view class="form">
                <view class="form-item">
                    <view class="form-item-label">
                        <text>所属类目</text>
                    </view>
                    <view class="form-item-input-box">
                        {{ categoryName }}
                    </view>
                </view>
                <view class="form-item">
                    <view class="form-item-label">
                        <text>图片</text>
                    </view>
                    <view class="form-item-input-box">
                        <ImageUpload :sImgList="imgList" @change-images="handleChangeImages" />
                    </view>
                </view>
                <view class="form-item">
                    <view class="form-item-label">
                        <text>规格名称（必填）</text>
                    </view>
                    <view class="form-item-input-box">
                        <input v-model="formData.name" class="form-item-input" placeholder="请输入" />
                    </view>
                </view>
                <view class="form-item">
                    <view class="form-item-label">
                        <text>规格对外名称</text>
                    </view>
                    <view class="form-item-input-box">
                        <input v-model="formData.otherName" class="form-item-input" placeholder="请输入" />
                    </view>
                </view>
                <view class="form-item">
                    <view class="form-item-label">
                        <text>单位（必填）</text>
                    </view>
                    <view class="form-item-input-box">
                        <picker @change="bindTypePickerChange" :value="typeIndex" :range="typeLabelDict">
                            <view class="uni-input">
                                <text v-if="typeIndex != null">
                                    {{ typeLabelDict[typeIndex] }}
                                </text>
                                <text v-else class="picker-default-label">
                                    请选择
                                </text>
                            </view>
                        </picker>
                    </view>
                </view>
            </view>
        </view>
        <BottomTool btnLabel="确认" :btnClick="handleSubmit" class="bottom-tool-component" />
    </view>
</template>

<script setup lang="ts">
import CommonHeader from "@/components/CommonHeader.vue";
import LoadingRound from "@/components/LoadingRound.vue";
import BottomTool from "@/components/BottomTool.vue";
import ImageUpload from "@/components/ImageUpload.vue";
import { ref, onMounted } from "vue";
import { onLoad, onShow } from "@dcloudio/uni-app";
import { getDictData } from "@/api/dict";
import { createSku, updateSku, getSkuDetail } from "@/api/category";

const pages = getCurrentPages();
const page = pages[pages.length - 1];
const eventChannel = page.getOpenerEventChannel();

// 配置title，如果参数中带有id，则是编辑，否则是新建
const categoryId = ref(null);
const categoryName = ref("");
const pageTitle = ref("");
const typeDict = ref([]);
const typeLabelDict = ref([]);
const typeValueDict = ref([]);
const typeIndex = ref(null);
const loading = ref(false);
const formData = ref({
    id: null,
    spuId: null,
    name: "",
    unit: "",
    introduction: "",
    otherName: "",
    picUrl: ""
});
const imgList = ref([]);

onMounted(() => {
    
});

onLoad((option) => {
    categoryName.value = option.categoryName;
    // 同步等待请求getDictData
    if (option.id) {
        pageTitle.value = "编辑规格";
        formData.value.id = option.id;
        uni.showLoading({
            title: "加载中"
        });
        getDictData({ type: 'zkzg_sku_unit' }).then((res) => {
            typeDict.value = res.data;
            typeDict.value.forEach((item) => {
                typeLabelDict.value.push(item.label);
                typeValueDict.value.push(item.value);
            });
            // 加载类目数据
            getSkuDetail({ id: option.id }).then((res) => {
                uni.hideLoading();
                formData.value = res.data;
                if (formData.value.picUrl) {
                    imgList.value = [formData.value.picUrl];
                    console.log(imgList.value)
                }
                typeIndex.value = typeValueDict.value.indexOf(formData.value.unit);
                if (typeIndex.value === -1) {
                    typeIndex.value = null;
                }
            });
        });
    } else {
        pageTitle.value = "新建规格";
        formData.value.spuId = option.categoryId;
        getDictData({ type: 'zkzg_sku_unit' }).then((res) => {
            typeDict.value = res.data;
            typeDict.value.forEach((item) => {
                typeLabelDict.value.push(item.label);
                typeValueDict.value.push(item.value);
            });
            // 默认选中第一个
            typeIndex.value = 0;
        });
    }
    uni.setNavigationBarTitle({
        title: pageTitle.value,
    });
});

const bindTypePickerChange = (e) => {
    typeIndex.value = e.detail.value
};

const handleChangeImages = (images) => {
    imgList.value = images;
    console.log(imgList.value);
}

const handleSubmit = () => {
    if (loading.value) {
        return;
    }
    if (!formData.value.name) {
        uni.showToast({
            title: "请输入规格名称！",
            icon: "none"
        });
        return;
    }
    if (typeIndex.value === null) {
        uni.showToast({
            title: "请选择规格单位！",
            icon: "none"
        });
        return;
    }
    formData.value.unit = typeDict.value[typeIndex.value].value;
    formData.value.picUrl = imgList.value.join(',');
    loading.value = true;
    if (formData.value.id) {
        updateSku(formData.value).then((res) => {
            uni.showToast({
                title: "编辑成功！",
                icon: "none"
            });
            eventChannel.emit('submit');
            uni.navigateBack();
        }).finally (() => {
            loading.value = false;
        });
    } else {
        createSku(formData.value).then((res) => {
            uni.showToast({
                title: "新建成功！",
                icon: "none"
            });
            eventChannel.emit('submit');
            uni.navigateBack();
        }).finally (() => {
            loading.value = false;
        });
    }
};
</script>
<style lang="scss" scoped>
.container {
    height: 100vh;
    background-color: #F6F7F9;
}

.content {
    background-color: #fff;

    .form {
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