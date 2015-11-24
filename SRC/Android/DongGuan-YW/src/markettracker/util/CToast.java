package markettracker.util;

import markettracker.util.Constants.AlertType;
import orient.champion.business.R;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class CToast {

	private Toast toast;
	private TextView textView;
	private Context context;

	private LinearLayout getTitleView(Context context, AlertType type) {
		this.context = context;
		
		LinearLayout mainLine = new LinearLayout(context);
		mainLine.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		mainLine.setLayoutParams(layoutParams);
		mainLine.setPadding(Tool.dip2px(context, 10), Tool.dip2px(context, 10), Tool.dip2px(context, 10), Tool.dip2px(context, 10));
		mainLine.setGravity(Gravity.CENTER_VERTICAL);
		
		mainLine.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.toast_background));
		
		ImageView imageView = new ImageView(context);
		if (type == AlertType.ERR){
			imageView.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.err));
		}
		else if(type == AlertType.INFO){
			imageView.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.radio_bt_selected));
		}
		else{
			imageView.setBackgroundDrawable(null);
		}
		mainLine.addView(imageView);
		
		textView = new TextView(context);
		textView.setTextColor(context.getResources().getColor(R.color.white));
	
		textView.setTextSize(16);
		mainLine.addView(textView);
		return mainLine;
	}

	public CToast(Context context, AlertType type) {
		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(getTitleView(context, type));

	}
	
	public CToast(Context context, AlertType type, int duration) {
		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
		toast.setDuration(duration);
		toast.setView(getTitleView(context, type));
	}

	public void showMsg(String msg) {
		textView.setText(msg);
		if(msg.equals("您有新消息")){
			textView.setTextColor(context.getResources().getColor(R.color.selectrect));
		}
		toast.show();
	}

}
