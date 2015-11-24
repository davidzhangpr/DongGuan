package markettracker.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import orient.champion.business.R;

import markettracker.util.CTable;
import markettracker.util.CTextView;
import markettracker.util.Constants;
import markettracker.util.ElectronicBuilder;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Frm_ElectornicList extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private List<FieldsList> groupList;
	private LinearLayout mainLine;
	private ImageView exit;
	private Fields selectData;
	private CTextView selectTxtView;

	private Handler handler;
	private SyncDataApp application;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frm_electronic);

		init();
		
		application = (SyncDataApp) getApplication();
		application.pushActivity(this);

	}

	private void initScreen() {
		Tool.getScreen(activity);
	}

	private void init() {
		initContext();
		initActivity();

		initHandler();
		initScreen();

		exit = (ImageView) findViewById(R.id.back);
		exit.setOnClickListener(this);

		initTemGroupList();
		initPage();
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.obj != null) {
					switch (msg.what) {

					case 1:
						selectTxtView.setText("  "
								+ selectData.getStrValue("AttachmentName")
								+ selectData.getStrValue("AttachmentType")
								+ "(" + selectData.getStrValue("str3")
								+ ")"
								+ "(" + selectData.getStrValue("status")
								+ ")");
						Tool.showToastMsg(context, "下载完成", AlertType.INFO);
						break;
					
					default:
						super.handleMessage(msg);
						
						break;
					}
				} else
					Tool.showErrMsg(context, "异常错误");
			}
		};
	}

	private void initPage() {
		initMainLine();
	}

	private void initTemGroupList() {
		groupList = Sqlite.getElectornicGroups(context);
	}

	// 获取描述信息
	private LinearLayout initMainLine() {
		mainLine = (LinearLayout) findViewById(R.id.list_electronic);
		FieldsList panal;
		for (int i = 0; i < groupList.size(); i++) {
			panal = groupList.get(i);
			mainLine.addView(new CTable(context, panal, getEocOnClickListener()));
		}
		return mainLine;
	}

	private OnClickListener getEocOnClickListener() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				selectTxtView = (CTextView) v;

				selectData = selectTxtView.ziliaoField;
				if (selectData.getStrValue("status").equals("未下载")) {
					showDownLoadDialog();
				} else
					open(selectData);
			}
		};
		return listener;
	}

	private void showDownLoadDialog() {
		Builder dialog = new Builder(context);
		dialog.setTitle("提示");
		dialog.setMessage("下载会产生流量，建议在WIFI环璄下载！确认下载？");
		dialog.setNeutralButton("取消", null);
		dialog.setNegativeButton("下载", new DialogInterface.OnClickListener() {
			// @Override
			public void onClick(DialogInterface arg0, int arg1) {
				Tool.showProgress(context, "正在下载");
				download();
			}
		});
		dialog.show();
	}

	private void download() {

		new Thread() {
			public void run() {
				String name = "";
				HttpResponse response;
				HttpGet get = null;
				HttpClient client = new DefaultHttpClient();
				if (!selectData.getStrValue("AttachmentURL").equals("")) {
					name = getResources().getString(R.string.folder_name) + selectData.getStrValue("serverid")
							+ selectData.getStrValue("AttachmentType");
					try {
						get = new HttpGet(
								selectData.getStrValue("AttachmentURL"));
					} catch (Exception e) {
						
					}
				} else {
					get = null;
				}

				try {
					if (get != null) {
						response = client.execute(get);
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						InputStream is = entity.getContent();
						FileOutputStream fileOutputStream = null;
						if (is != null) {
							File file = new File(
									Environment.getExternalStorageDirectory(),
									name);
							fileOutputStream = new FileOutputStream(file);
							byte[] buf = new byte[1024];
							int ch = -1;
							while ((ch = is.read(buf)) != -1) {
								fileOutputStream.write(buf, 0, ch);
								if (length > 0) {
								}
							}
						}
						fileOutputStream.flush();
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
						if (selectData != null) {
							Tool.stopProgress();
							selectData.put("status", "已下载");
							Sqlite.execSingleSql(
									context,
									"update T_Train_List set issubmit = '0' where "
											+ Constants.TableInfo.TABLE_KEY
											+ " ="
											+ selectData
													.getStrValue(Constants.TableInfo.TABLE_KEY));

							handler.sendMessage(handler
									.obtainMessage(1, 1));
						}

					}
				} catch (ClientProtocolException e) {
					Tool.stopProgress();
					e.printStackTrace();
				} catch (IOException e) {
					Tool.stopProgress();
					e.printStackTrace();
				}
			}
		}.start();
	}

	@SuppressLint("SdCardPath")
	private void open(Fields fields) {
		try {
			String name = fields.getStrValue("serverid")
					+ fields.getStrValue("AttachmentType");
			String type = fields.getStrValue("AttachmentType").substring(1,
					fields.getStrValue("AttachmentType").length());

			String sFile_path = "/sdcard/"+getResources().getString(R.string.folder_name);
			Intent it = null;

			if (type.equals("m4a") || type.equals("mp3") || type.equals("mid")
					|| type.equals("xmf") || type.equals("ogg")
					|| type.equals("wav")) {
				it = getAudioFileIntent(sFile_path + name);
			} else if (type.equals("3gp") || type.equals("mp4")) {
				it = getVideoFileIntent(sFile_path + name);
			} else if (type.equals("apk")) {
				it = getApkFileIntent(sFile_path + name);
			}

			else

			if ("txt".equalsIgnoreCase(type)) {
				it = getTextFileIntent(sFile_path + name, false);
			} else if ("doc".equalsIgnoreCase(type)
					|| "docx".equalsIgnoreCase(type)) {
				it = getWordFileIntent(sFile_path + name);
			} else if ("ppt".equalsIgnoreCase(type)
					|| "pptx".equalsIgnoreCase(type)) {
				it = getPptFileIntent(sFile_path + name);
			} else if ("xls".equalsIgnoreCase(type)
					|| "xlsx".equalsIgnoreCase(type)) {
				it = getExcelFileIntent(sFile_path + name);
			} else if ("png".equalsIgnoreCase(type)
					|| "jpg".equalsIgnoreCase(type)
					|| "gif".equalsIgnoreCase(type)) {
				it = getImageFileIntent(sFile_path + name);
			} else if ("pdf".equalsIgnoreCase(type)) {
				it = getPdfFileIntent(sFile_path + name);
			}

			else {
				it = getAllIntent(sFile_path);
			}
			getContext().startActivity(it);
		} catch (Exception e) {
			Tool.showToastMsg(context, "文件打开失败", AlertType.ERR);
		}

	}

	public static Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	public static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	public static Intent getPdfFileIntent(String param)

	{

		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));

		intent.setDataAndType(uri, "application/pdf");

		return intent;

	}

	public static Intent getTextFileIntent(String param, boolean paramBoolean)
	{
		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		if (paramBoolean)
		{
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		}
		else
		{
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}

		return intent;

	}

	/**
	 * android获取一个用于打开音频文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getAudioFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");

		return intent;
	}

	/**
	 * android获取一个用于打开视频文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getVideoFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);

		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");

		return intent;
	}

	/**
	 * android获取一个用于打开CHM文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getChmFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");

		return intent;
	}

	/**
	 * android获取一个用于打开Word文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getWordFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		
		return intent;
	}

	/**
	 * android获取一个用于打开PPT文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getPptFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

		return intent;
	}

	public static Intent getExcelFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		
		return intent;
	}

	public static Intent getImageFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		
		return intent;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishActivity();
			break;

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	ElectronicBuilder builder;

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
		this.context = Frm_ElectornicList.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_ElectornicList.this;
	}

	@Override
	protected void onResume() {
		Tool.setAutoTime(context);
		if (!Rms.getLoginDate(context).equals(Tool.getCurrDate())) {
			showTimeoutDialog();
		}
		super.onResume();
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
		application.pullActivity(this);
		this.finish();
		
		application.exit();
		Intent intent = new Intent(this, Frm_Login.class);
		startActivity(intent);
	}

}
