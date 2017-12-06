package com.dream.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import cn.waps.AppConnect;
import cn.waps.AppListener;
import cn.waps.extend.QuitPopAd;
import cn.waps.extend.SlideWall;

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
        
        mContext = this;
        
        setContentView(view);
        
        /********************************************************************************************/
        
		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		AppConnect.getInstance("b3954ec6eb597c48ae68ce3a4ed7aadc", "baidu", this);		
		
		// 带有默认参数值的在线配置，使用此方法，程序第一次启动使用的是"defaultValue"，之后再启动则是使用的服务器端返回的参数值
		String showAd = AppConnect.getInstance(this).getConfig("showAd", "defaultValue");
        
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
		/********************************************************************************************/
    }
    
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
        }else if (keyCode == KeyEvent.KEYCODE_BACK) {
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
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("hasState", true);
        save();
    }

	// 建议加入onConfigurationChanged回调方法
	// 注:如果当前Activity没有设置android:configChanges属性,或者是固定横屏或竖屏模式,则不需要加入
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// 横竖屏状态切换时,关闭处于打开状态中的退屏广告
		QuitPopAd.getInstance().close();

		super.onConfigurationChanged(newConfig);
	}    
    
    protected void onPause() {
        super.onPause();
        save();
    }

    protected void onResume() {
        super.onResume();
        load();
    }


	@Override
	protected void onDestroy() {
		// 释放资源，原finalize()方法名修改为close()
		AppConnect.getInstance(this).close();
		super.onDestroy();
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
}
