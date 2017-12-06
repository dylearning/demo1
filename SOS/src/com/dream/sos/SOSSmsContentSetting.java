package com.dream.sos;


import com.dream.sos.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dream.sos.widget.TopBar;
import com.dream.sos.widget.TopBar.onTopBarListener;

public class SOSSmsContentSetting extends Activity {
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos_sms_content_setting);
		
		initView();
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
		
		/*RelativeLayout title_layout  = (RelativeLayout) findViewById(R.id.title_layout);
		
		ImageButton btnCancel = (ImageButton) title_layout.findViewById(R.id.cancel);
				
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});*/	
		
		
		/*TextView txtTitle = (TextView) title_layout.findViewById(R.id.title);
		txtTitle.setText(R.string.Message_content);*/
		
		TopBar mTopBar = (TopBar)findViewById(R.id.topbar);
		// 设置监听
		mTopBar.setOnTopBarListener(mTopBarListener);
		
		Button doneBtn = (Button) findViewById(R.id.doneBtn);

		sharedPreferences = getSharedPreferences("sospref", Context.MODE_PRIVATE);
		String sos_sms_content = sharedPreferences.getString("sos_sms_content", getString(R.string.sos_sms_content));
		
		EditText number_editor = (EditText) findViewById(R.id.sms_content);
		number_editor.setText(sos_sms_content);
		
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText number_editor = (EditText) findViewById(R.id.sms_content);

				Editor editor = sharedPreferences.edit();
				editor.putString("sos_sms_content", number_editor.getText().toString());

				editor.commit();
				finish();				
			}
		});	
		
		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});	
	}
	
}
