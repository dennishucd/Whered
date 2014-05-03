package cn.dennishucd.whered.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.config.WheredContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Http implements Runnable {
	
	public static final int  ACTION_REGISTER = 1;
	public static final int  ACTION_LOGIN    = 2;
	public static final int  ACTION_UPLOAD   = 3;
	public static final int  ACTION_EXCEPT   = 4;
	
	private Intent mIntent = null;
	private Handler mHandler = null;
		
	public Http(Intent intent) {
		mIntent = intent;
	}
	
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	//用于后台服务
	public String post(String url, String body) {
		URL u = null;
		HttpURLConnection urlConn = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			u = new URL(url);
			
			urlConn = (HttpURLConnection) u.openConnection();
			
			urlConn.setRequestMethod("POST");
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-type", "application/json");
			urlConn.setRequestProperty("Charset", "utf-8");
			
			urlConn.connect();
			
			DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
			//dop.writeBytes("{\"phone\":\"13880000010\",\"password\":\"111111\",\"name\":\"test\"}");
			dop.write(body.getBytes("utf-8"));
			dop.flush();
			dop.close();
			
			//接收
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String readLine = null;
			
			while((readLine=br.readLine()) != null) {
				sb.append(readLine);
			}
			
			br.close();
			urlConn.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	//用于前台处理
	@Override
	public void run() {
		//网络处理
		int action  = mIntent.getExtras().getInt("Action");
		String url  = mIntent.getExtras().getString("URL");
		String body = mIntent.getExtras().getString("Body");
        
        String result = post(url, body);
        
        JSONObject json = JSON.parseObject(result);
        
        Message msg = new Message();
        msg.what = ACTION_EXCEPT;
         
        //解析返回结果
        switch(action) {
        case Http.ACTION_REGISTER: //注册
        	if (json != null) {
        		msg.what = ACTION_REGISTER;
         		if (json.getIntValue("code") == 0) {
         			//
         			msg.obj = 0;
         		}
         		else {
         			//注册失败
         			msg.obj = -1;
         			Log.e(Consts.TAG, "Register failed.");
         		}
         	}
        	
        	break;
        case Http.ACTION_LOGIN: //登录: 将token取出存入WheredContext
         	if (json != null) {
         		msg.what = ACTION_LOGIN;
         		if (json.getIntValue("code") == 0) {
         			WheredContext.token = (String)json.get("token");
         			msg.obj = 0;
         		}
         		else {
         			//登录失败
         			msg.obj = -1;
         			Log.e("Whered", "Login failed.");
         		}
         	}
         	
        	break;
        default:
        	//	
        }
        
        mHandler.sendMessage(msg);
	}
}
