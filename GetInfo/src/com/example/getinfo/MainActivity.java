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

		// 所有的设备都可以返回一个TelephonyManager.getDeviceId()
		// 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
		// 手机的唯一标识，像GSM手机的 IMEI 和 CDMA手机的 MEID. 但在中国山寨手机导致这个号码不是唯一标识了
		// 取出的值是international mobile Equipment identity手机唯一标识码，即imei；

		String deviceid = tm.getDeviceId();

		// 对于移动的用户，手机号码(MDN)保存在运营商的服务器中，而不是保存在SIM卡里。SIM卡只保留了IMSI和一些验证信息
		// 取出的 值是手机号，即MSISDN ： mobile subscriber ISDN用户号码，这个是我们说的139，136那个号码；
		// 是通过SIM卡相关文件记录得到的数据
		// 归结到是否可以从SIM卡的EFmsisdn文件取出手机号码了，不幸的是一般运营商不会把用户号码写在这个文件的，为什么呢？
		// 因为这个手机号码是在用户买到卡并开通时才将IMSI和MSISDN对应上的，卡内生产出来时只有IMSI，你不知道用户喜欢那个手机号码，因此一般不先对应IMSI和MSISDN，即时有对应也不写这个文件的。

		String tel = tm.getLine1Number();

		// 所有的GSM设备(测试设备都装载有SIM卡) 可以返回一个TelephonyManager.getSimSerialNumber()
		// 所有的CDMA 设备对于 getSimSerialNumber() 却返回一个空值！
		// 360手机卫士可能会影响到imei和imsi的获取，禁止了“获取该应用获取设备信息”，改为“允许”即可正常获取IMEI、IMSI
		// 返回SIM卡的序列号(ICCID) ICCID:ICC
		// identity集成电路卡标识，这个是唯一标识一张卡片物理号码的

		String iccid = tm.getSimSerialNumber();

		// sim卡的序列号(IMSI)，international
		// mobiles subscriber
		// identity国际移动用户号码标识，

		String imsi = tm.getSubscriberId();

		// 获取imei和imsi的第二种方法
		// String imsi2 =Android.os.SystemProperties.get(
		// android.telephony.TelephonyProperties.PROPERTY_IMSI);
		// String imei2 =android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMEI);
		// 此处获取设备ANDROID_ID
		// 所有添加有谷歌账户的设备可以返回一个 ANDROID_ID
		// 所有的CDMA设备对于 ANDROID_ID 和
		// TelephonyManager.getDeviceId()返回相同的值（只要在设置时添加了谷歌账户）
		// 有些山寨手机这个号码是一个…
		// 是一个64位的数字，在设备第一次启动的时候随机生成并在设备的整个生命周期中不变。（如果重新进行出厂设置可能会改变）

		String android_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);

		// 获取Android手机型号和OS的版本号
		String mobileName = Build.DEVICE;
		String osVersion = Build.VERSION.RELEASE;
		WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String macAddress = info.getMacAddress();// 更换为MacAddressWifi地址
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

		// 获取当前手机支持的移动网络类型
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
				"设备名称 : " + mobileName + "\n"
				 + "系统版本 :" + osVersion + "\n" 
				 + "设备ID[imei|deviceid] :" + deviceid + "\n" 
		         + "SIM国际移动号码标示[imsi] : " + imsi + "\n"				 
				 + "SIM卡集成电路卡标识[iccid] :" + iccid + "\n"
				 + "ANDROID_ID :" + android_id + "\n"				 
		         + "手机号  :" + tel + "\n"				 
				 + "软件版本 :" + softwareVersion + "\n" 
		         + "手机网络类型 :" + phoneType  + "\n"
		         + "Wifi MAC地址 :" + macAddress;
		
		txtInfo.setText(sytemInfo);
	}
}
