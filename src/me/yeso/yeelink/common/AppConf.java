package me.yeso.yeelink.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppConf {
	private static SharedPreferences preferences;
	private static Editor editor=null;
	public static String currentUser="";	//当前登录的用户
	
	public static void InitSharePreferences(Context context){
		if(editor==null){
			preferences=PreferenceManager.getDefaultSharedPreferences(context);
			editor=preferences.edit();
			ReadConf();
		}
	}
	
	//读取配置文件
	public static void ReadConf(){
		ReadCurrentUser();
	}
	
	/*
	 * 更改当前登录用户记录
	 */
	public static void WriteCurrentUser(String currentUser){
		AppConf.currentUser=currentUser;
		editor.putString("currentUser",currentUser);
		editor.commit();
	}
	
	/*
	 * 读取最后的登录用户
	 */
	public static void ReadCurrentUser(){
		currentUser=preferences.getString("currentUser", "");
	}
	
}
