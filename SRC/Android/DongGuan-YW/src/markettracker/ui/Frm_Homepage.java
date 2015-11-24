package markettracker.ui;

import java.util.List;

import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.LoginResult;
import markettracker.data.QueryResult;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.util.Constants;
import markettracker.util.HocBuilder;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import orient.champion.business.R;
import markettracker.util.Constants.AlertType;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 首页
 * @author Mark
 *
 */
public class Frm_Homepage extends Activity implements OnClickListener{
	private LinearLayout linearLayout;
	private Context context;
	private Activity activity;
	private TextView uname;
	private Button exit;
	private LinearLayout sybx, wjdc, drbf, bfdctj, jhsd, xxgg, dzzl, xtsz, lckpi, xsrb;
	private Handler handler;
	
	private boolean rptUploading = false, msgUploading;
	
	private TextView tv_hm_dzzl_count, tv_hm_wjdc_count, tv_hm_xxgg_count;
	
	private SyncDataApp application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.homepage);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);

		init();
	}

	private void init()
	{
		initContext();
		initActivity();
		initHandler();
		initUname();
		initReportCount();
		linearLayout = (LinearLayout) findViewById(R.id.line_homepage_linear);
		
		exit = (Button) findViewById(R.id.btn_homepage_exit);
		exit.setOnClickListener(this);

		sybx = (LinearLayout) findViewById(R.id.btn_homepage_sybx);
		sybx.setOnClickListener(this);

		wjdc = (LinearLayout) findViewById(R.id.btn_homepage_wjdc);
		wjdc.setOnClickListener(this);

		drbf = (LinearLayout) findViewById(R.id.btn_homepage_drbf);
		drbf.setOnClickListener(this);

		bfdctj = (LinearLayout) findViewById(R.id.btn_homepage_bfdctj);
		bfdctj.setOnClickListener(this);

		jhsd = (LinearLayout) findViewById(R.id.btn_homepage_jhsd);
		jhsd.setOnClickListener(this);

		xxgg = (LinearLayout) findViewById(R.id.btn_homepage_xxgg);
		xxgg.setOnClickListener(this);

		dzzl = (LinearLayout) findViewById(R.id.btn_homepage_dzzl);
		dzzl.setOnClickListener(this);

		xtsz = (LinearLayout) findViewById(R.id.btn_homepage_xtsz);
		xtsz.setOnClickListener(this);

		lckpi = (LinearLayout) findViewById(R.id.btn_homepage_lckpi);
		lckpi.setOnClickListener(this);
		
		xsrb = (LinearLayout) findViewById(R.id.btn_homepage_xsrb);
		xsrb.setOnClickListener(this);
		
//		addhoc = (Button) findViewById(R.id.addhoc);
//		addhoc.setOnClickListener(this);
//		initGrid();
//		
//		SyncData.StartQueryMsg(handler, activity);
//		SyncData.StartThread(activity);
		SyncData.syncData(handler, activity);
//		SyncData.StartDownFile(activity);
	}
	
	public void initReportCount(){
		initXXGGCount();
		initWJDCCount();
		initDZZLCount();
	}
	
	/**
	 * 展示未读的消息公告的数量
	 */
	public void initXXGGCount(){
		FieldsList xxggGroup = Sqlite.getFieldsList(context, 2, "");
		int xxggCount = 0;
		for (int i = 0; i < xxggGroup.size(); i++) {
			if("未读".equals((String)xxggGroup.getFields(i).getHashMap().get("status"))){
				xxggCount++;
			}
		}
		
		tv_hm_xxgg_count = (TextView) findViewById(R.id.tv_hm_xxgg_count);
		if(xxggCount > 0){
			tv_hm_xxgg_count.setText(xxggCount+"");
			tv_hm_xxgg_count.setVisibility(View.VISIBLE);
		}else{
			tv_hm_xxgg_count.setVisibility(View.GONE);
		}
		
	}
	
	/**
	 * 展示未填写的问卷调查的数量
	 */
	public void initWJDCCount(){
		FieldsList wjdcGroup = Sqlite.getFieldsList(context, 3, "");
		int wjdcCount = 0;
		for (int i = 0; i < wjdcGroup.size(); i++) {
			if("未填".equals((String)wjdcGroup.getFields(i).getHashMap().get("status"))){
				wjdcCount++;
			}
		}
		
		tv_hm_wjdc_count = (TextView) findViewById(R.id.tv_hm_wjdc_count);
		if(wjdcCount > 0){
			tv_hm_wjdc_count.setText(wjdcCount+"");
			tv_hm_wjdc_count.setVisibility(View.VISIBLE);
		}else{
			tv_hm_wjdc_count.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 展示未下载的电子资料的数量
	 */
	public void initDZZLCount(){
		List<FieldsList> groupList = Sqlite.getElectornicGroups(context);
		int dzzlCount = 0;
		for (int i = 0; i < groupList.size(); i++) {
			if("未下载".equals((String)groupList.get(i).getList().get(0).getHashMap().get("status"))){
				dzzlCount++;
			}
		}
		
		tv_hm_dzzl_count = (TextView) findViewById(R.id.tv_hm_dzzl_count);
		if(dzzlCount > 0){
			tv_hm_dzzl_count.setText(dzzlCount+"");
			tv_hm_dzzl_count.setVisibility(View.VISIBLE);
		}else{
			tv_hm_dzzl_count.setVisibility(View.GONE);
		}
	}
	
	public void initContext()
	{
		this.context = Frm_Homepage.this;
	}
	
	public void initActivity()
	{
		this.activity = Frm_Homepage.this;
	}
	
	private void initHandler()
	{
		handler = new Handler()
		{
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
							Tool.showErrMsg(context, errMsg);
							break;
						// case Constants.PropertyKey.SYNCERR:
						// errMsg = msg.obj.toString();
						// Tool.showToastMsg(context, "数据上传失败", 0);
						// break;
						
						case Constants.PropertyKey.LOGIN:
							Tool.stopProgress();
							LoginResult loginResult = (LoginResult) msg.obj;
							if (loginResult.isSuccess() == 1)
							{
								// checkLogin(loginResult);
							}
							else
							{
								Tool.showErrMsg(context, loginResult.getErrorMsg());
							}
							break;
						
//						case Constants.CustomGridType.ADDHOC:
//							
//							selectData = hocBuilder.getSelectData();
//							if (selectData != null)
//							{
//								showStartHocDialog();
//							}
//							break;
//						case Constants.CustomGridType.MAINMENU:
//							selectData = customGrid.getSelectData();
//							if (selectData != null)
//							{
//								showStartCallDialog();
//							}
//							break;
						case Constants.PropertyKey.UPLOAD:
							Tool.stopProgress();
							List<SObject> slist = Sqlite.getSubmitObject(context);
							if (slist != null && slist.size() > 0)
							{
								rptUploading = true;
								SyncData.Upload(slist, handler, activity);
							}
							else
							{
								//resetGrid();
								if (!msgUploading)
									SyncData.StartQueryMsg(handler, activity);
								rptUploading = false;
								Tool.showToastMsg(context, "数据上传完成", AlertType.INFO);
							}
							break;
						
						case Constants.PropertyKey.UPLOADMSG:
							if (!rptUploading)
								SyncData.StartQueryMsg(handler, activity);
							msgUploading = false;
							break;
							
//						case Constants.PropertyKey.DOWNFILE:
//							Tool.showToastMsg(context, "陈列图下载完成", AlertType.INFO);
//							break;
						
						case Constants.PropertyKey.RESETPWD:
							Tool.stopProgress();
							QueryResult queryResult = (QueryResult) msg.obj;
							if (queryResult.isSuccess() == 1)
							{
								stopDialog(restpwdAlertDialog);
								Tool.showToastMsg(context, queryResult.getErrorMsg(), AlertType.INFO);
							}
							else
								Tool.showToastMsg(context, queryResult.getErrorMsg(), AlertType.ERR);
							break;
						
						default:
							super.handleMessage(msg);
							break;
					}
				}
				else
					Tool.showErrMsg(context, "网络异常");
			}
		};
		
	}

	private void initUname()
	{
		uname = (TextView) findViewById(R.id.tv_homepage_uname);
		uname.setText(Rms.getEmpName(context));
	}
	
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) 
		{
			case R.id.btn_homepage_exit:	//退出
				showExitDialog();
				break;

			case R.id.btn_homepage_sybx:	//生意表现
				if(!Tool.isConnect(context)){
					Tool.showErrMsg(context, "网络连接失败，请确认网络连接！");
				}else{
					intent = new Intent(context, Frm_ShowData.class);
					intent.putExtra("name", "生意表现");
					intent.putExtra("ClientId", "-1");
					startActivityForResult(intent, 1000);
				}
				break;

			case R.id.btn_homepage_drbf:	//当日拜访
				intent = new Intent(context, Frm_MainMenu.class);
				startActivity(intent);
				break;

			case R.id.btn_homepage_bfdctj:	//拜访达成统计
				if(!Tool.isConnect(context)){
					Tool.showErrMsg(context, "网络连接失败，请确认网络连接！");
				}else{
					intent = new Intent(context, Frm_ShowData.class);
					intent.putExtra("name", "拜访达成统计");
					startActivityForResult(intent, 1000);
				}
				break;

			case R.id.btn_homepage_jhsd:	//计划设定
				SyncData.stopSyncData(activity);
				intent = new Intent(context, Frm_Plan.class);
				startActivityForResult(intent, 1001);
				break;

			case R.id.btn_homepage_xxgg:	//消息公告
				intent = new Intent(context, Frm_MsgList.class);
				startActivityForResult(intent, 3);
				break;

			case R.id.btn_homepage_wjdc:	//问卷调查
				intent = new Intent(context, Frm_SurveyList.class);
				intent.putExtra("teminalCode", "");
				intent.putExtra("terminalname", "问卷调查");
				startActivityForResult(intent, 3023);
				break;

			case R.id.btn_homepage_dzzl:	//电子资料
				SyncData.stopSyncData(activity);
				intent = new Intent(context, Frm_ElectornicList.class);
				startActivityForResult(intent, v.getId());
				break;

			case R.id.btn_homepage_xtsz:	//系统设置（重设密码）
				intent = new Intent(context, Frm_ResetPwd.class);
				startActivityForResult(intent, v.getId());
				break;

			case R.id.btn_homepage_xsrb:	// 销售日报
				intent = new Intent(context, Frm_Menu.class);
				intent.putExtra("template", "");
				intent.putExtra("name", "销售日报");
				intent.putExtra("code", "");
				intent.putExtra("type", "12");
				intent.putExtra("terminalname", "销售日报");
				intent.putExtra("teminalCode", "");
				intent.putExtra("clienttype", "");
				intent.putExtra("displaytype", "");
				activity.startActivityForResult(intent, 1000);
				break;
			
			case R.id.btn_homepage_lckpi:	//里程KPI
				if(!Tool.isConnect(context)){
					Tool.showErrMsg(context, "网络连接失败，请确认网络连接！");
				}else{
					intent = new Intent(context, Frm_ShowData.class);
					intent.putExtra("name", "里程KPI");
					startActivityForResult(intent, 1000);
				}
				break;
	
			default:
				break;
		}
	}
	
	private AlertDialog restpwdAlertDialog;
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		linearLayout.startAnimation(Tool.getAnimation(context));
		if (requestCode == 1000)
		{
			SyncData.syncData(handler, activity);
			SyncData.startSyncData(activity);
		}
		else 
		if (requestCode == 3)
		{
			SyncData.StopQueryMsg(activity);
			SyncData.startSyncData(activity);
			List<SObject> slist = Sqlite.getReadMsgId(context);
			if (slist != null && slist.size() > 0)
			{
				msgUploading = true;
				SyncData.Upload(slist, handler, activity);
			}
			
			initXXGGCount();
		}
		else if (requestCode == 1)
		//if (requestCode == R.id.btn_homepage_sybx)
		{
			SyncData.StopQueryMsg(activity);
			SyncData.startSyncData(activity);
			List<SObject> slist = Sqlite.getSubmitObject(context);
			if (slist != null && slist.size() > 0)
			{
				rptUploading = true;
				SyncData.Upload(slist, handler, activity);
			}
		}
		
		else if (requestCode == 1001)
		{
			SyncData.startSyncData(activity);
		}

		else if (requestCode == R.id.btn_homepage_dzzl)
		{
			initDZZLCount();
		}

		else if (requestCode == 3023)
		{
			initWJDCCount();
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			showExitDialog();
		}
		return false;
	}
	
	/**
	 *  展示退出对话框
	 */
	private void showExitDialog()
	{
		List<SObject> slist = Sqlite.getSubmitObject(context);
		if (slist != null && slist.size() > 0)
		{
			Tool.showToastMsg(context, "系统中有未提交的照片，请提交完再退出", AlertType.ERR);
		}
		else
		{
			Builder dialog = new Builder(context);
			dialog.setTitle("提示");
			dialog.setMessage("确认退出？");
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{
				// @Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					exit();
				}
			});
			dialog.setNegativeButton("取消", null);
			dialog.show();
		}
	}
	
//	private void toRptClass()
//	{
//		Intent i = new Intent(context, Frm_Rpt.class);
//		i.putExtra("type", "2222");
//		i.putExtra("name", "竞品上报");
//		//i.putExtra("teminalCode", clientId);
//		
//		//i.putExtra("clienttype", type);
//		
//		//i.putExtra("displaytype", displaytype);
//		
//		
//		activity.startActivityForResult(i, 1000);
//	}
	
	private void exit()
	{
		application.pullActivity(this);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private void stopDialog(AlertDialog dialog)
	{
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
	}
	
	@Override
	protected void onResume()
	{
		Tool.setAutoTime(context);
		
		if (!Rms.getLoginDate(context).equals(Tool.getCurrDate()))
		{
			showTimeoutDialog();
		}
		super.onResume();
	}
	
	private void showTimeoutDialog()
	{
		Builder dialog = new Builder(context);
		dialog.setTitle("警告");
		dialog.setMessage("登录超时,请重新登录！");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				exits();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}
	
	private void exits()
	{
		application.pullActivity(this);
		this.finish();
		
		application.exit();
		Intent intent = new Intent(this, Frm_Login.class);
		startActivity(intent);
	}

}
