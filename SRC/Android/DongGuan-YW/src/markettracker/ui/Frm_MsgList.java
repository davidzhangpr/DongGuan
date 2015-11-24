package markettracker.ui;

import java.util.ArrayList;
import java.util.List;


import markettracker.util.Constants.ControlType;
import markettracker.util.CGrid;
import markettracker.util.Constants;
import markettracker.util.MsgDetailBuilder;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import orient.champion.business.R;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
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

public class Frm_MsgList extends Activity implements OnClickListener {

	private Context context;
	private CGrid mCustomGrid;
	private List<UIItem> mRptDetail;
	private Button exit;
	private Handler mHandler;
	
	private SyncDataApp application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message);
		initHandler();
		init();
		
		application = (SyncDataApp) getApplication();
		application.pushActivity(this);
	}

	private void init() {
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		context = Frm_MsgList.this;
		getProductLine();
	}

	private void finishActivity() {
		setResult(RESULT_OK);
		application.pullActivity(this);
		this.finish();
	}

	// 获取产品UIItem
	private List<UIItem> getItemList() {
		if (mRptDetail == null)
			mRptDetail = getMsgItem();
		return mRptDetail;
	}

	public static List<UIItem> getMsgItem() {
		List<UIItem> list = new ArrayList<UIItem>();

		UIItem item = new UIItem();
		item.setCaption("状态");
		item.setControlType(ControlType.NONE);
		item.setDataKey("status");
		item.setWidth((Tool.getScreenWidth()) / 5);
		list.add(item);

		item = new UIItem();
		item.setCaption("标题");
		item.setControlType(ControlType.NONE);
		item.setDataKey("title");
		item.setAlign(Align.LEFT);
		item.setWidth((Tool.getScreenWidth()) * 2 / 5);
		list.add(item);

		item = new UIItem();
		item.setCaption("发送时间");
		item.setControlType(ControlType.NONE);
		item.setDataKey("stime");
		item.setWidth((Tool.getScreenWidth()) * 2 / 5);
		list.add(item);

		return list;
	}

	// 获取产品UIItem数量
	private int getItemListCount() {
		if (getItemList() == null)
			return 0;
		else
			return getItemList().size();
	}

	private LinearLayout mProductLine;

	private LinearLayout getProductLine() {
		if (mProductLine == null) {
			mProductLine = (LinearLayout) findViewById(R.id.message_list);
			if (getItemListCount() > 0) {
				mProductLine.addView(getCustomGrid());
			}
		}
		return mProductLine;
	}

	private View getCustomGrid() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.plan_detail_grid, null);

		mCustomGrid = (CGrid) view.findViewById(R.id.cgrid);
		initGrid();
		// mCustomGrid.setDataInfo(getItemList(), Sqlite.getMsgList(mContext));
		// mCustomGrid.setCustomGridType(Constants.CustomGridType.MESSAGE);
		// mCustomGrid.setLinkHandler(mHandler);

		return view;
	}

	private void initGrid() {
		FieldsList list = Sqlite.getFieldsList(context, 2, "");

		mCustomGrid.setDataInfo(getItemList(), list);
		mCustomGrid.setCustomGridType(Constants.CustomGridType.MESSAGE);
		mCustomGrid.setLinkHandler(mHandler);
	}

	public void onClick(View v) {
		System.out.println(v.getId());
		finishActivity();

	}

	private void initHandler() {
		mHandler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what) {

				case Constants.CustomGridType.MESSAGE:
					if (mCustomGrid.getSelectData() != null)
						showMsgDetail(mCustomGrid.getSelectData());
					break;
				case 1:// 更新消息状态
					mCustomGrid.invalidate();
					break;

				default:
					super.handleMessage(msg);
					break;
				}
			}
		};
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// sendRpt();

			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private AlertDialog mMsgAlertDialog;

	private void showMsgDetail(Fields field) {
		if (mMsgAlertDialog != null) {
			mMsgAlertDialog.dismiss();
			mMsgAlertDialog = null;
		}
		MsgDetailBuilder msg = new MsgDetailBuilder(context, mHandler, field);
		mMsgAlertDialog = msg.create();
		mMsgAlertDialog.show();
	}
	@Override
	protected void onRestart() {
		Tool.setAutoTime(context);
		super.onRestart();
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
			
			// @Override
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
}