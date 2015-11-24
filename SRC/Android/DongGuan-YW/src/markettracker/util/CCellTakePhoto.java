package markettracker.util;

import orient.champion.business.R;

import markettracker.data.Fields;
import markettracker.data.SObject;
import markettracker.data.UIItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class CCellTakePhoto extends LinearLayout {

	private SObject object;
	private TextView txt_count;

	private UIItem mUiItem;

	public CCellTakePhoto(Context context, SObject object, UIItem item,
			OnClickListener l) {
		super(context);
		mUiItem = item;
		this.object = object;
		init(context, l);
		// this.setOnClickListener(listener);
	}

	private void init(Context context, OnClickListener l) {

		this.setBackgroundDrawable(getDrawable(1));

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 40));
		layoutParams.bottomMargin = Tool.dip2px(context, 1);
		this.setLayoutParams(layoutParams);

		View view = LayoutInflater.from(context).inflate(R.layout.cell_photo,
				null);
		txt_count = (TextView) view.findViewById(R.id.status);
		// imageView = (ImageView) view.findViewById(R.id.show_image);
		// setImg(object);
		this.setOnClickListener(l);

		this.addView(view);
		resetCount();
	}

	private Drawable getDrawable(int type) {
		if (type == 1)
			return getResources().getDrawable(R.drawable.round);
		else
			return getResources().getDrawable(R.drawable.triangle);
	}

	public void resetCount() {
		int count = 0;
		Fields data;
		for (int i = 0; i < this.object.getAttCount(); i++) {
			try {
				data = this.object.getAttfield(i);
				if (data.getStrValue("remark").equals(mUiItem.getCaption())) {
					count++;
				}
			} catch (Exception ex) {
			}
		}
		if (count > 0)
			txt_count.setText("已拍" + count + "张");
		else {
			txt_count.setText("未拍摄");
		}
	}

	public SObject getCurObject() {
		return this.object;
	}

	public UIItem getCurItem() {
		return this.mUiItem;
	}
}
