package me.yeso.yeelink.base;

public class User {
	private String userName;	//用户账号
	private String password;	//用户密码
	private String apikey;		//用户APIKEY
	
	public User(String userName, String password, String apikey) {
		this.userName = userName;
		this.password = password;
		this.apikey = apikey;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
}
