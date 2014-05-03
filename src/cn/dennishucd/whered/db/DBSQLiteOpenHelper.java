package cn.dennishucd.whered.db;

import cn.dennishucd.whered.utils.DateUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBSQLiteOpenHelper extends SQLiteOpenHelper {
	
    private  final String SQL_CREATE_LOCATION = 
                "CREATE TABLE " +DBConsts.T_LOCATION_NAME + " (" +
                		DBConsts.F_TIMESTAMP + " TEXT, " +
                		DBConsts.F_LOC_TYPE + " INTEGER, " +
                		DBConsts.F_LONGITUDE + " REAL, " +
                		DBConsts.F_LATITUDE + " REAL, " +
                		DBConsts.F_UPLOADED + " INTEGER);";
    
    private final String SQL_CREATE_LAST_UPLOAD_DADE =
    		"CREATE TABLE "+DBConsts.T_LAST_UPLOAD_TIME_NAME + " (" +
    				DBConsts.F_LAST_UPLOAD_TIME + " TEXT);";
    
    //constructor
    public DBSQLiteOpenHelper(Context context) {
        super(context, DBConsts.D_NAME, null, DBConsts.D_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATION);
        
        db.execSQL(SQL_CREATE_LAST_UPLOAD_DADE);
        
        Log.d("Whered", DateUtils.getCurrentTime()+"DBSQLiteOpenHelper.onCreate is invoked.");
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBSQLiteOpenHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + DBConsts.T_LOCATION_NAME);
		    onCreate(db);
	}
}
