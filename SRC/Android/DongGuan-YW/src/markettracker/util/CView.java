package markettracker.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import orient.champion.business.R;

public class CView extends LinearLayout {

	public CView(Context context, int type) {
		super(context);
		init(context, type);
	}

	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}

	private void init(Context context, int type) {
		this.setLayoutParams(getCurLayoutParams());
		switch (type) {
		case Constants.TableType.LINE:
			this.setBackgroundResource(R.drawable.line);
			break;
		case Constants.TableType.FOOT:
			this.setBackgroundResource(R.drawable.table_foot);
			break;
		}
		this.setGravity(Gravity.CENTER_VERTICAL);
	}

}
