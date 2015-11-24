package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.UIItem;
import markettracker.util.Constants.ControlType;
import orient.champion.business.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ViewConstructor")
public class CDatePicker extends TextView {

	private Fields data;
	private UIItem item;
	private Handler handler;

	public CDatePicker(Context context, UIItem item, Fields data,
			final Handler handler) {
		super(context);
		this.data = data;
		this.item = item;
		this.handler = handler;
		this.setBackgroundDrawable(getDrawable(1));
		this.setCompoundDrawablesWithIntrinsicBounds(null, null,
				getDrawable(2), null);
		this.setCompoundDrawablePadding(Tool.dip2px(context, 10));
		this.setMinimumHeight(Tool.dip2px(context, 40));
		this.setTextColor(Tool.getTextColor(context));
		this.setTextSize(Tool.getTextSize());
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setPadding(Tool.dip2px(context, 10), 0, Tool.dip2px(context, 10),
				0);

		this.setLayoutParams(getCurLayoutParams());
		this.setOnClickListener(new showDatePicker());
		if (item.getControlType() == ControlType.DATATIME) {
			String date = data.getStrValue(item.getDataKey());
			if (!date.equals(""))
				this.setText(date);
			else
				this.setText(Tool.getCurrDateTime1());
		} else {
			this.setText(data.getStrValue(item.getDataKey()));
			// this.setText(Tool.getCurrTime());
		}

	}
	
	public CDatePicker(Context context, UIItem item, Fields data) {
		super(context);
		this.data = data;
		this.item = item;
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setBackgroundDrawable(getDrawable(1));
		this.setCompoundDrawablesWithIntrinsicBounds(null, null,
				getDrawable(2), null);
		this.setCompoundDrawablePadding(10);
		this.setPadding(20, 0, 10, 0);
		this.setTextSize(Tool.getTitleTextSize());

		this.setTextColor(Tool.getTextColor(context));
		this.setTextSize(Tool.getTextSize());
		this.setLayoutParams(getCurLayoutParams());
		this.setOnClickListener(new showDatePicker());
		this.setText(data.getStrValue(item.getDataKey()));
	}

	public void refresh() {
		this.setText(data.getStrValue(item.getDataKey()));
	}

	private Drawable getDrawable(int type) {
		if (type == 1)
			return getResources().getDrawable(R.drawable.round);
		else
			return getResources().getDrawable(R.drawable.triangle);
	}

	private void setData(String result) {
		data.put(item.getDataKey(), result);
	}

	private LayoutParams getCurLayoutParams() {
		LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}

	private void updateDate(String datetime) {
		this.setText(datetime);
		setData(datetime);
	}

	private AlertDialog alert;

	class showDatePicker implements OnClickListener {

		public void onClick(View v) {
			if (alert != null) {
				alert.dismiss();
				alert = null;
			}
			final DateTimeBuilder dp = new DateTimeBuilder(getContext(), data,
					item);
			dp.setTitle(item.getCaption());

			dp.setNeutralButton("确定", new DialogInterface.OnClickListener() {
				// @Override
				public void onClick(DialogInterface dialog, int which) {
					updateDate(dp.getDatetime());
					if (item.isRefreshItem())
						handler.sendMessage(handler.obtainMessage(
								Constants.CustomGridType.REFRESHITEM,
								dp.getDatetime()));

				}
			});
			dp.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				// @Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});

			alert = dp.create();
			alert.show();
		}
	}

}
