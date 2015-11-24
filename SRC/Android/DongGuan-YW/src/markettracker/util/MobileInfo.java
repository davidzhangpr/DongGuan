package markettracker.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class MobileInfo {

	private Context context;
	private TelephonyManager manager;

	public MobileInfo(Context context) {
		this.context = context;
	}

	private TelephonyManager getTelManager() throws Exception {
		if (manager == null)
			manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
		return manager;
	}

	public String getImei() throws Exception {
		return getTelManager().getDeviceId();
	}

	public String getTelNumber() throws Exception {
		return getTelManager().getLine1Number();
	}

	public void getNetWordInfo() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) { // 当前网络不可用
			new AlertDialog.Builder(context).setMessage(
					"检查到没有可用的网络连接,请打开网络连接").setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface,
								int i) {
							ComponentName cn = new ComponentName(
									"com.android.settings",
									"com.android.settings.Settings");
							Intent intent = new Intent();
							intent.setComponent(cn);
							intent.setAction("android.intent.action.VIEW");
							context.startActivity(intent);
							// finish();
						}
					}).show();
		}
	}
	
	
}
