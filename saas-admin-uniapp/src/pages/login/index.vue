<template>
	<view class="container">
		<view class="content">
			<text class="title">
				{{ title }}
			</text>

			<view class="form-container">
				<view class="form-item">
					<image class="input-prefix-icon" src="/static/img/icons/icon-phone.png" />
					<input class="form-item-input" name="tenantName" placeholder="请输入租户名称" v-model="form.tenantName" />
				</view>
				<view class="form-item">
					<image class="input-prefix-icon" src="/static/img/icons/icon-phone.png" />
					<input class="form-item-input" name="username" placeholder="请输入手机号" v-model="form.username" />
				</view>
				<view class="form-item">
					<image class="input-prefix-icon" src="/static/img/icons/icon-password.png" />
					<input class="form-item-input" v-model="form.password" type="password" placeholder="请输入密码"
						maxlength="16" />
				</view>
				<view @click="handleLogin" class="form-item-btn-box">
					<view class="form-item-btn">
						登录
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { login, getTenantIdByName } from '@/api/login';
import { setToken } from '@/utils/auth';

const title = ref('欢迎使用爱塔可仓库管理系统');
const pageTitle = ref('登录');
const form = ref({
	tenantName: '爱塔可',
	username: 'admin',
	password: 'admin123',
});
const loading = ref(false);

onMounted(() => {
	uni.setNavigationBarTitle({
		title: pageTitle.value,
	});
});

const handleLogin = () => {
	// 检查是否输入了用户名与密码
	if (!form.value.tenantName) {
		uni.showToast({
			title: '请输入租户名',
			icon: 'none'
		});
		return;
	}
	if (!form.value.username) {
		uni.showToast({
			title: '请输入用户名',
			icon: 'none'
		});
		return;
	}
	if (!form.value.password) {
		uni.showToast({
			title: '请输入密码',
			icon: 'none'
		});
		return;
	}
	loading.value = true;
	uni.showLoading({
		title: '登录中...',
		mask: true
	});
	getTenantIdByName(form.value.tenantName).then(data => {
		if (!data.data) {
			uni.showToast({
				title: '租户不存在',
				icon: 'none'
			});
			return;
		}
		// 全局保存tenantId
		uni.setStorageSync('tenantId', data.data);
		// 登录
		login(form.value.tenantName, form.value.username, form.value.password, '').then(data => {
			data = data.data;
			setToken(data);
			// 跳转到首页
			uni.redirectTo({
				url: '/pages/index/index'
			});
			// 设置 token
		}).catch(error => {
			console.error(error)
		}).finally(() => {
			uni.hideLoading();
			loading.value = false;
		});
	});
}

</script>
<style scoped lang="less">
.container {
	background: linear-gradient(180deg, #BCD7FF 0%, rgba(255, 255, 255, 0.5) 100%);
	align-items: center;
	height: 100vh;

	.content {
		padding: 200rpx 55rpx 0 55rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
	}

	.logo {
		width: 238rpx;
		height: 69rpx;
	}

	.title {
		margin-top: 40rpx;
		font-size: 40rpx;
		font-weight: 600;
		color: #000000;
		line-height: 67rpx;
		letter-spacing: 1px;
	}

	.form-container {
		margin-top: 80rpx;
		text-align: center;

		.form-item {
			height: 100rpx;
			background: #EEF5FF;
			border-radius: 58rpx;
			display: flex;
			align-items: center;
			padding-left: 50rpx;
			margin-bottom: 20rpx;

			.input-prefix-icon {
				width: 30rpx;
				height: 30rpx;
				margin-right: 20rpx;
			}

			.form-item-input {
				text-align: left;
			}
		}

		.form-item-btn-box {
			margin-top: 60rpx;
			width: 640rpx;
			height: 100rpx;
			background: #4187FE;
			border-radius: 58rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 36rpx;
			font-weight: 600;
			color: #FFFFFF;
			line-height: 50rpx;
		}
	}
}
</style>