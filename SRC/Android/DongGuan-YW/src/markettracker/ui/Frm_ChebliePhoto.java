package markettracker.ui;

import orient.champion.business.R;
import java.util.ArrayList;

import java.util.List;

import markettracker.util.Constants.AlertType;
import markettracker.util.Constants.ControlType;

import markettracker.util.CGrid;
import markettracker.util.Constants;
import markettracker.util.ImageBuilder;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.QueryConfig;
import markettracker.data.QueryResult;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Frm_ChebliePhoto extends Activity implements OnClickListener {

	private LinearLayout linearLayout;
	private Context context;
	private Activity activity;
	private CGrid customGrid;
	private Handler handler;
	private Button exit;

	private TextView title;
	private Fields selectData;

	private SyncDataApp application;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.form);

		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
	}

	private void init() {
		initContext();
		initActivity();
		initHandler();
		initTitle();
		linearLayout = (LinearLayout) findViewById(R.id.content);
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		initGrid();

	}

	private void initTitle() {
		title = (TextView) findViewById(R.id.txt);
		title.setText("陈列图");
	}

	public FieldsList getFieldsList() {
		FieldsList list = new FieldsList();
		Fields data;
		List<DicData> dicList = Sqlite.getDictDataList(getContext(), "10027",
				"");
		for (DicData dict : dicList) {
			data = new Fields();
			data.put("name", dict.getItemname());
			data.put("index", dict.getValue());
			list.setFields(data);
		}
		return list;
	}

	private View getCustomGrid(List<UIItem> itemList) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.plan_detail_grid, null);

		customGrid = (CGrid) view.findViewById(R.id.cgrid);
		FieldsList list = getFieldsList();

		customGrid.setDataInfo(itemList, list);
		customGrid.setCustomGridType(Constants.CustomGridType.CHENLIETTU);
		customGrid.setLinkHandler(handler);

		return view;
	}

	private List<UIItem> getItemList() {
		List<UIItem> itemList = new ArrayList<UIItem>();

		UIItem item = new UIItem();
		item.setCaption("名称");
		item.setWidth((Tool.getScreenWidth()));
		item.setControlType(ControlType.NONE);
		item.setDataKey("name");
		item.setAlign(Align.LEFT);
		itemList.add(item);

		return itemList;
	}

	private void initGrid() {
		linearLayout.addView(getCustomGrid(getItemList()));
//		linearLayout.startAnimation(Tool.getAnimation(context));
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishActivity();
			break;

		default:
			break;
		}
	}

	// @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (mBRptList) {
			// leaveHospital();
			//
			// } else
			finishActivity();
		}
		return false;// super.onKeyDown(keyCode, event);
	}

	private void initHandler() {
		handler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {
				Tool.stopProgress();
				String errMsg = "";
				if (msg != null && msg.obj != null) {
					switch (msg.what) {
					case Constants.PropertyKey.ERR:
						errMsg = msg.obj.toString();
						Tool.showErrMsg(context, errMsg);
						break;
					// case Constants.PropertyKey.SYNCERR:
					// errMsg = msg.obj.toString();
					// Tool.showToastMsg(context, "数据上传失败", 0);
					// break;

					case Constants.PropertyKey.QUERY:
						QueryResult result = (QueryResult) msg.obj;
						if (result.isSuccess() == 1) {
							if (result.getClientTable() != null
									&& !result.getClientTable().get(0)
											.getField("photo").equals(""))
								showImage(result.getClientTable().get(0)
										.getFields());
							else
								Tool.showToastMsg(context, "未查询到该陈列图",
										AlertType.ERR);
							// checkLogin(loginResult);
						} else {
							Tool.showErrMsg(context, result.getErrorMsg());
						}
						break;

					case Constants.CustomGridType.CHENLIETTU:
						selectData = customGrid.getSelectData();
						if (selectData != null) {
							if (Tool.isConnect(context)) {

								queryData();

							} else
								Tool.showErrMsg(context, "网络连接失败，请确认网络连接！");

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

	private AlertDialog imageAlertDialog;

	private void showImage(Fields field) {
		if (imageAlertDialog != null) {
			imageAlertDialog.dismiss();
			imageAlertDialog = null;
		}
		ImageBuilder msg = new ImageBuilder(context, field);
		imageAlertDialog = msg.create();
		imageAlertDialog.show();
	}

	private void queryData() {
		QueryConfig config = new QueryConfig();
		config.setUserId(Rms.getUserId(context));
		config.setIsAll("0");
		config.setStartRow("0");
		config.set("displaytype", selectData.getStrValue("index"));
		config.setLastTime(Tool.getCurrDate());
		config.setType("GetDisplayMsgList");

		Tool.showProgress(context, "正在查询陈列图");
		SyncData.Query(config, handler, activity);
	}

	private void finishActivity() {
		setResult(RESULT_CANCELED);
		
		application.pullActivity(this);
		this.finish();
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_ChebliePhoto.this;
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_ChebliePhoto.this;
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
