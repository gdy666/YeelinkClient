package me.yeso.yeelink.client;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	private SlidingMenu left_menu;
	private Toast toast;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init_menu();
		temp();
		init_view();
	}
	
	private void temp(){
		Button bn=(Button)findViewById(R.id.bn_login);
		bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/*
	 * 
	 */
	private void init_view(){
		toast=Toast.makeText(MainActivity.this, getString(R.string.exit_toast), Toast.LENGTH_SHORT);
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
	//	return ture;
	}
	

}
