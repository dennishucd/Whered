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
    
//    public BDLocation mLocation = null; //记录最近一次的位置
    private String lastTimestamp = null; //记录最后一次更新的时间	
	private boolean stoped = false; //表示停止下来了
    
	@Override
	public void onCreate() {
		
		//日志重定向
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
    	
    	//电源锁
    	PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		WheredContext.wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Whered");
		WheredContext.wl.setReferenceCounted(false);
		
		//网络状态
		WheredContext.ns = new NetworkStatus(this);
		
		mInstance = this;
	
    	 //百度地图
    	mBMapManager = new BMapManager(this);   
        
        if (!mBMapManager.init("CFc4d5756dfc401b91bcb5c411044d5e", null)) {
        	Toast.makeText(this, "Map init failed. Please check the API key.",
        			Toast.LENGTH_LONG).show();
        	
        	return;
        }
        
        mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(this);
		
		LocationClientOption option = new LocationClientOption();
        option.disableCache(true);//禁止启用缓存定位
        option.setOpenGps(false);
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setProdName("Whered");
        //option.setScanSpan(100);//设置发起定位请求的间隔时间(单位ms),不设或小于1000就表示一次定位
        option.setPriority(LocationClientOption.NetWorkFirst); //网路定位优先，注意这里还可设置GPS
        option.setPoiNumber(0);	//最多返回POI个数,为节省流量，这里不需要返回	
        mLocationClient.setLocOption(option);
        
        WheredContext.locClient = mLocationClient;
        
        mLocationClient.start();
        
        WheredContext.gps = new GPS(this);
        	
		super.onCreate();
		
		Log.d(Consts.TAG, DateUtils.getCurrentTime()+"WheredApplication.onCreate is invoked.");
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		
		//释放电源锁
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
		 * 算法描述：
		 * 1. 启动定位之后，获取的第一个位置赋值给mLocation，然后存入数据库
		 * 2. 获取到第N个位置：
		 *    (1) 判断是否与mLocation的时间相同，如果相同说明用户的位置没有发生改变;
		 *        a. 再判断stoped是否为true，如果不为true，此时将stoped置为true，然后获取系统当前时间
		 *          ，然后将该位置存入数据库;
		 *        b. 如果stoped为true, 获取时间并更新上条记录
		 *    (2) 如果不同，则位置发生变化了。置stoped为false，同时赋值给mLocation并插入数据库.
		 * */
		
		//TODO: 优化:把数据库的存储提到外面去，这里只调用
			
		//初始化数据库
		LocationDB db = new LocationDB(this);
		TLocation tLoc = new TLocation();
		db.open();
		
		tLoc.setTimestamp(location.getTime());
		tLoc.setLocType(1);
		tLoc.setLongitude(location.getLongitude());
		tLoc.setLatitude(location.getLatitude());
		tLoc.setUploaded(0);
	
		//算法实现:
		//首次定位
		if(WheredContext.mLocation == null) {
			WheredContext.mLocation = location;
			
			db.insert(tLoc);
			Log.d(Consts.TAG, DateUtils.getCurrentTime()+"=="+location.getTime()+"Location1 is inserted.");
		}
		//位置没变
		else if(location.getTime().equals(WheredContext.mLocation.getTime())) {
			
			//更新最新位置的时间
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
		//位置变化
		else {
			stoped = false;
			
			WheredContext.mLocation = location;
			
			//insert the record
			db.insert(tLoc);
			
			Log.d(Consts.TAG, DateUtils.getCurrentTime()+"Location4 is inserted.");
		}
		//算法结束
		
		//关闭数据库
        db.close();
        
        //上传到网络
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
