package com.youmi.android.addemo;

import com.dream.game.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.youmi.android.AdManager;
import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;
import net.youmi.android.normal.video.VideoAdListener;
import net.youmi.android.normal.video.VideoAdManager;
import net.youmi.android.normal.video.VideoAdSettings;
import net.youmi.android.normal.video.VideoInfoViewBuilder;

/**
 * <p>主窗口</p>
 * Edited by Alian Lee on 2016-06-16.
 */
public class MainActivity extends Activity {

	private static final String TAG = "youmi-demo";

	private Context mContext;

	/**
	 * 原生控件容器
	 */
	private RelativeLayout mNativeAdLayout;

	/**
	 * 视频信息栏容器
	 */
	private RelativeLayout mVideoInfoLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		
		//AdManager.getInstance(mContext).init("bf079c4df33f1e81", "99ced8bafa56533c", false, true);//dengying@20161123
		
		// 设置应用版本信息
		setupAppVersionInfo();
		// 设置插屏广告
		setupSpotAd();
		setupSlideableSpotAd();
		setupNativeSpotAd();
		// 设置视频广告
		setupVideoAd();
		setupNativeVideoAd();
		// 检查广告配置
		checkAdSettings();
		//设置广告条
		setupBannerAd();
	}

	/**
	 * 设置版本号信息
	 */
	private void setupAppVersionInfo() {
		TextView textVersionInfo = (TextView) findViewById(R.id.tv_version_info);
		if (textVersionInfo != null) {
			textVersionInfo.append(getAppVersionName());
		}
	}

	/**
	 * 设置插屏广告
	 */
	private void setupSpotAd() {
		// 设置插屏图片类型，默认竖图
		//		// 横图
		//		SpotManager.getInstance(mContext).setImageType(SpotManager
		// .IMAGE_TYPE_HORIZONTAL);
		// 竖图
		SpotManager.getInstance(mContext).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

		// 设置动画类型，默认高级动画
		//		// 无动画
		//		SpotManager.getInstance(mContext).setAnimationType(SpotManager
		// .ANIMATION_TYPE_NONE);
		//		// 简单动画
		//		SpotManager.getInstance(mContext).setAnimationType(SpotManager
		// .ANIMATION_TYPE_SIMPLE);
		// 高级动画
		SpotManager.getInstance(mContext)
				.setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

		Button btnShowSpot = (Button) findViewById(R.id.btn_show_spot);
		if (btnShowSpot.getVisibility() == View.GONE) {
			btnShowSpot.setVisibility(View.VISIBLE);
		}
		btnShowSpot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		});
	}

	/**
	 * 设置轮播插屏广告
	 */
	private void setupSlideableSpotAd() {
		// 设置插屏图片类型，默认竖图
		//		// 横图
		//		SpotManager.getInstance(mContext).setImageType(SpotManager
		// .IMAGE_TYPE_HORIZONTAL);
		// 竖图
		SpotManager.getInstance(mContext).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

		// 设置动画类型，默认高级动画
		//		// 无动画
		//		SpotManager.getInstance(mContext).setAnimationType(SpotManager
		// .ANIMATION_TYPE_NONE);
		//		// 简单动画
		//		SpotManager.getInstance(mContext).setAnimationType(SpotManager
		// .ANIMATION_TYPE_SIMPLE);
		// 高级动画
		SpotManager.getInstance(mContext)
				.setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

		Button btnShowSlideableSpot = (Button) findViewById(R.id.btn_show_slideable_spot);
		if (btnShowSlideableSpot.getVisibility() == View.GONE) {
			btnShowSlideableSpot.setVisibility(View.VISIBLE);
		}
		btnShowSlideableSpot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 展示轮播插屏广告
				SpotManager.getInstance(mContext)
						.showSlideableSpot(mContext, new SpotListener() {

							@Override
							public void onShowSuccess() {
								Log.d(TAG, "轮播插屏展示成功");
							}

							@Override
							public void onShowFailed(int errorCode) {
								Log.d(TAG, "轮播插屏展示失败");
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
								Log.d(TAG, "轮播插屏被关闭");
							}

							@Override
							public void onSpotClicked(boolean isWebPage) {
								Log.d(TAG, "轮播插屏被点击");
								Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
							}
						});
			}
		});
	}

	/**
	 * 设置原生插屏广告
	 */
	public void setupNativeSpotAd() {
		if (mNativeAdLayout == null) {
			mNativeAdLayout = (RelativeLayout) findViewById(R.id.rl_native_ad);
		}

		// 设置插屏图片类型，默认竖图
		//		// 横图
		//		SpotManager.getInstance(mContext).setImageType(SpotManager
		// .IMAGE_TYPE_HORIZONTAL);
		// 竖图
		SpotManager.getInstance(mContext).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

		// 设置动画类型，默认高级动画
		//		// 无动画
		//		SpotManager.getInstance(mContext).setAnimationType(SpotManager
		// .ANIMATION_TYPE_NONE);
		//		// 简单动画
		//		SpotManager.getInstance(mContext).setAnimationType(SpotManager
		// .ANIMATION_TYPE_SIMPLE);
		// 高级动画
		SpotManager.getInstance(mContext)
				.setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

		Button btnShowNativeSpot = (Button) findViewById(R.id.btn_show_native_spot);
		if (btnShowNativeSpot.getVisibility() == View.GONE) {
			btnShowNativeSpot.setVisibility(View.VISIBLE);
		}
		btnShowNativeSpot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取原生插屏控件
				View nativeSpotView = SpotManager.getInstance(mContext)
						.getNativeSpot(mContext, new SpotListener() {

							@Override
							public void onShowSuccess() {
								Log.d(TAG, "原生插屏展示成功");
							}

							@Override
							public void onShowFailed(int errorCode) {
								Log.d(TAG, "原生插屏展示失败");
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
								Log.d(TAG, "原生插屏被关闭");
							}

							@Override
							public void onSpotClicked(boolean isWebPage) {
								Log.d(TAG, "原生插屏被点击");
								Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
							}
						});
				if (nativeSpotView != null) {
					RelativeLayout.LayoutParams layoutParams =
							new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
					if (mNativeAdLayout != null) {
						mNativeAdLayout.removeAllViews();
						// 添加原生插屏控件到容器中
						mNativeAdLayout.addView(nativeSpotView, layoutParams);
						if (mNativeAdLayout.getVisibility() != View.VISIBLE) {
							mNativeAdLayout.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		});
	}

	/**
	 * 设置视频广告
	 */
	private void setupVideoAd() {
		// 设置视频广告
		final VideoAdSettings videoAdSettings = new VideoAdSettings();
		videoAdSettings.setInterruptTips("退出视频播放将无法获得奖励" + "\n确定要退出吗？");
		//		// 设置是否隐藏关闭按钮
		//		videoAdSettings.setHideCloseButton(true);

		// 设置服务器回调 userId，一定要在请求广告之前设置，否则无效
		VideoAdManager.getInstance(mContext).setUserId("userId");

		// 请求视频广告
		VideoAdManager.getInstance(mContext).requestVideoAd(mContext);

		Button btnShowVideo = (Button) findViewById(R.id.btn_show_video);
		if (btnShowVideo.getVisibility() == View.GONE) {
			btnShowVideo.setVisibility(View.VISIBLE);
		}

		btnShowVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 展示视频广告
				VideoAdManager.getInstance(mContext)
						.showVideoAd(mContext, videoAdSettings, new VideoAdListener() {
							@Override
							public void onPlayStarted() {
								Log.i(TAG, "开始播放视频");
							}

							@Override
							public void onPlayInterrupted() {
								Log.i(TAG, "播放视频被中断");
								Toast.makeText(mContext, "播放视频被中断", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onPlayFailed(int errorCode) {
								switch (errorCode) {
								case ErrorCode.NON_NETWORK:
									Log.e(TAG, "网络异常");
									Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
									break;
								case ErrorCode.NON_AD:
									Log.e(TAG, "暂无广告");
									Toast.makeText(mContext, "暂无广告", Toast.LENGTH_SHORT).show();
									break;
								case ErrorCode.RESOURCE_NOT_READY:
									Log.e(TAG, "资源还没准备好");
									break;
								case ErrorCode.SHOW_INTERVAL_LIMITED:
									Log.e(TAG, "展示间隔限制");
									break;
								case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
									Log.e(TAG, "控件处在不可见状态");
									break;
								}
							}

							@Override
							public void onPlayCompleted() {
								Log.i(TAG, "视频播放成功");
								Toast.makeText(mContext, "视频播放成功", Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
	}

	/**
	 * 设置原生视频广告
	 */
	private void setupNativeVideoAd() {
		// 设置视频广告
		final VideoAdSettings videoAdSettings = new VideoAdSettings();
		//		// 设置是否隐藏关闭按钮
		//		videoAdSettings.setHideCloseButton(true);

		// 一定要在请求广告之前设置服务器回调 userId
		VideoAdManager.getInstance(mContext).setUserId("userId");

		//		// 只需要调用一次，由于setupVideoAd()方法中已经调用了一次，所以此处无需调用
		//		VideoAdManager.getInstance().requestVideoAd(mContext);

		if (mVideoInfoLayout == null) {
			mVideoInfoLayout = (RelativeLayout) findViewById(R.id.rl_video_info);
		}

		// 设置信息流视图，将图标，标题，描述，下载按钮对应的ID传入
		final VideoInfoViewBuilder videoInfoViewBuilder =
				VideoAdManager.getInstance(mContext).getVideoInfoViewBuilder(mContext)
						.setRootContainer(mVideoInfoLayout).bindAppIconView(R.id.info_iv_icon)
						.bindAppNameView(R.id.info_tv_title).bindAppDescriptionView(R.id.info_tv_description)
						.bindDownloadButton(R.id.info_btn_download);

		// 实例化添加控件的容器
		if (mNativeAdLayout == null) {
			mNativeAdLayout = (RelativeLayout) findViewById(R.id.rl_native_ad);
		}
		Button btnShowNativeVideo = (Button) findViewById(R.id.btn_show_native_video);
		if (btnShowNativeVideo.getVisibility() == View.GONE) {
			btnShowNativeVideo.setVisibility(View.VISIBLE);
		}
		btnShowNativeVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取原生视频控件
				View nativeVideoAdView = VideoAdManager.getInstance(mContext)
						.getNativeVideoAdView(mContext, videoAdSettings, new VideoAdListener() {
							@Override
							public void onPlayStarted() {
								Log.d(TAG, "开始播放视频");
								// 显示信息流视图
								if (mVideoInfoLayout != null && mVideoInfoLayout.getVisibility() != View.VISIBLE) {
									mVideoInfoLayout.setVisibility(View.VISIBLE);
								}
							}

							@Override
							public void onPlayInterrupted() {
								Log.d(TAG, "播放视频被中断");
								Toast.makeText(mContext, "播放视频被中断", Toast.LENGTH_SHORT).show();
								// 隐藏信息流视图
								if (mVideoInfoLayout != null && mVideoInfoLayout.getVisibility() != View.GONE) {
									mVideoInfoLayout.setVisibility(View.GONE);
									// 释放资源
									videoInfoViewBuilder.release();
								}
								// 移除原生视频控件
								if (mNativeAdLayout != null) {
									mNativeAdLayout.removeAllViews();
									mNativeAdLayout.setVisibility(View.GONE);
								}
							}

							@Override
							public void onPlayFailed(int errorCode) {
								switch (errorCode) {
								case ErrorCode.NON_NETWORK:
									Log.e(TAG, "网络异常");
									Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
									break;
								case ErrorCode.NON_AD:
									Log.e(TAG, "暂无广告");
									Toast.makeText(mContext, "暂无广告", Toast.LENGTH_SHORT).show();
									break;
								case ErrorCode.RESOURCE_NOT_READY:
									Log.e(TAG, "资源还没准备好");
									break;
								case ErrorCode.SHOW_INTERVAL_LIMITED:
									Log.e(TAG, "展示间隔限制");
									break;
								case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
									Log.e(TAG, "控件处在不可见状态");
									break;
								}
							}

							@Override
							public void onPlayCompleted() {
								Log.i(TAG, "视频播放成功");
								Toast.makeText(mContext, "视频播放成功", Toast.LENGTH_SHORT).show();
								// 隐藏信息流视图
								if (mVideoInfoLayout != null && mVideoInfoLayout.getVisibility() != View.GONE) {
									mVideoInfoLayout.setVisibility(View.GONE);
									// 释放资源
									videoInfoViewBuilder.release();
								}
								// 移除原生视频控件
								if (mNativeAdLayout != null) {
									mNativeAdLayout.removeAllViews();
									mNativeAdLayout.setVisibility(View.GONE);
								}
							}

						});
				if (mNativeAdLayout != null) {
					final RelativeLayout.LayoutParams params =
							new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);
					if (nativeVideoAdView != null) {
						mNativeAdLayout.removeAllViews();
						// 添加原生视频广告
						mNativeAdLayout.addView(nativeVideoAdView, params);
						mNativeAdLayout.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	/**
	 * 检查广告配置
	 */
	private void checkAdSettings() {
		Button btnCheckAdSettings = (Button) findViewById(R.id.btn_check_ad_settings);
		if (btnCheckAdSettings.getVisibility() == View.GONE) {
			btnCheckAdSettings.setVisibility(View.VISIBLE);
		}
		btnCheckAdSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean result = VideoAdManager.getInstance(mContext).checkVideoAdConfig();
				Toast.makeText(mContext, String.format("配置 %s", result ? "正确" : "不正确"), Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 设置广告条广告
	 */
	private void setupBannerAd() {
		//		/**
		//		 * 普通布局
		//		 */
		//		View bannerView =
		//				BannerManager.getInstance(mContext).getBannerView(new
		// BannerViewListener
		//						() {
		//					@Override
		//					public void onRequestSuccess() {
		//						Log.d(TAG, "请求广告条成功");
		//					}
		//
		//					@Override
		//					public void onSwitchBanner() {
		//						Log.d(TAG, "广告条切换");
		//					}
		//
		//					@Override
		//					public void onRequestFailed() {
		//						Log.d(TAG, "请求广告条失败");
		//					}
		//				});
		//		LinearLayout bannerLayout = (LinearLayout) findViewById(R.id.ll_banner);
		//		bannerLayout.addView(bannerView);

		/**
		 * 悬浮布局
		 */
		// 实例化LayoutParams
		FrameLayout.LayoutParams layoutParams =
				new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置，这里示例为右下角
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		// 获取广告条控件
		final View bannerView =
				BannerManager.getInstance(mContext).getBannerView(new BannerViewListener
						() {

					@Override
					public void onRequestSuccess() {
						Log.i(TAG, "请求广告条成功");
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

	@Override
	public void onBackPressed() {
		//原生控件点击后退关闭
		if (mNativeAdLayout != null && mNativeAdLayout.getVisibility() != View.GONE) {
			mNativeAdLayout.removeAllViews();
			mNativeAdLayout.setVisibility(View.GONE);
			//隐藏信息流视图
			if (mVideoInfoLayout != null && mVideoInfoLayout.getVisibility() != View.GONE) {
				mVideoInfoLayout.setVisibility(View.GONE);
			}
			return;
		}
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
		VideoAdManager.getInstance(mContext).onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 插播广告
		SpotManager.getInstance(mContext).onPause();
		// 视频广告
		VideoAdManager.getInstance(mContext).onPause();
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
		VideoAdManager.getInstance(mContext).onDestroy();
	}

	/**
	 * 获取应用版本号
	 *
	 * @return 应用的当前版本号
	 */
	private String getAppVersionName() {
		try {
			PackageManager packageManager = getPackageManager();
			return packageManager.getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}
}