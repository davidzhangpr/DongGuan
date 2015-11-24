package markettracker.util;

import orient.champion.business.R;

import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.Sqlite;
import android.content.Context;
import markettracker.data.Template;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CButtonRe extends RelativeLayout {
	private Fields data;

	private TextView tvCount;

	public CButtonRe(Context context, ButtonConfig config, LayoutParams lay,
			Template temp, OnClickListener l) {
		super(context);
		init(config, lay, context, temp, l);
	}

	private void init(ButtonConfig config, LayoutParams lay, Context context,
			Template temp, OnClickListener l) {

		View view = LayoutInflater.from(context)
				.inflate(R.layout.btn_lay, null);
		this.setGravity(Gravity.CENTER);

		view.setId(config.getId());

		view.setOnClickListener(l);

		ImageView img = (ImageView) view.findViewById(R.id.btn_image);
		img.setImageDrawable(Tool.getDrawable(context, config.getImageId()));
		TextView title = (TextView) view.findViewById(R.id.btn_title);
		title.setText(config.getName());

		tvCount = (TextView) view.findViewById(R.id.btn_count);
		// title.setText(config.getName());

		this.setLayoutParams(lay);

		this.addView(view);

		if (config.getName().equals("消息公告")) {
			refreshCount();
		}
	}

	public void refreshCount() {
		int iCount = Sqlite.getUnReadMsgCount(getContext());
		if (iCount > 0) {
			tvCount.setText(iCount + "");
			tvCount.setVisibility(View.VISIBLE);
		} else {
			tvCount.setText("0");
			tvCount.setVisibility(View.GONE);
		}
	}

	public String getdate() {
		return data.getStrValue("dateofyear");
	}
}
