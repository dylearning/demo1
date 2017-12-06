package com.dream.sos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dream.sos.widget.TopBar;
import com.dream.sos.widget.TopBar.onTopBarListener;

public class SOSSmsNumberSetting extends Activity {

	EditText number_editor;

	private SharedPreferences sharedPreferences;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos_sms_number_setting);

		initView();
	}

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
				numberList = numberList + getcontactsList.get(i) + ",";
			}
			
			number_editor.setText(numberList);

			break;
		}
		}
	}

	private void initView() {

		RelativeLayout title_layout = (RelativeLayout) findViewById(R.id.title_layout);

		TopBar mTopBar = (TopBar)findViewById(R.id.topbar);
		// 设置监听
		mTopBar.setOnTopBarListener(mTopBarListener);		
		
		/*ImageButton btnCancel = (ImageButton) title_layout.findViewById(R.id.cancel);

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});*/

		/*TextView txtTitle = (TextView) title_layout.findViewById(R.id.title);
		txtTitle.setText(R.string.number_title_mms);*/

		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		Button doneBtn = (Button) findViewById(R.id.doneBtn);

		sharedPreferences = getSharedPreferences("sospref", Context.MODE_PRIVATE);
		String sos_sms_mobile_number = sharedPreferences.getString("sos_sms_mobile_number", "");

		number_editor = (EditText) findViewById(R.id.number_editor);
		number_editor.setText(sos_sms_mobile_number);
		//number_editor.addTextChangedListener(new TextFilter(number_editor));
		
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText number_editor = (EditText) findViewById(R.id.number_editor);

				String sos_number = number_editor.getText().toString();
				
				sos_number = ToDBC(sos_number);
				
				String[] str = sos_number.split(",");
				
				if(str.length > 5){
					showToast(getString(R.string.sos_sms_nubmer_toast));
					return;
				}
				
				Editor editor = sharedPreferences.edit();
				editor.putString("sos_sms_mobile_number", sos_number);

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

				Intent i = new Intent(SOSSmsNumberSetting.this, CopyContactsList.class);
				
				i.putExtra("copyContactsMode", "multiple");//Single
				
				startActivityForResult(i, 0);
			}
		});

	}

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
	private String ToDBC(String input) {
	 char c[] = input.toCharArray();
	 
	 for (int i = 0; i < c.length; i++) {
	   if (c[i] == '\u3000') {
		   c[i] = ' ';
	   } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
	     c[i] = (char) (c[i] - 65248);
	   }
	 }
	 
	 String returnString = new String(c);
	
	 return returnString;
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

	
    class TextFilter implements TextWatcher {  
        private EditText editText;  
        private String rt;  
  
        public TextFilter(EditText editText) {  
            this.editText = editText;  
        }  
  
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {  
            Log.e("dengying ", "beforeTextChanged CharSequence: " + s + " -start: " + start + " -count: " + count);  
        }  
  
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            Log.e("dengying ", "onTextChanged CharSequence: " + s + " -start: " + start + " before: " + before + " -count: " + count);   
        }  
  
        @Override  
        public void afterTextChanged(Editable s) {  
            Log.e("dengying", "afterTextChanged "+s.toString());  
        }  
    }  

	public void showToast(String msg) {
		LayoutInflater inflater = getLayoutInflater();
		View toastLayout = inflater.inflate(R.layout.toast_normal_layout, (ViewGroup) findViewById(R.id.toast_layout));

		Toast toast = new Toast(getApplicationContext());
		((TextView) toastLayout.findViewById(R.id.prompt_text)).setText(msg);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
