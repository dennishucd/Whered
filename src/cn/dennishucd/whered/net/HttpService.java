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
		
		//����Ƿ��������ϴ�����,���û����ֱ�ӷ���
		if(!WheredContext.isUpload) {
			return ;
		}
		
		int action = intent.getExtras().getInt("Action");
		String url  = intent.getExtras().getString("URL");
		String body = intent.getExtras().getString("Body");
        
        Http up = new Http(null);
        String result = up.post(url, body);
        
        JSONObject json = JSON.parseObject(result);
        
        //�������ؽ��
        switch(action) {
        case Http.ACTION_UPLOAD: //�ϴ�
        	if (json != null) {
         		if (json.getIntValue("code") == 0) {
         			//�ϴ��ɹ�
         			Log.d(Consts.TAG, "Upload data succeed.");
         		}
         		else {
         			//�ϴ�ʧ��
         			Log.e(Consts.TAG, "Upload data failed.");
         		}
         	}
        	
        	break;
        default:
        	//	
        }
	}
}
