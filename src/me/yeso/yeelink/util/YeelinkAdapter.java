package me.yeso.yeelink.util;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.yeso.yeelink.base.Device;
import me.yeso.yeelink.base.Devices;
import me.yeso.yeelink.base.Sensor;
import me.yeso.yeelink.base.Sensors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
				devices.devList=jsonToDevices(getDevicesJson(response));
			}else if("fail".equals(response)){	//获取失败
				devices.state="fail";
			}else{
				devices.state=response;			//网络异常
			}
			//System.out.println("devices :"+response);
		}
		return devices;
	}

	public static Sensors getSensors(int deviceId,String apikey){
		String url="http://api.yeelink.net/v1.0/device/"+deviceId+"/sensors";
		Sensors sensors=new Sensors();
		String response="";
		try{
			response=HttpRequest.get(url).header("U-ApiKey", apikey).body();
		}catch(Exception e){
			response=NET_ERROR;
		}
		finally{
			if(response.indexOf("ncorrect")!=-1){	//apikey有误
				sensors.state="fail";
			}else if(!NET_ERROR.equals(response)){	//获取成功
				sensors.state="success";
				sensors.senList=jsonToSensors(response);
			}else{
				sensors.state=response;			//网络异常
			}
		}
		return sensors;
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
	 * JSON转换为Device实例
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
	 * JSON转换为Sensor实例
	 */
	private static List<Sensor> jsonToSensors(String jsonStr){
		if(jsonStr.equals("[]")||jsonStr.equals(null)||jsonStr.equals("")){
			return null;
		}
		Type listType = new TypeToken<LinkedList<Sensor>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<Sensor> list = gson.fromJson(jsonStr, listType);
		return list;
	}
	
	public static String changeSwitchState(String apikey,int device_id,int sensor_id,boolean b){
		String response="";
		String order;
		if(b){
			order="{ \"value\":1}";
		}else{
			order="{ \"value\":0}";
		}
		try{
			response=HttpRequest.post("http://api.yeelink.net/v1.0/device/"+device_id+"/sensor/"+sensor_id+"/datapoints").header("U-ApiKey", apikey).send(order).body();
		}catch(Exception e){
			response="error";
		}
		return response;
	}

}
