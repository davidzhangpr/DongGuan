package markettracker.util;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * 每天的23:59 系统自动退出程序
 */
public class OutService extends Service {
	private SyncDataApp application;
	private Context context;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		context = getApplicationContext();
		new MyThread(startId).start();
		
		return Service.START_NOT_STICKY;
	}
	
	class MyThread extends Thread{
		int startId;
		URL url;
		URLConnection uc;
		long ld;
		Date date;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		int hour = 0;
		int minute = 0; 
		int second = 0;
		String time;
		
		
		public MyThread(int startId) {
			super();
			this.startId = startId;
		}

		@Override
		public void run() {
			super.run();
			
			while(true){
				try {
//					if(isNetworkConnected(getApplicationContext())){
//						url=new URL("http://www.baidu.com");//取得资源对象
//						uc=url.openConnection();//生成连接对象
//						uc.connect(); //发出连接
//						ld=uc.getDate(); //取得网站日期时间
//						date=new Date(ld); //转换为标准时间对象
//					}else{
						date=new Date();
//					}
					
					time = fmt.format(date);
			    
					//获取 时,分,秒
					date = fmt.parse(time);
					hour = date.getHours();
					minute = date.getMinutes();
					second = date.getSeconds();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//每一天的 23:59，系统自动退出
				if(hour==23 && minute==59){
					application.exit();
					//关闭服务
					stopSelf();
					return;
				}
				
				SystemClock.sleep(1000);
			}
			
		}
	}
	
	public boolean isNetworkConnected(Context context) { 
		if (context != null) { 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
			if (mNetworkInfo != null) { 
				return mNetworkInfo.isAvailable(); 
			} 
		} 
		return false; 
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		application = (SyncDataApp) getApplication();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
