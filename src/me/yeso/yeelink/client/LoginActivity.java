package me.yeso.yeelink.client;

import me.yeso.yeelink.base.User;
import me.yeso.yeelink.util.DBAdapter;
import me.yeso.yeelink.util.YeelinkDBHelper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private static String LOGIN_API="http://www.yeelink.net/mobile/login";
	private String username;
	private String passwd;
	private String apikey;
	private EditText et_username;
	private EditText et_passwd;
	private ImageView iv_login;
	private Intent intent;
	private Bundle bundle;
	private User user=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.login);
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
				login();
			}
		});
		
		intent=new Intent(this,MainActivity.class);
		bundle=new Bundle();
	}
	
	private void login(){
		username=et_username.getText().toString();
		passwd=et_passwd.getText().toString();
		if("".equals(username)||"".equals(passwd)){
			Toast.makeText(LoginActivity.this, getString(R.string.login_warning), Toast.LENGTH_SHORT).show();
		}else{
			//改变登陆按钮状态
			iv_login.setImageDrawable(getResources().getDrawable(R.drawable.login_btn1));
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("login", username);
			params.put("password", passwd);
			client.get(LOGIN_API, params, new AsyncHttpResponseHandler() {
	            @Override
	            public void onSuccess(String response) {
	            	login_result(response);	//登陆请求得到服务器响应
	            }

	            @Override
				public void onFailure(Throwable error, String content) {
	            	//登陆超时或网络有问题
	            	Toast.makeText(LoginActivity.this, getString(R.string.login_error_net), Toast.LENGTH_SHORT).show();
					super.onFailure(error, content);
				}
	            
				@Override
				public void onFinish() {
					super.onFinish();
					//改变登陆按钮状态
					iv_login.setImageDrawable(getResources().getDrawable(R.drawable.login_btn));
				}
		            
	        });
		}
	}
	
	private void login_result(String response){
		if(response.indexOf("success")!=-1){	//登陆成功
			int index;
			index=response.indexOf("apikey");
			index+=9;
			apikey=response.substring(index, index+32);//获取到APIKEY
			user=new User(username,passwd,apikey);
			UserRegister(user);	//登陆成功，记录用户信息到数据库
			bundle.putSerializable("user", user);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);	//返回结果
			finish();
		}else{//登陆失败
			Toast.makeText(LoginActivity.this, getString(R.string.login_failure), Toast.LENGTH_SHORT).show();
		}
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
