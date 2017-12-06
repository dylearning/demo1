package com.android.dream;

import com.android.dream.R;

import net.youmi.android.AdManager;
import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private static final String TAG = "dengying";

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate");
		
		setContentView(R.layout.activity_test);

		mContext = this;
		
		// 设置广告条
		setupBannerAd();

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
}
