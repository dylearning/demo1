package com.dream.sos;


import java.util.ArrayList;

import com.dream.sos.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
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

public class SOSNumberSetting extends Activity {

	private SharedPreferences sharedPreferences;
	EditText number_editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos_number_setting);
		
		initView();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case (RESULT_OK): {
				Bundle b = data.getExtras();
				ArrayList<String> getcontactsList = b.getStringArrayList("GET_CONTACT");
				String numberList = "";
				for(int i = 0;i < getcontactsList.size(); i ++){
					numberList = numberList + getcontactsList.get(i);
				}
				number_editor.setText(numberList);
				break;
		 	}
		}
		
		
		/*switch (requestCode) {
		case (1): {
			if (resultCode == Activity.RESULT_OK) {
				//Uri contactData = data.getData();
				//Cursor c = managedQuery(contactData, null, null, null, null);
				//c.moveToFirst();
				//String phoneNum = this.getContactPhone(c);
				//number_editor.setText(phoneNum); // mPhoneNum为接收的EditText或TextView
							
				
				Uri uri = data.getData();
				String[] colums = { ContactsContract.Contacts.Data._ID };
				Cursor cursor = getContentResolver().query(uri, colums, null, null, null);
				cursor.moveToFirst();

				int id = cursor.getInt(0);
				Uri dataUri = Uri.parse("content://com.android.contacts/data");
				Cursor dataCursor = getContentResolver().query(dataUri,null,"raw_contact_id=?", new String[] { id + "" }, null);

				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(dataCursor.getColumnIndex("data1"));
					String mime = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
					if ("vnd.android.cursor.item/phone_v2".equals(mime)) {
						number_editor.setText(data1);//number
					} else if ("vnd.android.cursor.item/name".equals(mime)) {
						//mPhoneNum.setText(mime);//name
					}
				}
			}
			break;
		}
		}*/
	}	
	
	private void initView() {
				
		/*ImageButton btnCancel = (ImageButton) findViewById(R.id.cancel);
				
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});	
		
		TextView txtTitle = (TextView) findViewById(R.id.title);
		txtTitle.setText(R.string.phone);*/
		
		
		onTopBarListener mTopBarListener = new onTopBarListener(){
		    public void onLeftClick(View v){
		    	finish();
		    }
		    
		    public void onRightClick(View v){
		    }   
		    
		    public void onTitleClick(View v){
		    }
		};		
		
		TopBar mTopBar = (TopBar)findViewById(R.id.topbar);
		// 设置监听
		mTopBar.setOnTopBarListener(mTopBarListener);
		
		number_editor = (EditText) findViewById(R.id.number_editor);
		number_editor.setFocusable(true);
		
		
		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});			

		Button doneBtn = (Button) findViewById(R.id.doneBtn);

		sharedPreferences = getSharedPreferences("sospref", Context.MODE_PRIVATE);
		String sos_mobile_number = sharedPreferences.getString("sos_mobile_number", "");

		number_editor.setText(sos_mobile_number);
		
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText number_editor = (EditText) findViewById(R.id.number_editor);

				Editor editor = sharedPreferences.edit();
				editor.putString("sos_mobile_number", number_editor.getText().toString());

				editor.commit();
				finish();				
			}
		});		
		
		Button btn_number_choose = (Button) findViewById(R.id.number_choose);

		btn_number_choose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				// Intent intent = new Intent(Intent.ACTION_PICK,
				// ContactsContract.Contacts.CONTENT_URI);
				// SOSSmsNumberSetting.this.startActivityForResult(intent, 1);
				
				Intent i = new Intent(SOSNumberSetting.this, CopyContactsList.class);
				i.putExtra("copyContactsMode", "Single");//Single
				startActivityForResult(i, 0);
			}
		});
		
	}
	
	// 获取联系人电话
	private String getContactPhone(Cursor cursor) {

		int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String phoneResult = "";
		// System.out.print(phoneNum);
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人的电话号码的cursor;
			Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
			// int phoneCount = phones.getCount();
			// allPhoneNum = new ArrayList<String>(phoneCount);
			if (phones.moveToFirst()) {
				// 遍历所有的电话号码
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);

					// 获取联系人姓名代码改为： String
					// phoneName=phones.getString(phones.getColumnIndex(PhoneLookup.DISPLAY_NAME));

					switch (phone_type) {
					case 2:
						phoneResult = phoneNumber;
						break;
					}
					// allPhoneNum.add(phoneNumber);
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
		}
		return phoneResult;
	}
}
