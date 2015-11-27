package markettracker.util;

import orient.champion.business.R;
import markettracker.data.Rms;
import markettracker.util.Constants.AlertType;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;

public class CBuilder extends Builder {

	public CBuilder(final Context context, final String strMsg, AlertType type) {
		super(context);
		if (type == AlertType.ERR) {
			this.setTitle(context.getString(R.string.txt_alert));
			this.setIcon(android.R.drawable.ic_dialog_alert);
		} else {
			this.setTitle(context.getString(R.string.txt_info));
			this.setIcon(android.R.drawable.ic_dialog_info);
		}
		this.setMessage(strMsg);
		this.setPositiveButton(context.getString(R.string.button_ok), new OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				if("请打开网络连接".equals(strMsg)){
					Rms.setIsConnent(context, true);	//标识跳转到了打开网络连接的界面
					
					Intent intent =  new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);  
					context.startActivity(intent);
				}
			}
		});
	}
}
