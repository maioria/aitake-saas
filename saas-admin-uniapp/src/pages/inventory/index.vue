<template>
    <view class="container">
        <CommonHeader :leftIcon="true" backgroundColor="#fff">
            <template v-slot:content>
                <text>{{ pageTitle }}</text>
            </template>
        </CommonHeader>
        <view class="content">
            <HeaderSearch :inputChange="handleInputSearch" inputPlaceholder="搜索类目名称" />
            <scroll-view class="spu-list-box" :scroll-y="true" @scrolltolower="handleScrollToLower">
                <view class="scroll-spu-list-box">
                    <view class="spu-box" v-for="product in spuList">
                        <view class="spu-title-box title-box">
                            <view class="spu-title title">
                                {{ product.name }}
                            </view>
                            <view @click="handleSpuMenu(product)" class="more-menu-btn-box">
                                <image class="more-menu-btn-icon" src="/static/img/icons/icon-more-menu.png"></image>
                            </view>
                        </view>

                        <template v-for="sku in product.specList">
                            <view class="sku-box" @click="handleShowStockList(sku)">
                                <view class="icon-box">
                                    <image class="collapse-icon"
                                        :class="{ collapse: !sku.stockListShow }" src="/static/img/icons/icon-collapse.png">
                                    </image>
                                </view>
                                <view class="info-box">
                                    <image class="sku-img" :src="sku.picUrl || '/static/img/img/inventory-default-pic.png'" lazy-load @click.stop="viewImage(sku.picUrl)">
                                    </image>
                                    <view class="sku-title">{{ sku.name }}</view>
                                    <view class="sku-count">
                                        {{ sku.stock }} {{ sku.unitName }}
                                    </view>
                                    <view @click.stop="handleSkuMenu(product, sku)" class="more-menu-btn-box">
                                        <image class="more-menu-btn-icon" src="/static/img/icons/icon-more-menu.png">
                                        </image>
                                    </view>
                                </view>
                            </view>
                            <view class="sku-stock-box" v-if="sku.stockListShow">
                                <LoadingRound v-if="sku.stockListLoading" />
                                <view class="sku-stock-item" v-for="(stock, index) in sku.stockList">
                                    <view class="sku-stock-item-storage">
                                        <text class="value">{{ stock.warehouseName }} - {{ stock.shelveName }}
                                            -
                                            {{ stock.storage }}</text>
                                        <text class="value" style="margin-left: 60rpx;">
                                            {{ stock.stock }} {{ sku.unitName }}
                                        </text>
                                    </view>
                                    <view class="sku-stock-item-count">
                                        <view class="stock-info">
                                        </view>
                                        <view class="btn-box">
                                            <view @click="handleDelete(stock)" class="btn">
                                                删除
                                            </view>
                                            <view @click="handleStock(product, sku, stock, 'IN')" class="btn">
                                                入库
                                            </view>
                                            <view @click="handleStock(product, sku, stock, 'OUT')" class="btn">
                                                出库
                                            </view>
                                        </view>
                                    </view>
                                    <!-- 如果是最后一项，不需要分隔线 -->
                                    <view v-if="index !== (sku.stockList.length - 1)" class="divider"></view>
                                </view>
                            </view>
                        </template>
                    </view>
                    <LoadingRound v-if="loading" />
                    <view v-if="isAllData" class="no-more-data">没有更多数据了~</view>
                </view>
            </scroll-view>
        </view>
        <BottomTool btnLabel="新增类目" :btnClick="handleGoCreate" class="bottom-tool-component" />

        <uni-popup class="popup-spu-menu popup-menu-container" ref="spuMenuPopupRef" type="bottom">
            <view class="popup-content">
                <view class="menu-item" @click="handleAddSku">新增规格</view>
                <view class="menu-item" @click="handleEditSpu">编辑</view>
                <view class="menu-item" @click="handleDeleteSpu">删除</view>
                <view class="menu-cancel-box menu-item">
                    <view class="menu-cancel-item" @click="handleCloseSpuMenu">取消</view>
                </view>
            </view>
        </uni-popup>

        <uni-popup class="popup-sku-menu popup-menu-container" ref="skuMenuPopupRef" type="bottom">
            <view class="popup-content">
                <view class="menu-item" @click="handleCreateInventory">录入库存</view>
                <view class="menu-item" @click="handleEditSku">编辑</view>
                <view class="menu-item" @click="handleDeleteSku">删除</view>
                <view class="menu-cancel-box menu-item">
                    <view class="menu-cancel-item" @click="handleCloseSkuMenu">取消</view>
                </view>
            </view>
        </uni-popup>
    </view>
</template>
<script setup lang="ts">
import CommonHeader from "@/components/CommonHeader.vue";
import LoadingRound from "@/components/LoadingRound.vue";
import BottomTool from "@/components/BottomTool.vue";
import HeaderSearch from "@/components/HeaderSearch.vue";
import { getInventoryList, getSkuStockList, deleteWarehouseStock } from "@/api/inventory";
import { deleteCategory, deleteSku } from "@/api/category";
import { ref, onMounted } from "vue";
import { onPullDownRefresh } from "@dcloudio/uni-app";

const pageTitle = ref('库存管理');
const loading = ref(false);
const spuList = ref([]);
const spuTotal = ref(0);
const queryParam = ref({
    pageNo: 1,
    pageSize: 10,
    keyword: ""
});
const popupSpu = ref({});
const popupSku = ref({});

// popup
const spuMenuPopupRef = ref(null);
const skuMenuPopupRef = ref(null);

// 已经是全部数据状态
const isAllData = ref(false);

onMounted(() => {
    uni.setNavigationBarTitle({
        title: pageTitle.value
    });
    loading.value = true;
    initData();
});

onPullDownRefresh(() => {
    initData();
    uni.stopPullDownRefresh();
});

const viewImage = (url) => {
    if (!url) {
        return;
    }
    uni.previewImage({
        urls: [url],
        current: url
    });
}

const initData = () => {
    queryParam.value.pageNo = 1;
    isAllData.value = false;
    getInventoryList(queryParam.value).then((res) => {
        loading.value = false;
        const data = res.data;
        spuTotal.value = data.total;
        // 补充默认的库存列表
        data.list.forEach((item) => {
            item.specList.forEach((sku) => {
                sku.stockList = [];
                sku.stockListLoading = false;
                sku.stockListShow = false;
            });
        });
        spuList.value = data.list;
    });
}

/**
 * 用户搜索
 */
const handleInputSearch = (value: string) => {
    queryParam.value.keyword = value;
    queryParam.value.pageNo = 1;
    getInventoryList(queryParam.value).then((res) => {
        const data = res.data;
        spuTotal.value = data.total;
        // 补充默认的库存列表
        data.list.forEach((item) => {
            item.skus.forEach((sku) => {
                sku.stockList = [];
                sku.stockListLoading = false;
                sku.stockListShow = false;
            });
        });
        spuList.value = data.list;
    });
};

/**
 * 加载sku的库存明细
 */
const handleShowStockList = (sku) => {
    sku.stockListShow = !sku.stockListShow;
    if (sku.stockListShow && sku.stockList.length === 0) {
        sku.stockListLoading = true;
        getSkuStockList({
            specId: sku.id
        }).then((res) => {
            sku.stockListLoading = false;
            sku.stockList = res.data;
        });
    }
};

/**
 * 滑动加载
 */
const handleScrollToLower = () => {
    if (spuList.value.length < spuTotal.value) {
        queryParam.value.pageNo++;
        getInventoryList(queryParam.value).then((res) => {
            const data = res.data;
            spuTotal.value = data.total;
            // 补充默认的库存列表
            data.list.forEach((item) => {
                item.skus.forEach((sku) => {
                    sku.stockList = [];
                    sku.stockListLoading = false;
                    sku.stockListShow = false;
                });
            });
            spuList.value = spuList.value.concat(data.list);
        });
    } else {
        // 已经是全部数据
        isAllData.value = true;
    }
};

/**
 * 跳转到新建类目
 */
const handleGoCreate = () => {
    navigateToAndOnSubmit("/pages/inventory/editSpu/index");
}

const handleStock = (product: any, sku: any, stock: any, type: string) => {
    let url = `/pages/inventory/handleStock/index?type=${type}&warehouseStockId=${stock.id}&skuImage=${sku.picUrl}&spuName=${product.name}&skuName=${sku.name}&warehouseName=${stock.warehouseName}&warehouseShelveName=${stock.warehouseShelveName}&warehouseStorage=${stock.warehouseStorage}&stock=${stock.stock}&unitName=${sku.unitName}`
    navigateToAndOnSubmit(url);
}

const handleDelete = (stock: any) => {
    uni.showModal({
        title: "提示",
        content: "确定删除该库存吗？",
        success: (res) => {
            if (res.confirm) {
                deleteWarehouseStock({ id: stock.id }).then(data => {
                    uni.showToast({
                        title: "删除成功",
                        icon: "none"
                    });
                    initData();
                })
            }
        }
    });
}

const handleSpuMenu = (product: any) => {
    popupSpu.value = product;
    spuMenuPopupRef.value.open();
}

const handleCloseSpuMenu = () => {
    spuMenuPopupRef.value.close();
}

const handleSkuMenu = (product: any, sku: any) => {
    popupSpu.value = product;
    popupSku.value = sku;
    skuMenuPopupRef.value.open();
}

const handleCloseSkuMenu = () => {
    skuMenuPopupRef.value.close();
}

const handleAddSku = () => {
    if (!popupSpu.value) {
        console.error('popupSpu is null!')
        return;
    }
    handleCloseSpuMenu();
    navigateToAndOnSubmit(`/pages/inventory/editSku/index?categoryId=${popupSpu.value.id}&categoryName=${popupSpu.value.name}`);
}

const handleEditSpu = () => {
    handleCloseSpuMenu();
    navigateToAndOnSubmit("/pages/inventory/editSpu/index?id=" + popupSpu.value.id);
}

const handleDeleteSpu = () => {
    /**
     * 删除类目
     */
    uni.showModal({
        title: "提示",
        content: `确定删除${popupSpu.value.name}吗？`,
        success: (res) => {
            if (res.confirm) {
                handleCloseSpuMenu();
                deleteCategory({ id: popupSpu.value.id }).then((res) => {
                    uni.showToast({
                        title: "删除成功！"
                    });
                    initData();
                });

            }
        }
    });
}

/**
 * 编辑规格
 */
const handleEditSku = () => {
    handleCloseSkuMenu();
    const url = `/pages/inventory/editSku/index?id=${popupSku.value.id}&categoryId=${popupSku.value.id}&categoryName=${popupSku.value.name}`;
    navigateToAndOnSubmit(url);
}

/**
 * 删除规格
 */
const handleDeleteSku = () => {
    handleCloseSkuMenu();
    uni.showModal({
        title: "提示",
        content: `确定删除${popupSku.value.name}吗？`,
        success: (res) => {
            if (res.confirm) {
                deleteSku({ id: popupSku.value.id }).then((res) => {
                    initData();
                    uni.showToast({
                        title: "删除成功！"
                    });
                });
            }
        }
    });
}

/**
 * 录入库存
 */
const handleCreateInventory = () => {
    handleCloseSkuMenu();
    let url = `/pages/inventory/createInventory/index?`;
    url += `spuId=${popupSpu.value.id}&spuName=${popupSpu.value.name}`;
    url += `&skuId=${popupSku.value.id}&skuName=${popupSku.value.name}`;
    url += `&skuPicUrl=${popupSku.value.picUrl}&skuUnitName=${popupSku.value.unitName}`;
    navigateToAndOnSubmit(url);
}

const navigateToAndOnSubmit = (url) => {
    uni.navigateTo({
        url: url,
        success: (res) => {
            res.eventChannel.on("submit", () => {
                initData();
            });
        },
    });
}
</script>
<style lang="scss" scoped>
.content {
    .spu-list-box {
        position: relative;
        height: calc(100vh - 300rpx);

        .scroll-spu-list-box {
            padding: 30rpx 30rpx 70rpx 30rpx;
        }

        .no-more-data {
            width: 100%;
            text-align: center;
            padding: 30rpx 0;
            color: #7A7A7A;
            font-size: 28rpx;
        }

        .spu-box {
            background: #FFFFFF;
            box-shadow: 0rpx 0rpx 10rpx 0rpx rgba(202, 202, 202, 0.5);
            border-radius: 20rpx;
            padding: 30rpx;
            margin-bottom: 20rpx;

            .spu-title {
                font-size: 32rpx;
                font-weight: 400;
                color: #000000;
                line-height: 46rpx;
            }

            .sku-box {
                margin-top: 30rpx;
                display: flex;
                align-items: center;

                .icon-box {
                    width: 32rpx;
                    height: 32rpx;
                    padding-right: 10rpx;
                }

                .info-box {
                    // margin-left: 10rpx;
                    flex: 1;
                    display: flex;
                    gap: 30rpx;
                    background: #FAFAFA;
                    align-items: center;
                    padding-right: 30rpx;

                    .sku-img {
                        width: 80rpx;
                        height: 80rpx;
                    }

                    .sku-title {
                        width: 180rpx;
                        overflow: hidden;
                    }

                    .sku-count {
                        flex: 1;
                    }
                }
            }

            .sku-stock-box {
                padding-left: 42rpx;

                .sku-stock-item {
                    margin-top: 20rpx;

                    .sku-stock-item-storage {}

                    .sku-stock-item-count {
                        margin-top: 12rpx;
                        display: flex;
                        align-items: center;

                        .stock-info {}

                        .btn-box {
                            flex: 1;
                            display: flex;
                            gap: 30rpx;
                            justify-content: flex-end;

                            .btn {
                                height: 40rpx;
                                font-size: 28rpx;
                                font-weight: 400;
                                color: #4187FE;
                                line-height: 40rpx;
                            }
                        }
                    }

                    .divider {
                        margin-top: 20rpx;
                        height: 1rpx;
                        border: 1rpx solid #EDEDED;
                    }

                    .label {
                        width: 86rpx;
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
                    }
                }
            }
        }
    }
}

.title-box {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .title {}
}

.more-menu-btn-box {
    height: 100%;
    width: 50rpx;
    text-align: center;
}

.more-menu-btn-icon {
    width: 27rpx;
    height: 6rpx;
}

.popup-menu-container {
    .popup-content {
        background-color: #F6F7F9;
        border-radius: 18rpx 18rpx 0rpx 0rpx;
    }

    .menu-item {
        height: 40rpx;
        background: #FFFFFF;
        font-size: 28rpx;
        font-weight: 400;
        color: #4187FE;
        line-height: 40rpx;
        text-align: center;
        background: #FFFFFF;
        padding: 30rpx 0;
    }

    .menu-cancel-box {
        margin-top: 20rpx;
        color: #000000;
    }
}

.collapse-icon {
    width: 32rpx;
    height: 32rpx;

    &.collapse {
        transform: rotate(270deg);
    }
}
</style>