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
import markettracker.util.Constants.AlertType;
import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
import markettracker.data.TemGroupList;
import markettracker.data.TemplateGroup;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;

public class Frm_SelectProduct extends Activity implements OnClickListener
{
	
	private Context context;
	private Activity activity;
	private TemGroupList temGroupList;
	private LinearLayout mainLine;
	private Button exit;
	private LinearLayout lineButton;
	private List<ButtonConfig> buttonlist;
	private ScrollView mainView;
	// private boolean startCall = false, endCall = false;
	
	private Fields comleteRpt = new Fields();
	
	private CTextView selectTView;
	// private String type;
	// private Handler handler;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	private SyncDataApp application;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rpt_product);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
	}
	
	private void exit()
	{
		setResult(-100);
		
		application.pullActivity(this);
		this.finish();
	}
	
	private void init()
	{
		initContext();
		initActivity();
		
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		
		initTemGroupList();
		initPage();
	}
	
	private void initPage()
	{
		initMainSView();
		setupButton();
	}
	
	private void initMainSView()
	{
		if (mainView == null)
		{
			mainView = (ScrollView) findViewById(R.id.sv_content);
			mainView.addView(getMainLine());
//			mainView.setAnimation(Tool.getAnimation(context));
		}
	}
	
	private void initTemGroupList()
	{
		temGroupList = TemplateFactory.getProductGroupList(context);
	}
	
	// 获取描述信息
	private LinearLayout getMainLine()
	{
		if (mainLine == null)
		{
			mainLine = new LinearLayout(context);
			// mainLine.setPadding(20, 0, 20, 20);
			mainLine.setOrientation(LinearLayout.VERTICAL);
		}
		
		// List<String> list = Sqlite.getTemplateIdList(context, clientId);
		List<Fields> list = new ArrayList<Fields>();
		list = Sqlite.getProductIdList(context);
		for (Fields template : list)
			comleteRpt.put(template.getStrValue("ClientId"), "true");
		
		if (temGroupList != null && temGroupList.getTempGroupList() != null)
		{
			for (TemplateGroup group : temGroupList.getTempGroupList())
			{
				mainLine.addView(new CTable(context, group, getOnClickListener(), list));
			}
		}
		
		return mainLine;
	}
	
	private OnClickListener getOnClickListener()
	{
		OnClickListener listener = new OnClickListener()
		{
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				toRpt(v);
			}
		};
		return listener;
	}
	
	private void initButton()
	{
		if (buttonlist == null)
			buttonlist = Tool.getMenuButton();
	}
	
	private void initButtonLine()
	{
		if (lineButton == null)
			lineButton = (LinearLayout) findViewById(R.id.line_buttom);
		else
			lineButton.removeAllViews();
	}
	
	private void setupButton()
	{
		initButton();
		initButtonLine();
		int count = buttonlist.size();
		if (buttonlist != null && count > 0)
		{
			layoutParams.width = Tool.getBWidth(activity, count);
			
			for (int i = 0; i < count; i++)
				lineButton.addView(getRptButton(buttonlist.get(i)));
		}
	}
	
	private CButton getRptButton(ButtonConfig config)
	{
		CButton button = new CButton(context, config, layoutParams, null,this);
//		button.setOnClickListener(this);
		return button;
	}
	
	public void OpenCarmer(int type)
	{
		try
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			startActivityForResult(intent, type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void onClick(View v)
	{
		resetbutton(v.getId());
		
		switch (v.getId())
		{
			case R.id.back:
				finishActivity();
				break;
			
			default:
				toRpt(v);
				break;
		}
	}
	
	private void toRpt(View v)
	{
		if (v instanceof CTextView)
		{
			
			selectTView = (CTextView) v;
			
			toRptClass();
			
		}
	}
	
	private void toRptClass()
	{
		Intent i = new Intent(getContext(), Frm_Rpt.class);
		i.putExtra("type", selectTView.getTemplateType());
		i.putExtra("name", selectTView.getTemplateName());
		i.putExtra("teminalCode", selectTView.getTemplateValue());
		i.putExtra("terminalname",this.getIntent().getStringExtra("name") );
		i.putExtra("clienttype", "1");
		
//		selectTView.gett
		i.putExtra("productid", selectTView.getTemplateValue());

		i.putExtra("key", "-1");
		activity.startActivityForResult(i, 1000);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// super.onActivityResult(requestCode, resultCode, data);
//		mainView.setAnimation(Tool.getAnimation(context));
		String strErrMsg = "";
		try
		{
			if (resultCode == RESULT_OK)
			{
				changeStatus();
			}
		}
		catch (Exception e)
		{
			Tool.showToastMsg(context, "拍照错误" + strErrMsg + e.getMessage(), AlertType.ERR);
			
		}
		// super.onActivityResult(requestCode, resultCode, data);
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
	
	private void changeStatus()
	{
		selectTView.change2Complete();
		if (selectTView.isComplete())
			comleteRpt.put(selectTView.getTemplateValue(), "true");
		// for (TemplateGroup group : temGroupList.getTempGroupList()) {
		// for (Template t : group.getTemplateList()) {
		// if (t.getType().equals(type)) {
		// t.setComplete(true);
		// return;
		// }
		// }
		// }
	}
	
	private void resetbutton(int id)
	{
		CButton b;
		for (int i = 0; i < lineButton.getChildCount(); i++)
		{
			b = ((CButton) lineButton.getChildAt(i));
			if (b.getId() == id)
			{
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, b.getText().toString(), true), null, null);
			}
			else
			{
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, b.getText().toString(), false), null, null);
			}
		}
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
		this.context = Frm_SelectProduct.this;
	}
	
	public Activity getActivity()
	{
		return activity;
	}
	
	public void initActivity()
	{
		this.activity = Frm_SelectProduct.this;
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
