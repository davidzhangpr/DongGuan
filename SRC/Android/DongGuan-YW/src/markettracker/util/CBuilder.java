package markettracker.util;

import orient.champion.business.R;
import markettracker.util.Constants.AlertType;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class CBuilder extends Builder {

	public CBuilder(Context context, String strMsg, AlertType type) {
		super(context);
		if (type == AlertType.ERR) {
			this.setTitle(context.getString(R.string.txt_alert));
			this.setIcon(android.R.drawable.ic_dialog_alert);
		} else {
			this.setTitle(context.getString(R.string.txt_info));
			this.setIcon(android.R.drawable.ic_dialog_info);
		}
		this.setMessage(strMsg);
		this.setPositiveButton(context.getString(R.string.button_ok), null);
	}
}
