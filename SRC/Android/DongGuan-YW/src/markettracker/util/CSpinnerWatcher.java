package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.Rms;
import markettracker.data.UIItem;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * 监听值发生改变的下拉框
 * @author Mark
 *
 */
public class CSpinnerWatcher implements TextWatcher
{
	private CSpinner spinner;
	private Fields data;
	private UIItem item;
	private Context context;
	private Handler handler;
	
	/**
	 * 构造函数
	 */
	public CSpinnerWatcher(UIItem item, CSpinner spinner, Fields data, Context context, Handler handler)
	{
		setSpinner(spinner);
		setData(data);
		setUIItem(item);
		setContext(context);
		this.handler = handler;
	}
	
	private void setContext(Context context)
	{
		this.context = context;
	}
	
	private void setSpinner(CSpinner spinner)
	{
		this.spinner = spinner;
	}
	
	private void setUIItem(UIItem item)
	{
		this.item = item;
	}

	private void setData(Fields data)
	{
		this.data = data;
	}
	
	private void saveData(String strData)
	{
		data.put(item.getDataKey(), strData);
	}
	
	public void onTextChanged(CharSequence ss, int start, int before, int count)
	{
		saveData(spinner.getWhich());
		if (item.getCaption().equals("申报类型"))
		{
			if(item.getDataKey().equals("int4")){
				if(!data.getStrValue("int4").equals("")){
					if(!spinner.getWhich().equals(Rms.getDeclareType(context))){	//下拉框选项发生了改变
						data.put("int2", "");
						Rms.setIsChange(context, true);
					}else{
						Rms.setIsChange(context, false);
					}
				}
			}
			
			Rms.setDeclareType(context, spinner.getWhich());
			this.handler.sendMessage(this.handler.obtainMessage(Constants.Control.EditText, ""));
		}
	}
	
	public void afterTextChanged(Editable s)
	{
		
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		
	}
	
}
