package cn.dennishucd.whered.utils;

public class Utils {
	
	public static boolean isMobile(String str)
    {
        return str != null
                && str.matches("(\\+86|86|0086)?(13[0-9]|15[0-35-9]|14[57]|18[02356789])\\d{8}");
    }

}
