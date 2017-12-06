package com.dream.game;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    protected final static String TAG = "dream";

	private Context mContext;
    
    MainView view;
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String SCORE = "score";
    public static final String HIGH_SCORE = "high score temp";
    public static final String UNDO_SCORE = "undo score";
    public static final String CAN_UNDO = "can undo";
    public static final String UNDO_GRID = "undo";
    public static final String GAME_STATE = "game state";
    public static final String UNDO_GAME_STATE = "undo game state";

    private boolean isSpotAd = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	Log.i(TAG, "onCreate");
    	
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = new MainView(getBaseContext());

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        view.hasSaveState = settings.getBoolean("save_state", false);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("hasState")) {
                load();
            }
        }
        setContentView(view);

		mContext = this;
		
		//设置广告条广告
		setupBannerAd();
			
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU) {
            //Do nothing
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            view.game.move(2);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            view.game.move(0);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            view.game.move(3);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            view.game.move(1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("hasState", true);
        save();
    }

    private void save() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        Tile[][] field = view.game.grid.field;
        Tile[][] undoField = view.game.grid.undoField;
        editor.putInt(WIDTH, field.length);
        editor.putInt(HEIGHT, field.length);
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    editor.putInt(xx + " " + yy, field[xx][yy].getValue());
                } else {
                    editor.putInt(xx + " " + yy, 0);
                }

                if (undoField[xx][yy] != null) {
                    editor.putInt(UNDO_GRID + xx + " " + yy, undoField[xx][yy].getValue());
                } else {
                    editor.putInt(UNDO_GRID + xx + " " + yy, 0);
                }
            }
        }
        editor.putLong(SCORE, view.game.score);
        editor.putLong(HIGH_SCORE, view.game.highScore);
        editor.putLong(UNDO_SCORE, view.game.lastScore);
        editor.putBoolean(CAN_UNDO, view.game.canUndo);
        editor.putInt(GAME_STATE, view.game.gameState);
        editor.putInt(UNDO_GAME_STATE, view.game.lastGameState);
        editor.commit();
    }

    private void load() {
        //Stopping all animations
        view.game.aGrid.cancelAnimations();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        for (int xx = 0; xx < view.game.grid.field.length; xx++) {
            for (int yy = 0; yy < view.game.grid.field[0].length; yy++) {
                int value = settings.getInt(xx + " " + yy, -1);
                if (value > 0) {
                    view.game.grid.field[xx][yy] = new Tile(xx, yy, value);
                } else if (value == 0) {
                    view.game.grid.field[xx][yy] = null;
                }

                int undoValue = settings.getInt(UNDO_GRID + xx + " " + yy, -1);
                if (undoValue > 0) {
                    view.game.grid.undoField[xx][yy] = new Tile(xx, yy, undoValue);
                } else if (value == 0) {
                    view.game.grid.undoField[xx][yy] = null;
                }
            }
        }

        view.game.score = settings.getLong(SCORE, view.game.score);
        view.game.highScore = settings.getLong(HIGH_SCORE, view.game.highScore);
        view.game.lastScore = settings.getLong(UNDO_SCORE, view.game.lastScore);
        view.game.canUndo = settings.getBoolean(CAN_UNDO, view.game.canUndo);
        view.game.gameState = settings.getInt(GAME_STATE, view.game.gameState);
        view.game.lastGameState = settings.getInt(UNDO_GAME_STATE, view.game.lastGameState);
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
						
						if(!isSpotAd){
							// 设置广告条
							setupSpotAd();
							
							isSpotAd =true;
						}
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
					//Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.NON_AD:
					//Toast.makeText(mContext, "暂无广告", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.RESOURCE_NOT_READY:
					Log.e(TAG, "资源还没准备好");
					//Toast.makeText(mContext, "请稍后再试", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.SHOW_INTERVAL_LIMITED:
					Log.e(TAG, "展示间隔限制");
					//Toast.makeText(mContext, "请勿频繁展示", Toast.LENGTH_SHORT).show();
					break;
				case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
					Log.e(TAG, "控件处在不可见状态");
					//Toast.makeText(mContext, "请设置插屏为可见状态", Toast.LENGTH_SHORT).show();
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
		
		load();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 插播广告
		SpotManager.getInstance(mContext).onPause();
		// 视频广告
		//VideoAdManager.getInstance(mContext).onPause();
		
        save();
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
