package com.example.smsdefault;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MmsReceiver extends BroadcastReceiver {

	private final static String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_ACTION)) {
			this.abortBroadcast();//不再往下传递消息
		}
	}

}
