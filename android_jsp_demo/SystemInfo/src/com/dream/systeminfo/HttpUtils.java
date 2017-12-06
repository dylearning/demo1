package com.dream.systeminfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtils {

	public static final String TAG = "dream";
	
	public static String HttpClientDoGet(String url) {
		String result = null;
		HttpResponse httpResponse = null;

		try {
			HttpGet httpget = new HttpGet(url);// 新建HttpGet对象
			httpResponse = new DefaultHttpClient().execute(httpget);// 获取HttpResponse实例

			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");// 使用getEntity方法活得返回结果

				Log.e(TAG, "HttpClientDoGet，success result：" + result);

			} else {
				Log.e(TAG, "HttpClientDoGet fail");
			}
		} catch (IOException e) {
			e.printStackTrace();

			Log.e(TAG, " HttpClientDoGet IOException:" + e.toString());
		}
		return result;
	}

	public static String HttpClientDoPost(String url) {
		
		String result = null;
		
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // 设置连接超时为5秒
			HttpClient client = new DefaultHttpClient(httpParams); // 生成一个http客户端发送请求对象
			HttpPost httpPost = new HttpPost(url); // 设定请求方式

			// 传递参数
			/*NameValuePair nameValuePair1 = new BasicNameValuePair("name", "yang");
			NameValuePair nameValuePair2 = new BasicNameValuePair("pwd", "123123");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(nameValuePair1);
			nameValuePairs.add(nameValuePair2);

			if (nameValuePairs != null && nameValuePairs.size() != 0) {
				// 把键值对进行编码操作并放入HttpEntity对象中
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			}*/

			HttpResponse httpResponse = client.execute(httpPost); // 发送请求并等待响应

			if (httpResponse.getStatusLine().getStatusCode() != 200) {// 判断网络连接是否成功
				Log.e(TAG, "HttpClientDoPost StatusCode != 200");
			}

			HttpEntity entity = httpResponse.getEntity(); // 获取响应里面的内容

			//InputStream inputStream = entity.getContent(); // 得到服务气端发回的响应的内容（都在一个流里面）

			result = EntityUtils.toString(entity); // 得到服务气端发回的响应的内容（都在一个字符串里面）

		} catch (Exception e) {
			Log.e(TAG, "HttpClientDoPost Exception e=" + e.toString());
		}

		return result;
	}

	public static String HttpURLConnectionDoGet(String url) throws IOException {
		String result = null;

		 /*StringBuilder buf = new StringBuilder("http://192.168.0.103:8080/Server/PrintServlet");  
         buf.append("?");  
         buf.append("name="+URLEncoder.encode(name.getText().toString(),"UTF-8")+"&");  
         buf.append("age="+URLEncoder.encode(age.getText().toString(),"UTF-8"));  
         URL mUrl = new URL(buf.toString());  */
		
		try {
			URL mUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();

			conn.setRequestMethod("GET");

			if (conn.getResponseCode() == 200) {

				StringBuffer sb = new StringBuffer();
				String readLine = new String();

				BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();

				result = sb.toString();

				Log.e(TAG, " HttpURLConnectionDoGet result:" + result);
			}

			conn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return result;
	}	
	
	public static String HttpURLConnectionDoPost(String url) throws IOException {
		String result = null;

	    //String urlPath = new String("http://localhost:8080/Test1/HelloWorld");  
	    //String param="name="+URLEncoder.encode("丁丁","UTF-8")+"&bb="; 
	    
		String param = "";
		
	    //建立连接 
	    URL mUrl=new URL(url); 
	    HttpURLConnection httpConn=(HttpURLConnection)mUrl.openConnection(); 
	    
	    //设置参数 
	    httpConn.setDoOutput(true);      	//需要输出 
	    httpConn.setDoInput(true);       	//需要输入 
	    httpConn.setUseCaches(false);  		//不允许缓存 
	    httpConn.setRequestMethod("POST");  //设置POST方式连接 

	    //设置请求属性"Content-Type"是数据类型 "application/octet-stream"
	    //httpConn.setRequestProperty("Content-Type", "application/octet-stream");//流信息 可以传输图片音频等信息
	    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //文本信息
	    
	    httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接 
	    httpConn.setRequestProperty("Charset", "UTF-8"); 
	    
	    //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect 
	    httpConn.connect(); 
	    
	    //建立输入流，向指向的URL传入参数 
	    DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream()); 
	    dos.writeBytes(param); 
	    dos.flush(); 
	    dos.close(); 
	    
	    //获得响应状态 
	    int resultCode=httpConn.getResponseCode(); 
	    
	    if(HttpURLConnection.HTTP_OK==resultCode){ 
	      StringBuffer sb=new StringBuffer(); 
	      String readLine=new String(); 
	      
	      BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8")); 
	      while((readLine=responseReader.readLine())!=null){ 
	        sb.append(readLine).append("\n"); 
	      } 
	      responseReader.close(); 
	      
	      result = sb.toString();
	    }  

	    httpConn.disconnect();
	    
		return result;
	}	
}
