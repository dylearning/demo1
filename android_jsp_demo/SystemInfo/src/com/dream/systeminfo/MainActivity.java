package com.dream.systeminfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class MainActivity extends Activity {

    private SystemInfo mSystemInfo;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
        if(mSystemInfo == null){
            mSystemInfo = SystemInfo.getInstance(this);
        }
		
		uploadServer();
	}
	
	
	private void uploadServer(){
	
        StringBuilder url = new StringBuilder("http://192.168.0.47:8080/PhoneSystemManager/system.do?");  
        
        StringBuffer urlParameter = new StringBuffer();		
		try {
			urlParameter.append("deviceid=" + URLEncoder.encode(mSystemInfo.deviceid, "UTF-8") + "&");
			urlParameter.append("imsi=" + URLEncoder.encode(mSystemInfo.imsi, "UTF-8") + "&");
			urlParameter.append("deviceName=" + URLEncoder.encode(mSystemInfo.deviceName, "UTF-8") + "&");
			urlParameter.append("brandName=" + URLEncoder.encode(mSystemInfo.brandName, "UTF-8") + "&");
			urlParameter.append("productName=" + URLEncoder.encode(mSystemInfo.productName, "UTF-8") + "&");
			urlParameter.append("manufactuereName=" + URLEncoder.encode(mSystemInfo.manufactuereName, "UTF-8") + "&");
			urlParameter.append("osVersion=" + URLEncoder.encode(mSystemInfo.osVersion, "UTF-8") + "&");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  	
		url.append(urlParameter);
		//http://192.168.0.47:8080/PhoneSystemManager/system.do?deviceid=test&imsi=666666&deviceName=C603&brandName=C603&productName=C603&manufactuereName=C603
		
		
        StringBuffer encryptUrlParameter = new StringBuffer();		
		try {
			encryptUrlParameter.append(/*"macAddress=" + */ URLEncoder.encode(mSystemInfo.macAddress, "UTF-8")/* + "&"*/);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  	
		
		Log.e("dream", "encryptUrlParameter=" + encryptUrlParameter);

		StringBuffer tokenUrlParameter = new StringBuffer();	
        url.append("token=");
		url.append(Base64.encodeToString(encryptUrlParameter.toString().getBytes(), Base64.DEFAULT));

		Log.e("dream", "url=" + url);
		
		HttpUtils.HttpClientDoGet(url.toString());
	}
	
	
    /*public static String getServerURL(int version){
        StringBuffer basePath = new StringBuffer();
        StringBuffer url = new StringBuffer();
        basePath.append("model=");
        basePath.append(android.os.Build.MODEL);
        basePath.append("&version=");
        basePath.append(version);
        basePath.append("cappu-g@od");

        url.append(HOST_PUSH);
        url.append("token=");
        url.append(Base64.encodeToString(basePath.toString().getBytes(), Base64.DEFAULT));
        Util.MyLog("i","url="+url.toString());
        //e.g http://theme.careos.com.cn/theme/validate/theme?token=bW9kZWw9YzYzMCZ2ZXJzaW9uPTBjYXBwdS1nQG9
        return url.toString();
    }*/
}
