package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.UIItem;
import markettracker.util.Constants.AlertType;
import orient.champion.business.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

@SuppressLint("ResourceAsColor")
public class EditTextWatcher implements TextWatcher
{
	
	// 最大长度
	// private int maxLen;
	
	// 监听改变的文本框
	private EditText editText;
	private Fields data;
	private UIItem item;
	private Context context;
	private Handler handler;
	
	/**
	 * 构造函数
	 */
	public EditTextWatcher(UIItem item, EditText editText, Fields data, Context context)
	{
		setEditText(editText);
		setData(data);
		setUIItem(item);
		setContext(context);
	}
	
	/**
	 * 构造函数
	 */
	public EditTextWatcher(UIItem item, EditText editText, Fields data, Context context, Handler handler)
	{
		setEditText(editText);
		setData(data);
		setUIItem(item);
		setContext(context);
		this.handler = handler;
	}
	
	private void setContext(Context context)
	{
		this.context = context;
	}
	
	private void setEditText(EditText editText)
	{
		this.editText = editText;
	}
	
	private void setUIItem(UIItem item)
	{
		this.item = item;
	}
	
	// private UIItem getUIItem() {
	// return this.item;
	// }
	
	private int getInputLength()
	{
		if (item.getVerifytype().equalsIgnoreCase("number"))
		{
			if (item.getMaxLength() > 8 || item.getMaxLength() < 0)
				return 8;
		}
		else if (item.getVerifytype().equalsIgnoreCase("phone"))
			return 11;
		else if (item.getMaxLength() < 0)
			return 255;
		return item.getMaxLength();
	}
	
	private void setData(Fields data)
	{
		this.data = data;
	}
	
	public void onTextChanged(CharSequence ss, int start, int before, int count)
	{
		editText.setTextColor(Color.BLACK);
		
		Editable editable = editText.getText();
		int len = editable.length();
		boolean bOK = false;
		
		if (checkLength(len))
		{
			if (checkData(editable.toString()))
				bOK = true;
		}
		if (!bOK)
		{
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			String newStr = str.substring(0, len - 1);
			editText.setText(newStr);
			editable = editText.getText();
			// 新字符串长度
			int newLen = editable.length();
			// 旧光标位置超过字符串长度
			if (selEndIndex > newLen)
			{
				selEndIndex = newLen;
			}
			// 设置新的光标所在位置
			Selection.setSelection(editable, selEndIndex);
			
		}
		else
		{
			saveData(editable.toString());
		}
	}
	
	private int getMaxValue()
	{
		if (item.getMaxValue() > 0)
			return item.getMaxValue();
		return 99999999;
	}
	
	private int getMinValue()
	{
		if (item.getMinValue() > 0)
			return item.getMinValue();
		return 0;
	}
	
	private boolean checkLength(int length)
	{
		
		
		if (length > getInputLength())
		{
			Tool.showToastMsg(context, item.getCaption() + "输入内容不能超过" + getInputLength() + "位", AlertType.ERR);
			return false;
		}
		return true;
	}
	
	private boolean checkData(String data)
	{
		if (data.equals(""))
			return true;
		if (item.getVerifytype().equalsIgnoreCase("amount"))
		{
			try
			{
				if (data.endsWith("."))
					return true;
				else if (data.indexOf(".") != -1)
				{
					data = data.substring(data.indexOf("."), data.length());
					if (data.length() > 3)
					{
						Tool.showToastMsg(context, item.getCaption() + "只能输入2位小数！", AlertType.ERR);
						return false;
					}
				}
				else
				{
					if (Double.parseDouble(data) > getMaxValue())
					{
						
						Tool.showToastMsg(context, item.getCaption() + "不能大于" + getMaxValue(), AlertType.ERR);
						return false;
					}
					if (Integer.parseInt(data) < getMinValue())
					{
						Tool.showToastMsg(context, item.getCaption() + "不能小于" + getMinValue(), AlertType.ERR);
						return false;
					}
				}
			}
			catch (Exception ex)
			{
				return false;
			}
		}
		else if (item.getVerifytype().equalsIgnoreCase("number"))
		{
			try
			{
				if (Integer.parseInt(data) > getMaxValue())
				{
					Tool.showToastMsg(context, item.getCaption() + "不能大于" + getMaxValue(), AlertType.ERR);
					return false;
				}
				if (Integer.parseInt(data) < getMinValue())
				{
					Tool.showToastMsg(context, item.getCaption() + "不能小于" + getMinValue(), AlertType.ERR);
					return false;
				}
			}
			catch (Exception ex)
			{
				return false;
			}
		}
		return true;
	}
	
	private void saveData(String strData)
	{
		data.put(item.getDataKey(), strData);
	}
	
	public void afterTextChanged(Editable s)
	{
		
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		
	}
	
}
