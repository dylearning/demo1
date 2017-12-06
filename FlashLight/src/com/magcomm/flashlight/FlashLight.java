package com.magcomm.flashlight;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.view.KeyEvent;
import android.util.Log;
import android.content.res.Configuration;

import com.magcomm.flashlight.FlashLightSwitch.OnSwitchListener;
import android.media.MediaPlayer;//hejianfeng add 

public class FlashLight extends Activity {
	private ImageView imageView;
	//private ImageView toggleButton;
	private Camera camera = null;
	private Parameters parameters = null;
	public static boolean flashligth_opened = true;//dengying@20140504 default open

	private Bitmap onBitmapBackground = null;
	private Bitmap offBitmapBackground = null;

	//private Bitmap onBitmap = null;
	//private Bitmap offBitmap = null;

	private AudioTrack at;
	private AudioManager am;
	private MediaPlayer mMediaPlayer;//hejianfeng add 
	private Button switch_Btn;
	private FlashLightSwitch slipswitch_MSL;
	
	//private boolean isOnSaveInstanceState = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v("dengying", "FlashLight onCreate");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);*/

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
        //WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
	    //WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		camera = Camera.open();

		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		setContentView(R.layout.activity_flash_light);

		onBitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.flashlight_bg_on);
		offBitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.flashlight_bg_off);

		//onBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_bg_poweron);
		//offBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_bg_poweroff);
		//hejianfeng add start 
		mMediaPlayer=MediaPlayer.create(this, R.raw.flashlight);
		mMediaPlayer.setLooping(false);
		//hejianfeng add end 
		imageView = (ImageView) findViewById(R.id.imageview);
		imageView.setImageBitmap(onBitmapBackground);//dengying@20140504 default open

		//toggleButton = (ImageView) findViewById(R.id.bt_light);
		//toggleButton.setImageBitmap(onBitmap);//dengying@20140504 default open
		//toggleButton.setOnClickListener(listener);
		// imageView.setOnClickListener(listener);

		slipswitch_MSL = (FlashLightSwitch) findViewById(R.id.flashlightswitch);
		slipswitch_MSL.setImageResource(R.drawable.switch_bg, R.drawable.switch_bg, R.drawable.slip);
		
		//dengying@20140504 default open
		if (null != camera) {
			parameters = camera.getParameters();
			parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameters);
		}
		//dengying@20140504 default open
		
		slipswitch_MSL.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub

				try {
					if (isSwitchOn) {
						imageView.setImageBitmap(onBitmapBackground);
						// toggleButton.setImageBitmap(onBitmap);
						// camera = Camera.open();
						if (null != camera) {
							parameters = camera.getParameters();
							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
							camera.setParameters(parameters);
						}
						flashligth_opened = true;
					} else {
						imageView.setImageBitmap(offBitmapBackground);
						//toggleButton.setImageBitmap(offBitmap);
						if (null != camera) {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
							camera.setParameters(parameters);
							flashligth_opened = false;
							// camera.release();
						}
					}
				} catch (Exception e) {
					imageView.setImageBitmap(offBitmapBackground);
					//toggleButton.setImageBitmap(offBitmap);
					flashligth_opened = false;
					e.printStackTrace();
				}
				mMediaPlayer.seekTo(0);//hejianfeng add 
				mMediaPlayer.start();//hejianfeng add 
				//playAudio();//hejianfeng delete 
			}
		});
		
		slipswitch_MSL.setSwitchState(true);//dengying@20140504 default open
		
		/*
		 * switch_Btn = (Button)findViewById(R.id.main_button_switch);
		 * switch_Btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * slipswitch_MSL.updateSwitchState(!slipswitch_MSL.getSwitchState());
		 * 
		 * if(slipswitch_MSL.getSwitchState()) {
		 * Toast.makeText(FlashLightActivity.this, "开关已经开启", 300).show(); } else
		 * { Toast.makeText(FlashLightActivity.this, "开关已经关闭", 300).show(); } }
		 * });
		 */
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

	/*private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				if (!flashligth_opened) {
					imageView.setImageBitmap(onBitmapBackground);
					toggleButton.setImageBitmap(onBitmap);
					// camera = Camera.open();
					if (null != camera) {
						parameters = camera.getParameters();
						parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						camera.setParameters(parameters);
					}
					flashligth_opened = true;
				} else {
					imageView.setImageBitmap(offBitmapBackground);
					toggleButton.setImageBitmap(offBitmap);
					if (null != camera) {
						parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(parameters);
						flashligth_opened = false;
						// camera.release();
					}
				}
				playAudio();
			} catch (Exception e) {
				imageView.setImageBitmap(offBitmap);
				toggleButton.setImageBitmap(offBitmap);
				flashligth_opened = false;
				e.printStackTrace();
			}
		}
	};*/
	
	 @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
		Log.v("dengying", "FlashLight onKeyDown");
		 
		return super.onKeyDown(keyCode, event); 
		 
		/*if(keyCode == KeyEvent.KEYCODE_BACK) { 
			 	Myback(); 
			 	return true; 
			 } else {
				return super.onKeyDown(keyCode, event); 
		} */
	}
	 
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("dengying", "FlashLight onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v("dengying", "FlashLight onRestart");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("dengying", "FlashLight onResume");
	}	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("dengying", "FlashLight onPause");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.v("dengying", "FlashLight onStop");
		//hejianfeng add start
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.stop();
		}
		mMediaPlayer.release();
		//hejianfeng add start
		Myback();
		
		/*if(!isOnSaveInstanceState){
			Myback();
		}else{
			isOnSaveInstanceState = false;
		}*/
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("dengying", "FlashLight onDestroy");
	}	
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		Log.d("dengying", "FlashLight onRestoreInstanceState");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//isOnSaveInstanceState = true;
		Log.d("dengying", "FlashLight onSaveInstanceState");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		try {
			super.onConfigurationChanged(newConfig);
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				//Log.v("dengying", "onConfigurationChanged_ORIENTATION_LANDSCAPE");
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				//Log.v("dengying", "onConfigurationChanged_ORIENTATION_PORTRAIT");
			}
		} catch (Exception ex) {
		}

		Log.d("dengying", "FlashLight onConfigurationChanged");
	}

	public void Myback() {
		/*if (!onBitmap.isRecycled()) {
			onBitmap.recycle();
			onBitmap = null;
		}

		if (!offBitmap.isRecycled()) {
			offBitmap.recycle();
			offBitmap = null;
		}*/

		if (!flashligth_opened) {
			FlashLight.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		} else if (flashligth_opened) {
			camera.release();
			FlashLight.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			flashligth_opened = false;
		}
	/**hejianfeng delete 
		at.stop();
		at.release();
		am.setMode(AudioManager.MODE_NORMAL);
		*/
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
