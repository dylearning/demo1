package com.dream.sos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.waps.AppConnect;
import cn.waps.AppListener;
import cn.waps.extend.QuitPopAd;
import cn.waps.extend.SlideWall;

import com.dream.sos.widget.TopBar;
import com.dream.sos.widget.TopBar.onTopBarListener;

public class SOSMainActivity extends Activity {
	
	//dream begin
	private final static String TAG = "dream";
    private Context mContext;
    //dream end
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos_main);
		
		initView();
		
		
        /*****************************dream begin***************************************************************/
		mContext = this;
		
		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		AppConnect.getInstance("0ec8f28ba94639939e442f01b430f4f8", "baidu", this);		
		
		// 带有默认参数值的在线配置，使用此方法，程序第一次启动使用的是"defaultValue"，之后再启动则是使用的服务器端返回的参数值
		String showAd = AppConnect.getInstance(this).getConfig("showAd", "false");
        
		Log.i(TAG, "showAd="+showAd);
		
		if(showAd.equals("true")){
			//调用以下接口进行卸载广告的初始化
			AppConnect.getInstance(this). initUninstallAd(this);
			
			// 预加载插屏广告内容（仅在使用到插屏广告的情况，才需要添加）
			AppConnect.getInstance(this).initPopAd(this);
	
			// 设置插屏广告无数据时的回调监听（该方法必须在showPopAd之前调用）
			AppConnect.getInstance(this).setPopAdNoDataListener(new AppListener() {
	
				@Override
				public void onPopNoData() {
					Log.i(TAG, "插屏广告暂无可用数据");
				}
			});
			
			String showPopAd = AppConnect.getInstance(this).getConfig("showPopAd", "false");
			
			String showBannerAd = AppConnect.getInstance(this).getConfig("showBannerAd", "false");
			
			String showMiniAd = AppConnect.getInstance(this).getConfig("showMiniAd", "false");
			
			Log.i(TAG, "showPopAd="+showPopAd+",showBannerAd="+showBannerAd+",showMiniAd="+showMiniAd);
			
			// 显示插屏广告
			if(showPopAd.equals("true")){
				AppConnect.getInstance(this).showPopAd(this);
			}
			
			// 互动广告
			if(showBannerAd.equals("true")){
				setupBannerAd();
			}
			
			//迷你广告
			if(showMiniAd.equals("true")){
				setupMiniAd();
			}	
		}
		/*****************************dream end***************************************************************/
	}

	// dream add 
    // 设置互动广告
	private void setupBannerAd() {
		
		// 设置互动广告无数据时的回调监听（该方法必须在showBannerAd之前调用）
		AppConnect.getInstance(this).setBannerAdNoDataListener(new AppListener() {

			@Override
			public void onBannerNoData() {
				Log.i(TAG, "banner广告暂无可用数据");
			}

		});
		
		
		LinearLayout layout = new LinearLayout(this);
		
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		AppConnect.getInstance(this).showBannerAd(this, layout);
		((Activity) mContext).addContentView(layout, layoutParams);  
		  
		/*LinearLayout adlayout = new LinearLayout(this);
		adlayout.setGravity(Gravity.CENTER_HORIZONTAL);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		AppConnect.getInstance(this).showBannerAd(this, adlayout);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//设置顶端或低端
		this.addContentView(adlayout, layoutParams);*/
	}    
    
	// dream add 
	private void setupMiniAd() {
		LinearLayout miniLayout = new LinearLayout(this);
		
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;

		//设置迷你广告背景颜色
		AppConnect.getInstance(this).setAdBackColor(Color.BLUE);
		//设置迷你广告广告诧颜色
		AppConnect.getInstance(this).setAdForeColor(Color.RED);
		
		AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10);// 10秒刷新一次
		
		AppConnect.getInstance(this).showBannerAd(this, miniLayout);
		((Activity) mContext).addContentView(miniLayout, layoutParams);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 实例化监听
	private onTopBarListener mTopBarListener = new onTopBarListener(){
	    public void onLeftClick(View v){
	    	finish();
	    }
	    
	    public void onRightClick(View v){
	    }   
	    
	    public void onTitleClick(View v){
	    }
	};
	
	private void initView() {
		
		TopBar mTopBar = (TopBar)findViewById(R.id.topbar);
		// 设置监听
		mTopBar.setOnTopBarListener(mTopBarListener);
		
		/*ImageButton mBtnCancel = (ImageButton) findViewById(R.id.cancel);

		mBtnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});*/
		
		RelativeLayout location_layout = (RelativeLayout) findViewById(R.id.location_layout);
		
		location_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SOSMainActivity.this, LocationOverlayDemo.class);//SOSMap LocationOverlayDemo
				startActivity(i);
			}
		});		
		
		RelativeLayout sos_layout = (RelativeLayout) findViewById(R.id.sos_layout);
		
		sos_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SOSMainActivity.this, SOSShowActivity.class);
				startActivity(i);
			}
		});			
		
		Button sos_setting = (Button) findViewById(R.id.sos_setting);
		
		sos_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SOSMainActivity.this, SOSSetting.class);
				startActivity(i);				
			}
		});			
		
		Button personal = (Button) findViewById(R.id.personal);
		
		personal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SOSMainActivity.this, SOSPersonalEdit.class);
				startActivity(i);					
			}
		});			
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// dream add begin
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (SlideWall.getInstance().slideWallDrawer != null && SlideWall.getInstance().slideWallDrawer.isOpened()) {
				// 如果抽屉式应用墙展示中，则关闭抽屉
				SlideWall.getInstance().closeSlidingDrawer();
				
				return true;
			} else {
				// 调用退屏广告
				
				String showQuitPopAd = AppConnect.getInstance(this).getConfig("showQuitPopAd", "false");
				
				Log.i(TAG, "showQuitPopAd="+showQuitPopAd);
				
				// 显示插屏广告
				if(showQuitPopAd.equals("true")){
					QuitPopAd.getInstance().show(this);
					
					return true;
				}
			}
		}
       
		return super.onKeyDown(keyCode, event);
		// dream add end
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		Log.i(TAG, "onDestroy");				
		
		// 释放资源，原finalize()方法名修改为close()
		AppConnect.getInstance(this).close();// dream add 
		
		super.onDestroy();
	}
}
