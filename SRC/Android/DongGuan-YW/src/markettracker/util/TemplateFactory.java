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

	public static TemGroupList getTASKTemplateGroupList() {
		TemGroupList list = new TemGroupList();
		TemplateGroup group = new TemplateGroup();
		group.setName("事件拜访报告");
		group.setShowTitle(false);
		Template t = new Template();
		t.setType("20");
		t.setOnlyType(20);
		t.setName("事件拜访报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		return list;
	}

	private static void addTemplate(Context context, TemplateGroup group, String dictId, int type) {
		List<DicData> dictList = Sqlite.getDictDataList(context, dictId, "");

		if (type == 9) { // BA检查报告
			groupSize = dictList.size();
		}

		int tempType;
		Template t = null;
		for (DicData dic : dictList) {
			if (type == 10) {
				tempType = type * 10000 + Integer.parseInt(dic.getValue());
			} else {
				tempType = type * 1000 + Integer.parseInt(dic.getValue());
			}

			t = new Template();
			t.setType(tempType + "");
			t.setOnlyType(tempType);
			t.setName(dic.getItemname());
			// if (type == 1 || type == 6)
			// t.setMutiInput(true);
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

	public static TemGroupList getBrandTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();

		TemplateGroup group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("生意表现");
		Template t = new Template();
		t.setType("1");
		t.setOnlyType(1);
		t.setName("生意表现");
		group.setTemplate(t);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setName("品牌");
		addTemplate(context, group, "161", 568);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("BA检查报告");
		addTemplate(context, group, "101115", 9);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("竞品报告");
		addTemplate(context, group, "-100", 10);
		list.setTempGroup(group);
		
		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("拜访总结");
		t = new Template();
		t.setType("11");
		t.setOnlyType(11);
		t.setName("拜访总结");
		group.setTemplate(t);
		list.setTempGroup(group);

		return list;
	}

	public static TemGroupList getTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();

		TemplateGroup group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("柜台检查报告");
		Template t = new Template();
		t.setType("2");
		t.setOnlyType(2);
		t.setName("柜台检查报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("促销活动报告");
		t = new Template();
		t.setType("3");
		t.setOnlyType(3);
		t.setName("促销活动报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("价格检查报告");
		t = new Template();
		t.setType("4");
		t.setOnlyType(4);
		t.setName("价格检查报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("试用装检查报告");
		t = new Template();
		t.setType("5");
		t.setOnlyType(5);
		t.setName("试用装检查报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("赠品管理报告");
		t = new Template();
		t.setType("6");
		t.setOnlyType(6);
		t.setName("赠品管理报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("库存报告");
		t = new Template();
		t.setType("7");
		t.setOnlyType(7);
		t.setName("库存报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		group = new TemplateGroup();
		group.setShowTitle(true);
		group.setName("品牌总结");
		t = new Template();
		t.setType("8");
		t.setOnlyType(8);
		t.setName("品牌总结");
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
		// t.setPhoto(true);
		Panal panal = new Panal();
		panal.setCaption("拜访总结");
		panal.setType(Constants.PanalType.PANEL);
		UIItem item = new UIItem();
		item.setCaption("拜访总结");
		item.setShowLable(false);
		item.setControlType(ControlType.TEXT);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		item.setMaxLength(50);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.HORIZONTAL);
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
	 * 店铺基本情况
	 * 
	 * @return
	 */
	private static Template getDPJBQKTemplate() {
		Template t = new Template();
		t.setType("1");
		t.setName("店铺基本情况");
		t.setOnlyType(1);

		Panal panal = new Panal();
		panal.setCaption("店铺基本情况");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();
		item.setCaption("本品在店内销售排名");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int3");
		item.setDicId("102");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("柜台版本");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int4");
		item.setDicId("103");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("柜台位置");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int5");
		item.setDicId("104");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("位置描述");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("专柜拍照");
		item.setControlType(ControlType.TAKEPHOTO);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setVerifytype("number");
		// item.setDataKey("int4");
		item.setMaxValue(999);
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		// ButtonConfig button = new ButtonConfig();
		// button.setId(1);
		// button.setName("基本信息");
		// t.setButton(button);
		//
		// button = new ButtonConfig();
		// button.setId(3);
		// button.setName("拍照");
		// t.setButton(button);

		return t;
	}

	/**
	 * 柜台检查报告
	 * 
	 * @return
	 */
	private static Template getGTJCTemplate(Context context) {
		Template t = new Template();
		t.setType("2");
		t.setName("柜台检查报告");
		t.setOnlyType(2);

		Panal panal = new Panal();
		panal.setCaption("柜台检查报告");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();
		item.setCaption("本店销售竞品品牌");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int3");
		item.setDicId("162");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		t.setPanal(panal);
		
		panal = new Panal();
		panal.setCaption("柜台检查报告");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANELDETAIL);
		
		addUIItem(context, panal, "165", "40");
		
		t.setPanal(panal);
		
		panal = new Panal();
		panal.setCaption("拍照");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);
		
		item = new UIItem();
		item.setCaption("柜台检查");
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
	 * BA检查报告
	 * 
	 * @param type
	 * @return
	 */
	private static Template getBAJCTemplate(int type) {
		Template t = new Template();
		t.setType("9");
		t.setName("BA检查报告");
		t.setOnlyType(type);

		Panal panal = new Panal();
		panal.setCaption("BA检查报告");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();
		item.setCaption("BA姓名");

		if (type == (9 * 1000 + groupSize)) {
			item.setControlType(ControlType.TEXT);
			item.setMustInput(true);
		} else {
			item.setControlType(ControlType.NONE);
		}

		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("BA检查报告");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PRODUCTTABLE);

		item = new UIItem();
		item.setCaption("评价角度");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setAlign(Align.LEFT);
		item.setDicId("121");
		item.setDataKey("productname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("得分");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("number");
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setAlign(Align.CENTER);
		item.setDataKey("int1");
		panal.setItem(item);

		t.setPanal(panal);

		ButtonConfig button = new ButtonConfig();
		button.setId(1);
		button.setName("基本信息");
		t.setButton(button);

		button = new ButtonConfig();
		button.setId(2);
		button.setName("BA检查");
		t.setButton(button);

		return t;
	}

	/**
	 * 促销活动报告
	 * 
	 * @return
	 */
	private static Template getCXHDTemplate() {
		Template t = new Template();
		t.setType("3");
		t.setName("促销活动报告");
		t.setOnlyType(3);

		Panal panal = new Panal();
		panal.setCaption("促销活动报告");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();

		item = new UIItem();
		item.setCaption("促销活动主题");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("促销活动形式");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int4");
		item.setDicId("163");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("促销活动时间段");
		panal.setType(Constants.PanalType.PANEL);

		item = new UIItem();
		item.setCaption("开始");
		item.setControlType(ControlType.DATE);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str2");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("结束");
		item.setControlType(ControlType.DATE);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str3");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("促销物料");
		panal.setType(Constants.PanalType.PANEL);

		item = new UIItem();
		item.setCaption("促销物料");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int5");
		item.setDicId("164");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);
		
		panal = new Panal();
		panal.setCaption("拍照");
		panal.setType(Constants.PanalType.PANEL);
		
		item = new UIItem();
		item.setCaption("促销活动");
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
	 * 赠品管理报告
	 * 
	 * @return
	 */
	private static Template getZPGLTemplate() {
		Template t = new Template();
		t.setType("6");
		t.setName("赠品管理报告");
		t.setOnlyType(6);

		Panal panal = new Panal();
		panal.setCaption("赠品管理报告");
		panal.setType(Constants.PanalType.PRODUCTTABLE);

		UIItem item = new UIItem();
		item.setCaption("赠品");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setAlign(Align.CENTER);
		item.setDataKey("productname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("库存量");
		item.setControlType(ControlType.TEXT);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setVerifytype("number");
		item.setDataKey("int1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}

	/**
	 * 试用装检查报告
	 * 
	 * @return
	 */
	private static Template getSYZJCTemplate() {
		Template t = new Template();
		t.setType("5");
		t.setName("试用装检查报告");
		t.setOnlyType(5);

		Panal panal = new Panal();
		panal.setCaption("试用装检查报告");
		panal.setType(Constants.PanalType.PRODUCTTABLE);

		UIItem item = new UIItem();
		item.setCaption("试用装名称");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setAlign(Align.CENTER);
		item.setDataKey("productname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("缺货");
		item.setControlType(ControlType.SELECTED);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setVerifytype("number");
		item.setDataKey("int1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}

	/**
	 * 库存报告
	 * 
	 * @return
	 */
	private static Template getKCTemplate() {
		Template t = new Template();
		t.setType("7");
		t.setName("库存报告");
		t.setOnlyType(7);

		Panal panal = new Panal();
		panal.setCaption("库存报告");
		panal.setType(Constants.PanalType.PRODUCTTABLE);

		UIItem item = new UIItem();
		item.setCaption("重点产品名称");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 3);
		item.setAlign(Align.LEFT);
		item.setDataKey("productname");
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("库存量");
		item.setControlType(ControlType.TEXT);
		item.setWidth(Tool.getScreenWidth() / 3);
		item.setVerifytype("number");
		item.setDataKey("int1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("建议订货数");
		item.setControlType(ControlType.TEXT);
		item.setWidth(Tool.getScreenWidth() / 3);
		item.setVerifytype("number");
		item.setDataKey("int2");
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
	 * 竞品报告
	 * 
	 * @return
	 */
	private static Template getJPTemplate(int type) {
		Template t = new Template();
		t.setType("10");
		t.setName("竞品报告");
		t.setOnlyType(type);

		Panal panal = new Panal();
		panal.setCaption("竞品报告");
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();
		item.setCaption("上月销量");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int3");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("进货扣率");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int4");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("上市新品");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("促销活动");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int5");
		item.setDicId("163");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
	
		item = new UIItem();
		item.setCaption("补充说明");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str2");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}
	
	/**
	 * 品牌总结
	 * @return
	 */
	private static Template getPPZJTemplate() {
		Template t = new Template();
		t.setType("8");
		t.setName("品牌总结");
		t.setOnlyType(8);

		Panal panal = new Panal();
		panal.setCaption("品牌总结");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);

		UIItem item = new UIItem();
		item.setCaption("本品牌问题及待处理事项");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		t.setPanal(panal);

		return t;
	}
	
	/**
	 * 拜访总结
	 * @return
	 */
	private static Template getBFZJTemplate() {
		Template t = new Template();
		t.setType("11");
		t.setName("拜访总结");
		t.setOnlyType(11);
		
		Panal panal = new Panal();
		panal.setCaption("拜访总结");
		panal.setShowCaption(false);
		panal.setType(Constants.PanalType.PANEL);
		
		UIItem item = new UIItem();
		item.setCaption("本次拜访目的");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("number");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("int3");
		item.setDicId("168");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("拜访达成总结");
		item.setControlType(ControlType.TEXT);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		t.setPanal(panal);
		
		return t;
	}

	/**
	 * 销售日报
	 * @return
	 */
	private static Template getXSRBTemplate(int type) {
		Template t = new Template();
		t.setType("12");
		t.setName("销售日报");
		t.setOnlyType(type);
		
		Panal panal = new Panal();
		panal.setCaption("销售日期");
		panal.setType(Constants.PanalType.PANEL);
		
		UIItem item = new UIItem();
		item.setCaption("销售日期");
		item.setShowLable(false);
		item.setControlType(ControlType.NONE);
		item.setVerifytype("text");
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.VERTICAL);
		panal.setItem(item);
		
		t.setPanal(panal);
		
		panal = new Panal();
		panal.setCaption("销售日报");
		panal.setType(Constants.PanalType.PRODUCTTABLE);
		
		item = new UIItem();
		item.setCaption("门店名称");
		item.setControlType(ControlType.NONE);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setAlign(Align.LEFT);
		item.setDataKey("fullname");
		panal.setItem(item);
		
		item = new UIItem();
		item.setCaption("销量");
		item.setControlType(ControlType.TEXT);
		item.setWidth(Tool.getScreenWidth() / 2);
		item.setVerifytype("number");
		item.setDataKey("int1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);
		
		t.setPanal(panal);
		
		ButtonConfig button = new ButtonConfig();
		button.setId(1);
		button.setName("销售日期");
		t.setButton(button);

		button = new ButtonConfig();
		button.setId(2);
		button.setName("销量填写");
		t.setButton(button);
		
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
		if ("1".equals(type)) {
			return getDPJBQKTemplate();
		}

		else if ("2".equals(type)) {
			return getGTJCTemplate(context);
		}

		else if ("3".equals(type)) {
			return getCXHDTemplate();
		}

		else if ("4".equals(type)) {
			return getJGJCTemplate();
		}

		else if ("5".equals(type)) {
			return getSYZJCTemplate();
		}

		else if ("6".equals(type)) {
			return getZPGLTemplate();
		}

		else if ("7".equals(type)) {
			return getKCTemplate();
		}

		else if ("8".equals(type)) {
			return getPPZJTemplate();
		}

		else if (type.startsWith("9")) {
			return getBAJCTemplate(Integer.parseInt(type));
		}

		else if (type.startsWith("10")) {
			return getJPTemplate(Integer.parseInt(type));
		}

		else if (type.equals("11")) {
			return getBFZJTemplate();
		}
		
		else if (type.startsWith("12")) {
			return getXSRBTemplate(Integer.parseInt(type));
		}

		else if (type.startsWith("20")) {
			return getTASKTemplate();
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
