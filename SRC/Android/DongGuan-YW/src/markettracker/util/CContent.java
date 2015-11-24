package markettracker.util;

import orient.champion.business.R;
import markettracker.data.UIItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ViewConstructor")
public class CContent extends MarqueeText
{
	
	public CContent(Context context, UIItem object)
	{
		super(context);
		this.setTextSize(Tool.getTextSize());
		this.setTextColor(Tool.getTextColor(context));
		
		this.setGravity(Gravity.CENTER_VERTICAL);
		
		this.setMinimumHeight(Tool.dip2px(context, 40));
		this.setPadding(Tool.dip2px(context, 10), 0, Tool.dip2px(context, 10), 0);
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.round));
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(layoutParams);
		
	}
}
