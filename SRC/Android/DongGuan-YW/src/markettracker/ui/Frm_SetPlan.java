package markettracker.ui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import orient.champion.business.R;

import markettracker.util.CGrid;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.util.Constants.ControlType;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.Template;
import markettracker.data.UIItem;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Frm_SetPlan extends Activity implements OnClickListener
{

	private Context context;
	private Activity activity;
	private Button exit, save;
	private LinearLayout linePlanList;
	private CGrid customGrid;
	private String selectedDate;
	private TextView selectDate;
	private SObject report;
	private EditText searchClient;

	private SyncDataApp application;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setplan_form);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
	}

	private void init()
	{
		initContext();
		initActivity();
		selectedDate = getExtraResult("selectedDate");
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);

		searchClient = (EditText) findViewById(R.id.searchclient);
		searchClient.addTextChangedListener(getTextWatcher());

		initReport();

		initPage();
	}

	private void initReport()
	{
		report = new SObject();
		report.setCallDate(selectedDate);
	}

	private TextWatcher getTextWatcher()
	{
		TextWatcher watcher = new TextWatcher()
		{

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			public void afterTextChanged(Editable s)
			{
				Editable editable = searchClient.getText();
				int len = editable.length();
				if (len != 0)
				{
					report.addDetailfields(customGrid.getDataList());

					Fields data = new Fields();
					data.put("code", editable.toString());
					FieldsList list = Sqlite.getFieldsList(context, 1, data);
					customGrid.setDataInfo(getItemList(), list);
					customGrid.setDataHashtable(initHashtable(list));
					customGrid.invalidate();
				}
				else
				{
					report.addDetailfields(customGrid.getDataList());
					getAllClient();
				}
			}
		};
		return watcher;
	}

	private void getAllClient()
	{
		Fields data = new Fields();
		data.put("date", selectedDate);

		FieldsList list = Sqlite.getFieldsList(context, 5, data);
		customGrid.setDataInfo(getItemList(), list);
		customGrid.setDataHashtable(initHashtable(list));
	}

	private String getExtraResult(String field)
	{
		return this.getIntent().getStringExtra(field);
	}

	private void initPage()
	{

		selectDate = (TextView) findViewById(R.id.txt);
		selectDate.setText(selectedDate);

		linePlanList = (LinearLayout) findViewById(R.id.planlist);
		linePlanList.addView(getCustomGrid());
	}

	private View getCustomGrid()
	{
		View view = LayoutInflater.from(this).inflate(R.layout.plan_detail_grid, null);
		customGrid = (CGrid) view.findViewById(R.id.cgrid);
		getAllClient();

		return view;
	}

	private Hashtable<String, Fields> initHashtable(FieldsList list)
	{
		Hashtable<String, Fields> hashtable = new Hashtable<String, Fields>();
		if (list.size() > 0)
		{
			if (report.getDetailCount() > 0)
			{
				for (Fields data : report.getDetailfields().getList())
				{
					if (data.getStrValue("selected").equals("1"))
					{
						for (Fields data1 : list.getList())
						{
							if (data.getStrValue("serverid").equals(data1.getStrValue("serverid")))
							{
								for (UIItem item : getItemList())
								{
									if (!item.getControlType().equals(ControlType.NONE))
										data1.put(item.getDataKey(), data.getStrValue(item.getDataKey()));
								}
								hashtable.put(data.getStrValue("serverid"), data1);
								break;
							}
						}
					}
				}
			}
			else
			{
				for (Fields data : list.getList())
				{
					if (data.getStrValue("selected").equals("1"))
						hashtable.put(data.getStrValue("serverid"), data);
				}
			}
		}
		return hashtable;
	}

	private List<UIItem> getItemList()
	{
		List<UIItem> itemList = new ArrayList<UIItem>();
		UIItem item = new UIItem();
		item.setCaption("客户");
		item.setWidth((Tool.getScreenWidth()) * 3 / 4);
		item.setControlType(ControlType.NONE);
		item.setDataKey("fullname");
		item.setAlign(Align.LEFT);
		itemList.add(item);

		item = new UIItem();
		item.setCaption("选择");
		item.setWidth((Tool.getScreenWidth()) / 4);
		item.setControlType(ControlType.SELECTED);
		item.setDataKey("selected");
		item.setValidate(1);
		itemList.add(item);

		return itemList;
	}

	public void onClick(View v)
	{

		switch (v.getId())
		{
			case R.id.back:
				finish(RESULT_CANCELED);
				break;
			case R.id.save:
				savePlan();
				break;

			default:
				break;
		}
	}

	private void savePlan()
	{
		report.setField("templateid", "-100");
		report.setField("onlytype", "-100");
		report.setField("reportdate", selectedDate);
		report.addDetailfields(customGrid.getDataList());
		
		long index = Sqlite.savePlan(context, report);
		Sqlite.setPlan(context, report);
		if (index > 0)
		{
			finish(RESULT_OK);
		}
		else{
			Tool.showToastMsg(context, "计划设定失败", AlertType.ERR);
		}
	}
	
	private void finish(int type)
	{
		setResult(type);
		
		application.pullActivity(this);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish(RESULT_CANCELED);
		}
		return false;
	}

	private void finishActivity()
	{
		setResult(RESULT_OK);
		
		application.pullActivity(this);
		this.finish();
	}

	public Context getContext()
	{
		return context;
	}

	public void initContext()
	{
		this.context = Frm_SetPlan.this;
	}

	public Activity getActivity()
	{
		return activity;
	}

	public void initActivity()
	{
		this.activity = Frm_SetPlan.this;
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