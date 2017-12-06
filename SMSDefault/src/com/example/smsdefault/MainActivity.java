package com.example.smsdefault;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import com.example.smsdefault.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.e("dengyingSMS", "MainActivity onCreate");
		
		Intent mIntent = new Intent(this, HeadlessSmsSendService.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startService(mIntent);
		
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
