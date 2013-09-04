package me.yeso.yeelink.base;

/*
 * 设备类
 */
public class Device {
	private int id;				//设备ID
	private String title;		//设备名称
	private String about;		//设备描述
	
	public Device(int id, String title, String about) {
		this.id = id;
		this.title = title;
		this.about = about;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}

}
