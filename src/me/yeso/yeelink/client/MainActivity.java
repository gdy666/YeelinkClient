package me.yeso.yeelink.client;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.view.KeyEvent;
import android.app.Activity;

public class MainActivity extends Activity {
	private SlidingMenu left_menu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init_menu();
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
				}else{
					left_menu.showMenu();
				}
				break;
			case KeyEvent.KEYCODE_BACK://按下返回键时
				if(left_menu.isMenuShowing()){
					left_menu.showContent();
				}else{
					super.onBackPressed();
				}
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
