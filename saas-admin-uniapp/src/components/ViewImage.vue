<template>
    <view>
        <image class="img" :src="imgSrc" :data-url="imgOriginalSrc" mode="aspectFill" @click="ViewImage"
            :style="{ width: width, height: height }" lazy-load />
    </view>
</template>
<script setup lang="ts">
const props = defineProps({
    imgSrc: {
        type: String,
        default: () => "",
    },
    imgOriginalSrc: {
        type: String,
        default: () => "",
    },
    width: {
        type: String,
        default: () => "100%",
    },
    height: {
        type: String,
        default: () => "100%",
    },
});

const ViewImage = (e) => {
    if (!props.imgOriginalSrc) {
        return;
    }
    uni.previewImage({
        urls: [e.currentTarget.dataset.url],
        current: e.currentTarget.dataset.url
    });
};
</script>
<style lang="scss" scoped></style>