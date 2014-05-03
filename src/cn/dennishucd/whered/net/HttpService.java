package cn.dennishucd.whered.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.config.WheredContext;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class HttpService extends IntentService {

	public HttpService() {
		super("HttpService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.d(Consts.TAG, "HttpService.onHandleIntent() is invoked.");
		
		//检查是否开启网络上传功能,如果没有则直接返回
		if(!WheredContext.isUpload) {
			return ;
		}
		
		int action = intent.getExtras().getInt("Action");
		String url  = intent.getExtras().getString("URL");
		String body = intent.getExtras().getString("Body");
        
        Http up = new Http(null);
        String result = up.post(url, body);
        
        JSONObject json = JSON.parseObject(result);
        
        //解析返回结果
        switch(action) {
        case Http.ACTION_UPLOAD: //上传
        	if (json != null) {
         		if (json.getIntValue("code") == 0) {
         			//上传成功
         			Log.d(Consts.TAG, "Upload data succeed.");
         		}
         		else {
         			//上传失败
         			Log.e(Consts.TAG, "Upload data failed.");
         		}
         	}
        	
        	break;
        default:
        	//	
        }
	}
}
