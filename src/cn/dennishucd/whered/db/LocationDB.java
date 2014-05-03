package cn.dennishucd.whered.db;

import cn.dennishucd.whered.config.Consts;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocationDB {
	//DB
	private SQLiteDatabase     mSQLite  = null;
	private DBSQLiteOpenHelper mDBHelper = null;
	
	
	public LocationDB(Context context) {
		mDBHelper = new DBSQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		mSQLite = mDBHelper.getWritableDatabase();
	}

	public void close() {
		mDBHelper.close();
	}
	
	public void insert(TLocation location) {
		String sql = "INSERT INTO "+DBConsts.T_LOCATION_NAME+" VALUES('" +
				location.getTimestamp()+"','" +
				location.getLocType()+"','" +
				location.getLongitude()+"','" +
				location.getLatitude()+"','" +
						"0')";
		
		// insert 还可以使用execSQL(String sql, Object[] bindArgs)
		Log.d(Consts.TAG, sql);
		mSQLite.execSQL(sql);  
	}
	
	public void update(String srcTimestamp, String dstTimestamp) {
		
		//update Location set timestamp = '2013-09-04 15:44:24' where timestamp = '2013-09-04 25:44:24';
		
		String sql = "UPDATE "+DBConsts.T_LOCATION_NAME+" set "+DBConsts.F_TIMESTAMP
				+"='"+dstTimestamp+"' WHERE "+DBConsts.F_TIMESTAMP+"='"+srcTimestamp+"'";
		
		Log.d(Consts.TAG, sql);
		
		mSQLite.execSQL(sql);
		
	}
	
	public void get() {
		String sql = "SELECT * FROM "+DBConsts.T_LOCATION_NAME+" WHERE timestamp = ?";
		
		Cursor cursor = mSQLite.rawQuery(sql, new String[]{"13424327"}); 
		
		if (cursor != null) {
			cursor.moveToFirst();
			 
			int row = cursor.getCount();
			
			cursor.close();
		}
	}
	
	public void dropTable(String tableName) {
		mSQLite.execSQL("DROP TABLE IF EXISTS " + tableName);
	}
	
	public void dropDB(String dbName) {
		mSQLite.execSQL("DROP DATABASE " + dbName);
	}
}
