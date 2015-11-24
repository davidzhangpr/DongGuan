package markettracker.util;

import orient.champion.business.R;
import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.UIItem;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup.LayoutParams;

public class CRadioButton extends RadioButton
{
	
	private DicData dict;
	
	public CRadioButton(Context context, DicData dict, Fields data, UIItem item)
	{
		super(context);
		this.setLayoutParams(getCurLayoutParams());
		this.setTextColor(Tool.getTextColor(getContext()));
		this.setTextSize(13.0f);
		this.dict = dict;
		this.setId(Integer.parseInt(dict.getValue()));
		this.setButtonDrawable(getResources().getDrawable(R.drawable.radiobutton));
		setText(dict, data, item);
		
	}
	
	private void setText(DicData dict, Fields data, UIItem item)
	{
		this.setText(dict.getItemname() + "    ");
	}
	
	public String getDictValue()
	{
		return dict.getValue();
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// layoutParams.leftMargin=20;
		return layoutParams;
	}
	
}
