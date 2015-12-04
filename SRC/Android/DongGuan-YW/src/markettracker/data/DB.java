package markettracker.data;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants.ControlType;
import markettracker.util.Constants.TableInfo;
import markettracker.util.Constants.DataBaseName;
import markettracker.util.Constants;
import markettracker.util.DBConfig;
import markettracker.util.Tool;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; 
import android.util.Base64;
import android.view.Gravity;
import android.widget.LinearLayout;

@SuppressLint("DefaultLocale")
public class DB {

	private static final String SERVERID = "ServerId";

	private SQLiteDatabase db;
	private static final int TRANCOUNT = 50;
	private Context context;

	public DB(Context context) {
		try {
			init(context);
		} finally {
			if (db != null)
				db.close();
		}
	}

	private void init(Context context) {
		initContext(context);
		initDB();
		setUpDb();
	}

	/**
	 * 保存设定计划数据
	 * @param report
	 * @return
	 */
	public synchronized long savePlan(SObject report) {
		long rowId = -1;
		try {
			execSingleSql("DELETE FROM t_data_callreportdetail where callReportId in (select key_id from t_data_callreport where templateid = '"+report.getField("templateid")+"' and reportdate = '"+report.getField("reportdate")+"')");
			execSingleSql("DELETE FROM t_data_callreport where key_id in (select key_id from t_data_callreport where templateid = '"+report.getField("templateid")+"' and reportdate = '"+report.getField("reportdate")+"')");
			
			openDatabase();
			beginTransaction();

			ContentValues values = new ContentValues();
			DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callreport");
			for (DBDetailConfig detail : dc.getFieldList()) {
				values.put(detail.getFieldName(),
						report.getField(detail.getFieldName()));
			}
			values.put("issubmit", 0);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());
			rowId = db.insert("t_data_callreport", null, values);

			createSetPlanReportDetail(report.getDetailfields(), rowId);
			setTransactionSuccessful();
		} finally {
			endTransaction();
			closeDatabase();
		}
		
		return rowId;
	}
	
	/**
	 * 保存设定计划详细数据
	 * @param list
	 * @param rptId
	 * @return
	 */
	private synchronized long createSetPlanReportDetail(FieldsList list, long rptId) {
		if (list == null || list.getList() == null)
			return -1;
		long rowId = -1;
		ContentValues values = null;
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callreportdetail");
		for (Fields data : list.getList()) {
			if(data.getStrValue("selected").equals("1")){
				values = new ContentValues();
				for (DBDetailConfig detail : dc.getFieldList()) {
					if (detail.getFieldName().equalsIgnoreCase("productid")){
						values.put("productid",data.getStrValue("serverid"));
					}
					else if (detail.getFieldName().equalsIgnoreCase("int1")){
						values.put("int1",data.getStrValue("outlettype"));
					}
					else if (detail.getFieldName().equalsIgnoreCase("callReportId")){
						values.put("callReportId", rptId);
					}
					else{
						values.put(detail.getFieldName(),data.getStrValue(detail.getFieldName()));
					}
				}
				
				values.put("UpDateTime", Tool.getCurrDateTime());
				values.put("InSertTime", Tool.getCurrDateTime());
				
				rowId = db.insert("t_data_callreportdetail", null, values);
			}
		}

		return rowId;
	}
	
	public synchronized long setPlan(SObject report) {
		if (report == null)
			return -1;
		long rowId = -1;
		try {
			execSingleSql("DELETE FROM t_visit_plan_detail where visittime='" + report.getCallDate() + "'");
			openDatabase();
			db.beginTransaction();
			ContentValues values = null;
			if (report.getDetailCount() > 0) {
				for (Fields data : report.getDetailfields().getList()) {
					values = new ContentValues();
					values.put("clientid", data.getStrValue("serverid"));
					values.put("visittime", report.getCallDate());
					values.put("issubmit", 0);
					if (data.getStrValue("selected").equals("1"))
						values.put("isdel", "0");
					else
						values.put("isdel", "1");

					values.put("clienttype", data.getStrValue("outlettype"));
					values.put("updatetime", Tool.getCurrDateTime());
					values.put("inserttime", Tool.getCurrDateTime());
					rowId = db.insert("t_visit_plan_detail", null, values);
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			closeDatabase();
		}
		return rowId;
	}

	public synchronized String addDate(String date, String days) {

		Cursor c = null;

		try {
			openDatabase();
			c = db.rawQuery("SELECT date('" + date + "','" + days + " days') as date", null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					date = getString(c, "date");
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return date;
	}

	private synchronized void initDB() {
		db = openOrCreateDatabase();
		db.setLockingEnabled(false);
	}

	public synchronized List<String> getTemplateIdList(String code, String key) {
		List<String> list = new ArrayList<String>();
		String templateid = "";
		
		String strSql = "select onlytype from t_data_callreport where reportdate='" + Tool.getCurrDate()
				+ "' and ClientId='" + code + "' and decimal10='" + key + "'";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					templateid = getString(c, "onlyType");
					list.add(templateid);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized List<Fields> getProductIdList() {
		List<Fields> list = new ArrayList<Fields>();
		Fields tempFields;
		String strSql = "select ClientId,issubmit from t_data_callreport where reportdate='" + Tool.getCurrDate()
				+ "' and onlyType=6001";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					tempFields = new Fields();
					tempFields.put("ClientId", getString(c, "ClientId"));
					tempFields.put("issubmit", getString(c, "issubmit"));
					// templateid = getString(c, "ClientId");
					list.add(tempFields);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	private synchronized SQLiteDatabase openOrCreateDatabase() {
		SQLiteDatabase db = null;
		// try {
		db = context.openOrCreateDatabase(DataBaseName.DATABASE_NAME, Context.MODE_PRIVATE, null);

		// } catch (SQLiteException ex) {
		// String err = ex.getMessage();
		// }

		return db;
	}

	private synchronized void setUpDb() {
		String strSql = "";
		List<String> listSql = new ArrayList<String>();
		List<DBMainConfig> list = DBConfig.getTableList();
		for (DBMainConfig dm : list) {
			strSql = "create table if not exists " + Tool.toLowerCase(dm.getTableName()) + "( " + TableInfo.TABLE_KEY
					+ " integer primary key not null ,";
			for (DBDetailConfig dc : dm.getFieldList()) {
				strSql += Tool.toLowerCase(dc.getFieldName()) + " " + dc.getType();
				if (!dc.isNull())
					strSql += " not null ";
				if (dc.isUnique())
					strSql += " UNIQUE ";
				strSql += ",";
			}
			strSql += "issubmit integer default -1, " + "isdel integer default 0, "

			+ "updatetime timestamp default current_timestamp, " + "inserttime timestamp default current_timestamp);";
			listSql.add(strSql);
		}
		execSqlList(listSql);
	}

	public synchronized int getUnSubmitRptCount() {
		int count = 0;
		String strSql = "select count(*) as count from t_data_callreport where issubmit=0 and ReportDate='"
				+ Tool.getCurrDate() + "'";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					count = c.getInt(c.getColumnIndex("count"));

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return count;
	}

	/**
	 * 查询未下载的信息公告和未读的事项提醒
	 * @param type	2=信息公告 3=事项提醒
	 * @return
	 */
	public synchronized int getUnReadMsgCount(int type) {
		int count = 0;
		
		String strSql = "";
		if(type == 2){	//信息公告
			strSql = "select count(issubmit) as count from t_train_list where isdel = 0 and issubmit <> 0 and str1 <> ''";
		}else if(type == 3){	//事项提醒
			strSql = "select sum(count) as count from (select count(*) as count from t_message_detail where status!='已读' union "
					+ "select count(*) as count from t_psq_payout where clienttype=0 and issubmit !=1)";
		}
		

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					count = c.getInt(c.getColumnIndex("count"));

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return count;
	}

	// private SQLiteDatabase getDB() {
	// if (mDB == null) {
	// mDB = openOrCreateDatabase();
	// mDB.setLockingEnabled(false);
	// }
	// return mDB;
	// }

	private synchronized void openDatabase() {
		if (!isOpen())
			db = openOrCreateDatabase();
	}

	private synchronized void closeDatabase() {
		if (isOpen())
			db.close();
	}

	private synchronized void beginTransaction() {
		try {
			if (inTransaction())
				endTransaction();
			db.beginTransaction();
		} catch (IllegalStateException e) {
		}
	}

	private boolean isOpen() {
		return db.isOpen();
	}

	private boolean inTransaction() {
		return db.inTransaction();
	}

	private synchronized void setTransactionSuccessful() {
		if (inTransaction())
			db.setTransactionSuccessful();
	}

	private synchronized void endTransaction() {
		if (inTransaction())
			db.endTransaction();
	}

	// public synchronized void deleteAllTable() {
	// String[] table = new String[] { "t_Product", "t_Account",
	// "t_VisitPlan", "t_Dictionary", "t_report_main",
	// "t_report_detail", "t_report_photo", "t_Message", "t_Attchment" };
	// deleteTable(table);
	//
	// }

	// public synchronized void deleteTable(String[] table) {
	// try {
	// openDatabase();
	// for (String name : table)
	// delete(name);
	// } finally {
	// closeDatabase();
	// }
	// }

	// public synchronized void delete(String tableName) {
	// try {
	// openDatabase();
	// getDB().delete(tableName, null, null);
	// } finally {
	// closeDatabase();
	// }
	// }

	private String getSObjectValue(SObject hm, String key) {
		return hm.getField(key);
	}

	private String replaceSql(SObject object, DBMainConfig dc) {
		String replaceSql = "REPLACE into ";
		replaceSql += dc.getTableName() + "(";
		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isDownLoad())
				replaceSql += Tool.toLowerCase(detail.getFieldName()) + ",";
		}
		replaceSql = Tool.getSubString(replaceSql) + ") VALUES (";

		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isDownLoad()) {
				if (detail.getFieldName().equalsIgnoreCase(dc.getKey()))
					replaceSql += "'" + getSObjectValue(object, SERVERID) + "' ,";
				else
					replaceSql += "'" + getSObjectValue(object, detail.getFieldName()) + "' ,";
			}

		}
		replaceSql = Tool.getSubString(replaceSql) + ");";
		return replaceSql;
	}

	private String deletSql(SObject object, DBMainConfig dc) {
		String deleteSql = "DELETE FROM " + dc.getTableName() + " where " + Tool.toLowerCase(dc.getKey()) + "='"
				+ getSObjectValue(object, SERVERID) + "'";
		return deleteSql;
	}

	public synchronized long upsertData(List<SObject> list, String type) throws Exception {
		long rowId = -1;
		if (type.equalsIgnoreCase("T_Message_Detail")) {
			rowId = upsertMsg(list);
		} else {
			DBMainConfig dc = DBConfig.getDBMainConfig(type);
			if (dc == null)
				return -1;
			List<String> listSql = new ArrayList<String>();

			for (SObject object : list) {
				if (object != null) {
					if (Tool.isDel(getSObjectValue(object, "IsDel")))
						listSql.add(deletSql(object, dc));
					else
						listSql.add(replaceSql(object, dc));
				}
			}
			// for (String sql : listSql)
			// System.out.println(sql);
			execSqlList(listSql);
			rowId = 1;
		}
		return rowId;
	}

	public synchronized String getDate(String str) {

		String date = Tool.getCurrDate();
		Cursor c = null;

		try {
			openDatabase();
			c = db.rawQuery(str, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					date = getString(c, "date");
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return date;
	}

	public synchronized FieldsList getFieldsList(int type, Fields data) {
		Fields fields;
		FieldsList list = new FieldsList();
		String strSql = "";
		if (type == 0)
			strSql = "select t.serverid as serverid, t.fullname as terminalname from (select * from  T_Visit_Plan_Detail where visittime ='"
					+ data.getStrValue("date")
					+ "' and isdel=0 and issubmit!=2)plan left join t_outlet_main t on plan.clientid=t.serverid order by t.outlettype desc";
		else if (type == 2)
			strSql = "select serverid ,title,content, strftime('%Y-%m-%d',stime) as stime,sender,status from t_message_detail  order by status desc,stime desc limit 50 ";
		else if (type == 3)
			strSql = "select * from t_data_terminal where terminalname like '%" + data.getStrValue("code")
					+ "%' or terminalcode like '%" + data.getStrValue("code")
					+ "%' and serverid not in (select clientid from t_data_plan) order by terminalname limit 20";
		else if (type == 4) {
			strSql = "select * from t_data_terminal where serverid not in (select clientid from t_data_plan)";
		} else if (type == 5) {
			strSql = "SELECT t.* ,case when p.key_id is not null then 1 else 0 end as selected FROM t_outlet_main t left join (select * from  T_Visit_Plan_Detail  where visittime ='"
					+ data.getStrValue("date")
					+ "' and isdel=0 and issubmit!=2) p on t.serverid=p.clientid order by selected desc,t.outlettype desc,cast(outletid as int) limit 50";
		}

		else if (type == 66) {
			if (data.getStrValue("searchClient") == null || data.getStrValue("searchClient").equals("")) {
				strSql = "select * from t_outlet_main where outlettype = 1 order by outletid";
			} else {
				strSql = "select * from t_outlet_main where outlettype == 1 and (fullname like '%"
						+ data.getStrValue("searchClient") + "%' or outletcode like '%"
						+ data.getStrValue("searchClient") + "%') order by outletid";
			}
		}
		
		else if (type == 313) // 促销活动(系统)
		{
			strSql = "select isfeedback,str5 as str1 , str3 as str2, str4 as str3, key_id,serverid,promotionobject,promotionsource,str6 as int2, clientid ,empid, issubmit, isdel, updatetime,inserttime from t_promotion_plan where promotionobject = '"+data.getStrValue("promotionobject")+"' and clientid = '"+data.getStrValue("clientid")+"' and empid = '"+Rms.getUserId(context)+"' order by promotionsource";
		}

		else if (type == 3133) // 促销活动（新增）
		{
			strSql = "select * from t_data_callreport where templateid = '"+data.getStrValue("templateid")+"' and clientid = '"+data.getStrValue("clientid")+"' and reportdate = '"+Tool.getCurrDate()+"'";
		}
		
		else if (type == 414) //查促销方式  辅助促销活动反馈
		{
			strSql = "select * from t_sys_dictionary where dicttype = '"+data.getStrValue("dicttype")+"' and dictclass = '"+data.getStrValue("dictclass")+"'";
		}

		else if (type == 6) {
			String channelId = "1";
			if (Tool.getCurClient().getStrValue("channelid").equals("4"))
				channelId = "4";
			if (data.getStrValue("code").equals(""))
				strSql = "select * from  T_Product	 where channelid='" + channelId
						+ "'   order by substr(productname,1) ";
			else
				strSql = "select * from  T_Product	 where channelid='" + channelId + "' and levelid='"
						+ data.getStrValue("code") + "'  order by substr(productname,1) ";
		}

		else if (type == 100)
			strSql = "select p.* ,case when strftime('%Y-%m-%d',r.updatetime) is null then p.finishtime else strftime('%Y-%m-%d',r.updatetime) end as wcdate from (select * from  t_promotion where clientid='"
					+ Tool.getCurClient().getStrValue("serverid") + "' and startdate<='" + Tool.getCurrDate()
					+ "' and enddate >='" + Tool.getCurrDate() + "' and PromotionName like '%"
					+ data.getStrValue("code") + "%' and (finishtime is '' or finishtime is null or finishtime='"
					+ Tool.getCurrDate()
					+ "')) p left join (select * from t_data_callreport where onlytype=203 and ClientId='"
					+ Tool.getCurClient().getStrValue("serverid") + "') r on p.key_id=r.int15 order by wcdate ";
		else {
			strSql = "select * ,0 as selected from t_outlet_main where fullname like '%" + data.getStrValue("code")
					+ "%' or outletcode like '%" + data.getStrValue("code")
					+ "%' order by outlettype desc limit 20";
		}
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			while (c.moveToNext()) {
				fields = new Fields();
				for (int i = 0; i < c.getColumnCount(); i++) {
					fields.put(c.getColumnName(i), c.getString(i));
				}
				list.setFields(fields);
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;

	}

	public synchronized FieldsList getFieldsList(int type, String code) {
		Fields fields;
		FieldsList list = new FieldsList();
		String strSql = "";
		if (type == 0) {
			strSql = "SELECT o.displaytype as displaytype, o.facialdiscount as facialdiscount, o.fullname as name, o.address as address, o.outletcode as outletcode, o.OutletLevel as OutletLevel, case when  sum(c.issubmit is not null and c.issubmit is not -1) is 0 then '未拜访' else (sum(c.issubmit is 1)*100/sum(c.issubmit is not null and c.issubmit is not -1 ))||'%' end as status1, case when  sum(c.issubmit is not null and c.issubmit is not -1) is 0 then '未拜访' else  sum(c.issubmit is 1)||'/'||sum(c.issubmit is not null and c.issubmit is not -1 ) end as status,  o.outlettype as outlettype, o.serverid as serverid, o.outletcode||o.fullname  as fullname,o.facialdiscount as clienttype FROM (select * from t_visit_plan_detail where visittime like '%"
					+ Tool.getCurrDate()
					+ "%' OR visittime IS NULL)  vd left join (select * from t_data_callreport where ReportDate='"
					+ Tool.getCurrDate()
					+ "') c on c.clientid=vd.clientid left join t_outlet_main o on o.outletid=vd.clientid group by vd.clientid order by status1 ";
		} else if (type == 2) {
			
			strSql = "select serverid ,title,content, strftime('%Y-%m-%d',stime) as stime,sender,status from t_message_detail  order by status desc,stime desc limit 50 ";
//			strSql = "select 1 as type, serverid ,title,content, strftime('%Y-%m-%d',stime) as stime,sender,status from t_message_detail  "
//					+ " union  SELECT 2 as type, psq.serverid , psq.title ,'' as content , strftime('%Y-%m-%d',pay.updatetime) as stime ,'' as sender,case when pay.issubmit is '-1' then '未填' else '已填' end as status"
//					+ " from (select * FROM t_psq where psqid in (select psqid from t_psq_payout where clienttype=0 and clientid ='"
//					+ code + "')) psq left join (select * from t_psq_payout where clienttype=0 and clientid ='" + code
//					+ "') pay on psq.serverid =pay.psqid order by status desc,stime desc ";
		} else if (type == 12) {
			// "select serverid ,title,content, strftime('%Y-%m-%d',stime) as
			// stime,sender,status from t_message_detail order by status
			// desc,stime desc limit 50 ";
			strSql = "select * from t_message_detail   where status='未读' ";
		} else if (type == 3) {
			// strSql = "select psq.* , case when pay.issubmit is '-1' then '未填'
			// else '已填' end as status from (SELECT * FROM t_psq where psqid in
			// (select psqid from t_psq_payout where clienttype=1 and
			// clientid='"
			// + code + "')) psq left join (select * from t_psq_payout where
			// clienttype=1 and clientid='" + code
			// + "') pay on psq.serverid=pay.psqid order by status
			// desc,updatetime desc";
			strSql = "select  psq.* , case when  pay.issubmit  is '-1' then '未填' else '已填' end as status from (SELECT * FROM t_psq where psqid in (select psqid from t_psq_payout where clienttype=1)) psq left join (select * from t_psq_payout where clienttype=1) pay on psq.serverid=pay.psqid order by status desc,updatetime desc";
		} else if (type == 13) {
			strSql = "SELECT p.*,o.fullname as fullname FROM t_client_rlt_activitypromoter p left join t_outlet_Main o on p.clientid=o.serverid order by key_id desc";
		} else if (type == 14) {
			if (code.equals(""))
				strSql = "SELECT * FROM t_outlet_main where outlettype=1  and (str1 >0 or str2 >0)  order by  fullname ";
			// strSql =
			// "SELECT * FROM t_outlet_main where outlettype=1 and str1 >0 order
			// by fullname ";
			else
				strSql = "SELECT * FROM t_outlet_main where fullname like  '%" + code + "%' or outletcode like '%"
						+ code + "%' and  outlettype=1 and (str1 >0 or str2 >0)  order by  fullname ";// and
																										// str1
																										// >0
		} else if (type == 20) {
			strSql = "SELECT * FROM t_outlet_main where outlettype=1  order by  fullname ";
		} else if (type == 200) {
			strSql = "SELECT  call.key_id as key_id, call.str1 as date,call.str2 as date1,call.int1 ,dic.dictname as name FROM (select * from t_data_callreport where onlytype=51) call left join (select * from t_sys_dictionary where dicttype='12003') dic on call.int1= dic.dictclass  order by  reportdate ";
		} else if (type == 15) {
			// strSql = "select * from t_activity_main where clientid='" + code
			// + "' and stime<='" + Tool.getCurrDate() + "' and etime >='" +
			// Tool.getCurrDate() + "'";
			strSql = "select * from t_activity_main where clientid='" + code + "' ";
		}

		else if (type == 16) {
			if (code.equals(""))
				strSql = "SELECT * FROM t_outlet_main where outlettype=1 and serverid in (select clientid from t_activity_main)  order by  fullname ";
			else
				strSql = "SELECT * FROM t_outlet_main where fullname like  '%" + code + "%' or outletcode like '%"
						+ code
						+ "%' and  outlettype=1 and serverid in (select clientid from t_activity_main)  order by  fullname ";
		}

		else {
			if (code.equals(""))
				strSql = "SELECT * FROM t_outlet_main where serverid not in ( select clientid from t_visit_plan_detail  where visittime == '"
						+ Tool.getCurrDate() + "' or visittime is null) order by outlettype desc limit 20";
			else
				strSql = "SELECT * FROM t_outlet_main where (fullname like  '%" + code + "%' or outletcode like '%"
						+ code
						+ "%') and serverid not in ( select clientid from t_visit_plan_detail where visittime == '"
						+ Tool.getCurrDate() + "' or visittime is null) order by outlettype desc limit 20";
		}
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					fields = new Fields();
					for (int i = 0; i < c.getColumnCount(); i++) {
						fields.put(c.getColumnName(i), c.getString(i));
					}
					list.setFields(fields);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	private ControlType getContralType(String type) {
		if (type.equalsIgnoreCase("3"))
			return ControlType.TEXT;
		else if (type.equalsIgnoreCase("2"))
			return ControlType.SINGLECHOICE;
		else if (type.equalsIgnoreCase("1"))
			return ControlType.MULTICHOICE;
		else
			return ControlType.NONE;
	}

	public synchronized Template getTemplate(String type) {
		Template template = new Template();
		template.setType("-100");
		// template.setVersion("-100");
		String strSql = "select s.title as title, s.isphoto as isphoto, q.*  from (select * from t_psq_question where psqid='"
				+ type + "' ) q left join t_psq s where q.psqid=s.serverid";
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			Panal panal;
			UIItem item;
			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {

					panal = new Panal();

					template.setVersion(getString(c, "psqid"));
					template.setName(getString(c, "title"));
					template.setPhoto(getString(c, "isphoto").equals("1") ? true : false);

					panal.setCaption(getString(c, "title"));
					panal.setType(Constants.PanalType.PANEL);
					panal.setId(getString(c, "serverid"));

					panal.setPanalType(1);
					item = new UIItem();
					item.setCaption("");

					item.setDicId(getString(c, "serverid"));
					item.setControlType(getContralType(getString(c, "type")));
					if (getString(c, "type").equals("3"))
						item.setVerifytype("text");
					item.setDataKey("str3");
					item.setItemType(1);
					item.setTitleWidth((Tool.getScreenWidth() - 40));
					item.setShowLable(false);
					item.setWidth(Tool.getScreenWidth() / 3);
					item.setOrientation(LinearLayout.HORIZONTAL);
					item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
					panal.setItem(item);
					template.setPanal(panal);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return template;
	}

	private String getReportDetialSql1(String reportId, String clientid) {
		DBMainConfig dc = DBConfig.getDBMainConfig("t_outlet_main");
		DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
		if (dc == null || dc1 == null)
			return "";
		String reportDetialSql = "SELECT ";

		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isQuery()) {
				reportDetialSql += " p." + Tool.toLowerCase(detail.getFieldName()) + " as "
						+ Tool.toLowerCase(detail.getFieldName()) + " ,";
			}
		}
		for (DBDetailConfig detail : dc1.getFieldList()) {
			if (detail.isQuery())
				reportDetialSql += " d." + Tool.toLowerCase(detail.getFieldName()) + " as "
						+ Tool.toLowerCase(detail.getFieldName()) + " ,";
		}

		reportDetialSql = Tool.getSubString(reportDetialSql)
				+ " , case when r.issubmit is null then '' when r.issubmit=0 then '已保存' when r.issubmit=1 then '已提交' when r.issubmit=2 then '上传失败' end as str10 FROM (select * from  t_outlet_main where outlettype=1) p  left join (select * from t_data_callreportdetail where callReportId='"
				+ reportId
				+ "') d on p.serverid=d.productid left join (select * from t_data_callreport where onlyType='1' and reportdate='"
				+ Tool.getCurrDate() + "' and int8='" + clientid
				+ "' ) r on p.serverid=r.clientid order by p.ShortName ";
		return reportDetialSql;
	}

	private String getReportDetialSql(String reportId) {
		DBMainConfig dc = DBConfig.getDBMainConfig("T_Product");
		DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
		if (dc == null || dc1 == null)
			return "";
		String reportDetialSql = "SELECT ";

		for (DBDetailConfig detail : dc1.getFieldList()) {
			if (detail.isQuery())
				reportDetialSql += " d." + Tool.toLowerCase(detail.getFieldName()) + " as "
						+ Tool.toLowerCase(detail.getFieldName()) + " ,";
		}
		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isQuery()) {
				if (detail.getFieldName().equalsIgnoreCase("AfterTaxPrice"))
					reportDetialSql += "round(p.aftertaxprice*100,1) as int1 ,";
				else
					reportDetialSql += " p." + Tool.toLowerCase(detail.getFieldName()) + " as "
							+ Tool.toLowerCase(detail.getFieldName()) + " ,";
			}
		}

		reportDetialSql = Tool.getSubString(reportDetialSql)
				+ "FROM (select * from  T_Product where isSensitive=1) p  left join (select * from t_data_callreportdetail where callReportId='"
				+ reportId + "') d on p.serverid=d.productid  order by p.ShortName ";
		return reportDetialSql;
	}

	private String getReportDetialSql(String type, String reportId, String dictId, String clientCode) {
		String reportDetialSql = "";
		if (dictId == null || dictId.equals("")) {
			DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
			if (dc1 == null)
				return "";

			
			reportDetialSql = "SELECT p.* ,";

			for (DBDetailConfig detail : dc1.getFieldList()) {
				if (detail.isQuery()) {
					if(Rms.getCheckProduct(context)){	//辅助 分销管理、库存检查 筛选模块 
						if("22".equals(type)){	//库存检查
							if (detail.getFieldName().equalsIgnoreCase("int1")){
								reportDetialSql += " case when d.int1 is not null then null else d.int1 end as int1,";
							}else{
								reportDetialSql += " d." + Tool.toLowerCase(detail.getFieldName()) + " as "
										+ Tool.toLowerCase(detail.getFieldName()) + " ,";
							}
						}else{	//分销管理
							if (detail.getFieldName().equalsIgnoreCase("int1")){
								reportDetialSql += " case when d.int1 == '1' then null else d.int1 end as int1,";
							}else if (detail.getFieldName().equalsIgnoreCase("int2")){
								reportDetialSql += " case when d.int2 == '1' then null else d.int2 end as int2,";
							}else{
								reportDetialSql += " d." + Tool.toLowerCase(detail.getFieldName()) + " as "
										+ Tool.toLowerCase(detail.getFieldName()) + " ,";
							}
						}
					}else{
						reportDetialSql += " d." + Tool.toLowerCase(detail.getFieldName()) + " as "
								+ Tool.toLowerCase(detail.getFieldName()) + " ,";
					}
				}
			}
			reportDetialSql = reportDetialSql.substring(0, reportDetialSql.length() - 1);

			if("2".equals(type)){	//分销管理(纸品)
				reportDetialSql += " FROM (select * from  T_Product where iscompete=0 and isgroup='1' order by levelid) p  left join (select * from t_data_callreportdetail where callReportId='"
						+ reportId + "') d on p.serverid=d.productid order by int1,int2";
			}else if("12".equals(type)){	//分销管理(卫品)
				reportDetialSql += " FROM (select * from  T_Product where iscompete=0 and isgroup='2' order by levelid) p  left join (select * from t_data_callreportdetail where callReportId='"
						+ reportId + "') d on p.serverid=d.productid order by int1,int2";
			}else if("22".equals(type)){	//库存检查
				reportDetialSql += " FROM (select * from  T_Product where iscompete=0 order by levelid) p  left join (select * from t_data_callreportdetail where callReportId='"
						+ reportId + "') d on p.serverid=d.productid order by int1";
			}else{
				reportDetialSql += " FROM (select * from  T_Product where iscompete=0) p  left join (select * from t_data_callreportdetail where callReportId='"
						+ reportId + "') d on p.serverid=d.productid order by d.int1";
			}
			
			if(Rms.getCheckProduct(context)){	//关闭辅助
				Rms.setCheckProduct(context, false);
			}
		} else {
			DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
			if (dc1 == null)
				return "";

			String resultttt = getFH(clientCode, 1);
			reportDetialSql = "SELECT '0' as have ,";

			for (DBDetailConfig detail : dc1.getFieldList()) {
				if (detail.isQuery()) {
					// if (detail.getFieldName().equalsIgnoreCase("int2"))
					// reportDetialSql += " case when d.int2 is null then 2 else
					// d.int2 end as int2,";
					// else
					reportDetialSql += " d." + Tool.toLowerCase(detail.getFieldName()) + " as "
							+ Tool.toLowerCase(detail.getFieldName()) + " ,";
				}
			}

//			if("2".equals(type)){
//				reportDetialSql += "p.FirstLevel as FirstLevel, p.dictclass as serverid , p.dictname as productname , p.remark as remark "
//						+ "FROM (select * from  t_sys_dictionary where dicttype='" + dictId + "' " + resultttt
//						+ ") p  left join (select * from t_data_callreportdetail where callReportId='" + reportId
//						+ "') d on p.dictid=d.productid ";
//			}else{
				reportDetialSql += "p.FirstLevel as FirstLevel, p.dictclass as serverid , p.dictname as productname , p.remark as remark "
						+ "FROM (select * from  t_sys_dictionary where dicttype='" + dictId + "' " + resultttt
						+ ") p  left join (select * from t_data_callreportdetail where callReportId='" + reportId
						+ "') d on p.dictclass=d.productid ";
	
				reportDetialSql += " order by have desc, p.dictclass ";
//			}
			
		}
		return reportDetialSql;
	}
	
	private String getFH(String code, int type2) {
		String result = "";

		Cursor c = null;

		try {
			c = db.rawQuery("SELECT type FROM t_visit_plan_detail where clientid='"+ code + "'", null);
			if (c != null && c.getCount() > 0) {
				String type;
				c.moveToFirst();
				do {
					type = getString(c, "type");
				} while (c.moveToNext());

				if (!type.equals("")) {
					String[] typeList = type.split(",");
					if (type2 == 0)
						result += " and dictname in (";
					else
						result += " and dictname not in (";
					for (String type1 : typeList) {
						result += "'" + type1 + "' ,";
					}
					result = result.substring(0, result.length() - 1);
					result += ")";
				}
			}
		} finally {
			if (c != null)
				c.close();
		}

		return result;
	}


	public synchronized List<SObject> getSubmitObject() {
		List<SObject> list = new ArrayList<SObject>();
		//
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callReport");
		DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
		DBMainConfig dc2 = DBConfig.getDBMainConfig("t_data_callReportPhoto");
		Fields data;
		SObject rpt;
		String strSql = "SELECT " + TableInfo.TABLE_KEY + ", ";

		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isUpload()) {
				if (detail.isReplace())
					strSql += "replace (" + detail.getFieldName() + ",' ','') as "
							+ Tool.toLowerCase(detail.getFieldName()) + ",";
				else
					strSql += detail.getFieldName() + ",";
			}
		}
		strSql = strSql.substring(0, strSql.length() - 1);
		strSql += " FROM t_data_callreport where issubmit=0 and ReportDate >='" + Tool.getCurrDate() + "' limit 1";

		// strSql =
		// "SELECT *, replace (str1,' ','') as str1 FROM t_data_callreport where
		// issubmit=0 and ReportDate='"
		// + Tool.getCurrDate() + "' limit 1";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					rpt = new SObject();
					rpt.setType("report");
					// rpt.setField("opetype", "upsert");

					for (DBDetailConfig detail : dc.getFieldList()) {
						if (detail.isUpload())
							rpt.set(detail.getFieldName(), getString(c, detail.getFieldName()));
					}
					// rpt.set("ClientType", "1");
					rpt.setField("userid", Rms.getUserId(context));
					rpt.setField(TableInfo.TABLE_KEY, getString(c, TableInfo.TABLE_KEY));
					list.add(rpt);
				} while (c.moveToNext());
			}
			c.close();
			for (SObject object : list) {

				strSql = "SELECT ";
				for (DBDetailConfig detail : dc2.getFieldList()) {
					if (detail.isUpload()) {
						if (detail.isReplace())
							strSql += "replace (" + detail.getFieldName() + ",' ','') as "
									+ Tool.toLowerCase(detail.getFieldName()) + ",";
						else
							strSql += detail.getFieldName() + ",";
					}
				}
				strSql = strSql.substring(0, strSql.length() - 1);
				strSql += " FROM t_data_callreportphoto where callReportId='" + object.getField(TableInfo.TABLE_KEY)
						+ "'";

				// strSql =
				// "SELECT * FROM t_data_callreportphoto where callReportId='"
				// + object.getField(TableInfo.TABLE_KEY) + "'";
				c = db.rawQuery(strSql, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						data = new Fields();
						for (DBDetailConfig detail : dc2.getFieldList()) {
							if (detail.isUpload()) {
								if (detail.getType().equals(Constants.FieldType.BLOB))
									data.Set(detail.getFieldName(), getByteString(c, detail.getFieldName()));
								else
									data.Set(detail.getFieldName(), getString(c, detail.getFieldName()));
							}
						}
						object.setAttfield(data);
					} while (c.moveToNext());
				}
				c.close();

				strSql = "SELECT ";

				for (DBDetailConfig detail : dc1.getFieldList()) {
					if (detail.isUpload()) {
						if (detail.isReplace())
							strSql += "replace (" + detail.getFieldName() + ",' ','') as "
									+ Tool.toLowerCase(detail.getFieldName()) + ",";
						else
							strSql += detail.getFieldName() + ",";
					}
				}
				strSql = strSql.substring(0, strSql.length() - 1);
				strSql += " FROM t_data_callreportdetail where callReportId='" + object.getField(TableInfo.TABLE_KEY)
						+ "'";
				// strSql =
				// "SELECT * FROM t_data_callreportdetail where callReportId='"
				// + object.getField(TableInfo.TABLE_KEY) + "'";
				c = db.rawQuery(strSql, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						data = new Fields();
						for (DBDetailConfig detail : dc1.getFieldList()) {
							if (detail.isUpload())
								data.Set(detail.getFieldName(), getString(c, detail.getFieldName()));
						}

						// for (int i = 0; i < c.getColumnCount(); i++) {
						// data.put(c.getColumnName(i), c.getString(i));
						// }
						object.setDetailfield(data);

					} while (c.moveToNext());
				}
				c.close();
			}

		}
		// catch (Exception e) {
		// // TODO: handle exception
		// String sssString=e.getMessage();
		// String sssString1=e.getMessage();
		// // Log.println(priority, tag, msg)
		//
		// }
		finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized List<SObject> getReadMsgId() {
		List<SObject> list = new ArrayList<SObject>();
		String strSql = "select serverid from t_message_detail  where issubmit=0 ";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				SObject data;

				c.moveToFirst();
				do {
					data = new SObject();
					data.setField("userid", Rms.getUserId(context));
					data.setType("msg");
					data.setField("MsgId", getString(c, "serverid"));
					list.add(data);

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized SObject getDPReport(Template temp, String promotionid) {
		SObject rpt = new SObject(temp);
		String strSql = "SELECT * FROM T_Client_Rlt_ActivityPromoter where PromoterId='" + promotionid + "' ";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				Fields data;
				c.moveToFirst();
				// do
				// {
				// for (int i = 0; i < c.getColumnCount(); i++)
				// {
				// rpt.setField(c.getColumnName(i), c.getString(i));
				// }
				// }
				rpt.setSaved(true);
				rpt.setField("int1", getString(c, "ClientId"));
				rpt.setField("str10", getString(c, "PromoterId"));
				rpt.setField("str2", getString(c, "Mobile"));
				rpt.setField("str1", getString(c, "PromoterName"));
				rpt.setField("int2", getString(c, "WorkDay"));

				rpt.setField("int3", getString(c, "str2"));

				rpt.setField("str5", getString(c, "str1"));
				rpt.setField(TableInfo.TABLE_KEY, getString(c, TableInfo.TABLE_KEY));

				while (c.moveToNext())
					;

				c.close();

				if (!rpt.getField(TableInfo.TABLE_KEY).equals("")) {
					strSql = "SELECT * FROM t_data_DPPhoto where callReportId='" + rpt.getField(TableInfo.TABLE_KEY)
							+ "'";
					c = db.rawQuery(strSql, null);
					if (c != null && c.getCount() > 0) {
						c.moveToFirst();
						do {
							data = new Fields();
							for (int i = 0; i < c.getColumnCount(); i++) {
								if (c.getColumnName(i).equalsIgnoreCase("Photo"))
									data.put(c.getColumnName(i), c.getBlob(i));
								else
									data.put(c.getColumnName(i), c.getString(i));
							}
							rpt.setAttfield(data);
						} while (c.moveToNext());
					}
				}
			}
			c.close();

			rpt.setField("onlyType", temp.getOnlyType() + "");
			rpt.setTemplateId(temp.getType());
			rpt.setTerminalCode("-1");
			// if (rpt.getCallDate().equals(""))
			rpt.setCallDate(Tool.getCurrDate());

		}
		// catch (Exception ex)
		// {
		// String sss = ex.getMessage();
		// }
		finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return rpt;
	}

	public synchronized SObject getReport(Template temp, String code, int type, String key) {
		SObject rpt = new SObject(temp);
		String strSql = "";
		if (type == 1) {
			strSql = "SELECT * FROM t_data_callreport where onlyType='" + temp.getOnlyType() + "' and templateid = '"+temp.getType()+"' and ReportDate='"
					+ Tool.getCurrDate() + "' and clientId='" + code + "'";
		}

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				rpt.setSaved(true);
				Fields data;
				c.moveToFirst();
				do {
					for (int i = 0; i < c.getColumnCount(); i++) {
						rpt.setField(c.getColumnName(i), c.getString(i));
					}
				} while (c.moveToNext());
				c.close();

				if (!rpt.getField(TableInfo.TABLE_KEY).equals("")
						&& rpt.getField("ReportDate").equals(Tool.getCurrDate())) {
					strSql = "SELECT * FROM t_data_callreportphoto where callReportId='"
							+ rpt.getField(TableInfo.TABLE_KEY) + "'";
					c = db.rawQuery(strSql, null);
					if (c != null && c.getCount() > 0) {
						c.moveToFirst();
						do {
							data = new Fields();
							for (int i = 0; i < c.getColumnCount(); i++) {
								if (c.getColumnName(i).equalsIgnoreCase("Photo"))
									data.put(c.getColumnName(i), c.getBlob(i));
								else
									data.put(c.getColumnName(i), c.getString(i));
							}
							rpt.setAttfield(data);
						} while (c.moveToNext());
					}
				}
			}
			c.close();

			if (temp.haveTable() || temp.haveDetail()) {
				
				strSql = getReportDetialSql(temp.getType(), rpt.getField(TableInfo.TABLE_KEY), "", code);
				
				c = db.rawQuery(strSql, null);

				if (c != null && c.getCount() > 0) {
					Fields data;

					c.moveToFirst();
					do {
						data = new Fields();
						for (int i = 0; i < c.getColumnCount(); i++) {
							data.put(c.getColumnName(i), c.getString(i));
						}

						rpt.setDetailfield(data);

					} while (c.moveToNext());
				}
			}

			rpt.setField("onlyType", temp.getOnlyType() + "");
			rpt.setTemplateId(temp.getType());
			rpt.setTerminalCode(code);
			rpt.setCallDate(Tool.getCurrDate());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return rpt;
	}

	public synchronized SObject getSurveyRpt(Template temp, String code) {
		SObject rpt = new SObject(temp);
		String strSql = "select * from t_data_callreport where templateid=-100 and clientid='" + code
				+ "' and reportdate ='" + Tool.getCurrDate() + "' and str1='" + temp.getVersion() + "'";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				Fields data;
				c.moveToFirst();
				do {
					for (int i = 0; i < c.getColumnCount(); i++) {
						rpt.setField(c.getColumnName(i), c.getString(i));
					}
				} while (c.moveToNext());
				c.close();

				if (!rpt.getField(TableInfo.TABLE_KEY).equals("")
						&& rpt.getField("ReportDate").equals(Tool.getCurrDate())) {
					strSql = "SELECT * FROM t_data_callreportphoto where callReportId='"
							+ rpt.getField(TableInfo.TABLE_KEY) + "'";
					c = db.rawQuery(strSql, null);
					if (c != null && c.getCount() > 0) {
						c.moveToFirst();
						do {
							data = new Fields();
							for (int i = 0; i < c.getColumnCount(); i++) {
								if (c.getColumnName(i).equalsIgnoreCase("Photo"))
									data.put(c.getColumnName(i), c.getBlob(i));
								else
									data.put(c.getColumnName(i), c.getString(i));
							}
							rpt.setAttfield(data);
						} while (c.moveToNext());
					}
				}

				c.close();
				// 1 if (temp.haveTable()) {
				strSql = "select * from t_data_callReportDetail where callReportId='"
						+ rpt.getField(TableInfo.TABLE_KEY) + "'";
				c = db.rawQuery(strSql, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						data = new Fields();
						for (int i = 0; i < c.getColumnCount(); i++) {
							data.put(c.getColumnName(i), c.getString(i));
						}

						rpt.setDetailfield(data);

					} while (c.moveToNext());
				}
				// }

			} else {
				Fields data = null;
				for (Panal panal : temp.getPanalList()) {
					data = new Fields();
					data.put("str1", temp.getVersion());
					data.put("str2", panal.getId());

					data.put("str4", code);
					// data.put("str1", template.getVersion());
					rpt.setDetailfield(data);
				}

				rpt.setField("onlyType", "-100");

				rpt.setField("str1", temp.getVersion());
				rpt.setTemplateId("-100");
				rpt.setTerminalCode(code);
				if (code.equals("-1"))
					// rpt.setField("ClientType", "3");
					rpt.setField("ClientType", "0");
				else
					// rpt.setField("ClientType", "4");
					rpt.setField("ClientType", "1");
				rpt.setCallDate(Tool.getCurrDate());

			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return rpt;
	}

	public synchronized long saveDP(SObject rpt) {
		long rowId = -1;
		try {
			// if (rpt.getKeyId() > 0)
			// {
			// execSingleSql("DELETE FROM t_data_callReportPhoto where
			// callReportId='"
			// + rpt.getField(TableInfo.TABLE_KEY) + "'");
			// execSingleSql("DELETE FROM t_data_callreportdetail where
			// callReportId='"
			// + rpt.getField(TableInfo.TABLE_KEY) + "'");
			// execSingleSql("DELETE FROM t_data_callreport where key_id='" +
			// rpt.getField(TableInfo.TABLE_KEY) + "'");
			// }
			openDatabase();
			beginTransaction();

			ContentValues values = new ContentValues();
			// DBMainConfig dc =
			// DBConfig.getDBMainConfig("T_Client_Rlt_ActivityPromoter");

			values.put("ClientId", rpt.getField("int1"));
			values.put("PromoterId", rpt.getField("id"));
			values.put("Mobile", rpt.getField("str2"));
			values.put("PromoterName", rpt.getField("str1"));
			values.put("WorkDay", rpt.getField("int2"));

			values.put("str2", rpt.getField("int3"));
			if (rpt.getField("str5").equals(""))
				values.put("str1", "N");
			else
				values.put("str1", rpt.getField("str5"));

			values.put("serverid", rpt.getField("id"));
			values.put("issubmit", 1);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());
			rowId = db.insert("T_Client_Rlt_ActivityPromoter", null, values);

			createDPPhoto(rpt.getAttfields(), rowId);

			setTransactionSuccessful();

		} finally {
			endTransaction();
			closeDatabase();
		}
		return rowId;
	}

	private synchronized long createDPPhoto(FieldsList list, long rptId) {
		if (list == null || list.getList() == null)
			return -1;
		long rowId = -1;
		ContentValues values = null;
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_DPPhoto");
		for (Fields data : list.getList()) {
			values = new ContentValues();

			for (DBDetailConfig detail : dc.getFieldList()) {
				if (detail.getFieldName().equalsIgnoreCase("callReportId"))
					values.put(detail.getFieldName(), rptId);
				else if (detail.getType() == Constants.FieldType.BLOB)
					values.put(detail.getFieldName(), data.getBValue(detail.getFieldName()));
				else
					values.put(detail.getFieldName(), data.getStrValue(detail.getFieldName()));
			}
			// values.put("callreportid", rptId);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());

			rowId = db.insert("t_data_DPPhoto", null, values);
		}

		return rowId;
	}

	public synchronized long saveReport(SObject rpt) {
		long rowId = -1;
		try {
			if (rpt.getKeyId() > 0) {
				execSingleSql("DELETE FROM t_data_callReportPhoto where callReportId='"
						+ rpt.getField(TableInfo.TABLE_KEY) + "'");
				execSingleSql("DELETE FROM t_data_callreportdetail where callReportId='"
						+ rpt.getField(TableInfo.TABLE_KEY) + "'");
				execSingleSql("DELETE FROM t_data_callreport where key_id='" + rpt.getField(TableInfo.TABLE_KEY) + "'");
			}
			
			if("4".equals(rpt.getTemplateId()) || "14".equals(rpt.getTemplateId())){	//促销活动反馈
				execSingleSql("update t_data_callreport set isfeedback = '1' where (templateid = '3' or templateid = '13') and serverid = '"+rpt.getSValue("onlytype")+"'");
				execSingleSql("update t_promotion_plan set isfeedback = '1', updatetime = '"+Tool.getCurrDateTime()+"' where serverid = '"+rpt.getSValue("int1")+"'");
			}

			if("3".equals(rpt.getTemplateId()) || "13".equals(rpt.getTemplateId())){	//促销活动
//				execSingleSql("update t_promotion_plan set str5 = '"+rpt.getSValue("str1")+"', str3 = '"+rpt.getSValue("str2")+"', str4 = '"+rpt.getSValue("str3")+"', str6 ='"+rpt.getSValue("int2")+"', updatetime = '"+Tool.getCurrDateTime()+"' where serverid = '"+rpt.getSValue("serverid")+"'");
				execSingleSql("delete from t_promotion_plan where serverid = '"+rpt.getSValue("serverid")+"'");
			}
			
			openDatabase();
			beginTransaction();

			ContentValues values = new ContentValues();
			DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callreport");
			for (DBDetailConfig detail : dc.getFieldList()) {
				values.put(detail.getFieldName(), rpt.getField(detail.getFieldName()));
			}

			
			values.put("issubmit", 0);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());
			rowId = db.insert("t_data_callreport", null, values);

			createReportDetail(rpt.getDetailfields(), rowId);
			createPhoto(rpt.getAttfields(), rowId);

			setTransactionSuccessful();

		} finally {
			endTransaction();
			closeDatabase();
		}
		return rowId;
	}

	private synchronized long createReportDetail(FieldsList list, long rptId) {
		if (list == null || list.getList() == null)
			return -1;
		long rowId = -1;
		ContentValues values = null;
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callreportdetail");
		for (Fields data : list.getList()) {
			values = new ContentValues();
			for (DBDetailConfig detail : dc.getFieldList()) {
				if (detail.getFieldName().equalsIgnoreCase("productid"))
					values.put(detail.getFieldName(), data.getStrValue("serverid"));
				else if (detail.getFieldName().equalsIgnoreCase("callReportId"))
					values.put(detail.getFieldName(), rptId);
				else
					values.put(detail.getFieldName(), data.getStrValue(detail.getFieldName()));
			}

			// values.put("callReportId", rptId);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());
			rowId = db.insert("t_data_callreportdetail", null, values);
		}

		return rowId;
	}

	private synchronized long createPhoto(FieldsList list, long rptId) {
		if (list == null || list.getList() == null)
			return -1;
		long rowId = -1;
		ContentValues values = null;
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callReportPhoto");
		for (Fields data : list.getList()) {
			values = new ContentValues();

			for (DBDetailConfig detail : dc.getFieldList()) {
				if (detail.getFieldName().equalsIgnoreCase("callReportId"))
					values.put(detail.getFieldName(), rptId);
				else if (detail.getType() == Constants.FieldType.BLOB)
					values.put(detail.getFieldName(), data.getBValue(detail.getFieldName()));
				else
					values.put(detail.getFieldName(), data.getStrValue(detail.getFieldName()));
			}
			// values.put("callreportid", rptId);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());

			rowId = db.insert("t_data_callReportPhoto", null, values);
		}

		return rowId;
	}

	public synchronized long upsertMsg(List<SObject> list) {
		if (list == null)
			return -1;
		long rowId = -1;
		try {
			ContentValues values = null;
			openDatabase();
			beginTransaction();
			for (SObject object : list) {
				if (object != null) {
					values = new ContentValues();

					values.put("ServerId", object.getField("ServerId"));
					values.put("Title", object.getField("Title"));
					values.put("Content", object.getField("Content"));
					values.put("Stime", object.getField("Stime"));
					values.put("Sender", object.getField("Sender"));
					values.put("UpDateTime", Tool.getCurrDateTime());

					rowId = db.update("T_Message_Detail", values, "ServerId='" + object.getField("ServerId") + "'",
							null);
					if (rowId <= 0) {
						values.put("status", "未读");
						rowId = db.insert("T_Message_Detail", null, values);
					}
				}
			}
			setTransactionSuccessful();
		} finally {
			endTransaction();
			closeDatabase();
		}
		return rowId;
	}

	// private boolean str2boolean(String result) {
	// if (result.equals("on"))
	// return true;
	// else
	// return false;
	// }

	// private ControlType getContralType(String type) {
	// if (type.equalsIgnoreCase("text"))
	// return ControlType.TEXT;
	// else if (type.equalsIgnoreCase("combox"))
	// return ControlType.SINGLECHOICE;
	// else if (type.equalsIgnoreCase("checkbox"))
	// return ControlType.MULTICHOICE;
	// else
	// return ControlType.NONE;
	// }

	public synchronized List<DicData> getDicList(String dictCode, String linkdictId) {
		DicData data = null;
		List<DicData> items = new ArrayList<DicData>();
		String strSql = "";
		if (linkdictId.equals("")) {
			if (dictCode.equals("-10"))
				strSql = "SELECT serverid as dictclass , fullname as dictname  FROM t_product where isSensitive='2'";
			else
				strSql = "select * from t_sys_dictionary where dicttype='" + dictCode + "' and dictclass>0";
		} else {
			strSql = "SELECT dictname,dictvalues FROM t_Dictionary where dictmainid='" + dictCode + "' and linkvalues='"
					+ linkdictId + "'";
		}

		if (dictCode.equals("-100")) {
			strSql = "select serverid as dictclass,productname as dictname from t_product where iscompete=1";
		}

		if (dictCode.equals("222")) {	//纸品
			strSql = "select levelid as dictclass,levelname as dictname from t_product where isgroup = '1' group by levelid,levelname";
		}

		if (dictCode.equals("122")) {	//卫品
			strSql = "select levelid as dictclass,levelname as dictname from t_product where isgroup = '2' group by levelid,levelname";
		}
		
		if (dictCode.equals("322")) {	//库存检查
			strSql = "select levelid as dictclass,levelname as dictname from t_product group by levelid,levelname";
		}
		
		if(dictCode.equals("222") || dictCode.equals("122") || dictCode.equals("322")){	//纸品或者卫品,库存检查
			data = new DicData();
			data.setItemname("全部");
			data.setValue("all");
			items.add(data);
		}
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);

			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {
					data = new DicData();
					data.setValue(getString(c, "dictclass"));
					data.setItemname(getString(c, "dictname"));
					data.setCode(getString(c, "dictid"));
					items.add(data);
				} while (c.moveToNext());
			} else {
				data = new DicData();
				data.setItemname("");
				data.setValue("");
				items.add(data);
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		
		return items;
	}

	public synchronized List<DicData> getAnswerList(String questionId) {
		DicData data = null;
		List<DicData> items = new ArrayList<DicData>();
		String strSql = "SELECT * FROM t_psq_options where questionid='" + questionId + "'";
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);

			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {
					data = new DicData();
					data.setValue(getString(c, "serverid"));
					data.setItemname(getString(c, "value"));

					items.add(data);
				} while (c.moveToNext());
			} else {
				data = new DicData();
				data.setItemname("");
				data.setValue("");
				items.add(data);
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return items;
	}

	public synchronized void execSqlList(List<String> sqlList) {
		try {
			openDatabase();
			if (sqlList.size() > TRANCOUNT)
				beginTransaction();
			for (String sql : sqlList)
				execSql(sql);
			if (sqlList.size() > TRANCOUNT)
				setTransactionSuccessful();

		} finally {
			if (sqlList.size() > TRANCOUNT)
				endTransaction();
			closeDatabase();
		}
	}

	private synchronized void execSql(String sql) {
		db.execSQL(sql);
	}

	public synchronized void execSingleSql(String sql) {
		try {
			openDatabase();
			execSql(sql);
		} finally {
			closeDatabase();
		}
	}

	private String getString(Cursor c, String columnName) {
		int index = -1;
		try {
			index = c.getColumnIndex((columnName.toLowerCase()));
		} catch (Exception ex) {
			index = -1;
		}
		if (index == -1)
			return "";
		else
			return getString(c, index);
	}

	private String getByteString(Cursor c, String columnName) {
		int index = -1;
		try {
			index = c.getColumnIndex((columnName.toLowerCase()));
		} catch (Exception ex) {
			index = -1;
		}
		if (index == -1)
			return "";
		else
			return Base64.encodeToString(getByte(c, index), Base64.DEFAULT);
	}

	// private int getInt(Cursor c, String columnName) {
	// int index = -1;
	// try {
	// index = c.getColumnIndex((columnName.toLowerCase()));
	// } catch (Exception ex) {
	// index = -1;
	// }
	// if (index == -1)
	// return 0;
	// else
	// return getInt(c, index);
	// }

	// private int getInt(Cursor c, int index) {
	// return c.getString(index) == null ? 0 : c.getInt(index);
	// }

	private String getString(Cursor c, int index) {
		return c.getString(index) == null ? "" : c.getString(index);
	}

	private byte[] getByte(Cursor c, int index) {
		// byte[] bytes=c.getBlob(index);
		// Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
		// bytes.length);
		// return Tool.Bitmap2Bytes(bitmap);
		return c.getBlob(index);
	}

	public Context getContext() {
		return context;
	}

	private void initContext(Context context) {
		this.context = context;
	}

	public synchronized FieldsList getReportList(Fields condition) {
		FieldsList list = new FieldsList();
		String strSql = "";
		strSql = "SELECT *, strftime('%H:%M:%S',updatetime) as updatetime ,case when issubmit is 0 then '已保存'  when issubmit is 1 then '已上传'  when issubmit is 2 then '上传失败' end as issubmit, '' as dictname FROM t_data_callreport where onlyType='"
				+ condition.getStrValue("onlytype") + "' and ReportDate='" + Tool.getCurrDate() + "' and clientId='"
				+ condition.getStrValue("clientid") + "' order by issubmit desc,updatetime desc ";

		Cursor c = null;
		Fields reslut;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {
					reslut = new Fields();
					for (int i = 0; i < c.getColumnCount(); i++) {
						reslut.put(c.getColumnName(i), c.getString(i));
					}
					list.setFields(reslut);

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	/**
	 * 电子资料
	 * 
	 * @return
	 */
	public synchronized List<FieldsList> getFieldsListList() {
		FieldsList fieldsList = null;
		Fields fields;
		List<FieldsList> list = new ArrayList<FieldsList>();
		String strSql = "";

		strSql = "select c.count ,case when t.issubmit=0 then '已下载' else '未下载' end as status, t.* from T_Train_List t LEFT JOIN (select str1,count(str1) as count from t_train_list where issubmit<>'0' group by str1) c on t.str1 = c.str1 where t.isdel=0 and t.str1 <> '' order by t.str2";
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				String oleName = "", newName = "";

				c.moveToFirst();
				do {
					newName = getString(c, "str1");

					// if (!newName.equals(oleName)) {
					fieldsList = new FieldsList();
					list.add(fieldsList);
					// }

					fields = new Fields();
					for (int i = 0; i < c.getColumnCount(); i++) {
						fields.put(c.getColumnName(i), c.getString(i));
					}
					fieldsList.setFields(fields);

					oleName = newName;
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

}
