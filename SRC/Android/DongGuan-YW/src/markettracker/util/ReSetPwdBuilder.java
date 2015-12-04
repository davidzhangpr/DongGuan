package markettracker.util;

import markettracker.data.QueryConfig;
import markettracker.data.Rms;
import orient.champion.business.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

public class ReSetPwdBuilder implements OnClickListener
{
	
	private Button mButton;
	private EditText mOld, mNew, mNewAg;
	private Context mContext;
	private Handler mHandler;
	private Activity activity;
	
	private Dialog alert;
	
	public ReSetPwdBuilder(Context context, Handler handler, Activity activity)
	{
		// super(context);
		mHandler = handler;
		
		this.activity = activity;
		init(context);
	}
	
	public void dismiss()
	{
		if (alert != null)
		{
			alert.dismiss();
			alert = null;
			// builder=null;
		}
	}
	
	private void init(Context context)
	{
		mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.setpwd, null);
		mButton = (Button) view.findViewById(R.id.pwd_ok);
		mButton.setOnClickListener(this);
		
		mOld = (EditText) view.findViewById(R.id.pwd_old);
		mNew = (EditText) view.findViewById(R.id.pwd_new);
		mNewAg = (EditText) view.findViewById(R.id.pwd_ag);
		// this.setView(view);
		
		alert = new Dialog(context, R.style.resetpwd_dialog);
		alert.setContentView(view);
		alert.show();
		alert.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
	}
	
	public void onClick(View v)
	{
		System.out.println(v.getId());
		switch (v.getId())
		{
			case R.id.pwd_ok:
				resetPwd();
				break;
			
			default:
				break;
		}
	}
	
	private void resetPwd()
	{
		String oldpwd = mOld.getText().toString().trim();
		String newpwd = mNew.getText().toString().trim();
		String newagpwd = mNewAg.getText().toString().trim();
		if (oldpwd.equals("") || newpwd.equals("") || newagpwd.equals(""))
		{
			Tool.showErrMsg(mContext, "密码不能为空！");
		}
		else if (!newpwd.equals(newagpwd))
		{
			Tool.showErrMsg(mContext, "两次密码输入不匹配！");
		}
		else
		{
			QueryConfig config = new QueryConfig();
			config.set("empId", Rms.getUserId(mContext));
			config.set("oldPwd", oldpwd);
			config.set("newPwd", newpwd);
			config.setType("ResetPassword");
			
			Tool.showProgress(mContext, "正在修改密码，请稍候", false, null, null);
			SyncData.Query(config, mHandler, activity);
		}
	}
}
