package markettracker.util;

import orient.champion.business.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Template;
import markettracker.data.TemplateGroup;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class CTable extends LinearLayout implements OnClickListener {

	private List<View> list;
	private boolean isShow = false;

	public CTable(Context context, TemplateGroup group, List<String> complete,
			OnClickListener listener) {
		super(context);
		init(group, complete, listener);
	}

	public CTable(Context context, TemplateGroup group,
			OnClickListener listener, List<Fields> complete) {
		super(context);
		init(group, listener, complete);
	}
	
	public CTable(final Context context, FieldsList group,
			OnClickListener listener) {
		super(context);
		init(group, listener);

	}
	
	private void init(FieldsList group, OnClickListener listener) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(1, 1, 1, 1);

		Title view = new Title(getContext(), group.getFields(0).getStrValue(
				"str1"));
		this.addView(view);
		CTextView textView;

		for (int i = 0; i < group.size(); i++) {
			textView = new CTextView(getContext(), group.getFields(i));
			textView.setOnClickListener(listener);
			this.addView(textView);
		}

	}


	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}

	private void init(TemplateGroup group, OnClickListener listener,
			List<Fields> complete) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		if (group.isShowTitle()) {
			Title view = new Title(getContext(), group);
			// view.setOnClickListener(this);
			this.addView(view);
		}
		CTextView textView;
		// CView cview;
		list = new ArrayList<View>();
		for (Template t : group.getTemplateList()) {

			for (Fields templateid : complete) {
				if (t.getOnlyType() == 6001) {
					if (t.getValue().equals(templateid.getStrValue("ClientId"))) {
						t.setComplete(true);
						if (templateid.getStrValue("issubmit").equals("1"))
							t.setSubmit(true);
						break;
					}
				} else {
					if (t.getType().equals(templateid)) {
						t.setComplete(true);
						break;
					}
				}
			}
			textView = new CTextView(getContext(), t);
			// textView.setVisibility(View.GONE);
			textView.setOnClickListener(listener);
			list.add(textView);
			this.addView(textView);
			// cview = new CView(getContext(), Constants.TableType.LINE);
			// // cview.setVisibility(View.GONE);
			// this.addView(cview);
			// list.add(cview);
		}
		// list.remove(view);
		// this.removeViewAt(this.getChildCount() - 1);
		// this.addView(new CView(getContext(), Constants.TableType.FOOT));
	}

	private void init(TemplateGroup group, List<String> complete,
			OnClickListener listener) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(LinearLayout.VERTICAL);
		if (group.isShowTitle()) {
			Title view = new Title(getContext(), group);
			view.setOnClickListener(this);
			this.addView(view);
		}
		CTextView textView;
		// CView cview;
		list = new ArrayList<View>();
		for (Template t : group.getTemplateList()) {

			for (String templateid : complete) {
				if (t.getOnlyType() == 6001) {
					if (t.getValue().equals(templateid)) {
						t.setComplete(true);
						break;
					}
				} else {
					if (t.getType().equals(templateid)) {
						t.setComplete(true);
						break;
					}
				}
			}
			textView = new CTextView(getContext(), t);
			if (group.isShowTitle())
				textView.setVisibility(View.GONE);
			textView.setOnClickListener(listener);
			list.add(textView);
			this.addView(textView);
			// cview = new CView(getContext(), Constants.TableType.LINE);
			// cview.setVisibility(View.GONE);
			// this.addView(cview);
			// list.add(cview);
		}
		// list.remove(view);
		// this.removeViewAt(this.getChildCount() - 1);
		// this.addView(new CView(getContext(), Constants.TableType.FOOT));
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (isShow)
			((Title) v).setCompoundDrawables(R.drawable.dropdown);
		else
			((Title) v).setCompoundDrawables(R.drawable.dropup);

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
