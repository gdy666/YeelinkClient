package me.yeso.yeelink.util;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.yeso.yeelink.base.Device;
import me.yeso.yeelink.base.Devices;

import org.json.JSONException;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;

public class YeelinkAdapter {
	private static final String NET_ERROR="网络异常";
	/*
	 * GET
	 * 参数
	 * login 用户名
	 * password 密码
	 * 验证通过时返回：
	 * {"status":"success","desc":"Login and password are ok","data":{"apikey":"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"}}
	 */
	private static final String GET_APIKEY_URL="http://www.yeelink.net/mobile/login";
	/*
	 * LIST_DEVICES_API接受的参数
	 * GET
	 * login	用户名
	 * apikey  APIKEY
	 * 返回用户设备列表
	 */
	private static final String LIST_DEVICES_URL="http://www.yeelink.net/mobile/list_devices";
	
	
	private static String result;	//存放结果
	
	/*
	 * 接受参数
	 * userName 用户名
	 * passwd 	密码
	 * 正常获取时返回APIKEY，,获取失败返回空字符串，异常返回error.
	 *
	 */
	public static String getAPIKEY(String userName,String passwd){
		String apikey="";
		String response="";
		try{
			response=HttpRequest.get(GET_APIKEY_URL,true,"login",userName,"password",passwd).body();
		}catch(Exception e){
			response=NET_ERROR;
		}finally{
			if(response.indexOf("success")!=-1){	//获取到APIKEY
				int index;
				index=response.indexOf("apikey");
				index+=9;
				apikey=response.substring(index, index+32);//获取到APIKEY
			}else if(NET_ERROR.equals(response)){	//网络异常
				apikey=response;
			}
		}
		
		
		return apikey;
	}

	/*
	 * 
	 */
	public static Devices getDevices(String user,String apikey){
		Devices devices=new Devices();
		String response="";
		try{
			response=HttpRequest.get(LIST_DEVICES_URL, true, "login",user,"apikey",apikey).body().toString();
		}catch(Exception e){
			//devices.state=NET_ERROR;
			response=NET_ERROR;
		}
		finally{
			if(response.indexOf("success")!=-1){	//获取成功
				devices.state="success";
				devices.devList=jsonToDevices(StringConvert(getDevicesJson(response)));
			}else if("fail".equals(response)){	//获取失败
				devices.state="fail";
			}else{
				devices.state=response;			//网络异常
			}
			//System.out.println("devices :"+response);
		}
		return devices;
	}

	public static void getSensors(int deviceId,String apikey){
		String url="http://api.yeelink.net/v1.0/device/"+deviceId+"/sensors";
	//	httpGet(url,null,apikey);
	}
	
	/*
	 * 从结果中提取devices部分的JSON
	 */
	private static String getDevicesJson(String str){
		String jsonStr="";
		int start=str.indexOf("data\":{\"devices\":")+17;
		int end=str.length()-2;
		jsonStr=str.substring(start, end);
		return jsonStr;
	}
	
	/*
	 * JSON转换为实例
	 */
	private static List<Device> jsonToDevices(String jsonStr){
		if(jsonStr.equals("[]")||jsonStr.equals(null)||jsonStr.equals("")){
			return null;
		}
		Type listType = new TypeToken<LinkedList<Device>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<Device> list = gson.fromJson(jsonStr, listType);
		return list;
	}

	/*
	 * 字符串转中文
	 */
	private static String StringConvert(String str){
		String s="'"+str+"'"; 
		String result="";
		try {
			result=new JSONTokener(s).nextValue().toString();
		} catch (JSONException e) {
			Log.e("JSON出错", "字符串转中文时出错！");
		}
		return result;
	}
	

}
