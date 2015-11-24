package markettracker.util;

import orient.champion.business.R;
import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CDeleteButton extends Button {

	public CDeleteButton(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.setId(1);
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.del));
		this.setPadding(0, 2, 0, 0);
		LayoutParams layoutParams = new LinearLayout.LayoutParams(Tool.dip2px(
				context, 50), Tool.dip2px(context, 30));

		layoutParams.leftMargin = 10;
		this.setLayoutParams(layoutParams);
		this.setGravity(Gravity.CENTER);
		this.setText("删除");
		this.setTextSize(14.0f);
		this.setTextColor(getResources().getColor(R.color.white));
	}

}
