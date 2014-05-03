package cn.dennishucd.whered.ui;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.utils.CoordinateConvert;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import cn.dennishucd.whered.AlarmReceiver;
import cn.dennishucd.whered.R;
import cn.dennishucd.whered.WheredApplication;
import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.config.WheredContext;
import cn.dennishucd.whered.map.BaiduMap;
import cn.dennishucd.whered.utils.DateUtils;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MapActivity extends Activity {
	//�ٶȵ�ͼ
		private MapView mMapView = null;
		private MapController mMapController = null;
		private  GraphicsOverlay mOverlay = null;
		
		private WheredApplication app = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.d(Consts.TAG, DateUtils.getCurrentTime()+"onCreate is invoked.");
        
        app = (WheredApplication)getApplication();
        
        if(app.mBMapManager != null){  
        	app.mBMapManager.start();  
        	Log.d("MainActivity", DateUtils.getCurrentTime()+"mBMapManager is started.");
        }
        
        //set������mBMapMan֮��
        setContentView(R.layout.map);
        
        mMapView=(MapView)findViewById(R.id.bmapsView);  
        mMapView.setBuiltInZoomControls(true);
        mMapController = mMapView.getController();
        
        GeoPoint point =new GeoPoint((int)(30.55088* 1E6),(int)(104.0674* 1E6));
        GeoPoint bDPoint = CoordinateConvert.fromWgs84ToBaidu(point);
        mMapController.setCenter(bDPoint);//���õ�ͼ���ĵ�  
        mMapController.setZoom(15);//���õ�ͼzoom����  
        
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
   
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
        		WheredContext.interval, pi);
        
		super.onCreate(savedInstanceState);
	}
	
	 @Override
	protected void onResume() {
		mMapView.onResume();  
		
		updateUI();
	    
		super.onResume();
	}
	 
	 @Override
	protected void onPause() {
		mMapView.onPause();  
		
		super.onPause();
	}
	 
	 @Override
	protected void onDestroy() {
		Log.e(Consts.TAG, DateUtils.getCurrentTime()+"MainActivity.onDestroy is invoked.");
		
		//�ٶȵ�ͼ
		mMapView.destroy();  
		
        if(app.mBMapManager != null){  
        	app.mBMapManager.stop();  
        	
        	Log.e(Consts.TAG, DateUtils.getCurrentTime()+"mBMapManager is stoped.");
        }  
		
		super.onDestroy();
	}
	 
	 //ˢ��UI����
	 private void updateUI() {	    	
    	if (WheredContext.mLocation == null) {
    		
    		Log.e(Consts.TAG, DateUtils.getCurrentTime()+"WheredContext.mLocation is null.");
    		
    		return ;
    	}
    	
    	Log.d(Consts.TAG, DateUtils.getCurrentTime()+"The latest mLocation is updated.");
    	
    	BDLocation location = WheredContext.mLocation;
    	
    	//���֮ǰ�и��ǲ㣬ȫ�����
    	if (!mMapView.getOverlays().isEmpty()) {
    		mMapView.getOverlays().removeAll(mMapView.getOverlays());
    	}
    	
    	GeoPoint point =new GeoPoint((int)(location.getLatitude()* 1E6), 
    		  (int)(location.getLongitude()* 1E6));
	     
	      //��ӵ�
	      mOverlay = new GraphicsOverlay(mMapView);
	      
	      mMapView.getOverlays().add(mOverlay);
	      mOverlay.setData(BaiduMap.drawPoint(point)); 
	      mMapController.setCenter(point);//���õ�ͼ���ĵ�  
	      mMapController.setZoom(16);//���õ�ͼzoom���� 
	    }
}
