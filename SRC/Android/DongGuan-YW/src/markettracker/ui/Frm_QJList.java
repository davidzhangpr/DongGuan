package markettracker.ui;

import orient.champion.business.R;

import markettracker.util.Tool;
import markettracker.adapter.QJListAdapter;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
import android.app.Activity;
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
import android.widget.ListView;

public class Frm_QJList extends Activity implements OnClickListener,
		OnItemClickListener {

	private Context context;
	// private CGrid mCustomGrid;
	// private List<UIItem> mRptDetail;
	private Button exit, add;
	private Handler mHandler;
	// private FieldsList list_qj;
	private ListView lv_qj;
	private QJListAdapter qjListAdapter;
	private Fields selectData;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_qj);
		initHandler();
		init();
	}

	private void toQJ(String key_id) {
		Intent intent = new Intent(context, Frm_Holiday.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("key_id", key_id);
		startActivityForResult(intent, 1000);
	}

	private void init() {
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		add = (Button) findViewById(R.id.addhoc);
		add.setOnClickListener(this);

		context = Frm_QJList.this;
		initGrid();
	}

	private void finishActivity() {

		setResult(RESULT_OK);
		this.finish();
	}

	private void initGrid() {
		lv_qj = (ListView) findViewById(R.id.main_list);
		FieldsList list = Sqlite.getFieldsList(context, 200, "");
		qjListAdapter = new QJListAdapter(context, list);
		lv_qj.setAdapter(qjListAdapter);
		lv_qj.setOnItemClickListener(this);
	}

	private void resetGrid() {
		FieldsList list = Sqlite.getFieldsList(context, 200, "");
		qjListAdapter = new QJListAdapter(context, list);
		lv_qj.setAdapter(qjListAdapter);
	}

	public void onClick(View v) {

		System.out.println(v.getId());

		switch (v.getId()) {
		case R.id.back:
			finishActivity();
			break;

		case R.id.addhoc:
			toQJ("-1");
			break;
		}

	}

	private void initHandler() {
		mHandler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what) {
				case 1:// 更新消息状态
						// mCustomGrid.invalidate();
					qjListAdapter.notifyDataSetChanged();
					break;

				default:
					super.handleMessage(msg);
					break;
				}
			}
		};
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000) {
			resetGrid();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// sendRpt();

			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

//	private AlertDialog mMsgAlertDialog;

//	private void showMsgDetail(Fields field) {
//		if (mMsgAlertDialog != null) {
//			mMsgAlertDialog.dismiss();
//			mMsgAlertDialog = null;
//		}
//		MsgDetailBuilder msg = new MsgDetailBuilder(context, mHandler, field);
//		mMsgAlertDialog = msg.create();
//		mMsgAlertDialog.show();
//	}

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

			// @Override
			public void onClick(DialogInterface arg0, int arg1) {
				exit();
			}
		});
		dialog.show();
	}

	private void exit() {
		setResult(-100);
		this.finish();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		selectData = qjListAdapter.getFields(arg2);
		if (selectData != null) {
			toQJ(selectData.getStrValue("key_id"));
		}

	}
}
