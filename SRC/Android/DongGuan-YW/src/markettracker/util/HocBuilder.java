package markettracker.util;

import orient.champion.business.R;

import java.util.ArrayList;
import java.util.List;
import markettracker.util.Constants.ControlType;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class HocBuilder extends Builder {

	private CGrid mCustomGrid;
	private LinearLayout mainLine;

	private EditText editText;


	public HocBuilder(Context context, Handler handler) {
		super(context);
		init(context, handler);
	}

	public Fields getSelectData() {
		return mCustomGrid.getSelectData();
	}

	private void initeditText(final Context context,final Handler handler) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		editText = new EditText(context);
		editText.setHint("至少输入2个字符");
		
//		editText.setBackgroundColor(context.getResources().getColor(android.R.color.white));
		layoutParams.bottomMargin=5;
		editText.setLayoutParams(layoutParams);
		editText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Editable editable = editText.getText();
				int len = editable.length();
				if(len>=2)
				{
					FieldsList list = Sqlite.getFieldsList(context,1,editable.toString());
					mCustomGrid.setDataInfo(getHocItem(), list);
					mCustomGrid.invalidate();
				}else if(len==0){
					FieldsList list = Sqlite.getFieldsList(context,1,"");
					mCustomGrid.setDataInfo(getHocItem(), list);
					mCustomGrid.invalidate();
				}
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void init(Context context, Handler handler) {

		initeditText(context,handler);
		mainLine = new LinearLayout(context);
		mainLine.setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mainLine.setLayoutParams(layoutParams);
		// mainLine.setPadding(20, 0, 20, 20);
		mainLine.setBackgroundColor(context.getResources().getColor(
				R.color.background));
		mCustomGrid = new CGrid(context);
		FieldsList list = Sqlite.getFieldsList(context, 1,"");
		mCustomGrid.setDataInfo(getHocItem(), list);
		mCustomGrid.setCustomGridType(Constants.CustomGridType.ADDHOC);
		mCustomGrid.setLinkHandler(handler);
		
		mainLine.addView(editText);
		mainLine.addView(mCustomGrid);

		this.setView(mainLine);
		this.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			// @Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
	}

	private List<UIItem> getHocItem() {

		List<UIItem> itemList = new ArrayList<UIItem>();

		UIItem item;

		item = new UIItem();
		item.setCaption("门店名称");
		item.setWidth(Tool.getScreenWidth() * 9 / 10);
		item.setAlign(Align.LEFT);
		item.setControlType(ControlType.NONE);
		item.setDataKey("fullname");
		itemList.add(item);

		// item = new UIItem();
		// item.setCaption("门店编码");
		// item.setAlign(Align.LEFT);
		// item.setWidth(Tool.getScreenWidth() * 2 / 5);
		// item.setControlType(ControlType.NONE);
		// item.setDataKey("outletcode");
		// itemList.add(item);

		return itemList;
	}

}
