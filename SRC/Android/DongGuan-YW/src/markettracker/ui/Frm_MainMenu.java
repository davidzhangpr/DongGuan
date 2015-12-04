package markettracker.ui;

import java.util.List;

import markettracker.util.CButtonRe;
import markettracker.util.Constants.AlertType;
import markettracker.util.CButton;
import markettracker.util.CGrid;
import markettracker.util.Constants;
import markettracker.util.HocBuilder;
import markettracker.util.ReSetPwdBuilder;
import markettracker.util.SelectRptBuilder;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import orient.champion.business.R;
import markettracker.adapter.PlanListAdapter;
import markettracker.comm.UpsertSoapResponse;
import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.LoginResult;
import markettracker.data.QueryConfig;
import markettracker.data.QueryResult;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
import markettracker.data.UpsertResult;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("HandlerLeak")
public class Frm_MainMenu extends Activity implements OnClickListener, OnItemClickListener {

	@SuppressWarnings("unused")
	private LinearLayout linearLayout;
	private PlanListAdapter planListAdapter;
	private Context context;

	private AlertDialog selectRptAlertDialog;
	private SelectRptBuilder selectRptBuilder;
	private Activity activity;
	private CGrid customGrid;
	private Handler handler;
	private LinearLayout lineButton;
	private Button exit, addhoc;

	private AlertDialog hocListAlertDialog, startHocAlertDialog;
	private HocBuilder hocBuilder;
	private final static String MOBILEKEY = "decimal10";
	private TextView title, username;
	private Fields selectData;

	private List<ButtonConfig> buttonlist;

	private ListView list_plan;
	
	private SyncDataApp application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_menu);

		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
		Tool.setAutoTime(context);
	}

	@Override
	protected void onRestart() {
		if (!Rms.getLoginDate(context).equals(Tool.getMoblieDate())) {
			showTimeoutDialog();
		}
		Tool.setAutoTime(context);
		super.onRestart();
	}

	private void showTimeoutDialog() {
		Builder dialog = new Builder(context);
		dialog.setTitle("警告");
		dialog.setMessage("登录超时,请重新登录！");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				exits();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	private void exit() {
		application.pullActivity(this);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private void exits() {
		application.pullActivity(this);
		this.finish();

		application.exit();
		Intent intent = new Intent(this, Frm_Login.class);
		startActivity(intent);
	}

	private void initScreen() {
		Tool.getScreen(activity);
	}

	private void init() {
		initContext();
		initActivity();
		initScreen();
		initHandler();
		initTitle();

		linearLayout = (LinearLayout) findViewById(R.id.main_menu_list);

		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(this);

		addhoc = (Button) findViewById(R.id.addhoc);
		addhoc.setOnClickListener(this);

		initGrid();
		setupButton();

		SyncData.syncData(handler, activity);
		SyncData.startSync(activity);
	}

	private void initTitle() {
		title = (TextView) findViewById(R.id.title);
		title.setText("东冠华洁纸业");

		username = (TextView) findViewById(R.id.tv_main_username);
		username.setText(Rms.getEmpName(context));
	}

	private void initButton() {
		if (buttonlist == null)
			buttonlist = Tool.getMainMenuButton();
	}

	private void initButtonLine() {
		if (lineButton == null)
			lineButton = (LinearLayout) findViewById(R.id.line_rpt_button);
	}

	RelativeLayout.LayoutParams params_main = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);

	private void setupButton() {
		initButton();
		initButtonLine();
		int count = buttonlist.size();

		params_main.width = Tool.getBWidth(activity, count);

		for (int i = 0; i < buttonlist.size(); i++)
			lineButton.addView(getRptButton(buttonlist.get(i)));
	}

	private CButtonRe MattersToRemindButton;	//事项提醒
	private CButtonRe InformationAnnouncementButton;	//信息公告

	private CButtonRe getRptButton(ButtonConfig config) {
		CButtonRe button = new CButtonRe(context, config, params_main, null, this);
		if ("事项提醒".equals(config.getName())){
			MattersToRemindButton = button;
		}else if("信息公告".equals(config.getName())){
			InformationAnnouncementButton = button;
		}
		return button;
	}

	private void initGrid() {
		list_plan = (ListView) findViewById(R.id.main_list);

		FieldsList list = Sqlite.getFieldsList(context, 0, "");
		planListAdapter = new PlanListAdapter(context, list);
		list_plan.setAdapter(planListAdapter);
		list_plan.setOnItemClickListener(this);

	}

	private void resetGrid() {
		FieldsList list = Sqlite.getFieldsList(context, 0, "");
		planListAdapter = new PlanListAdapter(context, list);
		list_plan.setAdapter(planListAdapter);
	}

	public void onClick(View v) {
		resetbutton(v.getId());
		Intent intent = null;
		switch (v.getId()) {

		case R.id.title:// 下载数据
			showDownLoadDialog();
			break;

		case 1:	//设定计划
			SyncData.stopSyncData(activity);
			intent = new Intent(context, Frm_Plan.class);
			startActivityForResult(intent, 1001);
			break;

		case 2:	//信息公告
			SyncData.stopSyncData(activity);
			intent = new Intent(context, Frm_ElectornicList.class);
			startActivityForResult(intent, v.getId());
			break;

		case 3:	//事项提醒
			intent = new Intent(context, Frm_MsgList.class);
			startActivityForResult(intent, v.getId());
			break;
			
		case 4:	//系统设置
			intent = new Intent(context, Frm_ResetPwd.class);
			startActivityForResult(intent, v.getId());
			break;
			
		case 5:
			showMoreDialog();
			break;

		case 6:
			intent = new Intent(context, Frm_QJList.class);
			startActivityForResult(intent, 6);
			break;

		case 7:
			intent = new Intent(context, Frm_ChebliePhoto.class);
			startActivityForResult(intent, 7);
			break;

		case 10:
			intent = new Intent(context, Frm_DPList.class);
			startActivityForResult(intent, 10);

			break;

		case 11:
			intent = new Intent(context, Frm_DayRpt.class);
			intent.putExtra("key", "1");
			startActivityForResult(intent, v.getId());

			break;

		case 12:
			intent = new Intent(context, Frm_DayRpt.class);
			intent.putExtra("key", "2");
			startActivityForResult(intent, v.getId());

			break;

		case 13:

			intent = new Intent(context, Frm_ClientList.class);
			startActivityForResult(intent, 13);

			break;

		case R.id.exit:
			if (Sqlite.getUnSubmitRptCount(context) > 0)
				Tool.showToastMsg(context, "数据上传中，请等待上传完成，再退出系统", AlertType.ERR);
			else
				showExitDialog();
			break;
		case R.id.addhoc:
			showHocBuilder();
			break;
		default:
			break;
		}
	}

	private void showMoreDialog() {
		stopDialog(startHocAlertDialog);
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("请选择请假或修改密码");
		builder.setIcon(android.R.drawable.ic_dialog_info);

		builder.setNeutralButton("陈列图", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context, Frm_ChebliePhoto.class);
				startActivityForResult(intent, 2);
			}
		});

		builder.setPositiveButton("请假", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context, Frm_Holiday.class);
				startActivityForResult(intent, 4);
			}
		});
		builder.setNegativeButton("修改密码", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				showResetPwd();
			}
		});
		startHocAlertDialog = builder.create();
		startHocAlertDialog.show();
	}

	private ReSetPwdBuilder restpwdAlertDialog;

	private void showResetPwd() {
		if (restpwdAlertDialog != null) {
			restpwdAlertDialog.dismiss();
			restpwdAlertDialog = null;
		}
		restpwdAlertDialog = new ReSetPwdBuilder(context, handler, activity);
	}

	private void resetbutton(int id) {
		CButtonRe button;
		CButton b;
		for (int i = 0; i < lineButton.getChildCount(); i++) {
			// button = (CButtonRe) lineButton.getChildAt(i);
			// b = (CButton) button.getChildAt(0);
			// if (b.getId() == id) {
			//
			// //
			// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg_click));
			// // b.setCompoundDrawablesWithIntrinsicBounds(
			// // null,
			// // Tool.getDrawable(context, b.getText().toString(), true),
			// // null, null);
			// // b.stopFlick(b);
			// } else {
			// //
			// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg));
			// // b.setCompoundDrawablesWithIntrinsicBounds(null, Tool
			// // .getDrawable(context, b.getText().toString(), false),
			// // null, null);
			// }
		}
		// for (int i = 0; i < lineButton1.getChildCount(); i++)
		// {
		// button = (CButtonRe) lineButton1.getChildAt(i);
		// b = (CButton) button.getChildAt(0);
		// if (b.getId() == id)
		// {
		//
		// //
		// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg_click));
		// b.setCompoundDrawablesWithIntrinsicBounds(null,
		// Tool.getDrawable(context, b.getText().toString(), true), null, null);
		// b.stopFlick(b);
		// }
		// else
		// {
		// //
		// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg));
		// b.setCompoundDrawablesWithIntrinsicBounds(null,
		// Tool.getDrawable(context, b.getText().toString(), false), null,
		// null);
		// }
		// }
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == -100) {
			exit();
		} else {
			if (requestCode == 1000) {
				SyncData.startSyncData(activity);
				resetGrid();
			} else if (requestCode == 3 || requestCode == 2) {
				SyncData.startSyncData(activity);
				MattersToRemindButton.refreshCount();
				InformationAnnouncementButton.refreshCount();
			} else if (requestCode == 1001) {
				SyncData.startSyncData(activity);
			} else {
				SyncData.startSyncData(activity);
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (Sqlite.getUnSubmitRptCount(context) > 0)
				Tool.showToastMsg(context, "数据上传中，请等待上传完成，再退出系统", AlertType.ERR);
			else
				showExitDialog();
		}
		return false;
	}

	private void showExitDialog() {
		Builder dialog = new Builder(context);
		dialog.setTitle("提示");
		dialog.setMessage("确认退出？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				exit();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}

	private void stopDialog(AlertDialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private void showHocBuilder() {
		stopDialog(hocListAlertDialog);

		hocBuilder = new HocBuilder(context, handler);
		hocListAlertDialog = hocBuilder.create();
		hocListAlertDialog.setTitle("计划外");
		hocListAlertDialog.show();
	}

	private void showDownLoadDialog() {
		Builder dialog = new Builder(context);
		dialog.setMessage("确认下载？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				download();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}

	private QueryConfig config;
	private FieldsList queryList;
	private int index = 0;

	private void download() {
		index = 0;
		queryList = Tool.getQueryList();

		config = new QueryConfig();
		config.setUserId(Rms.getUserId(context));
		config.setIsAll("0");
		config.setStartRow("0");

		config.setLastTime(Tool.getCurrDate());
		config.setType(queryList.getFields(index).getStrValue("MsgType"));

		Tool.showProgress(context, queryList.getFields(index).getStrValue("MsgInfo"), false, null, null);
		SyncData.Query(config, handler, activity);
	}

	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {

				String errMsg = "";
				if (msg != null && msg.obj != null) {
					switch (msg.what) {
					case Constants.PropertyKey.ERR:
						Tool.stopProgress();
						errMsg = msg.obj.toString();
						Tool.showErrMsg(context, errMsg);
						break;

					case Constants.PropertyKey.LOGIN:
						Tool.stopProgress();
						LoginResult loginResult = (LoginResult) msg.obj;
						if (loginResult.isSuccess() == 1) {
							// checkLogin(loginResult);
						} else {
							Tool.showErrMsg(context, loginResult.getErrorMsg());
						}
						break;

					case Constants.CustomGridType.ADDHOC:

						selectData = hocBuilder.getSelectData();
						if (selectData != null) {
							showStartHocDialog();
						}

						break;
					case Constants.CustomGridType.MAINMENU:
						selectData = customGrid.getSelectData();
						if (selectData != null) {
							showStartCallDialog();
						}
						break;
					case Constants.PropertyKey.UPLOAD:
						try {
							UpsertSoapResponse resonse = (UpsertSoapResponse) msg.obj;
							if (resonse != null && resonse.getResult() != null && resonse.getResult().size() > 0) {
								UpsertResult result = resonse.getResult().get(0);
								if (result.isSuccess() == 1){
									Tool.stopProgress();
									Tool.showToastMsg(context, "数据上传完成", AlertType.DEFAULT);
									resetGrid();
								}else {
									Tool.showErrMsg(context, result.getErrorMsg());
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

						break;

					case Constants.PropertyKey.QUERY:
						Tool.stopProgress();
						MattersToRemindButton.refreshCount();
						InformationAnnouncementButton.refreshCount();
						break;

					case Constants.PropertyKey.GETSERVERTIME:

						resetGrid();

						break;

					case Constants.PropertyKey.UPLOADMSG:
						break;

					case Constants.PropertyKey.RESETPWD:
						Tool.stopProgress();
						QueryResult queryResult = (QueryResult) msg.obj;
						if (queryResult.isSuccess() == 1) {
							restpwdAlertDialog.dismiss();
							Tool.showToastMsg(context, queryResult.getErrorMsg(), AlertType.INFO);
						} else
							Tool.showToastMsg(context, queryResult.getErrorMsg(), AlertType.ERR);
						break;

					case Constants.CustomGridType.SELECTRPT:

						Fields selectData = selectRptBuilder.getSelectData();
						if (selectData != null) {
							stopDialog(selectRptAlertDialog);
							toCall(selectData.getStrValue(MOBILEKEY));
						}
						break;

					default:
						super.handleMessage(msg);
						break;
					}
				} else
					Tool.showErrMsg(context, "网络异常");
			}
		};
	}

	private void showStartCallDialog() {
		stopDialog(startHocAlertDialog);
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("确认开始拜访  " + selectData.getStrValue("fullname") + "？");
		builder.setIcon(android.R.drawable.ic_dialog_info);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				stopDialog(hocListAlertDialog);
				toRpt(1);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		startHocAlertDialog = builder.create();
		startHocAlertDialog.show();
	}

	private void showStartHocDialog() {
		stopDialog(startHocAlertDialog);
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("确认开始计划外拜访  " + selectData.getStrValue("fullname") + "？");
		builder.setIcon(android.R.drawable.ic_dialog_info);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				stopDialog(hocListAlertDialog);
				toRpt(0);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		startHocAlertDialog = builder.create();
		startHocAlertDialog.show();
	}

	private void toRpt(int type) {

		if (type == 0) {
			String sql = "REPLACE into t_visit_plan_detail  ( clientid ,serverid ,VisitTime)values ('"
					+ selectData.getStrValue("serverid") + "' ,'-" + selectData.getStrValue("serverid") + "','"
					+ Tool.getCurrDate() + "') ";
			Sqlite.execSingleSql(context, sql);
		}
		toCall("");
	}

	private void toCall(String key) {
		Intent intent = new Intent(getContext(), Frm_Menu.class);
		intent.putExtra("teminalCode", selectData.getStrValue("serverid"));
		intent.putExtra("name", selectData.getStrValue("fullname"));
		intent.putExtra("type", selectData.getStrValue("outlettype"));
		intent.putExtra("facialdiscount", selectData.getStrValue("facialdiscount"));	//facialdiscount=10表示经销商；0表示普通门店
		intent.putExtra("displaytype", selectData.getStrValue("displaytype"));	
		intent.putExtra("key", key);
		startActivityForResult(intent, 1000);
	}

	private void showSelectRptBuilder(FieldsList list) {
		stopDialog(selectRptAlertDialog);

		selectRptBuilder = new SelectRptBuilder(context, handler, list);

		selectRptBuilder.setNeutralButton("新建", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				toCall(Tool.getMyUUID());
			}
		});
		selectRptBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		selectRptAlertDialog = selectRptBuilder.create();
		selectRptAlertDialog.show();
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_MainMenu.this;
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_MainMenu.this;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		selectData = planListAdapter.getFields(arg2);
		if (selectData != null) {
			showStartCallDialog();
		}

	}

}
