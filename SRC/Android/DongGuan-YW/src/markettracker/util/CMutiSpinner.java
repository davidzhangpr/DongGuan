package markettracker.util;

import java.util.List;

import markettracker.data.Fields;
import markettracker.data.DicData;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import orient.champion.business.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CMutiSpinner extends TextView
{
	
	private UIItem item;
	private CMutiSpinner cMutiSpinner;
	private List<DicData> list;
	private static final String CHARDATA = ",";
	private AlertDialog multiChoiceDialog;
	private Fields data;
	
	public CMutiSpinner(Context context, UIItem item, Fields data, final String strReDicId)
	{
		super(context);
		this.item = item;
		this.setBackgroundDrawable(getDrawable(1));
		this.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(2), null);
		this.setCompoundDrawablePadding(Tool.dip2px(context, 10));
		this.setLayoutParams(getCurLayoutParams());
		this.setMinimumHeight(Tool.dip2px(context, 40));
		this.setTextColor(Tool.getTextColor(context));
		this.setTextSize(Tool.getTextSize());
		this.setGravity(Gravity.CENTER_VERTICAL);
		
		this.setPadding(Tool.dip2px(context, 10), 0, Tool.dip2px(context, 10), 0);
		this.data = data;
		this.cMutiSpinner = this;
		this.initDictData(strReDicId);
		this.setText(getName());
		this.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				showMultiChoiceDialog(strReDicId);
			}
		});
	}
	
	private void setData(String result)
	{
		data.put(item.getDataKey(), result);
	}
	
	private String[] getResult()
	{
		return data.getStrValue(item.getDataKey()).split(CHARDATA);
	}
	
	private String getName()
	{
		String Name = "";
		String[] result = getResult();
		if (result != null)
		{
			for (DicData dic : list)
			{
				for (String string : result)
				{
					if (dic.getValue().equals(string))
					{
						Name += dic.getItemname() + CHARDATA;
					}
				}
			}
			return Name;
		}
		else
			return "";
	}
	
	private boolean isHave(String data)
	{
		String[] result = getResult();
		if (result != null)
		{
			for (DicData dic : list)
			{
				for (String string : result)
				{
					if (dic.getValue().equals(string))
					{
						if (dic.getItemname().equals(data))
							return true;
					}
				}
			}
			return false;
		}
		else
			return false;
	}
	
	private Drawable getDrawable(int type)
	{
		if (type == 1)
			return getResources().getDrawable(R.drawable.round);
		else
			return getResources().getDrawable(R.drawable.triangle);
	}
	
	private void initDictData(String dicId)
	{
		if (list == null)
		{
			if (this.item.getItemType() == 1)
				list = Sqlite.getAnswerList(getContext(), item.getDicId());
			else
				list = Sqlite.getDictDataList(getContext(), item.getDicId(), dicId);
		}
	}
	
	private int getDictDataCount()
	{
		if (list == null)
			return 0;
		return list.size();
	}
	
	private String[] getChoiceArr(String dicId)
	{
		try
		{
			String[] stringArr;
			stringArr = new String[getDictDataCount()];
			DicData dic;
			for (int i = 0; i < getDictDataCount(); i++)
			{
				dic = list.get(i);
				stringArr[i] = dic.getItemname();
			}
			return stringArr;
		}
		catch (Exception ex)
		{
		}
		return null;
	}
	
	private void showMultiChoiceDialog(String dicId)
	{
		if (multiChoiceDialog != null)
		{
			multiChoiceDialog.dismiss();
			multiChoiceDialog = null;
		}
		final String[] choiceArr = getChoiceArr(dicId);
		final boolean[] checkedArr = new boolean[getDictDataCount()];
		for (int i = 0; i < checkedArr.length; i++)
		{
			checkedArr[i] = isHave(choiceArr[i]);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(item.getCaption());
		// builder.setCustomTitle(getTitleView("请选择：" + item.getCaption()));
		
		builder.setMultiChoiceItems(choiceArr, checkedArr, new OnMultiChoiceClickListener()
		{
			public void onClick(DialogInterface dialog, int which, boolean isChecked)
			{
				checkedArr[which] = isChecked;
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				StringBuilder sb = new StringBuilder();
				StringBuilder sb1 = new StringBuilder();
				DicData dic;
				for (int i = 0; i < checkedArr.length; i++)
				{
					if (checkedArr[i])
					{
						dic = list.get(i);
						sb.append(dic.getValue());
						sb.append(CHARDATA);
						sb1.append(dic.getItemname());
						sb1.append(CHARDATA);
					}
				}
				cMutiSpinner.setText(sb1.toString());
				setData(sb.toString());
				invalidate();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		});
		multiChoiceDialog = builder.create();
		multiChoiceDialog.show();
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}
	
}
