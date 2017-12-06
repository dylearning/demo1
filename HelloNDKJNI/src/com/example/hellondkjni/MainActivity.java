package com.example.hellondkjni;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView txt=(TextView)findViewById(R.id.txt);
		
		txt.setText(stringFromNDKJNI());
	}

	public native String stringFromNDKJNI();

	static {
		System.loadLibrary("hello-ndk-jni");
	}
}
