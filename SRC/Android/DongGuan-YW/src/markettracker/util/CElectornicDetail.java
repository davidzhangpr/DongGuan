package markettracker.util;

import java.io.File;
import java.text.DecimalFormat;

import orient.champion.business.R;

import markettracker.data.Fields;
import markettracker.util.Constants.AlertType;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

@SuppressLint({ "ViewConstructor", "SdCardPath" })
public class CElectornicDetail extends RelativeLayout implements
		OnClickListener {

	private Context context;
	private Fields fields;
	
	private static String sFile_path;

	public CElectornicDetail(Context context, Fields fields,
			OnClickListener listener) {
		super(context);
		this.fields = fields;
		this.context = context;
		sFile_path = "/sdcard/"+context.getResources().getString(R.string.folder_name);
		init(context, fields, listener);
	}

	private RelativeLayout.LayoutParams getCurLayoutParams() {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		return layoutParams;
	}

	private void init(Context context, Fields fields, OnClickListener listener) {
		this.setMinimumHeight(Tool.dip2px(getContext(), 80));
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.list_content);
		this.setOnClickListener(this);

		RelativeLayout.LayoutParams params_title = new RelativeLayout.LayoutParams(
				Tool.dip2px(getContext(), 30), Tool.dip2px(getContext(), 30));

		params_title.rightMargin = Tool.dip2px(getContext(), 10);
		// Tool.dip2px(getContext(), 50).
		params_title.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params_title.addRule(RelativeLayout.CENTER_VERTICAL);
//		params_title.topMargin = Tool.dip2px(getContext(), 15);
		// params_title.addRule(RelativeLayout.CENTER_VERTICAL);

		ImageView button = new ImageView(getContext());
		button.setScaleType(ScaleType.CENTER_INSIDE);
		button.setOnClickListener(this);
		// button.setId(100);
		button.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow));
		this.addView(button, params_title);

		params_title = new RelativeLayout.LayoutParams(Tool.dip2px(
				getContext(), 60), Tool.dip2px(getContext(), 60));
		params_title.rightMargin = Tool.dip2px(getContext(), 5);
		params_title.leftMargin = Tool.dip2px(getContext(), 5);
//		params_title.topMargin = Tool.dip2px(getContext(), 5);
		// Tool.dip2px(getContext(), 50).
		params_title.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params_title.addRule(RelativeLayout.CENTER_VERTICAL);
		// params_title.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		// params_title.addRule(RelativeLayout.CENTER_VERTICAL);
		// params_title.topMargin = Tool.dip2px(getContext(), 5);

		button = new ImageView(getContext());
		button.setScaleType(ScaleType.CENTER_INSIDE);
		
		// button.setg
		button.setId(100);
		// button.setImageBitmap(bm);
		// Tool.getUriImage(context,"/sdcard/parrow/" +
		// fields.getStrValue("frontCoverPhotoPath").substring(fields.getStrValue("frontCoverPhotoPath").lastIndexOf("/")
		// + 1)
		String url = sFile_path
				+ fields.getStrValue("titleurl").substring(
						fields.getStrValue("titleurl").lastIndexOf("/") + 1);
		button.setImageDrawable(DrawableUtils.getUriImage(context, url));
		this.addView(button, params_title);

		params_title = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params_title.topMargin = Tool.dip2px(getContext(), 15);
		// params_title.addRule(RelativeLayout.ALIGN_PARENT_TOP, 20);
		params_title.addRule(RelativeLayout.RIGHT_OF, 100);
		DataTextView view = new DataTextView(getContext(),
				fields.getStrValue("description"), 20.0f, R.color.black, null);
		// this.ad

		view.setId(1);
		this.addView(view, params_title);

		// view.setId(1);
		// this.addView(view);

		RelativeLayout.LayoutParams params_cal_title = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params_cal_title.addRule(RelativeLayout.BELOW, 1);
		params_cal_title.bottomMargin = Tool.dip2px(getContext(), 5);
		params_cal_title.topMargin = Tool.dip2px(getContext(), 10);
		// params_cal_title.topMargin = 5;
		// mainLayout.addView(title_gView, params_cal_title);

		params_cal_title.addRule(RelativeLayout.RIGHT_OF, 100);
		DecimalFormat df = new DecimalFormat("0.00");

		Double fileSize = fields.getDoubleValue("contentfilesize") / 1024;

		view = new DataTextView(getContext(), fields.getStrValue("starttime")
				+ "  " + df.format(fileSize) + "K", 15.0f,
				R.color.background, context.getResources().getDrawable(
						R.drawable.checkbox_off));
		// view.setLayoutParams(params);
		view.setId(2);
		this.addView(view, params_cal_title);

	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (this.fields != null)
			open(this.fields);
	}

	private void open(Fields fields) {
		try {
			String name = fields.getStrValue("contenturl").substring(
					fields.getStrValue("contenturl").lastIndexOf("/") + 1);
			String type = fields.getStrValue("contenturl").substring(
					fields.getStrValue("contenturl").lastIndexOf(".") + 1);

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
			Tool.showToastMsg(this.context, "文件打开失败",AlertType.ERR);
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

	// public static Intent getAudioFileIntent( String param ){
	//
	// Intent intent = new Intent("android.intent.action.VIEW");
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// intent.putExtra("oneshot", 0);
	// intent.putExtra("configchange", 0);
	// Uri uri = Uri.fromFile(new File(param ));
	// intent.setDataAndType(uri, "audio/*");
	// return intent;
	// }

	public static Intent getPdfFileIntent(String param)

	{

		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));

		intent.setDataAndType(uri, "application/pdf");

		return intent;

	}

	public static Intent getTextFileIntent(String param, boolean paramBoolean) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// android获取一个用于打开音频文件的intent

	public static Intent getAudioFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// android获取一个用于打开视频文件的intent

	public static Intent getVideoFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// android获取一个用于打开CHM文件的intent

	public static Intent getChmFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// android获取一个用于打开PPT文件的intent

	public static Intent getPptFileIntent(String param) {
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
}
