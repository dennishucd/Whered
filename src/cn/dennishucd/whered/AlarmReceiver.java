package cn.dennishucd.whered;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.config.WheredContext;
import cn.dennishucd.whered.db.LocationDB;
import cn.dennishucd.whered.db.TLocation;
import cn.dennishucd.whered.net.HttpService;
import cn.dennishucd.whered.net.NetworkStatus;
import cn.dennishucd.whered.utils.DateUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	//��Ҫ����ʱ���������������������10s���ANR����
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d(Consts.TAG, DateUtils.getCurrentTime()+"AlarmReceiver.onReceive is invoked.");
		
		//��ȡ��Դ��
		WheredContext.wl.acquire();
		
		//ע�⣺���ܿ��Խ�GPS�����綨λ�ۺ��ڰٶ�һ�𣬿��Բ����£��������õ�����GPS
		//���WiFi���ӳɹ�����������綨λ
		if (WheredContext.ns.isWiFiConnected()) {
			//��������һ���Զ�λ
			WheredContext.locClient.requestLocation();
		}
		//�������GPS��λ����
		else {
			
		}
		
		
		//WheredContext.gps.getCurLoc();
	}
}
