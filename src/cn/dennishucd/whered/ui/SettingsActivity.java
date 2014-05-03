package cn.dennishucd.whered.ui;

import java.util.ArrayList;
import java.util.List;

import cn.dennishucd.whered.AlarmReceiver;
import cn.dennishucd.whered.R;
import cn.dennishucd.whered.config.Consts;
import cn.dennishucd.whered.config.WheredContext;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends Activity implements 
	CompoundButton.OnCheckedChangeListener, 
	OnItemSelectedListener {
	
	 private PendingIntent mPI   = null;
	 private AlarmManager mAlarm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.settings);
		
		mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
	    mPI = PendingIntent.getBroadcast(this, 0, intent, 0);
		
		//定位开关
		Switch s = (Switch)findViewById(R.id.switch_location);
        if (s != null) {
        	s.setChecked(true);
            s.setOnCheckedChangeListener(this);
        }
        
        //定位频率
        Spinner spin = (Spinner)findViewById(R.id.spinner_rate);
        List<String> list = new ArrayList<String>();
    	list.add("30s");
    	list.add("1m");
    	list.add("5m");
    	list.add("10m");
    	list.add("30m");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spin.setAdapter(dataAdapter);
    	spin.setOnItemSelectedListener(this);
        
		super.onCreate(savedInstanceState);
	}
	
	 @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		 if (isChecked) {
			 mAlarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
		        		WheredContext.interval, mPI);
			 Log.d(Consts.TAG, "Location switch is opened.");
		 }
		 else {
			 mAlarm.cancel(mPI);
			 Log.d(Consts.TAG, "Location switch is closed.");
		 }
    }

	@Override
	public void onItemSelected(AdapterView<?> av, View view, int pos,
			long id) {
		
		switch(pos) {
		case 0:
			WheredContext.interval = 30*1000;
			break;
		case 1:
			WheredContext.interval = 60*1000;
			break;
		case 2:
			WheredContext.interval = 300*1000;
			break;
		case 3:
			WheredContext.interval = 600*1000;
			break;
		case 4:
			WheredContext.interval = 1800*1000;
			break;
		default:
			break;
		}
		
		mAlarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
	        		WheredContext.interval, mPI);
		
		Log.d(Consts.TAG, "The location rate has changed to "+
				av.getItemAtPosition(pos).toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> av) {
	}
}
