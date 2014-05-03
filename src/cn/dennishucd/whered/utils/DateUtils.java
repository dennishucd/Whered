package cn.dennishucd.whered.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;

public class DateUtils {
	
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	private static final SimpleDateFormat FORMAT_FILE = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss", Locale.CHINA);

	public static String getCurrentTime() {
		return FORMAT.format(new Date());  
	}
	
	public static String getCurrentTimeFileName() {
		return FORMAT_FILE.format(new Date());  
	}
	
	public static long dateToLong(String strDate) {
		Date date = null;
		
		try {
			date = FORMAT.parse(strDate);
			
			return date.getTime();
			
		} catch (ParseException e) {
			throw new IllegalArgumentException("Can not parse " + strDate, e);
		}
	}
	
	public static String longToDate(long date) {
		return  FORMAT.format(new Date(date));
	}
}

