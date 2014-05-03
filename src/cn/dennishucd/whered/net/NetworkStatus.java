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
	
	//���WiFi�Ƿ��Ѿ����Ӳ�����,ֻ�е�WiFi���ӵ�ĳ���ȵ�ʱ�ŷ���true
	public  boolean isWiFiConnected() {
		return mWiFi.isConnected();
	}
	
	//���WiFi�Ƿ�򿪣�ֻҪWiFi���ش򿪾ͷ���true�����򷵻�false
	public  boolean isWiFiOpened() {
		return mWiFi.isAvailable();
	}
	
	//����ƶ��������������Ƿ��
	public  boolean isMobileConnected() {
		return mMobile.isConnected();
	}
	
	//����ƶ������Ƿ����ź�
	public  boolean isAvailable() {
		return mMobile.isAvailable();
	}
	
	//���GPS�Ƿ��
	public boolean isGPSEnabled() {
		return mLM.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}
