package cn.dennishucd.whered;

import java.io.File;
import java.io.IOException;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;

import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.config.WheredContext;
import cn.dennishucd.whered.db.LocationDB;
import cn.dennishucd.whered.db.TLocation;
import cn.dennishucd.whered.location.GPS;
import cn.dennishucd.whered.net.Http;
import cn.dennishucd.whered.net.HttpService;
import cn.dennishucd.whered.net.NetworkStatus;
import cn.dennishucd.whered.utils.DateUtils;
import android.app.Application;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class WheredApplication extends Application implements BDLocationListener {
	
	private static WheredApplication mInstance = null;
	public LocationClient mLocationClient = null;
    public BMapManager mBMapManager = null;
    
//    public BDLocation mLocation = null; //��¼���һ�ε�λ��
    private String lastTimestamp = null; //��¼���һ�θ��µ�ʱ��	
	private boolean stoped = false; //��ʾֹͣ������
    
	@Override
	public void onCreate() {
		
		//��־�ض���
		if (WheredContext.isLoged) {
			String fileName = "logcat_"+DateUtils.getCurrentTimeFileName()+".txt";
	    	File file = new File(this.getCacheDir(), fileName);
	    	
	    	String path = file.getAbsolutePath();
	    	
	    	Log.e(Consts.TAG, "The log has been redirected to."+path);
	    	
	    	try {
	    		@SuppressWarnings("unused")
	    		Process process = Runtime.getRuntime().exec("logcat -f "+file.getAbsolutePath()+" -s Whered");
	    		//Process process = Runtime.getRuntime().exec("logcat -f "+file.getAbsolutePath()+" *:e");
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	    	Log.d(Consts.TAG, DateUtils.getCurrentTime()+"The log redirect function is opened.");
		}
    	
    	//��Դ��
    	PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		WheredContext.wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Whered");
		WheredContext.wl.setReferenceCounted(false);
		
		//����״̬
		WheredContext.ns = new NetworkStatus(this);
		
		mInstance = this;
	
    	 //�ٶȵ�ͼ
    	mBMapManager = new BMapManager(this);   
        
        if (!mBMapManager.init("CFc4d5756dfc401b91bcb5c411044d5e", null)) {
        	Toast.makeText(this, "Map init failed. Please check the API key.",
        			Toast.LENGTH_LONG).show();
        	
        	return;
        }
        
        mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(this);
		
		LocationClientOption option = new LocationClientOption();
        option.disableCache(true);//��ֹ���û��涨λ
        option.setOpenGps(false);
        option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
        option.setProdName("Whered");
        //option.setScanSpan(100);//���÷���λ����ļ��ʱ��(��λms),�����С��1000�ͱ�ʾһ�ζ�λ
        option.setPriority(LocationClientOption.NetWorkFirst); //��·��λ���ȣ�ע�����ﻹ������GPS
        option.setPoiNumber(0);	//��෵��POI����,Ϊ��ʡ���������ﲻ��Ҫ����	
        mLocationClient.setLocOption(option);
        
        WheredContext.locClient = mLocationClient;
        
        mLocationClient.start();
        
        WheredContext.gps = new GPS(this);
        	
		super.onCreate();
		
		Log.d(Consts.TAG, DateUtils.getCurrentTime()+"WheredApplication.onCreate is invoked.");
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		
		//�ͷŵ�Դ��
		if ((WheredContext.wl != null) && (WheredContext.wl.isHeld())) {
			WheredContext.wl.acquire();
		}
		
		Log.d(Consts.TAG, DateUtils.getCurrentTime()+":"+"onReceiveLocation is invoked.");
		
		if (location == null) {
			return ;
		}
			
		switch(location.getLocType()) {
		case 61: //GPS
			Log.d(Consts.TAG, DateUtils.getCurrentTime() + ":GPS "+location.getLatitude()+", "+location.getLongitude());
			break;
		
		case 161: //Network
			Log.d(Consts.TAG, DateUtils.getCurrentTime() + ":Network "+location.getLatitude()+", "+location.getLongitude());
			 
			break;
		case 63: //Network Failure
			Log.e(Consts.TAG, DateUtils.getCurrentTime()+"Network is not available.");
		default:
				break;
		}
		
		/*
		 * �㷨������
		 * 1. ������λ֮�󣬻�ȡ�ĵ�һ��λ�ø�ֵ��mLocation��Ȼ��������ݿ�
		 * 2. ��ȡ����N��λ�ã�
		 *    (1) �ж��Ƿ���mLocation��ʱ����ͬ�������ͬ˵���û���λ��û�з����ı�;
		 *        a. ���ж�stoped�Ƿ�Ϊtrue�������Ϊtrue����ʱ��stoped��Ϊtrue��Ȼ���ȡϵͳ��ǰʱ��
		 *          ��Ȼ�󽫸�λ�ô������ݿ�;
		 *        b. ���stopedΪtrue, ��ȡʱ�䲢����������¼
		 *    (2) �����ͬ����λ�÷����仯�ˡ���stopedΪfalse��ͬʱ��ֵ��mLocation���������ݿ�.
		 * */
		
		//TODO: �Ż�:�����ݿ�Ĵ洢�ᵽ����ȥ������ֻ����
			
		//��ʼ�����ݿ�
		LocationDB db = new LocationDB(this);
		TLocation tLoc = new TLocation();
		db.open();
		
		tLoc.setTimestamp(location.getTime());
		tLoc.setLocType(1);
		tLoc.setLongitude(location.getLongitude());
		tLoc.setLatitude(location.getLatitude());
		tLoc.setUploaded(0);
	
		//�㷨ʵ��:
		//�״ζ�λ
		if(WheredContext.mLocation == null) {
			WheredContext.mLocation = location;
			
			db.insert(tLoc);
			Log.d(Consts.TAG, DateUtils.getCurrentTime()+"=="+location.getTime()+"Location1 is inserted.");
		}
		//λ��û��
		else if(location.getTime().equals(WheredContext.mLocation.getTime())) {
			
			//��������λ�õ�ʱ��
			String cur = DateUtils.getCurrentTime();
			
			if (stoped != true) {
				stoped = true;
				
				//update the time and insert the record
				tLoc.setTimestamp(cur);
				db.insert(tLoc);
				
				Log.d(Consts.TAG, DateUtils.getCurrentTime()+"Location2 is inserted.");
			}
			else {
				//update the time and update the previous record
				db.update(lastTimestamp, DateUtils.getCurrentTime());
				
				Log.d(Consts.TAG, DateUtils.getCurrentTime()+"Location3 is updated.");
			}
			
			lastTimestamp = cur;
		}
		//λ�ñ仯
		else {
			stoped = false;
			
			WheredContext.mLocation = location;
			
			//insert the record
			db.insert(tLoc);
			
			Log.d(Consts.TAG, DateUtils.getCurrentTime()+"Location4 is inserted.");
		}
		//�㷨����
		
		//�ر����ݿ�
        db.close();
        
        //�ϴ�������
        if(WheredContext.token == null) {
        	return ;
        }
        
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra("Action", Http.ACTION_UPLOAD);
        intent.putExtra("URL", Consts.UPLOAD_URL);
        
        String body = String.format("{\"token\":\"%s\",\"trails\":[{\"longitude\":%s,\"latitude\":%s,\"category\":%s,\"createDate\":%s}]}"
        		, WheredContext.token, 
        		location.getLongitude(),
        		location.getLatitude(),
        		1,
        		DateUtils.dateToLong(location.getTime()));
        intent.putExtra("Body", body);
        
        startService(intent);
		}

	@Override
	public void onReceivePoi(BDLocation arg0) {
	}
}
