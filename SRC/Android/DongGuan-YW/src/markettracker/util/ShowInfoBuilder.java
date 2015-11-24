package markettracker.util;

import orient.champion.business.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ShowInfoBuilder implements OnClickListener {

	private Button set, edit, cancel;
	private TextView textView;
	private Dialog alert;

	private View view;
	private Animation mButtomInAnimation, mButtomOutAnimation;

	private int type = 0;

	public ShowInfoBuilder(Context context, OnClickListener l, String promName) {
		init(context, l, promName);
	}

	public ShowInfoBuilder(Context context, OnClickListener l, String promName,
			int type) {
		this.type = type;
		init(context, l, promName);
	}

	public void dismiss() {
		if (alert != null) {
			if (view != null) {
				view.clearAnimation();
				view.startAnimation(mButtomOutAnimation);
			}

			// alert.dismiss();
			// alert = null;
			// builder=null;
		}
	}

	private void init(Context context, OnClickListener l, String promName) {
		mButtomInAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_buttom_in);
		mButtomOutAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_buttom_out);

		mButtomOutAnimation
				.setAnimationListener(new Animation.AnimationListener() {

					// @Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
					}

					// @Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
					}

					// @Override
					public void onAnimationEnd(Animation animation) {
						if (alert != null) {
							alert.dismiss();
							alert = null;
							// builder=null;
						}
					}
				});

		view = LayoutInflater.from(context).inflate(R.layout.showinfo, null);

		set = (Button) view.findViewById(R.id.set);
		set.setOnClickListener(l);

		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);

		edit = (Button) view.findViewById(R.id.edit);
		edit.setOnClickListener(l);

		if (this.type == 1) {
			set.setText("执行");
			edit.setText("变更");
		}

		textView = (TextView) view.findViewById(R.id.text);
		textView.setText(promName);
		// this.setView(view);

		alert = new Dialog(context, R.style.showinfo_dialog);
		alert.setContentView(view);
		alert.show();
		alert.setCancelable(false);

		alert.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		view.clearAnimation();
		view.startAnimation(mButtomInAnimation);

	}

	public void onClick(View v) {
		System.out.println(v.getId());
		switch (v.getId()) {
		case R.id.set:
			// resetPwd();
			break;

		case R.id.cancel:
			// resetPwd();
			dismiss();
			break;

		default:
			break;
		}
	}

}
