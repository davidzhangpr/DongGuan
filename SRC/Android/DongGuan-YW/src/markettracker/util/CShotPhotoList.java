package markettracker.util;

import java.util.List;

import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

public class CShotPhotoList extends LinearLayout {

	private Fields data;
	// private Fields rptData;
	// private SObject report;
	// private CSelectBox selectBox;
	private UIItem item;
	private List<DicData> list;

	public CShotPhotoList(Context context, UIItem item,
			OnClickListener listener, Fields data,SObject report) {
		super(context);
		this.item = item;
		this.data = data;
		getDictData(context);
		init(context, item, listener, report);
	}

	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// layoutParams.leftMargin=5;
		return layoutParams;
	}

	private void init(Context context, UIItem item, OnClickListener listener,SObject report) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(VERTICAL);
		// this.setBackgroundResource(R.drawable.mentou_bg);
		// this.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		initPhoto(context, item, listener,report);
		// initLine(context, temp);
	}

	private void getContentItem(UIItem item) {
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setOrientation(LinearLayout.HORIZONTAL);
	}

	private void initPhoto(Context context, UIItem item,
			OnClickListener listener,SObject report) {
		UIItem contenItem;
		for (DicData dic : getDictData(context)) {
			contenItem=new UIItem();
			getContentItem(contenItem);
			contenItem.setCaption(dic.getItemname());
			contenItem.setDataKey(dic.getRemark());
			contenItem.setDicId(dic.getDictValue());
			this.addView(new CustomTextView(context, contenItem));

//			shotPhoto = ;
			this.addView(new CShotPhoto(context, contenItem, listener,
					this.data, dic,report));
			this.addView(new CView(getContext(), Constants.TableType.LINE));
		}
		this.removeViewAt(this.getChildCount() - 1);

	}

	private List<DicData> getDictData(Context context) {
		if (list == null)
			list = Sqlite.getDictDataList(context, item.getDicId(), "");
		return list;
	}

	public static int getCallPlanTitleWidth() {
		return Tool.getScreenWidth() ;
	}

}
