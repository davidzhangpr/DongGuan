package markettracker.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class DataTextView extends TextView
{
	
	public DataTextView(Context context, String title, float size, int color, Drawable drawable)
	{
		super(context);
		// this.setTextColor(Tool.getTextColor(context));
		this.setText(title);
		this.setTextSize(size);
		this.setPadding(0, Tool.dip2px(context, 1), 0, Tool.dip2px(context, 1));
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		
		this.setTextColor(Tool.getColor(context, color));
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}
	
}
