package markettracker.ui;

import markettracker.util.CGrid;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import orient.champion.business.R;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Frm_ShowData extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private CGrid customGrid;
	private LinearLayout productLine;

	private Button back;
	private View view;
	private Handler handler;
	private TextView title;
	private FieldsList list;

	private ProgressBar mProgressbar;
	private WebView mWebView;

	private SyncDataApp application;
	
	private String name;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showdata);
		init();

		application = (SyncDataApp) getApplication();
		application.pushActivity(this);
	}

	private void init() {
		initContext();
		initActivity();
		initTitle();
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);

		mProgressbar = (ProgressBar) findViewById(R.id.pb_showdata_bar);
		mWebView = (WebView) findViewById(R.id.webView);

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					mProgressbar.setVisibility(View.GONE);
				} else {
					mProgressbar.setVisibility(View.VISIBLE);
					mProgressbar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		});

		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!Tool.isConnect(context)) {
					Tool.showErrMsg(context, "网络连接失败，请确认网络连接！");
				} else {
					view.loadUrl(url);
				}

				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				return true;
			}

		});

		WebSettings settings = mWebView.getSettings();
		
		settings.setCacheMode(WebSettings.LOAD_DEFAULT); //设置 缓存模式
		settings.setDomStorageEnabled(true);	// 开启 DOM storage API 功能
		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setUseWideViewPort(true);
		
		if("生意表现".equals(name)){
			mWebView.loadUrl("http://223.4.23.60:8363/Report/Mobile/CS/BsPceHead.aspx?EmpId="+Rms.getUserId(context)+"&ClientId="+getIntent().getStringExtra("ClientId"));
		}
		else if("拜访达成统计".equals(name)){
			mWebView.loadUrl("http://223.4.23.60:8363/Report/Mobile/CS/VisitRpt.aspx?EmpId="+Rms.getUserId(context));
		}
		else if("里程KPI".equals(name)){
			mWebView.loadUrl("http://223.4.23.60:8363/Report/Mobile/CS/KPI.aspx?EmpId=2"+Rms.getUserId(context));
		}
	}

	public void initTitle(){
		name = getIntent().getStringExtra("name");
		
		title = (TextView) findViewById(R.id.title);
		title.setText(name);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();// 返回上一页面
				return true;
			} else {
				application.pullActivity(this);
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void finish(int type) {
		Intent i = new Intent();
		i.putExtra("type", this.getIntent().getStringExtra("type"));
		setResult(type, i);

		application.pullActivity(this);
		this.finish();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish(RESULT_OK);
			break;

		default:
			break;
		}
	}
	
	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_ShowData.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_ShowData.this;
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
				exits();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	private void exits() {
		application.pullActivity(this);
		this.finish();

		application.exit();
		Intent intent = new Intent(this, Frm_Login.class);
		startActivity(intent);
	}
}
