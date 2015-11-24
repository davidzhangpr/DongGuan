package markettracker.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.SysLog;
import markettracker.data.UIItem;

import orient.champion.business.R;

import markettracker.util.Constants.AlertType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class Tool {

	private static CPDialog mProgress;
	private static AlertDialog mAlertDialog;
	private static Display mDisplay;

	public static Date mTimestamp;

	private static Fields selectfields;

	private static long serverTimes = 0;

	private static FieldsList queryList;

	private static SObject report;
	private static UIItem selectItem;

	private static Fields curClient;

	public static void setRpt(SObject rpt) {
		report = rpt;
	}

	public static SObject getCurrRpt() {
		return report;
	}

	public static void setCurItem(UIItem item) {
		selectItem = item;
	}

	public static UIItem getCurrItem() {
		return selectItem;
	}

	public static FieldsList getQueryList() {
		return queryList;
	}

	public static void setQueryList(FieldsList list) {
		queryList = list;
	}

	public static void setSelectData(Fields fields) {
		selectfields = fields;
	}

	public static Fields getSelectData() {
		return selectfields;
	}

	public synchronized static void setServerTimes(long t) {
		serverTimes = t;
	}

	public synchronized static void addServerTimes() {
		serverTimes++;
	}

	public static Date getServerTimestamp() {
		if (serverTimes == 0)
			serverTimes = System.currentTimeMillis();
		return new Date(serverTimes);
	}

	public static void createPhotoFile() throws IOException {
		File file = new File("/sdcard/DongGuan-YW/photo");
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			// Log.e("test", "cannot read exif", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	public final static Bitmap lessenUriImage() {
		String path = "/sdcard/DongGuan-YW/photo//test.JPEG";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
		// BitmapFactory.decodeFile(path);
		options.inJustDecodeBounds = false;
		int heightRatio = (int) Math.ceil(options.outHeight / (float) 1024);
		int widthRatio = (int) Math.ceil(options.outWidth / (float) 768);
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				options.inSampleSize = heightRatio;
			} else {
				options.inSampleSize = widthRatio;
			}
		} else
			options.inSampleSize = 1;

		options.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		options.inPurgeable = true;
		bitmap = BitmapFactory.decodeFile(path, options);

		int angle = getExifOrientation(path);
		if (angle != 0) { // 如果照片出现了 旋转 那么 就更改旋转度数
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		return bitmap;
	}

	public static String addDate(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	public static String getMyUUID() {

		final UUID uuid = UUID.randomUUID();
		final String uniqueId = uuid.toString();
		return uniqueId;
	}

	public static File saveFile(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File("sdcard/" + outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	public static String getFormatTime(String time) {

		String result = "";
		String s1 = time.substring(0, 10);
		String s2 = time.substring(11);
		result = s1 + "T" + s2 + "Z";
		return result;
	}

	public static String getFormatDate(String date) {

		String result = "";
		result = date + "T00:00:00Z";
		return result;
	}

	public static String getFormatDate(java.util.Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));

		return dateFormat.format(date);
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);

		int options = 80;
		while (baos.toByteArray().length / 1024 > 50) {
			baos.reset();
			options -= 10;
			bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		return baos.toByteArray();
	}

	public static Display getScreen(Activity activity) {
		if (mDisplay == null)
			mDisplay = activity.getWindowManager().getDefaultDisplay();
		return mDisplay;
	}

	public static int getScreenWidth() {
		if (mDisplay == null)
			return 480;
		return mDisplay.getWidth();
	}

	public static int getScreenHeight() {
		if (mDisplay == null)
			return 640;
		return mDisplay.getHeight();
	}

	public static int getTitleWidth() {
		return getScreenWidth();
	}

	public static int getItemWidth() {
		return getScreenWidth() * 9 / 10;
		// 480;
	}

	public static int getButtonWidth(Activity activity, int iCount) {
		return (getScreenWidth() - Constants.MarginWidth.LEFT) / iCount - Constants.MarginWidth.LEFT;
	}

	public static int getBWidth(Activity activity, int iCount) {
		return getScreenWidth() / iCount;
	}

	public static int getButtonGap(Activity activity, int iCount) {
		if (iCount > 5)
			iCount = 5;
		return (getScreenWidth() - iCount * getButtonWidth(activity, iCount)) / (iCount + 1);
	}

	public static int getRptButtonWidth(Activity activity) {
		return (getScreenWidth()) / 3;
	}

	public static LayoutParams getLayoutParams(UIItem item) {

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.rightMargin = Constants.MarginWidth.RIGHT;
		if (item.getOrientation() == LinearLayout.HORIZONTAL)
			layoutParams.leftMargin = 10;// Constants.MarginWidth.LEFT;
		else
			layoutParams.leftMargin = 5;// Constants.MarginWidth.RIGHT;

		return layoutParams;
	}

	private static LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);

	public static LayoutParams getTitleLayoutParams(int size) {
		// if(size!=-1)
		// mLayoutParams.width=size;
		// else
		// mLayoutParams.width=100;
		return mLayoutParams;
	}

	public static LayoutParams getTitleLayoutParams(UIItem item) {

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// layoutParams.leftMargin = 5;

		return layoutParams;
	}

	public static float getTextSize() {
		return 15.0f;
	}

	public static int getTitleTextSize() {
		return 16;
	}

	public static int getGridTextSize() {
		return 22;
	}

	public static int getGridTitleTextSize() {
		return 23;
	}

	public static int getCustomGridTitleTextSize() {
		return 20;
	}

	public static int getGravity() {
		return Gravity.CENTER_VERTICAL;
	}

	public static int getTextColor(Context context) {
		// return context.getResources().getColor(R.color.darkorange);
		return context.getResources().getColor(R.color.black);
	}

	public static int getTitleTextColor(Context context) {
		return context.getResources().getColor(android.R.color.black);
	}

	public static void sendCall(Context context, String strNumber) {
		Uri uri = Uri.parse("tel:" + strNumber);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		context.startActivity(intent);
	}

	public static void sendSMS(Context context, String strNumber, String content) {
		// Uri uri = Uri.parse("smsto:18621910236");
		// Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		// intent.putExtra("sms_body", content);
		// context.startActivity(intent);
		try {
			PendingIntent mPI = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(strNumber, null, content, mPI, null);
			showToastMsg(context, "短信发送成功", AlertType.INFO);
		} catch (Exception e) {

		}

	}

	public static void showRouteOnMap(Context context, String[] strGps) {
		if (strGps == null)
			return;
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="
				+ strGps[0] + "," + strGps[1] + "&daddr=31.239145,121.498914&hl=zh"));
		// Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
		// + strGps[0] + "," + strGps[1]));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		context.startActivity(i);
	}

	public static String getUpdateTime(Context context) {
		String strTime = Rms.getUpdateTime(context);
		if (strTime.equals(""))
			strTime = getCurrDateTime().substring(0, 10);
		else
			strTime = strTime.substring(0, 10);
		// Rms__c.getUpdateTime(context);
		return strTime + "T00:00:00Z";
	}

	private static CPDialog getProgress(Context context, String msg) {
		mProgress = new CPDialog(context, msg);
		return mProgress;
	}

	public static void showProgress(Context context, String msg) {
		try {
			stopProgress();
			getProgress(context, msg);
		} catch (Exception e) {

		}
	}

	public static void stopProgress() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}

	public static void dismissAlertDialog() {
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
	}

	public static AlertDialog getAlertDialog(Context context, String strMsg, AlertType type) {
		CBuilder builder = new CBuilder(context, strMsg, type);
		mAlertDialog = builder.create();
		return mAlertDialog;
	}

	public static void showErrMsg(Context context, String strMsg) {
		dismissAlertDialog();
		getAlertDialog(context, strMsg, AlertType.ERR).show();
	}

	public static void showToastMsg(Context context, String msg, AlertType type) {
		new CToast(context, type).showMsg(msg);
	}

	public static void showMsg(Context context, String strMsg) {
		dismissAlertDialog();
		getAlertDialog(context, strMsg, AlertType.INFO).show();
	}

	public static String getCurrPhotoTime() {
		String result;
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		java.util.Date date = new java.util.Date();
		// result = dateFormat.format(getServerTimestamp());
		result = dateFormat.format(date);
		dateFormat = new SimpleDateFormat(Constants.DateFormat.TIME);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		// result += "T" + dateFormat.format(getServerTimestamp());
		result += "T" + dateFormat.format(date);
		return result;
	}

	//
	public static String getCurrDateTime() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATETIME);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

	public static String getCurrDateTime1() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATETIME1);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

	public static String getCurrTime() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.TIME1);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

	public static Drawable getDrawable(Context context, int iMsgId) {
		return context.getResources().getDrawable(iMsgId);
	}

	//
	public static String date2String(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		// java.util.Date date = new java.util.Date();
		// return "2012-04-25";
		return dateFormat.format(date);
	}

	// public static String getCurrDate() {
	// SimpleDateFormat dateFormat = new SimpleDateFormat(
	// Constants.DateFormat.DATE);
	// dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
	// return dateFormat.format(getServerTimestamp());
	// }
	public static String getCurrDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		// return dateFormat.format(getServerTimestamp());
		return dateFormat.format(new Date());
	}

	public static String getServerDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		return dateFormat.format(getServerTimestamp());
		// return dateFormat.format(new Date());
	}

	public static String getMoblieDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		return dateFormat.format(new Date());
	}

	public static Date string2Date(String date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATETIME);
			dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
			// java.util.Date date = new java.util.Date();
			// return "2012-04-25";
			return dateFormat.parse(date);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String getCurrMonth() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.MONTH);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		java.util.Date date = new java.util.Date();
		// return "2012-04-25";
		return dateFormat.format(date);
	}

	public static HashMap<String, String> getDate() {
		// SimpleDateFormat dateFormat = new SimpleDateFormat(
		// Constants.DateFormat.DATE);
		// dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		HashMap<String, String> date = new HashMap<String, String>();
		Time t = new Time(Constants.TimeZone.ZH_CH);// 加上Time Zone资料。

		t.setToNow(); // 取得系统时间。

		int year = t.year;
		int month = t.month;
		int day = t.monthDay;
		// int hour = t.hour; // 0-23
		// int minute = t.minute;
		// int second = t.second;

		date.put("year", year + "");
		date.put("month", (month + 1) + "");
		date.put("day", day + "");

		return date;
	}

	public static HashMap<String, String> getLastMonthDate() {
		// SimpleDateFormat dateFormat = new SimpleDateFormat(
		// Constants.DateFormat.DATE);
		// dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		HashMap<String, String> date = new HashMap<String, String>();
		Time t = new Time(Constants.TimeZone.ZH_CH);// 加上Time Zone资料。

		t.setToNow(); // 取得系统时间。

		int year = t.year;
		int month = t.month;
		int day = t.monthDay;
		date.put("year", year + "");
		date.put("month", month + "");
		date.put("day", day + "");

		return date;
	}

	public static void updateLanguage(Context context, Locale locale) {
		Resources res = context.getResources();
		Configuration config = res.getConfiguration();
		DisplayMetrics metrics = res.getDisplayMetrics();
		config.locale = locale;
		res.updateConfiguration(config, metrics);
	}

	public static boolean isConnect(Context context) {
		//
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				//
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					//
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static List<ButtonConfig> getMainMenuButton() {
		List<ButtonConfig> list = new ArrayList<ButtonConfig>();

		ButtonConfig button;

		button = new ButtonConfig();
		button.setId(1);
		button.setImageId(R.drawable.plan);
		button.setName("拜访设定");
		list.add(button);

		button = new ButtonConfig();
		button.setId(6);
		button.setName("请假");
		button.setImageId(R.drawable.holiday);
		list.add(button);

		button = new ButtonConfig();
		button.setId(3);
		button.setName("消息公告");
		button.setImageId(R.drawable.message);
		list.add(button);

		button = new ButtonConfig();
		button.setId(8);
		button.setName("修改密码");
		button.setImageId(R.drawable.setup);
		list.add(button);

		// button = new ButtonConfig();
		// button.setId(5);
		// button.setName("修改密码");
		// list.add(button);

		return list;
	}

	public static List<ButtonConfig> getMainMenuButton1() {
		List<ButtonConfig> list = new ArrayList<ButtonConfig>();

		ButtonConfig button;

		button = new ButtonConfig();
		button.setId(10);
		button.setName("活动美顾");
		list.add(button);

		button = new ButtonConfig();
		button.setId(13);
		button.setName("活动");
		list.add(button);

		button = new ButtonConfig();
		button.setId(6);
		button.setName("请假");
		list.add(button);

		button = new ButtonConfig();
		button.setId(7);
		button.setName("陈列图");
		list.add(button);

		button = new ButtonConfig();
		button.setId(8);
		button.setName("修改密码");
		list.add(button);

		return list;
	}

	public static List<ButtonConfig> getMenuButton() {
		List<ButtonConfig> list = new ArrayList<ButtonConfig>();

		ButtonConfig button;

		// button = new ButtonConfig();
		// button.setId(1);
		// button.setName("问卷");
		// list.add(button);
		//
		// button = new ButtonConfig();
		// button.setId(10);
		// button.setName("活动美顾");
		// list.add(button);
		//
		// button = new ButtonConfig();
		// button.setId(3);
		// button.setName("工具包");
		// list.add(button);
		//
		// button = new ButtonConfig();
		// button.setId(4);
		// button.setName("更多");
		// list.add(button);

		button = new ButtonConfig();
		button.setId(1);
		button.setName("门店销量");
		list.add(button);

		button = new ButtonConfig();
		button.setId(2);
		button.setName("美顾考勤");
		list.add(button);

		button = new ButtonConfig();
		button.setId(3);
		button.setName("活动美顾");
		list.add(button);

		button = new ButtonConfig();
		button.setId(4);
		button.setName("新品");
		list.add(button);

		button = new ButtonConfig();
		button.setId(5);
		button.setName("陈列图");
		list.add(button);

		return list;
	}

	public static List<ButtonConfig> getRptTab() {
		List<ButtonConfig> list = new ArrayList<ButtonConfig>();

		ButtonConfig button;

		button = new ButtonConfig();
		button.setId(1);
		button.setName("基本信息");
		list.add(button);

		button = new ButtonConfig();
		button.setId(2);
		button.setName("产品信息");
		list.add(button);

		button = new ButtonConfig();
		button.setId(3);
		button.setName("拍照");
		list.add(button);

		// button = new ButtonConfig();
		// button.setId(4);
		// button.setName("签名");
		// list.add(button);

		return list;
	}

	public static List<ButtonConfig> getRptTab3() {
		List<ButtonConfig> list = new ArrayList<ButtonConfig>();

		ButtonConfig button;

		button = new ButtonConfig();
		button.setId(2);
		button.setName("产品信息");
		list.add(button);

		button = new ButtonConfig();
		button.setId(3);
		button.setName("拍照");
		list.add(button);

		// button = new ButtonConfig();
		// button.setId(4);
		// button.setName("签名");
		// list.add(button);

		return list;
	}

	public static List<ButtonConfig> getRptTab2() {
		List<ButtonConfig> list = new ArrayList<ButtonConfig>();

		ButtonConfig button;

		button = new ButtonConfig();
		button.setId(1);
		button.setName("基本信息");
		list.add(button);

		button = new ButtonConfig();
		button.setId(3);
		button.setName("拍照");
		list.add(button);

		// button = new ButtonConfig();
		// button.setId(4);
		// button.setName("签名");
		// list.add(button);

		return list;
	}

	public static List<ButtonConfig> getSurvyTab() {
		List<ButtonConfig> list = new ArrayList<ButtonConfig>();

		ButtonConfig button;

		button = new ButtonConfig();
		button.setId(1);
		button.setName("问题");
		list.add(button);

		// button = new ButtonConfig();
		// button.setId(2);
		// button.setName("产品信息");
		// list.add(button);

		button = new ButtonConfig();
		button.setId(3);
		button.setName("拍照");
		list.add(button);

		// button = new ButtonConfig();
		// button.setId(4);
		// button.setName("签名");
		// list.add(button);

		return list;
	}

	public static int getCallPlanTitleWidth() {
		return 150;
	}

	public static void openUrl(Uri uri, int type) {
		// android获取一个用于打开HTML文件的intent

		Intent intent = new Intent("android.intent.action.VIEW");
		switch (type) {
		case Constants.FileType.HTML:
			intent.setDataAndType(uri, "text/html");
			break;
		case Constants.FileType.IMAGE:
			// android获取一个用于打开图片文件的intent
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "image/*");
			break;
		case Constants.FileType.PDF:
			// android获取一个用于打开PDF文件的intent
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "application/pdf");
			break;
		case Constants.FileType.TEXT:
			// android获取一个用于打开文本文件的intent
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "text/plain");
			break;
		case Constants.FileType.AUDIO:
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("oneshot", 0);
			intent.putExtra("configchange", 0);
			intent.setDataAndType(uri, "audio/*");
			break;

		case Constants.FileType.VIDEO:
			// android获取一个用于打开视频文件的intent
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("oneshot", 0);
			intent.putExtra("configchange", 0);
			intent.setDataAndType(uri, "video/*");
			break;

		case Constants.FileType.CHM:
			// android获取一个用于打开CHM文件的intent
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "application/x-chm");
			break;

		case Constants.FileType.WORD:
			// android获取一个用于打开Word文件的intent
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "application/msword");
			break;

		case Constants.FileType.EXCEL:
			// android获取一个用于打开Excel文件的intent
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "application/vnd.ms-excel");
			break;

		case Constants.FileType.PPT:
			// android获取一个用于打开PPT文件的intent
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
			break;
		}
	}

	public static int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}

	public static Bitmap getBimap(Context context, int resId) {
		return BitmapFactory.decodeResource(context.getResources(), resId);
	}

	public static Drawable bitmap2Drawable(Context context, Bitmap bm) {
		return new BitmapDrawable(context.getResources(), bm);
	}

	public static Bitmap generatorContactCountIcon(Bitmap icon, String time, String clientName, String photoType,
			Context context) {
		Bitmap contactIcon = null;
		int width = icon.getWidth();
		int height = icon.getHeight();

		contactIcon = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(contactIcon);

		// 拷贝图片
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);// 防抖动
		iconPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
		Rect src = new Rect(0, 0, width, height);
		Rect dst = new Rect(0, 0, width, height);
		canvas.drawBitmap(icon, src, dst, iconPaint);

		// 启用抗锯齿和使用设备的文本字距
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.WHITE);
		countPaint.setTextSize(dip2px(context, 12)); // 18
		canvas.drawText(time, Tool.dip2px(context, 5), height - Tool.dip2px(context, 5), countPaint);
		canvas.drawText("照片类型:" + photoType, Tool.dip2px(context, 5), height - Tool.dip2px(context, 16), countPaint);
		canvas.drawText("门店名称:" + clientName, Tool.dip2px(context, 5), height - Tool.dip2px(context, 27), countPaint);
		canvas.drawText("拜访人:" + Rms.getEmpName(context), Tool.dip2px(context, 5), height - Tool.dip2px(context, 38),
				countPaint);
		return contactIcon;
	}

	public static Bitmap generatorContactCountIcon(int resId, int iCount, Context context) {
		// 初始化画布
		// int
		// iconSize=(int)getResources().getDimension(android.R.dimen.app_icon_size);
		Bitmap icon = getBimap(context, resId);
		Bitmap contactIcon = Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(contactIcon);

		// 拷贝图片
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);// 防抖动
		iconPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
		Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
		Rect dst = new Rect(0, 0, icon.getWidth(), icon.getHeight());
		canvas.drawBitmap(icon, src, dst, iconPaint);

		// canvas.drawOval(new RectF(16,0,36,48),iconPaint);

		// 启用抗锯齿和使用设备的文本字距
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.RED);
		countPaint.setTextSize(getTextSize() + 2);
		countPaint.setTypeface(Typeface.DEFAULT_BOLD);
		if (iCount != 0)
			canvas.drawText(String.valueOf(iCount), 18, 11, countPaint); // 8 18

		return contactIcon;
	}

	public static Bitmap getResIcon(Resources res, int resId) {
		Drawable icon = res.getDrawable(resId);
		if (icon instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) icon;
			return bd.getBitmap();
		} else {
			return null;
		}
	}

	public static double GetDistance(String lat1, String lng1, String lat2, String lng2) {
		try {
			return GetDistanceByGoogle(Double.parseDouble(lat1), Double.parseDouble(lng1), Double.parseDouble(lat2),
					Double.parseDouble(lng2));
		} catch (Exception ex) {
			return -1;
		}
	}

	public static double GetDistanceByGoogle(double lat1, double lng1, double lat2, double lng2) {
		Location locationA = new Location("point A");
		locationA.setLatitude(lat1);
		locationA.setLongitude(lng1);
		Location locationB = new Location("point B");
		locationB.setLatitude(lat2);
		locationB.setLongitude(lng2);
		double s = locationA.distanceTo(locationB);
		return s; // 米
	}

	// public static double GetDistance(double lat1, double lng1, double lat2,
	// double lng2) {
	// double radLat1 = Math.toRadians(lat1);
	// double radLat2 = Math.toRadians(lat2);
	// double a = radLat1 - radLat2;
	// double b = Math.toRadians(lng1) - Math.toRadians(lng2);
	// double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
	// + Math.cos(radLat1) * Math.cos(radLat2)
	// * Math.pow(Math.sin(b / 2), 2)));
	// s = s * EARTH_RADIUS;
	// s = Math.round(s * 10000) / 10;
	// return s; // 米
	// }

	// public static Message getMessage(int what, Object object) {
	// SyncResult result = new SyncResult();
	// switch (what) {
	// case 1:
	// SObject query = (SObject) object;
	// result.success = query.isDone();
	// result.errCode = "";
	// result.errMsg = "";
	// break;
	// }
	// Message msg = new Message();
	// msg.what = what;
	// msg.obj = result;
	// return msg;
	// }

	private static long mStartTime;

	public static void startLog() {
		mStartTime = System.currentTimeMillis();
	}

	public static void endLog(Context context, String moduleName, String status, String comments, String recordTime) {
		try {
			SysLog sl = new SysLog();
			sl.mComments__c = comments;
			sl.mModuleName__c = moduleName;
			sl.mRecordTime__c = recordTime;
			sl.mStatus__c = status;
			long timeSpending = (System.currentTimeMillis() - mStartTime) / 1000;
			sl.mTimeSpending__c = (int) timeSpending;
			// Sqlite.createLog(context, sl);
		} catch (Exception ex) {

		}
	}

	public static String GetCurrVersion(Context context) {
		// R.string.app_name\
		return context.getResources().getString(R.string.app_name);// "MSR-R12.06.09S-A";
	}

	// public static int getFontWeight(int fontSize) {
	// Paint paint = new Paint();
	// paint.setTextSize(fontSize);
	// FontMetrics fm = paint.getFontMetrics();
	// return (int) Math.ceil(fm.descent - fm.top) + 2;
	// }

	public static boolean isEmpty(EditText et) {
		if (et.getText().toString().trim().equals(""))
			return true;
		return false;
	}

	public static String getString(EditText et) {
		return et.getText().toString().trim();
	}

	public static boolean isSameUser(Context context, String user) {
		if (Rms.getFirst(context))
			return true;
		else if (!Rms.getFirst(context) && Rms.getUserName(context).equalsIgnoreCase(user))
			return true;
		return false;
	}

	public static boolean isDel(String isDel) {
		if (isDel.equals("1"))
			return true;
		else
			return false;
	}

	public static String getSubString(String string) {
		if (string.length() > 0)
			return string.substring(0, string.length() - 1);
		return "";
	}

	public static String toLowerCase(String string) {
		if (string != null)
			return string.toLowerCase();
		return "";
	}

	public static Drawable getDrawable(Context context, String name, boolean click) {
		if (click) {
			if (name.equals("更多"))
				return context.getResources().getDrawable(R.drawable.more_click);
			else if (name.equals("地图路线"))
				return context.getResources().getDrawable(R.drawable.route_click);
			else if (name.equals("上下班") || name.equals("请假"))
				return context.getResources().getDrawable(R.drawable.task_click);
			else if (name.indexOf("消息/问卷") != -1)
				return context.getResources().getDrawable(R.drawable.news_click);
			else if (name.equals("活动") || name.equals("计划设定") || name.equals("新品"))
				return context.getResources().getDrawable(R.drawable.plan_click);
			else if (name.equals("活动美顾") || name.equals("美顾考勤") || name.equals("问题") || name.equals("销量考勤")
					|| name.equals("基本信息") || name.equals("工具包") || name.equals("销售日期"))
				return context.getResources().getDrawable(R.drawable.basicinfo_click);
			else if (name.equals("门店销量") || name.equals("产品信息") || name.equals("信息") || name.equals("BA检查") || name.equals("销量填写"))
				return context.getResources().getDrawable(R.drawable.productinfo_click);
			else if (name.equals("拍照"))
				return context.getResources().getDrawable(R.drawable.camera_nav_click);
			else if (name.equals("签名") || name.equals("修改密码"))
				return context.getResources().getDrawable(R.drawable.signature_click);
			else if (name.equals("问卷") || name.equals("拜访汇总"))
				return context.getResources().getDrawable(R.drawable.ques_click);
			else if (name.equals("陈列图") || name.equals("订单") || name.equals("门店资料"))
				return context.getResources().getDrawable(R.drawable.order_click);
		} else {
			if (name.equals("更多"))
				return context.getResources().getDrawable(R.drawable.more);
			else if (name.equals("地图路线"))
				return context.getResources().getDrawable(R.drawable.route);
			else if (name.equals("上下班") || name.equals("请假"))
				return context.getResources().getDrawable(R.drawable.task);
			else if (name.indexOf("消息/问卷") != -1)
				return context.getResources().getDrawable(R.drawable.news);
			else if (name.equals("活动") || name.equals("计划设定") || name.equals("新品"))
				return context.getResources().getDrawable(R.drawable.plan);
			else if (name.equals("活动美顾") || name.equals("美顾考勤") || name.equals("问题") || name.equals("销量考勤")
					|| name.equals("基本信息") || name.equals("工具包") ||  name.equals("销售日期"))
				return context.getResources().getDrawable(R.drawable.basicinfo);
			else if (name.equals("门店销量") || name.equals("产品信息") || name.equals("信息") || name.equals("BA检查") || name.equals("销量填写"))
				return context.getResources().getDrawable(R.drawable.productinfo);
			else if (name.equals("拍照"))
				return context.getResources().getDrawable(R.drawable.camera_nav);
			else if (name.equals("签名") || name.equals("修改密码"))
				return context.getResources().getDrawable(R.drawable.signature);
			else if (name.equals("问卷") || name.equals("拜访汇总"))
				return context.getResources().getDrawable(R.drawable.ques);
			else if (name.equals("陈列图") || name.equals("订单") || name.equals("门店资料"))
				return context.getResources().getDrawable(R.drawable.order);
		}
		return null;
	}

	/**
	 * 判断是否是合法的email地址
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String email) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判断是否是合法的手机号码
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isMobileNO(String mobile) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{11}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 判断是否是合法URL
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isURL(String url) {
		Pattern p = Pattern.compile("[a-zA-z]+://[^s]*");
		Matcher m = p.matcher(url);
		return m.matches();
	}

	// public static Animation getAnimation(Context context)
	// {
	// return AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
	// }
	//
	// public static Animation getRightAnimation(Context context)
	// {
	// return AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
	// }

	public static boolean isPhoneNumberValid(String phoneNumber)

	{

		boolean isValid = false;

		/*
		 * 可接受的电话格式有:
		 * 
		 * ^//(? : 可以使用 "(" 作为开头
		 * 
		 * (//d{3}): 紧接着三个数字
		 * 
		 * //)? : 可以使用")"继续
		 * 
		 * [- ]? : 在上述格式后可以使用具有选择性的 "-".
		 * 
		 * (//d{3}) : 再紧接着三个数字
		 * 
		 * [- ]? : 可以使用具有选择性的 "-" 继续.
		 * 
		 * (//d{5})$: 以五个数字结束.
		 * 
		 * 可以比较下列数字格式:
		 * 
		 * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
		 */

		String expression =

		"^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";

		/*
		 * 可接受的电话格式有:
		 * 
		 * ^//(? : 可以使用 "(" 作为开头
		 * 
		 * (//d{3}): 紧接着三个数字
		 * 
		 * //)? : 可以使用")"继续
		 * 
		 * [- ]? : 在上述格式后可以使用具有选择性的 "-".
		 * 
		 * (//d{4}) : 再紧接着四个数字
		 * 
		 * [- ]? : 可以使用具有选择性的 "-" 继续.
		 * 
		 * (//d{4})$: 以四个数字结束.
		 * 
		 * 可以比较下列数字格式:
		 * 
		 * (02)3456-7890, 02-3456-7890, 0234567890, (02)-3456-7890
		 */

		String expression2 =

		"^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";

		CharSequence inputStr = phoneNumber;

		/* 创建Pattern */

		Pattern pattern = Pattern.compile(expression);

		/* 将Pattern 以参数传入Matcher作Regular expression */

		Matcher matcher = pattern.matcher(inputStr);

		/* 创建Pattern2 */

		Pattern pattern2 = Pattern.compile(expression2);

		/* 将Pattern2 以参数传入Matcher2作Regular expression */

		Matcher matcher2 = pattern2.matcher(inputStr);

		if (matcher.matches() || matcher2.matches())

		{

			isValid = true;

		}

		return isValid;

	}

	public static long getDateChazhi(String str1, String str2) {
		long index = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date1 = format.parse(str1);
			Date date2 = format.parse(str2);
			index = date2.getTime() - date1.getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return index;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void delPhotoFile() {
		try {
			File f = new File(Environment.getExternalStorageDirectory() + "/dcim/100MEDIA");
			File[] files = f.listFiles();// 列出所有文件
			if (files != null) {
				int count = files.length;// 文件个数
				File file;
				for (int i = 0; i < count; i++) {
					file = files[i];
					if (file.exists()) {
						file.delete();
					}
				}
			}
		} catch (Exception ex) {

		}
	}

	private static Thread timer = null;

	public static void startMyTime() {
		if (timer == null) {
			timer = new Thread() {
				public void run() {
					while (true) {
						addServerTimes();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			};
			timer.start();
		}

	}

	public static Bitmap getBitmapByfileName(String fileName) {
		// String fileName = "/data/data/com.test/aa.png;
		return BitmapFactory.decodeFile(fileName);

	}

	public final static Bitmap getUriImage(Context context, String url) {
		// Bitmap imageBitmap=null;
		// Drawable drawable = null;
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// Bitmap bitmap =null;
			bitmap = BitmapFactory.decodeFile(url, options); // 此时返回 bm
																// 为空

			options.inJustDecodeBounds = false;
			int heightRatio = (int) Math.ceil(options.outHeight / Tool.dip2px(context, 100));
			int widthRatio = (int) Math.ceil(options.outWidth / Tool.dip2px(context, 100));
			if (heightRatio > 1 && widthRatio > 1)
			// if (widthRatio > 1)
			{
				if (heightRatio > widthRatio) {
					options.inSampleSize = heightRatio;
				} else {
					options.inSampleSize = widthRatio;
				}
			} else
				options.inSampleSize = 1;
			bitmap = BitmapFactory.decodeFile(url, options);
			// drawable = bitmap2Drawable(context, bitmap);
		} catch (Exception ex) {
		}
		return bitmap;
	}

	// public static void getMyTime() {
	// tt=new Thread() {
	// public void run() {
	// while (true) {
	// addServerTimes();
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// };
	// }
	public static void setAutoTime(Context c) {
		try {
			Settings.System.putInt(c.getContentResolver(), Settings.System.AUTO_TIME, 1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 时间格式24小时
		// Settings.System.putInt(c.getContentResolver(),
		// Settings.System.TIME_12_24, 1);

		// String asdaString=getCurrPhotoTime();
	}

	public static Date string2Date1(String date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
			dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
			// java.util.Date date = new java.util.Date();
			// return "2012-04-25";
			return dateFormat.parse(date);
		} catch (Exception ex) {
			return null;
		}
	}

	private static String getWeekDay(int index) {
		String week;
		switch (index) {
		case 2:
			week = "周一";
			break;
		case 3:
			week = "周二";
			break;
		case 4:
			week = "周三";
			break;
		case 5:
			week = "周四";
			break;
		case 6:
			week = "周五";
			break;
		case 7:
			week = "周六";
			break;
		case 1:
			week = "周日";
			break;
		default:
			week = "";
			break;
		}
		return week;
	}

	public static String date2String1(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateFormat.DATE);
		dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TimeZone.ZH_CH));
		// java.util.Date date = new java.util.Date();
		// return "2012-04-25";

		return dateFormat.format(date);
	}

	public static FieldsList getWeekDays(String date) {

		FieldsList list = new FieldsList();
		Fields data = null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(string2Date1(date));
		if (cal.get(Calendar.DAY_OF_WEEK) == 1)
			cal.add(Calendar.WEEK_OF_YEAR, -1);
		for (int i = 2; i <= Calendar.SATURDAY; i++) {
			cal.set(Calendar.DAY_OF_WEEK, i);
			data = new Fields();

			data.put("date", cal.get(Calendar.DATE) + "");
			data.put("week", getWeekDay(i));
			if (cal.get(Calendar.DATE) == Integer.parseInt(date.substring(8, 10)))
				data.put("select", "1");
			else
				data.put("select", "0");
			data.put("dateofyear", date2String1(cal.getTime()));
			list.setFields(data);
		}
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		data = new Fields();
		data.put("date", cal.get(Calendar.DATE) + "");
		data.put("week", getWeekDay(Calendar.SUNDAY));
		if (cal.get(Calendar.DATE) == Integer.parseInt(date.substring(8, 10)))
			data.put("select", "1");
		else
			data.put("select", "0");
		data.put("dateofyear", date2String1(cal.getTime()));
		list.setFields(data);

		return list;
	}

	public static boolean isConSpeCharacters(String string) {
		// 如果不包含特殊字符
		if (string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0) {
			return true;
		}
		return false;
	}

	public static Animation getAnimation(Context context) {
		return AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
	}

	public static int getColor(Context context, int color) {
		return context.getResources().getColor(color);
	}

	public static Fields getCurClient() {
		return curClient;
	}

	public static void setCurClient(Fields t) {
		curClient = t;
	}
	
	/**
	 * 得到当前日期和当前日期的前6天日期
	 * @return
	 */
	public static List<Calendar> getBeforeDate(){
		List<Calendar> calendarGroup = new ArrayList<Calendar>();
		Calendar rightNow;
	
		for(int i=-6; i<0; i++){
			rightNow = Calendar.getInstance();
			rightNow.add(Calendar.DAY_OF_YEAR, i);
			calendarGroup.add(rightNow);
		}
		
		rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DAY_OF_YEAR, 0);
		calendarGroup.add(rightNow);
		
		return calendarGroup;
	}
	
}
