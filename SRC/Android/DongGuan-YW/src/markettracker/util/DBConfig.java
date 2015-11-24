package markettracker.util;

import java.util.ArrayList;
import java.util.List;

import markettracker.data.DBDetailConfig;
import markettracker.data.DBMainConfig;

public class DBConfig {

	private static List<DBMainConfig> tableList;

	public static DBMainConfig getDBMainConfig(String type) {
		for (DBMainConfig dc : getTableList()) {
			if (dc.getTableName().equalsIgnoreCase(type))
				return dc;
		}
		return null;
	}

	private static List<DBMainConfig> getDBConfig() {
		tableList = new ArrayList<DBMainConfig>();

		tableList.add(getTerrminal());
		tableList.add(getProduct());
		tableList.add(getDictionary());
		tableList.add(getPlan());
		
		tableList.add(getCallReport());
		tableList.add(getCallDetail());
		tableList.add(getCallPhoto());

		tableList.add(getMessage());
		
		
		tableList.add(getDP());
		tableList.add(getDPPhoto());
		
		
		tableList.add(getActivity());
		
		

		tableList.add(getSurveyHead());
		tableList.add(getSurveyQuestion());
		tableList.add(getSurveyQuestionOption());
		tableList.add(getSurveyRltClient());
		
		
		//ddd
		
		tableList.add(getDIsplay());
		tableList.add(getEoc());
		

		return tableList;
	}
	
	private static DBMainConfig getActivity() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Activity_Main");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ActivitiesCode");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayArea");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Cost");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Payment");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("STime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("ETime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("ClientId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("ClientCode");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("ClientAmountId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		
		d = new DBDetailConfig();
		d.setFieldName("EmpId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("EmpCode");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("Remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
	
		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}
	
	
	private static DBMainConfig getDP() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Client_Rlt_ActivityPromoter");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PromoterId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Mobile");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PromoterName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("WorkDay");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

	

		return m;
	}
	
	/**
	 * 报告附件
	 */
	private static DBMainConfig getDPPhoto() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_data_DPPhoto");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ShotTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("lShotTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("QuestionnaireId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("loginmobileTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("mobileTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("loginTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PhotoType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ProductId");
		d.setType(Constants.FieldType.STRING);
//		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayType");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("callReportId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Photo");
		d.setType(Constants.FieldType.BLOB);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayObject");
		d.setType(Constants.FieldType.STRING);
//		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);

		return m;
	}

	private static DBMainConfig getSurveyHead() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_PSQ");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PsqId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("title");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("empid");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("isPhoto");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("explain");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	private static DBMainConfig getSurveyQuestion() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_PSQ_Question");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("QuestionId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PSQId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("title");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("type");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("typeName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	private static DBMainConfig getSurveyQuestionOption() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_PSQ_Options");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("OptionId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("QuestionId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("value");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	private static DBMainConfig getSurveyRltClient() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_PSQ_Payout");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PayoutId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PSQId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Clientid");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("StartTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("EndTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Explain");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 
	 */
	private static DBMainConfig getMessage() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Message_Detail");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Title");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("errmsg");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Content");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Stime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Sender");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("status");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 报告附件
	 */
	private static DBMainConfig getCallPhoto() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_data_callReportPhoto");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ShotTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("lShotTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("QuestionnaireId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("loginmobileTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("mobileTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("loginTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PhotoType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ProductId");
		d.setType(Constants.FieldType.STRING);
//		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayType");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("callReportId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Photo");
		d.setType(Constants.FieldType.BLOB);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayObject");
		d.setType(Constants.FieldType.STRING);
//		d.setUpload(false);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("Remark");
		d.setType(Constants.FieldType.STRING);
//		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);

		return m;
	}

	/**
	 * 报告明细
	 */
	private static DBMainConfig getCallDetail() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_data_callReportDetail");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ProductId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("callReportId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT1");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT2");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT3");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT4");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT5");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT6");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT7");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT8");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT9");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT10");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("STR1");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR2");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR3");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR4");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR5");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR6");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR7");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR8");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR9");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR10");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("decimal1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 报告主表
	 */
	private static DBMainConfig getCallReport() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_data_callReport");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("onlyType");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("TemplateId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("errmsg");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ReportDate");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT1");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT2");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT3");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT4");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT5");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT6");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT7");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT8");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT9");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT10");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT11");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT12");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT13");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT14");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT15");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT16");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT17");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT18");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT20");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT19");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("STR1");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR2");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR3");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR4");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR5");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR6");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR7");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR8");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR9");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR10");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("decimal1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 计划表
	 */
	private static DBMainConfig getPlan() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Visit_Plan_Detail");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PlanId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("VisitTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("IsApproval");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Remark");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Analysis");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("Edate");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("Sdate");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("Type");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 参数数据字典
	 */
	private static DBMainConfig getDictionary() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Sys_Dictionary");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictId");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictType");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictClass");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("FirstLevel");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictValue");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * Product产品
	 */
	private static DBMainConfig getProduct() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Product");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverid");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ProductCode");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ProductName");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("FullName");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ShortName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("FullNameEn");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ShortNameEn");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Package");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Unit");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ChannelId");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("IsCompete");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("IsEnd");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("StandardPrice");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("WholesalePrice");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("AfterTaxPrice");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("BeforeTaxPrice");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("isgroup");
		d.setType(Constants.FieldType.INT);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("LevelId");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("isNew");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("brand");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("xilie");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("nodeid");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("IsSensitive");
		d.setType(Constants.FieldType.INT);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("remark");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		d.setQuery(false);
		m.setField(d);

		return m;
	}

	/**
	 * terrminal终端
	 */
	private static DBMainConfig getTerrminal() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Outlet_Main");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("OutletId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("OutletCode");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("OutletType");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("displaytype");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		
		

		d = new DBDetailConfig();
		d.setFieldName("FullName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ShortName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ChannelId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("OrgId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("RegionId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ChainStoreId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("facialdiscount");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("facediscount");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("milkdiscount");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("newdiscount");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("demiwarecount");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		

		d = new DBDetailConfig();
		d.setFieldName("Address");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("LogesticAddress");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ZIP");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Tel");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Fax");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DelDate");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("IsEnd");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("OutletLevel");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Attribute");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}
	
	private static DBMainConfig getEoc()
	{
		DBMainConfig m = null;
		DBDetailConfig d = null;
		
		m = new DBMainConfig();
		m.setTableName("T_Train_List");
		m.setKey("serverid");
		
		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("TrainId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("AttachmentName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("AttachmentType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("AttachmentURL");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("AtrType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		return m;
	}
	
	private static DBMainConfig getDIsplay() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_Display_Pic");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("DisplayTypeName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("URL");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Stime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("Sender");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("status");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	public static List<DBMainConfig> getTableList() {
		if (tableList == null)
			return getDBConfig();
		return tableList;
	}

	public static void setTableList(List<DBMainConfig> tableList) {
		DBConfig.tableList = tableList;
	}

}