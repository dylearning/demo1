package com.android.dream;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dream.game.R;

public class MainActivity extends Activity {

	private static final String TAG = "dream";

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate");

		setContentView(R.layout.activity_test);

		mContext = this;

		// 设置广告条
		setupBannerAd();

		// 设置插屏广告,这里设置，没有效果；等BannerAd播放完成，再设置，是有效果的
		//setupSpotAd();

	}

	/**
	 * 设置广告条广告
	 */
	private void setupBannerAd() {
	
		Log.d(TAG, "setupBannerAd");
		
		/**
		 * 悬浮布局
		 */
		// 实例化LayoutParams
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置，这里示例为右下角
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		// 获取广告条控件
		final View bannerView = BannerManager.getInstance(mContext).getBannerView(new BannerViewListener() {

			@Override
			public void onRequestSuccess() {
				Log.i(TAG, "请求广告条成功");
				
				// 设置插屏广告
				setupSpotAd();
			}

			@Override
			public void onSwitchBanner() {
				Log.i(TAG, "广告条切换");
			}

			@Override
			public void onRequestFailed() {
				Log.i(TAG, "请求广告条失败");
			}
		});
		((Activity) mContext).addContentView(bannerView, layoutParams);
	}

	/**
	 * 设置插屏广告
	 */
	private void setupSpotAd() {

		Log.d(TAG, "setupSpotAd");
		
		// 竖图
		SpotManager.getInstance(mContext).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);
		
		// 高级动画
		SpotManager.getInstance(mContext).setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

		// 展示插屏广告
		SpotManager.getInstance(mContext).showSpot(mContext, new SpotListener() {

			@Override
			public void onShowSuccess() {
				Log.d(TAG, "插屏展示成功");
			}

			@Override
			public void onShowFailed(int errorCode) {
				Log.d(TAG, "插屏展示失败");
				switch (errorCode) {
				case ErrorCode.NON_NETWORK:
					Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.NON_AD:
					Toast.makeText(mContext, "暂无广告", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.RESOURCE_NOT_READY:
					Log.e(TAG, "资源还没准备好");
					Toast.makeText(mContext, "请稍后再试", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.SHOW_INTERVAL_LIMITED:
					Log.e(TAG, "展示间隔限制");
					Toast.makeText(mContext, "请勿频繁展示", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
					Log.e(TAG, "控件处在不可见状态");
					Toast.makeText(mContext, "请设置插屏为可见状态", Toast.LENGTH_SHORT).show();
					break;
				}
			}

			@Override
			public void onSpotClosed() {
				Log.d(TAG, "插屏被关闭");
			}

			@Override
			public void onSpotClicked(boolean isWebPage) {
				Log.d(TAG, "插屏被点击");
				Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
			}
		});
	}

	@Override
	public void onBackPressed() {
		//原生控件点击后退关闭
		/*if (mNativeAdLayout != null && mNativeAdLayout.getVisibility() != View.GONE) {
			mNativeAdLayout.removeAllViews();
			mNativeAdLayout.setVisibility(View.GONE);
			//隐藏信息流视图
			if (mVideoInfoLayout != null && mVideoInfoLayout.getVisibility() != View.GONE) {
				mVideoInfoLayout.setVisibility(View.GONE);
			}
			return;
		}*/
		// 点击后退关闭插播广告
		// 当只嵌入插屏或轮播插屏其中一种广告时，不需要外层if判断
		if (SpotManager.getInstance(mContext).isSpotShowing() ||
		    SpotManager.getInstance(mContext).isSlideableSpotShowing()) {
			if (SpotManager.getInstance(mContext).isSpotShowing()) {
				SpotManager.getInstance(mContext).hideSpot();
			}
			if (SpotManager.getInstance(mContext).isSlideableSpotShowing()) {
				SpotManager.getInstance(mContext).hideSlideableSpot();
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 视频广告
		//VideoAdManager.getInstance(mContext).onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 插播广告
		SpotManager.getInstance(mContext).onPause();
		// 视频广告
		//VideoAdManager.getInstance(mContext).onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 插播广告
		SpotManager.getInstance(mContext).onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 插播广告
		SpotManager.getInstance(mContext).onDestroy();
		// 视频广告
		//VideoAdManager.getInstance(mContext).onDestroy();
	}
}
