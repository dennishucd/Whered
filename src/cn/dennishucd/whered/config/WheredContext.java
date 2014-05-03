package cn.dennishucd.whered.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.AlarmManager;
import android.os.PowerManager;
import cn.dennishucd.whered.location.GPS;
import cn.dennishucd.whered.net.Http;
import cn.dennishucd.whered.net.NetworkStatus;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

public class WheredContext {
	//是否开启日志重定向功能
	public static boolean isLoged = true;
		
	//是否开启网络就上传
	public static boolean isUpload = false;
	
	//仅WiFi情况下上传
	public static boolean OnlyWiFiUpload = true;
	
	//用于存放用户登录后返回的token
	public static String token = null;
	
	//定位时间间隔
	public static int interval = 10000; //ms 
	
	public static BDLocation mLocation = null; //记录最近一次的位置
	
	//百度定位
	public static LocationClient locClient = null;
	
	//GPS定位
	public static GPS gps = null;
	
	//电源锁
	public static PowerManager.WakeLock wl = null;
	
	//网络状态
	public static NetworkStatus ns = null;
	
	//线程池
	public static Executor mExecutor = Executors.newFixedThreadPool(3);
//
//	public static AlarmManager alarm = null;
}
