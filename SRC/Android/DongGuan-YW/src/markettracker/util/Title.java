package markettracker.util;

import markettracker.data.TemplateGroup;
import orient.champion.business.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Title extends TextView
{
	private TemplateGroup group;
	
	public Title(Context context, TemplateGroup group)
	{
		super(context);
		this.group = group;
		this.setTextColor(getResources().getColor(R.color.black));
		
		this.setText(group.getName());
		this.setTextSize(15);
		this.setPadding(Tool.dip2px(getContext(), 10), 1, Tool.dip2px(getContext(), 15), 1);
		this.setGravity(Gravity.CENTER_VERTICAL);
		
		this.setCompoundDrawablePadding(Tool.dip2px(context, 5));
		
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tabletop);
		this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(group.getImgId()), null, getDrawable(R.drawable.dropdown), null);
	}
	
	public Title(Context context, String title)
	{
		super(context);
		this.setTextColor(getResources().getColor(R.color.black));
		this.setText(title);
		this.setTextSize(15);
		this.setPadding(Tool.dip2px(getContext(), 5), 1, Tool.dip2px(getContext(), 15), 1);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tabletop);
//		this.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.dropdown), null);
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, getH(40));
		layoutParams.topMargin = getH(1);
		return layoutParams;
	}
	
	public void setCompoundDrawables(int type)
	{
		this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(group.getImgId()), null, getDrawable(type), null);
	}
	
	private int getH(int dip)
	{
		return Tool.dip2px(getContext(), dip);
	}
	
	private Drawable getDrawable(int type)
	{
		if(type==-1)
			return null;
		return getResources().getDrawable(type);
	}
	
}
