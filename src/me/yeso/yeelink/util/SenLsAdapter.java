package me.yeso.yeelink.util;

import java.util.List;

import me.yeso.yeelink.base.Sensor;
import me.yeso.yeelink.client.MainActivity;
import me.yeso.yeelink.client.R;
import me.yeso.yeelink.client.SensorsActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SenLsAdapter extends BaseAdapter {
	private static final int SWITCH_RES=0x00;
	private Context context;
	private List<Sensor> list;
	private Dialog dialog;
//	public static int deviceId;
//	public static String apikey;
	private Thread thread;
	private Handler handler;
	private CheckBox currentCB;		//当前点击的CB
	
	public SenLsAdapter(Context context, List<Sensor> list) {
		super();
		this.context = context;
		this.list = list;
	//	this.deviceId=device_id;
	//	this.apikey=apikey;
		
		handler=new MyHandler();
		dialog = new Dialog(context,R.style.LodingDialog);  
	    dialog.setContentView(R.layout.loading);
	    dialog.setCanceledOnTouchOutside(false);
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
	private View setSwitchview(final Sensor sensor,View convertView){
		convertView=View.inflate(context, R.layout.sensor_switch, null);
		TextView main_text=(TextView)convertView.findViewById(R.id.main_text);
		TextView about_text=(TextView)convertView.findViewById(R.id.about_text);
		final CheckBox switch_bn=(CheckBox)convertView.findViewById(R.id.switch_btn);
		main_text.setText(sensor.getTitle());
		about_text.setText(sensor.getAbout());
		
		if(sensor.getLast_data()!=null&&sensor.getLast_data().equals("1")){
			switch_bn.setChecked(true);
		}else{
			switch_bn.setChecked(false);
		}
		
		switch_bn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentCB=(CheckBox)v;
				boolean state=currentCB.isChecked();	//发送的开关指令
				dialog.show();
				thread=new Thread(new switchRunnable(sensor.getId(), state));
				thread.start();
				currentCB.setChecked(!state);	//开关先恢复，等命令成功执行后再改变开关状态
			}
		});
		return convertView;
	}
	
	/*
	 * 改变开关状态
	 */
	private void changeSwitchState(){
		if(currentCB.isChecked()){
			currentCB.setChecked(false);
		}else{
			currentCB.setChecked(true);
		}
	}
	class switchRunnable implements Runnable{
		private int sensorId;
		private boolean state;
		public switchRunnable(int sensorId,boolean state){
			this.sensorId=sensorId;
			this.state=state;
		}
		@Override
		public void run() {
			String result=YeelinkAdapter.changeSwitchState(SensorsActivity.apikey,SensorsActivity.dev.getId(), sensorId, state);
			Message msg=new Message();
			msg.arg1=SWITCH_RES;
			msg.obj=result;
			handler.sendMessage(msg);
			
		}
		
	};
	
	class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch(msg.arg1){
				case SWITCH_RES:
					dealSwitchResult((String)msg.obj);
					break;
			}
		}
		
	}
	
	private void dealSwitchResult(String result){
		if(result.equals("")){		
			changeSwitchState();		
		}else if(result.equals("error")){
			Toast.makeText(context, "网络出错，请重试", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "APIKEY有误", Toast.LENGTH_SHORT).show();
		}
		dialog.cancel();
		currentCB=null;
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
