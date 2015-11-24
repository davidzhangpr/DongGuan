package markettracker.ui;

import orient.champion.business.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants;
import markettracker.util.RptTable;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.data.Fields;
import markettracker.data.Panal;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.Template;
import markettracker.data.UpsertResult;
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Frm_Holiday extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private LinearLayout mainLine;
	private ScrollView mainSView;
	private Fields data;

	private Button back, save;

	private Template template;
	private SObject report;

	private TextView title;

	private String key_id;
	private Handler handler;

	private SyncDataApp application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qingjia_frm);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
	}

	private void init() {
		initContext();
		initActivity();
		initHandler();

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		key_id = this.getIntent().getStringExtra("key_id");
		initTitle();
		initReport();
		initPage();
	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		handler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what) {
				case Constants.PropertyKey.SYNCERR:
					// errMsg = msg.obj.toString();
					Tool.showToastMsg(context, "请假失败，请再次提交", AlertType.ERR);
					break;
				case Constants.PropertyKey.UPLOAD:

					if (msg.obj instanceof ArrayList) {
						UpsertResult result = ((ArrayList<UpsertResult>) msg.obj)
								.get(0);
						if (result.isSuccess() == 1) {
							report.setField("serverid", result.getRptId() + "");
							if (report.isSaved()) {
								Tool.showToastMsg(context, "请假删除成功",
										AlertType.INFO);
								DelData();
							} else {
								SaveData();
								Tool.showToastMsg(context, "请假成功",
										AlertType.INFO);
								Tool.sendSMS(
										context,
										Rms.getManagerMobile(context),
										Rms.getUserName(context) + "需要在"
												+ report.getSValue("STR1")
												+ "到"
												+ report.getSValue("STR2")
												+ "请假。谢谢！");
							}

						} else
							Tool.showToastMsg(context, result.getErrorMsg(),
									AlertType.ERR);
					} else
						Tool.showToastMsg(context, "网络异常", AlertType.ERR);
					break;

				default:
					super.handleMessage(msg);
					break;
				}
			}
		};
	}

	// 设置title
	private void initTitle() {
		title = (TextView) findViewById(R.id.txt);
		title.setText("请假");
	}

	private void initReport() {
		template = TemplateFactory.getQJTemplate();
		report = Sqlite.getReport(context, template, "-1", 3, key_id);
		if (report.isSaved()) {
			save.setText("删除");
			save.setTextColor(context.getResources().getColor(R.color.red));
		}
		data = new Fields();

		data.Set("TemplateId", "51");

		data.Set("userid", Rms.getUserId(context));
		data.Set("ReportDate", Tool.getCurrDate());
	}

	private void initPage() {
		initMainView();
	}

	private void initMainView() {
		if (template != null && template.havePanal()) {
			if (mainSView == null) {
				mainSView = (ScrollView) findViewById(R.id.sv_detail_bottom);
				mainSView.addView(getMainLine());
				mainSView.setVisibility(View.VISIBLE);
				// mainSView.setAnimation(Tool.getAnimation(context));
			}
		}
	}

	// 获取描述信息
	private LinearLayout getMainLine() {
		if (mainLine == null) {
			mainLine = new LinearLayout(context);
			mainLine.setOrientation(LinearLayout.VERTICAL);
			// mainLine.setPadding(20, 0, 20, 20);
		}
		for (Panal panal : template.getPanalList()) {

			mainLine.addView(new RptTable(context, panal, report, handler,
					this, null));

		}
		return mainLine;
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back:
			finishActivity();
			break;

		case R.id.save:
			submitData();
			break;

		default:
			break;
		}

	}

	private void submitData() {
		if (report.isSaved()) {
			SObject rpt = new SObject(template);
			rpt.setType("report");
			List<SObject> slist = new ArrayList<SObject>();

			data.Set("INT5", "1");
			data.Set("INT2", report.getField("serverid"));

			rpt.setFields(data);
			slist.add(rpt);
			SyncData.Upload(slist, handler, activity);
		} else {
			if (report.getField("str1").compareTo(report.getField("str2")) > 0) {
				Tool.showToastMsg(context, "开始时间不能小于结束时间", AlertType.ERR);
			} else if (report.getField("str1").compareTo(
					Tool.getCurrDateTime1()) < 0) {
				Tool.showToastMsg(context, "开始时间不能小于当前时间", AlertType.ERR);
			}
			// else if (Tool.getDateChazhi(report.getField("str1"),
			// report.getField("str2")) > 1000 * 60 * 60 * 4) {
			// Tool.showToastMsg(context, "请假时长不能超过4小时", AlertType.ERR);
			// }
			else if (report.getField("int1").equals("")) {
				Tool.showToastMsg(context, "请假类型必须填写", AlertType.ERR);
			} else if (report.getField("int1").equals("5")&&report.getField("str3").equals("")) {
				Tool.showToastMsg(context, "请填写请假原因", AlertType.ERR);
			} else {
				SObject rpt = new SObject(template);
				rpt.setType("report");
				List<SObject> slist = new ArrayList<SObject>();

				data.Set("STR1", report.getField("STR1"));
				data.Set("STR2", report.getField("STR2"));
				data.Set("STR3", report.getField("STR3"));
				data.Set("INT1", report.getField("INT1"));
				rpt.setFields(data);
				slist.add(rpt);
				SyncData.Upload(slist, handler, activity);
			}
		}

	}

	private void SaveData() {
		long index = Sqlite.saveReport(context, report);

		if (index > 0) {
			Tool.showToastMsg(context, "请假保存成功", AlertType.INFO);
			finishActivity();
		} else
			Tool.showToastMsg(context, "请假保存失败", AlertType.ERR);
	}

	private void DelData() {
		try {
			Sqlite.execSingleSql(context,
					"delete from  t_data_callreport where key_id='" + key_id
							+ "'");
			Tool.showToastMsg(context, "请假删除成功", AlertType.INFO);
			finishActivity();
		} catch (Exception e) {
			Tool.showToastMsg(context, "请假删除失败", AlertType.ERR);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		setResult(RESULT_OK);
		
		application.pullActivity(this);
		
		this.finish();
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_Holiday.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_Holiday.this;
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
