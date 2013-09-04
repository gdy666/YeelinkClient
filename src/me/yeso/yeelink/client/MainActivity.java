package me.yeso.yeelink.client;

import java.util.List;

import me.yeso.yeelink.base.Devices;
import me.yeso.yeelink.base.User;
import me.yeso.yeelink.common.AppConf;
import me.yeso.yeelink.util.DBAdapter;
import me.yeso.yeelink.util.DevLsAdapter;
import me.yeso.yeelink.util.HttpRequest;
import me.yeso.yeelink.util.ListViewDropFlush;
import me.yeso.yeelink.util.ListViewDropFlush.OnRefreshListener;
import me.yeso.yeelink.util.YeelinkAdapter;
import me.yeso.yeelink.util.YeelinkDBHelper;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity implements View.OnClickListener{
	private static final int MSG_MENU_ANI_0=0x00;
	private static final int MSG_MENU_ANI_1=0x01;
	private static final int FLUSH_LIST=0x02;
	private SlidingMenu left_menu;
	private Toast toast;		//用来提示用户再次按返回键时退出
	private ImageView iv_setting;
	private Button bn_about;
	private Button bn_login_out;
	private TextView tv_username;
	private ImageView iv_loginState;
	private User currentUser;	//当前用户
	private YeelinkDBHelper dbHelper;
	private SQLiteDatabase db;
	private ListViewDropFlush lv_dev;
	private MainHandler handler;
	private Thread getDataThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper=new YeelinkDBHelper(this, DBAdapter.DB_NAME, null, DBAdapter.VERSION);
		db=dbHelper.getWritableDatabase();
		setContentView(R.layout.main);
		init_menu();
		init_view();
		AppConf.InitSharePreferences(MainActivity.this);	//初始化并读取配置文件
		checkUser();
	}
	

	
	private void init_view(){
		toast=Toast.makeText(MainActivity.this, getString(R.string.exit_toast), Toast.LENGTH_SHORT);
		lv_dev=(ListViewDropFlush)findViewById(R.id.lv_dev);
		handler=new MainHandler();
		
		lv_dev.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getDevicesData();
			}
		});
	}
	
	class MainHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch(msg.arg1){
				case FLUSH_LIST:	//刷新列表
					flushDevList((Devices) msg.obj);
					break;
			}
		}
		
	}
	
	/*
	 * 刷新设备列表，刷新前先验证
	 */
	private void flushDevList(Devices dev){
		if(dev.state.equals("success")){	//获取数据成功，刷新列表
			DevLsAdapter ada=new DevLsAdapter(this,dev.devList);
			lv_dev.setAdapter(ada);
			lv_dev.onRefreshComplete();
		}else if("fail".equals(dev.state)){
			Toast.makeText(MainActivity.this, "获取设备列表数据失败，请尝试重新登陆或重新获取APIKEY.", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(MainActivity.this, "网络异常,获取设备列表数据失败", Toast.LENGTH_SHORT).show();
		}
	}
	/*
	 * 初始化菜单
	 */
	private void init_menu(){
		left_menu=new SlidingMenu(MainActivity.this);
		left_menu.setMode(SlidingMenu.LEFT);
		left_menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		left_menu.setShadowWidthRes(R.dimen.shadow_width);
		left_menu.setShadowDrawable(R.drawable.shadow);
		left_menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		left_menu.setFadeDegree(0.35f);
		left_menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		left_menu.setMenu(R.layout.menu);
		left_menu.setOnClosedListener(new OnClosedListener() {
			
			@Override
			public void onClosed() {
				iv_setting.setVisibility(View.INVISIBLE);
			}
		});
		left_menu.setOnOpenedListener(new OnOpenedListener() {
			
			@Override
			public void onOpened() {
				AlphaAnimation aa = new AlphaAnimation(0.0f,1.0f);
				aa.setDuration(800);
				iv_setting.setVisibility(View.VISIBLE);
				iv_setting.setAnimation(aa);
			}
		});
		
		iv_setting=(ImageView)findViewById(R.id.iv_setting);
		bn_about=(Button)findViewById(R.id.bn_about);
		bn_login_out=(Button)findViewById(R.id.bn_login_out);
		tv_username=(TextView)findViewById(R.id.tv_username);
		iv_loginState=(ImageView)findViewById(R.id.iv_loginState);
		
		bn_about.setOnClickListener(new MenuLister());
		bn_login_out.setOnClickListener(new MenuLister());
	}
	

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_MENU:	//按下菜单键时
				if(left_menu.isMenuShowing()){
					left_menu.showContent();
					return true;
					
				}else{
					left_menu.showMenu();
					return true;
				}
			case KeyEvent.KEYCODE_BACK://按下返回键时
				if(left_menu.isMenuShowing()){
					left_menu.showContent();
					return true;
				}
				if(!toast.getView().isShown()){
					toast.show();
					return true;
				}else{
					toast.cancel();
				}
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MenuLister implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.bn_about:
					Toast.makeText(MainActivity.this, "关于", Toast.LENGTH_SHORT).show();
					break;
				case R.id.bn_login_out:
					//Toast.makeText(MainActivity.this, "登录登出", Toast.LENGTH_SHORT).show();
					if(bn_login_out.getText().toString().equals("登录")){
						//Toast.makeText(MainActivity.this, "登录", Toast.LENGTH_SHORT).show();
						Login();
					}else{
						AppConf.WriteCurrentUser("");	//注销时用来清楚配置文件中保存的当前用户
						setViewNoLogin();
						Login();
					}
					break;
			}
		}
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		}
		
	}
	
	/*
	 * 检测配置文件中包存的当前用户
	 */
	private void checkUser(){
		if(!"".equals(AppConf.currentUser)){	//配置文件中保存有用户信息
			currentUser=DBAdapter.getUser(db, AppConf.currentUser);
			if(currentUser!=null){
				setViewLogin();
				return;
			}
			
		}
		//配置文件中未保存有配置信息,或者数据库中未有用户信息
			setViewNoLogin();
		
	}
	
	/*
	 * 将Menu中的控件设置为未登录状态
	 */
	private void setViewNoLogin(){
		tv_username.setText(getString(R.string.none_currentUser));
		bn_login_out.setText(getString(R.string.login));
		iv_loginState.setImageDrawable(getResources().getDrawable(R.drawable.login_state_0));
		left_menu.showMenu();
		Toast.makeText(MainActivity.this, "请登录", Toast.LENGTH_SHORT).show();
	}
	
	/*
	 * 将Menu中的控件设置为登录状态
	 */
	private void setViewLogin(){
		tv_username.setText(AppConf.currentUser);
		bn_login_out.setText(getString(R.string.logout));
		iv_loginState.setImageDrawable(getResources().getDrawable(R.drawable.login_state_1));
		//获取数据
		getDevicesData();
	}
	
	/*
	 * 从网络中获取设备数据
	 */
	private void getDevicesData(){
		//判断一下，防止出现多条相同获取数据的功能线程的情况
		if(getDataThread==null||getDataThread.getState()!=Thread.State.RUNNABLE){
		getDataThread=new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg=new Message();
							msg.arg1=FLUSH_LIST;
							msg.obj=YeelinkAdapter.getDevices("yeso", "6edcfb225b401db3bb165aa4c25a4d19");
							handler.sendMessage(msg);
						}
					});
		getDataThread.start();
		}
		
	}

	/*
	 * 跳转到登录页面
	 */
	private void Login(){
		Intent intent=new Intent(MainActivity.this,LoginActivity.class);
		startActivityForResult(intent,0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(resultCode){
		case RESULT_OK:
				Bundle bundle=data.getExtras();
				User user=(User) bundle.getSerializable("user");
				if(user!=null){
					currentUser=user;
					AppConf.WriteCurrentUser(user.getUserName());
					setViewLogin();
					Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					left_menu.showContent();
				}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
