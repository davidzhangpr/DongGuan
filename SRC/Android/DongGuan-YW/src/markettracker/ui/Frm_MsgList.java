package markettracker.ui;


import markettracker.util.Constants;
import markettracker.util.MsgDetailBuilder;
import markettracker.util.Tool;
import orient.champion.business.R;
import markettracker.adapter.MsgListAdapter;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
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
import android.widget.ListView;

public class Frm_MsgList extends Activity implements OnClickListener,
		OnItemClickListener {

	private Context context;
	private Button exit;
	private Handler mHandler;
	private ListView lv_msg;
	private MsgListAdapter msgListAdapter;
	private Fields selectData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message);
		initHandler();
		init();
	}

	private void init() {
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		context = Frm_MsgList.this;
		initGrid();
	}

	private void finishActivity() {
		setResult(RESULT_OK);
		this.finish();
	}

	private void initGrid() {
		lv_msg = (ListView) findViewById(R.id.main_list);
		FieldsList list = Sqlite.getFieldsList(context, 2, "-1");
		msgListAdapter = new MsgListAdapter(context, list);
		lv_msg.setAdapter(msgListAdapter);
		lv_msg.setOnItemClickListener(this);
	}

	public void onClick(View v) {
		finishActivity();
	}

	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				Tool.stopProgress();
				switch (msg.what) {
				case Constants.CustomGridType.MESSAGE:
					break;
				case 1:// 更新消息状态
					msgListAdapter.notifyDataSetChanged();
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
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
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
		selectData = msgListAdapter.getFields(arg2);
		if (selectData != null)
			showMsgDetail(selectData);
	}
}
