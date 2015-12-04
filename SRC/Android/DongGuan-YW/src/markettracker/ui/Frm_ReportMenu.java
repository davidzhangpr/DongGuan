package markettracker.ui;

import orient.champion.business.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.CButton;
import markettracker.util.CTable;
import markettracker.util.CTextView; 
import markettracker.util.SyncDataApp;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.PhotoType;
import markettracker.util.Constants.AlertType;
import markettracker.data.ButtonConfig;
import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.TemGroupList;
import markettracker.data.Template;
import markettracker.data.TemplateGroup;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Frm_ReportMenu extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private TemGroupList temGroupList;
	private LinearLayout mainLine;
	private Button exit;
	private LinearLayout lineButton;
	private List<ButtonConfig> buttonlist;
	private ScrollView mainView;
	private TextView title;

	private Fields comleteRpt = new Fields();

	private CTextView selectTView;
	private String clientId, type, key, name;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);

	private SyncDataApp application;
	
	private boolean isResult = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rpt_menu_form);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
	}

	@SuppressWarnings("unused")
	private void exit() {
		setResult(-100);
		
		application.pullActivity(this);
		this.finish();
	}

	private void init() {
		initContext();
		initActivity();
		initTitle();
		
		clientId = this.getIntent().getStringExtra("teminalCode");
		type = this.getIntent().getStringExtra("type");

		key = this.getIntent().getStringExtra("key");
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);

		initTemGroupList();
		initPage();
	}
	
	private void initTitle(){
		name = this.getIntent().getStringExtra("name");
		
		title = (TextView) findViewById(R.id.title);
		title.setText(name);
	}

	private void initPage() {
		initMainSView();
		setupButton();
	}

	private void initMainSView() {
		if (mainView == null) {
			mainView = (ScrollView) findViewById(R.id.sv_content);
			mainView.addView(getMainLine());
		}
	}

	private void initTemGroupList() {
		if("纸 品".equals(name)){
			temGroupList = TemplateFactory.getPaperProductsTemplateGroupList(context);
		}else{
			temGroupList = TemplateFactory.getWeiPinTemplateGroupList(context);
		}
	}

	private LinearLayout getMainLine() {
		if (mainLine == null) {
			mainLine = new LinearLayout(context);
			mainLine.setOrientation(LinearLayout.VERTICAL);
		}

		List<String> list = new ArrayList<String>();
		list = Sqlite.getTemplateIdList(context, clientId, key);
		for (String template : list)
			comleteRpt.put(template, "true");

		if (temGroupList != null && temGroupList.getTempGroupList() != null) {
			for (TemplateGroup group : temGroupList.getTempGroupList()) {
				mainLine.addView(new CTable(context, group, list, getOnClickListener()));
			}
		}

		return mainLine;
	}

	private OnClickListener getOnClickListener() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				toRpt(v);
			}
		};
		return listener;
	}

	private void initButton() {
		if (buttonlist == null)
			buttonlist = Tool.getMenuButton();
	}

	private void initButtonLine() {
		if (lineButton == null)
			lineButton = (LinearLayout) findViewById(R.id.line_buttom);
		else
			lineButton.removeAllViews();
	}

	private void setupButton() {
		initButton();
		initButtonLine();
		int count = buttonlist.size();
		if (buttonlist != null && count > 0) {
			layoutParams.width = Tool.getBWidth(activity, count);

			for (int i = 0; i < count; i++)
				lineButton.addView(getRptButton(buttonlist.get(i)));
		}
	}

	private CButton getRptButton(ButtonConfig config) {
		CButton button = new CButton(context, config, layoutParams, null, this);
		return button;
	}

	public void onClick(View v) {
		resetbutton(v.getId());

		switch (v.getId()) {

		case 3:
			Intent intent = new Intent(context, Frm_DPList.class);
			startActivityForResult(intent, 10);
			break;
		case 1:
			intent = new Intent(context, Frm_DayRpt.class);
			intent.putExtra("key", "1");
			startActivityForResult(intent, 10);

			break;
		case 4:
			intent = new Intent(context, Frm_SelectProduct.class);
			startActivityForResult(intent, 10);
			break;

		case 2:
			intent = new Intent(context, Frm_DayRpt.class);
			intent.putExtra("key", "2");
			startActivityForResult(intent, 10);
			break;

		case 5:
			intent = new Intent(context, Frm_ChebliePhoto.class);
			startActivityForResult(intent, 10);

			break;

		case R.id.back:
			finishActivity();
			break;
		
		default:
			toRpt(v);
			break;
		}
	}

	private void saveRpt(String type) {
		Template template = TemplateFactory.getTemplate(getContext(),type);
		SObject report = Sqlite.getReport(context, template, clientId, 1, key);
		report.setField("ClientType", this.type);
		Sqlite.saveReport(context, report);
	}

	private void toRpt(View v) {
		if (v instanceof CTextView) {
			selectTView = (CTextView) v;

			if ("3".equals(selectTView.getTemplateType()) || "13".equals(selectTView.getTemplateType())) { // 促销活动报告,试用装检查报告
				Intent i = new Intent(getContext(), Frm_SalesPromotion.class);
				i.putExtra("type", selectTView.getTemplateType());
				i.putExtra("name", selectTView.getTemplateName());
				i.putExtra("teminalCode", getIntent().getStringExtra("teminalCode"));
				i.putExtra("clienttype", getIntent().getStringExtra("clienttype"));
				i.putExtra("displaytype", getIntent().getStringExtra("displaytype"));
				i.putExtra("terminalname", getIntent().getStringExtra("terminalname"));
				i.putExtra("itemID", "");

				activity.startActivityForResult(i, 1000);
			} else {
				toRptClass("");
			}
		}
	}

	@SuppressWarnings("unused")
	private void changeRptStatus() {
		selectTView.changeStatus();
		if (selectTView.isComplete()) {
			comleteRpt.put(selectTView.getOnlyType(), "true");
			saveRpt(selectTView.getTemplateType());
		} else {
			comleteRpt.put(selectTView.getOnlyType(), "false");
			String sql = "DELETE FROM t_data_callreport where onlyType='" + selectTView.getTemplateType()
					+ "' and ReportDate='" + Tool.getCurrDate() + "' and clientId='" + clientId + "'";
			Sqlite.execSingleSql(context, sql);
		}

	}

	private void toRptClass(String itemID) {
		Intent i = new Intent(getContext(), Frm_Rpt.class);
		i.putExtra("type", selectTView.getTemplateType());
		i.putExtra("name", selectTView.getTemplateName());
		i.putExtra("teminalCode", getIntent().getStringExtra("teminalCode"));
		i.putExtra("clienttype", getIntent().getStringExtra("clienttype"));
		i.putExtra("displaytype", getIntent().getStringExtra("displaytype"));
		i.putExtra("terminalname", getIntent().getStringExtra("terminalname"));
		i.putExtra("itemID", itemID);

		activity.startActivityForResult(i, 1000);
	}
	
	private AlertDialog chooseDialog;

	/**
	 * 选择框
	 */
	private void chooseTrainingTheme(final Context context, String dicId, String title) {
		final List<DicData> itemGroup = Sqlite.getDictDataList(context, dicId, "");
		final String[] themeGroup = new String[itemGroup.size()];
		for (int i = 0; i < itemGroup.size(); i++) {
			themeGroup[i] = itemGroup.get(i).getItemname();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if("5".equals(selectTView.getTemplateType())){
			builder.setTitle(title+" 是否有缺货");
		}else{
			builder.setTitle(title);
		}

		/**
		 * 设置一个单项选择下拉框 第一个参数指定我们要显示的一组下拉单选框的数据集合 第二个参数代表索引，指定默认哪一个单选框被勾选上
		 * 第三个参数给每一个单选项绑定一个监听器
		 */

		builder.setSingleChoiceItems(themeGroup, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, final int which) {
				chooseDialog.dismiss();
				if ((itemGroup.get(0).getValue()).equals(itemGroup.get(which).getValue())) {
					toRptClass(itemGroup.get(0).getValue());
				}else{
					delRpt(itemGroup.get(which).getValue());
				}
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				chooseDialog.dismiss();
			}
		});

		chooseDialog = builder.show();
	}
	
	private void delRpt(String itemID){
		Template temp = TemplateFactory.getTemplate(getContext(), selectTView.getTemplateType());
		
		SObject report = Sqlite.getReport(context, temp, getIntent().getStringExtra("teminalCode"), 1,getIntent().getStringExtra("displaytype"));
		
		report.setField("ClientType", getIntent().getStringExtra("clienttype"));
		
		if(report.getFields().getHashMap().get("int3") != null){
			if("3".equals(selectTView.getTemplateType())){
				report.getFields().getHashMap().put("int4", "");
				report.getFields().getHashMap().put("int5", "");
				report.getFields().getHashMap().put("str1", "");
				report.getFields().getHashMap().put("str2", "");
				report.getFields().getHashMap().put("str3", "");
			}else if("5".equals(selectTView.getTemplateType())){
				for(int i=0; i<report.getDetailfields().getList().size(); i++){
					report.getDetailfields().getList().get(i).getHashMap().put("int1", "2");
				}
			}
		}
		
		report.getFields().getHashMap().put("int1", Rms.getBrandID(context));
		report.getFields().getHashMap().put("int3", itemID);
		
		
		Sqlite.saveReport(context, report);
	}

	@SuppressWarnings("unused")
	private SObject getRpt(PhotoType type) {
		Template template;
		if (type == PhotoType.CHECKIN)
			template = TemplateFactory.getStartTemplate();
		else
			template = TemplateFactory.getLeaveTemplate();
		SObject rpt;
		if (!this.type.equals("2")) {
			rpt = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 1, key);
		} else
			rpt = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 2, key);
		rpt.setField("ClientType", this.type);
		return rpt;
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String strErrMsg = "";
		try {
			if (resultCode == RESULT_OK) {
				 if (requestCode == 10) {

				} else{
					isResult = true;
					changeStatus();
				}
			}else{
				isResult = false;
			}
		} catch (Exception e) {
			Tool.showToastMsg(context, "错误" + strErrMsg + e.getMessage(), AlertType.ERR);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void changeStatus() {
		selectTView.change2Complete();
		if (selectTView.isComplete())
			comleteRpt.put(selectTView.getOnlyType(), "true");
	}

	private void resetbutton(int id) {
		CButton b;
		for (int i = 0; i < lineButton.getChildCount(); i++) {
			b = ((CButton) lineButton.getChildAt(i));
			if (b.getId() == id) {
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, b.getText().toString(), true),
						null, null);
			} else {
				b.setCompoundDrawablesWithIntrinsicBounds(null,
						Tool.getDrawable(context, b.getText().toString(), false), null, null);
			}
		}
	}

	private boolean checkData() {
		for (TemplateGroup group : temGroupList.getTempGroupList()) {
			for (Template t : group.getTemplateList()) {
				if (t.isMustComplete()) {
					if (!comleteRpt.isTrue(t.getOnlyType() + "")) {
						Tool.showToastMsg(context, t.getName() + "必须填写", AlertType.ERR);
						return false;
					}
				}
			}
		}

		return true;
	}

	private void finishActivity() {
		if (checkData()) {
//			if(isResult){
//				setResult(RESULT_OK);
//			}else{
				setResult(RESULT_CANCELED);
//			}
			
			application.pullActivity(this);
			this.finish();
		}
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_ReportMenu.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_ReportMenu.this;
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
