package markettracker.ui;

import java.io.File;

import orient.champion.business.R;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.UIItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Frm_RptPhoto extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private LinearLayout photoLine;
	private ScrollView photoSView;
	private ImageView[] imageView;
	private Button back, save;
	private AlertDialog backAlertDialog;
	private SObject report;
	private static final int TAKE_PICTURE = 3021;
	private TextView title;
	private LinearLayout.LayoutParams layoutParamsPhoto = new LinearLayout.LayoutParams(
			LayoutParams.FILL_PARENT, Tool.getScreenHeight() / 3);
	private FieldsList attfields;
	private UIItem curUiItem;
	
	private SyncDataApp application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.rptphoto_frm);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);

		init();
	}

	private void init() {
		initContext();
		initActivity();

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);

		initReport();

		initTitle();
		initPage();
	}

	private void finish(int type) {
		Intent i = new Intent();
		setResult(type, i);
		
		application.pullActivity(this);
		this.finish();
	}

	/**
	 * 设置title
	 */
	private void initTitle() {
		title = (TextView) findViewById(R.id.txt);
		title.setText(curUiItem.getCaption());
	}

	private void initReport() {
		report = Tool.getCurrRpt();
		curUiItem = Tool.getCurrItem();

		attfields = new FieldsList();
		Fields photo;
		for (int i = 0; i < report.getAttCount(); i++) {
			photo = (Fields) report.getAttfield(i);

			if (photo.getStrValue("remark").equals(curUiItem.getCaption()))
				attfields.setFields(photo);
		}

		// report = Sqlite.getReport(context, template, terminalcode, 1,
		// clienttype, planid, isevent, eventtype, eventName);
	}

	private void initPage() {
		initPhotoLine();
	}

	private int mIndex;

	private ImageView getRptPhoto(int index, Bitmap bitmap) {
		imageView[index] = new ImageView(context);
		imageView[index].setId(index - 100);
		layoutParamsPhoto.topMargin = Tool.dip2px(context, 10);
		// layoutParamsPhoto.height=Tool.dip2px(context, 120);
		imageView[index].setLayoutParams(layoutParamsPhoto);
		// imageView[index].setBackgroundDrawable(Tool.getDrawable(context,
		// R.drawable.photok));
		if (bitmap == null)
			imageView[index].setImageResource(R.drawable.camera1);
		else
			imageView[index].setImageBitmap(bitmap);
		// imageView[index].setMinimumWidth(Tool.getScreenWidth());
		// imageView[index].setMinimumHeight(Tool.dip2px(context, 100));

		imageView[index].setOnClickListener(this);
		return imageView[index];
	}

	public void OpenCarmer() {
		try {
			Tool.createPhotoFile();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File("/sdcard/"+getResources().getString(R.string.folder_name)+"photo//test.JPEG")));
			startActivityForResult(intent, TAKE_PICTURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isHavePhoto(int index) {
		if (this.attfields == null || this.attfields.size() <= index)
			return false;
		else
			return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		String strErrMsg = "";
		try {
			if (resultCode == RESULT_OK) {
				// if (Tool.getAutoTimeStatus(context) != 1) {
				// Tool.showErrMsg(context, "请在设置中把'自动设定日期和时间'打钩,然后再进行报告填写");
				// } else {
				// strErrMsg+="1";
				if (requestCode == TAKE_PICTURE) {
					// strErrMsg+="2";
					final Fields photo = new Fields();
					final Bitmap bm = Tool.lessenUriImage();
					
					String date = Tool.getCurrPhotoTime();
					Bitmap bm1 = Tool.generatorContactCountIcon(bm, date, this
							.getIntent().getStringExtra("terminalname"),curUiItem.getCaption(),
							context);
					
					boolean bHave = isHavePhoto(mIndex);

					photo.setShotTime(Tool.getCurrPhotoTime());
					photo.setPhoto(Tool.Bitmap2Bytes(bm1));

					photo.put("remark", curUiItem.getCaption());
					// photo.put("DisplayType", curUiItem.getDicId());
					// photo.put("displayobject", curUiItem.getDictValue());
					// photo.put("str10", "20");

					if (bHave)
						attfields.setFields(mIndex, photo);
					else
						attfields.setFields(photo);
					imageView[mIndex].setImageBitmap(bm1);// 想图像显示在ImageView视图上，private

					if (!bHave && mTotalPhoto < 5) {
						photoLine.addView(getRptPhoto(mTotalPhoto, null));
						mTotalPhoto++;
					}
					// }

				}

			}
		} catch (Exception e) {
			Tool.showToastMsg(context, "拍照错误" + strErrMsg + e.getMessage(),
					AlertType.ERR);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private int mTotalPhoto = 1;

	private void initPhotoLine() {
		if (photoSView == null)
			photoSView = (ScrollView) findViewById(R.id.sv_photo);

		photoLine = (LinearLayout) findViewById(R.id.line_rpt_photo);
		mTotalPhoto = attfields.size() + 1;
		imageView = new ImageView[100];
		Bitmap bitmap = null;
		byte[] bytes;
		Fields data;
		for (int i = 0; i < mTotalPhoto; i++) {
			try {
				data = attfields.getFields(i);
				bytes = data.getPhoto();
				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			} catch (Exception ex) {
				bitmap = null;
			}
			photoLine.addView(getRptPhoto(i, bitmap));
		}
	}

	public void onClick(View v) {

		if (v.getId() < 0) {
			mIndex = 100 + v.getId();
			OpenCarmer();
		} else {
			switch (v.getId()) {
			case R.id.back:
				showBackDialog(RESULT_CANCELED);
				break;

			case R.id.save:
				SaveData();
				break;

			default:
				break;
			}
		}
	}

	private void stopDialog(AlertDialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private void showBackDialog(final int type) {
		stopDialog(backAlertDialog);
		Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("照片未保存,确定返回？");
		builder.setIcon(android.R.drawable.ic_dialog_info);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish(type);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		backAlertDialog = builder.create();
		backAlertDialog.show();
	}

	private String checkData() {
		return "";
	}

	private void SaveData() {
		String errMsg = checkData();
		if (errMsg.equals("")) {
			Fields photo;
			for (int i = report.getAttCount() - 1; i >= 0; i--) {
				photo = (Fields) report.getAttfield(i);

				if (photo.getStrValue("remark").equals(curUiItem.getCaption()))
					report.removeAttfield(i);
			}

			report.addAttList(attfields);

			finish(RESULT_OK);

		} else
			Tool.showToastMsg(context, errMsg, AlertType.ERR);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			showBackDialog(RESULT_CANCELED);
		}
		return false;
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_RptPhoto.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_RptPhoto.this;
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
