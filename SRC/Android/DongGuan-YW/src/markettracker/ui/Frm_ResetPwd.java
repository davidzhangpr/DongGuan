package markettracker.ui;

import markettracker.util.Constants;
import markettracker.util.SyncData; 
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import orient.champion.business.R;
import markettracker.util.Constants.AlertType;
import markettracker.data.QueryConfig;
import markettracker.data.QueryResult;
import markettracker.data.Rms;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 *	系统设置
 */
@SuppressLint("HandlerLeak")
public class Frm_ResetPwd extends Activity implements OnClickListener
{
	private Button save, back;
	private EditText mOld, mNew, mNewAg;
	private Context mContext;
	private Handler mHandler;
	private Activity activity;
	
	private SyncDataApp application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setpwd);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		activity=this;
		init();
	}

	private void init() {
		mContext = this;
		save = (Button) findViewById(R.id.pwd_ok);
		save.setOnClickListener(this);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);

		mOld = (EditText) findViewById(R.id.pwd_old);
		mNew = (EditText) findViewById(R.id.pwd_new);
		mNewAg = (EditText) findViewById(R.id.pwd_ag);
		
		initHandler();
	}
	
	private void finishActivity()
	{
		setResult(RESULT_OK);
		application.pullActivity(this);
		this.finish();
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pwd_ok:
			resetPwd();
			break;

		case R.id.back:
			finishActivity();
			break;

		default:
			break;
		}
	}
	
	private void resetPwd() {
		String oldpwd = mOld.getText().toString().trim();
		String newpwd = mNew.getText().toString().trim();
		String newagpwd = mNewAg.getText().toString().trim();
		if (oldpwd.equals("") || newpwd.equals("") || newagpwd.equals("")) {
			Tool.showErrMsg(mContext, "密码不能为空！");
		} else if (!newpwd.equals(newagpwd)) {
			Tool.showErrMsg(mContext, "两次密码输入不匹配！");
		} else {
			QueryConfig config = new QueryConfig();
			config.set("empId", Rms.getUserId(mContext));
			config.set("oldPwd", oldpwd);
			config.set("newPwd", newpwd);
			config.setType("ResetPassword");
			
			Tool.showProgress(mContext, "正在修改密码，请稍候……");
			SyncData.Query(config, mHandler, activity);
		}
	}
	
	@Override
	protected void onResume()
	{
		Tool.setAutoTime(this);
		if (!Rms.getLoginDate(this).equals(Tool.getCurrDate()))
		{
			showTimeoutDialog();
		}
		super.onResume();
	}
	
	private void showTimeoutDialog()
	{
		Builder dialog = new Builder(this);
		dialog.setTitle("警告");
		dialog.setMessage("登录超时,请重新登录！");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				exit();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}
	
	private void exit()
	{
		setResult(-100);
		application.pullActivity(this);
		this.finish();
		
		application.exit();
		Intent intent = new Intent(this, Frm_Login.class);
		startActivity(intent);
	}
	
	private void initHandler()
	{
		mHandler = new Handler()
		{
			// @Override
			public void handleMessage(Message msg)
			{
				
				String errMsg = "";
				if (msg != null && msg.obj != null)
				{
					switch (msg.what)
					{
						case Constants.PropertyKey.ERR:
							Tool.stopProgress();
							errMsg = msg.obj.toString();
							Tool.showErrMsg(mContext, errMsg);
							break;
						
						case Constants.PropertyKey.RESETPWD:
							Tool.stopProgress();
							QueryResult queryResult = (QueryResult) msg.obj;
							if (queryResult.isSuccess() == 1)
							{
								finishActivity();
								Tool.showToastMsg(mContext, queryResult.getErrorMsg(), AlertType.INFO);
							}
							else
								Tool.showToastMsg(mContext, queryResult.getErrorMsg(), AlertType.ERR);
							break;
						
						default:
							super.handleMessage(msg);
							break;
					}
				}
				else
					Tool.showErrMsg(mContext, "网络异常");
			}
		};
		
	}
}
