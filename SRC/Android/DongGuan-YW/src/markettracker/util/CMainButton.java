package markettracker.util;

import orient.champion.business.R;

import markettracker.data.ButtonConfig;
import android.content.Context;
import markettracker.data.Template;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;

public class CMainButton{
	
	public CMainButton(Context context, ButtonConfig config, LayoutParams lay,
			Template temp, OnClickListener l) {
		
		init(config, lay, context, temp, l);
	}

	private void init(ButtonConfig config, LayoutParams lay, Context context,
			Template temp, OnClickListener l) {
		
		View view = LayoutInflater.from(context).inflate(R.layout.rpt_detail_grid,
				null);
		

		
	}


}
