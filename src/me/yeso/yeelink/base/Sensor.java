package me.yeso.yeelink.base;

/*
 * 传感器类
 */
public class Sensor {
	private int id;					//传感器Id
	private int title;				//传感器名称
	private String about;			//传感器描述
	private int type;				//传感器类型
	private String last_data;		//最新数据
	private String last_data_gen;	//最新情报（微博提醒器）
	
	public Sensor(int id, int title, String about, int type, String last_data,
			String last_data_gen) {
		super();
		this.id = id;
		this.title = title;
		this.about = about;
		this.type = type;
		this.last_data = last_data;
		this.last_data_gen = last_data_gen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLast_data() {
		return last_data;
	}

	public void setLast_data(String last_data) {
		this.last_data = last_data;
	}

	public String getLast_data_gen() {
		return last_data_gen;
	}

	public void setLast_data_gen(String last_data_gen) {
		this.last_data_gen = last_data_gen;
	}
	
	
}
