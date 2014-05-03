package cn.dennishucd.whered.ui;

import java.io.UnsupportedEncodingException;

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
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	
	private EditText etMobile = null;
	private EditText etPassword = null;
	private EditText etPasswordAgain = null;
	private EditText etNickName = null;
	
	private Http mHttp = null;
	
	private static Handler mHandler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.register);
		
		ImageButton imgBtnBack = (ImageButton)findViewById(R.id.img_btn_back); 
		imgBtnBack.setOnClickListener(this);
		
		Button btnRegister = (Button)findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);
		
		etMobile        = (EditText) findViewById(R.id.et_mobile);
		etPassword      = (EditText) findViewById(R.id.et_password);
		etPasswordAgain = (EditText) findViewById(R.id.et_password_again);
		etNickName      = (EditText) findViewById(R.id.et_nickname);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		Log.d(Consts.TAG, "Onclick is invoked.");
		int id  = view.getId();
		
		Intent intent = null;
		
		switch(id) {
		case R.id.img_btn_back: 
			intent = new Intent(Actions.LOGIN);
			
			startActivity(intent);
			finish();
			break;
		case R.id.btn_register: 
			
			Log.d(Consts.TAG, "btn_register is invoked.");
			
			//1. 输入检查
			String mobile   = etMobile.getText().toString();
			String pwd      = etPassword.getText().toString();
			String nickName = "";
			try {
				nickName = new String(etNickName.getText().toString().getBytes(),
						"utf-8");
			} catch (UnsupportedEncodingException e) {
				Toast.makeText(this, "编码格式不对!.",
	        			Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			
			//手机号格式
			if (!Utils.isMobile(mobile)) {
				Toast.makeText(this, "手机号为空或格式不对!.",
	        			Toast.LENGTH_LONG).show();
				
				return ;
			}
			//密码位数
			else if ( (pwd.length() <6)
					||(pwd.length() >20)) {
				Toast.makeText(this, "密码位数不为6-20位之间!.",
	        			Toast.LENGTH_LONG).show();
				return ;
			}
			//两次输入的密码不一致
			else if (!etPasswordAgain.getText().toString().equals(pwd)) {
				Toast.makeText(this, "两次输入的密码不一致!.",
	        			Toast.LENGTH_LONG).show();
				return ;
			}
			//昵称
			else if (etNickName.getText().toString().length() ==0 ) {
				Toast.makeText(this, "昵称不能为空!.",
	        			Toast.LENGTH_LONG).show();
				return ;
			}
			
			//2.网络处理 
			intent = new Intent(this, HttpService.class);
			intent.putExtra("Action", Http.ACTION_REGISTER);
			intent.putExtra("URL", Consts.REGISTER_URL);
			
			String body = String.format("{\"phone\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
					mobile, pwd, nickName);
			intent.putExtra("Body", body);
			
			mHttp = new Http(intent);
			mHttp.setHandler(getHandler());
			
			WheredContext.mExecutor.execute(mHttp);
			break;
			
		default:
			break;
		}
	}

	public void handleMessage(Message msg) {
		switch(msg.what) {
		case Http.ACTION_REGISTER:
			final int res = ((Integer)msg.obj).intValue();
			String tip = null; 
			if (res == 0) {
				tip = "注册成功";
			}
			else {
				tip = "注册失败";
			}
			//提示注册成功
			AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("注册")
				.setMessage(tip)
				.setIcon(R.drawable.ic_launcher)
				.setPositiveButton("确定",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {
                                    	
                                    	if (res == 0) {
                                    		//跳转Activity
                                			Intent intent = new Intent();
                                			intent.setAction(Actions.LOGIN);
                                			intent.putExtra("mobile", etMobile.getText().toString());
                                			
                                			startActivity(intent);
                                			
                                			finish();
                                    	}
                                    }
                                 }).show();
			
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
                        RegisterActivity.this.handleMessage(msg);
                    }
                }
            };
        }
        
        return mHandler;
    }
}
