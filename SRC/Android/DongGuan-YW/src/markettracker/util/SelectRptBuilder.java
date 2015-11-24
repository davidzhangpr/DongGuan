package markettracker.util;

import orient.champion.business.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants.ControlType;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.UIItem;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
public class SelectRptBuilder extends Builder {

	private CGrid customGrid;


	public SelectRptBuilder(Context context, Handler handler,FieldsList list) {
		super(context);
		init(context, handler,list);
	}

	public Fields getSelectData() {
		return customGrid.getSelectData();
	}


	private void init(Context context, Handler handler,FieldsList list) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.builder_grid, null);

		customGrid = (CGrid) view.findViewById(R.id.cgrid);
		customGrid.setDataInfo(getSelectRptItem(), list);
		customGrid.setCustomGridType(Constants.CustomGridType.SELECTRPT);
		customGrid.setLinkHandler(handler);
		
		this.setView(view);
	}

	private List<UIItem> getSelectRptItem() {

		List<UIItem> itemList = new ArrayList<UIItem>();

		UIItem item = new UIItem();
		item.setCaption("开始时间");
		item.setWidth(Tool.getScreenWidth() * 2 / 3);
//		item.setAlign(Align.LEFT);
		item.setControlType(ControlType.NONE);
		item.setDataKey("updatetime");
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("状态");
		item.setWidth(Tool.getScreenWidth() * 1 / 3);
//		item.setAlign(Align.CENTER);
		item.setControlType(ControlType.NONE);
		item.setDataKey("issubmit");
		itemList.add(item);
		
		return itemList;
	}

}
