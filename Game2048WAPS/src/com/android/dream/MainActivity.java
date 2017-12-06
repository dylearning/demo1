package com.android.dream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import cn.waps.AppConnect;
import cn.waps.AppListener;
import com.dream.game2048.R;
import cn.waps.extend.QuitPopAd;
import cn.waps.extend.SlideWall;

//dengying add
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		AppConnect.getInstance("09f277ca386ee99cb4c910e09f562112", "baidu", this);		
		
		// 预加载插屏广告内容（仅在使用到插屏广告的情况，才需要添加）
		AppConnect.getInstance(this).initPopAd(this);

		// 设置插屏广告无数据时的回调监听（该方法必须在showPopAd之前调用）
		AppConnect.getInstance(this).setPopAdNoDataListener(new AppListener() {

			@Override
			public void onPopNoData() {
				Log.i("debug", "插屏广告暂无可用数据");
			}
		});
		// 显示插屏广告
		AppConnect.getInstance(this).showPopAd(this);
		
		// 设置互动广告无数据时的回调监听（该方法必须在showBannerAd之前调用）
		AppConnect.getInstance(this).setBannerAdNoDataListener(new AppListener() {

			@Override
			public void onBannerNoData() {
				Log.i("debug", "banner广告暂无可用数据");
			}

		});
		// 互动广告调用方式
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.AdLinearLayout);
		AppConnect.getInstance(this).showBannerAd(this, layout);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (SlideWall.getInstance().slideWallDrawer != null && SlideWall.getInstance().slideWallDrawer.isOpened()) {
				// 如果抽屉式应用墙展示中，则关闭抽屉
				SlideWall.getInstance().closeSlidingDrawer();
			} else {
				// 调用退屏广告
				QuitPopAd.getInstance().show(this);
			}

		}
		return true;
	}
}
