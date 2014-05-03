package cn.dennishucd.whered.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.AlarmManager;
import android.os.PowerManager;
import cn.dennishucd.whered.location.GPS;
import cn.dennishucd.whered.net.Http;
import cn.dennishucd.whered.net.NetworkStatus;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

public class WheredContext {
	//�Ƿ�����־�ض�����
	public static boolean isLoged = true;
		
	//�Ƿ���������ϴ�
	public static boolean isUpload = false;
	
	//��WiFi������ϴ�
	public static boolean OnlyWiFiUpload = true;
	
	//���ڴ���û���¼�󷵻ص�token
	public static String token = null;
	
	//��λʱ����
	public static int interval = 10000; //ms 
	
	public static BDLocation mLocation = null; //��¼���һ�ε�λ��
	
	//�ٶȶ�λ
	public static LocationClient locClient = null;
	
	//GPS��λ
	public static GPS gps = null;
	
	//��Դ��
	public static PowerManager.WakeLock wl = null;
	
	//����״̬
	public static NetworkStatus ns = null;
	
	//�̳߳�
	public static Executor mExecutor = Executors.newFixedThreadPool(3);
//
//	public static AlarmManager alarm = null;
}
