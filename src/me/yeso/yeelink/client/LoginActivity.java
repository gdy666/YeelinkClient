package me.yeso.yeelink.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
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
		//	Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
			int index;
			index=response.indexOf("apikey");
			index+=9;
			apikey=response.substring(index, index+32);//获取到APIKEY
		}else{//登陆失败
			Toast.makeText(LoginActivity.this, getString(R.string.login_failure), Toast.LENGTH_SHORT).show();
		}
	}
}
