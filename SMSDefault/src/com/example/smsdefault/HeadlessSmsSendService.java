package com.example.smsdefault;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.util.Log;

public class HeadlessSmsSendService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		Log.e("dengyingSMS", "HeadlessSmsSendService oncreate");
		
		ContentObserver mObserver = new ContentObserver(new Handler()) {

			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				
				Context context = getApplicationContext();
				
				String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context);  
				Log.e("dengyingSMS","defaultSmsApp="+defaultSmsApp);
				
				
				Intent changeIntent = new Intent(Sms.Intents.ACTION_CHANGE_DEFAULT);  
				changeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				changeIntent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());  
				startActivity(changeIntent); 
				
				ContentResolver resolver = getContentResolver();
				Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), new String[] { "_id","address", "body" }, null, null, "_id desc");
				long id = -1;

				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					id = cursor.getLong(0);
					String address = cursor.getString(1);
					String body = cursor.getString(2);

					Log.e("dengyingSMS", String.format("address: %s\n body: %s", address, body));
				}
				cursor.close();

				if (id != -1) {
					int count = resolver.delete(Sms.CONTENT_URI, "_id=" + id,null);
					if (count == 1) {
						Log.e("dengyingSMS","删除成功");
					} else {
						Log.e("dengyingSMS","删除失败");
					}
				}
				
				 Intent restoreIntent = new Intent(Sms.Intents.ACTION_CHANGE_DEFAULT);  
				 restoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 restoreIntent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);  
				 startActivity(restoreIntent); 
			}
		};

		getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mObserver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub;
		Log.e("dengyingSMS", "HeadlessSmsSendService onStart");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
	}
}
