package com.dream.sos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;

import android.util.Log;

import com.dream.sos.R;

public class CopyContactsList extends Activity {

	private final static String dy_TAG = "dengying";

	private String copyContactsMode;

	private ListView listView;
	private List<Map<String, Object>> contactsList;
	private ArrayList<String> getcontactsList;

	private ImageButton okbtn;
	private ImageButton cancelbtn;

	private final int UPDATE_LIST = 1;
	private ProgressDialog proDialog;

	private Thread getcontacts = null;
	private Handler updateListHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case UPDATE_LIST:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				updateList();
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contacts_choose);

		Intent intent = getIntent();
		copyContactsMode = intent.getStringExtra("copyContactsMode");

		contactsList = new ArrayList<Map<String, Object>>();

		getcontactsList = new ArrayList<String>();

		okbtn = (ImageButton) findViewById(R.id.contacts_done_button);
		okbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (getcontactsList != null && getcontactsList.size() > 0) {
					Intent i = new Intent();
					Bundle b = new Bundle();
					b.putStringArrayList("GET_CONTACT", getcontactsList);
					i.putExtras(b);
					setResult(RESULT_OK, i);
				}

				finish();
			}
		});

		cancelbtn = (ImageButton) findViewById(R.id.contact_back_button);
		cancelbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (copyContactsMode.equals("Single")) {

					String number = (String) contactsList.get(position).get("number");
					getcontactsList.add(number);

					if (getcontactsList != null && getcontactsList.size() > 0) {
						Intent i = new Intent();
						Bundle b = new Bundle();
						b.putStringArrayList("GET_CONTACT", getcontactsList);
						i.putExtras(b);
						setResult(RESULT_OK, i);
					}
					finish();
				}
			}
		});

		getcontacts = new Thread(new GetContacts());
		getcontacts.start();
		proDialog = ProgressDialog.show(this, "loading", "loading", true, true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	void updateList() {
		if (contactsList != null) {
			MyAdapter adapter = new MyAdapter(this);
			listView.setAdapter(adapter);
		}
	}

	class GetContacts implements Runnable {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {

			// TODO Auto-generated method stub
			//queryContacts1();
			
			queryContacts2();

			Message msg = new Message();
			msg.what = UPDATE_LIST;
			updateListHandler.sendMessage(msg);
		}
	}

	private void queryContacts1(){
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;

		long start = System.currentTimeMillis();
		Cursor cursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
		long end = System.currentTimeMillis();
		Log.e(dy_TAG, "managedQuery Contacts,time(ms)=" + (end - start));

		Cursor phonecur = null;

		start = System.currentTimeMillis();

		while (cursor.moveToNext()) {

			// 取得联系人名字
			int nameFieldColumnIndex = cursor.getColumnIndex(android.provider.ContactsContract.PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);

			// 取得联系人ID
			String contactId = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
			phonecur = managedQuery(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

			// 取得电话号码(可能存在多个号码)
			while (phonecur.moveToNext()) {
				String strPhoneNumber = phonecur.getString(phonecur.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER));

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", name);
				map.put("number", strPhoneNumber);
				map.put("isChecked", "false");
				contactsList.add(map);
			}
		}

		end = System.currentTimeMillis();
		Log.e(dy_TAG, "cursor.move,time(ms)=" + (end - start));


		// 我在代码中使用了Context.managedQuery()，Cursor.close()方法，但是在android
		// 4.0及其以上的版本中，Cursor会自动关闭，不需要用户自己关闭。
		if (android.os.Build.VERSION.SDK_INT < 14) {
			if (phonecur != null) {
				phonecur.close();
			}

			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	private void queryContacts2(){
		Cursor phones = null;
		ContentResolver cr = getContentResolver();
		String[] CONTACTOR_ION = new String[]{
			    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			    ContactsContract.Contacts.DISPLAY_NAME,
			    ContactsContract.CommonDataKinds.Phone.NUMBER
			};
		
		try {
			phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, CONTACTOR_ION, null, null, "sort_key");

			if (phones != null) {
				final int contactIdIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
				final int displayNameIndex = phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				final int phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
				String strPhoneNumber, name, contactIdString;
				while (phones.moveToNext()) {
					strPhoneNumber = phones.getString(phoneIndex);
					name = phones.getString(displayNameIndex);
					//contactIdString = phones.getString(contactIdIndex);
					
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", name);
					map.put("number", strPhoneNumber);
					map.put("isChecked", "false");
					contactsList.add(map);
				}
			}
		} catch (Exception e) {
			Log.e(dy_TAG, e.getMessage());
		} finally {
			if (android.os.Build.VERSION.SDK_INT < 14) {
				if (phones != null){
					phones.close();
				}
			}
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		contactsList.clear();
		getcontactsList.clear();

		super.onDestroy();
	}

	static class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView number;
		public CheckBox ck;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;

		private MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return contactsList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.contacts_listitem, null);

				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.number = (TextView) convertView.findViewById(R.id.number);
				holder.ck = (CheckBox) convertView.findViewById(R.id.ck);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.icon.setImageResource(R.drawable.contact_default_photo);

			String name = (String) contactsList.get(position).get("name");
			final String number = (String) contactsList.get(position).get("number");

			holder.name.setText(name);
			holder.number.setText(number);

			final CheckBox ck = holder.ck;
			final int index = position;
			String isChecked = (String) contactsList.get(position).get("isChecked");

			Log.e("dengying", "position=" + position + " isChecked=" + isChecked);

			ck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
				
						/*if (getcontactsList.size() > 5) {
							ck.setChecked(false);
							showToast(getString(R.string.sos_sms_nubmer_toast));
							return;
						}*/

						if (!getcontactsList.contains(number)) {
							getcontactsList.add(number);
						}

						ck.setChecked(true);
						contactsList.get(index).put("isChecked", "true");

						Log.e("dengying", "index=" + index + " isChecked=" + (String) contactsList.get(index).get("isChecked"));
					} else {
						getcontactsList.remove(number);
						ck.setChecked(false);
						contactsList.get(index).put("isChecked", "false");
					}
				}
			});

			if ("true" == isChecked) {
				ck.setChecked(true);
			} else if ("false" == isChecked) {
				ck.setChecked(false);
			}

			if (copyContactsMode.equals("Single")) {
				ck.setVisibility(android.view.View.GONE);
			}

			return convertView;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent();
			Bundle b = new Bundle();
			b.putStringArrayList("GET_CONTACT", getcontactsList);
			i.putExtras(b);
			setResult(RESULT_OK, i);
		}

		return super.onKeyDown(keyCode, event);
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