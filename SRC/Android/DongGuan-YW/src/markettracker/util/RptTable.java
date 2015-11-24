package markettracker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.Panal;
import markettracker.data.SObject;
import markettracker.data.Template;
import markettracker.data.UIItem;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class RptTable extends LinearLayout implements OnClickListener {

	private List<View> list;
	private boolean isShow = true;

	public RptTable(Context context, Panal panal, SObject rpt, Handler handler,
			Activity activity,OnClickListener listener) {
		super(context);
		init(panal, rpt, handler, activity,listener);
	}

	public RptTable(Context context, Template template, Panal panal, SObject rpt, Handler handler,
			Activity activity,OnClickListener listener) {
		super(context);
		init(panal, template, rpt, handler, activity,listener);
	}

	public RptTable(Context context, Panal panal, Fields data, Handler handler,
			Activity activity) {
		super(context);
		init(panal, data, handler, activity);
	}

	public RptTable(Context context, DicData dicdata,
			HashMap<Integer, Fields> photo, OnTouchListener listener, int index) {
		super(context);
		init(dicdata, photo, listener, index);
	}

	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// layoutParams.topMargin = 20;
		return layoutParams;
	}

	public void refresh() {
		for (View context : list) {
			if (context instanceof CustomLineLayout) {
				((CustomLineLayout) context).refresh();
			}
		}
	}

	public void reset() {
		for (View context : list) {
			if (context instanceof CustomLineLayout) {
				((CustomLineLayout) context).resetData();
			}
		}
	}

	private void init(Panal panal, SObject report, Handler handler,
			Activity activity,OnClickListener listener) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);

		if (panal.isShowCaption()) {
			RptTitle view = new RptTitle(getContext(), panal);
			this.addView(view);
		}
		CustomLineLayout content;
		list = new ArrayList<View>();
		for (UIItem item : panal.getItemList()) {
			content = new CustomLineLayout(getContext(), item, activity,
					report.getFields(), handler, report,listener);
			list.add(content);
			this.addView(content);
		}
	}

	private void init(Panal panal, Template template, SObject report, Handler handler,
			Activity activity,OnClickListener listener) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		
		RptTitle view;
		if(panal.isShowGroupName()){
			view = new RptTitle(getContext(), panal.getGroupName());
			this.addView(view);
		}
		
		if (panal.isShowCaption()) {
			view = new RptTitle(getContext(), panal);
			this.addView(view);
		}
		
		CustomLineLayout content;
		list = new ArrayList<View>();
		for (UIItem item : panal.getItemList()) {
			content = new CustomLineLayout(getContext(), template, item, activity,
					report.getFields(), handler, report,listener);
			list.add(content);
			this.addView(content);
		}
	}

	private void init(Panal panal, Fields data, Handler handler,
			Activity activity) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		RptTitle view = new RptTitle(getContext(), panal);
		// view.setOnClickListener(this);
		this.addView(view);
		CustomLineLayout content;
		// CView cview;
		list = new ArrayList<View>();
		for (UIItem item : panal.getItemList()) {
			content = new CustomLineLayout(getContext(), item, activity, data,
					handler, null,null);
			list.add(content);
			this.addView(content);
			// cview = new CView(getContext(), Constants.TableType.LINE);
			// this.addView(cview);
			// list.add(cview);
		}
		// list.remove(view);
		// this.removeViewAt(this.getChildCount() - 1);
		// this.addView(new CView(getContext(), Constants.TableType.FOOT));
	}

	private void init(DicData dicdata, HashMap<Integer, Fields> photo,
			OnTouchListener listener, int index) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		RptTitle view = new RptTitle(getContext(), dicdata.getItemname());
		view.setOnClickListener(this);
		this.addView(view);
		// CustomLineLayout content;
		// CView cview;
		list = new ArrayList<View>();
		LinearLayout.LayoutParams layoutParamsPhoto = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 150));
		CImageView imageView = new CImageView(getContext(), photo, index,
				listener);
		layoutParamsPhoto.topMargin = 5;
		imageView.setLayoutParams(layoutParamsPhoto);
		this.addView(imageView);
		// return imageView;

		// for (UIItem item : panal.getItemList()) {
		// content = new CustomLineLayout(getContext(), item, activity, data,
		// handler);
		// list.add(content);
		// this.addView(content);
		// cview = new CView(getContext(), Constants.TableType.LINE);
		// this.addView(cview);
		// list.add(cview);
		// }
		// list.remove(view);
		// this.removeViewAt(this.getChildCount() - 1);
		// this.addView(new CView(getContext(), Constants.TableType.FOOT));
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		for (View view : list) {
			if (isShow)
				view.setVisibility(View.GONE);
			else
				view.setVisibility(View.VISIBLE);
		}
		if (isShow)
			isShow = false;
		else
			isShow = true;
	}

}
