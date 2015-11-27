package markettracker.util;

import orient.champion.business.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import markettracker.data.ButtonConfig;
import markettracker.data.DicData;
import markettracker.data.Panal;
import markettracker.data.Sqlite;
import markettracker.data.TemGroupList;
import markettracker.data.Template;
import markettracker.data.TemplateGroup;
import markettracker.data.UIItem;
import markettracker.util.Constants.ControlType;
import android.content.Context;
import android.graphics.Paint.Align;
import android.view.Gravity;
import android.widget.LinearLayout;

public class TemplateFactory {
	private static int groupSize = 0;

	public static int getCallPlanTitleWidth() {
		return Tool.getScreenWidth() / 2;
	}

	/**
	 * 进店模板
	 * @return
	 */
	public static Template getStartTemplate() {
		Template t = new Template();
		t.setType("-1");
		t.setOnlyType(-1);
		t.setName("进店");

		return t;
	}

	/**
	 * 出店模板
	 * @return
	 */
	public static Template getLeaveTemplate() {
		Template t = new Template();
		t.setType("-2");
		t.setOnlyType(-2);
		t.setName("出店");

		return t;
	}

	/**
	 * 事件拜访模版
	 * @return
	 */
	public static TemGroupList getTASKTemplateGroupList() {
		TemGroupList list = new TemGroupList();
		
		TemplateGroup group = new TemplateGroup();
		group.setName("事件");
		group.setShowTitle(false);
		
		Template t = new Template();
		t.setType("20");
		t.setOnlyType(20);
		t.setName("事件");
		
		group.setTemplate(t);
		
		list.setTempGroup(group);

		return list;
	}

	@SuppressWarnings("unused")
	private static void addTemplate(Context context, TemplateGroup group, String dictId, int type) {
		List<DicData> dictList = Sqlite.getDictDataList(context, dictId, "");

		int tempType;
		Template t = null;
		for (DicData dic : dictList) {
			tempType = type * 1000 + Integer.parseInt(dic.getValue());

			t = new Template();
			t.setType(tempType + "");
			t.setOnlyType(tempType);
			t.setName(dic.getItemname());
			group.setTemplate(t);
		}
	}

	private static void addUIItem(Context context, Panal panal,  String dictId, String itemDictID) {
		List<DicData> dictList = Sqlite.getDictDataList(context, dictId, "");
		
		UIItem item;
		
		for(int i=0; i<dictList.size(); i++){
			item = new UIItem();
			item.setId(Integer.parseInt(dictList.get(i).getCode()));
			item.setCaption(dictList.get(i).getItemname());
			item.setServerid(dictList.get(i).getValue());
			item.setControlType(ControlType.RADIOBUTTON);
			item.setVerifytype("number");
			item.setTitleWidth(getCallPlanTitleWidth());
			item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			item.setDataKey("int1");
			item.setDicId(itemDictID);
			item.setOrientation(LinearLayout.VERTICAL);
			panal.setItem(item);
		}
	}

	/**
	 * 添加相对应的面板
	 * @param context
	 * @param t
	 * @param dictId
	 */
	private static void addPanal(Context context, Template t, String dictId) {
		List<DicData> dictList = Sqlite.getDictDataList(context, dictId, "");
		
		Panal panal;
		UIItem item;
		
		for(int i=0; i<dictList.size(); i++){
			panal = new Panal();
			panal.setCaption(dictList.get(i).getItemname());
			panal.setType(Constants.PanalType.PANELDETAIL);
			
			if(i==0){
				panal.setGroupName("本月预期BA考评目标");
				panal.setShowGroupName(true);
			}
			
			item = new UIItem();
			item.setId(Integer.parseInt(dictList.get(i).getCode()));	//数据id
			item.setCaption("BA销售达成金额");
			item.setServerid(dictList.get(i).getValue());	//用于与数据配置的serverid
			item.setControlType(ControlType.TEXT);
			item.setVerifytype("amount");
			item.setTitleWidth(getCallPlanTitleWidth());
			item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			item.setDataKey("str1");
			item.setOrientation(LinearLayout.VERTICAL);
			panal.setItem(item);

			item = new UIItem();
			item.setId(Integer.parseInt(dictList.get(i).getCode()));
			item.setCaption("销售客单价");
			item.setServerid(dictList.get(i).getValue());
			item.setControlType(ControlType.TEXT);
			item.setVerifytype("amount");
			item.setTitleWidth(getCallPlanTitleWidth());
			item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			item.setDataKey("str2");
			item.setOrientation(LinearLayout.VERTICAL);
			panal.setItem(item);

			item = new UIItem();
			item.setId(Integer.parseInt(dictList.get(i).getCode()));
			item.setCaption("销售连带率");
			item.setServerid(dictList.get(i).getValue());
			item.setControlType(ControlType.TEXT);
			item.setVerifytype("amount");
			item.setTitleWidth(getCallPlanTitleWidth());
			item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			item.setDataKey("str3");
			item.setOrientation(LinearLayout.VERTICAL);
			panal.setItem(item);

			item = new UIItem();
			item.setId(Integer.parseInt(dictList.get(i).getCode()));
			item.setCaption("老会员返店率");
			item.setServerid(dictList.get(i).getValue());
			item.setControlType(ControlType.TEXT);
			item.setVerifytype("amount");
			item.setTitleWidth(getCallPlanTitleWidth());
			item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			item.setDataKey("str4");
			item.setOrientation(LinearLayout.VERTICAL);
			panal.setItem(item);

			item = new UIItem();
			item.setId(Integer.parseInt(dictList.get(i).getCode()));
			item.setCaption("新会员招募率");
			item.setServerid(dictList.get(i).getValue());
			item.setControlType(ControlType.TEXT);
			item.setVerifytype("amount");
			item.setTitleWidth(getCallPlanTitleWidth());
			item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			item.setDataKey("str5");
			item.setOrientation(LinearLayout.VERTICAL);
			panal.setItem(item);
			
			t.setPanal(panal);
		}
	}
	
	/**
	 * 门店拜访模版
	 * @param context
	 * @return
	 */
	public static TemGroupList getTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();

		TemplateGroup group = new TemplateGroup();
		group.setShowTitle(false);
		group.setName("纸品");
		Template t = new Template();
		t.setType("101");
		t.setOnlyType(101);
		t.setName("纸品");
		group.setTemplate(t);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setShowTitle(false);
		group.setName("卫品");
		t = new Template();
		t.setType("102");
		t.setOnlyType(102);
		t.setName("卫品");
		group.setTemplate(t);
		list.setTempGroup(group);

		return list;
	}

	/**
	 * 经销商拜访模版
	 * @param context
	 * @return
	 */
	public static TemGroupList getDealersTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();
		
		TemplateGroup group = new TemplateGroup();
		group.setShowTitle(false);
		group.setName("拜访目的");
		Template t = new Template();
		t.setType("21");
		t.setOnlyType(21);
		t.setName("拜访目的");
		group.setTemplate(t);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setShowTitle(false);
		group.setName("库存检查");
		t = new Template();
		t.setType("22");
		t.setOnlyType(22);
		t.setName("库存检查");
		group.setTemplate(t);
		list.setTempGroup(group);
		
		return list;
	}

	/**
	 * 纸品报告模版组
	 * @param context
	 * @return
	 */
	public static TemGroupList getPaperProductsTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();

		TemplateGroup group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("陈列检查");
		addTemplate(context, group, "210", 1);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("分销管理");
		Template t = new Template();
		t.setType("2");
		t.setOnlyType(2);
		t.setName("分销管理");
		group.setTemplate(t);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("促销活动");
		t = new Template();
		t.setType("3");
		t.setOnlyType(3);
		t.setName("促销活动");
		group.setTemplate(t);
		list.setTempGroup(group);

		return list;
	}

	/**
	 * 卫品报告模版组
	 * @param context
	 * @return
	 */
	public static TemGroupList getWeiPinTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();
		
		TemplateGroup group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("陈列检查");
		addTemplate(context, group, "215", 11);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("分销管理");
		Template t = new Template();
		t.setType("12");
		t.setOnlyType(12);
		t.setName("分销管理");
		group.setTemplate(t);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("促销活动");
		t = new Template();
		t.setType("13");
		t.setOnlyType(13);
		t.setName("促销活动");
		group.setTemplate(t);
		list.setTempGroup(group);
		
		return list;
	}
	
	/**
	 * 得到销售日报的模版组
	 * @param context
	 * @return
	 */
	public static TemGroupList getXSRBTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();
		
		TemplateGroup group;
		Template t;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Calendar> calendarGroup = Tool.getBeforeDate();
		for(int i = calendarGroup.size()-1; i>-1; i--){
			group = new TemplateGroup();
			group.setName(dateFormat.format(calendarGroup.get(i).getTime())+"(销售日报)");
			t = new Template();
			t.setType((12*1000+i)+"");
			t.setOnlyType(12*1000+i);
			t.setName(dateFormat.format(calendarGroup.get(i).getTime()));
			group.setTemplate(t);
			list.setTempGroup(group);
		}
		
		return list;
	}

	public static TemGroupList getProductGroupList(Context context) {
		TemGroupList list = new TemGroupList();
		TemplateGroup group = new TemplateGroup();
		group.setImgId(R.drawable.jptx);
		group.setName("新品列表");
		addTemplate(context, group, "-10");

		list.setTempGroup(group);

		return list;
	}

	private static void addTemplate(Context context, TemplateGroup group, String dictId) {
		List<DicData> dictList = Sqlite.getDictDataList(context, dictId, "");

		Template t = null;
		for (DicData dic : dictList) {
			t = new Template();
			t.setType("6001");
			t.setOnlyType(6001);
			t.setName(dic.getItemname());
			t.setValue(dic.getValue());

			// if (type == 1 || type == 6)
			// t.setMutiInput(true);
			group.setTemplate(t);
		}
	}

	/**
	 * 洁面类品类陈列
	 * @return
	 */
	private static Template getJMPLTemplate() {
		Template t = new Template();
		t.setType("4");
		t.setName("8");
		t.setPltype("1");
		t.setOnlyType(999);
		t.setPhoto(true);

		Panal panal = new Panal();
		panal.setCaption("竞争品牌");
		panal.setType(Constants.PanalType.PANEL);
		UIItem item = new UIItem();
		item.setCaption("左品牌");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setDicId("10100");
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("int4");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("右品牌");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDicId("10100");
		item.setDataKey("int5");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);
		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("其他");
		panal.setType(Constants.PanalType.PANEL);
		item = new UIItem();
		item.setCaption("位置距主通道第几节");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("number");
		item.setMaxValue(10);
		item.setMinValue(1);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("int6");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("是否按陈列图陈列");
		item.setControlType(ControlType.RADIOBUTTON);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setDicId("36");
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("int7");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("POSM");
		item.setControlType(ControlType.MULTICHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setDicId("10015");
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);
		t.setPanal(panal);

		return t;
	}

	/**
	 * 事件
	 * @return
	 */
	private static Template getTASKTemplate() {
		Template t = new Template();
		t.setType("20");
		t.setName("事件");
		t.setOnlyType(20);
		
		Panal panal = new Panal();
		panal.setCaption("事件");
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();
		item.setCaption("事件描述");
		item.setShowLable(false);
		item.setControlType(ControlType.TEXT);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		item.setMaxLength(50);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("事件拍照");
		item.setControlType(ControlType.TAKEPHOTO);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setVerifytype("number");
		item.setMaxValue(999);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}

	/**
	 * 请假
	 * @return
	 */
	public static Template getQJTemplate() {
		Template t = new Template();
		t.setType("51");
		t.setName("请假");
		t.setOnlyType(51);
		// t.setPhoto(true);
		Panal panal = new Panal();
		panal.setCaption("时间");
		panal.setType(Constants.PanalType.PANEL);
		UIItem item = new UIItem();
		item.setCaption("开始时间");
		item.setControlType(ControlType.DATATIME);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("结束时间");
		item.setControlType(ControlType.DATATIME);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str2");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("原因说明");
		panal.setType(Constants.PanalType.PANEL);
		item = new UIItem();
		item.setCaption("请假类型");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		// item.setShowLable(false);
		item.setMustInput(true);
		item.setDicId("12003");
		item.setMaxLength(50);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("int1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("原因");
		item.setControlType(ControlType.TEXT);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		// item.setShowLable(false);
		item.setMaxLength(50);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str3");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}

	/**
	 * 分销
	 * @return
	 */
	private static Template getXDTemplate() {
		Template t = new Template();
		t.setType("20");
		t.setName("巡店报告");
		t.setOnlyType(20);
		// t.setPhoto(true);
		Panal panal = new Panal();
		panal.setCaption("柜台");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);
		UIItem item = new UIItem();
		item.setCaption("柜台类型");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int1");
		// item.setDicId("12000");
		item.setDicId("200");
		item.setMustInput(true);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("陈列");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);
		item = new UIItem();
		item.setCaption("陈列类型");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int2");
		// item.setDicId("12001");
		item.setDicId("200");
		item.setMustInput(true);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("陈列拍照");
		item.setControlType(ControlType.TAKEPHOTO);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setVerifytype("number");
		// item.setDataKey("int4");
		item.setMaxValue(999);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("促销");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);
		item = new UIItem();
		item.setCaption("店内促销");
		item.setControlType(ControlType.RADIOBUTTON);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int3");
		item.setMustInput(true);
		// item.setDicId("11004");
		item.setDicId("200");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("促销拍照");
		item.setControlType(ControlType.TAKEPHOTO);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setVerifytype("number");
		// item.setDataKey("int4");
		item.setMaxValue(999);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		t.setPanal(panal);

		return t;
	}

	/**
	 * 陈列检查
	 * @param type
	 * @param onlyType
	 * @return
	 */
	private static Template getDisplayCheckTemplate(String type, int onlyType) {
		Template t = new Template();
		t.setType(type);
		t.setName("陈列检查");
		t.setOnlyType(onlyType);

		Panal panal = new Panal();
		panal.setCaption("陈列检查");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);
		
		UIItem item = new UIItem();
		item.setCaption("陈列照片");
		item.setControlType(ControlType.TAKEPHOTO);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setVerifytype("number");
		item.setMaxValue(999);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		t.setPanal(panal);
		
		return t;
	}

	/**
	 * 分销管理
	 * @param type
	 * @return
	 */
	private static Template getDistributionManagementTemplate(int type) {
		Template t = new Template();
		t.setType(type+"");
		t.setName("分销管理");
		t.setOnlyType(type);

		Panal panal = new Panal();
		panal.setCaption("分销管理");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PRODUCTTABLE);

		UIItem item = new UIItem();
		item.setCaption("品类");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 5);
		item.setAlign(Align.CENTER);
		item.setDataKey("levelname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("产品名称");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setAlign(Align.LEFT);
		item.setDataKey("productname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("是否上架");
		item.setControlType(ControlType.SELECTED);
		item.setWidth((Tool.getScreenWidth() - Tool.getScreenWidth() / 5 - Tool.getScreenWidth() / 2) / 2);
		item.setAlign(Align.CENTER);
		item.setDataKey("int1");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("是否缺货");
		item.setControlType(ControlType.SELECTED);
		item.setWidth((Tool.getScreenWidth() - Tool.getScreenWidth() / 5 - Tool.getScreenWidth() / 2) / 2);
		item.setAlign(Align.CENTER);
		item.setDataKey("int2");
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}
	
	/**
	 * 促销活动-新增
	 * @param type
	 * @return
	 */
	private static Template getSalesPromotionTemplate(int type) {
		Template t = new Template();
		t.setType(type+"");
		t.setName("促销活动");
		t.setOnlyType(type);

		Panal panal = new Panal();
		panal.setCaption("促销活动");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();

		item = new UIItem();
		item.setCaption("OA单号");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setMustInput(true);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("促销方式");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setMustInput(true);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int2");
		
		if(type == 3){	//纸品
			item.setDicId("211");
		}else{	//卫品
			item.setDicId("216");
		}
		
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("活动开始时间");
		item.setControlType(ControlType.DATE);
		item.setVerifytype("text");
		item.setMustInput(true);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str2");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("活动结束时间");
		item.setControlType(ControlType.DATE);
		item.setVerifytype("text");
		item.setMustInput(true);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str3");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}

	/**
	 * 促销活动-反馈
	 * @param type
	 * @return
	 */
	private static Template getSalesPromotionFeedbackTemplate(int type) {
		Template t = new Template();
		t.setType(type+"");
		t.setName("促销活动反馈");
		t.setOnlyType(type);
		
		Panal panal = new Panal();
		panal.setCaption("促销活动反馈");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);
		
		UIItem item = new UIItem();
		
		item = new UIItem();
		item.setCaption("OA单号");
		item.setControlType(ControlType.NONE);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str4");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("促销方式");
		item.setControlType(ControlType.NONE);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str5");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("活动开始时间");
		item.setControlType(ControlType.NONE);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str2");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("活动结束时间");
		item.setControlType(ControlType.NONE);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str3");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("实际陈列反馈");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("促销反馈拍照");
		item.setControlType(ControlType.TAKEPHOTO);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		t.setPanal(panal);
		
		return t;
	}

	/**
	 *	拜访目的 
	 * @return
	 */
	private static Template getPurposeOfVisitTemplate() {
		Template t = new Template();
		t.setType("21");
		t.setName("拜访目的");
		t.setOnlyType(21);

		Panal panal = new Panal();
		panal.setCaption("拜访目的");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();
		item.setCaption("拜访目的");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int1");
		item.setDicId("217");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("备注");
		item.setControlType(ControlType.TEXT);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}

	/**
	 * 库存检查
	 * @return
	 */
	private static Template getInventoryCheckTemplate() {
		Template t = new Template();
		t.setType("22");
		t.setName("库存检查");
		t.setOnlyType(22);

		Panal panal = new Panal();
		panal.setCaption("库存检查");
		panal.setType(Constants.PanalType.PRODUCTTABLE);

		UIItem item = new UIItem();
		item.setCaption("品类");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 4);
		item.setAlign(Align.CENTER);
		item.setDataKey("levelname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("产品名称");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setAlign(Align.LEFT);
		item.setDataKey("productname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("库存数");
		item.setControlType(ControlType.TEXT);
		item.setWidth(Tool.getScreenWidth() / 4);
		item.setVerifytype("number");
		item.setDataKey("int1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}

	/**
	 * 价格检查报告
	 * @return
	 */
	private static Template getJGJCTemplate() {
		Template t = new Template();
		t.setType("4");
		t.setName("价格检查报告");
		t.setOnlyType(4);
		
		Panal panal = new Panal();
		panal.setCaption("价格检查报告");
		panal.setType(Constants.PanalType.PRODUCTTABLE);
		
		UIItem item = new UIItem();
		item.setCaption("重点产品名称");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 3);
		item.setAlign(Align.LEFT);
		item.setDataKey("productname");
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("标准零售价格");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 3);
		item.setVerifytype("number");
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("门店价格（元）");
		item.setControlType(ControlType.TEXT);
		item.setWidth(Tool.getScreenWidth() / 3);
		item.setVerifytype("amount");
		item.setDataKey("str2");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);
		
		t.setPanal(panal);
		
		return t;
	}

	/**
	 * 上班模版
	 * 
	 * @return
	 */
	// public static Template getGoToWorkTemplate() {
	// Template t = new Template();
	// t.setType("-3");
	// t.setOnlyType(-3);
	// t.setName("上班");
	// t.setPhoto(1);
	// Panal panal = new Panal();
	// panal.setCaption("备注");
	// panal.setType(Constants.PanalType.PANEL);
	// UIItem item = new UIItem();
	// item.setCaption("本日工作计划");
	// item.setControlType(ControlType.TEXT);
	// // item.setVerifytype("number");
	// // item.setMaxValue(1000000);
	// item.setTitleWidth(getCallPlanTitleWidth());
	// item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
	// item.setDataKey("str1");
	// // item.setMustInput(true);
	// item.setShowLable(false);
	// item.setOrientation(LinearLayout.HORIZONTAL);
	// panal.setItem(item);
	//
	// t.setPanal(panal);
	//
	// ButtonConfig button = new ButtonConfig();
	// button.setId(1);
	// button.setName("基本信息");
	// t.setButton(button);
	//
	// button = new ButtonConfig();
	// button.setId(3);
	// button.setName("拍照");
	// t.setButton(button);
	//
	// return t;
	// }

	/**
	 * 下班模板
	 * 
	 * @return
	 */
	// public static Template getOffWorkTemplate() {
	// Template t = new Template();
	// t.setType("-4");
	// t.setOnlyType(-4);
	// t.setName("下班");
	// t.setPhoto(1);
	// Panal panal = new Panal();
	// panal.setCaption("备注");
	// panal.setType(Constants.PanalType.PANEL);
	// UIItem item = new UIItem();
	// item.setCaption("本日工作报告");
	// item.setControlType(ControlType.TEXT);
	// // item.setVerifytype("number");
	// // item.setMaxValue(1000000);
	// item.setTitleWidth(getCallPlanTitleWidth());
	// item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
	// item.setDataKey("str1");
	// // item.setMustInput(true);
	// item.setShowLable(false);
	// item.setOrientation(LinearLayout.HORIZONTAL);
	// panal.setItem(item);
	//
	// t.setPanal(panal);
	//
	// ButtonConfig button = new ButtonConfig();
	// button.setId(1);
	// button.setName("基本信息");
	// t.setButton(button);
	//
	// button = new ButtonConfig();
	// button.setId(3);
	// button.setName("拍照");
	// t.setButton(button);
	//
	// return t;
	// }

	// public static Template getWorkContentTemplate() {
	// Template t = new Template();
	// t.setType("-6");
	// t.setOnlyType(-6);
	// t.setName("工作内容");
	// t.setPhoto(0);
	//
	// Panal panal = new Panal();
	// panal.setCaption("工作");
	// panal.setType(Constants.PanalType.PANEL);
	// UIItem item = new UIItem();
	// item.setCaption("本日工作计划");
	// item.setControlType(ControlType.TEXT);
	// // item.setVerifytype("number");
	// // item.setMaxValue(1000000);
	// item.setTitleWidth(getCallPlanTitleWidth());
	// item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
	// item.setDataKey("str1");
	// // item.setMustInput(true);
	// item.setOrientation(LinearLayout.HORIZONTAL);
	// panal.setItem(item);
	//
	// item = new UIItem();
	// item.setCaption("工作完成情况");
	// item.setControlType(ControlType.TEXT);
	// // item.setVerifytype("number");
	// // item.setMaxValue(1000000);
	// item.setTitleWidth(getCallPlanTitleWidth());
	// item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
	// item.setDataKey("str2");
	// // item.setMustInput(true);
	// item.setOrientation(LinearLayout.HORIZONTAL);
	// panal.setItem(item);
	// t.setPanal(panal);
	//
	// return t;
	// }

	public static Template getTemplate(Context context, String type) {
		if (type.startsWith("10") || type.startsWith("110")) {	//陈列检查
			if(type.startsWith("10")){	//纸品
				return getDisplayCheckTemplate("1",Integer.parseInt(type));
			}else{	//卫品
				return getDisplayCheckTemplate("11",Integer.parseInt(type));
			}
		}

		else if ("2".equals(type) || "12".equals(type)) {	//分销管理
			if("2".equals(type)){	//纸品
				return getDistributionManagementTemplate(2);
			}else{	//卫品
				return getDistributionManagementTemplate(12);
			}
		}

		else if ("3".equals(type) || "13".equals(type)) {	//促销活动－新增
			if("3".equals(type)){	//纸品
				return getSalesPromotionTemplate(3);
			}else{	//卫品
				return getSalesPromotionTemplate(13);
			}
		}

		else if ("4".equals(type) || "14".equals(type)) {	//促销活动－反馈
			if("4".equals(type)){	//纸品
				return getSalesPromotionFeedbackTemplate(4);
			}else{	//卫品
				return getSalesPromotionFeedbackTemplate(14);
			}
		}

		else if (type.startsWith("20")) {	//事件
			return getTASKTemplate();
		}

		else if ("21".equals(type)) {	//拜访目的(经销商)
			return getPurposeOfVisitTemplate();
		}

		else if ("22".equals(type)) {	//库存检查(经销商)
			return getInventoryCheckTemplate();
		}
		// else if (type.equals("-3")) {
		// return getGoToWorkTemplate();
		// } else if (type.equals("-4")) {
		// return getOffWorkTemplate();
		// }
		// else if (type.equals("-6"))
		// {
		// return getWorkContentTemplate();
		// }

		return null;
	}
}
