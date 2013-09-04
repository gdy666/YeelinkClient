package me.yeso.yeelink.client;

import me.yeso.yeelink.base.User;
import me.yeso.yeelink.util.DBAdapter;
import me.yeso.yeelink.util.YeelinkAdapter;
import me.yeso.yeelink.util.YeelinkDBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private String username;
	private String passwd;
	private EditText et_username;
	private EditText et_passwd;
	private ImageView iv_login;
	private Intent intent;
	private Bundle bundle;
	private User user=null;
	private Handler handler;
	private Thread loginThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.login_yeelink);
		init_view();
		super.onCreate(savedInstanceState);
	}
	
	private void init_view(){
		et_username=(EditText)findViewById(R.id.et_user);
		et_passwd=(EditText)findViewById(R.id.et_passwd);
		iv_login=(ImageView)findViewById(R.id.iv_login);
		
		iv_login.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				//检测登陆线程是否存在或者在运行，防止重复请求登陆
				if(loginThread==null||loginThread.getState()!=Thread.State.RUNNABLE){
					login();
				}					
			}
		});
		
		intent=new Intent(this,MainActivity.class);
		bundle=new Bundle();
		handler=new LoginHandler();
	}
	
	class LoginHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {	//接受子线程的登陆返回信息
			login_result((String) msg.obj);
		}

		
		
	}
	
	private void login(){
		username=et_username.getText().toString();
		passwd=et_passwd.getText().toString();
		if("".equals(username)||"".equals(passwd)){
			Toast.makeText(LoginActivity.this, getString(R.string.login_warning), Toast.LENGTH_SHORT).show();
		}else{
			//改变登陆按钮状态
			iv_login.setImageDrawable(getResources().getDrawable(R.drawable.login_btn1));
			
			loginThread=new Thread(new Runnable() {		
				@Override
				public void run() {
					String apikey=YeelinkAdapter.getAPIKEY(username, passwd);
					Message msg=new Message();
					msg.obj=apikey;
					handler.sendMessage(msg);
				}
			});
			loginThread.start();
		}
	}
	
	private void login_result(String apikey){
		if(apikey.length()==32){	//成功获取到key
			user=new User(username,passwd,apikey);
			UserRegister(user);	//登陆成功，记录用户信息到数据库
			bundle.putSerializable("user", user);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);	//返回结果
			finish();
		}else if("".equals(apikey)){//用户验证失败
			Toast.makeText(LoginActivity.this, getString(R.string.login_failure), Toast.LENGTH_SHORT).show();
			
		}else{
			Toast.makeText(LoginActivity.this, getString(R.string.login_error_net), Toast.LENGTH_SHORT).show();
		}
		iv_login.setImageDrawable(getResources().getDrawable(R.drawable.login_btn));
	}
	
	/*
	 * 记录用户账号密码APIKEY到数据库
	 */
	private void UserRegister(User user){
		YeelinkDBHelper dbHelper=new YeelinkDBHelper(this, DBAdapter.DB_NAME, null, DBAdapter.VERSION);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		if(DBAdapter.queryUser(db, user)){	//如果表中已经存在用户记录，则更新用户信息
			DBAdapter.UserUpdate(db, user);
			Log.v("用户登陆：","表中已经存在用户记录，更新用户信息");
		}else{//否则插入新记录
			DBAdapter.UserAdd(db, user);
			Log.v("用户登陆:","插入新记录");
		}
	}	

}
