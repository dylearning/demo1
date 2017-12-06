package com.dream.systeminfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class SystemInfo {

	private Context mContext;
	private TelephonyManager mTelephonyManager;

	public String deviceName; // �豸����
	public String brandName; // ϵͳ������
	public String productName; // �ֻ�������
	public String manufactuereName; // Ӳ��������
	public String osVersion;
	
	public String deviceid;
	public String imsi; // SIM�����ƶ������ʾ
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
		
		// ���е��豸�����Է���һ��TelephonyManager.getDeviceId()
		// �����GSM���磬����IMEI�������CDMA���磬����MEID
		// �ֻ���Ψһ��ʶ����GSM�ֻ��� IMEI �� CDMA�ֻ��� MEID. �����й�ɽկ�ֻ�����������벻��Ψһ��ʶ��
		// ȡ����ֵ��international mobile Equipment identity�ֻ�Ψһ��ʶ�룬��imei��
		deviceid = mTelephonyManager.getDeviceId();
		
		imsi = mTelephonyManager.getSubscriberId();
		if (imsi == null) {
			imsi = "1234567";
		}
		
		WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		macAddress = info.getMacAddress();// ����ΪMacAddressWifi��ַ
	}

	@Override
	public String toString() {
		return "SystemInfo deviceName=" + deviceName + ", brandName=" + brandName + ", productName=" + productName + ", manufactuereName=" + manufactuereName + ", deviceid=" + deviceid + ", imsi=" + imsi + ", androidid=" + androidid;
	}
}
