package cn.dennishucd.whered.ui;

import cn.dennishucd.whered.R;
import cn.dennishucd.whered.config.Consts;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FriendsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//String loginBody = "{\"phone\":\"13880000010\",\"password\":\"111111\"}";
		
		setContentView(R.layout.friends);
		
		super.onCreate(savedInstanceState);
	}
}
