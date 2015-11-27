package markettracker.ui;

import orient.champion.business.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import markettracker.util.CButton;
import markettracker.util.CCellTakePhoto;
import markettracker.util.CImageView;
import markettracker.util.ChoiceGroupAdapter;
import markettracker.util.Constants;
import markettracker.util.CustomGrid;
import markettracker.util.NumericKeyboard;
import markettracker.util.RptTable;
import markettracker.util.SyncDataApp;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.util.NumericKeyboard.OnNumberClickListener;
import markettracker.data.ButtonConfig;
import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Panal;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.Template;
import markettracker.data.UIItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
public class Frm_Rpt extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private List<ButtonConfig> buttonlist;
	private LinearLayout lineButton;
	private LinearLayout mainLine, photoLine;
	private ScrollView mainSView;
	private CustomGrid customGrid;
	private LinearLayout productLine;
	private ScrollView photoSView;
	private CImageView[] imageView;
	private CImageView clickImgView;
	private Button back, save;
	private AlertDialog backAlertDialog;
	private View view;

	private NumericKeyboard keyboard;

	private Hashtable<String, Fields> hashtable;

	private Template template;
	private SObject report;
	private static final int TAKE_PICTURE = 3021;

	private TextView title,category;
	private Fields selectData;
	private Handler handler;
	private String key;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);

	private LinearLayout.LayoutParams layoutParamsPhoto = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 200);

	private SyncDataApp application;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rpt_frm);
		
		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);
		
		init();
	}

	private void init() {
		initContext();
		initActivity();
		initHandler();
		key = this.getIntent().getStringExtra("key");
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);

		initTitle();
		initReport();
		initPage();
		
		initCategory();
	}
	
	/**
	 * 初始化品类信息
	 */
	private void initCategory(){
		if("2".equals(template.getType()) || "12".equals(template.getType()) || "22".equals(template.getType())){	//分销管理(门店)，库存检查(经销商)
			//显示提示文字和品类单选框
			LinearLayout ll_rpt_prompt = (LinearLayout) findViewById(R.id.ll_rpt_prompt);
			ll_rpt_prompt.setVisibility(View.VISIBLE);
			
			//得到选择品类的控件
			category = (TextView) findViewById(R.id.tv_rpt_category);
			category.setOnClickListener(this);
		}
	}

	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				Tool.stopProgress();
				switch (msg.what) {

				case Constants.CustomGridType.PICKPHOTO:
					selectData = (Fields) msg.obj;
					Intent i = new Intent(getContext(), Frm_PickPhoto.class);
					i.putExtra("type", "1");
					i.putExtra("name", "新品拍照");
					i.putExtra("teminalCode", selectData.getStrValue("serverid"));
					i.putExtra("terminalname", selectData.getStrValue("fullname"));
					i.putExtra("clienttype", "1");

					i.putExtra("productid", Frm_Rpt.this.getIntent().getStringExtra("productid"));

					i.putExtra("key", key);
					activity.startActivityForResult(i, 20000);
					// OpenCarmer(TAKE_PRODUCT_PICTURE);1
					// selectProduct = (Fields) msg.obj;
					break;

				// case Constants.CustomGridType.RESETREPORT:
				// // initReport(strMsg);
				// // retRptButton();
				// resetRptHead();
				// // resetButton();
				// resetMainSView();
				// // resetPhotoLine();
				// break;
				//
				// case Constants.CustomGridType.ADDHOC:
				//
				// selectData = (Fields) msg.obj;
				//
				// // Tool.showMsg(getCurrContext(), strMsg);
				// break;

				default:
					super.handleMessage(msg);
					break;
				}
			}
		};
	}

	private void finish(int type) {
		Intent i = new Intent();
		i.putExtra("type", this.getIntent().getStringExtra("type"));
		setResult(type, i);
		
		application.pullActivity(this);
		this.finish();
	}

	/**
	 * 设置title
	 */
	private void initTitle() {
		title = (TextView) findViewById(R.id.txt);
		title.setText(this.getIntent().getStringExtra("name"));
	}

	private void initReport() {
		template = TemplateFactory.getTemplate(context, this.getIntent().getStringExtra("type"));
		if("3".equals(template.getType())){	//促销活动(纸品)
			if(getIntent().getStringExtra("size") != null){
				template.setOnlyType(Integer.parseInt(getIntent().getStringExtra("size"))+(3*1000)+1);
			}
		}
		if("13".equals(template.getType())){	//促销活动(卫品)
			if(getIntent().getStringExtra("size") != null){
				template.setOnlyType(Integer.parseInt(getIntent().getStringExtra("size"))+(13*1000)+1);
			}
		}

		report = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 1, key);
		
		report.setField("ClientType", this.getIntent().getStringExtra("clienttype"));
		
		if("3".equals(template.getType()) || "13".equals(template.getType())){
			if(!"".equals(getIntent().getStringExtra("type2"))){	//促销活动反馈
				title.setText("促销活动反馈");
				
				if(getIntent().getBooleanExtra("isSystem", false) == false){	//非系统促销活动反馈
					SObject reReport = report;
					template = TemplateFactory.getTemplate(context, this.getIntent().getStringExtra("type2"));
					template.setOnlyType(Integer.parseInt(reReport.getField("serverid")));
					report = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 1, key);
					report.setField("int1", reReport.getField("serverid"));
					report.setField("str4", reReport.getField("str1"));
					report.setField("str2", reReport.getField("str2"));
					report.setField("str3", reReport.getField("str3"));
					report.setField("ClientType", this.getIntent().getStringExtra("clienttype"));
					
					Fields data = new Fields();
					if("4".equals(this.getIntent().getStringExtra("type2"))){
						data.Set("dicttype", "211");
					}else{
						data.Set("dicttype", "216");
					}
					data.Set("dictclass", reReport.getField("int2"));
					FieldsList list = Sqlite.getFieldsList(context, 414, data);
					String promotion = "";
					if(list.getList() != null){
						promotion = list.getFields(0).getStrValue("dictname");
					}
					
					report.setField("str5", promotion);
					
					if("".equals(report.getField("str1"))){
						report.setField("str1", "地堆一平米，地堆两平米");
					}
				}else{
					template = TemplateFactory.getTemplate(context, this.getIntent().getStringExtra("type2"));
					template.setOnlyType(Integer.parseInt(getIntent().getStringExtra("serverid")));
					report = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 1, key);
					report.setField("int1", getIntent().getStringExtra("serverid"));
					report.setField("str4", getIntent().getStringExtra("oaodd"));
					report.setField("str2", getIntent().getStringExtra("beginTime"));
					report.setField("str3", getIntent().getStringExtra("endTime"));
					report.setField("ClientType", this.getIntent().getStringExtra("clienttype"));
					
					Fields data = new Fields();
					if("4".equals(this.getIntent().getStringExtra("type2"))){
						data.Set("dicttype", "211");
					}else{
						data.Set("dicttype", "216");
					}
					data.Set("dictclass", getIntent().getStringExtra("promotion"));
					FieldsList list = Sqlite.getFieldsList(context, 414, data);
					String promotion = "";
					if(list.getList() != null){
						promotion = list.getFields(0).getStrValue("dictname");
					}
					
					report.setField("str5", promotion);
					
					if("".equals(report.getField("str1"))){
						report.setField("str1", "地堆一平米，地堆两平米");
					}
				}
			}
		}
	}

	private void initPage() {
		initMainView();
		initProductLine();
		initPhotoLine();
		setupButton();
	}

	private void initButton() {
		if (buttonlist == null) {
			buttonlist = template.getButtonList();
		}
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
		int count = buttonlist == null ? 0 : buttonlist.size();
		if (count > 0) {
			layoutParams.width = Tool.getBWidth(activity, count);

			for (int i = 0; i < count; i++)
				lineButton.addView(getRptButton(buttonlist.get(i)));
		} else {
			lineButton.setVisibility(View.GONE);
		}
	}

	private CButton getRptButton(ButtonConfig config) {
//		CButton button = new CButton(context, config, layoutParams, template, this);
//		button.setOnClickListener(this);
		
		CButton button = new CButton(context, config, layoutParams, template);
		button.setOnClickListener(this);
		
		return button;
	}

	private void initMainView() {
		if (template != null && template.havePanal()) {
			if (mainSView == null) {
				mainSView = (ScrollView) findViewById(R.id.sv_detail_bottom);
				getMainLine();
				mainSView.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private LinearLayout getMainLine() {
		if (mainLine == null) {
			mainLine = (LinearLayout) findViewById(R.id.line_main);
		}
		
		for (Panal panal : template.getPanalList()) {
			//添加相对应的面板
			if("detail".equals(panal.getType())){
				mainLine.addView(new RptTable(context, template, panal, report, handler, this, getRptOnClickListener()));
			}else{
				mainLine.addView(new RptTable(context, panal, report, handler, this, getRptOnClickListener()));
			}
		}
		return mainLine;
	}

	private CCellTakePhoto selectImgButton;

	private OnClickListener getRptOnClickListener() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {

				if (v instanceof CCellTakePhoto) {
					selectImgButton = (CCellTakePhoto) v;
					Tool.setCurItem(selectImgButton.getCurItem());
					Tool.setRpt(report);
					toRptPhoto(4000);
				}

			}
		};
		return listener;
	}

	private void toRptPhoto(int type) {
		Intent i = new Intent(this, Frm_RptPhoto.class);
		i.putExtra("terminalname", this.getIntent().getStringExtra("terminalname"));
		startActivityForResult(i, type);
	}

	private OnNumberClickListener mOnNumberClickListener = new OnNumberClickListener() {
		public void OnNumberClick(String numberStr) {
			customGrid.setNumeric(numberStr);
		}
	};

	private void initProductLine() {
		if (template != null && template.haveTable()) {
			if (productLine == null) {
				productLine = (LinearLayout) findViewById(R.id.line_rpt_product);
			}
			productLine.addView(getCustomGrid(template.getTableItem()));

			if (!template.havePanal()) {
				productLine.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initView() {
		if (view == null)
			view = LayoutInflater.from(context).inflate(R.layout.rpt_detail_grid, null);
	}

	private void initKeyboard() {
		if (keyboard == null) {
			keyboard = new NumericKeyboard(view);
			keyboard.setOnNumberClickListener(mOnNumberClickListener);
		}
	}

	private void initCustomGrid() {
		if (customGrid == null) {
			customGrid = (CustomGrid) view.findViewById(R.id.parrow_grid);
			customGrid.setNumericKeyboard(keyboard);
		}
	}

	private void initData() {
		hashtable = new Hashtable<String, Fields>();
		if (report.getDetailCount() > 0) {

			if (template.getOnlyType() == 6001) {
				for (Fields data : report.getDetailfields().getList()) {
					if (data.getIntValue("int1") == 1)
						hashtable.put(data.getStrValue("serverid"), data);
				}
			} else {
				for (Fields data : report.getDetailfields().getList()) {
					if (data.getIntValue("int3") != 0)
						hashtable.put(data.getStrValue("serverid"), data);
				}
			}

		}
	}

	private View getCustomGrid(List<UIItem> itemlist) {
		initView();
		initKeyboard();
		initCustomGrid();
		initData();
		customGrid.setDataInfo(itemlist, report.getDetailfields());
		customGrid.setDataHashtable(hashtable);
		customGrid.setLinkHandler(handler);
		return view;
	}

	private int mIndex;

	private OnTouchListener getOnTouchListener() {

		OnTouchListener listener = new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					mIndex = 100 + v.getId();
					OpenCarmer();
					if (template.getOnlyType() > 5000 && template.getOnlyType() < 5999)
						clickImgView = (CImageView) v;
				}
				return true;
			}
		};
		return listener;
	}

	private OnClickListener getOnClickListener() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				delView(v.getId());
			}
		};
		return listener;
	}

	private void delView(int id) {
		CImageView view;
		for (int i = 0; i < photoLine.getChildCount(); i++) {
			view = (CImageView) photoLine.getChildAt(i);
			if (view.getId() == id) {
				photoLine.removeViewAt(i);
				photo.remove(id);
				break;
			}
		}
	}

	private CImageView getRptPhoto(int index) {

		imageView[index] = new CImageView(context, template, getOnTouchListener(), photo, index, getOnClickListener());
		layoutParamsPhoto.height = Tool.dip2px(context, 150);
		layoutParamsPhoto.topMargin = 5;
		imageView[index].setLayoutParams(layoutParamsPhoto);
		return imageView[index];
	}

	public void OpenCarmer() {
		try {
			Tool.createPhotoFile();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/"+getResources().getString(R.string.folder_name)+"photo//test.JPEG")));
			startActivityForResult(intent, TAKE_PICTURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String strErrMsg = "";
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == TAKE_PICTURE) {
					final Bitmap bm = Tool.lessenUriImage();
					String date = Tool.getCurrPhotoTime();
					Bitmap bm1 = Tool.generatorContactCountIcon(bm, date,
							this.getIntent().getStringExtra("terminalname"), getIntent().getStringExtra("name"),context);

					if (template.getOnlyType() > 5000 && template.getOnlyType() < 5999) {
						clickImgView.setImageBitmap(bm1, date, 1);//
					} else {
						boolean bHave = report.isHavePhoto(mIndex);
						imageView[mIndex].setImageBitmap(bm1, date, 0);//
						if (!bHave) {
							photoLine.addView(getRptPhoto(mTotalPhoto));
							mTotalPhoto++;
						}
					}

				} else if (requestCode == 20000) {
					selectData.put("str10", "已保存");
					customGrid.invalidate();
				} else if (requestCode == 4000) {
					selectImgButton.resetCount();
				}
			}
		} catch (Exception e) {
			Tool.showToastMsg(context, "拍照错误" + strErrMsg + e.getMessage(), AlertType.ERR);

		}
	}

	private int mTotalPhoto = 1;

	private Fields getPhotoData(DicData dicData) {
		Fields data;
		for (int i = 0; i < report.getAttCount(); i++) {
			data = report.getAttfields().getFields(i);
			if (data.getStrValue("displayobject").equals(dicData.getValue()))
				return data;
		}
		data = new Fields();
		data.put("displayobject", dicData.getValue());
		return data;
	}

	private HashMap<Integer, Fields> photo = new HashMap<Integer, Fields>();

	private void initPhotoLine() {
		if (photoSView == null)
			photoSView = (ScrollView) findViewById(R.id.sv_photo);

		photoLine = (LinearLayout) findViewById(R.id.line_rpt_photo);

		if (template.getOnlyType() > 5000 && template.getOnlyType() < 5999) {
			List<DicData> list = Sqlite.getDictDataList(context, template.getPhotodict(), "");
			int index = 0;

			for (DicData dictdata : list) {
				photo.put(index - 100, getPhotoData(dictdata));
				photoLine.addView(new RptTable(context, dictdata, photo, getOnTouchListener(), index));
				index++;
			}
		} else {
			mTotalPhoto = report.getAttCount() + 1;
			imageView = new CImageView[100];
			Fields data;
			for (int i = 0; i < mTotalPhoto; i++) {
				try {
					data = report.getAttfields().getFields(i);

					photo.put(i - 100, data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				photoLine.addView(getRptPhoto(i));
			}
		}

//		// 纸品或者卫品的陈列检查
//		if ("1".equals(template.getType()) || "11".equals(template.getType())){
//			photoSView.setVisibility(View.VISIBLE);
//		}
	}
	
	@SuppressWarnings("unused")
	private CImageView getRptPhoto(int index, Bitmap bitmap) {

		imageView[index] = new CImageView(context, template,
				getOnClickListener(), report, index);
		imageView[index].setId(index - 100);

		layoutParamsPhoto.topMargin = 5;

		imageView[index].setLayoutParams(layoutParamsPhoto);

		return imageView[index];
	}

	private void hideKey(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	private void resetPage(int id) {

		switch (id) {
		case 1:
			if (productLine != null)
				productLine.setVisibility(View.GONE);
			if (photoSView != null)
				photoSView.setVisibility(View.GONE);
			if (mainSView != null) {
				mainSView.setVisibility(View.VISIBLE);
			}
			break;
		case 2:
			if (photoSView != null)
				photoSView.setVisibility(View.GONE);
			if (mainSView != null)
				mainSView.setVisibility(View.GONE);
			if (productLine != null) {
				hideKey(productLine);
				productLine.setVisibility(View.VISIBLE);
			}
			break;
		case 3:
			if (productLine != null)
				productLine.setVisibility(View.GONE);
			if (mainSView != null)
				mainSView.setVisibility(View.GONE);
			if (photoSView != null) {
				hideKey(photoSView);
				photoSView.setVisibility(View.VISIBLE);
				photoSView.setAnimation(Tool.getAnimation(context));
			}
			break;

		default:
			break;
		}
	}

	public void onClick(View v) {

		if (v.getId() < 0) {
			mIndex = 100 + v.getId();
			OpenCarmer();
		} else {
			resetbutton(v.getId());

			switch (v.getId()) {
			case R.id.back:
				showBackDialog(RESULT_CANCELED);
				break;

			case R.id.save:
				SaveData();
				break;

			case R.id.tv_rpt_category:
				showSingleChoiceDialog();
				break;

			default:
				resetPage(v.getId());
				break;
			}
		}
	}

	private AlertDialog singleChoiceDialog;
	private int itemIndex = -1;
	private void showSingleChoiceDialog()
	{
		UIItem item = new UIItem();
		
		if("2".equals(template.getType())){	//纸品
			item.setDicId("222");
		}else if("12".equals(template.getType())){	//卫品
			item.setDicId("122");
		}else if("22".equals(template.getType())){	//库存检查
			item.setDicId("322");
		}
		
		final ChoiceGroupAdapter choiceGroupAdapter = new ChoiceGroupAdapter(getContext(), item);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("品类选择");
		builder.setSingleChoiceItems(choiceGroupAdapter, itemIndex, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				itemIndex = which;
				category.setText(choiceGroupAdapter.getName(which));
				resetReportByCategory(choiceGroupAdapter.getValue(which));
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", null);
		singleChoiceDialog = builder.create();
		singleChoiceDialog.show();
	}
	
	
	private List<Fields> checkedArray = new ArrayList<Fields>();
	/**
	 * 根据品类id来重新加载数据
	 * @param levelid
	 */
	private void resetReportByCategory(String levelid){
		//得到用户选中的数据
		for(Fields f : report.getDetailfields().getList()){
			if("2".equals(template.getType()) || "12".equals(template.getType())){	//分销管理（纸品和卫品）
				if("1".equals(f.getStrValue("int1")) || "1".equals(f.getStrValue("int2"))){
					checkedArray.add(f);
				}
			}else{	//库存检查（经销商）
				if(f.getStrValue("int1") != null && !"".equals(f.getStrValue("int1"))){
					checkedArray.add(f);
				}
			}
		}
		
		//得到所有品类的产品
		Rms.setCheckProduct(context, true);	// 开启辅助
		FieldsList fieldsGroup = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 1, key).getDetailfields();
		List<Fields> fieldsArray = fieldsGroup.getList();
		//存放用户选择的品类下的产品
		List<Fields> newFieldsGroup = new ArrayList<Fields>();
		for(Fields fields : fieldsArray){
			if("all".equals(levelid)){	//添加所有产品
				newFieldsGroup.add(fields);
			}else{	//添加用户选择的品类下的产品
				if(levelid.equals(fields.getStrValue("levelid"))){
					newFieldsGroup.add(fields);
				}
			}
		}
		
		//如果选中的数据集当中有当前品类的就在当前品类中替换掉,并排序，选中的排前面
		List<Fields> newFieldsGroup2 = new ArrayList<Fields>();	//用来存放排序后的数据
		for(int i=0; i<newFieldsGroup.size(); i++){
			for(Fields checkedf : checkedArray){
				if(newFieldsGroup.get(i).getStrValue("serverid").equals(checkedf.getStrValue("serverid"))){
					newFieldsGroup.remove(i);	//在数据集合中删除选中的数据，留下未选中的数据
					newFieldsGroup2.add(checkedf);	//添加选中的数据
				}
			}
		}
		
		//添加未选中的数据
		for(Fields f : newFieldsGroup){
			newFieldsGroup2.add(f);
		}
		
		//刷新页面
		report.getDetailfields().setList(newFieldsGroup2);
		initData();
		customGrid.setDataInfo(template.getTableItem(), report.getDetailfields());
		customGrid.setDataHashtable(hashtable);
		customGrid.setLinkHandler(handler);
		customGrid.invalidate();
	}
	
	private void showBackDialog(final int type) {
		stopDialog(backAlertDialog);
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("数据未保存，确认返回？");
		builder.setIcon(android.R.drawable.ic_dialog_info);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				stopDialog(backAlertDialog);
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

	private void stopDialog(AlertDialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private void resetbutton(int id) {
		CButton b;
		for (int i = 0; i < lineButton.getChildCount(); i++) {
			b = ((CButton) lineButton.getChildAt(i));
			if (b.getId() == id) {

				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg_click));
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, b.getText().toString(), true),
						null, null);
			} else {
				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg));
				b.setCompoundDrawablesWithIntrinsicBounds(null,
						Tool.getDrawable(context, b.getText().toString(), false), null, null);
			}
		}
	}

	private void SaveData() {
		//分销管理（纸品和卫品）	库存检查（经销商）
		if("2".equals(template.getType()) || "12".equals(template.getType()) || "22".equals(template.getType())){
			//得到用户选中的数据
			for(Fields f : report.getDetailfields().getList()){
				if("2".equals(template.getType()) || "12".equals(template.getType())){	//分销管理（纸品和卫品）
					if("1".equals(f.getStrValue("int1")) || "1".equals(f.getStrValue("int2"))){
						checkedArray.add(f);
					}
				}else{	//库存检查（经销商）
					if(f.getStrValue("int1") != null && !"".equals(f.getStrValue("int1"))){
						checkedArray.add(f);
					}
				}
			}
			
			Rms.setCheckProduct(context, true);	// 开启辅助
			report = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 1, key);
			//如果选中的数据集当中有当前品类的就在当前品类中替换掉
			for(int i=0; i<report.getDetailfields().getList().size(); i++){
				for(Fields checkedf : checkedArray){
					if(report.getDetailfields().getList().get(i).getStrValue("serverid").equals(checkedf.getStrValue("serverid"))){
						report.getDetailfields().getList().remove(i);
						report.getDetailfields().getList().add(checkedf);
					}
				}
			}
		}
		
		String errMsg = report.checkData();
		if (errMsg.equals("")) {
			Tool.showProgress(context, "");
			
			if("1".equals(template.getType())){	//陈列检查（纸品）
				report.setField("int1", (template.getOnlyType()-1000)+"");
			}

			if("11".equals(template.getType())){//陈列检查（卫品）
				report.setField("int1", (template.getOnlyType()-11*1000)+"");
			}
			
			//分销管理，库存检查
			if("22".equals(template.getType()) || "2".equals(template.getType()) || "12".equals(template.getType())){
				report.setField("ClientType", "1");
			}
			
			//促销活动
			if("3".equals(template.getType()) || "13".equals(template.getType())){
				report.setField("int1", this.getIntent().getStringExtra("teminalCode"));
				
				if(report.getField("str10") == null || "".equals(report.getField("str10"))){
					report.setField("str10", Tool.getMyUUID());
				}
			}
			
			long index = Sqlite.saveReport(context, report);

			Tool.stopProgress();
			if (index > 0) {
				if(!"3".equals(template.getType()) && !"13".equals(template.getType()) && !"4".equals(template.getType()) && !"14".equals(template.getType())){
					Tool.showToastMsg(context, "报告保存成功", AlertType.INFO);
				}
				finish(RESULT_OK);
			} else
				Tool.showToastMsg(context, "报告保存失败", AlertType.ERR);
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
		this.context = Frm_Rpt.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_Rpt.this;
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// init();
	// }

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
