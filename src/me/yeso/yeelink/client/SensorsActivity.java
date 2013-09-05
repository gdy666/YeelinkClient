package me.yeso.yeelink.client;

import me.yeso.yeelink.base.Device;
import me.yeso.yeelink.base.Sensors;
import me.yeso.yeelink.util.YeelinkAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SensorsActivity extends Activity {
	private static final int FLUSH_VIEW=0x00;
	private String apikey;
	private Device dev;
	private ListView sensorsList;
	private TextView nodata;
	private Handler sensorHandler;
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
		sensorsList=(ListView)findViewById(R.id.lv_sensors);
		nodata=(TextView)findViewById(R.id.tv_sensorslistnodata);
		
		tv_title.setText(dev.getTitle());
		sensorHandler=new SensorHandler();
		test();
	}
	
	class SensorHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.arg1){
				case FLUSH_VIEW:
					break;
			}
		}
	}
	
	private void test(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Sensors sensors=YeelinkAdapter.getSensors(dev.getId(), apikey);
				int size=sensors.senList.size();
				for(int i=0;i<size;i++){
					System.out.println(sensors.senList.get(i));
				}
				System.out.println("sensors size is "+size);
				
			}
		}).start();
	}

}
