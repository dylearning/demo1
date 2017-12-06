package com.dream.flashlight;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.waps.AppConnect;
import cn.waps.AppListener;
import cn.waps.extend.QuitPopAd;
import cn.waps.extend.SlideWall;

import com.dream.flashlight.FlashLightSwitch.OnSwitchListener;

public class FlashLight extends Activity {
	
	//dream begin
	private final static String TAG = "dream";
    private Context mContext;
    //dream end
    
	private ImageView imageView;

	private Camera camera = null;
	private Parameters parameters = null;
	public static boolean flashligth_opened = true;

	private Bitmap onBitmapBackground = null;
	private Bitmap offBitmapBackground = null;

	private AudioTrack at;
	private AudioManager am;
	private MediaPlayer mMediaPlayer;
	private Button switch_Btn;
	private FlashLightSwitch slipswitch_MSL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		camera = Camera.open();

		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		setContentView(R.layout.activity_flash_light);

		onBitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.flashlight_bg_on);
		offBitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.flashlight_bg_off);

		mMediaPlayer = MediaPlayer.create(this, R.raw.flashlight);
		mMediaPlayer.setLooping(false);

		imageView = (ImageView) findViewById(R.id.imageview);
		imageView.setImageBitmap(onBitmapBackground);

		slipswitch_MSL = (FlashLightSwitch) findViewById(R.id.flashlightswitch);
		slipswitch_MSL.setImageResource(R.drawable.switch_bg, R.drawable.switch_bg, R.drawable.slip);

		if (null != camera) {
			parameters = camera.getParameters();
			parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameters);
		}

		slipswitch_MSL.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub

				try {
					if (isSwitchOn) {
						imageView.setImageBitmap(onBitmapBackground);
						if (null != camera) {
							parameters = camera.getParameters();
							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
							camera.setParameters(parameters);
						}
						flashligth_opened = true;
					} else {
						imageView.setImageBitmap(offBitmapBackground);
						if (null != camera) {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
							camera.setParameters(parameters);
							flashligth_opened = false;
						}
					}
				} catch (Exception e) {
					imageView.setImageBitmap(offBitmapBackground);
					flashligth_opened = false;
					e.printStackTrace();
				}
				mMediaPlayer.seekTo(0);
				mMediaPlayer.start();

			}
		});

		slipswitch_MSL.setSwitchState(true);

        /*****************************dream begin***************************************************************/
		mContext = this;
		
		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		AppConnect.getInstance("a52950f64e23038630fbe0a38262cbe0", "baidu", this);		
		
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
	
	public void playAudio() {
		if (am.isSpeakerphoneOn()) {
			am.setSpeakerphoneOn(false);
		}
		setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
		am.setMode(AudioManager.MODE_IN_CALL);
		int bufferSizeInBytes = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		if (at == null) {
			at = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);
			new AudioTrackThread().start();
		} else {
			if (at.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
			} else {
				at = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);
				new AudioTrackThread().start();
			}
		}
	}



	@Override
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
	protected void onStart() {
		Log.i(TAG, "onStart");
		
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "onRestart");		
		
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");		
		
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");			
		
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();

		Log.i(TAG, "onStop");			
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		Log.i(TAG, "onDestroy");				
		
		// 释放资源，原finalize()方法名修改为close()
		AppConnect.getInstance(this).close();// dream add 
		
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
		mMediaPlayer.release();
		
		Myback();
		
		super.onDestroy();
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}
  
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		try {
			super.onConfigurationChanged(newConfig);
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			}
		} catch (Exception ex) {
		}
		
		// dream add 
		// 横竖屏状态切换时,关闭处于打开状态中的退屏广告
		//QuitPopAd.getInstance().close();
	}

	public void Myback() {

		if (!flashligth_opened) {
			FlashLight.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		} else if (flashligth_opened) {
			camera.release();
			FlashLight.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			flashligth_opened = false;
		}

	}

	class AudioTrackThread extends Thread {

		@Override
		public void run() {
			byte[] out_bytes = new byte[44100];
			InputStream is = getResources().openRawResource(R.raw.flashlight);
			int length;
			at.play();
			try {
				while ((length = is.read(out_bytes)) != -1) {
					System.out.println(length);
					at.write(out_bytes, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (at.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				at.stop();
				at.release();
				am.setMode(AudioManager.MODE_NORMAL);
			}
		}
	}
}
