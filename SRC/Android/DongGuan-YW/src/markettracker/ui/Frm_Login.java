package markettracker.ui;

import orient.champion.business.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import markettracker.util.Constants;
import markettracker.util.DBConfig;
import markettracker.util.SyncData; //import markettracker.util.SyncData;
import markettracker.util.Tool;
import markettracker.data.DBMainConfig;
import markettracker.data.FieldsList;
import markettracker.data.LoginConfig;
import markettracker.data.LoginResult;
import markettracker.data.QueryConfig;
import markettracker.data.QueryResult;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("HandlerLeak")
public class Frm_Login extends Activity implements OnClickListener, OnEditorActionListener
{
	private Button login;
	private EditText eName, ePwd;
	private TextView txtHotline;
	private CheckBox checkBox;
	private String sName, sPwd;
	private Context context;
	private Handler handler;
	private FieldsList queryList;
	private int index = 0;
	private QueryConfig config;
	private Activity activity;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();
		Tool.setAutoTime(context);
	}
	
	private void init()
	{
		
		initContext();
		initActivity();
		initScreen();
		initHandler();
		
		login = (Button) findViewById(R.id.btn_login);
		login.setOnClickListener(this);
		
		txtHotline = (TextView) findViewById(R.id.hotline);
		txtHotline.setOnClickListener(this);
		eName = (EditText) findViewById(R.id.edt_name);
		eName.setText(Rms.getUserName(context));
		// eName.setText("liuhm");
		
		ePwd = (EditText) findViewById(R.id.edt_pwd);
		ePwd.setOnEditorActionListener(this);
		if (Rms.isSavePwd(context))
			ePwd.setText(Rms.getPwd(context));
		
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setChecked(Rms.isSavePwd(context));
	}
	
	// private void creatIcon()
	// {
	// Intent startMain = new Intent(Intent.ACTION_MAIN);
	//
	// startMain.addCategory(Intent.CATEGORY_HOME);
	//
	// startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//
	// startActivity(startMain);
	// }
	
	private void checkLogin(LoginResult loginResult)
	{
		try
		{
			Tool.setQueryList(loginResult.getMsgTypeList());
			
			if (Rms.getFirst(context))
			{
				queryList = loginResult.getMsgTypeList();
				
				// Fields data = new Fields();
				// data.put("MsgType", "GetSurveyQuestionOptionsList");
				// data.put("MsgInfo", "下载ssssssssssss");
				// queryList.setFields(data);
				
				config = new QueryConfig();
				config.setUserId(loginResult.getUserId());
				config.setIsAll("0");
				config.setStartRow("0");
				
				config.setLastTime(Tool.getCurrDate());
				config.setType(queryList.getFields(index).getStrValue("MsgType"));
				
				Tool.showProgress(context, queryList.getFields(index).getStrValue("MsgInfo"), false, null, null);
				SyncData.Query(config, handler, activity);
			}
			else
			{
				if (Tool.getCurrDate().equals(Rms.getLoginDate(context)))
					toMenu();
				else
				{
					if (!Tool.getCurrDate().substring(0, 7).equals(Rms.getLoginDate(context).substring(0, 7)))
					{
						Sqlite.execSingleSql(context, "delete from t_data_callReport");
						Sqlite.execSingleSql(context, "delete from t_data_callReportDetail");
						
						String date = Sqlite.getDate(context, "select date('" + Tool.getCurrDate() + "','-1 month','start of month') as date");
						Sqlite.execSingleSql(context, "delete from t_message_detail where stime <'" + date + "'");
						
						Sqlite.execSingleSql(context, "delete from  t_psq_payout where psqid in( select psqid from t_psq where updatetime <'" + date + "')");
						Sqlite.execSingleSql(context, "delete from t_psq_question where psqid in( select psqid from t_psq where updatetime <'" + date + "')");
						Sqlite.execSingleSql(context, "delete from t_psq where updatetime <'" + date + "'");
						
						Sqlite.execSingleSql(context, "delete from t_promotion_plan");
					}
					Sqlite.execSingleSql(context, "delete from t_data_callReportPhoto");
					queryList = loginResult.getMsgTypeList();
					config = new QueryConfig();
					config.setUserId(loginResult.getUserId());
					if (queryList.getFields(index).getStrValue("MsgType").equals("GetClientList"))
						config.setIsAll("0");
					else if (queryList.getFields(index).getStrValue("MsgType").equals("GetDictionaryList"))
						config.setIsAll("0");
					else
						config.setIsAll("1");
					config.setStartRow("0");
					
					config.setLastTime(Rms.getLoginDate(context));
					config.setType(queryList.getFields(index).getStrValue("MsgType"));
					
					if (queryList.getFields(index).getStrValue("MsgType").equals("GetPlanList"))
						Sqlite.execSingleSql(context, "DELETE from  T_Visit_Plan_Detail");
					else if (queryList.getFields(index).getStrValue("MsgType").equals("GetClientList"))
						Sqlite.execSingleSql(context, "DELETE from  T_Outlet_Main");
					else if (queryList.getFields(index).getStrValue("MsgType").equals("GetDictionaryList"))
						Sqlite.execSingleSql(context, "DELETE from  T_Sys_Dictionary");
					
					Tool.showProgress(context, queryList.getFields(index).getStrValue("MsgInfo"), false, null, null);
					SyncData.Query(config, handler, activity);
				}
			}
		}
		catch (Exception e)
		{
			Tool.showErrMsg(context, e.getMessage());
		}
	}
	
	private void initHandler()
	{
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				Tool.stopProgress();
				if (msg != null && msg.obj != null)
				{
					switch (msg.what)
					{
						case Constants.PropertyKey.ERR:
							String errMsg = msg.obj.toString();
							Tool.showErrMsg(context, errMsg);
							break;
						
						case Constants.PropertyKey.LOGIN:
							LoginResult loginResult = (LoginResult) msg.obj;
							if (loginResult.isSuccess() == 1)
							{
								Rms.setUserId(context, loginResult.getUserId());
								Rms.setManagerMobile(context, loginResult.getField("superiorMobile"));
								Rms.setRUserName(context, loginResult.getField("username"));
								
								Rms.setEmpName(context, loginResult.getField("EmpName"));
								
								checkLogin(loginResult);
							}
							else if (loginResult.isSuccess() == -2)
							{
								
								showUpdateDialog(loginResult.getField("AppURL"));
								// Tool.showErrMsg(context,
								// loginResult.getErrorMsg());
							}
							else
							{
								Tool.showErrMsg(context, loginResult.getErrorMsg());
							}
							break;
						
						case Constants.PropertyKey.GETSERVERTIME:
							if (Tool.getServerDate().equals(Tool.getMoblieDate()))
							{
								Tool.showProgress(context, "正在验证用户名密码", false, null, null);
								sName = Tool.getString(eName);
								sPwd = Tool.getString(ePwd);
								index = 0;
								
								if (!Rms.getFirst(context) && !sName.equals(Rms.getUserName(context)))
									clearData();
								
								Rms.setUserName(context, sName);
								Rms.setPwd(context, sPwd);
								Rms.setSavePwd(context, checkBox.isChecked());
								
								LoginConfig config = new LoginConfig();
								config.setUsername(sName);
								config.setPassword(sPwd);

								config.setImei("1");
								config.setApiVersion(Constants.CurVersion.VERSION);
								config.setDeviceType("sales");
								
								SyncData.Login(config, handler, activity);
							}
							else
								Tool.showErrMsg(context, "请调整好您的手机时间再进行登录！");
							
							break;
						
						case Constants.PropertyKey.QUERY:
							QueryResult queryResult = (QueryResult) msg.obj;
							if (queryResult.isSuccess() == 1)
							{
								index++;
								if (index < queryList.size())
								{
									
									if (!Rms.getFirst(context) && !queryList.getFields(index).getStrValue("MsgType").equals("GetClientList") && !queryList.getFields(index).getStrValue("MsgType").equals("GetDictionaryList"))
										config.setIsAll("1");
									else
										config.setIsAll("0");
									
									config.setStartRow("0");
									config.setType(queryList.getFields(index).getStrValue("MsgType"));
									
									if (queryList.getFields(index).getStrValue("MsgType").equals("GetPlanList"))
										Sqlite.execSingleSql(context, "DELETE from  T_Visit_Plan_Detail");
									else if (queryList.getFields(index).getStrValue("MsgType").equals("GetClientList"))
										Sqlite.execSingleSql(context, "DELETE from  T_Outlet_Main");
									else if (queryList.getFields(index).getStrValue("MsgType").equals("GetDictionaryList"))
										Sqlite.execSingleSql(context, "DELETE from  T_Sys_Dictionary");
									
									Tool.showProgress(context, queryList.getFields(index).getStrValue("MsgInfo"), false, null, null);
									SyncData.Query(config, handler, activity);
								}
								else
								{
									Rms.setFirst(context, false);
									Rms.setLoginDate(context, Tool.getCurrDate());
									
									toMenu();
								}
							}
							else
							{
								Tool.showErrMsg(context, queryResult.getErrorMsg());
							}
							break;
						
						default:
							super.handleMessage(msg);
							break;
					}
				}
				else
					Tool.showErrMsg(context, "网络异常");
			}
		};
	}
	
	// @Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		switch (actionId)
		{
			case EditorInfo.IME_ACTION_DONE:
				if (v.getId() == R.id.edt_pwd)
				{
					Login();
				}
				break;
		}
		return true;
	}
	
	private String checklog()
	{
		if (Tool.isEmpty(eName))
			return "请输入用户名";
		else if (Tool.isEmpty(ePwd))
			return "请输入密码";
		else if (!Tool.isConnect(context))
			return "网络连接失败，请确认网络连接！";
		// else if (!Tool.isSameUser(context, Tool.getString(eName))) {
		// return "此用户和之前登陆用户不符，不能登陆！";
		// }
		return "";
	}
	
	private void Login()
	{
		try
		{
			
			String err = checklog();
			if (err.equals(""))
			{
				Tool.showProgress(context, "正在校验手机时间", false, null, null);

				SyncData.GetServerTime(handler, activity);
				
			}
			else
				Tool.showErrMsg(context, err);
		}
		catch (Exception e)
		{
			Tool.showErrMsg(context, e.getMessage());
		}
	}
	
	private void clearData()
	{
		List<String> list = new ArrayList<String>();
		
		for (DBMainConfig db : DBConfig.getTableList())
		{
			list.add("delete from " + db.getTableName());
		}
		Sqlite.execSqlList(context, list);
		Rms.setFirst(context, true);
		Rms.setUserId(context, "");
	}
	
	private void toMenu()
	{
		Rms.setLoginDate(context, Tool.getCurrDate());
		
		Intent i = new Intent(Frm_Login.this, Frm_MainMenu.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Frm_Login.this.startActivity(i);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		this.finish();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			showExitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showExitDialog()
	{
		Builder dialog = new Builder(context);
		dialog.setTitle("提示");
		dialog.setMessage("确认退出？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			
			// @Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
				// finish();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}
	
	public void showUpdateDialog(final String appUrl)
	{
		Builder mDialog = new Builder(context);
		mDialog.setTitle("提示");
		mDialog.setMessage(Constants.CurVersion.UPDATEMSG);
		mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				/* User clicked OK so do some stuff */
				dialog.dismiss();
				downFile(appUrl);
				
			}
		});
		mDialog.show();
	}
	
	void downFile(final String url)
	{
		Tool.showProgress(context, "正在升级，请稍候", false, null, null);
		new Thread()
		{
			public void run()
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try
				{
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null)
					{
						// File file = new File("/sdcard/android/kowa.apk");
						
						File file = null;
						file = new File(Environment.getExternalStorageDirectory(), Constants.CurVersion.APPNAME);
						
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
//						int count = 0;
						while ((ch = is.read(buf)) != -1)
						{
							fileOutputStream.write(buf, 0, ch);
//							count += ch;
							if (length > 0)
							{
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null)
					{
						fileOutputStream.close();
					}
					down();
				}
				catch (ClientProtocolException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	void down()
	{
		handler.post(new Runnable()
		{
			public void run()
			{
				Tool.stopProgress();
				update();
			}
		});
	}
	
	void update()
	{
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Constants.CurVersion.APPNAME)), "application/vnd.android.package-archive");
		
		startActivity(intent);
	}
	
	private void callHotline()
	{
		Tool.sendCall(context,"4006729851");
	}
	
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_login:
				Login();
				break;
			
			case R.id.hotline:
				callHotline();
				break;
				
			default:
				break;
		}
	}
	
	private void initScreen()
	{
		Tool.getScreen(activity);
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public Handler getHandler()
	{
		return handler;
	}
	
	public void initActivity()
	{
		this.activity = Frm_Login.this;
	}
	
	public void initContext()
	{
		this.context = Frm_Login.this;
	}
	
}
