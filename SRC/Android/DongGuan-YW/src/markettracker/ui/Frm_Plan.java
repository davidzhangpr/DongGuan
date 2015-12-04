package markettracker.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import markettracker.util.CButton;
import markettracker.util.CDateButton;
import markettracker.util.CGrid;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.util.Constants.ControlType;
import orient.champion.business.R;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class Frm_Plan extends Activity implements OnClickListener, OnGestureListener
{
	
	private Context context;
	private Activity activity;
	private Button exit, addplan, today;
	private LinearLayout lineDate, linePlanList;
	private ViewFlipper flipDate;
	private TextView month;
	private ImageButton pre, next, fastbacked, fastforward;
	private CGrid customGrid;
	private String selectedDate;
	private FieldsList weeklist;
	
	private GestureDetector detector;
	
	private SyncDataApp application;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plan_form);
		init();
		
		application = (SyncDataApp) getApplication();
		application.pushActivity(this);
	}
	
	private void init()
	{
		initContext();
		initActivity();
		
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		addplan = (Button) findViewById(R.id.addplan);
		addplan.setOnClickListener(this);
		today = (Button) findViewById(R.id.today);
		today.setOnClickListener(this);
		
		pre = (ImageButton) findViewById(R.id.backed);
		pre.setOnClickListener(this);
		next = (ImageButton) findViewById(R.id.forward);
		next.setOnClickListener(this);

		fastbacked = (ImageButton) findViewById(R.id.fastbacked);
		fastbacked.setOnClickListener(this);
		fastforward = (ImageButton) findViewById(R.id.fastforward);
		fastforward.setOnClickListener(this);
		
		initPage();
	}
	
	private void initPage()
	{
		selectedDate = Tool.getCurrDate();
		month = (TextView) findViewById(R.id.month);
		setMonth();
		initday();
	}
	
	private void setMonth()
	{
		month.setText(selectedDate.substring(0, 7));
	}
	
	private void initday()
	{
		flipDate = (ViewFlipper) findViewById(R.id.flipday);
		detector = new GestureDetector(this);
		
		lineDate = (LinearLayout) findViewById(R.id.day);
		weeklist = Tool.getWeekDays(selectedDate);
		for (Fields data : weeklist.getList())
		{
			lineDate.addView(new CDateButton(context, data, getOnClickListener()));
		}
		linePlanList = (LinearLayout) findViewById(R.id.planlist);
		linePlanList.addView(getCustomGrid());
	}
	
	private View getCustomGrid()
	{
		View view = LayoutInflater.from(this).inflate(R.layout.plan_detail_grid, null);
		
		customGrid = (CGrid) view.findViewById(R.id.cgrid);
		
		Fields condition = new Fields();
		condition.put("date", selectedDate);
		FieldsList list = Sqlite.getFieldsList(context, 0, condition);
		
		customGrid.setDataInfo(getItemList(), list);
		
		return view;
	}
	
	private List<UIItem> getItemList()
	{
		List<UIItem> itemList = new ArrayList<UIItem>();
		UIItem item = new UIItem();
		item.setCaption("客户");
		item.setWidth((Tool.getScreenWidth()));
		item.setControlType(ControlType.NONE);
		item.setDataKey("terminalName");
		item.setAlign(Align.LEFT);
		itemList.add(item);
		
		return itemList;
	}
	
	private void resetday()
	{
		lineDate.removeAllViews();
		
		weeklist = Tool.getWeekDays(selectedDate);
		for (Fields data : weeklist.getList())
		{
			lineDate.addView(new CDateButton(context, data, getOnClickListener()));
		}
		
		resetGrid();
	}
	
	private void resetSelectday(String selectDate)
	{
		lineDate.removeAllViews();
		for (Fields data : weeklist.getList())
		{
			
			if (data.getIntValue("date") == Integer.parseInt(selectDate.substring(8, 10)))
				data.put("select", "1");
			else
				data.put("select", "0");
			
			lineDate.addView(new CDateButton(context, data, getOnClickListener()));
		}
		resetGrid();
	}
	
	private OnClickListener getOnClickListener()
	{
		OnClickListener listener = new OnClickListener()
		{
			
			public void onClick(View v)
			{
				selectedDate = ((CButton) v).getdate();
				setMonth();
				resetSelectday(selectedDate);
			}
		};
		return listener;
	}
	
	private boolean checkDate(){
		boolean isOK = false;
		int dayScalar = 0;
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			int week = getCurrentWeekOfDate(dateFormat.parse(selectedDate));
			int poor = (7-week) == 7 ? 0 : 7-week;
			
			switch (getCurrentWeekOfDate(dateFormat.parse(Tool.getCurrDate()))) {
			case 0:
				dayScalar = 7;
				break;
			case 1:
				dayScalar = 6;
				break;
			case 2:
				dayScalar = 5;
				break;
			case 3:
				dayScalar = 4;
				break;
			case 4:
				dayScalar = 3;
				break;
			case 5:
				dayScalar = 2;
				break;
			case 6:
				dayScalar = 1;
				break;
			}
			
			if ((getIntervalDays(dateFormat.parse(Tool.getCurrDate()),dateFormat.parse(selectedDate)) + poor) <= dayScalar)
			{
				Tool.showToastMsg(context, "不能设定本周及本周以前的计划", AlertType.ERR);
				isOK = false;
			}
			else{
				isOK = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isOK;
	}
	
	/**
	 * 根据用户选择的日期得到星期几
	 * 0:星期日
	 * 1:星期一
	 * 2:星期二
	 * 3:星期三
	 * 4:星期四
	 * 5:星期五
	 * 6:星期六
	 * @param date
	 * @return
	 */
	public int getCurrentWeekOfDate(Date date){
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK)-1;
        if (week < 0){
        	week = 0;
        }
        return week;
	}
	
	/**
	 * 计算两个日期之间的天数
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public int getIntervalDays(Date fDate, Date oDate){
		long intervalMilli = oDate.getTime() - fDate.getTime();
		
	    return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}
	
	public void onClick(View v)
	{
		
		switch (v.getId())
		{
			case R.id.back:
				finishActivity();
				break;
				
			case R.id.addplan:
				if (checkDate())
				{
					Intent intent = new Intent(context, Frm_SetPlan.class);
					intent.putExtra("selectedDate", selectedDate);
					startActivityForResult(intent, 1000);
				}
				break;
				
			case R.id.backed:
				selectedDate = Sqlite.getDate(context, "select date('" + selectedDate + "','-1 month','start of month') as date");
				setMonth();
				resetday();
				break;
				
			case R.id.forward:
				selectedDate = Sqlite.getDate(context, "select date('" + selectedDate + "','+1 month','start of month') as date");
				setMonth();
				resetday();
				break;

			case R.id.fastbacked:
				selectedDate = Sqlite.addDate(context, selectedDate, "-7");
				setMonth();
				resetday();
				break;

			case R.id.fastforward:
				selectedDate = Sqlite.addDate(context, selectedDate, "+7");
				setMonth();
				resetday();
				break;
			
			case R.id.today:
				selectedDate = Tool.getCurrDate();
				setMonth();
				resetday();
				break;
			
			default:
				break;
		}
	}
	
	private void resetGrid()
	{
		Fields condition = new Fields();
		condition.put("date", selectedDate);
		FieldsList list = Sqlite.getFieldsList(context, 0, condition);
		customGrid.setDataInfo(getItemList(), list);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1000)
		{
			if (resultCode == RESULT_OK)
			{
				if (Tool.isConnect(context)) {
					Tool.showProgress(context, "上传计划中...", false, null, null);
					SyncData.startSyncData(activity);
				} else {
					Tool.showErrMsg(context, "请打开网络连接");
				}
				
				resetGrid();
			}
			else
			{
				
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finishActivity();
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
		this.context = Frm_Plan.this;
	}
	
	public Activity getActivity()
	{
		return activity;
	}
	
	public void initActivity()
	{
		this.activity = Frm_Plan.this;
	}
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		
		if (Math.abs(e1.getY() - e2.getY()) > 100)
		{
			return false;
		}
		if (e1.getX() - e2.getX() > 120)
		{
			selectedDate = Sqlite.addDate(context, selectedDate, "+7");
			setMonth();
			
			// flipDate.setInAnimation(slideLeftIn);
			resetday();
			
			// flipDate.setInAnimation(Tool.getAnimation(context,
			// R.anim.slide_left_in));
			// flipDate.setOutAnimation(Tool.getAnimation(context,
			// R.anim.slide_left_out));
			//
			// flipDate.showNext();
			return true;
		}
		else if (e1.getX() - e2.getX() < -120)
		{
			selectedDate = Sqlite.addDate(context, selectedDate, "-7");
			setMonth();
			resetday();
			
			// flipDate.setInAnimation(Tool.getAnimation(context,
			// R.anim.slide_right_in));
			// flipDate.setOutAnimation(Tool.getAnimation(context,
			// R.anim.slide_right_out));
			// flipDate.showPrevious();
			return true;
		}
		
		return true;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		boolean flag = detector.onTouchEvent(ev);
		if (!flag)
		{
			flag = super.dispatchTouchEvent(ev);
		}
		return flag;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// 需要通过手势识别器 去识别触摸的动作
		detector.onTouchEvent(event);
		return true;
	}
	
	public boolean onDown(MotionEvent e)
	{
		return false;
	}
	
	public void onLongPress(MotionEvent e)
	{
		
	}
	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return false;
	}
	
	public void onShowPress(MotionEvent e)
	{
		
	}
	
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}
	
	@Override
	protected void onResume()
	{
		if (Rms.getIsConnent(context)) { // 是从手机打开网络连接的页面跳转过来的
			Rms.setIsConnent(context, false);
			if (Tool.isConnect(context)) {
				Tool.showProgress(context, "上传计划中...", false, null, null);
				SyncData.startSyncData(activity);
			} else {
				Tool.showErrMsg(context, "请打开网络连接");
			}
		}
		
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
