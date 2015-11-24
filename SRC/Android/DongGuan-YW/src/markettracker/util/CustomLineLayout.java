package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.SObject;
import markettracker.data.Template;
import markettracker.data.UIItem;

import orient.champion.business.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CustomLineLayout extends LinearLayout implements OnClickListener
{
	
	private Context context;
	private CustomLineLayoutContent customLineLayoutContent;
	private CustomTextView customTextView;
	
	public CustomLineLayout(Context context, UIItem item, Activity activity, Fields data, Handler handler,SObject object,OnClickListener listener)
	{
		super(context);
		this.context = context;
		init(context, item, activity, data, handler,object,listener);
	}

	public CustomLineLayout(Context context, Template template, UIItem item, Activity activity, Fields data, Handler handler,SObject object,OnClickListener listener)
	{
		super(context);
		this.context = context;
		init(context, template, item, activity, data, handler,object,listener);
	}
	
	public CustomLineLayout(Context context, UIItem item, Activity activity, Fields data, Handler handler, OnClickListener listener)
	{
		super(context);
		init(context, item, activity, data, handler, listener);
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = 1;
		return layoutParams;
	}
	
	private void init(Context context, UIItem item, Activity activity, Fields data, Handler handler,SObject object,OnClickListener listener)
	{
		this.setOrientation(item.getOrientation());
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tablemid);
		
		// 添加标题
		if (item.isShowLable())
		{
			customTextView = new CustomTextView(context, item, activity);
			this.addView(customTextView);
		}
		customLineLayoutContent = new CustomLineLayoutContent(context, item, activity, data, handler,object,listener);
		this.addView(customLineLayoutContent);
	}

	private void init(Context context, Template template, UIItem item, Activity activity, Fields data, Handler handler,SObject object,OnClickListener listener)
	{
		this.setOrientation(item.getOrientation());
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tablemid);
		
		// 添加标题
		if (item.isShowLable())
		{
			customTextView = new CustomTextView(context, item, activity);
			this.addView(customTextView);
		}
		customLineLayoutContent = new CustomLineLayoutContent(context, template, item, activity, data, handler,object,listener);
		this.addView(customLineLayoutContent);
	}
	
	public void refresh()
	{
		customLineLayoutContent.refresh();
	}
	
	public void resetData()
	{
		customLineLayoutContent.resetData();
	}
	
	public void onClick(View v)
	{
		try
		{
			Toast.makeText(context, customTextView.getText().toString().trim(), Toast.LENGTH_SHORT).show();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	private void init(Context context, UIItem item, Activity activity, Fields data, Handler handler, OnClickListener listener)
	{
		this.setOrientation(item.getOrientation());
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.table_mid);
		
		// //添加标题
		if (item.isShowLable())
		{
			customTextView = new CustomTextView(context, item);
			// mCustomTextView.setOnClickListener(this);
			this.addView(customTextView);
		}
		
		// itemList.add(customLineLayoutContent);
		customLineLayoutContent = new CustomLineLayoutContent(context, item, activity, data, listener);
		this.addView(customLineLayoutContent);
		
	}
}
