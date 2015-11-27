package markettracker.util;

import orient.champion.business.R;

import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Sqlite;

import java.util.List;

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

	private Context mContext;
	private TextView tvCount;
	private TextView title;
	private ImageView img;

	public CButtonRe(Context context, ButtonConfig config, LayoutParams lay,
			Template temp, OnClickListener l) {
		super(context);
		init(config, lay, context, temp, l);
	}

	private void init(ButtonConfig config, LayoutParams lay, Context context,
			Template temp, OnClickListener l) {
		mContext = context;
		
		View view = LayoutInflater.from(context).inflate(R.layout.btn_lay, null);
		this.setGravity(Gravity.CENTER);

		view.setId(config.getId());

		view.setOnClickListener(l);

		img = (ImageView) view.findViewById(R.id.btn_image);
		img.setImageDrawable(Tool.getDrawable(context, config.getImageId()));
		title = (TextView) view.findViewById(R.id.btn_title);
		title.setText(config.getName());

		tvCount = (TextView) view.findViewById(R.id.btn_count);

		this.setLayoutParams(lay);

		this.addView(view);

		if ("事项提醒".equals(config.getName()) || "信息公告".equals(config.getName())) {
			refreshCount();
		}
	}

	public void refreshCount() {
		int iCount = 0;
		if("事项提醒".equals(title.getText().toString())){
			iCount = Sqlite.getUnReadMsgCount(getContext(), 3);
		}else if("信息公告".equals(title.getText().toString())){
			iCount = Sqlite.getUnReadMsgCount(getContext(), 2);
		}
		
		if (iCount > 0) {
			if(iCount > 99){
				tvCount.setText("99+");
				tvCount.setVisibility(View.VISIBLE);
			}else{
				tvCount.setText(iCount + "");
				tvCount.setVisibility(View.VISIBLE);
			}
		} else {
			tvCount.setText("0");
			tvCount.setVisibility(View.GONE);
		}
	}

	public String getdate() {
		return data.getStrValue("dateofyear");
	}
}
