package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import orient.champion.business.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CSpinnerList extends TextView
{
	
	private UIItem item;
	private Fields data, selectData;
	
	private SpinnerListBuilder selectDataAlertDialog;
	
	private Handler handler;
	private CSpinnerList cSpinnerList;
	
	public CSpinnerList(Context context, UIItem item, Fields data, final Handler handler)
	{
		super(context);
		
		initHandler();
		
		this.setBackgroundDrawable(getDrawable(1));
		this.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(2), null);
		this.setCompoundDrawablePadding(Tool.dip2px(context, 10));
		this.item = item;
		this.setMinimumHeight(Tool.dip2px(context, 40));
		this.setLayoutParams(getCurLayoutParams());
		this.setTextColor(Tool.getTextColor(context));
		this.setTextSize(Tool.getTextSize());
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setPadding(Tool.dip2px(context, 10), 0, Tool.dip2px(context, 10), 0);
		
		cSpinnerList = this;
		this.data = data;
		
		this.setText(getResult1());
		this.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// showSingleChoiceDialog();
				
				showDataList();
			}
		});
	}
	
	private String getResult1()
	{
		
		FieldsList list = Sqlite.getFieldsList(getContext(), 20, "");
		if (list != null && list.getList() != null)
		{
			for (Fields fields : list.getList())
			{
				if (fields.getStrValue("serverid").equals(this.data.getStrValue(this.item.getDataKey())))
					return fields.getStrValue("fullname");
			}
		}
		return "";
		
	}
	
	private void showDataList()
	{
		if (selectDataAlertDialog != null)
		{
			// selectDataAlertDialog.dismiss();
			selectDataAlertDialog = null;
		}
		selectDataAlertDialog = new SpinnerListBuilder(getContext(), item, data, handler);
		// restpwdAlertDialog = msg.create();
		// restpwdAlertDialog.show();
	}
	
	// private void setText() {
	// this.setText(choiceGroupAdapter.getName(getResult()));
	// }
	
	private Drawable getDrawable(int type)
	{
		if (type == 1)
			return getResources().getDrawable(R.drawable.round);
		else
			return getResources().getDrawable(R.drawable.triangle);
	}
	
	private String getResult()
	{
		return data.getStrValue(item.getDataKey());
	}
	
	// private void setData(String value)
	// {
	// data.put(item.getDataKey(), value);
	// }
	
	// private LinearLayout getTitleView(String msg) {
	// LinearLayout mainLine = new LinearLayout(getContext());
	// mainLine.setOrientation(LinearLayout.HORIZONTAL);
	// LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
	// 60);
	// mainLine.setLayoutParams(layoutParams);
	// mainLine.setGravity(Gravity.CENTER_VERTICAL);
	// mainLine.setBackgroundDrawable(getContext().getResources().getDrawable(
	// R.drawable.mentou_bg));
	//
	// TextView textView = new TextView(getContext());
	// textView.setTextColor(getContext().getResources().getColor(R.color.black));
	// textView.setTextSize(16);
	// textView.setText(msg);
	// mainLine.addView(textView);
	// return mainLine;
	// }
	
	// private void showSingleChoiceDialog()
	// {
	// if (null != singleChoiceDialog)
	// {
	// singleChoiceDialog.dismiss();
	// singleChoiceDialog = null;
	// }
	// AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
	// // builder.setTitle(item.getCaption());
	// // builder.setCustomTitle(getTitleView("请选择："+item.getCaption()));
	// builder.setSingleChoiceItems(choiceGroupAdapter,
	// choiceGroupAdapter.getPosition(getResult()), new
	// DialogInterface.OnClickListener()
	// {
	// // @Override
	// public void onClick(DialogInterface dialog, int which)
	// {
	// setData(choiceGroupAdapter.getSaveValue(which));
	// cSpinner.setText(choiceGroupAdapter.getName(which));
	// invalidate();
	// dialog.dismiss();
	// }
	// });
	// builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
	// {
	// // @Override
	// public void onClick(DialogInterface dialog, int which)
	// {
	//
	// }
	// });
	// singleChoiceDialog = builder.create();
	// singleChoiceDialog.show();
	// }
	
	private void initHandler()
	{
		handler = new Handler()
		{
			// @Override
			public void handleMessage(Message msg)
			{
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what)
				{
				
					case Constants.CustomGridType.SELECTDATA:
						if (msg.obj != null)
						{
							selectData = (Fields) msg.obj;
							data.put(item.getDataKey(), selectData.getStrValue("serverid"));
							
							// Tool.showMsg(getCurrContext(), strMsg);
							
							selectDataAlertDialog.dismiss();
							cSpinnerList.setText(selectData.getStrValue("fullname"));
						}
						break;
					
					default:
						super.handleMessage(msg);
						break;
				}
			}
		};
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}
	
}
