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
	
	//不要将耗时的任务放在这个方法里，超过10s会包ANR错误
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d(Consts.TAG, DateUtils.getCurrentTime()+"AlarmReceiver.onReceive is invoked.");
		
		//获取电源锁
		WheredContext.wl.acquire();
		
		//注意：可能可以将GPS和网络定位糅合在百度一起，可以测试下，这样不用单独用GPS
		//如果WiFi连接成功，则采用网络定位
		if (WheredContext.ns.isWiFiConnected()) {
			//主动请求一次性定位
			WheredContext.locClient.requestLocation();
		}
		//否则采用GPS定位优先
		else {
			
		}
		
		
		//WheredContext.gps.getCurLoc();
	}
}
