package cn.dennishucd.whered.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Process;
import cn.dennishucd.whered.R;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabHostActivity extends TabActivity {
	
	private TabHost tabHost = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);

		initTab();
	}
	
	@Override
	public void onBackPressed() {
		//处理程序退出的事情
		
		super.onBackPressed();
	}

	private void initTab() {
		tabHost = getTabHost();
		tabHost.setup();
		tabHost.bringToFront();
		Resources ressources = getResources();
		
		 Intent mapIntent = new Intent().setClass(this, MapActivity.class);
	     TabSpec mapTS = tabHost.newTabSpec("Map")
	                .setIndicator("我的位置", ressources.getDrawable(R.drawable.ic_launcher))
	                .setContent(mapIntent);
	     
	     Intent friendsIntent = new Intent().setClass(this, FriendsActivity.class);
	     TabSpec friendsTS = tabHost.newTabSpec("Friends")
	                .setIndicator("好友", ressources.getDrawable(R.drawable.ic_launcher))
	                .setContent(friendsIntent);
	     Intent settingsIntent = new Intent().setClass(this, SettingsActivity.class);
	     TabSpec settingsTS = tabHost.newTabSpec("Setting")
	                .setIndicator("设置", ressources.getDrawable(R.drawable.ic_launcher))
	                .setContent(settingsIntent);
	         
		tabHost.addTab(mapTS);
		tabHost.addTab(friendsTS);
		tabHost.addTab(settingsTS);
	}
}
