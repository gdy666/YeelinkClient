package me.yeso.yeelink.util;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import me.yeso.yeelink.base.User;

public class DBAdapter {
	public static final String DB_NAME="YeLinkDB";
	public static final int VERSION=1;
	
	/*
	 * 删除一个用户的记录
	 */
	public static long UserDelete(SQLiteDatabase db,User user){
		return db.delete("user", "username=?", new String[]{user.getUserName()});
	}
	
	/*
	 * 增加一个用户的记录
	 */
	public static long UserAdd(SQLiteDatabase db,User user){
		ContentValues values=new ContentValues();
		values.put("username", user.getUserName());
		values.put("password",user.getPassword());
		values.put("apikey",user.getApikey());
		return db.insert("user", null,values);
	}
	
	/*
	 * 更新一条用户记录
	 */
	public static long UserUpdate(SQLiteDatabase db,User user){
		ContentValues values=new ContentValues();
		values.put("password",user.getPassword());
		values.put("apikey",user.getApikey());
		return db.update("user", values, "username=?", new String[]{user.getUserName()});
	}
	
	/*
	 * 查询所有用户信息
	 */
	public static List<User> queryAllUser(SQLiteDatabase db){
		Cursor cursor=db.rawQuery("select username,password,apikey from user", null);
		List<User> list=new LinkedList<User>();
		while(cursor.moveToNext()){
			list.add(new User(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
		}
		return list;
	}
	
	/*
	 * 根据用户名查询用户是否存在表中
	 */
	public static boolean queryUser(SQLiteDatabase db,User user){
		Cursor cursor=db.rawQuery("select * from user where username='"+user.getUserName()+"'", null);
		if(cursor.moveToNext()){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * 根据用户名获取从数据库中获取一个用户实例
	 */
	public static User getUser(SQLiteDatabase db,String userName){
		User user=null;
		Cursor cursor=db.rawQuery("select * from user where username='"+userName+"'", null);
		if(cursor.moveToNext()){
			user=new User(cursor.getString(1),cursor.getString(2),cursor.getString(3));
		}
		return user;
	}
}
