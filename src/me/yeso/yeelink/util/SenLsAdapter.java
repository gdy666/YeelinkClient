package me.yeso.yeelink.util;

import java.util.List;

import me.yeso.yeelink.base.Sensor;
import me.yeso.yeelink.client.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SenLsAdapter extends BaseAdapter {

	private Context context;
	private List<Sensor> list;
	
	
	public SenLsAdapter(Context context, List<Sensor> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Sensor sensor=list.get(position);
		switch(sensor.getType()){//根据不同类型显示不同
			case 0://数值型传感器
				convertView=setNumview(sensor,convertView);
				break;
			case 5://开关型传感器
				convertView=setSwitchview(sensor,convertView);
				break;
			case 6://GPS
				convertView=setGPSview(sensor,convertView);
				break;
			case 8://泛型传感器
				convertView=setGenview(sensor,convertView);
				break;
			case 9://图像传感器
				convertView=setPicview(sensor,convertView);
				break;
			case 10://微博抓取器
				convertView=setWBview(sensor,convertView);
				break;
			default:
				convertView=setWBview(sensor,convertView);
				break;
		}
		return convertView;
	}
	
	//数值型传感器VIew
	private View setNumview(Sensor sensor,View convertView){
		convertView=View.inflate(context, R.layout.sensor_normal, null);
		RelativeLayout itemt_layout=(RelativeLayout)convertView.findViewById(R.id.item_layout);
		ImageView list_image=(ImageView)convertView.findViewById(R.id.list_image);
		TextView main_text=(TextView)convertView.findViewById(R.id.main_text);
		TextView about_text=(TextView)convertView.findViewById(R.id.about_text);
		
		itemt_layout.setBackground(context.getResources().getDrawable(R.drawable.item_bg0));
		list_image.setImageDrawable(context.getResources().getDrawable(R.drawable.charts_icon));
		main_text.setText(sensor.getTitle());
		about_text.setText(sensor.getAbout());
		return convertView;
	}
	
	//开关传感器VIew
	private View setSwitchview(Sensor sensor,View convertView){
		convertView=View.inflate(context, R.layout.sensor_switch, null);
		TextView main_text=(TextView)convertView.findViewById(R.id.main_text);
		//TextView about_text=(TextView)convertView.findViewById(R.id.about_text);	
		main_text.setText(sensor.getTitle());
	//	about_text.setText(sensor.getAbout());
		return convertView;
	}
	
	//GPS传感器VIew
	private View setGPSview(Sensor sensor,View convertView){
		convertView=View.inflate(context, R.layout.sensor_normal, null);
		RelativeLayout itemt_layout=(RelativeLayout)convertView.findViewById(R.id.item_layout);
		ImageView list_image=(ImageView)convertView.findViewById(R.id.list_image);
		TextView main_text=(TextView)convertView.findViewById(R.id.main_text);
		TextView about_text=(TextView)convertView.findViewById(R.id.about_text);
		
		itemt_layout.setBackground(context.getResources().getDrawable(R.drawable.item_bg0));
		list_image.setImageDrawable(context.getResources().getDrawable(R.drawable.gps_icon));
		main_text.setText(sensor.getTitle());
		about_text.setText(sensor.getAbout());
		return convertView;
	}
	
	//泛型传感器VIew
	private View setGenview(Sensor sensor,View convertView){
		convertView=View.inflate(context, R.layout.sensor_normal, null);
		RelativeLayout itemt_layout=(RelativeLayout)convertView.findViewById(R.id.item_layout);
		ImageView list_image=(ImageView)convertView.findViewById(R.id.list_image);
		TextView main_text=(TextView)convertView.findViewById(R.id.main_text);
		TextView about_text=(TextView)convertView.findViewById(R.id.about_text);
		
		itemt_layout.setBackground(context.getResources().getDrawable(R.drawable.item_bg0));
		list_image.setImageDrawable(context.getResources().getDrawable(R.drawable.keyval));
		main_text.setText(sensor.getTitle());
		about_text.setText(sensor.getAbout());
		return convertView;
	}

	//图像传感器VIew
	private View setPicview(Sensor sensor,View convertView){
		convertView=View.inflate(context, R.layout.sensor_normal, null);
		RelativeLayout itemt_layout=(RelativeLayout)convertView.findViewById(R.id.item_layout);
		ImageView list_image=(ImageView)convertView.findViewById(R.id.list_image);
		TextView main_text=(TextView)convertView.findViewById(R.id.main_text);
		TextView about_text=(TextView)convertView.findViewById(R.id.about_text);
		
		itemt_layout.setBackground(context.getResources().getDrawable(R.drawable.item_bg0));
		list_image.setImageDrawable(context.getResources().getDrawable(R.drawable.pic_icon));
		main_text.setText(sensor.getTitle());
		about_text.setText(sensor.getAbout());
		return convertView;
	}
	
	//微博传感器VIew
	private View setWBview(Sensor sensor,View convertView){
		convertView=View.inflate(context, R.layout.sensor_normal, null);
		RelativeLayout itemt_layout=(RelativeLayout)convertView.findViewById(R.id.item_layout);
		ImageView list_image=(ImageView)convertView.findViewById(R.id.list_image);
		TextView main_text=(TextView)convertView.findViewById(R.id.main_text);
		TextView about_text=(TextView)convertView.findViewById(R.id.about_text);
		
		itemt_layout.setBackground(context.getResources().getDrawable(R.drawable.item_bg0));
		list_image.setImageDrawable(context.getResources().getDrawable(R.drawable.bired));
		main_text.setText(sensor.getTitle());
		about_text.setText(sensor.getAbout());
		return convertView;
	}
}
