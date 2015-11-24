package markettracker.util;

import java.util.ArrayList;
import java.util.List;

import markettracker.data.Fields;
import markettracker.data.Panal;
import markettracker.data.UIItem;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class Rpt_Table extends LinearLayout implements OnClickListener {

	private List<View> list;
	private boolean isShow = true;

	public Rpt_Table(Context context, Panal panal, Fields data,
			Handler handler, Activity activity) {
		super(context);
		init(panal, data, handler, activity);
	}

	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}

	private void init(Panal panal, Fields data, Handler handler,
			Activity activity) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		if (panal.isShowCaption()) {
			RptTitle view = new RptTitle(getContext(), panal);
			view.setOnClickListener(this);
			this.addView(view);
		}
		CustomLineLayout content;
		CView cview;
		list = new ArrayList<View>();
		for (UIItem item : panal.getItemList()) {
			// content = new CustomLineLayout(getContext(), item, activity,
			// data,
			// handler,null);
			data.put("DisplayType", item.getDicId());
			content = new CustomLineLayout(getContext(), item, activity, data,
					handler, null);
			list.add(content);
			list.add(content);
			this.addView(content);
			cview = new CView(getContext(), Constants.TableType.LINE);
			this.addView(cview);
			list.add(cview);
		}
		list.remove(list.size() - 1);
		this.removeViewAt(this.getChildCount() - 1);
		this.addView(new CView(getContext(), Constants.TableType.FOOT));
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
