package me.yeso.yeelink.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.yeso.yeelink.base.User;

import org.json.JSONException;
import org.json.JSONTokener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class YeelinkAdapter {
	/*
	 * LIST_DEVICES_API接受的参数
	 * login	用户名
	 * apikey  APIKEY
	 */
	private static final String LIST_DEVICES_API="http://www.yeelink.net/mobile/list_devices";
	private static String result;	//存放结果
	public static void getDevices(User user){
		RequestParams params=new RequestParams();
		params.put("login", user.getUserName());
		params.put("apikey", user.getApikey());
		httpGet(LIST_DEVICES_API,params,null);
	}

	public static void getSensors(int deviceId,String apikey){
		String url="http://api.yeelink.net/v1.0/device/"+deviceId+"/sensors";
		httpGet(url,null,apikey);
	}
	private static void httpGet(String url,RequestParams params,String apikey){
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("U-ApiKey",apikey);
		client.get(url, params, new AsyncHttpResponseHandler(){
			String httpResult="";
			@Override
			public void onSuccess(String content) {
				httpResult+=content;
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				httpResult+=content;
				super.onFailure(error, content);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				 String v = "'"+httpResult+"'";  
				 try {
					 System.out.println("FFFFFFF->>>"+new JSONTokener(v).nextValue().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	System.out.println("FFFFFFF->>>"+httpResult);
				super.onFinish();
			}
		});
	}
	

}
