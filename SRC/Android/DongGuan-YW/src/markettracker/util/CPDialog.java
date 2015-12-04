package markettracker.util;

import orient.champion.business.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import markettracker.util.Constants.Locations;

public class CPDialog {
	private Dialog dialog;
	private Context mContext;
	private static TextView tipTextView;
	//倒计时(时长2分钟)
	private ShowTime showTime;
	private Handler mHandler;

	public CPDialog(Context context, String strMsg, boolean isLocation, android.view.View.OnClickListener listener, Handler handler) {
		mContext = context;
		mHandler = handler;
		
		dialog = createLoadingDialog(context, strMsg, isLocation, listener);
		dialog.show();
		
		if(isLocation){	//如果是定位对话框
			//实现倒计时功能
			showTime = new ShowTime((1000*60*2)+1000, 1000);
			showTime.start();
		}
	}

	public void dismiss() {
		if(showTime != null){	//取消倒计时
			showTime.cancel();
		}
		dialog.dismiss();
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg, boolean isLocation, android.view.View.OnClickListener listener) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		tipTextView = (TextView) v.findViewById(R.id.tipTextView);
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		if (isLocation) { // 如果是定位对话框
			LinearLayout ll_dialog_btn = (LinearLayout) v.findViewById(R.id.ll_dialog_btn);
			ll_dialog_btn.setVisibility(View.VISIBLE);
			
			Button btn_dialog_cancel = (Button) v.findViewById(R.id.btn_dialog_cancel);
			btn_dialog_cancel.setOnClickListener(listener);
		}

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	/**
	 * 定义一个倒计时的内部类
	 * @author Mark
	 * @Date 2015年12月2日
	 */
	class ShowTime extends CountDownTimer {
		int minute = 1; // 分钟
		int seconds = 59; // 秒钟

		public ShowTime(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		/**
		 * 倒计时结束以后要执行的动作
		 */
		@Override
		public void onFinish() {
			//向handler发送停止获取位置信息的消息
			Message msg = new Message();
			msg.what = Locations.ENDLOCATION;
			mHandler.sendMessage(msg);
			
			// 弹出对话框
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("提示"); // 设置对话框标题
			builder.setMessage("未获取到位置信息,请寻找信号好并且空旷的地点重新获取"); // 设置对话框消息
			// 给对话框添加确定按钮
			builder.setPositiveButton("再次获取", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Tool.stopProgress();
					//向handler发送再次获取的消息
					Message msg = new Message();
					msg.what = Locations.RELOCATION;
					mHandler.sendMessage(msg);
					
					dialog.cancel();
				}

			});
			
			builder.setNegativeButton("取消", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Tool.stopProgress();
				}
			});

			AlertDialog ad = builder.create();
			ad.setCancelable(false);
			builder.show();
		}

		/**
		 * 倒计时开始时要做的事情
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			seconds -= 1;

			if (seconds == 0 && minute > 0) {
				minute -= 1;
				seconds = 60;
			}

			// 给倒计时文本控件设置视图
			tipTextView.setText(minute + ":" + seconds + " 后可再次获取位置信息");
			if (minute < 10) {
				tipTextView.setText("0" + minute + ":" + seconds + " 后可再次获取位置信息");
			}

			if (seconds < 10) {
				tipTextView.setText(minute + ":0" + seconds + " 后可再次获取位置信息");
			}

			if (minute < 10 && seconds < 10) {
				tipTextView.setText("0" + minute + ":0" + seconds + " 后可再次获取位置信息");
			}

			if (minute == 0 && seconds == 0) { // 分钟数为0，倒计时结束
				tipTextView.setText("00:00 后可再次获取位置信息");
			}

		}

	}

}
