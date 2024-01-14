<template>
    <view>
        <view class="flex-sub">
            <view class="img-default-box" v-for="(item, index) in imgList" :key="index" @tap="ViewImage" :data-url="imgList[index]">
                <LoadingRound class="loading-box" v-if="loading"></LoadingRound>
                <image class="img-default-size" style="left: 0upx;" :src="imgList[index]" mode="aspectFill"></image>
                <view v-if="!readOnly" class="img-default-tip delete-label" @tap.stop="DelImg" :data-index="index">
                    删除
                </view>
            </view>
            <view v-if="readOnly && imgList.length < 1" class="solids">
                <image class="img-default-size" src="/static/img/img/pic-default.png" />
            </view>
            <view v-if="!readOnly && imgList.length < num" class="img-default-box" @tap="ChooseImage">
                <image class="img-default-size" src="/static/img/img/pic-default.png" />
                <view class="img-default-tip">请上传或拍照</view>
            </view>
        </view>
    </view>
</template>
<script setup lang="ts">
import { ref, onMounted, watch, defineEmits } from 'vue';
import { getAccessToken } from '@/utils/auth';
import LoadingRound from './LoadingRound.vue';
import __config from "@/config/env";

const emit = defineEmits();

const imgList = ref([]);

const props = defineProps({
    num: {
        type: Number,
        default: () => 1,
    },
    readOnly: {
        type: [Boolean, String],
        default: () => false,
    },
    sImgList: {
        type: Array,
        default: () => ([]),
    },
});

const imgListRef = ref(props.sImgList);
const loading = ref(false);

onMounted(() => {
    imgList.value = props.sImgList;
});

watch(() => props.sImgList, (newValue, oldValue) => {
    console.log(newValue)
    imgList.value = newValue;
}, { deep: true });

const callBack = () => {
    emit("change-images", imgList.value);
};

const ChooseImage = () => {
    const _self = this;
    loading.value = true;
    uni.chooseImage({
        count: 1,
        // sizeType: ['original', 'compressed'],
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
            uni.uploadFile({
                header: {
                    "Authorization": 'Bearer ' + getAccessToken(),
                },
                name: "file",
                filePath:res.tempFilePaths[0],
                url: `${__config.baseUrl}${__config.uploadImagePath}`,
                success: (res) => {
                    let imgUrl = JSON.parse(res.data).data;
                    imgList.value.push(imgUrl);
                    console.log(imgList.value)
                },
                fail(e) {
                    console.error(e, "失败原因");
                    uni.showToast({
                        title: "上传失败",
                        icon: "none",
                    });
                },
                complete: () => {
                    loading.value = false;
                },
            });
        },
    });
};

const ViewImage = (e) => {
    uni.previewImage({
        urls: imgList.value,
        current: e.currentTarget.dataset.url
    });
};

const DelImg = (e) => {
    uni.showModal({
        title: '提示',
        content: '确定要删除该图片吗？',
        cancelText: '取消',
        confirmText: '确定',
        success: (res) => {
            if (res.confirm) {
                imgList.value.splice(e.currentTarget.dataset.index, 1)
                callBack();
            }
        },
    });
};

</script>

<style lang="scss" scoped>
.icon-add {
    text-align: center;
    padding-bottom: 0px !important;
}

.img-default-box {
    height: 200rpx;
    display: flex;
    align-items: flex-end;

    .img-default-tip {
        margin-left: 30rpx;
        width: 200rpx;
        height: 46rpx;
        font-size: 28rpx;
        font-weight: 400;
        color: #7A7A7A;
        line-height: 46rpx;
        &.delete-label {
            color: #4187FE;
        }
    }
}

.img-default-size {
    width: 200rpx;
    height: 200rpx;
}

.loading-box {
    width: 100%;
    height: 300rpx;
    position: absolute;
}
</style>