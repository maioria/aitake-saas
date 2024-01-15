<template>
    <view class="container">
        <CommonHeader :leftIcon="true" backgroundColor="#fff">
            <template v-slot:content>
                <text>{{ title }}</text>
            </template>
        </CommonHeader>
        <view class="content">
            <view class="form">
                <view class="form-item">
                    <view class="form-item-label">
                        <text>类目名称（必填）</text>
                    </view>
                    <view class="form-item-input-box">
                        <input v-model="formData.name" class="form-item-input" placeholder="请输入" />
                    </view>
                </view>
                <view class="form-item">
                    <view class="form-item-label">
                        <text>类型（必填）</text>
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
                <view class="form-item">
                    <view class="form-item-label">
                        <text>单位（必填）</text>
                    </view>
                    <view class="form-item-input-box">
                        <picker @change="bindUnitPickerChange" :value="unitIndex" :range="unitLabelDict">
                            <view class="uni-input">
                                <text v-if="unitIndex != null">
                                    {{ unitLabelDict[unitIndex] }}
                                </text>
                                <text v-else class="picker-default-label">
                                    请选择
                                </text>
                            </view>
                        </picker>
                    </view>
                </view>
                <view class="form-item">
                    <view class="form-item-label">
                        <text>类目简介</text>
                    </view>
                    <view class="form-item-input-box">
                        <input v-model="formData.introduction" class="form-item-input" placeholder="请输入" />
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
import { ref, onMounted } from "vue";
import { onLoad, onShow } from "@dcloudio/uni-app";
import { getDictData } from "@/api/dict";
import { createCategory, updateCategory, getCategoryDetail } from "@/api/category";

const pages = getCurrentPages();
const page = pages[pages.length - 1];
const eventChannel = page.getOpenerEventChannel();

// 配置title，如果参数中带有id，则是编辑，否则是新建
const title = ref("");
const typeDict = ref([]);
const typeLabelDict = ref([]);
const typeValueDict = ref([]);
const typeIndex = ref(null);
const unitDict = ref([]);
const unitLabelDict = ref([]);
const unitValueDict = ref([]);
const unitIndex = ref(null);
const loading = ref(false);
const formData = ref({
    id: null,
    name: "",
    type: "",
    unit: "",
    introduction: ""
});
onLoad((option) => {
    // 同步等待请求getDictData
    if (option.id) {
        title.value = "编辑类目";
        formData.value.id = option.id;
        uni.showLoading({
            title: "加载中"
        });
        getDictData({ type: 'wms_unit' }).then((res) => {
            unitDict.value = res.data;
            unitDict.value.forEach((item) => {
                unitLabelDict.value.push(item.label);
                unitValueDict.value.push(item.value);
            });
            getDictData({ type: 'wms_category_type' }).then((res) => {
                typeDict.value = res.data;
                typeDict.value.forEach((item) => {
                    typeLabelDict.value.push(item.label);
                    typeValueDict.value.push(item.value);
                });
                // 加载类目数据
                getCategoryDetail({ id: option.id }).then((res) => {
                    uni.hideLoading();
                    formData.value = res.data;
                    typeIndex.value = typeValueDict.value.indexOf(formData.value.type);
                    unitIndex.value = unitValueDict.value.indexOf(formData.value.unit);
                    if (typeIndex.value === -1) {
                        typeIndex.value = null;
                    }
                    if (unitIndex.value === -1) {
                        unitIndex.value = null;
                    }
                });
            });
        });
    } else {
        title.value = "新建类目";
        getDictData({ type: 'wms_unit' }).then((res) => {
            unitDict.value = res.data;
            unitDict.value.forEach((item) => {
                unitLabelDict.value.push(item.label);
                unitValueDict.value.push(item.value);
            });
        });
        getDictData({ type: 'wms_category_type' }).then((res) => {
            typeDict.value = res.data;
            typeDict.value.forEach((item) => {
                typeLabelDict.value.push(item.label);
                typeValueDict.value.push(item.value);
            });
        });
    }
});
const bindTypePickerChange = (e) => {
    typeIndex.value = e.detail.value
};
const bindUnitPickerChange = (e) => {
    unitIndex.value = e.detail.value
    console.log(unitIndex.value)
};
const handleSubmit = () => {
    if (loading.value) {
        return;
    }
    if (!formData.value.name) {
        uni.showToast({
            title: "请输入类目名称！",
            icon: "none"
        });
        return;
    }
    if (typeIndex.value === "" || typeIndex.value === null) {
        uni.showToast({
            title: "请选择类型！",
            icon: "none"
        });
        return;
    }
    if (unitIndex.value === "" || unitIndex.value === null) {
        uni.showToast({
            title: "请选择单位！",
            icon: "none"
        });
        return;
    }
    loading.value = true;
    formData.value.type = typeDict.value[typeIndex.value].value;
    formData.value.unit = unitDict.value[unitIndex.value].value;
    if (formData.value.id) {
        updateCategory(formData.value).then((res) => {
            uni.showToast({
                title: "编辑成功！",
                icon: "none"
            });
            eventChannel.emit('submit');
            uni.navigateBack();
        }).finally(() => {
            loading.value = false;
        });;
    } else {
        createCategory(formData.value).then((res) => {
            uni.showToast({
                title: "新建成功！",
                icon: "none"
            });
            eventChannel.emit('submit');
            uni.navigateBack();
        }).finally(() => {
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