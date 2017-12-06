package com.dream.systeminfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class SystemInfo {

	private Context mContext;
	private TelephonyManager mTelephonyManager;

	public String deviceName; // 设备名称
	public String brandName; // 系统定制商
	public String productName; // 手机制造商
	public String manufactuereName; // 硬件制造商
	public String osVersion;
	
	public String deviceid;
	public String imsi; // SIM国际移动号码标示
	public String androidid;
	public String macAddress;
	
	public static String version;
	
    static SystemInfo mSystemInfo;
    
    public static SystemInfo getInstance(Context context) {
        if (mSystemInfo == null) {
            mSystemInfo = new SystemInfo(context);
        }
        return mSystemInfo;
    }	
	    
	private SystemInfo(Context context) {
		this.mContext = context;
		this.mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		initSystemInfo();
	}

	public void initSystemInfo() {

		deviceName = Build.DEVICE;
		brandName = Build.BRAND;
		productName = Build.PRODUCT;
		manufactuereName = Build.MANUFACTURER;
		osVersion = Build.VERSION.RELEASE;
		
		// 所有的设备都可以返回一个TelephonyManager.getDeviceId()
		// 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
		// 手机的唯一标识，像GSM手机的 IMEI 和 CDMA手机的 MEID. 但在中国山寨手机导致这个号码不是唯一标识了
		// 取出的值是international mobile Equipment identity手机唯一标识码，即imei；
		deviceid = mTelephonyManager.getDeviceId();
		
		imsi = mTelephonyManager.getSubscriberId();
		if (imsi == null) {
			imsi = "1234567";
		}
		
		WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		macAddress = info.getMacAddress();// 更换为MacAddressWifi地址
	}

	@Override
	public String toString() {
		return "SystemInfo deviceName=" + deviceName + ", brandName=" + brandName + ", productName=" + productName + ", manufactuereName=" + manufactuereName + ", deviceid=" + deviceid + ", imsi=" + imsi + ", androidid=" + androidid;
	}
}
