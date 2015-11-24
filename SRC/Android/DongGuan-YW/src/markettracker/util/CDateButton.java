package markettracker.util;

import orient.champion.business.R;
import markettracker.data.Fields;
import markettracker.data.Rms;
import android.content.Context;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CDateButton extends LinearLayout
{
	
	public CDateButton(Context context, Fields data,OnClickListener listener) {
		super(context);
		init(context, data,listener);
	}
	
	public CDateButton(Context context, Fields data, OnClickListener listener, OnLongClickListener longClickListener)
	{
		super(context);
		init(context, data, listener, longClickListener);
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.weight = 1;
		return layoutParams;
	}
	
	private void init(Context context, Fields data, OnClickListener listener, OnLongClickListener longClickListener)
	{
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		TextView tv = new TextView(context);
		// if (data.getStrValue("week").equals("周六") ||
		// data.getStrValue("week").equals("周日"))
		// tv.setTextColor(getResources().getColor(R.color.red));
		// else
		// if (Rms.getCompanyId(context).equals("1"))
		tv.setTextColor(getResources().getColor(R.color.black));
		// else {
		// tv.setTextColor(getResources().getColor(R.color.white));
		// }
		tv.setTextSize(10);
		tv.setGravity(Gravity.CENTER);
		tv.setText(data.getStrValue("week"));
		this.addView(tv);
		
		this.addView(new CButton(context, data, listener, longClickListener));
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		
	}
	
	private void init(Context context, Fields data,OnClickListener listener) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		TextView tv = new TextView(context);
		if (data.getStrValue("week").equals("周六")
				|| data.getStrValue("week").equals("周日"))
			tv.setTextColor(getResources().getColor(R.color.red));
		else
			tv.setTextColor(getResources().getColor(R.color.black));
		tv.setTextSize(10);
		tv.setGravity(Gravity.CENTER);
		tv.setText(data.getStrValue("week"));
		this.addView(tv);
		
		this.addView(new CButton(context,data,listener));
		this.setGravity(Gravity.CENTER_HORIZONTAL);
	}
}
