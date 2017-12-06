package com.dream.sos;


import com.dream.sos.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class SOSPersonal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		initView();
	}
	
	private void initView() {
				
		ImageButton btnCancel = (ImageButton) findViewById(R.id.cancel);
				
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});	
		
		
		TextView txtTitle = (TextView) findViewById(R.id.title);
		txtTitle.setText(R.string.edit_title);
		
		ImageButton btnEdit = (ImageButton) findViewById(R.id.edit);
		
		btnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SOSPersonal.this, SOSPersonalEdit.class);
				startActivity(i);
			}
		});			
		
		SharedPreferences sharedPreferences = getSharedPreferences("sospref", Context.MODE_PRIVATE);
		
		String name = sharedPreferences.getString("name", "");
		String sex = sharedPreferences.getString("sex", "");
		String age = sharedPreferences.getString("age", "");		
		String mobile = sharedPreferences.getString("mobile", "");
		String address = sharedPreferences.getString("address", "");
		String note = sharedPreferences.getString("note", ""); 
		
		
		TextView txtName = (TextView) findViewById(R.id.info_name);
		TextView txtSex = (TextView) findViewById(R.id.info_sex);
		TextView txtAge = (TextView) findViewById(R.id.info_age);
		TextView txtMobile = (TextView) findViewById(R.id.info_mobile);		
		TextView txtAddress = (TextView) findViewById(R.id.info_address);
		TextView txtNote = (TextView) findViewById(R.id.info_note);		

		txtName.setText(name);
		txtSex.setText(sex);
		txtAge.setText(age);
		txtMobile.setText(mobile);
		txtAddress.setText(address);
		txtNote.setText(note);
	}
	
}
