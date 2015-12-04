package markettracker.ui;

import orient.champion.business.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants;
import markettracker.util.CustomGrid;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.util.Constants.ControlType;
import markettracker.data.FieldsList;
import markettracker.data.QueryConfig;
import markettracker.data.QueryResult;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.UIItem;
import android.app.Activity;
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

public class Frm_DayRpt extends Activity implements OnClickListener
{
	
	private Context context;
	private Activity activity;
	private CustomGrid customGrid;
	private LinearLayout productLine;
	
	private Button back;
	private View view;
	private String key;
	private Handler handler;
	private TextView title;
	private FieldsList list;
	
	private SyncDataApp application;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.day_rpt);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
	}
	
	private void init()
	{
		initContext();
		initActivity();
		initHandler();
		key = this.getIntent().getStringExtra("key");
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.txt);
		if (key.equals("1"))
			title.setText("门店销量");
		else if (key.equals("2"))
			title.setText("美顾考勤");
		// initTitle();
		// initReport();
		queryData();
		
	}
	
	private void queryData()
	{
		QueryConfig config = new QueryConfig();
		config.setUserId(Rms.getUserId(context));
		config.setIsAll("0");
		config.setStartRow("0");
		
		config.setLastTime(Tool.getCurrDate());
		if (key.equals("1"))
			config.setType("GetClientPromoterSalesList");
		else if (key.equals("2"))
			config.setType("GetDPVisitRltClientSalesList");
		else
			config.setType("GetVisitTotalList");
		
		Tool.showProgress(context, "正在查询，请稍候！", false, null, null);
		SyncData.Query(config, handler, activity);
	}
	
	private void initHandler()
	{
		handler = new Handler()
		{
			// @Override
			public void handleMessage(Message msg)
			{
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what)
				{
					case Constants.PropertyKey.QUERY:
						QueryResult queryResult = (QueryResult) msg.obj;
						list = new FieldsList();
						if (queryResult.getClientTable() != null && queryResult.getClientTable().size() > 0)
						{
							for (SObject data : queryResult.getClientTable())
							{
								list.setFields(data.getFields());
							}
						}
						initPage();
						break;
					
					default:
						super.handleMessage(msg);
						break;
				}
			}
		};
	}
	
	private void finish(int type)
	{
		Intent i = new Intent();
		i.putExtra("type", this.getIntent().getStringExtra("type"));
		setResult(type, i);
		
		application.pullActivity(this);
		this.finish();
	}
	
	private void initPage()
	{
		// initMainView();
		initProductLine();
		// initPhotoLine();
		// setupButton();
	}
	
	private List<UIItem> getItemList()
	{
		List<UIItem> itemList = new ArrayList<UIItem>();
		
		UIItem item = new UIItem();
		item.setCaption("客户");
		item.setWidth((Tool.getScreenWidth()) / 2);
		item.setControlType(ControlType.NONE);
		item.setDataKey("clientname");
		item.setAlign(Align.LEFT);
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("已拜访");
		item.setWidth((Tool.getScreenWidth()) / 4);
		item.setControlType(ControlType.NONE);
		item.setDataKey("isvisit");
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("耗时(分)");
		item.setWidth((Tool.getScreenWidth()) / 4);
		item.setControlType(ControlType.NONE);
		item.setDataKey("timeSpand");
		itemList.add(item);
		
		// item = new UIItem();
		// item.setCaption("缺货");
		// item.setWidth((Tool.getScreenWidth()) / 4-10);
		// item.setControlType(ControlType.NONE);
		// item.setDataKey("isoos");
		// itemList.add(item);
		//
		// item = new UIItem();
		// item.setCaption("价格异动");
		// item.setWidth((Tool.getScreenWidth()) / 4-10);
		// item.setControlType(ControlType.NONE);
		// item.setDataKey("iserrprice");
		// itemList.add(item);
		return itemList;
	}
	
	private List<UIItem> getItemList1()
	{
		List<UIItem> itemList = new ArrayList<UIItem>();
		
		UIItem item = new UIItem();
		item.setCaption("门店名称");
		item.setWidth((Tool.getScreenWidth()) / 2);
		item.setControlType(ControlType.NONE);
		item.setDataKey("OutletName");
		item.setAlign(Align.LEFT);
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("昨天销量");
		item.setWidth((Tool.getScreenWidth()) / 4);
		item.setControlType(ControlType.NONE);
		item.setDataKey("SalesAmount");
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("当月销量");
		item.setWidth((Tool.getScreenWidth()) / 4);
		item.setControlType(ControlType.NONE);
		item.setDataKey("MonthsAmount");
		itemList.add(item);
		
		// item = new UIItem();
		// item.setCaption("缺货");
		// item.setWidth((Tool.getScreenWidth()) / 4-10);
		// item.setControlType(ControlType.NONE);
		// item.setDataKey("isoos");
		// itemList.add(item);
		//
		// item = new UIItem();
		// item.setCaption("价格异动");
		// item.setWidth((Tool.getScreenWidth()) / 4-10);
		// item.setControlType(ControlType.NONE);
		// item.setDataKey("iserrprice");
		// itemList.add(item);
		return itemList;
	}
	
	private List<UIItem> getItemList2()
	{
		List<UIItem> itemList = new ArrayList<UIItem>();
		
		UIItem item = new UIItem();
		item.setCaption("美顾");
		item.setWidth((Tool.getScreenWidth()) / 5);
		item.setControlType(ControlType.NONE);
		item.setDataKey("PromoterName");
		
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("门店名称");
		item.setWidth((Tool.getScreenWidth()) * 3 / 5);
		item.setControlType(ControlType.NONE);
		item.setDataKey("OutletName");
		item.setAlign(Align.LEFT);
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("上班时间");
		item.setWidth((Tool.getScreenWidth()) / 5);
		item.setControlType(ControlType.NONE);
		item.setDataKey("STime");
		itemList.add(item);
		
		// item = new UIItem();
		// item.setCaption("缺货");
		// item.setWidth((Tool.getScreenWidth()) / 4-10);
		// item.setControlType(ControlType.NONE);
		// item.setDataKey("isoos");
		// itemList.add(item);
		//
		// item = new UIItem();
		// item.setCaption("价格异动");
		// item.setWidth((Tool.getScreenWidth()) / 4-10);
		// item.setControlType(ControlType.NONE);
		// item.setDataKey("iserrprice");
		// itemList.add(item);
		return itemList;
	}
	
	private void initProductLine()
	{
		
		if (productLine == null)
		{
			productLine = (LinearLayout) findViewById(R.id.line_rpt_product);
			// productLine.setPadding(20, 0, 20, 20);
			if (key.equals("1"))
				productLine.addView(getCustomGrid(getItemList1()));
			else if (key.equals("2"))
				productLine.addView(getCustomGrid(getItemList2()));
			else
				productLine.addView(getCustomGrid(getItemList()));
			
			// if (!template.havePanal()) {
			// productLine.setVisibility(View.VISIBLE);
			// productLine.setAnimation(Tool.getAnimation(context));
			// }
		}
	}
	
	private void initView()
	{
		if (view == null)
			view = LayoutInflater.from(context).inflate(R.layout.rpt_detail_grid, null);
	}
	
	private void initCustomGrid()
	{
		if (customGrid == null)
		{
			customGrid = (CustomGrid) view.findViewById(R.id.parrow_grid);
		}
	}
	
	private View getCustomGrid(List<UIItem> itemlist)
	{
		initView();
		initCustomGrid();
		customGrid.setDataInfo(itemlist, list);
		// customGrid.setDataHashtable(hashtable);
		return view;
	}
	
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.back:
				
				finish(RESULT_OK);
				break;
			
			default:
				break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish(RESULT_OK);
		}
		return false;
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public void initContext()
	{
		this.context = Frm_DayRpt.this;
	}
	
	public Activity getActivity()
	{
		return activity;
	}
	
	public void initActivity()
	{
		this.activity = Frm_DayRpt.this;
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
