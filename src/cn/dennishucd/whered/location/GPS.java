package cn.dennishucd.whered.location;

import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.db.LocationDB;
import cn.dennishucd.whered.db.TLocation;
import cn.dennishucd.whered.utils.DateUtils;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPS implements LocationListener {
	private LocationManager mLM = null;
	
	private Context mContext = null;

	public GPS(Context context) {
		
		mContext = context;
		
		mLM = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		if (!mLM.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			 //Toast.makeText(this, "GPS is disabled.", Toast.LENGTH_SHORT).show();
			 
			 return ;
		 }
	
	}
	
	//调用GPS获取当前位置，然后onLocationChanged将接收到位置
	public void getCurLoc() {
		//TODO: 可能需要优化
//		mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
//	    		120,      // seconds
//	    		5000,     // meter
//	    		this);
		mLM.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		//
		Log.d(Consts.TAG, DateUtils.getCurrentTime() + ":GPS "+
				location.getLatitude()+", "+location.getLongitude());
		
		LocationDB db = new LocationDB(mContext);
		TLocation tLoc = new TLocation();
		db.open();
		
		tLoc.setTimestamp(DateUtils.longToDate(location.getTime()));
		tLoc.setLocType(1);
		tLoc.setLongitude(location.getLongitude());
		tLoc.setLatitude(location.getLatitude());
		tLoc.setUploaded(0);
		
		db.insert(tLoc);
		
		//关闭数据库
        db.close();
		
		//获取到位置之后立即停止
		mLM.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
