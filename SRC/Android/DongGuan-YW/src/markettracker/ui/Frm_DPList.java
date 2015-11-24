package markettracker.ui;

import orient.champion.business.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants.ControlType;
import markettracker.util.CGrid;
import markettracker.util.Constants;
import markettracker.util.ReSetPwdBuilder;
import markettracker.util.ShowInfoBuilder;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
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

public class Frm_DPList extends Activity implements OnClickListener
{
	
	private Context context;
	private CGrid mCustomGrid;
	private List<UIItem> mRptDetail;
	private Button exit, add;
	private Handler mHandler;
	
	private Fields selectData;
	
	private SyncDataApp application;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dp_list);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		initHandler();
		init();
	}
	
	private void init()
	{
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		
		add = (Button) findViewById(R.id.addhoc);
		add.setOnClickListener(this);
		
		context = Frm_DPList.this;
		getProductLine();
	}
	
	private void finishActivity()
	{
		setResult(RESULT_OK);
		
		application.pullActivity(this);
		this.finish();
	}
	
	/**
	 * 获取产品UIItem
	 * @return
	 */
	private List<UIItem> getItemList()
	{
		if (mRptDetail == null)
			mRptDetail = getMsgItem();
		return mRptDetail;
	}
	
	public static List<UIItem> getMsgItem()
	{
		List<UIItem> list = new ArrayList<UIItem>();
		
		UIItem item = new UIItem();
		item.setCaption("姓名");
		item.setControlType(ControlType.NONE);
		item.setDataKey("promotername");
		item.setWidth((Tool.getScreenWidth()) / 4);
		// item.setAlign(Align.LEFT);
		list.add(item);
		
		item = new UIItem();
		item.setCaption("门店");
		item.setControlType(ControlType.NONE);
		item.setDataKey("fullname");
		item.setWidth((Tool.getScreenWidth()) / 2);
		item.setAlign(Align.LEFT);
		list.add(item);
		
		item = new UIItem();
		item.setCaption("是否排班");
		item.setControlType(ControlType.NONE);
		item.setDataKey("str1");
		item.setWidth((Tool.getScreenWidth()) / 4);
		// item.setAlign(Align.LEFT);
		list.add(item);
		
		return list;
	}
	
	// 获取产品UIItem数量
	private int getItemListCount()
	{
		if (getItemList() == null)
			return 0;
		else
			return getItemList().size();
	}
	
	private LinearLayout mProductLine;
	
	private LinearLayout getProductLine()
	{
		if (mProductLine == null)
		{
			mProductLine = (LinearLayout) findViewById(R.id.message_list);
			if (getItemListCount() > 0)
			{
				mProductLine.addView(getCustomGrid());
			}
		}
		return mProductLine;
	}
	
	private View getCustomGrid()
	{
		View view = LayoutInflater.from(context).inflate(R.layout.plan_detail_grid, null);
		
		mCustomGrid = (CGrid) view.findViewById(R.id.cgrid);
		initGrid();
		
		return view;
	}
	
	private void initGrid()
	{
		FieldsList list = Sqlite.getFieldsList(context, 13, "");
		
		mCustomGrid.setDataInfo(getItemList(), list);
		mCustomGrid.setCustomGridType(Constants.CustomGridType.SELECTCX);
		mCustomGrid.setLinkHandler(mHandler);
	}
	
	public void onClick(View v)
	{
		System.out.println(v.getId());
		
		switch (v.getId())
		{
			case R.id.back:
				finishActivity();
				break;
			
			case R.id.addhoc:
				toRpt();
				break;
		}
		
	}
	
	private void toRpt()
	{
		Intent i = new Intent(context, Frm_SubmitRpt.class);
		i.putExtra("type", "20000");
		i.putExtra("name", "新建美顾");
		i.putExtra("teminalCode", "-1");
		
		
		i.putExtra("PromoterId", "");
		i.putExtra("clienttype", "-1");
		
		Frm_DPList.this.startActivityForResult(i, 1000);
		
	}
	
	private void toRpt1()
	{
		Intent i = new Intent(context, Frm_SubmitRpt.class);
		i.putExtra("type", "20000");
		i.putExtra("name", "修改美顾");
		i.putExtra("teminalCode", "-1");
		i.putExtra("clienttype", "-1");
		
		i.putExtra("PromoterId", selectData.getStrValue("PromoterId"));
		
		Frm_DPList.this.startActivityForResult(i, 1000);
		
	}
	
	private void resetGrid()
	{
		FieldsList list = Sqlite.getFieldsList(context, 13, "");
		
		mCustomGrid.setDataInfo(getItemList(), list);
		// mCustomGrid.setCustomGridType(Constants.CustomGridType.SELECTCX);
		// mCustomGrid.setLinkHandler(mHandler);
		mCustomGrid.invalidate();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// super.onActivityResult(requestCode, resultCode, data);
		// mainView.setAnimation(Tool.getAnimation(context));
		
		if (resultCode == RESULT_OK)
		{
			resetGrid();
		}
		
		// super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initHandler()
	{
		mHandler = new Handler()
		{
			// @Override
			public void handleMessage(Message msg)
			{
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what)
				{
				
					case Constants.CustomGridType.SELECTCX:
						if (mCustomGrid.getSelectData() != null)
						{
							selectData = mCustomGrid.getSelectData();
							showInfo();
						}
						break;
					
					default:
						super.handleMessage(msg);
						break;
				}
			}
		};
	}
	
	private ShowInfoBuilder showInfoBuild;
	
	private void showInfo()
	{
		if (showInfoBuild != null)
		{
			showInfoBuild.dismiss();
			showInfoBuild = null;
		}
		showInfoBuild = new ShowInfoBuilder(context, getOnClickListener(), selectData.getStrValue("promotername"));
		// restpwdAlertDialog = msg.create();
		// restpwdAlertDialog.show();
	}
	
	private OnClickListener getOnClickListener()
	{
		OnClickListener listener = new OnClickListener()
		{
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				switch (v.getId())
				{
					case R.id.set:
						Intent i = new Intent(context, Frm_Plan.class);
						
						i.putExtra("type", "20001");
						i.putExtra("name", "美顾排班");
						i.putExtra("teminalCode", selectData.getStrValue("PromoterId"));
						
						// i.putExtra(Constants.MOBILEKEY,
						// Tool.getMyUUID());
						
						i.putExtra("clienttype", "-1");
						
						startActivityForResult(i, 1000);
						break;
					
					case R.id.edit:
						toRpt1();
						break;
				}
				
				showInfoBuild.dismiss();
				// delView(v.getId());
			}
		};
		return listener;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
