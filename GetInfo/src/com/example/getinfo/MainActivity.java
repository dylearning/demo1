package com.example.getinfo;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView txtInfo = (TextView) findViewById(R.id.txtInfo);

		TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

		// ���е��豸�����Է���һ��TelephonyManager.getDeviceId()
		// �����GSM���磬����IMEI�������CDMA���磬����MEID
		// �ֻ���Ψһ��ʶ����GSM�ֻ��� IMEI �� CDMA�ֻ��� MEID. �����й�ɽկ�ֻ�����������벻��Ψһ��ʶ��
		// ȡ����ֵ��international mobile Equipment identity�ֻ�Ψһ��ʶ�룬��imei��

		String deviceid = tm.getDeviceId();

		// �����ƶ����û����ֻ�����(MDN)��������Ӫ�̵ķ������У������Ǳ�����SIM���SIM��ֻ������IMSI��һЩ��֤��Ϣ
		// ȡ���� ֵ���ֻ��ţ���MSISDN �� mobile subscriber ISDN�û����룬���������˵��139��136�Ǹ����룻
		// ��ͨ��SIM������ļ���¼�õ�������
		// ��ᵽ�Ƿ���Դ�SIM����EFmsisdn�ļ�ȡ���ֻ������ˣ����ҵ���һ����Ӫ�̲�����û�����д������ļ��ģ�Ϊʲô�أ�
		// ��Ϊ����ֻ����������û��򵽿�����ͨʱ�Ž�IMSI��MSISDN��Ӧ�ϵģ�������������ʱֻ��IMSI���㲻֪���û�ϲ���Ǹ��ֻ����룬���һ�㲻�ȶ�ӦIMSI��MSISDN����ʱ�ж�ӦҲ��д����ļ��ġ�

		String tel = tm.getLine1Number();

		// ���е�GSM�豸(�����豸��װ����SIM��) ���Է���һ��TelephonyManager.getSimSerialNumber()
		// ���е�CDMA �豸���� getSimSerialNumber() ȴ����һ����ֵ��
		// 360�ֻ���ʿ���ܻ�Ӱ�쵽imei��imsi�Ļ�ȡ����ֹ�ˡ���ȡ��Ӧ�û�ȡ�豸��Ϣ������Ϊ����������������ȡIMEI��IMSI
		// ����SIM�������к�(ICCID) ICCID:ICC
		// identity���ɵ�·����ʶ�������Ψһ��ʶһ�ſ�Ƭ��������

		String iccid = tm.getSimSerialNumber();

		// sim�������к�(IMSI)��international
		// mobiles subscriber
		// identity�����ƶ��û������ʶ��

		String imsi = tm.getSubscriberId();

		// ��ȡimei��imsi�ĵڶ��ַ���
		// String imsi2 =Android.os.SystemProperties.get(
		// android.telephony.TelephonyProperties.PROPERTY_IMSI);
		// String imei2 =android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMEI);
		// �˴���ȡ�豸ANDROID_ID
		// ��������йȸ��˻����豸���Է���һ�� ANDROID_ID
		// ���е�CDMA�豸���� ANDROID_ID ��
		// TelephonyManager.getDeviceId()������ͬ��ֵ��ֻҪ������ʱ����˹ȸ��˻���
		// ��Щɽկ�ֻ����������һ����
		// ��һ��64λ�����֣����豸��һ��������ʱ��������ɲ����豸���������������в��䡣��������½��г������ÿ��ܻ�ı䣩

		String android_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);

		// ��ȡAndroid�ֻ��ͺź�OS�İ汾��
		String mobileName = Build.DEVICE;
		String osVersion = Build.VERSION.RELEASE;
		WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String macAddress = info.getMacAddress();// ����ΪMacAddressWifi��ַ
		String softwareVersion = tm.getDeviceSoftwareVersion();// String

		// String versionName = null;
		// String versionCode = null;
		// PackageManagerpm = this.getPackageManager();
		// PackageInfo pi;
		// try {
		// pi = pm.getPackageInfo(this.getPackageName(), 0);
		// versionName = pi.versionName;
		// versionCode = String.valueOf(pi.versionCode);
		// } catch (NameNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// ��ȡ��ǰ�ֻ�֧�ֵ��ƶ���������
		String phoneType = null;

		switch (tm.getPhoneType()) {
			case TelephonyManager.PHONE_TYPE_NONE:
				phoneType = "NONE: ";
				break;
	
			case TelephonyManager.PHONE_TYPE_GSM:
				phoneType = "GSM: IMEI";
				break;
	
			case TelephonyManager.PHONE_TYPE_CDMA:
				phoneType = "CDMA: MEID/ESN";
				break;
	
			default:
				phoneType = "UNKNOWN: ID";
				break;
		}

		String sytemInfo = 
				"�豸���� : " + mobileName + "\n"
				 + "ϵͳ�汾 :" + osVersion + "\n" 
				 + "�豸ID[imei|deviceid] :" + deviceid + "\n" 
		         + "SIM�����ƶ������ʾ[imsi] : " + imsi + "\n"				 
				 + "SIM�����ɵ�·����ʶ[iccid] :" + iccid + "\n"
				 + "ANDROID_ID :" + android_id + "\n"				 
		         + "�ֻ���  :" + tel + "\n"				 
				 + "����汾 :" + softwareVersion + "\n" 
		         + "�ֻ��������� :" + phoneType  + "\n"
		         + "Wifi MAC��ַ :" + macAddress;
		
		txtInfo.setText(sytemInfo);
	}
}
