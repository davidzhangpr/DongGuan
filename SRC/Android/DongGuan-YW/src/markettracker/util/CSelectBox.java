package markettracker.util;

import orient.champion.business.R;

import java.util.List;

import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CSelectBox extends RadioGroup implements OnCheckedChangeListener
{
	
	private UIItem item;
	private Fields data;
	private List<DicData> list;
	
	private CRadioButton radioButton;
	
	public CSelectBox(Context context, UIItem item, Fields data)
	{
		super(context);
		this.item = item;
		this.data = data;
		initDictData();
		initRadioGroup();
	}
	
	private void initRadioGroup()
	{
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(HORIZONTAL);
		this.setPadding(Tool.dip2px(getContext(), 10), 0, Tool.dip2px(getContext(), 10), 0);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setMinimumHeight(Tool.dip2px(getContext(), 40));
		String content = getResult(data, item);
		for (DicData dic : list)
		{
			radioButton = new CRadioButton(getContext(), dic, data, item);
			if (!content.equals("") && content.equals(dic.getValue()))
			{
				radioButton.setChecked(true);
				this.check(radioButton.getId());
			}
			this.addView(radioButton);
		}
		this.setOnCheckedChangeListener(this);
		this.setBackgroundDrawable(getDrawable(1));
	}
	
	private Drawable getDrawable(int type)
	{
		return getResources().getDrawable(R.drawable.round);
	}
	
	private String getResult(Fields data, UIItem item)
	{
		if (data == null || item == null)
			return "";
		else
			return data.getStrValue(item.getDataKey());
	}
	
	private void setData(String value)
	{
		data.put(item.getDataKey(), value);
	}
	
	private void initDictData()
	{
		if (list == null)
			list = Sqlite.getDictDataList(getContext(), item.getDicId(), "");
	}
	
	private LinearLayout.LayoutParams getCurLayoutParams()
	{
		android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 40));
		return layoutParams;
	}
	
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		CRadioButton rb;
		for (int i = 0; i < this.getChildCount(); i++)
		{
			rb = (CRadioButton) this.getChildAt(i);
			if (rb.getId() == checkedId)
			{
				setData(rb.getDictValue());
				break;
			}
		}
	}
	
}
