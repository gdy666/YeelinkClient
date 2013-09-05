package me.yeso.yeelink.base;

/*
 * 传感器类
 * type:
 * 数值型传感器		0
 * 开关型传感器		5
 * GPS				6		*
 * 泛型传感器		8		*
 * 图像传感器		9		*
 * 微博抓取器		10		*
 */
public class Sensor {
	private int id;					//传感器Id
	private String title;				//传感器名称
	private String about;			//传感器描述
	private int type;				//传感器类型
	private String last_data;		//最新数据
	private String last_data_gen;	//最新情报（微博提醒器），GPS。。。
	
	public Sensor(int id, String title, String about, int type, String last_data,
			String last_data_gen) {
		super();
		this.id = id;
		this.title = title;
		this.about = about;
		this.type = type;
		this.last_data = last_data;
	//	this.last_data_gen = last_data_gen;
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id:"+id+" title:"+title+" about:"+about+" type:"+type+" last_data:"+last_data+
				" last_data_gen:"+last_data_gen;
	}
}
