package cn.dennishucd.whered.ui;

import cn.dennishucd.whered.R;
import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.config.WheredContext;
import cn.dennishucd.whered.net.Http;
import cn.dennishucd.whered.net.HttpService;
import cn.dennishucd.whered.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	
	private EditText etMobile = null;
	private EditText etPwd    = null;
	
	private Http mHttp = null;
	
	private static Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.login);
		
		Button btnLogin = (Button)findViewById(R.id.btn_login); 
		btnLogin.setOnClickListener(this);
		
		TextView tvRegister = (TextView)findViewById(R.id.tv_register); 
		tvRegister.setOnClickListener(this);
		
		
		etMobile = (EditText) findViewById(R.id.et_mobile_login);
		etPwd    = (EditText) findViewById(R.id.et_pwd_login);
		
		Intent intent = this.getIntent();
		Bundle bd = intent.getExtras();
	    String mobile = null; 
		
		if (bd != null && (mobile=bd.getString("mobile")) != null) {
			etMobile.setText(mobile);
		}
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	@Override
	public void onClick(View view) {
		int id  = view.getId();
		
		Intent intent = new Intent();
		
		switch(id) {
		case R.id.tv_register: 
			intent.setAction(Actions.REGISTER);
			
			startActivity(intent);
			finish();
			break;
		case R.id.btn_login: 
			//1. 输入检查
			String mobile   = etMobile.getText().toString();
			String pwd      = etPwd.getText().toString();

			//手机号格式
			if (!Utils.isMobile(mobile)) {
				Toast.makeText(this, "手机号为空或格式不对!.",
	        			Toast.LENGTH_LONG).show();
			}
			//密码位数
			else if ( (pwd.length() <6)
					||(pwd.length() >20)) {
				Toast.makeText(this, "密码位数不为6-20位之间!.",
	        			Toast.LENGTH_LONG).show();
			}
			
			//2.网络处理 
			intent = new Intent(this, HttpService.class);
			intent.putExtra("Action", Http.ACTION_LOGIN);
			intent.putExtra("URL", Consts.LOGIN_URL);
			
			String body = String.format("{\"phone\":\"%s\",\"password\":\"%s\"}",
					mobile, pwd);
			intent.putExtra("Body", body);
			
			mHttp = new Http(intent);
			mHttp.setHandler(getHandler());
			
			WheredContext.mExecutor.execute(mHttp);
			break;
			
			default:
				break;
		}
		//进入进度界面
		
		//后台登录处理
		
		//转入主界面
	}
	
	public void handleMessage(final Message msg) {
		switch(msg.what) {
		case Http.ACTION_LOGIN:
    		//跳转Activity
			Intent intent = new Intent();
			intent.setAction(Actions.MAIN);
			
			startActivity(intent);
			
			finish();
			break;
		case Http.ACTION_EXCEPT:
    		//异常情况
			Toast.makeText(this, "服务器异常.", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	public Handler getHandler()
    {
        if (mHandler == null)
        {
            mHandler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    if (!isFinishing())
                    {
                        LoginActivity.this.handleMessage(msg);
                    }
                }
            };
        }
        
        return mHandler;
    }
}
