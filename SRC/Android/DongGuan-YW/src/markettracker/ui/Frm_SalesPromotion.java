package markettracker.ui;

import orient.champion.business.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import markettracker.util.CButton;
import markettracker.util.CTable;
import markettracker.util.CTextView;
import markettracker.util.Constants;
import markettracker.util.ShowInfoBuilder;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.PhotoType;
import markettracker.util.Constants.AlertType;
import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
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
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

/**
 * 促销活动
 * @author Mark
 *
 */
@SuppressWarnings("unused")
public class Frm_SalesPromotion extends Activity implements OnClickListener {

	private Context context;
	private SyncDataApp application;
	private ListView showdata;
	private Button back, add;
	
	private FieldsList dataGroup;
	private Fields data;
	private int size = 0;
	private DataAdapter adapter;
	
	private boolean isUpload = false;	//是否即时上传

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sales_promotion_form);

		application = (SyncDataApp) this.getApplication();
		application.pushActivity(this);

		init();
	}

	private void init() {
		initContext();
	
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		add = (Button) findViewById(R.id.btn_sales_add);
		add.setOnClickListener(this);
		
		initData();
		initListView();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		data = new Fields();
		if("3".equals(getIntent().getStringExtra("type"))){
			data.Set("promotionobject", "1");	//1、纸品；2、卫品。
			data.Set("templateid", "3");	//3、纸品；13、卫品。
		}else{
			data.Set("promotionobject", "2");	//1、纸品；2、卫品。
			data.Set("templateid", "13");	//3、纸品；13、卫品。
		}
		
		data.Set("clientid", getIntent().getStringExtra("teminalCode"));	//门店id
		dataGroup = Sqlite.getFieldsList(context, 313, data);	//查询系统的促销活动和后台返回的有效期内的新增促销活动
		
		addNewDateGroup();
	}
	
	private FieldsList newDateGroup;
	
	/**
	 * 添加新增的促销活动
	 */
	private void addNewDateGroup(){
		if(dataGroup.getList() == null){
			dataGroup.setList(new ArrayList<Fields>());
		}
		
		newDateGroup = Sqlite.getFieldsList(context, 3133, data);
		if(newDateGroup.getList() != null){
			size = newDateGroup.getList().size();
			dataGroup.getList().addAll(newDateGroup.getList());
		}
	}
	
	/**
	 * 初始化ListView
	 */
	private void initListView(){
		showdata = (ListView) findViewById(R.id.lv_sales_showdata);
		
		if(dataGroup.getList() == null){
			dataGroup.setList(new ArrayList<Fields>());
		}
		
		adapter = new DataAdapter(context, android.R.layout.simple_expandable_list_item_1, dataGroup.getList());
		showdata.setAdapter(adapter);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishActivity();
			break;
		case R.id.btn_sales_add:
			toRptClass();
			break;
		default:
			break;
		}
	}
	
	private void toRptClass() {
		isUpload = true;	//即时上传
		
		Intent i = new Intent(this, Frm_Rpt.class);
		i.putExtra("type", getIntent().getStringExtra("type"));
		i.putExtra("name", getIntent().getStringExtra("name"));
		i.putExtra("teminalCode", getIntent().getStringExtra("teminalCode"));
		i.putExtra("clienttype", getIntent().getStringExtra("clienttype"));
		i.putExtra("displaytype", getIntent().getStringExtra("displaytype"));
		i.putExtra("terminalname", getIntent().getStringExtra("terminalname"));
		i.putExtra("size", size+"");
		i.putExtra("itemID", "");
		i.putExtra("type2", "");

		startActivityForResult(i, 1000);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == RESULT_OK) {
				if(dataGroup.getList() != null){
					dataGroup.getList().removeAll(dataGroup.getList());
				}
				
				initData();
				
				initListView();
				
				if(isUpload){
					Tool.showProgress(context, "数据上传中...", false, null, null);
					SyncData.startSyncData(this);
				}else{
					Tool.showToastMsg(context, "报告保存成功", AlertType.INFO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		this.context = Frm_SalesPromotion.this;
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
	
	/**
	 * 数据适配器
	 * @author Mark
	 *
	 */
	class DataAdapter extends ArrayAdapter<Fields> {

		public DataAdapter(Context context, int textViewResourceId, List<Fields> objects) {
			super(context, textViewResourceId, objects);
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 得到item视图
			convertView = LayoutInflater.from(context).inflate(R.layout.sales_promotion_item, null);
			
			RelativeLayout rl_sp_all = (RelativeLayout) convertView.findViewById(R.id.rl_sp_all);
			if(position%2 == 0){
				rl_sp_all.setBackgroundResource(R.color.white);
			}else{
				rl_sp_all.setBackgroundResource(R.color.background);
			}

			// 给相应的控件设值
			TextView tv_item_from = (TextView) convertView.findViewById(R.id.tv_item_from);
			if("".equals(dataGroup.getFields(position).getStrValue("empid")) || "2".equals(dataGroup.getFields(position).getStrValue("promotionsource"))){
				if("2".equals(dataGroup.getFields(position).getStrValue("promotionsource"))){
					tv_item_from.setText("来源：单店活动");
				}else{
					tv_item_from.setText("来源：单店活动 ");
				}
			}else{
				tv_item_from.setText("来源：系统活动");
			}
			
			TextView tv_item_odd = (TextView) convertView.findViewById(R.id.tv_item_odd);
			tv_item_odd.setText("OA单号："+dataGroup.getFields(position).getStrValue("str1"));

			TextView tv_item_begin_time = (TextView) convertView.findViewById(R.id.tv_item_begin_time);
			String beginTime = dataGroup.getFields(position).getStrValue("str2");
			if(beginTime != null){
				beginTime = beginTime.substring(0, 10);
			}
			tv_item_begin_time.setText("开始时间："+beginTime);

			TextView tv_item_end_time = (TextView) convertView.findViewById(R.id.tv_item_end_time);
			String endTime = dataGroup.getFields(position).getStrValue("str3");
			if(endTime != null){
				endTime = endTime.substring(0, 10);
			}
			tv_item_end_time.setText("结束时间："+endTime);
			
			//得到item,并给它添加点击事件
			LinearLayout ll_sales_item = (LinearLayout) convertView.findViewById(R.id.ll_sales_item);
			ll_sales_item.setTag(position);
			ll_sales_item.setOnClickListener(new OnClickListener() {
				
				public void onClick(View view) {
					showInfo(Integer.parseInt(view.getTag().toString()));
				}
			});

			return convertView;
		}

	}
	
	private ShowInfoBuilder showInfoBuild;
	private int itemIndex = -1;	//用户点击的item的索引
	private boolean isSystem = false;	//不是系统的单店活动
	private boolean isFeedBack = false;	//未反馈
	private boolean isUpdate = false;	//不可修改的往日单店活动
	
	private void showInfo(int position) {
		itemIndex = position;
		
		if (showInfoBuild != null) {
			showInfoBuild.dismiss();
			showInfoBuild = null;
		}
		
		if("".equals(dataGroup.getFields(position).getStrValue("empid"))){	//新增
			if("1".equals(dataGroup.getFields(position).getStrValue("isfeedback"))){	//已反馈
				isFeedBack = true;
			}else{
				isFeedBack = false;
			}
			
			isSystem = false;
			showInfoBuild = new ShowInfoBuilder(context, getOnClickListener1(),
					"OA单号："+dataGroup.getFields(position).getStrValue("str1"), 1, isSystem, isFeedBack);
		}else{	//系统
			isSystem = true;
			if("2".equals(dataGroup.getFields(position).getStrValue("promotionsource"))){	//往日单店活动
				if("1".equals(dataGroup.getFields(position).getStrValue("isfeedback"))){	//已反馈
					isFeedBack = true;
					isUpdate = false;
				}else{
					isFeedBack = false;
					isUpdate = true;
				}
				
				showInfoBuild = new ShowInfoBuilder(context, getOnClickListener1(),
						"OA单号："+dataGroup.getFields(position).getStrValue("str1"), 1, false, isFeedBack);
			}else{
				showInfoBuild = new ShowInfoBuilder(context, getOnClickListener1(),
						"OA单号："+dataGroup.getFields(position).getStrValue("str1"), 1, isSystem, false);
			}
			
		}
	}
	
	private OnClickListener getOnClickListener1() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.set:
					showInfoBuild.dismiss();
					toRptClass2("");
					break;

				case R.id.edit:
					showInfoBuild.dismiss();
					if("3".equals(getIntent().getStringExtra("type"))){
						toRptClass2("4");
					}else{
						toRptClass2("14");
					}
					break;
				}
			}
		};
		return listener;
	}
	
	private void toRptClass2(String type) {
		isUpload = false;	//不即时上传
		
		Intent i = new Intent(this, Frm_Rpt.class);
		i.putExtra("type", getIntent().getStringExtra("type"));
		i.putExtra("name", getIntent().getStringExtra("name"));
		i.putExtra("teminalCode", getIntent().getStringExtra("teminalCode"));
		i.putExtra("clienttype", getIntent().getStringExtra("clienttype"));
		i.putExtra("displaytype", getIntent().getStringExtra("displaytype"));
		i.putExtra("terminalname", getIntent().getStringExtra("terminalname"));
		
		if(!isSystem){
			i.putExtra("size", ""+(Integer.parseInt(dataGroup.getFields(itemIndex).getStrValue("onlytype"))-Integer.parseInt(getIntent().getStringExtra("type"))*1000-1));
		}
		
		i.putExtra("itemID", "");
		
		i.putExtra("type2", type);	//促销反馈
		
		i.putExtra("isSystem", isSystem);	//标识是否为系统促销反馈
		i.putExtra("isUpdate", isUpdate);	

		if(isSystem || isUpdate ){ 	//系统促销反馈 或者 可以修改
			i.putExtra("serverid", dataGroup.getFields(itemIndex).getStrValue("serverid"));
			i.putExtra("oaodd", dataGroup.getFields(itemIndex).getStrValue("str1"));	//OA单号
			i.putExtra("promotion", dataGroup.getFields(itemIndex).getStrValue("int2"));		//促销方式
			
			//活动开始时间
			String beginTime = dataGroup.getFields(itemIndex).getStrValue("str2");
			if(beginTime != null){
				beginTime = beginTime.substring(0, 10);
			}
			i.putExtra("beginTime", beginTime);
			
			//活动结束时间
			String endTime = dataGroup.getFields(itemIndex).getStrValue("str3");
			if(endTime != null){
				endTime = endTime.substring(0, 10);
			}
			i.putExtra("endTime", endTime);
		}

		startActivityForResult(i, 1000);
	}

}
