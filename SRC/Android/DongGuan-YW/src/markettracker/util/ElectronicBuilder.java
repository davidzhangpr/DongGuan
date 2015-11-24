package markettracker.util;

import orient.champion.business.R;
import markettracker.util.CElectornicDetail;
import markettracker.util.EditTextUtils;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ElectronicBuilder implements OnClickListener {

	private LinearLayout mainLine;
	private EditText search;
	private Button back;
	private Dialog alert;

	public ElectronicBuilder(Context context, Handler handler,
			FieldsList group) {
		init(context, handler, group);
	}

	private void init(final Context context, Handler handler,
			final FieldsList group) {

		final View view = LayoutInflater.from(context).inflate(
				R.layout.alert_electronic, null);
		back = (Button) view.findViewById(R.id.back);
		back.setOnClickListener(this);

		search = (EditText) view.findViewById(R.id.searchclient);

		search.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (EditTextUtils.isEmpty(search))
					initElectornic(context, view, group, "");
				else {
					initElectornic(context, view, group,
							EditTextUtils.getString(search));
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

		initElectornic(context, view, group, "");
		// mainLine.addView(cGrid);
		alert = new Dialog(context, R.style.addhoc_dialog);
		alert.setContentView(view);
	}

	private void initElectornic(Context context, View view,
			FieldsList group, String text) {
		if (mainLine == null) {
			mainLine = (LinearLayout) view.findViewById(R.id.line_electronic);
		} else {
			mainLine.removeAllViews();
		}
		CElectornicDetail detail;
		for (Fields fields : group.getList()) {
			if (text.equals("")
					|| fields.getStrValue("description").indexOf(text) != -1) {
				detail = new CElectornicDetail(context, fields, null);
				mainLine.addView(detail);
			}
		}
	}

	public void show() {
		if (alert != null) {
			alert.show();
			alert.getWindow().setLayout(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
		}
	}

	public void dismiss() {
		if (alert != null) {
			alert.dismiss();
			alert = null;
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismiss();
	}

}
