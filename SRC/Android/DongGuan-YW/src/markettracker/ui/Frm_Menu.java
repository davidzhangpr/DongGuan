package markettracker.ui;

import orient.champion.business.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import markettracker.util.CButton;
import markettracker.util.CTable;
import markettracker.util.CTextView;
import markettracker.util.SyncDataApp;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.PhotoType;
import markettracker.util.Constants.AlertType;
import markettracker.util.Constants.Locations;
import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.TemGroupList;
import markettracker.data.Template;
import markettracker.data.TemplateGroup;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("HandlerLeak")
public class Frm_Menu extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private TemGroupList temGroupList;
	private LinearLayout mainLine;
	private Button exit, start, leave, paper, weipin, zw;
	private ImageView startPhoto, leavePhoto;
	private LinearLayout lineButton, ll_menu_button;
	private List<ButtonConfig> buttonlist;
	private ScrollView mainView;
	private TextView title;
	private boolean startCall = false, endCall = false;
	private static final int TAKE_PICTURE_START = 3021, TAKE_PICTURE_LEAVE = 3022, SURVEY = 3023;

	private Fields comleteRpt = new Fields();

	private CTextView selectTView;
	private String clientId, type, key, facialdiscount, displaytype;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);

	private SyncDataApp application;

	private RelativeLayout comephoto, leavere;

	private Bitmap bm12;
	private Bitmap bm1;
	private Fields photo;
	
	private boolean isInto = true;
	private Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rpt_form);

		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);

		init();
	}

	private void exit() {
		setResult(-100);

		application.pullActivity(this);
		this.finish();
	}

	private void init() {
		initContext();
		initActivity();
		initHandler();
		
		clientId = this.getIntent().getStringExtra("teminalCode");
		type = this.getIntent().getStringExtra("type");
		facialdiscount = this.getIntent().getStringExtra("facialdiscount");
		displaytype = this.getIntent().getStringExtra("displaytype");

		key = this.getIntent().getStringExtra("key");
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);

		start = (Button) findViewById(R.id.start);

		leave = (Button) findViewById(R.id.leave);

		title = (TextView) findViewById(R.id.title);
		title.setText(this.getIntent().getStringExtra("name"));

		startPhoto = (ImageView) findViewById(R.id.startphoto);
		startPhoto.setImageBitmap(getBitmap(PhotoType.CHECKIN));

		leavePhoto = (ImageView) findViewById(R.id.leavephoto);
		leavePhoto.setImageBitmap(getBitmap(PhotoType.CHECKOUT));

		initTemGroupList();

		initPage();
	}

	/**
	 * 初始化Handler
	 */
	private void initHandler(){
		handler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Locations.ENDLOCATION:	//停止获取位置信息
					closeLocation();
					break;

				case Locations.RELOCATION:	//重新获取位置信息
					closeLocation();
					
					showLocationDialog();
					startLocation();
					break;

				default:
					break;
				}
			}
		};
	}
	
	private Bitmap getBitmap(PhotoType type) {
		SObject report = getRpt(type);
		try {
			Fields data = report.getAttfields().getFields(0);
			byte[] bytes = data.getPhoto();
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			if (type == PhotoType.CHECKIN)
				startCall = true;
			else
				endCall = true;
			return bitmap;
		} catch (Exception ex) {
			if (type == PhotoType.CHECKIN)
				start.setOnClickListener(this);
			else
				leave.setOnClickListener(this);
			return Tool.getBimap(context, R.drawable.camera1);
		}
	}

	private void initPage() {
		if (!"0".equals(facialdiscount)) { // 门店
			initMainSView();
		}

		setupButton();
	}

	private void initMainSView() {
		if (mainView == null) {
			mainView = (ScrollView) findViewById(R.id.sv_content);
			mainView.setVisibility(View.VISIBLE);
			mainView.addView(getMainLine());
			// mainView.setAnimation(Tool.getAnimation(context));
		}
	}

	private void initTemGroupList() {
		if ("0".equals(facialdiscount)) { // 门店
			// temGroupList = TemplateFactory.getTemplateGroupList(context);
			ll_menu_button = (LinearLayout) findViewById(R.id.ll_menu_button);
			ll_menu_button.setVisibility(View.VISIBLE);

			paper = (Button) findViewById(R.id.btn_menu_paper);
			paper.setOnClickListener(getOnClickListener());

			weipin = (Button) findViewById(R.id.btn_menu_weipin);
			weipin.setOnClickListener(getOnClickListener());
		} else if ("10".equals(facialdiscount)) { // 经销商
			temGroupList = TemplateFactory.getDealersTemplateGroupList(context);
		} else { // 事件
			temGroupList = TemplateFactory.getTASKTemplateGroupList();
		}
	}

	private LinearLayout getMainLine() {
		if (mainLine == null) {
			mainLine = new LinearLayout(context);
			mainLine.setOrientation(LinearLayout.VERTICAL);
		}

		List<String> list = new ArrayList<String>();
		list = Sqlite.getTemplateIdList(context, clientId, key);
		for (String template : list) {
			comleteRpt.put(template, "true");
		}

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

	// public void OpenCarmer(int type)
	// {
	// try
	// {
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//
	// "android.media.action.IMAGE_CAPTURE"
	// startActivityForResult(intent, type);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public void OpenCarmer(int type) {
		try {
			Tool.createPhotoFile();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
					new File("/sdcard/" + getResources().getString(R.string.folder_name) + "photo//test.JPEG")));
			startActivityForResult(intent, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClick(View v) {
		resetbutton(v.getId());

		switch (v.getId()) {

		case 3:
			// Intent intent = new Intent(context, Frm_ChebliePhoto.class);
			// startActivityForResult(intent, v.getId());
			Intent intent = new Intent(context, Frm_DPList.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intent, 10);
			break;
		case 1:

			// intent = new Intent(context, Frm_SurveyList.class);
			// intent.putExtra("teminalCode", clientId);
			//
			// startActivityForResult(intent, SURVEY);

			intent = new Intent(context, Frm_DayRpt.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("key", "1");
			startActivityForResult(intent, 10);

			break;
		case 4:
			intent = new Intent(context, Frm_SelectProduct.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intent, 10);
			break;

		case 2:

			intent = new Intent(context, Frm_DayRpt.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("key", "2");
			startActivityForResult(intent, 10);

			break;

		case 5:
			// 1 showResetPwd();
			intent = new Intent(context, Frm_ChebliePhoto.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intent, 10);

			break;

		case R.id.back:
			finishActivity();
			break;
			
		case R.id.start:
			if (Tool.openGPSSettings(context)){
				OpenCarmer(TAKE_PICTURE_START);
			}
			break;
			
		case R.id.leave:
			if (startCall) {
				if (Tool.openGPSSettings(context)){
					OpenCarmer(TAKE_PICTURE_LEAVE);
				}
			} else
				Tool.showToastMsg(context, "请先拍摄进店照片！", AlertType.ERR);
			break;

		case R.id.btn_dialog_cancel:	//停止定位
			Tool.stopProgress();
			closeLocation();
			break;
			
		default:
			toRpt(v);
			break;
		}
	}

	private void saveRpt(String type) {
		Template template = TemplateFactory.getTemplate(getContext(), type);
		SObject report = Sqlite.getReport(context, template, clientId, 1, key);
		report.setField("ClientType", this.type);
		Sqlite.saveReport(context, report);
	}

	private void toRpt(View v) {
		if ("0".equals(facialdiscount)) { // 门店
			zw = (Button) v;
			if (startCall) {
				toRptClass();
			} else {
				Tool.showToastMsg(context, "请先拍照,再填写拜访内容", AlertType.ERR);
			}
		}

		if (v instanceof CTextView) {
			if (startCall) {
				selectTView = (CTextView) v;

				toRptClass();
			} else {
				Tool.showToastMsg(context, "请先拍照,再填写拜访内容", AlertType.ERR);
			}
		}
	}

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

	private void toRptClass() {
		Intent i;

		if ("0".equals(facialdiscount)) { // 门店
			i = new Intent(getContext(), Frm_ReportMenu.class);

			if ("纸品".equals(zw.getText().toString())) {
				i.putExtra("type", "101");
			} else {
				i.putExtra("type", "102");
			}
			i.putExtra("name", zw.getText().toString());
		} else { // 经销商，事件
			i = new Intent(getContext(), Frm_Rpt.class);

			i.putExtra("type", selectTView.getTemplateType());
			i.putExtra("name", selectTView.getTemplateName());
		}

		i.putExtra("teminalCode", clientId);
		i.putExtra("terminalname", this.getIntent().getStringExtra("name"));
		i.putExtra("clienttype", type);
		i.putExtra("displaytype", getIntent().getStringExtra("displaytype"));
		i.putExtra("key", key);

		startActivityForResult(i, 1000);
	}

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

	private void savePhoto(PhotoType type, Fields photo) {
		SObject rpt = getRpt(type);
		rpt.resetAttfield(photo);
		Sqlite.saveReport(context, rpt);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String strErrMsg = "";
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == TAKE_PICTURE_START) {
					photo = new Fields();
					final Bitmap bm = Tool.lessenUriImage();

					String date = Tool.getCurrPhotoTime();

					bm12 = Tool.generatorContactCountIcon(bm, date, this.getIntent().getStringExtra("name"),
							start.getText().toString(), context);

					photo.setShotTime(date);
					photo.setPhoto(Tool.Bitmap2Bytes(bm12));

//					savePhoto(PhotoType.CHECKIN, photo);
//					startPhoto.setImageBitmap(bm12);
//					startCall = true;

					isInto = true;
					SObject rpt = getGpsRpt(true);
					if (!rpt.isSaved()) {
						showLocationDialog();
						startLocation();
					}
				} else if (requestCode == TAKE_PICTURE_LEAVE) {
					photo = new Fields();

					final Bitmap bm = Tool.lessenUriImage();

					String date = Tool.getCurrPhotoTime();
					bm1 = Tool.generatorContactCountIcon(bm, date, this.getIntent().getStringExtra("name"),
							leave.getText().toString(), context);

					photo.setShotTime(date);
					photo.setPhoto(Tool.Bitmap2Bytes(bm1));
					
//					savePhoto(PhotoType.CHECKOUT, photo);
//					leavePhoto.setImageBitmap(bm1);
//					endCall = true;
					
					isInto = false;
					SObject rpt = getGpsRpt(false);
					if (!rpt.isSaved()) {
						showLocationDialog();
						startLocation();
					}
				} else if (requestCode == SURVEY) {

				} else if (requestCode == 10) {

				} else
					changeStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Tool.showToastMsg(context, "拍照错误" + strErrMsg + e.getMessage(), AlertType.ERR);
		}
	}

	/**
	 * 展示定位对话框
	 */
	public void showLocationDialog(){
		Tool.showProgress(context, "定位中...", true, this, handler);
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
		// for (TemplateGroup group : temGroupList.getTempGroupList()) {
		// for (Template t : group.getTemplateList()) {
		// if (t.getType().equals(type)) {
		// t.setComplete(true);
		// return;
		// }
		// }
		// }
	}

	private void resetbutton(int id) {
		CButton b;
		for (int i = 0; i < lineButton.getChildCount(); i++) {
			b = ((CButton) lineButton.getChildAt(i));
			if (b.getId() == id) {

				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg_click));
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, b.getText().toString(), true),
						null, null);
				// b.stopFlick(b);
			} else {
				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg));
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
		if (startCall) {
			if ("0".equals(facialdiscount)) { // 门店
				if (endCall) {
					setResult(RESULT_OK);
					application.pullActivity(this);
					this.finish();
				} else {
					Tool.showToastMsg(context, "离店前,请拍摄收银台照片", AlertType.ERR);
					if(Tool.openGPSSettings(context)){
						OpenCarmer(TAKE_PICTURE_LEAVE);
					}
				}
			} else {
				if (checkData()) {
					if (endCall) {
						setResult(RESULT_OK);
						application.pullActivity(this);
						this.finish();
					} else {
						Tool.showToastMsg(context, "离店前,请拍摄收银台照片", AlertType.ERR);
						
						if(Tool.openGPSSettings(context)){
							OpenCarmer(TAKE_PICTURE_LEAVE);
						}
					}
				}

			}
		} else {
			setResult(RESULT_OK);
			application.pullActivity(this);
			this.finish();
		}
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_Menu.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_Menu.this;
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

	private BDLocation bdLocation;

	private MyReceiveListenner mListenner = null;
	private LocationClient mLocationClient = null;

	/**
	 * 获取GPS模版
	 * @param isInto	//是否为进店
	 * @return
	 */
	private SObject getGpsRpt(boolean isInto) {
		Template template;
		if(isInto){
			template = TemplateFactory.getIntoGpsTemplate();
		}else{
			template = TemplateFactory.getOutGpsTemplate();
		}

		SObject rpt = Sqlite.getReport(context, template, clientId, 1, displaytype);
		rpt.setField("ClientType", this.type);
		return rpt;
	}

	/**
	 * 接受定位得到的消息
	 */
	private class MyReceiveListenner implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			if ((location.hasRadius() && location.getRadius() < 500)) {

				if (location.getLatitude() != 4.9E-324 && location.getLongitude() != 4.9E-324
						&& location.getLatitude() != 0 && location.getLatitude() != -1) {
					if(isInto){
						savePhoto(PhotoType.CHECKIN, photo);
						startPhoto.setImageBitmap(bm12);
						startCall = true;
					}else{
						savePhoto(PhotoType.CHECKOUT, photo);
						leavePhoto.setImageBitmap(bm1);
						endCall = true;
					}
					
					SObject rpt = getGpsRpt(isInto);
					rpt.setField("str5", String.valueOf(location.getLatitude()));
					rpt.setField("str6", String.valueOf(location.getLongitude()));
					Sqlite.saveReport(context, rpt);
					closeLocation();
					
					Tool.stopProgress();
					Tool.showToastMsg(context, "获取位置信息成功", AlertType.INFO);
				}
			} else {
				if (bdLocation == null && location.hasRadius())
					bdLocation = location;
				else {
					if (location.hasRadius() && location.getRadius() < bdLocation.getRadius())
						bdLocation = location;
				}
			}
		}

		public void onReceivePoi(BDLocation arg0) {
		}
	}

	/**
	 * start定位
	 */
	private void startLocation() {
		try {
			mLocationClient = new LocationClient(getApplicationContext());
			bdLocation = null;
			mListenner = new MyReceiveListenner();
			mLocationClient.registerLocationListener(mListenner);

			setLocationOption();
			mLocationClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LocationClientOption option = null;

	private void setLocationOption() {

		option = new LocationClientOption();
		option.setOpenGps(true); //打开GPRS
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5m
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		// option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		// option.setPoiNumber(10);
//		 option.disableCache(true);	
		mLocationClient.setLocOption(option);

	}

	/**
	 * end 定位
	 */
	public void closeLocation() {
		try {
			mLocationClient.stop(); // 结束定位

			mLocationClient.unRegisterLocationListener(mListenner);

			bdLocation = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
