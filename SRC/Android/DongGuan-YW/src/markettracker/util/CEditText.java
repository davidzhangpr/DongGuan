package markettracker.util;

import orient.champion.business.R;
import markettracker.data.UIItem;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CEditText extends EditText
{
	
	public CEditText(Context context, UIItem item)
	{
		super(context);
		setTextSize();
		setLayoutParams(item);
		setInputType(item);
		setGravity();
		setTextColor();
		setHint(item);
		setBackGround();
		this.setMinimumHeight(Tool.dip2px(context, 40));
	}
	
	private void setHint(UIItem item)
	{
		this.setHint(getCurHintText(item));
	}
	
	private String getCurHintText(UIItem item)
	{
		return "";
	}
	
	private void setTextColor()
	{
		this.setTextColor(getCurTextColor());
	}
	
	private int getCurTextColor()
	{
		return Tool.getTextColor(getContext());
	}
	
	private void setBackGround()
	{
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.round));
	}
	
	private int getCurBackgroundColor()
	{
		return getResources().getColor(android.R.color.white);
	}
	
	private void setTextSize()
	{
		this.setTextSize(Tool.getTextSize());
	}
	
	private void setLayoutParams(UIItem item)
	{
		this.setLayoutParams(getCurLayoutParams(item));
	}
	
	private void setGravity()
	{
		this.setGravity(getCurGravity());
	}
	
	private void setInputType(UIItem item)
	{
		this.setInputType(getInputType(item));
	}
	
	private int getInputType(UIItem item)
	{
		if (item.getVerifytype().equalsIgnoreCase("number"))
			return InputType.TYPE_CLASS_NUMBER;
		else if (item.getVerifytype().equalsIgnoreCase("amount"))
			return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
		else if (item.getVerifytype().equalsIgnoreCase("url"))
			return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI;
		else if (item.getVerifytype().equalsIgnoreCase("phone"))
			return InputType.TYPE_CLASS_NUMBER;
		return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
	}
	
	private int getCurGravity()
	{
		return Gravity.CENTER_VERTICAL;
	}
	
	private LayoutParams getCurLayoutParams(UIItem object)
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.rightMargin = 2;
		return layoutParams;
	}
	
}
