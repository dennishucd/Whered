package cn.dennishucd.whered.net;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {
	
	private NetworkInfo mWiFi = null;
	private NetworkInfo mMobile = null;
	private LocationManager mLM = null;
	
	public NetworkStatus(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		mLM = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		mWiFi   = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mMobile = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}
	
	//检查WiFi是否已经连接并可用,只有当WiFi连接到某个热点时才返回true
	public  boolean isWiFiConnected() {
		return mWiFi.isConnected();
	}
	
	//检查WiFi是否打开，只要WiFi开关打开就返回true，否则返回false
	public  boolean isWiFiOpened() {
		return mWiFi.isAvailable();
	}
	
	//检查移动网络数据连接是否打开
	public  boolean isMobileConnected() {
		return mMobile.isConnected();
	}
	
	//检查移动网络是否有信号
	public  boolean isAvailable() {
		return mMobile.isAvailable();
	}
	
	//检查GPS是否打开
	public boolean isGPSEnabled() {
		return mLM.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}
