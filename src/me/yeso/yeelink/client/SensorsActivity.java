package me.yeso.yeelink.client;

import me.yeso.yeelink.base.Device;
import me.yeso.yeelink.base.Devices;
import me.yeso.yeelink.base.Sensors;
import me.yeso.yeelink.util.ListViewDropFlush;
import me.yeso.yeelink.util.ListViewDropFlush.OnRefreshListener;
import me.yeso.yeelink.util.SenLsAdapter;
import me.yeso.yeelink.util.YeelinkAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SensorsActivity extends Activity {
	private static final int FLUSH_LIST=0x00;
	public static String apikey;
	public static Device dev;
	private ListViewDropFlush sensorsList;
	private TextView nodata;
	private Handler sensorHandler;
	private Thread thread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sersors_show);
		getDataByIntent();
		initView();
	}
	
	private void getDataByIntent(){
		Bundle bundle=this.getIntent().getBundleExtra("Bundle");
		apikey=bundle.getString("apikey");
		dev=(Device) bundle.getSerializable("device");
	}
	
	private void initView(){
		TextView tv_title=(TextView)findViewById(R.id.sensors_title);
		sensorsList=(ListViewDropFlush)findViewById(R.id.lv_sensors);
		nodata=(TextView)findViewById(R.id.tv_sensorslistnodata);
		
		tv_title.setText(dev.getTitle());
		sensorHandler=new SensorHandler();
		getSensorsData();
		
		sensorsList.setonRefreshListener(new OnRefreshListener() {	
			@Override
			public void onRefresh() {
				getSensorsData();
			}
		});
	}
	
	class SensorHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.arg1){
				case FLUSH_LIST:
					flushList((Sensors) msg.obj);
					break;
			}
		}
	}
	
	private void flushList(Sensors sen){
		if(sen.state.equals("success")){//获取成功
			SenLsAdapter ada=new SenLsAdapter(this, sen.senList);
			sensorsList.setAdapter(ada);
		}else if(sen.state.equals("fail")){	//apikey有误
			Toast.makeText(this, "获取传感器列表数据失败，请尝试重新登陆或重新获取APIKEY.", Toast.LENGTH_SHORT).show();
		}else{//网络异常
			Toast.makeText(this, "网络异常,获取设备列表数据失败", Toast.LENGTH_SHORT).show();
		}
		showNodata(sen);
		sensorsList.onRefreshComplete();
	}
	
	private void showNodata(Sensors sen){
		if(!sen.state.equals("success")||sen.senList.size()==0){
			nodata.setVisibility(View.VISIBLE);
			sensorsList.setVisibility(View.GONE);
		}else{
			nodata.setVisibility(View.GONE);
			sensorsList.setVisibility(View.VISIBLE);
		}
	}
	
	private void getSensorsData(){
		//判断一下，防止出现多条相同获取数据的功能线程的情况
		if(thread==null||thread.getState()!=Thread.State.RUNNABLE){
			thread=	new Thread(new Runnable() {
					
					@Override
					public void run() {
						Message msg=new Message();
						msg.arg1=FLUSH_LIST;
						msg.obj=YeelinkAdapter.getSensors(dev.getId(), apikey);
						sensorHandler.sendMessage(msg);
					}
				});
			thread.start();
		}
	}

}
