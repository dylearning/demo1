package com.android.topbardemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.topbardemo.widget.TopBar;
import com.android.topbardemo.widget.TopBar.onTopBarListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TopBar mTopBar = (TopBar) findViewById(R.id.topbar);
		mTopBar.setOnTopBarListener(mTopBarListener);
	}

	private onTopBarListener mTopBarListener = new onTopBarListener() {
		public void onLeftClick(View v) {
			finish();
		}

		public void onRightClick(View v) {
		}

		public void onTitleClick(View v) {
		}
	};
}
